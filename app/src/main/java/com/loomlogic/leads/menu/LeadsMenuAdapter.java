package com.loomlogic.leads.menu;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loomlogic.R;
import com.loomlogic.base.LoomLogicApp;

import java.util.ArrayList;

/**
 * Created by alex on 3/1/17.
 */

public class LeadsMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    interface Listener {
        void onItemClickListener(LeadMenuItem item);
    }

    private ArrayList<LeadMenuItem> items;
    private boolean isSellers;
    private Listener listener;

    public LeadsMenuAdapter(ArrayList<LeadMenuItem> items, boolean isSellers, Listener listener) {
        this.items = items;
        this.isSellers = isSellers;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new LeadMenuItemViewHolder(inflater.inflate(R.layout.item_lead_menu, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
        LeadMenuItemViewHolder holder = (LeadMenuItemViewHolder) h;
        final LeadMenuItem item = items.get(position);
        holder.txtName.setText(item.getName());
        holder.txtCount.setText(String.valueOf(item.getCount()));
        if (isSellers) {
            int height = LoomLogicApp.getSharedContext().getResources().getDimensionPixelSize(R.dimen.lead_menu_seller_item_height);
            holder.layoutRoot.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        }

        holder.layoutRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClickListener(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class LeadMenuItemViewHolder extends RecyclerView.ViewHolder {

        private View layoutRoot;
        private TextView txtName;
        private TextView txtCount;

        LeadMenuItemViewHolder(View v) {
            super(v);

            layoutRoot = (View) v.findViewById(R.id.containter_leadMenuItem);
            txtName = (TextView) v.findViewById(R.id.tv_leadMenuItem_name);
            txtCount = (TextView) v.findViewById(R.id.tv_leadMenuItem_count);
        }
    }
}