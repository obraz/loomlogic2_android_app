package com.loomlogic.leads.details;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by alex on 3/15/17.
 */

public class LeadDetailsPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public LeadDetailsPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new DefaultLeadDetailsFragment();
            case 1:
                return new DefaultLeadDetailsFragment();
            case 2:
                return new DefaultLeadDetailsFragment();
            case 3:
                return new DefaultLeadDetailsFragment();
            case 4:
                return new DefaultLeadDetailsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
