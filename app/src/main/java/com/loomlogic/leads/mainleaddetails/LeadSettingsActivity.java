package com.loomlogic.leads.mainleaddetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.loomlogic.R;
import com.loomlogic.base.BaseActivity;

/**
 * Created by alex on 4/12/17.
 */

public class LeadSettingsActivity extends BaseActivity {

    public static Intent getLeadSettingsActivityIntent(Context context) {
        Intent intent = new Intent(context, LeadSettingsActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_settings);

        findViewById(R.id.view_leadSettingsContact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.view_leadSettingsParticipants).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.view_leadSettingsContract).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (getToolbar() != null) {
            getToolbar().setNavigationIcon(R.drawable.ic_close);
            getToolbar().setTitle(R.string.lead_settings_title);
        }
    }
}
