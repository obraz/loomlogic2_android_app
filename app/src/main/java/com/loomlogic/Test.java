package com.loomlogic;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loomlogic.base.BaseActivity;
import com.loomlogic.leads.base.LeadAvatarView;

/**
 * Created by alex on 2/14/17.
 */

public class Test extends BaseActivity {
    private ProgressBar barTimer;
    private TextView textTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        barTimer = (ProgressBar) findViewById(R.id.barTimer);
        textTimer = (TextView) findViewById(R.id.tvBarProgress);

        // barTimer.getProgressDrawable().setTint(Color.BLUE);
        LeadAvatarView avatarView = (LeadAvatarView) findViewById(R.id.claimAvatar);
        avatarView.setAvatar(null, "e", "w");

        startTimer(1);

    }

    private void startTimer(final int minuti) {
        barTimer.setMax(60 * minuti);
        CountDownTimer countDownTimer = new CountDownTimer(60 * minuti * 1000, 1000) {
            // 500 means, onTick function will be called at every 500 milliseconds

            @Override
            public void onTick(long leftTimeInMilliseconds) {
                long seconds = leftTimeInMilliseconds / 1000;
                barTimer.setProgress((int) seconds);
                textTimer.setText(String.format("%02d", seconds / 60) + ":" + String.format("%02d", seconds % 60));
                // format the textview to show the easily readable format

            }

            @Override
            public void onFinish() {
                if (textTimer.getText().equals("00:00")) {
                    textTimer.setText("STOP");
                } else {
                    textTimer.setText("2:00");
                    barTimer.setProgress(60 * minuti);
                }
            }
        }.start();

    }
}
