package com.loomlogic.leads.base;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultAction;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionDefault;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionMoveToSwipedDirection;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractSwipeableItemViewHolder;
import com.loomlogic.R;
import com.loomlogic.leads.entity.LeadItem;
import com.loomlogic.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import static com.loomlogic.R.color.lead_call_btn_bg_color;
import static com.loomlogic.R.color.lead_msg_btn_bg_color;

/**
 * Created by Alexandr on 11/19/15.
 */
public class LeadsAdapter extends RecyclerView.Adapter<LeadsAdapter.LeadsHolder> implements SwipeableItemAdapter<LeadsAdapter.LeadsHolder>, View.OnClickListener {

    private interface Swipeable extends SwipeableItemConstants {
    }

    private List<LeadItem> leads;
    private Context context;
    private float maxSwipeWidthCurrent;
    private float maxSwipeWidth2Buttons;
    private float maxSwipeWidth3Buttons;
    private int[] colors;

    private static final int BACKGROUND_COLOR_FRACTION_COUNT = 20;
    private float[] positions = {1, BACKGROUND_COLOR_FRACTION_COUNT};

    private OnLeadClickListener onClickListener;

    interface OnLeadClickListener {
        void onItemClickListener(LeadItem item);

        void onMessageEmailClickListener(LeadItem item);

        void onMessageNoteClickListener(LeadItem item);

        void onMessageSMSClickListener(LeadItem item);

        void onCallSystemClickListener(LeadItem item);

        void onCallTwillioClickListener(LeadItem item);
    }

    public LeadsAdapter(Context context, OnLeadClickListener eventListener) {
        this.context = context;
        float paddings = context.getResources().getDimension(R.dimen.lead_swipe_action_button_padding) * 2;
        maxSwipeWidth2Buttons = context.getResources().getDimension(R.dimen.lead_swipe_action_button_width) * 2 + paddings;
        maxSwipeWidth3Buttons = context.getResources().getDimension(R.dimen.lead_swipe_action_button_width) * 3 + paddings;
        maxSwipeWidthCurrent = maxSwipeWidth2Buttons;

        this.onClickListener = eventListener;
        this.leads = new ArrayList<>();

        // SwipeableItemAdapter requires stable ID, and also
        // have to implement the getItemId() method appropriately.
        setHasStableIds(true);

        int colorFrom = Color.WHITE;
        int colorTo = ContextCompat.getColor(context, R.color.lead_btn_container_bg_color);
        colors = new int[]{colorFrom, colorTo};
    }

    public void setData(List<LeadItem> leads) {
        this.leads = leads;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return leads.get(position).id;
    }

    @Override
    public LeadsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.item_lead, parent, false);
        return new LeadsHolder(v);
    }

    @Override
    public void onBindViewHolder(final LeadsHolder holder, final int position) {
        final LeadItem lead = leads.get(position);
        setClickListenerToView(holder.mContainer, lead);
        setClickListenerToView(holder.mCallSystemIv, lead);
        setClickListenerToView(holder.mCallTwillioIv, lead);
        setClickListenerToView(holder.mMessageSmsIv, lead);
        setClickListenerToView(holder.mMessageEmailIv, lead);
        setClickListenerToView(holder.mMessageNoteIv, lead);

        holder.mCallIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mMessageIv.setVisibility(View.GONE);
                holder.mCallIv.setVisibility(View.GONE);

                animateShowLeadConnectBtn(holder.mCallSystemIv);
                animateShowLeadConnectBtn(holder.mCallTwillioIv);
            }
        });

        holder.mMessageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lead.setPinned(true);
                maxSwipeWidthCurrent = maxSwipeWidth3Buttons;
                notifyItemChanged(position);

                holder.mMessageIv.setVisibility(View.GONE);
                holder.mCallIv.setVisibility(View.GONE);

                holder.mMessageNoteIv.setVisibility(View.VISIBLE);
                holder.mMessageSmsIv.setVisibility(View.VISIBLE);
                holder.mMessageEmailIv.setVisibility(View.VISIBLE);
            }
        });

        setBgColor(holder.mCallIv, lead_call_btn_bg_color);
        setBgColor(holder.mMessageIv, lead_msg_btn_bg_color);

        holder.mAvatarV.setLeadAvatar(lead);
        setDeadline(holder, lead);
        holder.mNameTxt.setText(lead.getFullFormattedName());
        holder.mStatusTxt.setText(lead.address);
        holder.mStatusTxt.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, lead.isFinancing ? R.drawable.ic_buyer_financing : R.drawable.ic_buyer_cash), null, null, null);

        if (lead.unreadNotificationCount > 0) {
            holder.mNotificationCountTxt.setText(String.valueOf(lead.unreadNotificationCount));
            holder.mNotificationCountTxt.setVisibility(View.VISIBLE);
        } else {
            holder.mNotificationCountTxt.setVisibility(View.INVISIBLE);
        }

        holder.setProportionalSwipeAmountModeEnabled(false);
        holder.setMaxRightSwipeAmount(0);
        holder.setMaxLeftSwipeAmount(-maxSwipeWidthCurrent);
        holder.setSwipeItemHorizontalSlideAmount(lead.isPinned() ? -maxSwipeWidthCurrent : 0);

        lead.setPinned(false);
    }

    private void setClickListenerToView(View view, LeadItem lead) {
        view.setTag(lead);
        view.setOnClickListener(this);
    }

    private void setBgColor(View view, @ColorRes int color) {
        Drawable circleDrawable = ContextCompat.getDrawable(context, R.drawable.circle);
        Drawable msgBg = circleDrawable.getConstantState().newDrawable();
        msgBg.setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_ATOP);
        view.setBackground(msgBg);
    }

    private void animateShowLeadConnectBtn(View view) {
        view.setVisibility(View.VISIBLE);

        AnimatorSet as = new AnimatorSet();
        as.playTogether(
                ObjectAnimator.ofFloat(view, "scaleX", 0.5f, 1),
                ObjectAnimator.ofFloat(view, "scaleY", 0.5f, 1),
                ObjectAnimator.ofFloat(view, "alpha", 0.5f, 1));
        as.setDuration(300);
        as.start();
    }

    private void setDeadline(LeadsHolder holder, LeadItem lead) {
        int typeStyle = Typeface.NORMAL;
        String deadlineText = String.format("%s days", Math.abs(lead.deadlineDate));
        int deadlineColor = ContextCompat.getColor(context, R.color.lead_deadline_upnext_color);
        if (lead.deadlineDate < 0) {
            typeStyle = Typeface.BOLD;
            deadlineColor = ContextCompat.getColor(context, R.color.lead_deadline_expired_color);
        } else if (lead.deadlineDate < 2) {
            deadlineText = "Due today";
            deadlineColor = ContextCompat.getColor(context, R.color.lead_deadline_today_color);
        }
        holder.mDeadLineDateTxt.setTypeface(null, typeStyle);
        holder.mDeadLineDateTxt.setTextColor(deadlineColor);
        holder.mDeadLineDateTxt.setText(deadlineText);
    }

    @Override
    public int getItemCount() {
        return leads.size();
    }

    @Override
    public int onGetSwipeReactionType(LeadsHolder holder, int position, int x, int y) {
        return Swipeable.REACTION_CAN_SWIPE_BOTH_H;
    }

    @Override
    public void onSetSwipeBackground(LeadsHolder holder, int position, int type) {
    }

    @Override
    public SwipeResultAction onSwipeItem(LeadsHolder holder, int position, int result) {
        switch (result) {
            case Swipeable.RESULT_SWIPED_LEFT:
                return new ItemSwipeResultAction(this, position);
            case Swipeable.RESULT_SWIPED_RIGHT:
            case Swipeable.RESULT_CANCELED:
            default:
                if (position != RecyclerView.NO_POSITION) {
                    return new SwipeResultActionDefault();
                } else {
                    return null;
                }
        }
    }

    private static class ItemSwipeResultAction extends SwipeResultActionMoveToSwipedDirection {
        private LeadsAdapter mAdapter;
        private final int mPosition;

        ItemSwipeResultAction(LeadsAdapter adapter, int position) {
            mAdapter = adapter;
            mPosition = position;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();

            LeadItem lead = mAdapter.leads.get(mPosition);
            if (!lead.isPinned()) {
                lead.setPinned(true);
                mAdapter.notifyItemChanged(mPosition);
            }
        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            mAdapter = null;
        }
    }

    public class LeadsHolder extends AbstractSwipeableItemViewHolder {
        private RelativeLayout mContainer;
        private LeadAvatarView mAvatarV;
        private TextView mNameTxt;
        private TextView mStatusTxt;
        private TextView mDeadLineDateTxt;
        private TextView mNotificationCountTxt;
        private View mContainerBgView;

        private View mCallIv;
        private View mCallSystemIv;
        private View mCallTwillioIv;
        private View mMessageIv;
        private View mMessageNoteIv;
        private View mMessageEmailIv;
        private View mMessageSmsIv;

        public LeadsHolder(View v) {
            super(v);
            mContainer = (RelativeLayout) v.findViewById(R.id.container);
            mAvatarV = (LeadAvatarView) v.findViewById(R.id.view_leadAvatar);
            mNameTxt = (TextView) v.findViewById(R.id.tv_leadName);
            mStatusTxt = (TextView) v.findViewById(R.id.tv_leadAddress);
            mDeadLineDateTxt = (TextView) v.findViewById(R.id.tv_leadDate);
            mNotificationCountTxt = (TextView) v.findViewById(R.id.tv_leadNotificationsCounter);
            mContainerBgView = v.findViewById(R.id.leadContainerBg);

            mCallIv = v.findViewById(R.id.iv_leadCall);
            mCallSystemIv = v.findViewById(R.id.view_leadCall_system);
            mCallTwillioIv = v.findViewById(R.id.view_leadCall_twillio);
            mMessageIv = v.findViewById(R.id.iv_leadMessage);
            mMessageNoteIv = v.findViewById(R.id.view_leadMsg_note);
            mMessageEmailIv = v.findViewById(R.id.view_leadMsg_email);
            mMessageSmsIv = v.findViewById(R.id.view_leadMsg_sms);
        }

        @Override
        public View getSwipeableContainerView() {
            return mContainer;
        }

        @Override
        public void onSlideAmountUpdated(float horizontalAmount, float verticalAmount, boolean isSwiping) {
            updateContainerBg(horizontalAmount);
            updateBtnScale(horizontalAmount, isSwiping);
        }

        private void updateContainerBg(float horizontalAmount) {
            float colorFraction = maxSwipeWidthCurrent / BACKGROUND_COLOR_FRACTION_COUNT;
            int colorIdx = (int) (Math.abs(horizontalAmount) / colorFraction);
            int color = ViewUtils.getColorFromGradient(colors, positions, colorIdx);
            mContainerBgView.setBackgroundColor(color);
        }

        private void updateBtnScale(float horizontalAmount, boolean isSwiping) {
            if (Math.abs(horizontalAmount) > 0 && Math.abs(horizontalAmount) < maxSwipeWidthCurrent) {
                float scaleFactor = Math.abs(horizontalAmount) / maxSwipeWidthCurrent;
                if (scaleFactor < 0.3) {
                    maxSwipeWidthCurrent = maxSwipeWidth2Buttons;

                    mCallIv.setVisibility(View.VISIBLE);
                    mMessageIv.setVisibility(View.VISIBLE);

                    mCallSystemIv.setVisibility(View.GONE);
                    mCallTwillioIv.setVisibility(View.GONE);
                    mMessageNoteIv.setVisibility(View.GONE);
                    mMessageSmsIv.setVisibility(View.GONE);
                    mMessageEmailIv.setVisibility(View.GONE);
                }
                scaleView(mCallIv, scaleFactor);
                scaleView(mCallSystemIv, scaleFactor);
                scaleView(mCallTwillioIv, scaleFactor);
                scaleView(mMessageIv, scaleFactor);

                scaleView(mMessageNoteIv, scaleFactor);
                scaleView(mMessageSmsIv, scaleFactor);
                scaleView(mMessageEmailIv, scaleFactor);
            }
        }

        private void scaleView(View view, float scaleFactor) {
            if (view.getVisibility() == View.VISIBLE) {
                ViewCompat.setScaleX(view, scaleFactor);
                ViewCompat.setScaleY(view, scaleFactor);
                ViewCompat.setAlpha(view, scaleFactor);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (onClickListener != null) {
            LeadItem lead = (LeadItem) v.getTag();
            switch (v.getId()) {
                case R.id.container:
                    onClickListener.onItemClickListener(lead);
                    break;
                case R.id.view_leadCall_system:
                    onClickListener.onCallSystemClickListener(lead);
                    break;
                case R.id.view_leadCall_twillio:
                    onClickListener.onCallTwillioClickListener(lead);
                    break;
                case R.id.view_leadMsg_email:
                    onClickListener.onMessageEmailClickListener(lead);
                    break;
                case R.id.view_leadMsg_note:
                    onClickListener.onMessageNoteClickListener(lead);
                    break;
                case R.id.view_leadMsg_sms:
                    onClickListener.onMessageSMSClickListener(lead);
                    break;
            }
        }
    }

}