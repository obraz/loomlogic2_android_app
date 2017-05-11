package com.loomlogic.leads.create;

import android.content.Intent;

import com.loomlogic.R;

/**
 * Created by alex on 5/8/17.
 */

public class SendToAgentActivity extends SendToActivity {
    @Override
    int getTitleRes() {
        return R.string.create_new_lead_send_to_agent_title;
    }

    @Override
    int getSendToHintRes() {
        return R.string.create_new_lead_send_to_agent;
    }

    @Override
    int getValidationError() {
        return R.string.create_new_lead_need_agent_error;
    }

    @Override
    void setSendToData() {
        setSendToAgentData();
    }

    @Override
    Intent getListIntent() {
        return new Intent(this, AgentsListActivity.class);
    }
}
