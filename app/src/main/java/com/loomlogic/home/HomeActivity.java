package com.loomlogic.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.loomlogic.R;
import com.loomlogic.leads.base.LeadsFragment;
import com.loomlogic.multibackstack.BackStackActivity;
import com.loomlogic.utils.LeadUtils;

import java.util.List;

/**
 * Created by alex on 2/22/17.
 */

public class HomeActivity extends BackStackActivity implements BottomNavigationBar.OnTabSelectedListener {
    private static final String STATE_CURRENT_TAB_ID = "current_tab_id";
    private static final int MAIN_TAB_ID = 0;

    private BottomNavigationBar bottomNavBar;
    private Fragment curFragment;
    private int curTabId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setUpBottomNavBar();
        refreshNavBar();
        if (savedInstanceState == null) {
            showFragment(rootTabFragment(MAIN_TAB_ID));
        }
    }

    private void setUpBottomNavBar() {
        bottomNavBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation);
        bottomNavBar.setAutoHideEnabled(true);
        bottomNavBar.setTabSelectedListener(this);
    }

    public void refreshNavBar() {
        bottomNavBar.clearAll();
        bottomNavBar
                .addItem(new BottomNavigationItem(R.drawable.ic_navigation_notifications, R.string.notifications))
                .addItem(new BottomNavigationItem(R.drawable.ic_navigation_search, R.string.search))
                .addItem(new BottomNavigationItem(R.drawable.ic_navigation_leads, R.string.leads))
                .addItem(new BottomNavigationItem(R.drawable.ic_navigation_tasks, R.string.tasks))
                .addItem(new BottomNavigationItem(R.drawable.ic_navigation_profile, R.string.profile))
                .setActiveColor(R.color.navBarBgColor)
                .setBarBackgroundColor(LeadUtils.getCurrentLeadRoleColor())
                .setFirstSelectedPosition(curTabId)
                .initialise();
    }

    @NonNull
    private Fragment rootTabFragment(int tabId) {
        switch (tabId) {
            case 0:
                return DefaultFragment.newInstance();
            case 1:
                return DefaultFragment.newInstance();
            case 2:
                return LeadsFragment.newInstance();
            case 3:
                return DefaultFragment.newInstance();
            case 4:
                return DefaultFragment.newInstance();
            default:
                return DefaultFragment.newInstance();
        }
    }

    public BottomNavigationBar getBottomNavBar() {
        return bottomNavBar;
    }


    public BaseHomeFragment gerCurrentFragment() {
        return (BaseHomeFragment) curFragment;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        curFragment = getSupportFragmentManager().findFragmentById(R.id.container);
        curTabId = savedInstanceState.getInt(STATE_CURRENT_TAB_ID);
        bottomNavBar.selectTab(curTabId, false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_CURRENT_TAB_ID, curTabId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        BaseHomeFragment fragment = getVisibleFragment();
        if (fragment != null && fragment.onBackPressed()) {
            return;
        }

        Pair<Integer, Fragment> pair = popFragmentFromBackStack();
        if (pair != null) {
            getBottomNavBar().show();
            backTo(pair.first, pair.second);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onTabSelected(int position) {
        if (curFragment != null) {
            pushFragmentToBackStack(curTabId, curFragment);
        }
        curTabId = position;
        Fragment fragment = popFragmentFromBackStack(curTabId);
        if (fragment == null) {
            fragment = rootTabFragment(curTabId);
        }
        replaceFragment(fragment);
    }

    @Override
    public void onTabReselected(int position) {
        backToRoot();
    }

    @Override
    public void onTabUnselected(int position) {
    }

    public void showFragment(@NonNull Fragment fragment) {
        showFragment(fragment, true);
    }

    public void showFragment(@NonNull Fragment fragment, boolean addToBackStack) {
        if (curFragment != null && addToBackStack) {
            pushFragmentToBackStack(curTabId, curFragment);
        }
        replaceFragment(fragment);
    }

    private void backTo(int tabId, @NonNull Fragment fragment) {
        if (tabId != curTabId) {
            curTabId = tabId;
            bottomNavBar.selectTab(curTabId, false);
        }
        replaceFragment(fragment);
        getSupportFragmentManager().executePendingTransactions();
    }

    private void backToRoot() {
        if (isRootTabFragment(curFragment, curTabId)) {
            return;
        }
        resetBackStackToRoot(curTabId);
        Fragment rootFragment = popFragmentFromBackStack(curTabId);
        assert rootFragment != null;
        backTo(curTabId, rootFragment);
    }

    private boolean isRootTabFragment(@NonNull Fragment fragment, int tabId) {
        return fragment.getClass() == rootTabFragment(tabId).getClass();
    }

    private void replaceFragment(@NonNull Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tr = fm.beginTransaction();
        tr.replace(R.id.container, fragment);
        tr.commitAllowingStateLoss();
        curFragment = fragment;
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
