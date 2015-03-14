package net.errol.emart.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;

/**
 * Created by JasmineGayle on 3/10/2015.
 */
public class RetrieveBitmapFromURL extends AsyncTask<String, String, Bitmap> {

    @Override
    protected Bitmap doInBackground(String... urls) {
        Bitmap bmp = null;
        String url = urls[0];
        InputStream in=null;
        try{
            in = new java.net.URL(url).openStream();
            bmp = BitmapFactory.decodeStream(in);
            in.close();
        }
        catch(Exception ex){
            Log.e("Error: ", ex.getMessage());
            ex.printStackTrace();
        }
        return bmp;
    }
}