package net.errol.emart.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ListView;

import net.errol.emart.lib.ImageListAdapter;
import net.errol.emart.lib.ImageListModel;
import net.errol.emart.lib.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by JasmineGayle on 3/10/2015.
 */
public class RetrieveItemsTask extends AsyncTask<String, String, String> {

    private String RETRIEVE_URL;
    private Activity SOURCE;
    private ListView listView;

    ArrayList<ImageListModel> data = new ArrayList<>();
    //Product Indices
    private final String PRODUCT_ID = "ProductID";
    private final String CATEGORY = "Category";
    private final String PRODUCT_NAME = "ProductName";
    private final String PRICE = "Price";
    private final String PICTURE = "Picture";
    private String ROOT_URL = "ROOT";
    private final String COUNT = "product_count";

    public void setParams(String root, String url, Activity src, ListView lv){
        ROOT_URL = root;
        RETRIEVE_URL = url;
        SOURCE = src;
        listView = lv;
    }

    @Override
    protected String doInBackground(String... params) {
        JSONParser j = new JSONParser();
        JSONObject json = j.getJSONFromUrl(RETRIEVE_URL);
        JSONArray products = null;
        try{
            products = json.getJSONArray("product_list");
            for(int i=0;i<products.length();i++){
                JSONObject c = products.getJSONObject(i);
                String imgUrl = c.getString(PICTURE);
                String productName = c.getString(PRODUCT_NAME);
                String productPrice = c.getString(PRICE);

                ImageListModel model = new ImageListModel(ROOT_URL+imgUrl, productName, productPrice);
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
        ImageListAdapter adapter = new ImageListAdapter(SOURCE, data);
        listView.setAdapter(adapter);
    }
}
