package com.loomlogic.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.loomlogic.R;
import com.loomlogic.base.BaseActivity;
import com.loomlogic.leads.LeadsFragment;
import com.loomlogic.utils.LeadUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 2/22/17.
 */

public class HomeActivity extends BaseActivity implements View.OnClickListener {
    private ArrayList<ImageView> navigationIconsList;
    private int currentTab;
    private LeadsFragment leadsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        leadsFragment= new LeadsFragment();
        initNavigationButtons();
    }

    private void initNavigationButtons() {
        navigationIconsList = new ArrayList<>();
        navigationIconsList.add((ImageView) findViewById(R.id.iv_navigation_notifications));
        navigationIconsList.add((ImageView) findViewById(R.id.iv_navigation_search));
        navigationIconsList.add((ImageView) findViewById(R.id.iv_navigation_leads));
        navigationIconsList.add((ImageView) findViewById(R.id.iv_navigation_tasks));
        navigationIconsList.add((ImageView) findViewById(R.id.iv_navigation_profile));

        findViewById(R.id.fl_navigation_notifications).setOnClickListener(this);
        findViewById(R.id.fl_navigation_search).setOnClickListener(this);
        findViewById(R.id.fl_navigation_leads).setOnClickListener(this);
        findViewById(R.id.fl_navigation_tasks).setOnClickListener(this);
        findViewById(R.id.fl_navigation_profile).setOnClickListener(this);

        findViewById(R.id.fl_navigation_leads).performClick();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_navigation_notifications:
                selectNavigationTab(0, new DefaultFragment());
                break;
            case R.id.fl_navigation_search:
                selectNavigationTab(1, new DefaultFragment());
                break;
            case R.id.fl_navigation_leads:
                selectNavigationTab(2, leadsFragment);
                break;
            case R.id.fl_navigation_tasks:
                selectNavigationTab(3, new DefaultFragment());
                break;
            case R.id.fl_navigation_profile:
                selectNavigationTab(4, new DefaultFragment());
                break;
        }
    }

    private void selectNavigationTab(int currentTab, Fragment fragment) {
        this.currentTab = currentTab;
        updateNavigationTabIcons();
        replaceFragment(fragment);
    }

    public void updateNavigationTabIcons() {
        int color;
        for (int i = 0; i < navigationIconsList.size(); i++) {
            ImageView view = navigationIconsList.get(i);
            if (currentTab == i) {
                color = LeadUtils.getCurrentLeadRoleColor();
            } else {
                color = ContextCompat.getColor(this, R.color.colorNavigationDefault);
            }
            DrawableCompat.setTint(view.getDrawable(), color);
        }
    }

    protected void replaceFragment(Fragment fragment) {
        String tag = fragment.getClass().getName();

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.findFragmentByTag(tag) == null) {
            fragmentManager.beginTransaction().replace(R.id.container, fragment, tag).commit();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        BaseHomeFragment fragment = getVisibleFragment();
        if (fragment != null) {
            if (fragment.hasMenuOptions()) {
                return false;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public BaseHomeFragment getVisibleFragment() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible())
                    return (BaseHomeFragment) fragment;
            }
        }
        return null;
    }
}
