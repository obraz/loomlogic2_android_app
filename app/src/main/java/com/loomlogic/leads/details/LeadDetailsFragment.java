package com.loomlogic.leads.details;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loomlogic.R;
import com.loomlogic.home.BaseHomeFragment;
import com.loomlogic.leads.LeadItem;

/**
 * Created by alex on 3/14/17.
 */

public class LeadDetailsFragment extends BaseHomeFragment implements TabLayout.OnTabSelectedListener, Toolbar.OnMenuItemClickListener {
    public static LeadDetailsFragment newInstance(LeadItem item) {
        LeadDetailsFragment fragment = new LeadDetailsFragment();
        Bundle args = new Bundle();
        // args.putString(ARG_ITEM, item);
        fragment.setArguments(args);
        return fragment;
    }

    private TabLayout mTabLayout;

    Runnable mTabLayout_config = new Runnable() {
        @Override
        public void run() {
            if (mTabLayout.getWidth() < getHomeActivity().getResources().getDisplayMetrics().widthPixels) {
                mTabLayout.setTabMode(TabLayout.MODE_FIXED);
                ViewGroup.LayoutParams mParams = mTabLayout.getLayoutParams();
                mParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                mTabLayout.setLayoutParams(mParams);
            } else {
                mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lead_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initToolbar(view);

    }

    private void initViews(View view) {
        mTabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(getViewForTab(R.string.lead_details_tab_details, 0)));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(getViewForTab(R.string.lead_details_tab_messages, 10)));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(getViewForTab(R.string.lead_details_tab_dates, 0)));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(getViewForTab(R.string.lead_details_tab_tasks, 0)));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(getViewForTab(R.string.lead_details_tab_documents, 0)));
        mTabLayout.addOnTabSelectedListener(this);
        mTabLayout.post(mTabLayout_config);

        onTabSelected(mTabLayout.getTabAt(0));
    }

    private View getViewForTab(@StringRes int tabName, int notifCount) {
        View customTabView = LayoutInflater.from(getContext()).inflate(R.layout.tab_with_bage_view, null);

        TextView name = (TextView) customTabView.findViewById(R.id.tv_bageName);
        name.setText(tabName);

        if (notifCount > 0) {
            TextView counter = (TextView) customTabView.findViewById(R.id.tv_bageCounter);
            counter.setVisibility(View.VISIBLE);
            counter.setText("20000");
        }
        return customTabView;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        updateTabTextColor(tab, R.color.white);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        updateTabTextColor(tab, R.color.lead_detail_tabText_color);
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void updateTabTextColor(TabLayout.Tab tab, @ColorRes int color) {
        TextView name = (TextView) tab.getCustomView().findViewById(R.id.tv_bageName);
        name.setTextColor(ContextCompat.getColor(getContext(), color));
    }


    private void initToolbar(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("John Doussing");
        toolbar.inflateMenu(R.menu.menu_lead_details);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHomeActivity().onBackPressed();
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_one:
                getHomeActivity().showErrorSnackBar("action 1");
                break;
            case R.id.action_two:
                getHomeActivity().showErrorSnackBar("action 2");
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
