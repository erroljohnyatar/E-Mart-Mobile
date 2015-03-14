package net.errol.emart.tabsadapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import net.errol.emart.CategoriesFragment;
import net.errol.emart.MyCartFragment;
import net.errol.emart.OnSaleFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {
 
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
 
    @Override
    public Fragment getItem(int index) {
 
        switch (index) {
        case 0:
            // On Sale fragment activity
            return new OnSaleFragment();
        case 1:
            // Categories fragment activity
            return new CategoriesFragment();
        case 2:
            // My Cart fragment activity
            return new MyCartFragment();
        }
 
        return null;
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }
 
}