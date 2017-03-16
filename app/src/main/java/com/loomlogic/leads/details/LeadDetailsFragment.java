package com.loomlogic.leads.details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loomlogic.R;
import com.loomlogic.home.BaseHomeFragment;
import com.loomlogic.leads.base.LeadItem;

/**
 * Created by alex on 3/2/17.
 */

public class LeadDetailsFragment extends BaseHomeFragment {
    private LeadItem lead;

    public static LeadDetailsFragment newInstance() {
        LeadDetailsFragment fragment = new LeadDetailsFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lead = new LeadItem(1, 2, 3, "http://loomlogic.ucoz.net/4.jpg", "Jhon", "Doussing", LeadItem.Gender.MALE, "322 Lennie Squares Apt. 344", true, 3);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lead_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initInfoView(view);
    }

    private void initInfoView(View view) {
        LeadDetailsInfoView infoView = (LeadDetailsInfoView) view.findViewById(R.id.view_leadDetailsInfo);
        infoView.setLead(lead);
    }
}
