package com.loomlogic.signin;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kt.http.base.ResponseData;
import com.loomlogic.R;
import com.loomlogic.network.Model;
import com.loomlogic.network.managers.BaseItemManager;
import com.loomlogic.network.managers.ResetPasswordManager;
import com.loomlogic.network.responses.ResetPasswordData;
import com.loomlogic.network.responses.errors.ApiError;
import com.loomlogic.network.responses.errors.ErrorsConstant;
import com.loomlogic.utils.Utils;

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
    private ResetPasswordManager resetPasswordManager;
    private EditText forgotPasswordEmailEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_forgot_password);
        overridePendingTransition(anim_activity_left_in, anim_activity_left_out);

        initView();

        resetPasswordManager = Model.instance().getResetPasswordManager();
        resetPasswordManager.addDataFetchCompleteListener(completeListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        resetPasswordManager.removeDataFetchCompleteListener(completeListener);
    }

    private void initView() {
        forgotPasswordEmailEt = (EditText) findViewById(et_forgotPassword_email);
        setUpEditTextLeftIcon(forgotPasswordEmailEt);

        Button submitBtn = (Button) findViewById(btn_forgotPassword_submit);
        submitBtn.setOnClickListener(this);

        findViewById(btn_back).setOnClickListener(this);

        forgotPasswordEmailEt.setText("alex@tmregroup.com");
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
                if (isOnline() && validateEmail()) {
                    doResetPassword();
                }
                break;
        }
    }

    private void doResetPassword() {
        String email = forgotPasswordEmailEt.getText().toString();
        resetPasswordManager.resetPassword(email);
        showProgressBar();
    }

    private boolean validateEmail() {
        final String email = forgotPasswordEmailEt.getText().toString();
        if (!Utils.isEmailValid(email)) {
            showValidationError(forgotPasswordEmailEt);
            showErrorSnackBar(getString(R.string.email_error));
            return false;
        }
        return true;
    }

    private final BaseItemManager.OnDataFetchCompleteListener<ResetPasswordData, String> completeListener
            = new BaseItemManager.OnDataFetchCompleteListener<ResetPasswordData, String>() {

        @Override
        public void onDataFetchComplete(ResetPasswordData result, ResponseData response, String requestTag) {
            hideProgressBar();
            showCheckEmailDialog();
        }

        @Override
        public void onDataFetchFailed(ResetPasswordData result, ResponseData response, String requestTag) {
            hideProgressBar();
            if (isValidationError(response)) {
                ApiError errors = getApiError(response);
                for (String error:errors.getErrors()) {
                    if (error.equals(ErrorsConstant.ERROR_CREDENTIALS)){
                        showValidationError(forgotPasswordEmailEt);
                        break;
                    }
                }
            }
            showResponseError(response);
        }
    };

    private void showCheckEmailDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.forgotPasswordCheckEmail);
        final AlertDialog dialog = builder.setPositiveButton(android.R.string.ok, null).create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorAccent));
            }
        });

        dialog.show();
    }
}
