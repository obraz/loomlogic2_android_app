package com.loomlogic.base;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.androidadvance.topsnackbar.TSnackbar;
import com.github.pwittchen.networkevents.library.ConnectivityStatus;
import com.github.pwittchen.networkevents.library.event.ConnectivityChanged;
import com.kt.http.base.ResponseData;
import com.loomlogic.R;
import com.loomlogic.base.resultfix.ActivityResultFixActivity;
import com.loomlogic.network.RetryRequestCallback;
import com.loomlogic.utils.IntentUtils;
import com.loomlogic.utils.InternetConnectionManager;
import com.loomlogic.utils.Utils;
import com.loomlogic.utils.ViewUtils;

import org.greenrobot.eventbus.Subscribe;

import java.net.HttpURLConnection;


public class BaseActivity extends ActivityResultFixActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();
    public static final String CONNECTED_ACTION = "connected_action";
    public static final String DISCONNECTED_ACTION = "disconnected_action";

    boolean toolbarInitialized;
    private ProgressDialog progressDialog;
    private InternetConnectionManager internetConnectionManager;
    private Toolbar toolbar;
    private TSnackbar noConnectionSnackBar;
    private View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbarInitialized = false;
        internetConnectionManager = InternetConnectionManager.from(this);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        view = getLayoutInflater().inflate(layoutResID, null);
        super.setContentView(view);
    }

    @Override
    public void setContentView(View view) {
        this.view = view;
        super.setContentView(view);
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onEvent(ConnectivityChanged event) {
        if (event.getConnectivityStatus() == ConnectivityStatus.MOBILE_CONNECTED || event.getConnectivityStatus() == ConnectivityStatus.WIFI_CONNECTED_HAS_INTERNET) {
            onConnect();
        } else {
            onDisconnect();
        }
    }

    public void onConnect() {
        hideNoConnectionSnackBar();
        onFragmentCallback(null, CONNECTED_ACTION, null);
    }

    public void onDisconnect() {
        showNoConnectionSnackBar();
        onFragmentCallback(null, DISCONNECTED_ACTION, null);
    }

    @Override
    protected void onResume() {
        super.onResume();

//        if (isOnline()) {
//            onConnect();
//        } else {
//            onDisconnect();
//        }
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        internetConnectionManager.register(this);
    }

    @Override
    protected void onStop() {
        hideProgressBar();
        hideNoConnectionSnackBar();
        internetConnectionManager.unregister(this);
        super.onStop();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initToolBar();
    }

    @Nullable
    @Override
    public ActionBar getSupportActionBar() {
        initToolBar();
        return super.getSupportActionBar();
    }

    @Nullable
    public Toolbar getToolbar() {
        return toolbar;
    }

    private void initToolBar() {
        if (!this.toolbarInitialized) {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            if (toolbar != null) {
                setSupportActionBar(toolbar);
                toolbar.setTitle(getTitle());
                this.toolbarInitialized = true;
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    public void setTitle(@StringRes int title) {
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void hideBackButton() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    public void showProgressBar(String msg) {
        progressDialog = ProgressDialog.show(this, null, null);

        if (msg != null) {
            progressDialog.setMessage(msg);
        } else {
            ProgressBar spinner = new android.widget.ProgressBar(this, null, android.R.attr.progressBarStyle);
            spinner.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressDialog.setContentView(spinner);
        }
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void showProgressBar() {
        showProgressBar(null);
    }

    public void hideProgressBar() {
        if (progressDialog != null) {
            progressDialog.hide();
            progressDialog.cancel();
            progressDialog = null;
        }
    }

    public boolean isOnline() {
        return internetConnectionManager.isConnected();
    }

    public void showResponseError(ResponseData data) {
        showResponseError(data, null);
    }

    public void showResponseError(ResponseData data, RetryRequestCallback callback) {
        if (data != null) {
            if (data.getStatusCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
//                Toast.makeText(this, R.string.response401, Toast.LENGTH_LONG).show();
//                logout();
            } else {
//                if (data.getParsedErrorResponse() != null) {
//                    ResponseData dataWrapper = (ResponseData) data.getParsedErrorResponse();
//                    ApiError error = (ApiError) dataWrapper.getData();
//                    if (error != null && error.getMessage() != null && !TextUtils.isEmpty(error.getMessage())) {
//                        showErrorSnackBar(error.getMessage());
//                        Log.e(TAG, error.getMessage());
//                    } else {
//                        showErrorSnackBar(ResponseUtils.getResponseErrorByCode(this, data.getStatusCode()));
//                        Log.e(TAG, ResponseUtils.getResponseErrorByCode(this, data.getStatusCode()));
//                    }
//                } else {
//                    showErrorSnackBar(ResponseUtils.getResponseErrorByCode(this, data.getStatusCode()));
//                    Log.e(TAG, ResponseUtils.getResponseErrorByCode(this, data.getStatusCode()));
//                }
                if (data.getParsedErrorResponse() != null) {

                } else {
                    showGeneralAlertSnackBar(callback);
                }
            }
        } else {
            showGeneralAlertSnackBar(callback);
        }
    }

    public void showAlertSnackBar(String errorMsg, RetryRequestCallback callback) {
        if (view != null && errorMsg != null) {
            hideSoftKeyboard();
            ViewUtils.showAlertSnackBar(view, errorMsg, callback);
        }
    }

    public void showGeneralAlertSnackBar(RetryRequestCallback callback) {
        showAlertSnackBar(getString(R.string.general_error), callback);
    }


    public void showErrorSnackBar(String errorMsg) {
        if (view != null && errorMsg != null) {
            hideSoftKeyboard();
            ViewUtils.showErrorSnackBar(view, errorMsg);
        }
    }

    public void showNoConnectionSnackBar() {
        if (noConnectionSnackBar == null && view != null) {
            noConnectionSnackBar = ViewUtils.displayNoInternetSnackBar(this, view);
        } else {
            if (!noConnectionSnackBar.isShown()) {
                noConnectionSnackBar.show();
            }
        }
    }

    public void hideNoConnectionSnackBar() {
        if (noConnectionSnackBar != null) {
            noConnectionSnackBar.dismiss();
            noConnectionSnackBar = null;
        }
    }

    public void showSoftKeyboard() {
        if (view != null) {
            ViewUtils.showSoftKeyboard(view);
        }
    }

    public void hideSoftKeyboard() {
        if (view != null) {
            ViewUtils.hideSoftKeyboard(view);
        }
    }

    public void showMapDialog(final String latitude, final String longitude, final String address) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.map_open_dialog_title);
        builder.setMessage(R.string.map_open_dialog_message);
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                IntentUtils.openMapIntent(BaseActivity.this, latitude, longitude, address);
            }
        });
        builder.show();
    }

    public void showCopyTextDialog(final String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] de = {getString(R.string.copy_text)};
        builder.setItems(de, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Utils.copyTextToClipboard(text);
            }
        });

        builder.show();
    }
}