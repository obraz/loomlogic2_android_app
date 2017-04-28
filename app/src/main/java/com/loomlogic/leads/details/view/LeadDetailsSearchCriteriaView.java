package com.loomlogic.leads.details.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loomlogic.R;

import static com.loomlogic.utils.AnimationUtils.collapse;
import static com.loomlogic.utils.AnimationUtils.expand;

/**
 * Created by alex on 3/16/17.
 */

public class LeadDetailsSearchCriteriaView extends LinearLayout {
    private boolean isVisible = false;

    public LeadDetailsSearchCriteriaView(Context context) {
        super(context);
        initViews();
    }

    public LeadDetailsSearchCriteriaView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public LeadDetailsSearchCriteriaView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    public LeadDetailsSearchCriteriaView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews();
    }

    private void initViews() {
        inflate(getContext(), R.layout.view_lead_details_search_criteria, this);
    }

    public void setSearchCriteria(Object searchCriteria) {
        setSearchCriteriaData(searchCriteria);
        setShowMoreBtn();
    }

    private void setShowMoreBtn() {
        final View divider = findViewById(R.id.divider);
        final LinearLayout infoLayout = (LinearLayout) findViewById(R.id.ll_leadDetailsContractContainer);

        final ImageView showMoreIcon = (ImageView) findViewById(R.id.iv_showMore);
        findViewById(R.id.ll_headerContainer).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVisible) {
                    showMoreIcon.setRotation(0);
                    divider.setVisibility(GONE);
                    collapse(infoLayout);
                } else {
                    showMoreIcon.setRotation(180);
                    divider.setVisibility(VISIBLE);
                    expand(infoLayout);
                }
                isVisible = !isVisible;
            }
        });
    }

    private void setSearchCriteriaData(Object searchCriteria) {
        TextView bedroomsTv = (TextView) findViewById(R.id.tv_leadDetailsSearch_bedrooms);
        bedroomsTv.setText("3");

        TextView bathroomTv = (TextView) findViewById(R.id.tv_leadDetailsSearch_bathroom);
        bathroomTv.setText("2");

        TextView purchasePriceTv = (TextView) findViewById(R.id.tv_leadDetailsSearch_purchase);
        purchasePriceTv.setText("$350,000");

        TextView areaTv = (TextView) findViewById(R.id.tv_leadDetailsSearch_area);
        areaTv.setText("125 ft²");

    }

}
