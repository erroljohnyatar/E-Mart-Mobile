package net.errol.emart.lib;

import android.graphics.Bitmap;

/**
 * Created by JasmineGayle on 3/10/2015.
 */
public class ImageListModel {
    private String imgUrl;
    private String productName;
    private String productPrice;

    public ImageListModel(String i, String pN, String pP){
        imgUrl = i;
        productName = pN;
        productPrice = pP;
    }

    public String getImgUrl(){
        return imgUrl;
    }

    public String getProductName(){
        return productName;
    }

    public String getProductPrice() {
        return productPrice;
    }
}
