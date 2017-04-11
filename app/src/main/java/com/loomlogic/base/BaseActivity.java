package com.loomlogic.base;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.loomlogic.R;
import com.loomlogic.base.resultfix.ActivityResultFixActivity;
import com.loomlogic.utils.Utils;
import com.loomlogic.utils.ViewUtils;
import com.novoda.merlin.Merlin;
import com.novoda.merlin.MerlinsBeard;
import com.novoda.merlin.registerable.connection.Connectable;
import com.novoda.merlin.registerable.disconnection.Disconnectable;

/**
 * Created on 11.12.2015.
 * Use this class as superclass for all your activities
 */
public class BaseActivity extends ActivityResultFixActivity implements Connectable, Disconnectable {
    private static final String TAG = BaseActivity.class.getSimpleName();
    public static final String CONNECTED_ACTION = "connected_action";
    public static final String DISCONNECTED_ACTION = "disconnected_action";

    boolean toolbarInitialized;
    private ProgressDialog progressDialog;
    private MerlinsBeard merlinsBeard;
    private Merlin merlin;
    private Toolbar toolbar;
    private Snackbar noConnectionSnackBar;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbarInitialized = false;
        merlinsBeard = MerlinsBeard.from(this);
        merlin = new Merlin.Builder()
                .withConnectableCallbacks()
                .withBindableCallbacks()
                .withDisconnectableCallbacks()
                .build(this);
        merlin.registerConnectable(this);
        merlin.registerDisconnectable(this);


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

    @Override
    public void onConnect() {
        hideNoConnectionSnackBar();
        onFragmentCallback(null, CONNECTED_ACTION, null);
    }

    @Override
    public void onDisconnect() {
        showNoConnectionSnackBar();
        onFragmentCallback(null, DISCONNECTED_ACTION, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        merlin.bind();
        if (isOnline()) {
            onConnect();
        } else {
            onDisconnect();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        merlin.unbind();
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

    @Override
    protected void onStop() {
        super.onStop();
        hideProgressBar();
    }

    public boolean isOnline() {
        return merlinsBeard != null ? merlinsBeard.isConnected() : true;
    }


    public void showErrorSnackBar(String errorMsg) {
        if (view != null && errorMsg != null) {
            hideSoftKeyboard();
            ViewUtils.showWhiteMessageInSnackBar(view, errorMsg);
        }
    }

    public void showGeneralErrorSnackBar() {
        showErrorSnackBar(getString(R.string.general_error));
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

    public void openDialIntent(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        if (Utils.isPhoneValid(phone)) {
            intent.setData(Uri.parse("tel:" + phone));
        }
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            showErrorSnackBar(getString(R.string.call_error));
        }
    }

    public void showMapDialog(final String latitude, final String longitude, final String address){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.map_open_dialog_title);
        builder.setMessage(R.string.map_open_dialog_message);
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openMapIntent(latitude, longitude, address);
            }
        });
        builder.show();
    }

    public void openMapIntent(String latitude, String longitude, String address) {
        String mapRequest = "geo:" + latitude + "," + longitude;
        if (address != null && !TextUtils.isEmpty(address)) {
            mapRequest = mapRequest + "?q=" + Uri.encode(address);
        }
        Uri gmmIntentUri = Uri.parse(mapRequest);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            showErrorSnackBar(getString(R.string.map_error));
        }
    }

    public void showCopyTextDialog(final String text){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] de =  {getString(R.string.copy_text)};
        builder.setItems(de, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Utils.copyTextToClipboard(text);
            }
        });

        builder.show();
    }
}