package com.loomlogic.leads.menu;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.loomlogic.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 4/6/17.
 */

public class LeadMenuFilterAdapter extends BaseAdapter {
    private List<LeadFilterItem> itemList;

    public LeadMenuFilterAdapter() {
        itemList = new ArrayList<>();
        itemList.add(new LeadFilterItem(R.drawable.ic_lead_filter_my, R.string.lead_menu_filter_my));
        itemList.add(new LeadFilterItem(R.drawable.ic_lead_filter_team, R.string.lead_menu_filter_team));
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_lead_menu_filter, null);
        }
        Drawable drawable = ContextCompat.getDrawable(parent.getContext(), itemList.get(position).getIconRes());
        drawable.mutate();
        drawable.setTint(Color.WHITE);

        TextView tv = (TextView) convertView.findViewById(R.id.tv_leadFilterName);
        tv.setText(itemList.get(position).getNameRes());
        tv.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        tv.setTextColor(Color.WHITE);
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_lead_menu_filter, null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.tv_leadFilterName);
        tv.setText(itemList.get(position).getNameRes());
        tv.setCompoundDrawablesWithIntrinsicBounds(itemList.get(position).getIconRes(), 0, 0, 0);

        convertView.findViewById(R.id.view_divider).setVisibility(position == getCount() - 1 ? View.GONE : View.VISIBLE);
        return convertView;
    }
}
