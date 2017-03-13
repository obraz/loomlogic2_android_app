package com.loomlogic.leads;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * Created by alex on 2/22/17.
 */

public class LeadsFragment extends BaseHomeFragment implements LeadsAdapter.OnLeadClickListener {
    private LeadsMenuManager leadsMenuManager;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter mWrappedAdapter;
    private RecyclerViewSwipeManager mRecyclerViewSwipeManager;
    private RecyclerViewTouchActionGuardManager mRecyclerViewTouchActionGuardManager;

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
        initViews(view);
    }


    private void initViews(View view) {
        LinearLayout mainContent = (LinearLayout) view.findViewById(R.id.ll_leadsMainContent);
        DrawerLayout drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        View navigationViewContainer = view.findViewById(R.id.leadMenuLayout);

        leadsMenuManager = new LeadsMenuManager(getHomeActivity(), mainContent, drawerLayout, navigationViewContainer);

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
// Gender gender, String status, boolean isFinancing, int statusCount
        ArrayList<LeadItem> fakeList = new ArrayList<>();
        fakeList.add(new LeadItem(1, 2, 3, "http://klub.life/wp-content/uploads/2017/03/%D0%91%D0%B0%D0%BD%D0%B5%D1%80-%D0%BA%D0%BB%D1%83%D0%B1-%D0%BB%D0%B0%D0%B9%D1%84-%D0%BD%D0%B0-%D0%B3%D0%BB%D0%B0%D0%B2%D0%BD%D1%83%D1%8E-%D0%94%D0%B5%D0%B5%D0%B2-%D0%A4%D0%BE%D1%82%D0%BE%D1%88%D0%BE%D0%BF-932x460.jpg", "Vasya", "Kaban", LeadItem.Gender.MALE, "escrow status 0", true, 0));
        fakeList.add(new LeadItem(2, 0, -5, "https://www.rbc.ua/static/img/_/v/_vinogorodskiy_650x410.jpg", "Vasya", "Kaban", LeadItem.Gender.MALE, "escrow status 1", true, 1));
        fakeList.add(new LeadItem(3, 100, 0, "", "Vasilina", "Kabaniha", LeadItem.Gender.FEMALE, "escrow status 2", true, 2));
        fakeList.add(new LeadItem(4, 0, -15, null, "Vasya", "Kaban", LeadItem.Gender.MALE, "escrow status 3", true, 3));
        fakeList.add(new LeadItem(5, 12, 20, "https://www.rbc.ua/static/img/9/1/9178412a31245a1fa39dda0b0bb6de07_650x410.jpg", "Vasilina", "Kabaniha", LeadItem.Gender.FEMALE, "escrow status 4", true, 4));
        fakeList.add(new LeadItem(6, 7, 5, null, "Bespoloe", "Sywestvo", LeadItem.Gender.NONE, "escrow status 5", true, 5));
        fakeList.add(new LeadItem(7, 0, 1, "https://www.rbc.ua/static/img/_/v/_vinogorodskiy_650x410.jpg", "Vasya", "Kaban", LeadItem.Gender.FEMALE, "escrow status 6", true, 6));
        fakeList.add(new LeadItem(8, 0, -7, null, "Vasya", "Kaban", LeadItem.Gender.MALE, "escrow status 7", true, 7));
        fakeList.add(new LeadItem(9, 5, 21, "https://www.rbc.ua/static/img/_/v/_vinogorodskiy_650x410.jpg", "Vasya", "Kaban", LeadItem.Gender.FEMALE, "escrow status 0", false, 0));
        fakeList.add(new LeadItem(10, 15, -22, null, "Vasya", "Kaban", LeadItem.Gender.MALE, "escrow status 1", false, 1));
        fakeList.add(new LeadItem(11, 789, 1, "", "Ochen'", "Dlinnoe Imjaaaaaaaaaaaaaaaaaaa", LeadItem.Gender.MALE, "escrow status 2", false, 2));
        fakeList.add(new LeadItem(12, 2, 12, "http://mozuanbotoks.newbloggs.xyz/doc-rus-1.jpeg", "Noname", "", LeadItem.Gender.MALE, "escrow status 2", true, 2));
        fakeList.add(new LeadItem(13, 1, 3, "http://i1.nuttit.com/nsfw/Fresh_peaches_28188407.jpg", "Prosto", "Siski", LeadItem.Gender.FEMALE, "escrow status 1", true, 1));
        fakeList.add(new LeadItem(14, 0, -5, null, "Vasya", "Kaban", LeadItem.Gender.MALE, "escrow status 1", false, 1));
        fakeList.add(new LeadItem(15, 66, 7, "", "Kakoeto", "Imja", LeadItem.Gender.NONE, "escrow status 4", true, 4));


        LeadsAdapter mItemAdapter = new LeadsAdapter(getContext(), this);
        mItemAdapter.setData(fakeList);

        mAdapter = mItemAdapter;

        mWrappedAdapter = mRecyclerViewSwipeManager.createWrappedAdapter(mItemAdapter);      // wrap for swiping

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
        getHomeActivity().showErrorSnackBar("onItemClickListener");
        mAdapter.notifyDataSetChanged();
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
/*

    private Spannable getPhoneFormattedText(String title, String phone) {
        String txt = title + "\n" + phone;
        Spannable sb = new SpannableString(txt);
        sb.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.accent)), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new RelativeSizeSpan(0.8f), title.length(), txt.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }*/
}
