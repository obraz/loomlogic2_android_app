package com.loomlogic.leads.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.loomlogic.leads.base.LeadStatus;
import com.loomlogic.leads.list.LeadsFragment;

/**
 * Created by alex on 3/15/17.
 */

public class LeadsMainPagerAdapter extends FragmentPagerAdapter {
   // private LeadItem mLeadItem;
    private int mNumOfTabs;

    public LeadsMainPagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
       // this.mLeadItem = leadItem;
        this.mNumOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = LeadsFragment.newInstance(LeadStatus.ACTIVE);
                break;
            case 1:
                fragment = LeadsFragment.newInstance(LeadStatus.ACTIVE);
                break;
            case 2:
                fragment = LeadsFragment.newInstance(LeadStatus.ACTIVE);
                break;
            case 3:
                fragment = LeadsFragment.newInstance(LeadStatus.ACTIVE);
                break;
            case 4:
                fragment = LeadsFragment.newInstance(LeadStatus.ACTIVE);
                break;
            default:
                fragment = LeadsFragment.newInstance(LeadStatus.ACTIVE);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
