package com.loomlogic.leads.create;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.loomlogic.R;
import com.loomlogic.leads.base.LeadAvatarView;
import com.loomlogic.leads.entity.State;

import java.util.List;

/**
 * Created by alex on 05.10.2016.
 */

public class AgentsLendersListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public interface ItemSelectorCallback {
        void onItemSelected(State state);
    }

    // private List<State> states;
    private ItemSelectorCallback callback;

    public AgentsLendersListAdapter(ItemSelectorCallback callback) {
        this.callback = callback;
    }

    public void setStateList(List<State> states) {
        // this.states = states;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_agent_lender, parent, false);
        return new ViewHolderItemsList(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolderItemsList vh = (ViewHolderItemsList) holder;
        // final State state = states.get(position);
        vh.avatarV.setAvatar("", "H", "D");
        vh.nameTv.setText("Hallie Delgado");
        vh.phoneTv.setText("000-856-0986");
        vh.ratingBar.setRating(position/2);

        vh.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 callback.onItemSelected(null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolderItemsList extends RecyclerView.ViewHolder {
        private View container;
        private TextView nameTv;
        private TextView phoneTv;
        private LeadAvatarView avatarV;
        private RatingBar ratingBar;

        public ViewHolderItemsList(View view) {
            super(view);
            container = view;
            nameTv = (TextView) view.findViewById(R.id.tv_name);
            phoneTv = (TextView) view.findViewById(R.id.tv_phone);
            avatarV = (LeadAvatarView) view.findViewById(R.id.avatar);
            ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        }
    }
}
