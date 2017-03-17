package com.loomlogic.leads.menu;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by alex on 2/28/17.
 */

public class LeadsTypeAdapter extends PagerAdapter {
    private Context context;

    public LeadsTypeAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 2;
    }


    @Override
    public Object instantiateItem(ViewGroup parent, int position) {
        RecyclerView list = new RecyclerView(context);
        initList(list, position);
        ((ViewPager) parent).addView(list);
        return list;
    }

    private void initList(RecyclerView recyclerView, int position) {

        LeadRole role = position == 0 ? LeadRole.BUYER : LeadRole.SELLER;
        LeadsMenuAdapter adapter = new LeadsMenuAdapter(getFakeMenuItems(role));

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<LeadMenuItem> getFakeMenuItems(LeadRole role) {
        ArrayList<LeadMenuItem> items = new ArrayList<>();
        if (role == LeadRole.BUYER) {
            items.add(new LeadMenuItem(LeadTypes.LEADS, 12, role));
            items.add(new LeadMenuItem(LeadTypes.LENDER, 0, role));
            items.add(new LeadMenuItem(LeadTypes.SHOPPING, 22, role));
            items.add(new LeadMenuItem(LeadTypes.CONTRACT, 32, role));
            items.add(new LeadMenuItem(LeadTypes.CLOSED, 7, role));
        } else {
            items.add(new LeadMenuItem(LeadTypes.LEADS, 12, role));
            items.add(new LeadMenuItem(LeadTypes.COMING_SOON, 0, role));
            items.add(new LeadMenuItem(LeadTypes.ACTIVE, 22, role));
            items.add(new LeadMenuItem(LeadTypes.WITHDRAW, 32, role));
            items.add(new LeadMenuItem(LeadTypes.PENDING, 1, role));
            items.add(new LeadMenuItem(LeadTypes.CLOSED, 7, role));
        }
        return items;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        ((ViewPager) collection).removeView((RecyclerView) view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RecyclerView) object);
    }
}
