package com.loomlogic.signin;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;

import com.loomlogic.R;
import com.loomlogic.base.BaseActivity;

/**
 * Created by alex on 2/14/17.
 */

public class BaseSignInActivity extends BaseActivity {

    protected void animateViewFadeIn(View view) {
        animateViewFadeIn(view, 0);
    }

    protected void animateViewFadeIn(View view, int offset) {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(500);
        fadeIn.setStartOffset(offset);

        view.setVisibility(View.VISIBLE);
        view.startAnimation(fadeIn);
    }

    protected void animateViewFadeOut(View view) {
        animateViewFadeOut(view, 0);
    }

    protected void animateViewFadeOut(View view, int offset) {
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setDuration(500);
        fadeOut.setStartOffset(offset);

        view.setVisibility(View.GONE);
        view.startAnimation(fadeOut);
    }

    public void setUpEditTextLeftIcon(final EditText editText) {
        editText.setTag("text");
        final int colorWhite = Color.WHITE;
        final int colorWhiteTransparent = ContextCompat.getColor(this, R.color.white_transparent_50);
        final int colorError = ContextCompat.getColor(this, R.color.errorIconColor);
        final Drawable icEditText = editText.getCompoundDrawables()[0];
        DrawableCompat.setTint(icEditText, colorWhiteTransparent);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editText.getTag().equals("error")) {
                    changeIconColor(icEditText, colorError, colorWhite);
                }
                if (s.length() == 1 && start == 0 && before == 0 && count == 1) {
                    changeIconColor(icEditText, colorWhiteTransparent, colorWhite);
                }
                if (s.length() == 0) {
                    changeIconColor(icEditText, colorWhite, colorWhiteTransparent);

                }
                editText.setTag("text");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void changeIconColor(final Drawable icon, int colorFrom, int colorTo) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(250);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                icon.mutate();
                DrawableCompat.setTint(icon, (int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
    }
}
