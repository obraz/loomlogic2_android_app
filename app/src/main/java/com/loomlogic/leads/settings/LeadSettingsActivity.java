package com.loomlogic.leads.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.loomlogic.R;
import com.loomlogic.base.BaseActivity;
import com.loomlogic.leads.entity.LeadItem;

import static com.loomlogic.leads.mainleaddetails.LeadDetailsMainFragment.KEY_LEAD_ITEM;

/**
 * Created by alex on 4/12/17.
 */

public class LeadSettingsActivity extends BaseActivity {

    public static Intent getLeadSettingsActivityIntent(Context context, LeadItem leadItem) {
        Intent intent = new Intent(context, LeadSettingsActivity.class);
        intent.putExtra(KEY_LEAD_ITEM, new Gson().toJson(leadItem));
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_settings);

        final LeadItem leadItem = new Gson().fromJson(getIntent().getStringExtra(KEY_LEAD_ITEM), LeadItem.class);

        findViewById(R.id.view_leadSettingsContact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(EditLeadActivity.getEditLeadActivityIntent(LeadSettingsActivity.this, null));
            }
        });

        findViewById(R.id.view_leadSettingsParticipants).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ParticipantsListActivity.getParticipantsListActivityIntent(LeadSettingsActivity.this, leadItem.participantList));
            }
        });

        findViewById(R.id.view_leadSettingsContract).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(EditLeadContractActivity.getEditLeadContractActivityIntent(LeadSettingsActivity.this, null));
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
