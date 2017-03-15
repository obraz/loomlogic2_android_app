package com.loomlogic.leads.details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loomlogic.R;
import com.loomlogic.home.BaseHomeFragment;

/**
 * Created by alex on 3/2/17.
 */

public class DefaultLeadDetailsFragment extends BaseHomeFragment {

    public static DefaultLeadDetailsFragment newInstance() {
        DefaultLeadDetailsFragment fragment = new DefaultLeadDetailsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_default, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
