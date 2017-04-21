package com.loomlogic.leads.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.loomlogic.leads.base.LeadData;
import com.loomlogic.leads.base.LeadSubStage;
import com.loomlogic.leads.base.LeadUtils;
import com.loomlogic.leads.list.LeadsFragment;

/**
 * Created by alex on 3/15/17.
 */

public class LeadsMainPagerAdapter extends FragmentStatePagerAdapter {
    private LeadData leadData;
    private int mNumOfTabs;

    public LeadsMainPagerAdapter(FragmentManager fm, LeadData leadData) {
        super(fm);
        this.leadData = leadData;

    }

    @Override
    public Fragment getItem(int position) {
        switch (leadData.getStatus()) {
            case LEADS:
                setLeadsNewSubStage(position);
                break;
            case LENDER:
                setLeadsLenderSubStage(position);
                break;
        }
        return LeadsFragment.newInstance(leadData);
    }

    private void setLeadsNewSubStage(int position) {
        switch (position) {
            case 0:
                leadData.setSubStage(LeadSubStage.NEW);
                break;
            case 1:
                leadData.setSubStage(LeadSubStage.ENGAGED);
                break;
            case 2:
                leadData.setSubStage(LeadSubStage.FUTURE);
                break;
        }
    }

    private void setLeadsLenderSubStage(int position) {
        switch (position) {
            case 0:
                leadData.setSubStage(LeadSubStage.NEW);
                break;
            case 1:
                leadData.setSubStage(LeadSubStage.ENGAGED);
                break;
            case 2:
                leadData.setSubStage(LeadSubStage.APPOINTMENT);
                break;
            case 3:
                leadData.setSubStage(LeadSubStage.APPLICATION);
                break;
        }
    }

    @Override
    public int getCount() {
        return LeadUtils.getLeadSubStagesCount(leadData);
    }
}
