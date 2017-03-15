package com.loomlogic.leads;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultAction;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionDefault;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionMoveToSwipedDirection;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractSwipeableItemViewHolder;
import com.loomlogic.R;
import com.loomlogic.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandr on 11/19/15.
 */
public class LeadsAdapter extends RecyclerView.Adapter<LeadsAdapter.LeadsHolder> implements SwipeableItemAdapter<LeadsAdapter.LeadsHolder>, View.OnClickListener {

    private interface Swipeable extends SwipeableItemConstants {
    }

    private List<LeadItem> leads;
    private Context context;
    private float maxSwipeWidth;

    private OnLeadClickListener onClickListener;

    interface OnLeadClickListener {
        void onItemClickListener(LeadItem item);

        void onMessageClickListener(LeadItem item);

        void onCallClickListener(LeadItem item);
    }

    public LeadsAdapter(Context context, OnLeadClickListener eventListener) {
        this.context = context;
        maxSwipeWidth = context.getResources().getDimension(R.dimen.lead_swipe_action_button_width) * 2;

        this.onClickListener = eventListener;
        this.leads = new ArrayList<>();

        // SwipeableItemAdapter requires stable ID, and also
        // have to implement the getItemId() method appropriately.
        setHasStableIds(true);
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
    public void onBindViewHolder(LeadsHolder holder, int position) {
        LeadItem lead = leads.get(position);
        holder.mContainer.setTag(lead);
        holder.mContainer.setOnClickListener(this);

        holder.mCallIv.setTag(lead);
        holder.mCallIv.setOnClickListener(this);

        holder.mMessageIv.setTag(lead);
        holder.mMessageIv.setOnClickListener(this);

        setAvatar(holder, lead);
        setDeadline(holder, lead);
        setEscrowStatusBg(holder, lead);
        holder.mNameTxt.setText(String.format("%s %s", lead.firstName, lead.lastName));
        holder.mStatusTxt.setText(lead.address);
        holder.mStatusTxt.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, lead.isFinancing ? R.drawable.ic_buyer_financing : R.drawable.ic_buyer_cash), null, null, null);

        if (lead.unreadNotificationCount > 0) {
            holder.mNotificationCountTxt.setText(String.valueOf(lead.unreadNotificationCount));
            holder.mNotificationCountTxt.setVisibility(View.VISIBLE);
        } else {
            holder.mNotificationCountTxt.setVisibility(View.INVISIBLE);
        }

        holder.setProportionalSwipeAmountModeEnabled(false);
        holder.setMaxLeftSwipeAmount(-maxSwipeWidth);
        holder.setMaxRightSwipeAmount(0);
        holder.setSwipeItemHorizontalSlideAmount(lead.isPinned() ? -maxSwipeWidth : 0);
    }

    private void setAvatar(LeadsHolder holder, LeadItem lead) {
        if (Utils.isUrlValid(lead.avatarUrl)) {
            Picasso.with(context)
                    .load(lead.avatarUrl)
                    .into(holder.mAvatarIv);
            holder.mInitialsTxt.setVisibility(View.GONE);
        } else {
            holder.mInitialsTxt.setVisibility(View.VISIBLE);
            holder.mInitialsTxt.setText(String.format("%s%s", !TextUtils.isEmpty(lead.firstName) ? lead.firstName.charAt(0) : "", !TextUtils.isEmpty(lead.lastName) ? lead.lastName.charAt(0) : ""));
            int color;
            switch (lead.gender) {
                case MALE:
                    color = ContextCompat.getColor(context, R.color.lead_gender_male_color);
                    break;
                case FEMALE:
                    color = ContextCompat.getColor(context, R.color.lead_gender_female_color);
                    break;
                case NONE:
                    color = ContextCompat.getColor(context, R.color.lead_gender_none_color);
                    break;
                default:
                    color = ContextCompat.getColor(context, R.color.lead_gender_none_color);
            }
            holder.mAvatarIv.setBackgroundColor(color);
            holder.mAvatarIv.setImageDrawable(null);
        }
    }

    private void setDeadline(LeadsHolder holder, LeadItem lead) {
        String deadlineText = String.format("%s days", Math.abs(lead.deadlineDate));
        int deadlineColor = ContextCompat.getColor(context, R.color.lead_deadline_upnext_color);
        if (lead.deadlineDate < 0) {
            deadlineColor = ContextCompat.getColor(context, R.color.lead_deadline_expired_color);
        } else if (lead.deadlineDate < 2) {
            deadlineText = "Due today";
            deadlineColor = ContextCompat.getColor(context, R.color.lead_deadline_today_color);
        }
        holder.mDeadLineDateTxt.setTextColor(deadlineColor);
        holder.mDeadLineDateTxt.setText(deadlineText);
    }

    private void setEscrowStatusBg(LeadsHolder holder, LeadItem lead) {
        holder.mEscrowStatusBgView.setBackground(ContextCompat.getDrawable(context, lead.isFinancing ? R.drawable.lead_escrow_status_financing_bg : R.drawable.lead_escrow_status_cash_bg));

        int maxStatusCount = lead.isFinancing ? 7 : 2;
        int currentStatusWidth = lead.statusCount * Utils.getDisplayWidth(context) / maxStatusCount;
        holder.mEscrowStatusBgView.setLayoutParams(new RelativeLayout.LayoutParams(currentStatusWidth, RelativeLayout.LayoutParams.MATCH_PARENT));
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
                    return new UnpinResultAction(this, position);
                } else {
                    return null;
                }
        }
    }

    private static class ItemSwipeResultAction extends SwipeResultActionMoveToSwipedDirection {
        private LeadsAdapter mAdapter;
        private final int mPosition;
        private boolean mSetPinned;

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
                mSetPinned = true;
            }
        }

        @Override
        protected void onSlideAnimationEnd() {
            super.onSlideAnimationEnd();
            if (mSetPinned) {
                LeadItem lead = mAdapter.leads.get(mPosition);
                lead.setPinned(false);
            }
        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            mAdapter = null;
        }
    }

    private static class UnpinResultAction extends SwipeResultActionDefault {
        private LeadsAdapter mAdapter;
        private final int mPosition;

        UnpinResultAction(LeadsAdapter adapter, int position) {
            mAdapter = adapter;
            mPosition = position;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();

            LeadItem lead = mAdapter.leads.get(mPosition);
            if (lead.isPinned()) {
                lead.setPinned(false);
                mAdapter.notifyItemChanged(mPosition);
            }
        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            // clear the references
            mAdapter = null;
        }
    }

    public class LeadsHolder extends AbstractSwipeableItemViewHolder {
        private RelativeLayout mContainer;
        private ImageView mAvatarIv;
        private TextView mNameTxt;
        private TextView mInitialsTxt;
        private TextView mStatusTxt;
        private TextView mDeadLineDateTxt;
        private TextView mNotificationCountTxt;
        private View mEscrowStatusBgView;

        private View mCallIv;
        private View mMessageIv;


        public LeadsHolder(View v) {
            super(v);
            mContainer = (RelativeLayout) v.findViewById(R.id.container);
            mAvatarIv = (ImageView) v.findViewById(R.id.iv_leadImage);
            mNameTxt = (TextView) v.findViewById(R.id.tv_leadName);
            mInitialsTxt = (TextView) v.findViewById(R.id.tv_leadInitials);
            mStatusTxt = (TextView) v.findViewById(R.id.tv_leadStatus);
            mDeadLineDateTxt = (TextView) v.findViewById(R.id.tv_leadDate);
            mNotificationCountTxt = (TextView) v.findViewById(R.id.tv_leadNotificationsCounter);
            mEscrowStatusBgView = v.findViewById(R.id.leadEscrowStatusBg);

            mCallIv = v.findViewById(R.id.iv_leadCall);
            mMessageIv = v.findViewById(R.id.iv_leadMessage);
        }

        @Override
        public View getSwipeableContainerView() {
            return mContainer;
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
                case R.id.iv_leadCall:
                    onClickListener.onCallClickListener(lead);
                    break;
                case R.id.iv_leadMessage:
                    onClickListener.onMessageClickListener(lead);
                    break;
            }
        }
    }
}