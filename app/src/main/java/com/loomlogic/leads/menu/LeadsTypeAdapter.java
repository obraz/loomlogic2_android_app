package com.loomlogic.leads.menu;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * Created by alex on 2/28/17.
 */

public class LeadsTypeAdapter extends FragmentPagerAdapter {
    public LeadsTypeAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new LeadTypeBuyersFragment();
            case 1:
                return new LeadTypeSellersFragment();
        }

        return new Fragment();
    }

    @Override
    public int getCount() {
        return 2;
    }

    public Fragment getFragment(ViewGroup view, int position) {
        return (Fragment) instantiateItem(view, position);
    }
}
