package com.loomlogic.leads.details.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loomlogic.R;
import com.loomlogic.leads.base.LeadParticipantItem;
import com.loomlogic.leads.base.LeadParticipantRole;

import java.util.ArrayList;

import static com.loomlogic.utils.AnimationUtils.collapse;
import static com.loomlogic.utils.AnimationUtils.expand;

/**
 * Created by alex on 3/16/17.
 */

public class LeadDetailsParticipantsView extends LinearLayout {
    private LeadDetailsParticipantItemView currentParticipantView;
    private LeadParticipantItem currentParticipant;
    private TextView participantNameTv, participantRoleTv, participantPhoneTv, participantAddressTv, participantEmailTv;
    private LinearLayout infoLayout;
    private ImageButton showMoreBtn;
    private boolean isVisible = false;

    public interface OnLeadParticipantsClickListener {
        void onWriteNoteClick(LeadParticipantItem participant);

        void onAddParticipantClick();
    }

    private ArrayList<LeadParticipantItem> participantsList;

    public LeadDetailsParticipantsView(Context context) {
        super(context);
        initViews();
    }

    public LeadDetailsParticipantsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public LeadDetailsParticipantsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    public LeadDetailsParticipantsView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews();
    }

    private void initViews() {
        inflate(getContext(), R.layout.view_lead_details_participants, this);
        participantNameTv = (TextView) findViewById(R.id.tv_leadParticipantName);
        participantRoleTv = (TextView) findViewById(R.id.tv_leadParticipantRole);
        participantPhoneTv = (TextView) findViewById(R.id.tv_leadParticipantPhone);
        participantAddressTv = (TextView) findViewById(R.id.tv_leadParticipantAddress);
        participantEmailTv = (TextView) findViewById(R.id.tv_leadParticipantEmail);

        infoLayout = (LinearLayout) findViewById(R.id.ll_leadDetailsParticipantsContainer);
        showMoreBtn = (ImageButton) findViewById(R.id.ib_showMore);
    }

    public void setParticipants(ArrayList<LeadParticipantItem> participantsList) {
        this.participantsList = participantsList;
        updateData();
    }

    public void setButtonsListener(final OnLeadParticipantsClickListener callback) {
        findViewById(R.id.ib_addParticipant).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onAddParticipantClick();
            }
        });

        findViewById(R.id.btn_writeNote).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onWriteNoteClick(currentParticipant);
            }
        });
    }

    private void updateData() {
        setParticipants();
        setShowMoreBtn();
    }


    private void setParticipants() {
        GridLayout participantsAvatarLayout = (GridLayout) findViewById(R.id.gl_leadParticipants);
        for (int i = 0; i < participantsList.size(); i++) {
            final LeadParticipantItem participant = participantsList.get(i);

            final LeadDetailsParticipantItemView participantAvatarView = new LeadDetailsParticipantItemView(getContext());
            participantAvatarView.setParticipant(participant, new LeadDetailsParticipantItemView.OnParticipantItemClickListener() {
                @Override
                public void onParticipantItemClick(LeadParticipantItem participantItem) {
                    currentParticipant = participant;

                    currentParticipantView.deselectView();
                    currentParticipantView = participantAvatarView;

                    setParticipantInfo(participantItem);
                    if (!isVisible) {
                        showMoreBtn.performClick();
                    }
                }
            });
            participantsAvatarLayout.addView(participantAvatarView);

            if (i == 0) {
                currentParticipant = participant;
                currentParticipantView = participantAvatarView;
                participantAvatarView.selectView();
                setParticipantInfo(participant);
            }
        }

    }

    private void setShowMoreBtn() {
        showMoreBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVisible) {
                    showMoreBtn.setRotation(0);
                    collapse(infoLayout);
                } else {
                    showMoreBtn.setRotation(180);
                    expand(infoLayout);
                }
                isVisible = !isVisible;
            }
        });
    }

    private void setParticipantInfo(LeadParticipantItem participantItem) {
        participantNameTv.setText(String.format("%s %s", participantItem.firstName, participantItem.lastName));
        participantRoleTv.setText(LeadParticipantRole.toString(participantItem.role));
        participantPhoneTv.setText(participantItem.phone);
        participantAddressTv.setText(participantItem.address);
        participantEmailTv.setText(participantItem.email);
    }

}
