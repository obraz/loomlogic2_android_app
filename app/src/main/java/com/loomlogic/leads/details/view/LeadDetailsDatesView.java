package com.loomlogic.leads.details.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.loomlogic.R;

import static com.loomlogic.utils.AnimationUtils.collapse;
import static com.loomlogic.utils.AnimationUtils.expand;

/**
 * Created by alex on 3/16/17.
 */

public class LeadDetailsDatesView extends LinearLayout {
    private boolean isVisible = false;

    public LeadDetailsDatesView(Context context) {
        super(context);
        initViews();
    }

    public LeadDetailsDatesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public LeadDetailsDatesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    public LeadDetailsDatesView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews();
    }

    private void initViews() {
        inflate(getContext(), R.layout.view_lead_details_dates, this);
        setShowMoreBtn();
    }


    private void setShowMoreBtn() {
        final LinearLayout infoLayout = (LinearLayout) findViewById(R.id.ll_leadDetailsDatesContainer);

        final ImageButton showMoreBtn = (ImageButton) findViewById(R.id.ib_showMore);
        showMoreBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVisible) {
                    showMoreBtn.setRotation(0);
                    collapse(infoLayout);
                } else {
                    showMoreBtn.setRotation(180);
                    expand(infoLayout);
                }
                isVisible = !isVisible;
            }
        });
    }

}
