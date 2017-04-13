package com.loomlogic.leads.create;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.google.gson.Gson;
import com.loomlogic.R;
import com.loomlogic.base.BaseActivity;
import com.loomlogic.leads.entity.LeadSourceItem;
import com.loomlogic.leads.entity.State;
import com.loomlogic.utils.ViewUtils;
import com.loomlogic.view.LLEditTextWithHint;

import static com.loomlogic.R.id.rb_createLead_buyer;
import static com.loomlogic.R.id.rb_createLead_seller;
import static com.loomlogic.leads.create.SourceListActivity.KEY_SOURCE;
import static com.loomlogic.leads.create.StateListActivity.KEY_STATE;
import static com.loomlogic.utils.AnimationUtils.collapse;
import static com.loomlogic.utils.AnimationUtils.expand;
import static com.loomlogic.utils.ViewUtils.changeRadioBtnTextColor;

/**
 * Created by alex on 3/27/17.
 */

public class CreateLeadActivity extends BaseActivity implements View.OnClickListener {
    private static final String KEY_IS_FROM_CONTACT = "KEY_IS_FROM_CONTACT";
    private static final int RESULT_PICK_CONTACT = 123;
    private static final int RESULT_PICK_SOURCE = 124;
    private static final int RESULT_PICK_STATE = 125;

    private LLEditTextWithHint nameEt, phoneEt, emailEt, sourceEt, stateEt;
    private LeadSourceItem sourceSelected;
    private State stateSelected;

    public static Intent getCreateLeadActivityIntent(Context context, boolean isFromContact) {
        Intent intent = new Intent(context, CreateLeadActivity.class);
        intent.putExtra(KEY_IS_FROM_CONTACT, isFromContact);
        return intent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lead);
        setTitle(R.string.create_new_lead_title);

        initViews();

        openContactPickerIfNeed();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (getToolbar() != null) {
            getToolbar().setNavigationIcon(R.drawable.ic_back);
        }
    }

    private void initViews() {
        initRoleViews();

        nameEt = (LLEditTextWithHint) findViewById(R.id.et_createLead_name);
        phoneEt = (LLEditTextWithHint) findViewById(R.id.et_createLead_phone);
        emailEt = (LLEditTextWithHint) findViewById(R.id.et_createLead_email);
        sourceEt = ((LLEditTextWithHint) findViewById(R.id.et_createLead_source));
        stateEt = ((LLEditTextWithHint) findViewById(R.id.et_createLead_state));

        findViewById(R.id.view_createLead_source).setOnClickListener(this);
        findViewById(R.id.view_createLead_state).setOnClickListener(this);

        phoneEt.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }

    private void initRoleViews() {
        final View addressContainer = findViewById(R.id.ll_createLead_addressContainer);

        final int colorBlack = ContextCompat.getColor(this, R.color.lead_create_new_black_text_color);
        final int colorGrey = ContextCompat.getColor(this, R.color.lead_create_new_grey_text_color);

        final RadioButton buyerRole = (RadioButton) findViewById(rb_createLead_buyer);
        final RadioButton sellerRole = (RadioButton) findViewById(rb_createLead_seller);

        buyerRole.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setRightMarginToAddressContainerView(addressContainer, 0);
                    collapse(addressContainer);
                    sellerRole.setChecked(false);
                    changeRadioBtnTextColor(buyerRole, colorGrey, colorBlack);
                } else {
                    changeRadioBtnTextColor(buyerRole, colorBlack, colorGrey);
                }
            }
        });

        sellerRole.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setRightMarginToAddressContainerView(addressContainer, (int) getResources().getDimension(R.dimen.margins15));
                    expand(addressContainer);
                    buyerRole.setChecked(false);
                    changeRadioBtnTextColor(sellerRole, colorGrey, colorBlack);
                } else {
                    changeRadioBtnTextColor(sellerRole, colorBlack, colorGrey);
                }
            }
        });

        // if (LeadPreferencesUtils.getCurrentLeadRole() == LeadRole.SELLER) {
        //      sellerRole.setChecked(true);
        //  }
    }

    private void setRightMarginToAddressContainerView(View addressContainer, int margin) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, margin);
        addressContainer.setLayoutParams(params);
    }

    private void openContactPickerIfNeed() {
        if (getIntent().getBooleanExtra(KEY_IS_FROM_CONTACT, false)) {
            Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
            startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
                case RESULT_PICK_STATE:
                    statePicked(data);
                    break;
                case RESULT_PICK_SOURCE:
                    sourcePicked(data);
                    break;
            }
        }
    }

    private void contactPicked(Intent data) {
        String[] PROJECTION = new String[]{
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.Data.LOOKUP_KEY
        };

        Cursor cur = getContentResolver().query(data.getData(), PROJECTION, null, null, null);
        if (cur != null && cur.moveToFirst()) {
            String name = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            nameEt.setText(name);
            nameEt.setSelection(name.length());
            phoneEt.setText(phoneNumber);

         /*   String lookUpKey = cur.getString(cur.getColumnIndex(ContactsContract.Data.LOOKUP_KEY));
            if (lookUpKey != null) {
                getContactEmail(lookUpKey);
            }
*/
            cur.close();
        }
    }

    private void getContactEmail(String lookupKey) {
        String[] PROJECTION = new String[]{
                ContactsContract.CommonDataKinds.Email.DATA
        };
        String emailWhere = ContactsContract.Data.LOOKUP_KEY + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
        String[] emailWhereParams = new String[]{lookupKey, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE};
        Cursor emailCursor = getContentResolver().query(ContactsContract.Data.CONTENT_URI, PROJECTION, emailWhere, emailWhereParams, null);
        if (emailCursor != null && emailCursor.moveToNext()) {
            String email = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
            emailEt.setText(email);

            emailCursor.close();
        }
    }

    private void statePicked(Intent data) {
        stateSelected = new Gson().fromJson(data.getExtras().getString(KEY_STATE), State.class);
        stateEt.setText(stateSelected.name);
    }

    private void sourcePicked(Intent data) {
        sourceSelected = new Gson().fromJson(data.getExtras().getString(KEY_SOURCE), LeadSourceItem.class);
        sourceEt.setText(sourceSelected.name);
        sourceEt.setError(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_lead, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_ok:
                if (validateFields()) {
                    ViewUtils.hideSoftKeyboard(getCurrentFocus());
// TODO: 3/29/17  only if user PRo Agent
                    openCreateLeadDialog();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean validateFields() {
        if (sourceSelected == null) {
            sourceEt.setError("");
            showErrorSnackBar(getString(R.string.create_new_lead_source_error));
            return false;
        }
        return true;
    }

    private void openCreateLeadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.create_new_lead_dialog_title);
        builder.setItems(R.array.create_new_lead_dialog_chooser, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showErrorSnackBar("Do something");
            }

        });
        builder.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_createLead_source:
                startActivityForResult(new Intent(CreateLeadActivity.this, SourceListActivity.class), RESULT_PICK_SOURCE);
                break;
            case R.id.view_createLead_state:
                startActivityForResult(new Intent(CreateLeadActivity.this, StateListActivity.class), RESULT_PICK_STATE);
                break;
        }
    }
}
