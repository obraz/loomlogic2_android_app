package com.loomlogic.leads.details.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loomlogic.R;
import com.loomlogic.leads.entity.LeadContractItem;

import static com.loomlogic.utils.AnimationUtils.collapse;
import static com.loomlogic.utils.AnimationUtils.expand;

/**
 * Created by alex on 3/16/17.
 */

public class LeadDetailsContractView extends LinearLayout {
    private boolean isVisible = false;

    public LeadDetailsContractView(Context context) {
        super(context);
        initViews();
    }

    public LeadDetailsContractView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public LeadDetailsContractView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    public LeadDetailsContractView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews();
    }

    private void initViews() {
        inflate(getContext(), R.layout.view_lead_details_contract, this);
    }

    public void setContract(LeadContractItem contract) {
        setContractInfo(contract);
        setShowMoreBtn();
    }

    private void setShowMoreBtn() {
        final LinearLayout infoLayout = (LinearLayout) findViewById(R.id.ll_leadDetailsContractContainer);

        final ImageView showMoreIcon = (ImageView) findViewById(R.id.iv_showMore);
        findViewById(R.id.ll_headerContainer).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVisible) {
                    showMoreIcon.setRotation(0);
                    collapse(infoLayout);
                } else {
                    showMoreIcon.setRotation(180);
                    expand(infoLayout);
                }
                isVisible = !isVisible;
            }
        });
    }

    private void setContractInfo(LeadContractItem contract) {
        TextView contractAddressTv = (TextView) findViewById(R.id.tv_leadDetailsContractAddress);
        contractAddressTv.setText(contract.address);

        TextView contractPurchasesTv = (TextView) findViewById(R.id.tv_leadDetailsContractPurchase);
        contractPurchasesTv.setText(contract.purchase);

        TextView contractCommissionsTv = (TextView) findViewById(R.id.tv_leadDetailsContractCommissions);
        contractCommissionsTv.setText(contract.commissions);

        TextView contractGrossTv = (TextView) findViewById(R.id.tv_leadDetailsContractGross);
        contractGrossTv.setText(contract.gross);
    }

}
