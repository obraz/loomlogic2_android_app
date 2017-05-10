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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kt.http.base.ResponseData;
import com.loomlogic.R;
import com.loomlogic.base.BaseActivity;
import com.loomlogic.leads.entity.LeadSourceItem;
import com.loomlogic.leads.entity.State;
import com.loomlogic.network.Model;
import com.loomlogic.network.managers.BaseItemManager;
import com.loomlogic.network.managers.LeadManager;
import com.loomlogic.network.requests.data.LeadRequestData;
import com.loomlogic.network.responses.ResponseDataWrapper;
import com.loomlogic.network.responses.UserData;
import com.loomlogic.network.responses.errors.ErrorsConstant;
import com.loomlogic.utils.Utils;
import com.loomlogic.utils.ViewUtils;
import com.loomlogic.view.LLEditTextWithHint;

import java.util.List;

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

    private LLEditTextWithHint nameEt, additionalNameEt, phoneEt, emailEt, sourceEt, noteEt;
    private LLEditTextWithHint addressEt, unitEt, cityEt, zipCodeEt, stateEt;
    private Switch dripCompaignsSwitch;

    private LeadSourceItem sourceSelected;
    private State stateSelected = new State();
    private LeadManager leadManager;
    private LeadRequestData leadRequestData;

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

        leadRequestData = new LeadRequestData();
        leadRequestData.setLeadType(LeadRequestData.LeadType.BUYER);

        initViews();
        openContactPickerIfNeed();

        leadManager = Model.instance().getLeadManager();
        leadManager.addDataFetchCompleteListener(completeListener);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (getToolbar() != null) {
            getToolbar().setNavigationIcon(R.drawable.ic_back);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        leadManager.removeDataFetchCompleteListener(completeListener);
    }

    private void initViews() {
        initTypeViews();

        nameEt = (LLEditTextWithHint) findViewById(R.id.et_createLead_name);
        additionalNameEt = (LLEditTextWithHint) findViewById(R.id.et_createLead_additionalName);
        phoneEt = (LLEditTextWithHint) findViewById(R.id.et_createLead_phone);
        emailEt = (LLEditTextWithHint) findViewById(R.id.et_createLead_email);
        sourceEt = ((LLEditTextWithHint) findViewById(R.id.et_createLead_source));
        stateEt = ((LLEditTextWithHint) findViewById(R.id.et_createLead_state));

        noteEt = ((LLEditTextWithHint) findViewById(R.id.et_createLead_note));
        addressEt = ((LLEditTextWithHint) findViewById(R.id.et_createLead_address));
        unitEt = ((LLEditTextWithHint) findViewById(R.id.et_createLead_unit));
        cityEt = ((LLEditTextWithHint) findViewById(R.id.et_createLead_city));
        zipCodeEt = ((LLEditTextWithHint) findViewById(R.id.et_createLead_zip));

        dripCompaignsSwitch = (Switch) findViewById(R.id.sw_createLead_dripCompaigns);

        findViewById(R.id.view_createLead_source).setOnClickListener(this);
        findViewById(R.id.view_createLead_state).setOnClickListener(this);

        phoneEt.addTextChangedListener(new CustomPhoneTextWatcher());

        emailEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                phoneEt.removeError();
                if (phoneEt.getEditText().getText().length() == 0) {
                    phoneEt.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initTypeViews() {
        final View addressContainer = findViewById(R.id.ll_createLead_addressContainer);

        final int colorBlack = ContextCompat.getColor(this, R.color.lead_create_new_black_text_color);
        final int colorGrey = ContextCompat.getColor(this, R.color.lead_create_new_grey_text_color);

        final RadioButton buyerType = (RadioButton) findViewById(rb_createLead_buyer);
        final RadioButton sellerType = (RadioButton) findViewById(rb_createLead_seller);

        buyerType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setRightMarginToAddressContainerView(addressContainer, 0);
                    collapse(addressContainer);
                    sellerType.setChecked(false);
                    changeRadioBtnTextColor(buyerType, colorGrey, colorBlack);
                    leadRequestData.setLeadType(LeadRequestData.LeadType.BUYER);
                    leadRequestData.clearAddress();
                } else {
                    changeRadioBtnTextColor(buyerType, colorBlack, colorGrey);
                }
            }
        });

        sellerType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setRightMarginToAddressContainerView(addressContainer, (int) getResources().getDimension(R.dimen.margins15));
                    expand(addressContainer);
                    buyerType.setChecked(false);
                    changeRadioBtnTextColor(sellerType, colorGrey, colorBlack);
                    leadRequestData.setLeadType(LeadRequestData.LeadType.SELLER);
                } else {
                    changeRadioBtnTextColor(sellerType, colorBlack, colorGrey);
                }
            }
        });

        // if (LeadPreferencesUtils.getCurrentLeadType() == LeadType.SELLER) {
        //      sellerType.setChecked(true);
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

    public void setAddressContainerTitle(String text) {
        TextView addressTitle = (TextView) findViewById(R.id.tv_createLead_addressTitle);
        addressTitle.setText(text);
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
        } else {
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    finish();
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
                //  createLead();
                if (validateFields(false)) {
                    ViewUtils.hideSoftKeyboard(getCurrentFocus());
// TODO: 3/29/17  only if user PRo Agent
                    openCreateLeadDialog();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean validateFields(boolean validateEmailAndPhone) {
        boolean isValid = true;
        String errors = "";

        if (validateEmailAndPhone && TextUtils.isEmpty(emailEt.getText()) && TextUtils.isEmpty(phoneEt.getText())) {
            emailEt.setError();
            phoneEt.setError();
            errors = errors + getString(R.string.create_new_lead_email_phone_error) + "\n";
            isValid = false;
        }

        if (!TextUtils.isEmpty(emailEt.getText()) && !Utils.isEmailValid(emailEt.getText())) {
            emailEt.setError();
            errors = errors + getString(R.string.create_new_lead_email_error) + "\n";
            isValid = false;
        }
        if (sourceSelected == null) {
            sourceEt.setError();
            errors = errors + getString(R.string.create_new_lead_source_error) + "\n";
            isValid = false;
        }

        if (!isValid) {
            errors = errors.substring(0, errors.length() - 1);
            showErrorSnackBar(errors);
        }
        return isValid;
    }

    private void openCreateLeadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.create_new_lead_dialog_title);
        builder.setItems(leadRequestData.isSeller() ? R.array.create_new_lead_seller_dialog_chooser : R.array.create_new_lead_buyer_dialog_chooser, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ListView lv = ((AlertDialog) dialog).getListView();
                String checkedItem = (String) lv.getAdapter().getItem(which);
                if (checkedItem.equals(getString(R.string.create_new_lead_dialog_create))) {
                    leadRequestData.setSendToAction(LeadRequestData.SendToAction.CRM);

                } else if (checkedItem.equals(getString(R.string.create_new_lead_dialog_send_to_lender))) {
                    leadRequestData.setSendToAction(LeadRequestData.SendToAction.LENDER);
                    openAgentLenderChoseActivity(SendToLenderActivity.class);
                } else if (checkedItem.equals(getString(R.string.create_new_lead_dialog_send_to_agent))) {
                    leadRequestData.setSendToAction(LeadRequestData.SendToAction.AGENT);
                    openAgentLenderChoseActivity(SendToAgentActivity.class);
                } else if (checkedItem.equals(getString(R.string.create_new_lead_dialog_send_to_team))) {
                    leadRequestData.setSendToAction(LeadRequestData.SendToAction.TEAM);

                }

            }

        });
        builder.show();
    }

    private void openAgentLenderChoseActivity(Class _class) {
        if (validateFields(true)) {
            startActivity(SendToActivity.getSendToActivityIntent(this, _class, leadRequestData));
        }
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

    private void createLead() {
        showProgressBar();
        leadRequestData.setLeadData(
                nameEt.getText(),
                additionalNameEt.getText(),
                phoneEt.getText(),
                emailEt.getText(),
                1, //todo set real
                noteEt.getText(),
                dripCompaignsSwitch.isChecked()
        );
        if (leadRequestData.isSeller()) {
            leadRequestData.setAddress(addressEt.getText(), unitEt.getText(), cityEt.getText(), stateSelected.abbreviation, zipCodeEt.getText());
        }
        leadManager.createNewLead(leadRequestData);
    }

    private final BaseItemManager.OnDataFetchCompleteListener<UserData, LeadAction> completeListener
            = new BaseItemManager.OnDataFetchCompleteListener<UserData, LeadAction>() {

        @Override
        public void onDataFetchComplete(UserData result, ResponseData response, LeadAction requestTag) {
            if (requestTag == LeadAction.CREATE) {
                hideProgressBar();

            }
        }

        @Override
        public void onDataFetchFailed(UserData result, ResponseData response, LeadAction requestTag) {
            if (requestTag == LeadAction.CREATE) {
                hideProgressBar();

                if (response != null && response.getData() != null) {
                    ResponseDataWrapper dataWrapper = (ResponseDataWrapper) response.getData();

                    if (isValidationError(response)) {
                        List<String> errors = dataWrapper.getErrors();
                        for (String error : errors) {
                            if (error.equals(ErrorsConstant.ERROR_CREDENTIALS)) {
                                break;
                            }
                        }
                    }
                }
                showResponseError(response);
            }
        }
    };

    class CustomPhoneTextWatcher extends PhoneNumberFormattingTextWatcher {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            super.onTextChanged(s, start, before, count);
            emailEt.removeError();
            if (emailEt.getEditText().getText().length() == 0) {
                emailEt.setText("");
            }
        }
    }
}
