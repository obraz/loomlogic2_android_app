package com.loomlogic.leads.create;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.kt.http.base.ResponseData;
import com.loomlogic.R;
import com.loomlogic.base.BaseActivity;
import com.loomlogic.network.Model;
import com.loomlogic.network.managers.BaseItemManager;
import com.loomlogic.network.managers.LeadManager;
import com.loomlogic.network.requests.data.LeadRequestData;
import com.loomlogic.network.responses.ResponseDataWrapper;
import com.loomlogic.network.responses.UserData;
import com.loomlogic.network.responses.errors.ErrorsConstant;
import com.loomlogic.utils.TimeUtils;
import com.loomlogic.utils.ViewUtils;
import com.loomlogic.view.LLEditTextWithHint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by alex on 5/8/17.
 */

public abstract class SendToActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private static final String KEY_LEAD_REQUEST_DATA = "KEY_LEAD_REQUEST_DATA";
    private static final int RESULT_PICK_ITEM = 17;

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
    public static SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm aa", Locale.getDefault());

    private LLEditTextWithHint sendToEt;
    private Switch sendFutureSw, claimSw;
    private TextView dateTv, timeTv;
    private LeadManager leadManager;
    private LeadRequestData leadRequestData;
    private Calendar sendToCalendar;

    public static Intent getSendToActivityIntent(Context context, Class _class, LeadRequestData leadRequestData) {
        Intent intent = new Intent(context, _class);
        intent.putExtra(KEY_LEAD_REQUEST_DATA, new Gson().toJson(leadRequestData));
        return intent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_send_to);
        setTitle(getTitleRes());

        sendToCalendar = Calendar.getInstance();

        initViews();
        setCurrentDateTime();
        leadManager = Model.instance().getLeadManager();
        leadManager.addDataFetchCompleteListener(completeListener);

        leadRequestData = new Gson().fromJson(getIntent().getStringExtra(KEY_LEAD_REQUEST_DATA), LeadRequestData.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        leadManager.removeDataFetchCompleteListener(completeListener);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (getToolbar() != null) {
            getToolbar().setNavigationIcon(R.drawable.ic_back);
        }
    }

    @StringRes
    abstract int getTitleRes();

    @StringRes
    abstract int getSendToHintRes();

    @StringRes
    abstract int getValidationError();

    abstract void setSendToData();

    abstract Intent getListIntent();

    protected void initViews() {
        sendToEt = (LLEditTextWithHint) findViewById(R.id.et_createLead_sendTo);
        sendFutureSw = (Switch) findViewById(R.id.sw_createLead_sendFuture);
        final View timeContainer = findViewById(R.id.createLeadSpecifyTimeContainer);
        final View timeDefaultTitle = findViewById(R.id.tv_createLead_sendFutureDefaultTitle);
        dateTv = (TextView) findViewById(R.id.tv_createLead_sendFutureDate);
        timeTv = (TextView) findViewById(R.id.tv_createLead_sendFutureTime);
        claimSw = (Switch) findViewById(R.id.sw_createLead_sendToClaim);

        sendToEt.setHint(getSendToHintRes());

        findViewById(R.id.view_createLead_sendTo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(getListIntent(), RESULT_PICK_ITEM);
            }
        });

        sendFutureSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    timeContainer.setVisibility(View.GONE);
                    timeDefaultTitle.setVisibility(View.VISIBLE);
                } else {
                    timeContainer.setVisibility(View.VISIBLE);
                    timeDefaultTitle.setVisibility(View.GONE);
                }
            }
        });

        dateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog();
            }
        });

        timeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePickerDialog();
            }
        });

    }

    protected void openDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DatePickerDialogTheme, this, sendToCalendar.get(Calendar.YEAR), sendToCalendar.get(Calendar.MONTH), sendToCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(sendToCalendar.getTimeInMillis());

        datePickerDialog.show();
    }

    protected void openTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.DatePickerDialogTheme, this, sendToCalendar.get(Calendar.HOUR_OF_DAY), sendToCalendar.get(Calendar.MINUTE), false);

        timePickerDialog.show();
    }

    protected void setCurrentDateTime() {
        // dateTv.setText(dateFormat.format(sendToCalendar.getTime()));
        // timeTv.setText(timeFormat.format(sendToCalendar.getTime()));

//        Date date = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat();
//        sdf.setTimeZone(TimeZone.getTimeZone("PST8PDT"));
//        date = new Date(sdf.format(date));

        //  Date date = cal.getTime();
//        TimeZone timeZone = TimeZone.getTimeZone("PST8PDT");
//        long dstTime = 0;
//        if (timeZone.inDaylightTime(date)) {
//            dstTime = timeZone.getDSTSavings();
//        }
//        date.setTime(date.getTime() + timeZone.getRawOffset() + dstTime);

        // cal.setTime(date);


        dateFormat.setTimeZone(TimeZone.getTimeZone("PST8PDT"));
        timeFormat.setTimeZone(TimeZone.getTimeZone("PST8PDT"));
        dateTv.setText(dateFormat.format(sendToCalendar.getTime()));
        timeTv.setText(timeFormat.format(sendToCalendar.getTime()));

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        sendToCalendar.set(year, month, day);
        dateTv.setText(dateFormat.format(sendToCalendar.getTime()));

        openTimePickerDialog();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        sendToCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        sendToCalendar.set(Calendar.MINUTE, minute);
        timeTv.setText(timeFormat.format(sendToCalendar.getTime()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == RESULT_PICK_ITEM) {
            itemPicked(data);
        }
    }

    private void itemPicked(Intent data) {
        //stateSelected = new Gson().fromJson(data.getExtras().getString(KEY_STATE), State.class);
        sendToEt.setText("Harriett Delgado");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_lead_send, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_ok:
                ViewUtils.hideSoftKeyboard(getCurrentFocus());
                if (validateFields()) {
                    createLead();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean validateFields() {
        if (TextUtils.isEmpty(sendToEt.getText())) {
            sendToEt.setError();
            showErrorSnackBar(getString(getValidationError()));
            return false;
        }
        return true;
    }

    private void createLead() {
        showProgressBar();
        setSendToData();

        leadManager.createNewLead(leadRequestData);
    }

    protected void setSendToAgentData() {
        leadRequestData.setSendToAgentData(312, claimSw.isChecked(), TimeUtils.getFormattedDateToRequest(sendToCalendar));
    }

    protected void setSendToLenderData() {
        leadRequestData.setSendToLenderData(6, claimSw.isChecked(), TimeUtils.getFormattedDateToRequest(sendToCalendar));
    }

    private final BaseItemManager.OnDataFetchCompleteListener<UserData, LeadAction> completeListener
            = new BaseItemManager.OnDataFetchCompleteListener<UserData, LeadAction>() {

        @Override
        public void onDataFetchComplete(UserData result, ResponseData response, LeadAction requestTag) {
            if (requestTag == LeadAction.CREATE) {
                hideProgressBar();
                finish();
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
}
