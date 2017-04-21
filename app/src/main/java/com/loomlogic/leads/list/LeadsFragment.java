package com.loomlogic.leads.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.SwipeDismissItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.touchguard.RecyclerViewTouchActionGuardManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
import com.loomlogic.R;
import com.loomlogic.home.BaseHomeFragment;
import com.loomlogic.leads.base.LeadStatus;
import com.loomlogic.leads.entity.Gender;
import com.loomlogic.leads.entity.LeadItem;
import com.loomlogic.leads.mainleaddetails.LeadDetailsMainFragment;
import com.loomlogic.utils.LeadUtils;

import java.util.ArrayList;

import static com.loomlogic.leads.main.LeadsMainFragment.KEY_LEAD_STATUS;

/**
 * Created by alex on 2/22/17.
 */

public class LeadsFragment extends BaseHomeFragment implements LeadsAdapter.OnLeadClickListener, SearchView.OnQueryTextListener {
    private ArrayList<LeadItem> fakeList;
    private SwipeRefreshLayout layoutSwipeRefresh;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter mWrappedAdapter;
    private RecyclerViewSwipeManager mRecyclerViewSwipeManager;
    private RecyclerViewTouchActionGuardManager mRecyclerViewTouchActionGuardManager;

    private LeadsAdapter mItemAdapter;

    public static LeadsFragment newInstance(LeadStatus status) {
        LeadsFragment fragment = new LeadsFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_LEAD_STATUS, status);
        fragment.setArguments(args);
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
        initViews(view);
        LeadStatus leadStatus = (LeadStatus) getArguments().get(KEY_LEAD_STATUS);
        Log.e(  "onViewCreated: ", ""+leadStatus);
    }

    private void initViews(View view) {
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
        fakeList.add(new LeadItem(1, 2, 3, "http://loomlogic.ucoz.net/3.jpg", "Randall", "Padilla", Gender.MALE, "322 Lennie Squares Apt. 344", true, 0));
        fakeList.add(new LeadItem(2, 0, -5, "http://loomlogic.ucoz.net/1.jpg", "Craig", "Perez", Gender.MALE, "036 Ozella Lights", true, 1));
        fakeList.add(new LeadItem(3, 100, 0, "http://loomlogic.ucoz.net/6.jpg", "Darrell", "Pierce", Gender.MALE, "960 Xavier Branch Apt. 901", true, 2));
        fakeList.add(new LeadItem(4, 0, -15, "http://loomlogic.ucoz.net/4.jpg", "Benjamin", "Carlson", Gender.MALE, "9549 Cummings Lane Apt. 681", true, 3));
        fakeList.add(new LeadItem(5, 12, 20, "http://loomlogic.ucoz.net/5.jpg", "Glenn", "Flowers", Gender.FEMALE, "941 Hyatt Divide Suite 043", true, 4));
        fakeList.add(new LeadItem(6, 7, 5, "http://loomlogic.ucoz.net/5.jpg", "Tom", "Foster", Gender.FEMALE, "804 Mosciski Station", true, 5));
        fakeList.add(new LeadItem(7, 0, 1, "", "Jonathan", "Perkins", Gender.MALE, "804 Mosciski Station", true, 6));
        fakeList.add(new LeadItem(8, 0, -7, "http://loomlogic.ucoz.net/2.jpg", "William", "Miller", Gender.MALE, "536 Feeney Springs", true, 7));
        fakeList.add(new LeadItem(9, 5, 21, "http://loomlogic.ucoz.net/4.jpg", "Justin", "Keller", Gender.MALE, "631 Berge Coves", false, 0));
        fakeList.add(new LeadItem(10, 15, -22, "http://loomlogic.ucoz.net/3.jpg", "Steven", "Lucas", Gender.MALE, "9579 Ignatius Centers Suite 537", false, 1));
        fakeList.add(new LeadItem(11, 789, 1, "", "Steven'", "Lucas", Gender.MALE, "9579 Ignatius Centers Suite 537", false, 2));
        fakeList.add(new LeadItem(12, 2, 12, "", "Steven", "Lucas", Gender.MALE, "9579 Ignatius Centers Suite 537", true, 2));
        fakeList.add(new LeadItem(13, 1, 3, "", "Steven", "Lucas", Gender.FEMALE, "9579 Ignatius Centers Suite 537", true, 1));
        fakeList.add(new LeadItem(14, 0, -5, null, "Steven", "Lucas", Gender.MALE, "9579 Ignatius Centers Suite 537", false, 1));
        fakeList.add(new LeadItem(15, 66, 7, "", "Steven", "Lucas", Gender.NONE, "9579 Ignatius Centers Suite 537", true, 4));


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

    @Override
    public void onItemClickListener(LeadItem item) {
        mAdapter.notifyDataSetChanged();
        getHomeActivity().getBottomNavBar().show();
        showFragment(LeadDetailsMainFragment.newInstance(item));
    }

    @Override
    public void onMessageEmailClickListener(LeadItem item) {
        mAdapter.notifyDataSetChanged();
        getHomeActivity().showErrorSnackBar("email");
    }

    @Override
    public void onMessageNoteClickListener(LeadItem item) {
        mAdapter.notifyDataSetChanged();
        getHomeActivity().showErrorSnackBar("note");
    }

    @Override
    public void onMessageSMSClickListener(LeadItem item) {
        mAdapter.notifyDataSetChanged();
        getHomeActivity().showErrorSnackBar("SMS");
    }

    @Override
    public void onCallSystemClickListener(LeadItem item) {
        mAdapter.notifyDataSetChanged();
        getHomeActivity().showErrorSnackBar("call");
    }

    @Override
    public void onCallTwillioClickListener(LeadItem item) {
        mAdapter.notifyDataSetChanged();
        getHomeActivity().showErrorSnackBar("twillio call");
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        doSearch(newText);
        return false;
    }

    private void doSearch(String newText) {
        if (TextUtils.isEmpty(newText)) {
            mItemAdapter.setData(fakeList);
            return;
        }
        final ArrayList<LeadItem> foundItemsList = new ArrayList<>();

        for (LeadItem item : fakeList) {
            String fullName = item.firstName.toLowerCase() + " " + item.lastName.toLowerCase();
            if (fullName.contains(newText.toLowerCase()) || fullName.contains(newText.toLowerCase())) {
                foundItemsList.add(item);
            }
        }

        mItemAdapter.setData(foundItemsList);
    }

}
