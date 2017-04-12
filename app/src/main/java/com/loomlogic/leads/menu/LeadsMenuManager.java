package com.loomlogic.leads.menu;

import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.loomlogic.R;
import com.loomlogic.home.HomeActivity;
import com.loomlogic.leads.create.CreateLeadActivity;
import com.loomlogic.leads.entity.LeadRole;
import com.loomlogic.utils.LeadPreferencesUtils;
import com.loomlogic.utils.LeadUtils;
import com.loomlogic.utils.ViewUtils;
import com.loomlogic.view.EventListeningSpinner;
import com.loomlogic.view.LinePageIndicator;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

/**
 * Created by alex on 2/28/17.
 */

public class LeadsMenuManager {
    private static final int ROLE_BUYER_MENU_POSITION = 0;
    private static final int ROLE_BUYER_SELLER_POSITION = 1;
    private HomeActivity activity;
    private View mainContent;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private View navigationViewContainer;
    private LeadsTypeAdapter adapter;
    private ViewPager viewPager;

    public LeadsMenuManager(HomeActivity activity, View mainContent, DrawerLayout drawerLayout, View navigationViewContainer) {
        this.activity = activity;
        this.mainContent = mainContent;
        this.drawer = drawerLayout;
        this.navigationViewContainer = navigationViewContainer;
        initViews();
        initNavigation();
    }

    private void initViews() {
        initLeadFilterSpinner();
        final FloatingActionButton newLeadFAB = (FloatingActionButton) navigationViewContainer.findViewById(R.id.fab_leadNew);
        newLeadFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateLeadDialog();
            }
        });

        final TextView buyersTv = (TextView) navigationViewContainer.findViewById(R.id.tv_leadMenu_buyers);
        final TextView sellersTv = (TextView) navigationViewContainer.findViewById(R.id.tv_leadMenu_sellers);

        final LinePageIndicator titleIndicator = (LinePageIndicator) navigationViewContainer.findViewById(R.id.indicator);
        titleIndicator.setFullWidht();
        titleIndicator.setUnselectedColor(ContextCompat.getColor(activity, R.color.lead_menu_title_bg_color));

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
                        buyersTv.setTextColor(ContextCompat.getColor(activity, R.color.lead_menu_title_active));
                        sellersTv.setTextColor(ContextCompat.getColor(activity, R.color.lead_menu_title_notactive));
                        break;
                    case ROLE_BUYER_SELLER_POSITION:
                        if (LeadPreferencesUtils.getCurrentLeadRole() == LeadRole.BUYER) {
                            roleWasChanged = true;
                        }
                        LeadPreferencesUtils.setCurrentLeadRole(LeadRole.SELLER);
                        buyersTv.setTextColor(ContextCompat.getColor(activity, R.color.lead_menu_title_notactive));
                        sellersTv.setTextColor(ContextCompat.getColor(activity, R.color.lead_menu_title_active));
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

    private void initLeadFilterSpinner() {
        final View spinnerContainer = navigationViewContainer.findViewById(R.id.fl_leadFilterContainer);
        final ImageView spinnerArrow = (ImageView) navigationViewContainer.findViewById(R.id.iv_leadFilterArrow);

        EventListeningSpinner leadFilterSp = (EventListeningSpinner) navigationViewContainer.findViewById(R.id.sp_leadFilter);
        leadFilterSp.setAdapter(new LeadMenuFilterAdapter());
        leadFilterSp.setSpinnerEventsListener(new EventListeningSpinner.OnSpinnerEventsListener() {
            @Override
            public void onSpinnerOpened(Spinner spinner) {
                spinnerContainer.setBackgroundResource(R.drawable.bg_lead_filter_spinner_opened);
                spinnerArrow.setRotation(90);
                spinnerArrow.setColorFilter(Color.WHITE);
            }

            @Override
            public void onSpinnerClosed(Spinner spinner) {
                spinnerContainer.setBackgroundResource(R.drawable.bg_lead_filter_spinner_closed);
                spinnerArrow.setRotation(270);
                spinnerArrow.setColorFilter(ContextCompat.getColor(activity, R.color.lead_menu_spinner_arrow_closed_color));
            }
        });
    }

    private void initNavigation() {
        mDrawerToggle = new ActionBarDrawerToggle(activity, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                activity.supportInvalidateOptionsMenu();
                activity.getBottomNavBar().show();
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
        if (!closeDrawer()) {
            openDrawer();
        }
    }

    public boolean isDrawerOpen() {
        return drawer.isDrawerVisible(GravityCompat.START);
    }

    public void openDrawer() {
        if (KeyboardVisibilityEvent.isKeyboardVisible(activity)) {
            ViewUtils.hideSoftKeyboard(drawer);
        }
        drawer.openDrawer(GravityCompat.START);
        activity.getBottomNavBar().hide();
    }

    public boolean closeDrawer() {
        if (isDrawerOpen()) {
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }

    private void openCreateLeadDialog() {
        ListAdapter adapter = new ArrayAdapter<String>(
                activity,
                android.R.layout.select_dialog_item,
                android.R.id.text1,
                activity.getResources().getStringArray(R.array.lead_create_new_chooser)) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                TextView tv = (TextView) v.findViewById(android.R.id.text1);

                tv.setCompoundDrawablesWithIntrinsicBounds(position == 0 ? R.drawable.ic_lead_create_from_contact : R.drawable.ic_lead_create_new, 0, 0, 0);
                tv.setCompoundDrawablePadding((int)activity.getResources().getDimension(R.dimen.dialog_item_icon_margin));
                tv.setTextSize(16);
                return v;
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.startActivity(CreateLeadActivity.getCreateLeadActivityIntent(activity, which == 0));
            }
        });
        builder.show();
    }

}
