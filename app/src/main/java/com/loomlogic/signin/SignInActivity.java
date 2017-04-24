package com.loomlogic.signin;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
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
import com.loomlogic.signin.signup.SignUpActivity;
import com.loomlogic.utils.Utils;

import static com.loomlogic.R.id.btn_signIn;


public class SignInActivity extends BaseSignInActivity implements View.OnClickListener {
    private EditText signInEmailEt;
    private EditText signInPasswordEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initView();

        if (BuildConfig.FLAVOR.equals("loomlogicDebug")) {
            //  startActivity(new Intent(SignInActivity.this, HomeActivity.class));
            // startActivity(CreateLeadActivity.getCreateLeadActivityIntent(this, false));
        }
        if (Model.instance().getLoginManager().canRestoreLogin()) {
            HomeActivity.start(this);
        }
        //  Model.instance().getRegisterManager().fetchData(null);
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


        signInEmailEt.setText("loomlogic.agent@gmail.com");
        signInPasswordEt.setText("54321");
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

//                ResponseData errorDataWrapper = (ResponseData) response.getParsedErrorResponse();
//                ApiError error = (ApiError) errorDataWrapper.getData();
//                SignInErrors errors = (SignInErrors) error.getErrors();
                if (response != null && response.getData() != null) {
                    startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                } else {
                    showResponseError(response);
                }
            }
        }.execute();
    }

    private boolean validateFields() {
        final String email = signInEmailEt.getText().toString();
        if (!Utils.isEmailValid(email)) {
            showValidationError(signInEmailEt, getString(R.string.email_error));
            return false;
        }

        final String password = signInPasswordEt.getText().toString();
        if (password.length() < 3) {
            showValidationError(signInPasswordEt, getString(R.string.password_error));
            return false;
        }

        return true;
    }

    private void showValidationError(EditText editText, String error) {
        editText.setTag("error");

        final Drawable icon = editText.getCompoundDrawables()[0];
        final int colorError = ContextCompat.getColor(this, R.color.errorIconColor);
        DrawableCompat.setTint(icon, colorError);

        showErrorSnackBar(error);
    }

}
