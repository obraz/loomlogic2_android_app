package com.loomlogic.leads.mainleaddetails;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loomlogic.R;
import com.loomlogic.home.BaseHomeFragment;
import com.loomlogic.leads.entity.LeadItem;
import com.loomlogic.leads.settings.LeadSettingsActivity;
import com.loomlogic.utils.Utils;

/**
 * Created by alex on 3/14/17.
 */

public class LeadDetailsMainFragment extends BaseHomeFragment implements TabLayout.OnTabSelectedListener, Toolbar.OnMenuItemClickListener {
    public static final String KEY_LEAD_ITEM = "KEY_LEAD_ITEM";

    public static LeadDetailsMainFragment newInstance(LeadItem item) {
        LeadDetailsMainFragment fragment = new LeadDetailsMainFragment();
        Bundle args = new Bundle();
        args.putString(KEY_LEAD_ITEM, new Gson().toJson(item));
        fragment.setArguments(args);
        return fragment;
    }

    private LeadItem leadItem;
    private TabLayout mTabLayout;
    private ViewPager viewPager;

    Runnable mTabLayout_config = new Runnable() {
        @Override
        public void run() {
            int widthDiff = Utils.getDisplayWidth(getContext()) - mTabLayout.getWidth();
            if (widthDiff > 0) {
                int widthAdd = widthDiff / mTabLayout.getTabCount();
                for (int i = 0; i < mTabLayout.getTabCount(); i++) {
                    if (i == mTabLayout.getTabCount() - 1) {
                        widthAdd = widthAdd + widthDiff % mTabLayout.getTabCount();
                    }
                    View tabView = mTabLayout.getTabAt(i).getCustomView();
                    tabView.setLayoutParams(new LinearLayout.LayoutParams(tabView.getWidth() + widthAdd, LinearLayout.LayoutParams.WRAP_CONTENT));
                }

            } else {
                // todo do it only once
                mTabLayout.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mTabLayout.fullScroll(HorizontalScrollView.FOCUS_LEFT);
                    }
                }, 800);

            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lead_details_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        leadItem = new Gson().fromJson(getArguments().getString(KEY_LEAD_ITEM), LeadItem.class);

        initToolbar(view);
        initTabLayout(view);
        initViewPager(view);

        onTabSelected(mTabLayout.getTabAt(0));
    }

    private void initViewPager(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.vp_leadDetails);
        LeadDetailsPagerAdapter adapter = new LeadDetailsPagerAdapter(getChildFragmentManager(), leadItem, mTabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
    }

    private void initTabLayout(View view) {
        mTabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(getViewForTab(R.string.lead_details_tab_details, 0)));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(getViewForTab(R.string.lead_details_tab_messages, leadItem.unreadNotificationCount)));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(getViewForTab(R.string.lead_details_tab_tasks, 0)));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(getViewForTab(R.string.lead_details_tab_documents, 0)));
        mTabLayout.addOnTabSelectedListener(this);
        mTabLayout.post(mTabLayout_config);
    }

    private View getViewForTab(@StringRes int tabName, int notifCount) {
        View customTabView = LayoutInflater.from(getContext()).inflate(R.layout.tab_with_bage_view, null);

        TextView name = (TextView) customTabView.findViewById(R.id.tv_bageName);
        name.setText(tabName);

        if (notifCount > 0) {
            TextView counter = (TextView) customTabView.findViewById(R.id.tv_bageCounter);
            counter.setVisibility(View.VISIBLE);
            counter.setText(String.valueOf(notifCount));

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
    }

    private void initToolbar(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(String.format(leadItem.getFullFormattedName()));
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
            case R.id.action_edit:
                getContext().startActivity(LeadSettingsActivity.getLeadSettingsActivityIntent(getContext(), leadItem));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
