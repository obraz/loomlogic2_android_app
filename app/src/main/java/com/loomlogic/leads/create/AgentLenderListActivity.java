package com.loomlogic.leads.create;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.TextView;

import com.loomlogic.R;
import com.loomlogic.base.BaseActivity;
import com.loomlogic.leads.entity.State;
import com.loomlogic.utils.ViewUtils;

/**
 * Created by alex on 5/8/17.
 */

public abstract class AgentLenderListActivity extends BaseActivity implements SearchView.OnQueryTextListener, AgentsLendersListAdapter.ItemSelectorCallback {
    private TextView noResultsTv;
    private AgentsLendersListAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agents_lenders_list);
        setTitle(getTitleRes());

        initViews();
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

    private void initViews() {
        noResultsTv = (TextView) findViewById(R.id.tv_noResults);
        adapter = new AgentsLendersListAdapter(this);
        //  adapter.setStateList(states);

        RecyclerView listStates = (RecyclerView) findViewById(R.id.list);
        listStates.setHasFixedSize(true);
        listStates.setLayoutManager(new LinearLayoutManager(this));
        listStates.setAdapter(adapter);

        SearchView searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.clearFocus();
        ViewUtils.hideSearchPlate(searchView);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.isEmpty()) {
            noResultsTv.setVisibility(View.GONE);
            // adapter.setStateList(states);
        } else {
            // List<State> foundStates = findStates(newText);
            // adapter.setStateList(foundStates);
            // noResultsTv.setVisibility(foundStates.isEmpty() ? View.VISIBLE : View.GONE);
        }
        return false;
    }

    @Override
    public void onItemSelected(State state) {
        Intent resultIntent = new Intent();
       // resultIntent.putExtra(KEY_STATE, new Gson().toJson(state));
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
