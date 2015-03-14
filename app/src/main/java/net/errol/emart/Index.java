package net.errol.emart;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import net.errol.emart.core.Transaction;
import net.errol.emart.lib.ImageListAdapter;
import net.errol.emart.lib.ImageListModel;
import net.errol.emart.lib.JSONParser;
import net.errol.emart.tabsadapter.TabsPagerAdapter;
import net.errol.emart.tasks.RetrieveItemsTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Index extends ActionBarActivity implements ActionBar.TabListener {

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    private int currentTab = 0;

    private int[] productIDs;
    private ArrayList<String> purchasedProducts = new ArrayList<>();
    private ArrayList<Double> purchasePrices = new ArrayList<>();
    private ArrayList<Integer> purchasedProductIDs = new ArrayList<>();
    private ArrayList<Integer> purchasedQuantities = new ArrayList<>();

    private int ADD_CART=0;

    private String[] tabNames = {"All Products", "Categories", "My Cart"};

    private boolean hasLoaded = false;

    class RetrieveItemsTask extends AsyncTask<String, String, String> {

        ArrayList<ImageListModel> data = new ArrayList<>();
        //Product Indices
        private final String PRODUCT_ID = "ProductID";
        private final String CATEGORY = "Category";
        private final String PRODUCT_NAME = "ProductName";
        private final String PRICE = "Price";
        private final String PICTURE = "Picture";

        @Override
        protected void onPreExecute(){

        }

        @Override
        protected String doInBackground(String... params) {
            JSONParser j = new JSONParser();
            JSONObject json = j.getJSONFromUrl(getString(R.string.retrieve_products_url));
            JSONArray products = null;
            try{
                products = json.getJSONArray("product_list");
                productIDs = new int[products.length()];
                for(int i=0;i<products.length();i++){
                    JSONObject c = products.getJSONObject(i);
                    String thumbUrl = c.getString("thumb");
                    String productName = c.getString(PRODUCT_NAME);
                    String productPrice = "Php " + c.getString(PRICE);

                    productIDs[i] = c.getInt(PRODUCT_ID);

                    ImageListModel model = new ImageListModel(getString(R.string.root_url)+thumbUrl, productName, productPrice);
                    data.add(model);
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            ImageListAdapter adapter = new ImageListAdapter(Index.this, data);
            ListView lv = (ListView) findViewById(R.id.SaleProductsList);
            lv.setAdapter(adapter);
            hasLoaded = true;
        }
    }

    private void refreshCart(){
        ListView lv = (ListView)findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,purchasedProducts);
        lv.setAdapter(adapter);
        double totalPrice = 0;
        for(int i=0;i<purchasePrices.size();i++){
            totalPrice += purchasePrices.get(i);
        }
        TextView tp = (TextView) findViewById(R.id.totalPrice);
        tp.setText("Total Price: Php " + totalPrice);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK && requestCode == ADD_CART) {
            Toast.makeText(this, "Added " + data.getExtras().getInt("QUANTITY") + " " + data.getExtras().getString("PRODUCT_NAME") + "(s) to cart.",
                Toast.LENGTH_LONG).show();
            purchasedProducts.add(data.getExtras().getString("PRODUCT_NAME"));
            purchasedProductIDs.add(data.getExtras().getInt("PRODUCT_ID"));
            purchasePrices.add(data.getExtras().getDouble("PRODUCT_PRICE"));
            purchasedQuantities.add(data.getExtras().getInt("QUANTITY"));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        // Initialization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getSupportActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Adding Tabs
        for (String tab_name : tabNames) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }
        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        // Create global configuration and initialize ImageLoader with this config
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_index, menu);
        MenuItem item = menu.findItem(R.id.action_reload);
        if(currentTab!=0){
            item.setVisible(false);
        }
        else{
            item.setVisible(true);
        }

        // Associate searchable configuration with the SearchView
        /*SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(new ComponentName(getApplicationContext(), Index.class)));*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch(id){
            case R.id.action_settings:
                return true;
            case R.id.action_checkout:
                return true;
            case R.id.action_reload:
                Toast.makeText(this, "Reloading items...", Toast.LENGTH_SHORT).show();
                new RetrieveItemsTask().execute();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void OnListItemClick(int position){
        //Toast.makeText(this, "Click: Item "+position+" Product ID: "+productIDs[position], Toast.LENGTH_SHORT).show();
        Intent i = new Intent(Index.this, ProductActivity.class);
        i.putExtra("PRODUCT_ID", productIDs[position]);
        startActivityForResult(i, ADD_CART);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
        currentTab = tab.getPosition();
        viewPager.setCurrentItem(currentTab);
        switch(currentTab){
            case 0:
                if(!hasLoaded){
                    RetrieveItemsTask task = new RetrieveItemsTask();
                    task.execute();
                }
                break;
            case 2:
                refreshCart();
                break;
        }
        invalidateOptionsMenu();
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }
}
