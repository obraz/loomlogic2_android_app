package com.loomlogic.signin.signup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.loomlogic.R;
import com.loomlogic.base.resultfix.ActivityResultFixFragment;

import static com.loomlogic.R.id.et_signUp_email;
import static com.loomlogic.R.id.et_signUp_password;

/**
 * Created by alex on 2/15/17.
 */

public class SignUpFirstStepFragment extends ActivityResultFixFragment {
    private SignUpActivity activity;
    private EditText emailEt;
    private EditText passwordEt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup_step_1, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (SignUpActivity) getActivity();
        initViews(view);
    }

    private void initViews(View view) {
        emailEt = (EditText) view.findViewById(et_signUp_email);
        activity.setUpEditTextLeftIcon(emailEt);

        passwordEt = (EditText) view.findViewById(et_signUp_password);
        activity.setUpEditTextLeftIcon(passwordEt);

        final ImageButton passwordVisibilityView = (ImageButton) view.findViewById(R.id.ib_passswordVisibility);
        passwordVisibilityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordEt.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)) {
                    passwordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordVisibilityView.setImageResource(R.drawable.ic_password_show);
                } else {
                    passwordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordVisibilityView.setImageResource(R.drawable.ic_password_hide);
                }
                passwordEt.setSelection(passwordEt.getText().length());
            }
        });

        view.findViewById(R.id.btn_signUp_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.showSignUpStep2Fragment();
            }
        });
    }
}
