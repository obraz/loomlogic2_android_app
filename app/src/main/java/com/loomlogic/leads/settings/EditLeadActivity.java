package com.loomlogic.leads.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.loomlogic.R;
import com.loomlogic.leads.create.CreateLeadActivity;
import com.loomlogic.leads.entity.LeadItem;

/**
 * Created by alex on 3/27/17.
 */

public class EditLeadActivity extends CreateLeadActivity {
    public static Intent getEditLeadActivityIntent(Context context, LeadItem lead) {
        Intent intent = new Intent(context, EditLeadActivity.class);
        //intent.putExtra(KEY_IS_FROM_CONTACT, isFromContact);
        return intent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.lead_edit_title);

        findViewById(R.id.ll_createLead_typeContainer).setVisibility(View.GONE);
        findViewById(R.id.ll_createLead_addressContainer).setVisibility(View.VISIBLE);
        setAddressContainerTitle(getString(R.string.edit_lead_address_title));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lead_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                if (validateFields(false)) {
                    showErrorSnackBar("save");
                }
        }
        return super.onOptionsItemSelected(item);
    }
}
