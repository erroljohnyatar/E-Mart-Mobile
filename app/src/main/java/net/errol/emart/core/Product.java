package net.errol.emart.core;

/**
 * Created by JasmineGayle on 3/12/2015.
 */
public class Product {
    private String NAME;
    private double PRICE;

    public Product(String name, double price){
        this.NAME = name;
        this.PRICE = price;
    }

    public String getName() {
        return NAME;
    }

    public void setName(String NAME) {
        this.NAME = NAME;
    }

    public double getPrice() {
        return PRICE;
    }

    public void setPrice(double PRICE) {
        this.PRICE = PRICE;
    }
}