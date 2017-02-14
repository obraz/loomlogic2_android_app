package com.loomlogic;

import android.animation.ArgbEvaluator;
import android.animation.LayoutTransition;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.loomlogic.base.BaseActivity;

import static com.loomlogic.R.id.btn_signIn;

public class SignInActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initView();
    }

    private void initView() {
        RelativeLayout mContentView = (RelativeLayout) findViewById(R.id.fullscreen_content);
        mContentView.setLayoutTransition(new LayoutTransition());

        final View logoView = findViewById(R.id.iv_logo);

        final View createAccountView = findViewById(R.id.btn_createAccount);
        createAccountView.setOnClickListener(this);

        final View forgotPasswordView = findViewById(R.id.btn_forgotPassword);
        forgotPasswordView.setOnClickListener(this);

        final View signInContainerView = findViewById(R.id.ll_signIn_container);

        final EditText signInEmailEt = (EditText) findViewById(R.id.et_signIn_email);
        setUpEditTextIcon(signInEmailEt);

        final EditText signInPasswordEt = (EditText) findViewById(R.id.et_signIn_password);
        setUpEditTextIcon(signInPasswordEt);

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
                animateViewFadeIn(signInBtn, 1000);
                animateViewFadeIn(createAccountView, 2000);
                animateViewFadeIn(forgotPasswordView, 2000);
                showControls();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        logoView.startAnimation(splashAnim);
    }

    private void setUpEditTextIcon(EditText editText) {
        final int colorWhite = Color.WHITE;
        final int colorWhiteTransparent = ContextCompat.getColor(SignInActivity.this, R.color.white_transparent_50);
        final Drawable icEditText = editText.getCompoundDrawables()[0];
        DrawableCompat.setTint(icEditText, colorWhiteTransparent);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1 && start == 0 && before == 0 && count == 1) {
                    changeIconColor(icEditText, colorWhiteTransparent, colorWhite);
                }
                if (s.length() == 0) {
                    changeIconColor(icEditText, colorWhite, colorWhiteTransparent);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void changeIconColor(final Drawable icon, int colorFrom, int colorTo) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(250);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                DrawableCompat.setTint(icon, (int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
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

    private void animateViewFadeIn(View view, int offset) {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(500);
        fadeIn.setStartOffset(offset);

        view.setVisibility(View.VISIBLE);
        view.startAnimation(fadeIn);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_createAccount:
                break;
            case R.id.btn_forgotPassword:
                break;
            case btn_signIn:
                break;
        }
    }
}
