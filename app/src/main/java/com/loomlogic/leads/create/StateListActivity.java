package com.loomlogic.leads.create;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loomlogic.R;
import com.loomlogic.base.BaseActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.loomlogic.utils.Utils.readFromAssets;

/**
 * Created by alex on 3/29/17.
 */

public class StateListActivity extends BaseActivity implements StatesListAdapter.StateSelectorCallback, SearchView.OnQueryTextListener {
    public static final String KEY_STATE = "KEY_STATE";
    private StatesListAdapter adapter;
    private List<State> states;
    private TextView noResultsTv;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lead_states);
        setTitle(R.string.create_new_lead_state_title);
        getToolbar().setNavigationIcon(R.drawable.ic_back);

        initViews();
    }

    private void initViews() {
        noResultsTv = (TextView) findViewById(R.id.tv_states_noResults);
        states = readStatesFromAssets();
        adapter = new StatesListAdapter(this);
        adapter.setStateList(states);

        RecyclerView listStates = (RecyclerView) findViewById(R.id.list_states);
        listStates.setHasFixedSize(true);
        listStates.setLayoutManager(new LinearLayoutManager(this));
        listStates.setAdapter(adapter);

        SearchView searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.clearFocus();
    }

    private List<State> readStatesFromAssets() {
        String states = readFromAssets(this, "StatesList.json");
        if (states != null) {
            Type listType = new TypeToken<List<State>>() {
            }.getType();
            return new Gson().fromJson(states, listType);
        } else {
            return new ArrayList<State>();
        }
    }

    @Override
    public void onStateSelected(State state) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_STATE, new Gson().toJson(state));
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.isEmpty()) {
            noResultsTv.setVisibility(View.GONE);
            adapter.setStateList(states);
        } else {
            List<State> foundStates = findStates(newText);
            adapter.setStateList(foundStates);
            noResultsTv.setVisibility(foundStates.isEmpty() ? View.VISIBLE : View.GONE);
        }
        return false;
    }

    private List<State> findStates(String newText) {
        List<State> foundStates = new ArrayList<>();
        for (State state : states) {
            if (state.name.toLowerCase().contains(newText.toLowerCase())) {
                foundStates.add(state);
            }
        }
        return foundStates;
    }
}
