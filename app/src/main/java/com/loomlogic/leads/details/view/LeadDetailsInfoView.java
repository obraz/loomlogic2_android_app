package com.loomlogic.leads.details.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loomlogic.R;
import com.loomlogic.home.HomeActivity;
import com.loomlogic.leads.base.LeadAvatarView;
import com.loomlogic.leads.details.LeadEscrowStatusItem;
import com.loomlogic.leads.details.LeadEscrowStatusState;
import com.loomlogic.leads.details.LeadEscrowStatusUtils;
import com.loomlogic.leads.entity.LeadItem;

import java.util.ArrayList;

import static com.loomlogic.utils.AnimationUtils.collapse;
import static com.loomlogic.utils.AnimationUtils.expand;

/**
 * Created by alex on 3/16/17.
 */

public class LeadDetailsInfoView extends LinearLayout {
    private boolean isVisible = false;

    public interface OnLeadInfoClickListener {
        void onChangeStatusClick();

        void onSendClick();

        void onPhoneClick();

        void onMessageClick();

        void onDetailsViewShow();

        void onDetailsViewHide();
    }

    private LeadItem leadItem;
    private OnLeadInfoClickListener callback;
    private int fullViewHeight;

    public LeadDetailsInfoView(Context context) {
        super(context);
        initViews();
    }

    public LeadDetailsInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public LeadDetailsInfoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    public LeadDetailsInfoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews();
    }

    private void initViews() {
        inflate(getContext(), R.layout.view_lead_details_info, this);
    }

    public void setLead(LeadItem lead) {
        this.leadItem = lead;
        updateData();
    }

    public void setCallbacks(final OnLeadInfoClickListener callback) {
        this.callback = callback;

        findViewById(R.id.btn_changeStatus).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onChangeStatusClick();
            }
        });
        findViewById(R.id.btn_send).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onSendClick();
            }
        });

        findViewById(R.id.tv_leadDetailsPhone).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onPhoneClick();
            }
        });

        findViewById(R.id.tv_leadDetailsEmail).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onMessageClick();
            }
        });
    }

    private void updateData() {
        setLeadEscrowStatus();
        setLeadInfo();
    }

    private void setLeadEscrowStatus() {
        LinearLayout leadDetailsEscrowStatusContainer = (LinearLayout) findViewById(R.id.ll_leadDetailsEscrowStatusContainer);

        ArrayList<LeadEscrowStatusItem> list = LeadEscrowStatusUtils.getEscrowStatusItemList(leadItem);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f);

        for (LeadEscrowStatusItem item : list) {
            View escrowStatusView = LayoutInflater.from(getContext()).inflate(R.layout.item_lead_details_escrow_status, null);
            TextView escrowStatus = (TextView) escrowStatusView.findViewById(R.id.tv_escrowStatusAbbreviation);
            escrowStatus.setText(item.getAbbreviation());

            if (item.getState() == LeadEscrowStatusState.DONE) {
                escrowStatusView.setBackgroundResource(leadItem.isFinancing ? R.color.lead_detail_escrow_status_financing_done_bg_color : R.color.lead_detail_escrow_status_cash_done_bg_color);
            } else if (item.getState() == LeadEscrowStatusState.CURRENT) {
                escrowStatus.setBackgroundResource(leadItem.isFinancing ? R.drawable.lead_details_escrow_status_financing_current_bg : R.drawable.lead_details_escrow_status_cash_current_bg);
                escrowStatus.setTextColor(ContextCompat.getColor(getContext(), R.color.lead_detail_escrow_status_current_text_color));
            }
            escrowStatusView.setLayoutParams(param);

            leadDetailsEscrowStatusContainer.addView(escrowStatusView);
        }
    }

    private void setLeadInfo() {
        LeadAvatarView avatarV = (LeadAvatarView) findViewById(R.id.view_leadAvatar);
        avatarV.setLeadAvatar(leadItem);

        TextView nameTv = (TextView) findViewById(R.id.tv_leadName);
        nameTv.setText(leadItem.getFullFormattedName());

        TextView addressTv = (TextView) findViewById(R.id.tv_leadInfo);
        addressTv.setText(leadItem.address);
        addressTv.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), leadItem.isFinancing ? R.drawable.ic_buyer_financing : R.drawable.ic_buyer_cash), null, null, null);

        final LinearLayout infoLayout = (LinearLayout) findViewById(R.id.ll_leadDetailsInfoContainer);

        final ImageView showMoreIcon = (ImageView) findViewById(R.id.iv_showMore);
        findViewById(R.id.rl_headerContainer).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVisible) {
                    showMoreIcon.setRotation(0);
                    collapse(infoLayout);
                    callback.onDetailsViewHide();
                } else {
                    showMoreIcon.setRotation(180);
                    expand(infoLayout);
                    if (fullViewHeight==0){
                        calculateFullHeight();
                    }else {
                        callback.onDetailsViewShow();
                    }
                }
                isVisible = !isVisible;
            }
        });

        TextView infoPhoneTv = (TextView) findViewById(R.id.tv_leadDetailsPhone);
        infoPhoneTv.setText(leadItem.phone);
        infoPhoneTv.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((HomeActivity) getContext()).showCopyTextDialog(leadItem.phone);
                return true;
            }
        });


        TextView infoAddressTv = (TextView) findViewById(R.id.tv_leadDetailsAddress);
        infoAddressTv.setText(leadItem.address);
        infoAddressTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getContext()).showMapDialog(leadItem.lat, leadItem.lon, leadItem.address);
            }
        });

        TextView infoEmailTv = (TextView) findViewById(R.id.tv_leadDetailsEmail);
        infoEmailTv.setText(leadItem.email);
        infoEmailTv.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((HomeActivity) getContext()).showCopyTextDialog(leadItem.email);
                return true;
            }
        });

        TextView infoSourceTv = (TextView) findViewById(R.id.tv_leadDetailsSource);
        infoSourceTv.setText(leadItem.source);
    }

    private void calculateFullHeight() {
        post(new Runnable() {
            @Override
            public void run() {
                fullViewHeight = getHeight();
                callback.onDetailsViewShow();
            }
        });
    }

    public boolean isDetailsViewVisible() {
        return isVisible;
    }

    public int getFullHeight() {
        return fullViewHeight;
    }
}
