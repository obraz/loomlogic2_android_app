package com.loomlogic.signin;

import android.content.Intent;
import android.os.Bundle;

import com.loomlogic.BuildConfig;
import com.loomlogic.R;
import com.loomlogic.home.HomeActivity;

/**
 * Created by alex on 4/25/17.
 */

public class ResetPasswordActivity extends BaseSignInActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initView(); 
    }

    private void initView() {
    }
}
