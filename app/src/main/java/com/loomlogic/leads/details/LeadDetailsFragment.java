package com.loomlogic.leads.details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loomlogic.R;
import com.loomlogic.home.BaseHomeFragment;
import com.loomlogic.leads.base.Gender;
import com.loomlogic.leads.base.LeadItem;
import com.loomlogic.leads.base.LeadParticipantItem;
import com.loomlogic.leads.details.view.LeadDetailsInfoView;
import com.loomlogic.leads.details.view.LeadDetailsParticipantsView;

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
        lead = new LeadItem(1, 2, 3, "http://loomlogic.ucoz.net/4.jpg", "Jhon", "Doussing", Gender.MALE, "322 Lennie Squares Apt. 344", true, 3);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lead_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initInfoView(view);
        initParticipantsView(view);
    }

    private void initInfoView(View view) {
        LeadDetailsInfoView infoView = (LeadDetailsInfoView) view.findViewById(R.id.view_leadDetailsInfo);
        infoView.setLead(lead);
        infoView.setButtonsListener(new LeadDetailsInfoView.OnLeadInfoClickListener() {
            @Override
            public void onChangeStatusClick() {
                getHomeActivity().showErrorSnackBar("onChangeStatusClick");
            }

            @Override
            public void onSendClick() {
                getHomeActivity().showErrorSnackBar("onSendClick");
            }
        });
    }

    private void initParticipantsView(View view) {
        LeadDetailsParticipantsView participantsView = (LeadDetailsParticipantsView) view.findViewById(R.id.view_leadDetailsParticipants);
        participantsView.setParticipants(lead.participantList);
        participantsView.setButtonsListener(new LeadDetailsParticipantsView.OnLeadParticipantsClickListener() {
            @Override
            public void onWriteNoteClick(LeadParticipantItem participant) {
                getHomeActivity().showErrorSnackBar(participant.firstName + " - onWriteNoteClick");
            }

            @Override
            public void onAddParticipantClick() {
                getHomeActivity().showErrorSnackBar("onAddParticipantClick");
            }

        });
    }
}
