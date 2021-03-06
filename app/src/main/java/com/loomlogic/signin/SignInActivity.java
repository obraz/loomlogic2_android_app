package com.loomlogic.signin;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.kt.http.base.ResponseData;
import com.loomlogic.BuildConfig;
import com.loomlogic.R;
import com.loomlogic.home.HomeActivity;
import com.loomlogic.network.Model;
import com.loomlogic.network.responses.ResponseDataWrapper;
import com.loomlogic.network.responses.UserData;
import com.loomlogic.network.responses.errors.ErrorsConstant;
import com.loomlogic.signin.signup.SignUpActivity;
import com.loomlogic.utils.Utils;

import java.util.List;

import static com.loomlogic.R.id.btn_signIn;


public class SignInActivity extends BaseSignInActivity implements View.OnClickListener {
    private EditText signInEmailEt;
    private EditText signInPasswordEt;

    public static void start(@NonNull Activity activity) {
        Model.instance().performLogout();

        Intent intent = new Intent(activity, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initView();

        if (BuildConfig.FLAVOR.equals("loomlogicDebug")) {
            // startActivity(new Intent(SignInActivity.this, SendToAgentActivity.class));
            // startActivity(CreateLeadActivity.getCreateLeadActivityIntent(this, false));
        }
        if (Model.instance().getLoginManager().canRestoreLogin()) {
            HomeActivity.start(this);
            //    startActivity(CreateLeadActivity.getCreateLeadActivityIntent(this, false));
        }//HomeActivity.start(this);
    }


    private void initView() {
        RelativeLayout mContentView = (RelativeLayout) findViewById(R.id.fullscreen_content);
        mContentView.setLayoutTransition(new LayoutTransition());

        final View logoView = findViewById(R.id.iv_logo);

        final View createAccountView = findViewById(R.id.btn_createAccount);
        createAccountView.setVisibility(View.INVISIBLE);
        // createAccountView.setOnClickListener(this);

        final View forgotPasswordView = findViewById(R.id.btn_forgotPassword);
        forgotPasswordView.setOnClickListener(this);

        final View signInContainerView = findViewById(R.id.ll_signIn_container);

        signInEmailEt = (EditText) findViewById(R.id.et_signIn_email);
        setUpEditTextLeftIcon(signInEmailEt);

        signInPasswordEt = (EditText) findViewById(R.id.et_signIn_password);
        setUpEditTextLeftIcon(signInPasswordEt);

        final ImageButton passwordVisibilityView = (ImageButton) findViewById(R.id.ib_passswordVisibility);
        passwordVisibilityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (signInPasswordEt.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)) {
                    signInPasswordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordVisibilityView.setImageResource(R.drawable.ic_password_show);
                } else {
                    signInPasswordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordVisibilityView.setImageResource(R.drawable.ic_password_hide);
                }
                signInPasswordEt.setSelection(signInPasswordEt.getText().length());
            }
        });

        final Button signInBtn = (Button) findViewById(btn_signIn);
        signInBtn.setOnClickListener(this);

        Animation splashAnim = AnimationUtils.loadAnimation(this, R.anim.anim_splash);
        splashAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                logoView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                signInContainerView.setVisibility(View.VISIBLE);
                animateViewFadeIn(signInEmailEt, 0);
                animateViewFadeIn(signInPasswordEt, 500);
                animateViewFadeIn(passwordVisibilityView, 500);
                animateViewFadeIn(signInBtn, 1000);
                // animateViewFadeIn(createAccountView, 2000);
                animateViewFadeIn(forgotPasswordView, 2000);
                showControls();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        logoView.startAnimation(splashAnim);

        if (BuildConfig.FLAVOR.equals("loomlogicDebug")) {
            signInEmailEt.setText("loomlogic.agent@gmail.com");
            signInPasswordEt.setText("54321");

            signInEmailEt.setText("arttheft89@gmail.com");
            signInPasswordEt.setText("qwertyui");


             signInEmailEt.setText("alex@tmregroup.com");
             signInPasswordEt.setText("password");

            // signInEmailEt.setText("olegdfgdfg@tmregroup.com");
            // signInEmailEt.setText("alexandrobraz@gmail.com");
            //
            // signInPasswordEt.setText("");
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        hideControls();
    }

    private void hideControls() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
    }

    private void showControls() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_createAccount:
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
                break;
            case R.id.btn_forgotPassword:
                startActivity(new Intent(SignInActivity.this, ForgotPasswordActivity.class));
                break;
            case R.id.btn_signIn:
                doLogin();
                break;
        }
    }

    private void doLogin() {
        if (!isOnline()) {
            return;
        }
        if (!validateFields()) {
            return;
        }
        final String email = signInEmailEt.getText().toString();
        final String password = signInPasswordEt.getText().toString();

        showProgressBar();
        new AsyncTask<Void, Void, ResponseData>() {

            @Override
            protected ResponseData doInBackground(Void... params) {
                ResponseData response = Model.instance().performLogin(email, password);
                return response;
            }

            @Override
            protected void onPostExecute(ResponseData response) {
                super.onPostExecute(response);
                hideProgressBar();

                if (response != null && response.getData() != null) {
                    ResponseDataWrapper dataWrapper = (ResponseDataWrapper) response.getData();
                    UserData userData = (UserData) dataWrapper.getData();
                    if (dataWrapper.isSuccess() && userData != null) {
                        HomeActivity.start(SignInActivity.this);
                    } else {
                        if (isValidationError(response)) {
                            List<String> errors = dataWrapper.getErrors();
                            for (String error : errors) {
                                if (error.equals(ErrorsConstant.ERROR_CREDENTIALS)) {
                                    showValidationError(signInEmailEt);
                                    showValidationError(signInPasswordEt);
                                    break;
                                }
                            }
                        }
                        showResponseError(response);
                    }

                } else {
                    showResponseError(response);
                }
            }
        }.execute();
    }

    private boolean validateFields() {
        final String email = signInEmailEt.getText().toString();
        if (!Utils.isEmailValid(email)) {
            showValidationError(signInEmailEt);
            showErrorSnackBar(getString(R.string.email_error));
            return false;
        }

        final String password = signInPasswordEt.getText().toString();
        if (!Utils.isPasswordValid(password)) {
            showValidationError(signInPasswordEt);
            showErrorSnackBar(getString(R.string.password_error));
            return false;
        }

        return true;
    }

}
