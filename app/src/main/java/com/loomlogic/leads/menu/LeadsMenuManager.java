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
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loomlogic.R;
import com.loomlogic.home.HomeActivity;
import com.loomlogic.leads.LeadRole;
import com.loomlogic.utils.LeadPreferencesUtils;
import com.loomlogic.utils.LeadUtils;
import com.loomlogic.utils.ViewUtils;
import com.loomlogic.view.LinePageIndicator;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

/**
 * Created by alex on 2/28/17.
 */

public class LeadsMenuManager {
    private static final int ROLE_BUYER_MENU_POSITION = 0;
    private static final int ROLE_BUYER_SELLER_POSITION = 1;
    private HomeActivity activity;
    private LinearLayout mainContent;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private View navigationViewContainer;
    private LeadsTypeAdapter adapter;
    private ViewPager viewPager;

    public LeadsMenuManager(HomeActivity activity, LinearLayout mainContent, DrawerLayout drawerLayout, View navigationViewContainer) {
        this.activity = activity;
        this.mainContent = mainContent;
        this.drawer = drawerLayout;
        this.navigationViewContainer = navigationViewContainer;
        initViews();
        initNavigation();
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

        adapter = new LeadsTypeAdapter(activity.getBaseContext());

        viewPager = (ViewPager) navigationViewContainer.findViewById(R.id.vp_leadsMenu);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                boolean roleWasChanged = false;
                switch (position) {
                    case ROLE_BUYER_MENU_POSITION:
                        if (LeadPreferencesUtils.getCurrentLeadRole() == LeadRole.SELLER) {
                            roleWasChanged = true;
                        }
                        LeadPreferencesUtils.setCurrentLeadRole(LeadRole.BUYER);
                        buyersTv.setTextColor(Color.WHITE);
                        sellersTv.setTextColor(ContextCompat.getColor(activity, R.color.white_transparent_50));
                        break;
                    case ROLE_BUYER_SELLER_POSITION:
                        if (LeadPreferencesUtils.getCurrentLeadRole() == LeadRole.BUYER) {
                            roleWasChanged = true;
                        }
                        LeadPreferencesUtils.setCurrentLeadRole(LeadRole.SELLER);
                        buyersTv.setTextColor(ContextCompat.getColor(activity, R.color.white_transparent_50));
                        sellersTv.setTextColor(Color.WHITE);
                        break;
                }

                titleIndicator.setSelectedColor(ContextCompat.getColor(activity, LeadUtils.getCurrentLeadRoleColor()));
                newLeadFAB.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(activity, LeadUtils.getCurrentLeadRoleColor())));

                if (roleWasChanged) {
                    activity.refreshNavBar();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        titleIndicator.setViewPager(viewPager);
        if (LeadPreferencesUtils.getCurrentLeadRole() == LeadRole.SELLER) {
            viewPager.setCurrentItem(ROLE_BUYER_SELLER_POSITION);
        }

        buyersTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (viewPager.getCurrentItem()) {
                    case ROLE_BUYER_SELLER_POSITION:
                        viewPager.setCurrentItem(ROLE_BUYER_MENU_POSITION);
                        break;
                }
            }
        });

        sellersTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (viewPager.getCurrentItem()) {
                    case ROLE_BUYER_MENU_POSITION:
                        viewPager.setCurrentItem(ROLE_BUYER_SELLER_POSITION);
                        break;
                }
            }
        });
    }

    private void initNavigation() {
        mDrawerToggle = new ActionBarDrawerToggle(activity, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                activity.supportInvalidateOptionsMenu();
                activity.gerBottomNavBar().show();
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

        mainContent.findViewById(R.id.ib_leadsMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateDrawer();
            }
        });
    }

    public void navigateDrawer() {
        if (!closeDrawer()) {
            openDrawer();
        }
    }

    public boolean isDrawerOpen() {
        return drawer.isDrawerVisible(GravityCompat.START);
    }

    public void openDrawer() {
        if (KeyboardVisibilityEvent.isKeyboardVisible(activity)){
            ViewUtils.hideSoftKeyboard(drawer);
        }
        drawer.openDrawer(GravityCompat.START);
        activity.gerBottomNavBar().hide();
    }

    public boolean closeDrawer() {
        if (isDrawerOpen()) {
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }

}
