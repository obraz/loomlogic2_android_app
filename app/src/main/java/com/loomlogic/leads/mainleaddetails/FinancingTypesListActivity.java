package com.loomlogic.leads.mainleaddetails;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.loomlogic.R;
import com.loomlogic.base.BaseActivity;
import com.loomlogic.leads.entity.FinancingTypeItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 3/29/17.
 */

public class FinancingTypesListActivity extends BaseActivity implements FinancigTypesListAdapter.FinancingTypeSelectorCallback {
    public static final String KEY_ITEM = "KEY_ITEM";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financing_types);
        setTitle(R.string.financing_types_list_title);

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
        FinancigTypesListAdapter adapter = new FinancigTypesListAdapter(this);

        List<FinancingTypeItem> listItems = new ArrayList<>();
        listItems.add(new FinancingTypeItem("Cash Buyer"));
        listItems.add(new FinancingTypeItem("Conventional"));
        listItems.add(new FinancingTypeItem("FHA"));
        listItems.add(new FinancingTypeItem("VA"));
        listItems.add(new FinancingTypeItem("Loan"));
        listItems.add(new FinancingTypeItem("CalVet"));
        adapter.setFinancingTypeList(listItems);

        RecyclerView listView = (RecyclerView) findViewById(R.id.list);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(adapter);
    }

    @Override
    public void onFinancingTypeSelected(FinancingTypeItem item) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_ITEM, new Gson().toJson(item));
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
