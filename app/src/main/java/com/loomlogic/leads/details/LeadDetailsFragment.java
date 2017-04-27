package com.loomlogic.leads.details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.loomlogic.R;
import com.loomlogic.base.MessageEvent;
import com.loomlogic.home.BaseHomeFragment;
import com.loomlogic.leads.details.view.LeadDetailsActionButtonsView;
import com.loomlogic.leads.details.view.LeadDetailsContractView;
import com.loomlogic.leads.details.view.LeadDetailsDatesView;
import com.loomlogic.leads.details.view.LeadDetailsInfoView;
import com.loomlogic.leads.details.view.LeadDetailsParticipantsView;
import com.loomlogic.leads.details.view.LeadDetailsPreApprovalView;
import com.loomlogic.leads.details.view.LeadDetailsSearchCriteriaView;
import com.loomlogic.leads.details.view.LeadDetailsTransactionView;
import com.loomlogic.leads.entity.LeadItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.loomlogic.leads.mainleaddetails.LeadDetailsMainFragment.KEY_LEAD_ITEM;

/**
 * Created by alex on 3/2/17.
 */

public class LeadDetailsFragment extends BaseHomeFragment {
    private LeadItem lead;
    private LeadDetailsActionButtonsView actionBtnContainer;
    private LeadDetailsInfoView infoView;

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
        initActionButtons(view);

        switch (lead.leadData.getStatus()) {
            case LEADS:
            case LENDER:
                initSearchCriteriaView(view);
                break;
            case SHOPPING:
                initPreApprovalView(view);
                break;
            case CONTRACT:
                initContractView(view);
                initTransactionsView(view);
                initDatesView(view);
                break;
            case CLOSED:
                initContractView(view);
                initDatesView(view);
                break;
        }

        setupScrollViewBehaviour(view);
    }

    private void initActionButtons(View view) {
        actionBtnContainer = (LeadDetailsActionButtonsView) view.findViewById(R.id.view_leadDetailsActionButtons);
    }

    private void initInfoView(View view) {
        infoView = (LeadDetailsInfoView) view.findViewById(R.id.view_leadDetailsInfo);
        infoView.setLead(lead);
        infoView.setCallbacks(new LeadDetailsInfoView.OnLeadInfoClickListener() {
            @Override
            public void onChangeStatusClick() {
                getHomeActivity().showErrorSnackBar("onChangeStatusClick");
            }

            @Override
            public void onSendClick() {
                getHomeActivity().showErrorSnackBar("onSendClick");
            }

            @Override
            public void onPhoneClick() {
                actionBtnContainer.setPhoneNumber(lead.phone);
                actionBtnContainer.onCallClick();
            }

            @Override
            public void onMessageClick() {
                actionBtnContainer.onMsgClick();
            }

            @Override
            public void onDetailsViewShow() {
                if (infoView.getFullHeight() > actionBtnContainer.getActionButtonTopOffset()) {
                    actionBtnContainer.hideMainActionButtons();
                }
            }

            @Override
            public void onDetailsViewHide() {
                actionBtnContainer.showMainActionButtons();
            }
        });
    }

    private void initParticipantsView(View view) {
        LeadDetailsParticipantsView participantsView = (LeadDetailsParticipantsView) view.findViewById(R.id.view_leadDetailsParticipants);
        participantsView.setVisibility(View.VISIBLE);
        participantsView.setParticipants(lead.participantList);
        participantsView.setButtonsListener(new LeadDetailsParticipantsView.OnLeadParticipantsClickListener() {

            @Override
            public void onAddParticipantClick() {
                getHomeActivity().showErrorSnackBar("onAddParticipantClick");
            }

        });
    }

    private void initSearchCriteriaView(View view) {
        LeadDetailsSearchCriteriaView searchCriteriaView = (LeadDetailsSearchCriteriaView) view.findViewById(R.id.view_leadDetailsSearchCriteria);
        searchCriteriaView.setVisibility(View.VISIBLE);
        searchCriteriaView.setSearchCriteria(lead.leadContract);
    }

    private void initPreApprovalView(View view) {
        LeadDetailsPreApprovalView preApprovalView = (LeadDetailsPreApprovalView) view.findViewById(R.id.view_leadDetailsPreApproval);
        preApprovalView.setVisibility(View.VISIBLE);
        preApprovalView.setPreApproval(lead.leadContract);
    }

    private void initContractView(View view) {
        LeadDetailsContractView contractView = (LeadDetailsContractView) view.findViewById(R.id.view_leadDetailsContract);
        contractView.setVisibility(View.VISIBLE);
        contractView.setContract(lead.leadContract);
    }

    private void initTransactionsView(View view) {
        LeadDetailsTransactionView transactionView = (LeadDetailsTransactionView) view.findViewById(R.id.view_leadDetailsTransaction);
        transactionView.setVisibility(View.VISIBLE);
        transactionView.setTransactions(lead.transactionList);
    }

    private void initDatesView(View view) {
        LeadDetailsDatesView datesView = (LeadDetailsDatesView) view.findViewById(R.id.view_leadDetailsDates);
        datesView.setVisibility(View.VISIBLE);
    }

    private void setupScrollViewBehaviour(View view) {
        NestedScrollView scrollView = (NestedScrollView) view.findViewById(R.id.scrollView_leadDetails);
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (infoView.isDetailsViewVisible()) {
                    if ((infoView.getHeight() - scrollY) > actionBtnContainer.getActionButtonTopOffset()) {
                        actionBtnContainer.hideMainActionButtons();
                    } else {
                        actionBtnContainer.showMainActionButtons();
                    }
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getType()) {
            case NAVIGATION_BAR_SHOW:
                animateViewAboveNavBar(actionBtnContainer, true);
                break;
            case NAVIGATION_BAR_HIDE:
                animateViewAboveNavBar(actionBtnContainer, false);
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
