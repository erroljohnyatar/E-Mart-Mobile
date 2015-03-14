package net.errol.emart.core;

import android.os.AsyncTask;

import java.util.ArrayList;

/**
 * Created by JasmineGayle on 3/12/2015.
 */
public class Apriori {

    private ArrayList<Transaction> TRANSACTIONS;

    private double SUPPORT = 0;
    private double CONFIDENCE = 0;
    private double SUPPORT_PRICE = 0;
    private double CONFIDENCE_PRICE = 0;

    private int MIN_PRODUCT_PER_TRANSACTION=0;

    public Apriori(double supp, double conf, double supp_p, double conf_p, int min_p_t){

    }

    private class getTransactions extends AsyncTask<String, String, String>{ //Params, Progress, Result

        @Override
        protected String doInBackground(String... params) {
            return null;
        }
    }

}
