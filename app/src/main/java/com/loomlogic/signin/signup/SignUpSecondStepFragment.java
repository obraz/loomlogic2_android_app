package com.loomlogic.signin.signup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loomlogic.R;
import com.loomlogic.base.resultfix.ActivityResultFixFragment;

/**
 * Created by alex on 2/15/17.
 */

public class SignUpSecondStepFragment extends ActivityResultFixFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup_step_1, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //activity = (BookServiceActivity) getActivity();
        initViews(view);

    }

    private void initViews(View view) {
    }
}
