package com.loomlogic.leads.list;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
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
import com.loomlogic.leads.base.LeadAvatarView;
import com.loomlogic.leads.base.LeadData;
import com.loomlogic.leads.base.LeadSubStage;
import com.loomlogic.leads.base.LeadUtils;
import com.loomlogic.leads.entity.LeadItem;

import java.util.ArrayList;
import java.util.List;

import static com.loomlogic.R.color.lead_call_btn_bg_color;
import static com.loomlogic.R.color.lead_msg_btn_bg_color;
import static com.loomlogic.utils.ViewUtils.animViewBgColor;

/**
 * Created by Alexandr on 11/19/15.
 */
public class LeadsAdapter extends RecyclerView.Adapter<LeadsAdapter.LeadsHolder> implements SwipeableItemAdapter<LeadsAdapter.LeadsHolder>, View.OnClickListener {

    private interface Swipeable extends SwipeableItemConstants {
    }

    private LeadData leadData;
    private List<LeadItem> leads;
    private Context context;
    private float widthButton;
    private float maxSwipeWidthCurrent;
    private float maxSwipeWidth2Buttons;
    private float maxSwipeWidth3Buttons;
    private static int colorSwipeBg;

    private static final int BACKGROUND_COLOR_ANIMATION_TIME = 500;

    private boolean onMessageClick;
    private OnLeadClickListener onClickListener;

    interface OnLeadClickListener {
        void onItemClickListener(LeadItem item);

        void onMessageEmailClickListener(LeadItem item);

        void onMessageNoteClickListener(LeadItem item);

        void onMessageSMSClickListener(LeadItem item);

        void onCallSystemClickListener(LeadItem item);

        void onCallTwillioClickListener(LeadItem item);
    }

    public LeadsAdapter(Context context, LeadData leadData, OnLeadClickListener eventListener) {
        this.context = context;
        this.leadData = leadData;
        float paddings = context.getResources().getDimension(R.dimen.lead_swipe_action_button_padding) * 2;
        widthButton = context.getResources().getDimension(R.dimen.lead_swipe_action_button_width);
        maxSwipeWidth2Buttons = widthButton * 2 + paddings;
        maxSwipeWidth3Buttons = widthButton * 3 + paddings;
        maxSwipeWidthCurrent = maxSwipeWidth2Buttons;

        this.onClickListener = eventListener;
        this.leads = new ArrayList<>();

        // SwipeableItemAdapter requires stable ID, and also
        // have to implement the getItemId() method appropriately.
        setHasStableIds(true);

        colorSwipeBg = ContextCompat.getColor(context, R.color.lead_btn_container_bg_color);
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
        setCallbacks(holder, position);

        final LeadItem lead = leads.get(position);

        setActionButtonBgColor(holder.mCallIv, lead_call_btn_bg_color);
        setActionButtonBgColor(holder.mMessageIv, lead_msg_btn_bg_color);

        holder.mAvatarV.setLeadAvatar(lead);
        holder.mNameTxt.setText(lead.getFullFormattedName());
        setLeadNotificationsCount(holder, lead);

        switch (leadData.getStatus()) {
            case LEADS:
                setDeadline(holder, lead);
                holder.mInfoTxt.setText(lead.source);

                if (leadData.getSubStage() == LeadSubStage.ALL || leadData.getSubStage() == LeadSubStage.NEW || leadData.getSubStage() == LeadSubStage.ENGAGED) {
                    setLeadQuality(holder, lead);
                    if (position == 3) {
                        holder.mInfoTxt.setText("Gilbert Patterson");
                        holder.mInfoTxt.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable.ic_buyer_financing), null, ContextCompat.getDrawable(context, R.drawable.ic_lead_new_pre_approved), null);
                    }
                } else if (leadData.getSubStage() == LeadSubStage.FUTURE) {
                    holder.mDeadLineDateTxt.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable.ic_lead_future_call), null, null, null);
                }

                break;
            case LENDER:
                if (leadData.getSubStage() == LeadSubStage.APPOINTMENT) {
                    holder.mDeadLineDateTxt.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable.ic_lead_appointment_calendar), null, null, null);
                } else {
                    setLeadQuality(holder, lead);
                }
                setDeadline(holder, lead);
                holder.mInfoTxt.setText(lead.lenderName);
                holder.mInfoTxt.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable.ic_buyer_financing), null, null, null);
                break;
            case SHOPPING:
                setDeadline(holder, lead);
                holder.mInfoTxt.setText(String.format("%s - %s", lead.price, lead.loanType));
                break;
            case CONTRACT:
                holder.mAvatarV.setEscrowStatus(lead);
                setDeadline(holder, lead);
                holder.mInfoTxt.setText(lead.address);
                holder.mInfoTxt.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, lead.isFinancing ? R.drawable.ic_buyer_financing : R.drawable.ic_buyer_cash), null, null, null);
                break;
            case CLOSED:
                setDeadline(holder, lead);
                holder.mInfoTxt.setText(lead.lenderName);
                holder.mInfoTxt.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, lead.isFinancing ? R.drawable.ic_buyer_financing : R.drawable.ic_buyer_cash), null, null, null);
                break;
        }

        if (!lead.isPinned() && ((ColorDrawable) holder.mContainerBgView.getBackground()).getColor() == colorSwipeBg) {
            animViewBgColor(holder.mContainerBgView, colorSwipeBg, Color.WHITE, BACKGROUND_COLOR_ANIMATION_TIME);
        }

        holder.setProportionalSwipeAmountModeEnabled(false);
        holder.setMaxRightSwipeAmount(0);
        holder.setMaxLeftSwipeAmount(-maxSwipeWidthCurrent);
        holder.setSwipeItemHorizontalSlideAmount(lead.isPinned() ? -maxSwipeWidthCurrent : 0);

        lead.setPinned(false);
    }

    private void setLeadNotificationsCount(LeadsHolder holder, LeadItem lead) {
        if (lead.unreadNotificationCount > 0) {
            holder.mNotificationCountTxt.setText(String.valueOf(lead.unreadNotificationCount));
            holder.mNotificationCountTxt.setVisibility(View.VISIBLE);
        } else {
            holder.mNotificationCountTxt.setVisibility(View.INVISIBLE);
        }
    }

    private void setLeadQuality(LeadsHolder holder, LeadItem lead) {
        if (lead.quality != null) {
            holder.mQualityTxt.setVisibility(View.VISIBLE);
            holder.mQualityTxt.setText(lead.quality);
            holder.mQualityTxt.setBackground(LeadUtils.getLeadQuality(lead.quality));
        } else {
            holder.mQualityTxt.setVisibility(View.GONE);
        }
    }

    private void setCallbacks(final LeadsHolder holder, final int position) {
        final LeadItem lead = leads.get(position);
        lead.leadData = leadData;

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

                animateShowLeadConnectBtn(holder.mCallTwillioIv, 1);
                animateShowLeadConnectBtn(holder.mCallSystemIv, 2);
            }
        });

        holder.mMessageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMessageClick = true;
                lead.setPinned(true);
                maxSwipeWidthCurrent = maxSwipeWidth3Buttons;
                notifyItemChanged(position);

                holder.mMessageIv.setVisibility(View.GONE);
                holder.mCallIv.setVisibility(View.GONE);

                animateShowLeadConnectBtn(holder.mMessageSmsIv, 1);
                animateShowLeadConnectBtn(holder.mMessageEmailIv, 2);
                animateShowLeadConnectBtn(holder.mMessageNoteIv, 3);
            }
        });
    }

    private void setClickListenerToView(View view, LeadItem lead) {
        view.setTag(lead);
        view.setOnClickListener(this);
    }

    private void setActionButtonBgColor(View view, @ColorRes int color) {
        Drawable circleDrawable = ContextCompat.getDrawable(context, R.drawable.circle);
        Drawable msgBg = circleDrawable.getConstantState().newDrawable();
        msgBg.setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_ATOP);
        view.setBackground(msgBg);
    }

    private void animateShowLeadConnectBtn(View view, int btnCountFromRight) {
        view.setVisibility(View.VISIBLE);

        AnimatorSet as = new AnimatorSet();
        as.playTogether(
                ObjectAnimator.ofFloat(view, "scaleX", 0.5f, 1),
                ObjectAnimator.ofFloat(view, "scaleY", 0.5f, 1),
                ObjectAnimator.ofFloat(view, "translationX", -widthButton * btnCountFromRight),
                ObjectAnimator.ofFloat(view, "alpha", 0.5f, 1));
        as.setDuration(300);
        as.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                onMessageClick = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        as.start();
    }

    private void setDeadline(LeadsHolder holder, LeadItem lead) {
        int typeStyle = Typeface.NORMAL;
        String deadlineText = String.format("%s days", Math.abs(lead.deadlineDate));
        int deadlineColor = ContextCompat.getColor(context, R.color.lead_deadline_upnext_color);
        if (lead.deadlineDate < 0) {
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
                return new ItemSwipeResultAction(this, holder, position);
            case Swipeable.RESULT_SWIPED_RIGHT:
            case Swipeable.RESULT_CANCELED:
            default:
                if (position != RecyclerView.NO_POSITION) {
                    return new ItemUnpinSwipeResultAction(holder);
                } else {
                    return null;
                }
        }
    }

    private static class ItemSwipeResultAction extends SwipeResultActionMoveToSwipedDirection {
        private LeadsAdapter mAdapter;
        private LeadsHolder mHolder;
        private final int mPosition;

        ItemSwipeResultAction(LeadsAdapter adapter, LeadsHolder holder, int position) {
            mAdapter = adapter;
            mHolder = holder;
            mPosition = position;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();

            LeadItem lead = mAdapter.leads.get(mPosition);
            if (!lead.isPinned()) {
                lead.setPinned(true);
                mAdapter.notifyItemChanged(mPosition);
                animViewBgColor(mHolder.mContainerBgView, Color.WHITE, colorSwipeBg, BACKGROUND_COLOR_ANIMATION_TIME);
            }
        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            mAdapter = null;
        }
    }

    private static class ItemUnpinSwipeResultAction extends SwipeResultActionDefault {
        private LeadsHolder mHolder;

        ItemUnpinSwipeResultAction(LeadsHolder holder) {
            mHolder = holder;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();
            animViewBgColor(mHolder.mContainerBgView, colorSwipeBg, Color.WHITE, BACKGROUND_COLOR_ANIMATION_TIME);
        }
    }

    public class LeadsHolder extends AbstractSwipeableItemViewHolder {
        private RelativeLayout mContainer;
        private LeadAvatarView mAvatarV;
        private TextView mQualityTxt;
        private TextView mNameTxt;
        private TextView mInfoTxt;
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
            mQualityTxt = (TextView) v.findViewById(R.id.tv_leadQuality);
            mNameTxt = (TextView) v.findViewById(R.id.tv_leadName);
            mInfoTxt = (TextView) v.findViewById(R.id.tv_leadInfo);
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
            updateBtnScale(horizontalAmount);
        }

        private void updateBtnScale(float horizontalAmount) {
            float scaleFactor = 1;
            if (Math.abs(horizontalAmount) > 0 && Math.abs(horizontalAmount) < maxSwipeWidthCurrent) {
                scaleFactor = Math.abs(horizontalAmount) / maxSwipeWidthCurrent;
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

                scaleView(mCallIv, scaleFactor, 2);
                scaleView(mCallTwillioIv, scaleFactor, 1);
                scaleView(mCallSystemIv, scaleFactor, 2);
                scaleView(mMessageIv, scaleFactor, 1);
                if (!onMessageClick) {
                    scaleView(mMessageSmsIv, scaleFactor, 1);
                    scaleView(mMessageEmailIv, scaleFactor, 2);
                    scaleView(mMessageNoteIv, scaleFactor, 3);
                }
            }
        }

        private void scaleView(View view, float scaleFactor, int btnCountFromRight) {
            if (view.getVisibility() == View.VISIBLE) {
                ViewCompat.setScaleX(view, scaleFactor);
                ViewCompat.setScaleY(view, scaleFactor);
                ViewCompat.setTranslationX(view, -scaleFactor * widthButton * btnCountFromRight);
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