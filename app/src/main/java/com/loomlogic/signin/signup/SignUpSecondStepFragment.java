package com.loomlogic.signin.signup;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.loomlogic.R;
import com.loomlogic.base.resultfix.ActivityResultFixFragment;

import static com.loomlogic.R.id.et_signUp_license;
import static com.loomlogic.R.id.et_signUp_phone;
import static com.loomlogic.R.id.rb_signUp_agent;
import static com.loomlogic.R.id.rb_signUp_lender;
import static com.loomlogic.R.id.rg_signUp_role;

/**
 * Created by alex on 2/15/17.
 */

public class SignUpSecondStepFragment extends ActivityResultFixFragment {
    private SignUpActivity activity;
    private EditText phoneEt;
    private EditText licenseEt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup_step_2, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (SignUpActivity) getActivity();
        initViews(view);
    }

    private void initViews(View view) {
        final int colorWhite = Color.WHITE;
        final int colorWhiteTransparent = ContextCompat.getColor(getContext(), R.color.white_transparent_50);

        RadioGroup roleGroup = (RadioGroup) view.findViewById(rg_signUp_role);
        final RadioButton agentRole = (RadioButton) view.findViewById(rb_signUp_agent);
        final RadioButton lenderRole = (RadioButton) view.findViewById(rb_signUp_lender);

        roleGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case rb_signUp_agent:
                        changeRadioBtnTextColor(agentRole, colorWhiteTransparent, colorWhite);
                        changeRadioBtnTextColor(lenderRole, colorWhite, colorWhiteTransparent);
                        break;
                    case rb_signUp_lender:
                        changeRadioBtnTextColor(agentRole, colorWhite, colorWhiteTransparent);
                        changeRadioBtnTextColor(lenderRole, colorWhiteTransparent, colorWhite);
                        break;
                }
            }
        });


        phoneEt = (EditText) view.findViewById(et_signUp_phone);
        activity.setUpEditTextLeftIcon(phoneEt);

        licenseEt = (EditText) view.findViewById(et_signUp_license);
        activity.setUpEditTextLeftIcon(licenseEt);

        view.findViewById(R.id.btn_signUp_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void changeRadioBtnTextColor(final RadioButton radio, int colorFrom, int colorTo) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(250);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                radio.setTextColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
    }
}
