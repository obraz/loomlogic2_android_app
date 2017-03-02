package com.loomlogic.leads;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.loomlogic.R;
import com.loomlogic.base.MessageEvent;
import com.loomlogic.home.BaseHomeFragment;
import com.loomlogic.leads.menu.LeadMenuItem;
import com.loomlogic.leads.menu.LeadsMenuManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by alex on 2/22/17.
 */

public class LeadsFragment extends BaseHomeFragment {
    private LeadsMenuManager leadsMenuManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_leads, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }


    private void initViews(View view) {
        // Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        LinearLayout mainContent = (LinearLayout) view.findViewById(R.id.ll_leadsMainContent);
        DrawerLayout drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        View navigationViewContainer = view.findViewById(R.id.leadMenuLayout);

        leadsMenuManager = new LeadsMenuManager(getHomeActivity(), mainContent, drawerLayout, navigationViewContainer);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        leadsMenuManager.navigateDrawer();
        return super.onOptionsItemSelected(item);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.getType() == MessageEvent.MessageEventType.LEADS_MENU_SELECT){
            leadsMenuManager.closeDrawer();
            getHomeActivity().showErrorSnackBar(((LeadMenuItem)(event.getObject())).getName());
        }
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

    @Override
    public boolean hasMenuOptions(){
        return true;
    }

}
