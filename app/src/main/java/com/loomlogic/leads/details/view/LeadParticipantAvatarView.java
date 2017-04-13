package com.loomlogic.leads.details.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.loomlogic.R;
import com.loomlogic.leads.entity.LeadParticipantItem;
import com.loomlogic.utils.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by alex on 3/16/17.
 */

public class LeadParticipantAvatarView extends FrameLayout {

    private View selectedBg;

    public interface OnParticipantItemClickListener {
        void onParticipantItemClick(LeadParticipantItem participantItem);
    }

    public LeadParticipantAvatarView(Context context) {
        super(context);
        initViews();
    }

    public LeadParticipantAvatarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public LeadParticipantAvatarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    public LeadParticipantAvatarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews();
    }

    private void initViews() {
        inflate(getContext(), R.layout.view_item_participant, this);

        selectedBg = findViewById(R.id.view_selected);
    }

    public void setParticipant(final LeadParticipantItem participant) {
        setParticipant(participant, null);
    }

    public void setParticipant(final LeadParticipantItem participant, final OnParticipantItemClickListener callback) {
        if (Utils.isUrlValid(participant.avatarUrl)) {
            ImageView avatarIV = (ImageView) findViewById(R.id.iv_avatar);
            final TextView initialsTV = (TextView) findViewById(R.id.tv_initials);
            Picasso.with(getContext())
                    .load(participant.avatarUrl)
                    .into(avatarIV, new Callback() {
                        @Override
                        public void onSuccess() {
                            initialsTV.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            setDefaultAvatar(participant);
                        }
                    });

        } else {
            setDefaultAvatar(participant);
        }

        if (callback != null) {
            findViewById(R.id.fl_avatarContainer).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedBg.getVisibility() == GONE) {
                        selectView();
                        callback.onParticipantItemClick(participant);
                    }
                }
            });
        }
    }

    private void setDefaultAvatar(LeadParticipantItem participant) {
        ImageView avatarIV = (ImageView) findViewById(R.id.iv_avatar);
        final TextView initialsTV = (TextView) findViewById(R.id.tv_initials);

        initialsTV.setVisibility(View.VISIBLE);
        initialsTV.setText(String.format("%s%s", !TextUtils.isEmpty(participant.firstName) ? participant.firstName.charAt(0) : "", !TextUtils.isEmpty(participant.lastName) ? participant.lastName.charAt(0) : ""));
        int color;
        switch (participant.gender) {
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

    public void selectView() {
        selectedBg.setVisibility(VISIBLE);
    }

    public void deselectView() {
        selectedBg.setVisibility(GONE);
    }

}
