package com.loomlogic.leads.create;

import android.content.Intent;

import com.loomlogic.R;

/**
 * Created by alex on 5/8/17.
 */

public class SendToLenderActivity extends SendToActivity {
    @Override
    int getTitleRes() {
        return R.string.create_new_lead_send_to_lender_title;
    }

    @Override
    int getSendToHintRes() {
        return R.string.create_new_lead_send_to_lender;
    }

    @Override
    int getValidationError() {
        return R.string.create_new_lead_need_lender_error;
    }

    @Override
    void setSendToData() {
        setSendToLenderData();
    }

    @Override
    Intent getListIntent() {
        return new Intent(this, LendersListActivity.class);
    }
}
