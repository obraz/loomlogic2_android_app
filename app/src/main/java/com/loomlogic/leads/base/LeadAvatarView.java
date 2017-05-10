package com.loomlogic.leads.base;

import android.content.Context;
import android.content.res.TypedArray;
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
import com.loomlogic.leads.entity.Gender;
import com.loomlogic.leads.entity.LeadItem;
import com.loomlogic.utils.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by alex on 3/16/17.
 */

public class LeadAvatarView extends FrameLayout {
    private ImageView avatarIV;
    private TextView initialsTV;

    public LeadAvatarView(@NonNull Context context) {
        super(context);
    }

    public LeadAvatarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(attrs);
    }

    public LeadAvatarView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(attrs);
    }

    public LeadAvatarView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(attrs);
    }

    private void initViews(AttributeSet attrs) {
        inflate(getContext(), R.layout.view_lead_avatar, this);
        avatarIV = (ImageView) findViewById(R.id.iv_leadAvatar);
        initialsTV = (TextView) findViewById(R.id.tv_leadInitials);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AvatarView);

        boolean needEscrowMargin = a.getBoolean(R.styleable.AvatarView_needEscrowMargin, false);
        a.recycle();

        if (needEscrowMargin){
            int margin = (int)getResources().getDimension(R.dimen.lead_escrow_default_margins);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) avatarIV.getLayoutParams();
            params.setMargins(margin, margin, margin, margin);
            avatarIV.setLayoutParams(params);
        }
    }

    public void setLeadAvatar(LeadItem lead) {
        setAvatar(lead.avatarUrl, lead.firstName, lead.lastName, lead.gender);
    }

    public void setAvatar(String url, final String firstName, final String lastName) {
        setAvatar(url, firstName, lastName, Gender.NONE);
    }

    public void setAvatar(final String url, final String firstName, final String lastName, final Gender gender) {
        if (Utils.isUrlValid(url)) {
            Picasso.with(getContext())
                    .load(url)
                    .into(avatarIV, new Callback() {
                        @Override
                        public void onSuccess() {
                            initialsTV.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            setDefaultAvatar(firstName, lastName, gender);
                        }
                    });

        } else {
            setDefaultAvatar(firstName, lastName, gender);
        }
    }

    private void setDefaultAvatar(String firstName, String lastName, Gender gender) {
        initialsTV.setVisibility(View.VISIBLE);
        initialsTV.setText(String.format("%s%s", !TextUtils.isEmpty(firstName) ? firstName.charAt(0) : "", !TextUtils.isEmpty(lastName) ? lastName.charAt(0) : ""));
        int color;
        switch (gender) {
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
