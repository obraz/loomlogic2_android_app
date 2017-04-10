package com.loomlogic.view.navigationbar;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.loomlogic.R;
import com.loomlogic.utils.LeadUtils;
import com.loomlogic.view.navigationbar.behaviour.BottomVerticalScrollBehavior;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 4/10/17.
 */
@CoordinatorLayout.DefaultBehavior(BottomVerticalScrollBehavior.class)
public class BottomNavigationBar extends FrameLayout {
    public interface BottomNavigationBarListener {
        void onTabSelected(int position);

        void onBarNavigationShow();

        void onBarNavigationHide();
    }

    private static final int DEFAULT_TAB_POSITION = 2;
    public static final int DEFAULT_ANIMATION_DURATION = 500;
    private BottomNavigationBarListener mBottomNavigationBarListener;
    private List<ImageView> btnIvList;
    private boolean mIsHidden = false;
    public static final Interpolator INTERPOLATOR = new LinearOutSlowInInterpolator();
    private ViewPropertyAnimatorCompat mTranslationAnimator;
    private int currentPosition;

    public BottomNavigationBar(@NonNull Context context) {
        super(context);
        init();
    }

    public BottomNavigationBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BottomNavigationBar(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public BottomNavigationBar(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View parentView = inflater.inflate(R.layout.bottom_navigation_bar_container, this, true);

        btnIvList = new ArrayList<>();
        btnIvList.add((ImageView) parentView.findViewById(R.id.tab1));
        btnIvList.add((ImageView) parentView.findViewById(R.id.tab2));
        btnIvList.add((ImageView) parentView.findViewById(R.id.tab3));
        btnIvList.add((ImageView) parentView.findViewById(R.id.tab4));
        btnIvList.add((ImageView) parentView.findViewById(R.id.tab5));

        for (int i = 0; i < btnIvList.size(); i++) {
            final int finalI = i;
            btnIvList.get(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectTab(finalI);
                }
            });
        }
        setClipToPadding(false);

        post(new Runnable() {
            @Override
            public void run() {
                btnIvList.get(DEFAULT_TAB_POSITION).performClick();
            }
        });

    }

    public BottomNavigationBar setmBottomNavigationBarListenerListener(BottomNavigationBarListener bottomNavigationBarListener) {
        this.mBottomNavigationBarListener = bottomNavigationBarListener;
        return this;
    }

    public void refreshCurrentTab() {
        selectTab(currentPosition, false);
    }

    public void selectTab(int newPosition) {
        selectTab(newPosition, true);
    }

    public void selectTab(int newPosition, boolean callListener) {
        if (callListener) {
            mBottomNavigationBarListener.onTabSelected(newPosition);
        }
        btnIvList.get(currentPosition).getDrawable().setTint(ContextCompat.getColor(getContext(), R.color.colorNavigationDefault));
        btnIvList.get(newPosition).getDrawable().setTint(ContextCompat.getColor(getContext(), LeadUtils.getCurrentLeadRoleColor()));
        currentPosition = newPosition;
    }

    public void hide() {
        mIsHidden = true;
        animateOffset(getNavBarHeight());
    }

    public void show() {
        mIsHidden = false;
        animateOffset(0);
    }

    private void animateOffset(final int offset) {
        if (mTranslationAnimator == null) {
            mTranslationAnimator = ViewCompat.animate(this);
            mTranslationAnimator.setDuration(DEFAULT_ANIMATION_DURATION);
            mTranslationAnimator.setInterpolator(INTERPOLATOR);
            mTranslationAnimator.setListener(new ViewPropertyAnimatorListener() {
                @Override
                public void onAnimationStart(View view) {
                    if (!mIsHidden) {
                        mBottomNavigationBarListener.onBarNavigationShow();
                    } else {
                        mBottomNavigationBarListener.onBarNavigationHide();
                    }
                }

                @Override
                public void onAnimationEnd(View view) {

                }

                @Override
                public void onAnimationCancel(View view) {

                }
            });
        } else {
            mTranslationAnimator.cancel();
        }
        mTranslationAnimator.translationY(offset).start();
    }

    public boolean isHidden() {
        return mIsHidden;
    }

    public int getNavBarHeight() {
        return this.getHeight();
    }
}
