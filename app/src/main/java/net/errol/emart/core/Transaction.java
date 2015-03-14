package net.errol.emart.core;

import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by JasmineGayle on 3/12/2015.
 */
public class Transaction {
    private ArrayList<Product> ProductList;
    private String ID;
    private double totalPrice;

    public Transaction(){
        totalPrice = 0;
        ProductList = new ArrayList<>();
    }

    public void addItem(Product item){
        ProductList.add(item);
        totalPrice += item.getPrice();
    }

    public Product getItem(String productName){
        for (int i = 0; i < ProductList.size(); i++) {
            if(ProductList.get(i).getName()==productName){
                return ProductList.get(i);
            }
        }
        return null;
    }

    public double getTotalPrice(){
        return totalPrice;
    }

}