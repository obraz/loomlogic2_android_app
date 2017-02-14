package com.loomlogic.base;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.loomlogic.R;
import com.loomlogic.base.resultfix.ActivityResultFixActivity;
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
        Log.i(TAG, " Connected to network ");
        onFragmentCallback(null, CONNECTED_ACTION, null);
    }

    @Override
    public void onDisconnect() {
        Log.i(TAG, " Disconnected from network ");
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

    protected void replaceFragment(int holderId, Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.findFragmentByTag(tag) == null) {
            fragmentManager.beginTransaction().replace(holderId, fragment, tag).commit();
        }
    }
}
