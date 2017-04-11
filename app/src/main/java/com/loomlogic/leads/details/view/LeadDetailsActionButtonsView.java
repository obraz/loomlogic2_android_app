package com.loomlogic.leads.details.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.loomlogic.R;
import com.loomlogic.home.HomeActivity;
import com.loomlogic.utils.ViewUtils;


/**
 * Created by alex on 4/10/17.
 */

public class LeadDetailsActionButtonsView extends RelativeLayout implements View.OnClickListener {
    enum ActionState {MAIN_SHOW, MAIN_HIDE, CALL, MESSAGE}

    private static final int ANIMATION_DURATION = 300;
    private static final int ANIMATION_SECOND_BUTTON_DELAY = ANIMATION_DURATION / 3;
    private static final int ANIMATION_THIRD_BUTTON_DELAY = ANIMATION_DURATION / 2;

    private float animationOffset;
    private ActionState currentState;
    private View callBtnView, msgBtnView, callTwillioBtnView, callSystemBtnView, msgNoteBtnView, msgEmailBtnView, msgSmsBtnView;
    private String phoneNumber;

    public LeadDetailsActionButtonsView(Context context) {
        super(context);
        initViews();
    }

    public LeadDetailsActionButtonsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public LeadDetailsActionButtonsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    public LeadDetailsActionButtonsView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews();
    }

    private void initViews() {
        inflate(getContext(), R.layout.view_lead_details_action_buttons, this);
        callBtnView = findViewById(R.id.view_leadCall);
        msgBtnView = findViewById(R.id.view_leadMsg);

        callTwillioBtnView = findViewById(R.id.view_leadCall_twillio);
        callSystemBtnView = findViewById(R.id.view_leadCall_system);

        msgNoteBtnView = findViewById(R.id.view_leadMsg_note);
        msgEmailBtnView = findViewById(R.id.view_leadMsg_email);
        msgSmsBtnView = findViewById(R.id.view_leadMsg_SMS);

        animationOffset = getResources().getDimension(R.dimen.action_btn_size) + getResources().getDimension(R.dimen.action_btn_margins);
        ViewCompat.setTranslationY(callSystemBtnView, animationOffset);
        ViewCompat.setTranslationY(callTwillioBtnView, animationOffset);
        ViewCompat.setTranslationY(msgNoteBtnView, animationOffset);
        ViewCompat.setTranslationY(msgEmailBtnView, animationOffset);
        ViewCompat.setTranslationY(msgSmsBtnView, animationOffset);

        callBtnView.setOnClickListener(this);
        msgBtnView.setOnClickListener(this);
        callTwillioBtnView.setOnClickListener(this);
        callSystemBtnView.setOnClickListener(this);
        msgNoteBtnView.setOnClickListener(this);
        msgEmailBtnView.setOnClickListener(this);
        msgSmsBtnView.setOnClickListener(this);

        currentState = ActionState.MAIN_SHOW;

        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (currentState == ActionState.MAIN_SHOW || currentState == ActionState.MAIN_HIDE) {
                    return false;
                }

                if (currentState == ActionState.CALL) {
                    removeActionBg();
                    hideCallActionButtons();
                    showMainActionButtons();
                } else if (currentState == ActionState.MESSAGE) {
                    setBackground(null);
                    hideMsgActionButtons();
                    showMainActionButtons();
                }
                return true;
            }
        });
    }

    public void onMsgClick() {
        setActionBg();
        hideMainActionButtons();
        showMsgActionButtons();
    }

    public void onCallClick() {
        setActionBg();
        hideMainActionButtons();
        showCallActionButtons();
    }

    private void setActionBg() {
        ViewUtils.animViewBgColor(this, Color.TRANSPARENT, ContextCompat.getColor(getContext(), R.color.lead_details_action_buttons_bg_color), ANIMATION_DURATION);
        // setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lead_details_action_buttons_bg_color));
    }

    private void removeActionBg() {
        ViewUtils.animViewBgColor(this, ContextCompat.getColor(getContext(), R.color.lead_details_action_buttons_bg_color), Color.TRANSPARENT, ANIMATION_DURATION);
        // setBackgroundColor(null);
    }

    public void showMainActionButtons() {
        if (currentState == ActionState.MAIN_SHOW) {
            return;
        }
        currentState = ActionState.MAIN_SHOW;
        animateShowActionBtn(callBtnView, ANIMATION_DURATION);
        animateShowActionBtn(msgBtnView, ANIMATION_DURATION + ANIMATION_SECOND_BUTTON_DELAY);
    }

    public void hideMainActionButtons() {
        if (currentState == ActionState.MAIN_HIDE) {
            return;
        }
        currentState = ActionState.MAIN_HIDE;
        animateHideActionBtn(callBtnView, 0);
        animateHideActionBtn(msgBtnView, ANIMATION_SECOND_BUTTON_DELAY);
    }

    private void showCallActionButtons() {
        currentState = ActionState.CALL;
        animateShowActionBtn(callSystemBtnView, ANIMATION_DURATION);
        animateShowActionBtn(callTwillioBtnView, ANIMATION_DURATION + ANIMATION_SECOND_BUTTON_DELAY);
    }

    private void hideCallActionButtons() {
        animateHideActionBtn(callSystemBtnView, 0);
        animateHideActionBtn(callTwillioBtnView, ANIMATION_SECOND_BUTTON_DELAY);
    }

    private void showMsgActionButtons() {
        currentState = ActionState.MESSAGE;
        animateShowActionBtn(msgNoteBtnView, ANIMATION_DURATION);
        animateShowActionBtn(msgEmailBtnView, ANIMATION_DURATION + ANIMATION_SECOND_BUTTON_DELAY);
        animateShowActionBtn(msgSmsBtnView, ANIMATION_DURATION + ANIMATION_SECOND_BUTTON_DELAY + ANIMATION_THIRD_BUTTON_DELAY);
    }

    private void hideMsgActionButtons() {
        animateHideActionBtn(msgNoteBtnView, 0);
        animateHideActionBtn(msgEmailBtnView, ANIMATION_SECOND_BUTTON_DELAY);
        animateHideActionBtn(msgSmsBtnView, ANIMATION_THIRD_BUTTON_DELAY);
    }

    private void animateHideActionBtn(View view, int delay) {
        AnimatorSet as = new AnimatorSet();
        as.playTogether(
                ObjectAnimator.ofFloat(view, "translationY", 0, animationOffset),
                ObjectAnimator.ofFloat(view, "alpha", 1, 0));
        as.setDuration(ANIMATION_DURATION);
        as.setStartDelay(delay);
        as.start();
    }

    private void animateShowActionBtn(View view, int delay) {
        AnimatorSet as = new AnimatorSet();
        as.playTogether(
                ObjectAnimator.ofFloat(view, "translationY", animationOffset, 0),
                ObjectAnimator.ofFloat(view, "alpha", 0, 1));
        as.setDuration(ANIMATION_DURATION);
        as.setStartDelay(delay);
        as.start();
    }

    public void setPhoneNumber(String phone) {
        this.phoneNumber = phone;
    }

    public int getActionButtonTopOffset() {
        return callBtnView.getTop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_leadCall:
                onCallClick();
                break;
            case R.id.view_leadMsg:
                onMsgClick();
                break;
            case R.id.view_leadCall_system:
                ((HomeActivity) getContext()).openDialIntent(phoneNumber);
                break;
            case R.id.view_leadCall_twillio:
                ((HomeActivity) getContext()).showErrorSnackBar("twillio");
                break;
            case R.id.view_leadMsg_note:
                ((HomeActivity) getContext()).showErrorSnackBar("note");
                break;
            case R.id.view_leadMsg_email:
                ((HomeActivity) getContext()).showErrorSnackBar("email");
                break;
            case R.id.view_leadMsg_SMS:
                ((HomeActivity) getContext()).showErrorSnackBar("sms");
                break;
        }
    }
}
