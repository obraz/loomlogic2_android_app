package com.loomlogic.leads.create;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loomlogic.R;
import com.loomlogic.leads.entity.State;

import java.util.List;

/**
 * Created by alex on 05.10.2016.
 */

public class StatesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public interface StateSelectorCallback {
        void onStateSelected(State state);
    }

    private List<State> states;
    private StateSelectorCallback callback;

    public StatesListAdapter(StateSelectorCallback callback) {
        this.callback = callback;
    }

    public void setStateList(List<State> states) {
        this.states = states;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_state, parent, false);
        return new ViewHolderStatesList(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolderStatesList vh = (ViewHolderStatesList) holder;
        final State state = states.get(position);
        vh.stateName.setText(state.name);

        vh.stateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onStateSelected(state);
            }
        });
    }

    @Override
    public int getItemCount() {
        return states.size();
    }

    public class ViewHolderStatesList extends RecyclerView.ViewHolder {
        TextView stateName;

        public ViewHolderStatesList(View view) {
            super(view);
            stateName = (TextView) view.findViewById(R.id.tv_stateName);
        }
    }
}
