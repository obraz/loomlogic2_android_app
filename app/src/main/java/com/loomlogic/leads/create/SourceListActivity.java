package com.loomlogic.leads.create;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.gson.Gson;
import com.loomlogic.R;
import com.loomlogic.base.BaseActivity;
import com.loomlogic.leads.entity.LeadSourceItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 3/29/17.
 */

public class SourceListActivity extends BaseActivity implements SourcesListAdapter.SourceSelectorCallback {
    public static final String KEY_SOURCE = "KEY_SOURCE";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lead_sources);
        setTitle(R.string.create_new_lead_source_title);

        initViews();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (getToolbar() != null) {
            getToolbar().setNavigationIcon(R.drawable.ic_back);
        }
    }

    private void initViews() {
        SourcesListAdapter adapter = new SourcesListAdapter(this);

        List<LeadSourceItem> sources = new ArrayList<>();
        sources.add(new LeadSourceItem("Zillow"));
        sources.add(new LeadSourceItem("Tulia"));
        sources.add(new LeadSourceItem("Open House"));
        sources.add(new LeadSourceItem("Past Client"));
        sources.add(new LeadSourceItem("Realtor"));
        adapter.setSourcesList(sources);

        RecyclerView listSources = (RecyclerView) findViewById(R.id.list_sources);
        listSources.setHasFixedSize(true);
        listSources.setLayoutManager(new LinearLayoutManager(this));
        listSources.setAdapter(adapter);

        findViewById(R.id.fab_lead_source_new).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewLeadSourceDialog();
            }
        });
    }

    private void showNewLeadSourceDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.create_new_lead_source_add_new_title);

        final EditText input = new EditText(this);
        input.setSingleLine();
        FrameLayout container = new FrameLayout(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(30, 20, 30, 20);
        input.setLayoutParams(params);
        container.addView(input, params);
        input.setHint(R.string.create_new_lead_state_add_new_hint);

        alertDialog.setView(container);

        alertDialog.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String source = input.getText().toString();
                        if (!TextUtils.isEmpty(source)) {
                            onSourceSelected(new LeadSourceItem(source));
                        }
                    }
                });

        alertDialog.setNegativeButton(android.R.string.cancel, null);

        alertDialog.show();
    }

    @Override
    public void onSourceSelected(LeadSourceItem source) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_SOURCE, new Gson().toJson(source));
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
