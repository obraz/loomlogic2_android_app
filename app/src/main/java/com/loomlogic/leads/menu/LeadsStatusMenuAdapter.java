package com.loomlogic.leads.menu;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loomlogic.R;
import com.loomlogic.base.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by alex on 3/1/17.
 */

public class LeadsStatusMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<LeadMenuItem> items;

    public LeadsStatusMenuAdapter(ArrayList<LeadMenuItem> items) {
        this.items = items;
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
        holder.txtName.setCompoundDrawablesWithIntrinsicBounds(item.getDrawableIcon(), null, null, null);
        holder.txtName.setText(item.getName());
        holder.txtCount.setText(String.valueOf(item.getCount()));

        holder.layoutRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MessageEvent(MessageEvent.MessageEventType.LEADS_MENU_SELECT, item.getLead()));
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
