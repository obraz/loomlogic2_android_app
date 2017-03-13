package com.loomlogic.leads.menu;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.loomlogic.R;

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
        ((ViewPager) parent).addView(list, 0);
        return list;
    }

    private void initList(RecyclerView recyclerView, int position) {
        boolean isBuyers = position == 0;
        LeadsMenuAdapter adapter = new LeadsMenuAdapter(getFakeMenuItems(isBuyers), isBuyers);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(context, mLayoutManager.getOrientation());
        mDividerItemDecoration.setDrawable(ContextCompat.getDrawable(context, R.drawable.leads_menu_item_divider));

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(mDividerItemDecoration);
    }

    private ArrayList<LeadMenuItem> getFakeMenuItems(boolean forBuyer) {
        ArrayList<LeadMenuItem> items = new ArrayList<>();
        if (forBuyer) {
            items.add(new LeadMenuItem(LeadTypes.LEADS, 12));
            items.add(new LeadMenuItem(LeadTypes.LENDER, 0));
            items.add(new LeadMenuItem(LeadTypes.SHOPPING, 22));
            items.add(new LeadMenuItem(LeadTypes.CONTRACT, 32));
            items.add(new LeadMenuItem(LeadTypes.CLOSED, 7));
        } else {
            items.add(new LeadMenuItem(LeadTypes.LEADS, 12));
            items.add(new LeadMenuItem(LeadTypes.COMING_SOON, 0));
            items.add(new LeadMenuItem(LeadTypes.ACTIVE, 22));
            items.add(new LeadMenuItem(LeadTypes.WITHDRAW, 32));
            items.add(new LeadMenuItem(LeadTypes.PENDING, 1));
            items.add(new LeadMenuItem(LeadTypes.CLOSED, 7));
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
