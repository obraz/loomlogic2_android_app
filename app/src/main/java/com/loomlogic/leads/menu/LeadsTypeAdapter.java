package com.loomlogic.leads.menu;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.loomlogic.leads.base.LeadData;
import com.loomlogic.leads.base.LeadOwner;
import com.loomlogic.leads.base.LeadStatus;
import com.loomlogic.leads.base.LeadType;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by alex on 2/28/17.
 */

public class LeadsTypeAdapter extends PagerAdapter {
    private Context context;
    private LeadOwner leadOwner;

    public LeadsTypeAdapter(Context context, LeadOwner leadOwner) {
        this.context = context;
        this.leadOwner = leadOwner;
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
        LeadsStatusMenuAdapter adapter = new LeadsStatusMenuAdapter(getMenuItems(position));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<LeadMenuItem> getMenuItems(int position) {
        ArrayList<LeadMenuItem> items = new ArrayList<>();
        if (position == LeadsMenuManager.TYPE_BUYER_MENU_POSITION) {
            LeadType type = LeadType.BUYER;

            items.add(new LeadMenuItem(new LeadData(type, leadOwner, LeadStatus.LEADS), new Random().nextInt()));
            items.add(new LeadMenuItem(new LeadData(type, leadOwner, LeadStatus.LENDER), 12));
            items.add(new LeadMenuItem(new LeadData(type, leadOwner, LeadStatus.SHOPPING), 12));
            items.add(new LeadMenuItem(new LeadData(type, leadOwner, LeadStatus.CONTRACT), 12));
            items.add(new LeadMenuItem(new LeadData(type, leadOwner, LeadStatus.CLOSED), 12));
//            items.add(new LeadMenuItem(LeadStatus.LENDER, 0, role));
//            items.add(new LeadMenuItem(LeadStatus.SHOPPING, 22, role));
//            items.add(new LeadMenuItem(LeadStatus.CONTRACT, 32, role));
//            items.add(new LeadMenuItem(LeadStatus.CLOSED, 7, role));
        } else {
            LeadType type = LeadType.SELLER;

            items.add(new LeadMenuItem(new LeadData(type, leadOwner, LeadStatus.LEADS), 12));
            items.add(new LeadMenuItem(new LeadData(type, leadOwner, LeadStatus.COMING_SOON), 12));
            items.add(new LeadMenuItem(new LeadData(type, leadOwner, LeadStatus.ACTIVE), 12));
            items.add(new LeadMenuItem(new LeadData(type, leadOwner, LeadStatus.WITHDRAW), 12));
            items.add(new LeadMenuItem(new LeadData(type, leadOwner, LeadStatus.PENDING), 12));
            items.add(new LeadMenuItem(new LeadData(type, leadOwner, LeadStatus.CLOSED), 12));
//            items.add(new LeadMenuItem(LeadStatus.LEADS, 12, role));
//            items.add(new LeadMenuItem(LeadStatus.COMING_SOON, 0, role));
//            items.add(new LeadMenuItem(LeadStatus.ACTIVE, 22, role));
//            items.add(new LeadMenuItem(LeadStatus.WITHDRAW, 32, role));
//            items.add(new LeadMenuItem(LeadStatus.PENDING, 1, role));
//            items.add(new LeadMenuItem(LeadStatus.CLOSED, 7, role));
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
