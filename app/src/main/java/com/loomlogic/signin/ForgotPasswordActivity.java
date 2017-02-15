package com.loomlogic.signin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static com.loomlogic.R.anim.anim_activity_left_in;
import static com.loomlogic.R.anim.anim_activity_left_out;
import static com.loomlogic.R.anim.anim_activity_right_in;
import static com.loomlogic.R.anim.anim_activity_right_out;
import static com.loomlogic.R.id.btn_back;
import static com.loomlogic.R.id.btn_forgotPassword_submit;
import static com.loomlogic.R.id.et_forgotPassword_email;
import static com.loomlogic.R.layout.activity_forgot_password;

/**
 * Created by alex on 2/14/17.
 */

public class ForgotPasswordActivity extends BaseSignInActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_forgot_password);
        overridePendingTransition(anim_activity_left_in, anim_activity_left_out);

        initView();
    }

    private void initView() {
        EditText forgotPasswordEmailEt = (EditText) findViewById(et_forgotPassword_email);
        setUpEditTextLeftIcon(forgotPasswordEmailEt);

        Button submitBtn = (Button) findViewById(btn_forgotPassword_submit);
        submitBtn.setOnClickListener(this);

        findViewById(btn_back).setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(anim_activity_right_in, anim_activity_right_out);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case btn_back:
                onBackPressed();
                break;
            case btn_forgotPassword_submit:

                break;
        }
    }
}
