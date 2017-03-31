package com.loomlogic.leads.details.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loomlogic.R;
import com.loomlogic.leads.entity.LeadTransactionItem;

import java.util.ArrayList;

import static com.loomlogic.utils.AnimationUtils.collapse;
import static com.loomlogic.utils.AnimationUtils.expand;

/**
 * Created by alex on 3/16/17.
 */

public class LeadDetailsTransactionView extends LinearLayout {
    private boolean isVisible = false;

    public LeadDetailsTransactionView(Context context) {
        super(context);
        initViews();
    }

    public LeadDetailsTransactionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public LeadDetailsTransactionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    public LeadDetailsTransactionView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews();
    }

    private void initViews() {
        inflate(getContext(), R.layout.view_lead_details_transaction, this);
    }

    public void setTransactions(ArrayList<LeadTransactionItem> transactionList) {
        setTransactionsData(transactionList);
        setShowMoreBtn();
    }

    private void setShowMoreBtn() {
        final LinearLayout infoLayout = (LinearLayout) findViewById(R.id.ll_leadDetailsTransactionContainer);

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

    private void setTransactionsData(ArrayList<LeadTransactionItem> transactionList) {
        TextView transactionTitleTv = (TextView) findViewById(R.id.tv_leadDetailsTransactionTitle);
        transactionTitleTv.setText(getResources().getString(R.string.lead_details_transactions, transactionList.size()));

        LinearLayout infoLayout = (LinearLayout) findViewById(R.id.ll_leadDetailsTransactionContainer);

        for (LeadTransactionItem transactionItem : transactionList) {
            View transactionItemView = LayoutInflater.from(getContext()).inflate(R.layout.item_lead_details_transaction, null);
            TextView address = (TextView) transactionItemView.findViewById(R.id.tv_leadDetailsTransactionAddress);
            address.setText(transactionItem.address);

            infoLayout.addView(transactionItemView);
        }
    }

}
