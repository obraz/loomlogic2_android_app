package com.loomlogic.leads.menu;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loomlogic.R;
import com.loomlogic.home.HomeActivity;
import com.loomlogic.leads.LeadRole;
import com.loomlogic.utils.LeadPreferencesUtils;
import com.loomlogic.utils.LeadUtils;
import com.loomlogic.view.LinePageIndicator;

/**
 * Created by alex on 2/28/17.
 */

public class LeadsMenuManager {
    private HomeActivity activity;
    private LinearLayout mainContent;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private View navigationViewContainer;

    public LeadsMenuManager(HomeActivity activity, LinearLayout mainContent, DrawerLayout drawerLayout, View navigationViewContainer) {
        this.activity = activity;
        this.mainContent = mainContent;
        Toolbar toolbar = (Toolbar) mainContent.findViewById(R.id.toolbar);
        this.drawer = drawerLayout;
        this.navigationViewContainer = navigationViewContainer;
        initNavigation(toolbar);
        initViews();
    }

    private void initViews() {
        final FloatingActionButton newLeadFAB = (FloatingActionButton) navigationViewContainer.findViewById(R.id.fab_leadNew);
        newLeadFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
                activity.showErrorSnackBar("new lead click");
            }
        });

        final TextView buyersTv = (TextView) navigationViewContainer.findViewById(R.id.tv_leadMenu_buyers);
        final TextView sellersTv = (TextView) navigationViewContainer.findViewById(R.id.tv_leadMenu_sellers);

        final LinePageIndicator titleIndicator = (LinePageIndicator) navigationViewContainer.findViewById(R.id.indicator);
        titleIndicator.setFullWidht();

        LeadsTypeAdapter adapter = new LeadsTypeAdapter(activity.getSupportFragmentManager());

        final ViewPager viewPager = (ViewPager) navigationViewContainer.findViewById(R.id.vp_leadsMenu);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        LeadPreferencesUtils.setCurrentLeadRole(LeadRole.BUYER);
                        buyersTv.setTextColor(Color.WHITE);
                        sellersTv.setTextColor(ContextCompat.getColor(activity, R.color.white_transparent_50));
                        break;
                    case 1:
                        LeadPreferencesUtils.setCurrentLeadRole(LeadRole.SELLER);
                        buyersTv.setTextColor(ContextCompat.getColor(activity, R.color.white_transparent_50));
                        sellersTv.setTextColor(Color.WHITE);
                        break;
                }
                titleIndicator.setSelectedColor(LeadUtils.getCurrentLeadRoleColor());
                newLeadFAB.setBackgroundTintList(ColorStateList.valueOf(LeadUtils.getCurrentLeadRoleColor()));
                activity.updateNavigationTabIcons();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        titleIndicator.setViewPager(viewPager);
        if (LeadPreferencesUtils.getCurrentLeadRole() == LeadRole.SELLER) {
            viewPager.setCurrentItem(1);
        }

        buyersTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (viewPager.getCurrentItem()) {
                    case 1:
                        viewPager.setCurrentItem(0);
                        break;
                }
            }
        });

        sellersTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (viewPager.getCurrentItem()) {
                    case 0:
                        viewPager.setCurrentItem(1);
                        break;
                }
            }
        });


    }

    private void initNavigation(Toolbar toolbar) {
        mDrawerToggle = new ActionBarDrawerToggle(activity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                activity.supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                activity.supportInvalidateOptionsMenu();
            }

            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float moveFactor = (drawerView.getWidth() * slideOffset);
                mainContent.setTranslationX(moveFactor);
            }
        };

        drawer.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        mDrawerToggle.setDrawerIndicatorEnabled(false);

        Drawable drawable = ResourcesCompat.getDrawable(activity.getResources(), R.drawable.ic_navigation_leads, activity.getTheme());
        mDrawerToggle.setHomeAsUpIndicator(drawable);

        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void navigateDrawer() {
        if (drawer.isDrawerVisible(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    public void closeDrawer() {
        if (drawer.isDrawerVisible(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    public DrawerLayout getDrawer() {
        return drawer;
    }

    public ActionBarDrawerToggle getDrawerToggle() {
        return mDrawerToggle;
    }
}
