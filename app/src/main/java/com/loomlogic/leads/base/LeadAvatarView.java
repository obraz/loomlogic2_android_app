package com.loomlogic.leads.base;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.loomlogic.R;
import com.loomlogic.leads.details.LeadEscrowStatusUtils;
import com.loomlogic.leads.entity.LeadItem;
import com.loomlogic.utils.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by alex on 3/16/17.
 */

public class LeadAvatarView extends FrameLayout {

    public LeadAvatarView(@NonNull Context context) {
        super(context);
        initViews();
    }

    public LeadAvatarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public LeadAvatarView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    public LeadAvatarView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews();
    }

    private void initViews() {
        inflate(getContext(), R.layout.view_lead_avatar, this);
    }

    public void setLeadAvatar(final LeadItem lead) {
        ImageView avatarIV = (ImageView) findViewById(R.id.iv_leadAvatar);
        final TextView initialsTV = (TextView) findViewById(R.id.tv_leadInitials);

        if (Utils.isUrlValid(lead.avatarUrl)) {
            Picasso.with(getContext())
                    .load(lead.avatarUrl)
                    .into(avatarIV, new Callback() {
                        @Override
                        public void onSuccess() {
                            initialsTV.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            setLeadDefaultAvatar(lead);
                        }
                    });

        } else {
            setLeadDefaultAvatar(lead);
        }
    }

    private void setLeadDefaultAvatar(LeadItem lead) {
        ImageView avatarIV = (ImageView) findViewById(R.id.iv_leadAvatar);
        TextView initialsTV = (TextView) findViewById(R.id.tv_leadInitials);

        initialsTV.setVisibility(View.VISIBLE);
        initialsTV.setText(String.format("%s%s", !TextUtils.isEmpty(lead.firstName) ? lead.firstName.charAt(0) : "", !TextUtils.isEmpty(lead.lastName) ? lead.lastName.charAt(0) : ""));
        int color;
        switch (lead.gender) {
            case MALE:
                color = ContextCompat.getColor(getContext(), R.color.lead_gender_male_color);
                break;
            case FEMALE:
                color = ContextCompat.getColor(getContext(), R.color.lead_gender_female_color);
                break;
            case NONE:
                color = ContextCompat.getColor(getContext(), R.color.lead_gender_none_color);
                break;
            default:
                color = ContextCompat.getColor(getContext(), R.color.lead_gender_none_color);
        }
        avatarIV.setBackgroundColor(color);
        avatarIV.setImageDrawable(null);
    }

    public void setEscrowStatus(LeadItem lead) {
        LeadEscrowStatusView escrowStatusView = (LeadEscrowStatusView) findViewById(R.id.pr_leadEscrowStatus);
        escrowStatusView.setStrokeWidth(getResources().getDimension(R.dimen.lead_escrow_progress_width));
        escrowStatusView.setColor(ContextCompat.getColor(getContext(), LeadEscrowStatusUtils.getEscrowStatusProgressColor(lead)));
        escrowStatusView.setMax(LeadEscrowStatusUtils.getMaxEscrowStatusCount(lead));
        escrowStatusView.setProgress(lead.escrowStatusDoneCount);
    }
}
