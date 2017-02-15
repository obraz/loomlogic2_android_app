package com.loomlogic.signin.signup;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.loomlogic.R;
import com.loomlogic.signin.BaseSignInActivity;
import com.loomlogic.view.ViewPagerSpeedScroller;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.lang.reflect.Field;

import static com.loomlogic.R.anim.anim_activity_left_in;
import static com.loomlogic.R.anim.anim_activity_left_out;
import static com.loomlogic.R.anim.anim_activity_right_in;
import static com.loomlogic.R.anim.anim_activity_right_out;
import static com.loomlogic.R.id.btn_back;
import static com.loomlogic.R.id.btn_forgotPassword_submit;
import static com.loomlogic.R.id.tv_signUp_privacy;
import static com.loomlogic.R.id.tv_signUp_terms;
import static com.loomlogic.R.layout.activity_sign_up;

/**
 * Created by alex on 2/14/17.
 */

public class SignUpActivity extends BaseSignInActivity implements View.OnClickListener {
    private ViewPager viewPager;
    private View termContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_sign_up);
        overridePendingTransition(anim_activity_left_in, anim_activity_left_out);

        initView();
    }

    private void initView() {
        termContainer = findViewById(R.id.ll_terms_container);

        KeyboardVisibilityEvent.setEventListener(this, new KeyboardVisibilityEventListener() {
            @Override
            public void onVisibilityChanged(boolean isOpen) {
                updateKeyboardStatus(isOpen);
            }
        });
        updateKeyboardStatus(KeyboardVisibilityEvent.isKeyboardVisible(this));

        findViewById(btn_back).setOnClickListener(this);

        SignUpFragmentAdapter adapter = new SignUpFragmentAdapter(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount());

        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            ViewPagerSpeedScroller scroller = new ViewPagerSpeedScroller(viewPager.getContext());
            mScroller.set(viewPager, scroller);
        } catch (Exception e) {
        }

        Paint p = new Paint();
        p.setFlags(Paint.UNDERLINE_TEXT_FLAG);

        TextView termsTv = (TextView) findViewById(tv_signUp_terms);
        termsTv.setPaintFlags(p.getFlags());
        termsTv.setOnClickListener(this);

        TextView privacyTv = (TextView) findViewById(tv_signUp_privacy);
        privacyTv.setPaintFlags(p.getFlags());
        privacyTv.setOnClickListener(this);
    }

    private void updateKeyboardStatus(boolean isOpen) {
        termContainer.setVisibility(isOpen ? View.GONE : View.VISIBLE);
    }

    public void showSignUpStep1Fragment() {
        viewPager.setCurrentItem(0);
    }

    public void showSignUpStep2Fragment() {
        viewPager.setCurrentItem(1);
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() > 0) {
            showSignUpStep1Fragment();
            return;
        }
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
            case tv_signUp_terms:

                break;
            case tv_signUp_privacy:

                break;
        }
    }

}
