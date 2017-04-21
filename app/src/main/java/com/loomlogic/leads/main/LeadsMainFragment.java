package com.loomlogic.leads.main;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loomlogic.R;
import com.loomlogic.base.MessageEvent;
import com.loomlogic.home.BaseHomeFragment;
import com.loomlogic.leads.base.LeadData;
import com.loomlogic.leads.base.LeadUtils;
import com.loomlogic.leads.list.LeadsFilterActivity;
import com.loomlogic.leads.menu.LeadsMenuManager;
import com.loomlogic.utils.ViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * Created by alex on 3/14/17.
 */

public class LeadsMainFragment extends BaseHomeFragment implements TabLayout.OnTabSelectedListener {
    public static final String KEY_LEAD_DATA = "KEY_LEAD_DATA";

    public static LeadsMainFragment newInstance(LeadData leadData) {
        LeadsMainFragment fragment = new LeadsMainFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_LEAD_DATA, new Gson().toJson(leadData));
        fragment.setArguments(args);
        return fragment;
    }

    private Toolbar toolbar;
    private TabLayout mTabLayout;
    private ViewPager viewPager;
    private LeadsMenuManager leadsMenuManager;
    private View controlBtnContainer;
    private View leadFilterMarkerView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_leads_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LeadData leadData = new Gson().fromJson(getArguments().getString(KEY_LEAD_DATA), LeadData.class);

        initViews(view);
        updateTabLayout(leadData);
        initViewPager(view);
        updateViewPager(leadData);

        onTabSelected(mTabLayout.getTabAt(0));
    }

    private void initViews(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mTabLayout = (TabLayout) view.findViewById(R.id.tab_layout);

        controlBtnContainer = view.findViewById(R.id.view_leadControlBtnContainer);
        leadFilterMarkerView = view.findViewById(R.id.view_leadFilter);
        View mainContent = view.findViewById(R.id.view_leadsMainContent);
        DrawerLayout drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        View navigationViewContainer = view.findViewById(R.id.leadMenuLayout);

        leadsMenuManager = new LeadsMenuManager(getHomeActivity(), mainContent, drawerLayout, navigationViewContainer);


        view.findViewById(R.id.view_leadMenuBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leadsMenuManager.navigateDrawer();
            }
        });

        view.findViewById(R.id.view_leadFilterBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHomeActivity().startActivity(LeadsFilterActivity.getLeadsFilterActivityIntent(getContext()));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getType()) {
            case LEADS_MENU_SELECT:
                leadsMenuManager.closeDrawer();
                LeadData leadData = (LeadData) (event.getObject());
                updateTabLayout(leadData);
                updateViewPager(leadData);
                break;
            case NAVIGATION_BAR_SHOW:
                animateViewAboveNavBar(controlBtnContainer, true);
                break;
            case NAVIGATION_BAR_HIDE:
                animateViewAboveNavBar(controlBtnContainer, false);
                break;
        }
    }

    @Override
    public boolean onBackPressed() {
        return leadsMenuManager.closeDrawer();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void initViewPager(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.vp_leads);
        // LeadsMainPagerAdapter adapter = new LeadsMainPagerAdapter(getChildFragmentManager(), mTabLayout.getTabCount());
        // viewPager.setAdapter(adapter);
        //updateViewPager();
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
    }

    private void updateViewPager(LeadData leadData) {
        LeadsMainPagerAdapter adapter = new LeadsMainPagerAdapter(getChildFragmentManager(), leadData);
        viewPager.setAdapter(adapter);
    }

    private void updateTabLayout(LeadData leadData) {
        mTabLayout.removeAllTabs();

        switch (leadData.getStatus()) {
            case LEADS:
                mTabLayout.addTab(mTabLayout.newTab().setCustomView(getViewForTab(R.string.lead_tab_name_new, 10)));
                mTabLayout.addTab(mTabLayout.newTab().setCustomView(getViewForTab(R.string.lead_tab_name_engaged, 2)));
                mTabLayout.addTab(mTabLayout.newTab().setCustomView(getViewForTab(R.string.lead_tab_name_future, 10)));
                mTabLayout.setVisibility(View.VISIBLE);
                break;
            case LENDER:
                mTabLayout.addTab(mTabLayout.newTab().setCustomView(getViewForTab(R.string.lead_tab_name_new, 10)));
                mTabLayout.addTab(mTabLayout.newTab().setCustomView(getViewForTab(R.string.lead_tab_name_engaged, 2)));
                mTabLayout.addTab(mTabLayout.newTab().setCustomView(getViewForTab(R.string.lead_tab_name_appointment, 10)));
                mTabLayout.addTab(mTabLayout.newTab().setCustomView(getViewForTab(R.string.lead_tab_name_application, 10)));
                mTabLayout.setVisibility(View.VISIBLE);
                break;
            default:
                mTabLayout.setVisibility(View.GONE);
        }
        mTabLayout.addOnTabSelectedListener(this);
        mTabLayout.post(ViewUtils.configTab(mTabLayout, false));

        toolbar.setTitle(LeadUtils.getLeadStatusTitle(leadData.getStatus()));
    }

    private View getViewForTab(@StringRes int tabName, int notifCount) {
        View customTabView = LayoutInflater.from(getContext()).inflate(R.layout.tab_with_bage_view, null);

        TextView name = (TextView) customTabView.findViewById(R.id.tv_bageName);
        name.setText(tabName);

        if (notifCount > 0) {
            TextView counter = (TextView) customTabView.findViewById(R.id.tv_bageCounter);
            counter.setVisibility(View.VISIBLE);
            counter.setText(String.valueOf(notifCount));
            counter.getBackground().setTint(ContextCompat.getColor(getContext(), R.color.leads_substage_counter_bg_color));
            counter.setTextColor(ContextCompat.getColor(getContext(), R.color.lead_detail_tabText_color));
            name.setPadding(name.getPaddingLeft(), name.getPaddingTop(), 0, name.getPaddingBottom());
        }
        return customTabView;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        updateTabTextColor(tab, R.color.white);
        viewPager.setCurrentItem(tab.getPosition());
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

        TextView counter = (TextView) tab.getCustomView().findViewById(R.id.tv_bageCounter);
        counter.setTextColor(ContextCompat.getColor(getContext(), color));
    }


}
