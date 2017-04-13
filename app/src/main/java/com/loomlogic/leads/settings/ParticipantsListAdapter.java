package com.loomlogic.leads.settings;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loomlogic.R;
import com.loomlogic.leads.details.view.LeadParticipantAvatarView;
import com.loomlogic.leads.entity.LeadParticipantItem;

import java.util.List;

/**
 * Created by alex on 05.10.2016.
 */

public class ParticipantsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface ParticipantRemoveListener {
        void onParticipantRemove(LeadParticipantItem item);
    }

    public interface ParticipantSelectListener {
        void onParticipantSelect(LeadParticipantItem item);
    }

    private List<LeadParticipantItem> list;
    private ParticipantRemoveListener callbackRemove;
    private ParticipantSelectListener callbackSelect;

    public ParticipantsListAdapter(ParticipantRemoveListener callbackRemove) {
        this.callbackRemove = callbackRemove;
    }

    public ParticipantsListAdapter(ParticipantSelectListener callbackSelect) {
        this.callbackSelect = callbackSelect;
    }

    public void setParticipantsList(List<LeadParticipantItem> listItems) {
        this.list = listItems;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_participant_edit, parent, false);
        return new ViewHolderList(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolderList vh = (ViewHolderList) holder;
        final LeadParticipantItem item = list.get(position);
        vh.name.setText(item.getFullFormattedName());
        vh.avatar.setParticipant(item);
        if (callbackRemove != null) {
            vh.removeBtn.setVisibility(View.VISIBLE);
            vh.removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbackRemove.onParticipantRemove(item);
                }
            });
        }

        if (callbackSelect != null) {
            vh.mainContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbackSelect.onParticipantSelect(item);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolderList extends RecyclerView.ViewHolder {
        private TextView name;
        private LeadParticipantAvatarView avatar;
        private View removeBtn;
        private View mainContainer;

        public ViewHolderList(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.tv_leadParticipantName);
            avatar = (LeadParticipantAvatarView) view.findViewById(R.id.leadParticipantAvatar);
            removeBtn = view.findViewById(R.id.view_leadParticipantRemove);
            mainContainer = view;
        }
    }
}
