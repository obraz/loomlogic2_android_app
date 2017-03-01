package com.loomlogic.leads.menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loomlogic.R;
import com.loomlogic.home.BaseHomeFragment;

import java.util.ArrayList;

/**
 * Created by alex on 2/28/17.
 */

public class LeadTypeBuyersFragment extends BaseHomeFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_leads_menu_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view) {
        ArrayList<LeadMenuItem> items = new ArrayList<>();
        items.add(new LeadMenuItem(LeadTypes.LEADS, 12));
        items.add(new LeadMenuItem(LeadTypes.LENDER, 0));
        items.add(new LeadMenuItem(LeadTypes.SHOPPING, 22));
        items.add(new LeadMenuItem(LeadTypes.CONTRACT, 32));
        items.add(new LeadMenuItem(LeadTypes.CLOSED, 7));

        LeadsMenuAdapter adapter = new LeadsMenuAdapter(items, false, new LeadsMenuAdapter.Listener() {
            @Override
            public void onItemClickListener(LeadMenuItem item) {

            }
        });

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(getContext(), mLayoutManager.getOrientation());
        mDividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.leads_menu_item_divider));

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(mDividerItemDecoration);
    }
}
