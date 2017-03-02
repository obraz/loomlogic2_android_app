package com.loomlogic.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by alex on 2/28/17.
 */

public abstract class BaseHomeFragment extends Fragment {
    private HomeActivity activity;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (HomeActivity) getActivity();
    }

    public HomeActivity getHomeActivity() {
        return activity;
    }

    public abstract boolean hasMenuOptions();

}
