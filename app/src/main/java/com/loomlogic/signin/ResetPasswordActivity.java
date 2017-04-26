package com.loomlogic.signin;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.kt.http.base.ResponseData;
import com.loomlogic.R;
import com.loomlogic.network.Model;
import com.loomlogic.network.managers.BaseItemManager;
import com.loomlogic.network.managers.ResetPasswordManager;
import com.loomlogic.network.responses.ResetPasswordData;
import com.loomlogic.utils.Utils;

import static com.loomlogic.R.id.btn_resetPassword;

/**
 * Created by alex on 4/25/17.
 */
//http://serzub1.freehostia.com/test_reset.html?reset_password_token=zXARy2E8XB-uSbtQsJNM
public class ResetPasswordActivity extends BaseSignInActivity {
    private static final String QUERY_RESET_PASSWORD_TOKEN = "reset_password_token";

    private EditText resetPasswordEt;
    private ResetPasswordManager resetPasswordManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initView();
        Model.instance().performLogout();

        resetPasswordManager = Model.instance().getResetPasswordManager();
        resetPasswordManager.addDataFetchCompleteListener(completeListener);

        showProgressBar();
        String token = getIntent().getData().getQueryParameter(QUERY_RESET_PASSWORD_TOKEN);
        resetPasswordManager.resetPasswordAuth(token);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        resetPasswordManager.removeDataFetchCompleteListener(completeListener);
    }

    private void initView() {
        resetPasswordEt = (EditText) findViewById(R.id.et_reset_password);
        setUpEditTextLeftIcon(resetPasswordEt);

        final ImageButton passwordVisibilityView = (ImageButton) findViewById(R.id.ib_passswordVisibility);
        passwordVisibilityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resetPasswordEt.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)) {
                    resetPasswordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordVisibilityView.setImageResource(R.drawable.ic_password_show);
                } else {
                    resetPasswordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordVisibilityView.setImageResource(R.drawable.ic_password_hide);
                }
                resetPasswordEt.setSelection(resetPasswordEt.getText().length());
            }
        });

        findViewById(btn_resetPassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validatePassword()) {
                    showProgressBar();
                    resetPasswordManager.resetPasswordComplete(resetPasswordEt.getText().toString());
                }
            }
        });
    }

    private final BaseItemManager.OnDataFetchCompleteListener<ResetPasswordData, ResetPasswordAction> completeListener
            = new BaseItemManager.OnDataFetchCompleteListener<ResetPasswordData, ResetPasswordAction>() {

        @Override
        public void onDataFetchComplete(ResetPasswordData result, ResponseData response, ResetPasswordAction requestTag) {
            hideProgressBar();
            switch (requestTag) {
                case AUTH:
                    break;
                case COMPLETE:
                    SignInActivity.start(ResetPasswordActivity.this);
                    break;
            }
        }

        @Override
        public void onDataFetchFailed(ResetPasswordData result, ResponseData response, ResetPasswordAction requestTag) {
            hideProgressBar();
            showResponseError(response);
            switch (requestTag) {
                case AUTH:
                    SignInActivity.start(ResetPasswordActivity.this);
                    break;
                case COMPLETE:

                    break;
            }
        }
    };

    private boolean validatePassword() {
        final String password = resetPasswordEt.getText().toString();
        if (!Utils.isPasswordValid(password)) {
            showValidationError(resetPasswordEt);
            showErrorSnackBar(getString(R.string.password_error));
            return false;
        }
        return true;
    }
}
