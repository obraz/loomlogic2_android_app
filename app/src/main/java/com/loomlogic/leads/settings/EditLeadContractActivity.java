package com.loomlogic.leads.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.loomlogic.R;
import com.loomlogic.base.BaseActivity;
import com.loomlogic.leads.create.StateListActivity;
import com.loomlogic.leads.entity.FinancingTypeItem;
import com.loomlogic.leads.entity.LeadItem;
import com.loomlogic.leads.entity.State;
import com.loomlogic.view.LLEditTextWithHint;

import static com.loomlogic.leads.create.StateListActivity.KEY_STATE;

/**
 * Created by alex on 3/27/17.
 */

public class EditLeadContractActivity extends BaseActivity implements View.OnClickListener {
    private static final int RESULT_PICK_TYPE = 124;
    private static final int RESULT_PICK_STATE = 125;
    private LLEditTextWithHint addressEt, unitEt, cityEt, zipEt, stateEt, priceEt, commissionEt, grossEt, typeEt;
    private State stateSelected;

    public static Intent getEditLeadContractActivityIntent(Context context, LeadItem lead) {
        Intent intent = new Intent(context, EditLeadContractActivity.class);
        //intent.putExtra(KEY_IS_FROM_CONTACT, isFromContact);
        return intent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_lead_contract);
        setTitle(R.string.lead_edit_contract_title);

        initViews();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (getToolbar() != null) {
            getToolbar().setNavigationIcon(R.drawable.ic_back);
        }
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
                showErrorSnackBar("save");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        addressEt = (LLEditTextWithHint) findViewById(R.id.et_editContract_address);
        unitEt = (LLEditTextWithHint) findViewById(R.id.et_editContract_unit);
        cityEt = (LLEditTextWithHint) findViewById(R.id.et_editContract_city);
        zipEt = ((LLEditTextWithHint) findViewById(R.id.et_editContract_zip));
        stateEt = ((LLEditTextWithHint) findViewById(R.id.et_editContract_state));

        priceEt = (LLEditTextWithHint) findViewById(R.id.et_editContract_price);
        commissionEt = (LLEditTextWithHint) findViewById(R.id.et_editContract_commission);
        grossEt = ((LLEditTextWithHint) findViewById(R.id.et_editContract_gross));
        typeEt = ((LLEditTextWithHint) findViewById(R.id.et_editContract_type));

        priceEt.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    priceEt.addTextChangedListener(priceTextChangedListener);
                } else {
                    priceEt.removeTextChangedListener(priceTextChangedListener);
                }
            }
        });

        commissionEt.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    commissionEt.addTextChangedListener(commissionTextChangedListener);
                } else {
                    commissionEt.removeTextChangedListener(commissionTextChangedListener);
                }
            }
        });

        grossEt.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    grossEt.addTextChangedListener(grossTextChangedListener);
                    grossEt.setMaxSelection();
                } else {
                    grossEt.removeTextChangedListener(grossTextChangedListener);
                }
            }
        });

        findViewById(R.id.view_editContract_type).setOnClickListener(this);
        findViewById(R.id.view_editContract_state).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_editContract_type:
                startActivityForResult(new Intent(EditLeadContractActivity.this, FinancingTypesListActivity.class), RESULT_PICK_TYPE);
                break;
            case R.id.view_editContract_state:
                startActivityForResult(new Intent(EditLeadContractActivity.this, StateListActivity.class), RESULT_PICK_STATE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_PICK_TYPE:
                    typePicked(data);
                    break;
                case RESULT_PICK_STATE:
                    statePicked(data);
                    break;
            }
        }
    }

    private void statePicked(Intent data) {
        stateSelected = new Gson().fromJson(data.getExtras().getString(KEY_STATE), State.class);
        stateEt.setText(stateSelected.name);
    }

    private void typePicked(Intent data) {
        FinancingTypeItem typeSelected = new Gson().fromJson(data.getExtras().getString(FinancingTypesListActivity.KEY_ITEM), FinancingTypeItem.class);
        typeEt.setText(typeSelected.name);
    }

    TextWatcher priceTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(commissionEt.getText()) || TextUtils.isEmpty(priceEt.getText())) {
                grossEt.setText("");
                return;
            }

            long price = Long.parseLong(s.toString());
            float commission = Float.parseFloat(commissionEt.getText());
            long gross = (long) Math.ceil(commission * price / 100);

            grossEt.setText(String.valueOf(gross));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    TextWatcher commissionTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(commissionEt.getText())) {
                grossEt.setText("");
                return;
            }

            String input = s.toString();
            if (input.contains(".") && s.charAt(s.length() - 1) != '.') {
                if (input.indexOf(".") + 3 <= input.length() - 1) {
                    String formatted = input.substring(0, input.indexOf(".") + 3);
                    commissionEt.setText(formatted);
                    commissionEt.setMaxSelection();
                }
            }

            if (TextUtils.isEmpty(priceEt.getText())) {
                priceEt.setText("0");
            }

            if (commissionEt.getText().equals(".")) {
                commissionEt.setText("0.");
                commissionEt.setMaxSelection();
            }

            float commission = Float.parseFloat(commissionEt.getText());
            if (commission > 100) {
                commission = 100f;
                commissionEt.setText(String.valueOf(commission));
                commissionEt.setMaxSelection();
            }

            long price = Long.parseLong(priceEt.getText());
            long gross = (long) Math.ceil(commission * price / 100);
            grossEt.setText(String.valueOf(gross));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    TextWatcher grossTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(grossEt.getText()) ) {
                grossEt.setText("0");
                return;
            }

            if (TextUtils.isEmpty(priceEt.getText())) {
                priceEt.setText("0");
            }

            if (TextUtils.isEmpty(commissionEt.getText())) {
                commissionEt.setText("0");
            }

            float price = Float.parseFloat(priceEt.getText());
            if (price == 0) {
                grossEt.removeTextChangedListener(grossTextChangedListener);
                grossEt.setText("0");
                grossEt.addTextChangedListener(grossTextChangedListener);
                return;
            }
            float gross = Float.parseFloat(s.toString());
            float commission = gross * 100 / price;

            if (gross > price) {
                commission = 100f;
                grossEt.setText(String.valueOf((long)price));
                grossEt.setMaxSelection();
            }
            commissionEt.setText(String.format("%.2f", commission));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
