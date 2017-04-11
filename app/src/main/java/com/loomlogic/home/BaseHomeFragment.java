package com.loomlogic.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.view.View;

import static com.loomlogic.view.navigationbar.BottomNavigationBar.DEFAULT_ANIMATION_DURATION;
import static com.loomlogic.view.navigationbar.BottomNavigationBar.INTERPOLATOR;

/**
 * Created by alex on 2/28/17.
 */

public abstract class BaseHomeFragment extends Fragment {
    private HomeActivity activity;
    private ViewPropertyAnimatorCompat mTranslationAnimator;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (HomeActivity) getActivity();
    }

    public HomeActivity getHomeActivity() {
        return activity;
    }

    public boolean onBackPressed(){
        return false;
    }

    public void showFragment(Fragment fragment){
        getHomeActivity().showFragment(fragment);
    }


    public void animateViewAboveNavBar(View view, boolean show) {
        if (mTranslationAnimator == null) {
            mTranslationAnimator = ViewCompat.animate(view);
            mTranslationAnimator.setDuration(DEFAULT_ANIMATION_DURATION);
            mTranslationAnimator.setInterpolator(INTERPOLATOR);
        } else {
            mTranslationAnimator.cancel();
        }
        mTranslationAnimator.translationY(!show ? getHomeActivity().getBottomNavBar().getHeight() : 0).start();
    }
}
