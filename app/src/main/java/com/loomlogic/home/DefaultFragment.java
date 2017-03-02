package com.loomlogic.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loomlogic.R;

/**
 * Created by alex on 3/2/17.
 */

public class DefaultFragment extends BaseHomeFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_default, container, false);
    }

    @Override
    public boolean hasMenuOptions() {
        return false;
    }
}
