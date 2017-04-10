package com.loomlogic.leads.details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.loomlogic.R;
import com.loomlogic.home.BaseHomeFragment;
import com.loomlogic.leads.details.view.LeadDetailsContractView;
import com.loomlogic.leads.details.view.LeadDetailsDatesView;
import com.loomlogic.leads.details.view.LeadDetailsInfoView;
import com.loomlogic.leads.details.view.LeadDetailsParticipantsView;
import com.loomlogic.leads.details.view.LeadDetailsTransactionView;
import com.loomlogic.leads.entity.LeadItem;

import static com.loomlogic.leads.base.LeadDetailsMainFragment.KEY_LEAD_ITEM;

/**
 * Created by alex on 3/2/17.
 */

public class LeadDetailsFragment extends BaseHomeFragment {
    private LeadItem lead;

    public static LeadDetailsFragment newInstance(LeadItem lead) {
        LeadDetailsFragment fragment = new LeadDetailsFragment();
        Bundle args = new Bundle();
        args.putString(KEY_LEAD_ITEM, new Gson().toJson(lead));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lead = new Gson().fromJson(getArguments().getString(KEY_LEAD_ITEM), LeadItem.class);
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
        initContractView(view);
        initTransactionsView(view);
        initDatesView(view);
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
            public void onAddParticipantClick() {
                getHomeActivity().showErrorSnackBar("onAddParticipantClick");
            }

        });
    }

    private void initContractView(View view) {
        LeadDetailsContractView contractView = (LeadDetailsContractView) view.findViewById(R.id.view_leadDetailsContract);
        contractView.setContract(lead.leadContract);
    }

    private void initTransactionsView(View view) {
        LeadDetailsTransactionView transactionView = (LeadDetailsTransactionView) view.findViewById(R.id.view_leadDetailsTransaction);
        transactionView.setTransactions(lead.transactionList);
    }

    private void initDatesView(View view) {
        LeadDetailsDatesView datesView = (LeadDetailsDatesView) view.findViewById(R.id.view_leadDetailsDates);
    }
}
