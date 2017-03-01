package com.loomlogic.leads.menu;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by alex on 2/28/17.
 */

public class LeadsTypeAdapter extends FragmentStatePagerAdapter {
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
}
