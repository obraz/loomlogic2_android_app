package com.loomlogic.leads.mainleaddetails;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.loomlogic.leads.details.LeadDetailsFragment;
import com.loomlogic.leads.entity.LeadItem;

/**
 * Created by alex on 3/15/17.
 */

public class LeadDetailsPagerAdapter extends FragmentPagerAdapter {
    private LeadItem mLeadItem;
    private int mNumOfTabs;

    public LeadDetailsPagerAdapter(FragmentManager fm, LeadItem leadItem, int numOfTabs) {
        super(fm);
        this.mLeadItem = leadItem;
        this.mNumOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = LeadDetailsFragment.newInstance(mLeadItem);
                break;
            case 1:
                fragment = new DefaultLeadDetailsFragment();
                break;
            case 2:
                fragment = new DefaultLeadDetailsFragment();
                break;
            case 3:
                fragment = new DefaultLeadDetailsFragment();
                break;
            case 4:
                fragment = new DefaultLeadDetailsFragment();
                break;
            default:
                fragment = new DefaultLeadDetailsFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
