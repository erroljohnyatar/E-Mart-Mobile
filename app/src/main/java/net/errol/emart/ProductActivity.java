package net.errol.emart;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import net.errol.emart.lib.JSONParser;
import net.errol.emart.tasks.RetrieveBitmapFromURL;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ProductActivity extends ActionBarActivity {

    private int PRODUCT_ID;
    private int SUCCESS;

    private String PRODUCT_NAME;
    private double PRODUCT_PRICE;
    private String PICTURE_URL;

    private int QUANTITY=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        PRODUCT_ID = getIntent().getExtras().getInt("PRODUCT_ID");
        new GetProductInfo().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product, menu);
        return true;
    }

    class GetProductInfo extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            try{
                //Get JSON Object
                JSONParser jparser = new JSONParser();
                JSONObject j = jparser.getJSONFromUrl(getString(R.string.retrieve_product_url)+"?product_id="+PRODUCT_ID);

                //Check login status
                int success=0;
                try{
                    success = j.getInt("success");
                }catch(NullPointerException e){
                    SUCCESS=2;
                }
                if(success==1){
                    SUCCESS=1;
                    PRODUCT_NAME = j.getString("ProductName");
                    PRODUCT_PRICE = Double.parseDouble(j.getString("Price"));
                    PICTURE_URL = j.getString("Picture");
                }
            }catch(JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url){
            ImageView iv = (ImageView) findViewById(R.id.productImg);
            TextView productName = (TextView) findViewById(R.id.productName);
            TextView productPrice = (TextView) findViewById(R.id.productPrice);
            TextView productDesc = (TextView) findViewById(R.id.productDesc);
            try{
                iv.setImageBitmap(new RetrieveBitmapFromURL().execute(getString(R.string.root_url)+PICTURE_URL).get());
            }
            catch(Exception e){
                Toast.makeText(getApplicationContext(), "Cannot load image.", Toast.LENGTH_SHORT).show();
            }
            productName.setText(PRODUCT_NAME);
            productPrice.setText("Php "+PRODUCT_PRICE);
        }
    }



    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == R.id.action_addcart){
            //Initialize alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this).setIcon(R.drawable.ic_addcart_black);
            LayoutInflater inflater = (LayoutInflater)
                    getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View npView = inflater.inflate(R.layout.number_picker_dialog_layout,null);
            builder.setTitle(getString(R.string.quantity));
            builder.setView(npView);
            builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    EditText np = (EditText) npView.findViewById(R.id.NumberPicker1);
                    QUANTITY = Integer.parseInt(np.getText().toString());
                    if(QUANTITY<1){
                        Toast.makeText(getApplicationContext(), "Invalid value!", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Intent i = new Intent();
                        i.putExtra("QUANTITY", QUANTITY);
                        i.putExtra("PRODUCT_NAME", PRODUCT_NAME);
                        i.putExtra("PRODUCT_ID", PRODUCT_ID);
                        i.putExtra("PRODUCT_PRICE", PRODUCT_PRICE);
                        setResult(RESULT_OK, i);
                        finish();
                    }
                }
            });

            builder.setNegativeButton(R.string.dialog_cancel, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }
}
