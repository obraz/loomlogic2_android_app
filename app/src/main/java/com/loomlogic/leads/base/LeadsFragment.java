package com.loomlogic.leads.base;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.SwipeDismissItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.touchguard.RecyclerViewTouchActionGuardManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
import com.loomlogic.R;
import com.loomlogic.base.MessageEvent;
import com.loomlogic.home.BaseHomeFragment;
import com.loomlogic.leads.menu.LeadMenuItem;
import com.loomlogic.leads.menu.LeadsMenuManager;
import com.loomlogic.utils.LeadUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * Created by alex on 2/22/17.
 */

public class LeadsFragment extends BaseHomeFragment implements LeadsAdapter.OnLeadClickListener, SearchView.OnQueryTextListener, Toolbar.OnMenuItemClickListener {
    private LeadsMenuManager leadsMenuManager;
    private ArrayList<LeadItem> fakeList;
    private SwipeRefreshLayout layoutSwipeRefresh;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter mWrappedAdapter;
    private RecyclerViewSwipeManager mRecyclerViewSwipeManager;
    private RecyclerViewTouchActionGuardManager mRecyclerViewTouchActionGuardManager;

    private LeadsAdapter mItemAdapter;

    public static LeadsFragment newInstance() {
        LeadsFragment fragment = new LeadsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_leads, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar(view);
        initViews(view);
    }

    private void initToolbar(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("Contract");
        toolbar.inflateMenu(R.menu.menu_leads_filter);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leadsMenuManager.navigateDrawer();
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                getHomeActivity().showErrorSnackBar("filter");
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews(View view) {
        LinearLayout mainContent = (LinearLayout) view.findViewById(R.id.ll_leadsMainContent);
        DrawerLayout drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        View navigationViewContainer = view.findViewById(R.id.leadMenuLayout);

        leadsMenuManager = new LeadsMenuManager(getHomeActivity(), mainContent, drawerLayout, navigationViewContainer);

        layoutSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.layoutSwipeRefresh);
        layoutSwipeRefresh.setColorSchemeColors(ContextCompat.getColor(getContext(), LeadUtils.getCurrentLeadRoleColor()));
        layoutSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                layoutSwipeRefresh.setRefreshing(false);
            }
        });

        initList();
    }

    private void initList() {
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.list);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        // touch guard manager  (this class is required to suppress scrolling while swipe-dismiss animation is running)
        mRecyclerViewTouchActionGuardManager = new RecyclerViewTouchActionGuardManager();
        mRecyclerViewTouchActionGuardManager.setInterceptVerticalScrollingWhileAnimationRunning(true);
        mRecyclerViewTouchActionGuardManager.setEnabled(true);

        // swipe manager
        mRecyclerViewSwipeManager = new RecyclerViewSwipeManager();
//int id, int unreadNotificationCount, int deadlineDate, String avatarUrl, String firstName, String lastName,
// Gender gender, String address, boolean isFinancing, int escrowStatusDoneCount
        fakeList = new ArrayList<>();
        fakeList.add(new LeadItem(1, 2, 3, "http://loomlogic.ucoz.net/3.jpg", "Randall", "Padilla", LeadItem.Gender.MALE, "322 Lennie Squares Apt. 344", true, 0));
        fakeList.add(new LeadItem(2, 0, -5, "http://loomlogic.ucoz.net/1.jpg", "Craig", "Perez", LeadItem.Gender.MALE, "036 Ozella Lights", true, 1));
        fakeList.add(new LeadItem(3, 100, 0, "http://loomlogic.ucoz.net/6.jpg", "Darrell", "Pierce", LeadItem.Gender.MALE, "960 Xavier Branch Apt. 901", true, 2));
        fakeList.add(new LeadItem(4, 0, -15, "http://loomlogic.ucoz.net/4.jpg", "Benjamin", "Carlson", LeadItem.Gender.MALE, "9549 Cummings Lane Apt. 681", true, 3));
        fakeList.add(new LeadItem(5, 12, 20, "http://loomlogic.ucoz.net/5.jpg", "Glenn", "Flowers", LeadItem.Gender.FEMALE, "941 Hyatt Divide Suite 043", true, 4));
        fakeList.add(new LeadItem(6, 7, 5, "http://loomlogic.ucoz.net/5.jpg", "Tom", "Foster", LeadItem.Gender.FEMALE, "804 Mosciski Station", true, 5));
        fakeList.add(new LeadItem(7, 0, 1, "", "Jonathan", "Perkins", LeadItem.Gender.MALE, "804 Mosciski Station", true, 6));
        fakeList.add(new LeadItem(8, 0, -7, "http://loomlogic.ucoz.net/2.jpg", "William", "Miller", LeadItem.Gender.MALE, "536 Feeney Springs", true, 7));
        fakeList.add(new LeadItem(9, 5, 21, "http://loomlogic.ucoz.net/4.jpg", "Justin", "Keller", LeadItem.Gender.MALE, "631 Berge Coves", false, 0));
        fakeList.add(new LeadItem(10, 15, -22, "http://loomlogic.ucoz.net/3.jpg", "Steven", "Lucas", LeadItem.Gender.MALE, "9579 Ignatius Centers Suite 537", false, 1));
        fakeList.add(new LeadItem(11, 789, 1, "", "Steven'", "Lucas", LeadItem.Gender.MALE, "9579 Ignatius Centers Suite 537", false, 2));
        fakeList.add(new LeadItem(12, 2, 12, "", "Steven", "Lucas", LeadItem.Gender.MALE, "9579 Ignatius Centers Suite 537", true, 2));
        fakeList.add(new LeadItem(13, 1, 3, "", "Steven", "Lucas", LeadItem.Gender.FEMALE, "9579 Ignatius Centers Suite 537", true, 1));
        fakeList.add(new LeadItem(14, 0, -5, null, "Steven", "Lucas", LeadItem.Gender.MALE, "9579 Ignatius Centers Suite 537", false, 1));
        fakeList.add(new LeadItem(15, 66, 7, "", "Steven", "Lucas", LeadItem.Gender.NONE, "9579 Ignatius Centers Suite 537", true, 4));


        mItemAdapter = new LeadsAdapter(getContext(), this);
        mItemAdapter.setData(fakeList);

        mAdapter = mItemAdapter;

        mWrappedAdapter = mRecyclerViewSwipeManager.createWrappedAdapter(mItemAdapter);      // wrap for swiping

        mWrappedAdapter = new LeadsSearchHeaderAdapter(mWrappedAdapter, this);
        final GeneralItemAnimator animator = new SwipeDismissItemAnimator();

        // Change animations are enabled by default since support-v7-recyclerview v22.
        // Disable the change animation in order to make turning back animation of swiped item works properly.
        animator.setSupportsChangeAnimations(false);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mWrappedAdapter);  // requires *wrapped* adapter
        mRecyclerView.setItemAnimator(animator);

        //  mRecyclerView.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(getContext(), R.drawable.list_divider_h), true));

        // NOTE:
        // The initialization order is very important! This order determines the priority of touch event handling.
        //
        // priority: TouchActionGuard > Swipe > DragAndDrop
        mRecyclerViewTouchActionGuardManager.attachRecyclerView(mRecyclerView);
        mRecyclerViewSwipeManager.attachRecyclerView(mRecyclerView);
    }

    @Override
    public void onDestroyView() {
        if (mRecyclerViewSwipeManager != null) {
            mRecyclerViewSwipeManager.release();
            mRecyclerViewSwipeManager = null;
        }

        if (mRecyclerViewTouchActionGuardManager != null) {
            mRecyclerViewTouchActionGuardManager.release();
            mRecyclerViewTouchActionGuardManager = null;
        }

        if (mRecyclerView != null) {
            mRecyclerView.setItemAnimator(null);
            mRecyclerView.setAdapter(null);
            mRecyclerView = null;
        }

        if (mWrappedAdapter != null) {
            WrapperAdapterUtils.releaseAll(mWrappedAdapter);
            mWrappedAdapter = null;
        }
        mAdapter = null;
        mLayoutManager = null;

        super.onDestroyView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getType()) {
            case LEADS_MENU_SELECT:
                leadsMenuManager.closeDrawer();
                getHomeActivity().showErrorSnackBar(((LeadMenuItem) (event.getObject())).getName());
                return;
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
    public boolean onBackPressed() {
        return leadsMenuManager.closeDrawer();
    }

    @Override
    public void onItemClickListener(LeadItem item) {
        mAdapter.notifyDataSetChanged();
        showFragment(LeadDetailsMainFragment.newInstance(item));
    }

    @Override
    public void onMessageClickListener(LeadItem item) {
        mAdapter.notifyDataSetChanged();
        openMessageDialog(item);
    }

    @Override
    public void onCallClickListener(LeadItem item) {
        mAdapter.notifyDataSetChanged();
        openCallDialog(item);
    }

    private void openCallDialog(LeadItem leadItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(R.array.lead_call_chooser, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        getHomeActivity().showErrorSnackBar("twillio call");
                        break;
                    case 1:
                        getHomeActivity().showErrorSnackBar("call");
                        break;
                }
            }

        });
        builder.show();
    }

    private void openMessageDialog(LeadItem leadItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(R.array.lead_message_chooser, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        getHomeActivity().showErrorSnackBar("note");
                        break;
                    case 1:
                        getHomeActivity().showErrorSnackBar("email");
                        break;
                    case 2:
                        getHomeActivity().showErrorSnackBar("sms");
                        break;
                }
            }

        });
        builder.show();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.e("onQueryTextSubmit: ", query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.e("onQueryTextChange: ", "  " + newText);
        doSearch(newText);
        return false;
    }

    private void doSearch(String newText) {
        if (TextUtils.isEmpty(newText)) {
            mItemAdapter.setData(fakeList);
            return;
        }
        ArrayList<LeadItem> foundItemsList = new ArrayList<>();

        for (LeadItem item : fakeList) {
            if (item.firstName.toLowerCase().contains(newText) || item.lastName.toLowerCase().contains(newText)) {
                foundItemsList.add(item);
            }
        }
        mItemAdapter.setData(foundItemsList);
    }

}
