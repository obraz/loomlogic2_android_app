package com.loomlogic.leads.settings;

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
import com.loomlogic.R;
import com.loomlogic.base.BaseActivity;
import com.loomlogic.leads.entity.Gender;
import com.loomlogic.leads.entity.LeadParticipantItem;
import com.loomlogic.leads.entity.LeadParticipantRole;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 3/29/17.
 */

public class ParticipantAddActivity extends BaseActivity implements ParticipantsListAdapter.ParticipantSelectListener, SearchView.OnQueryTextListener {
    public static final String KEY_PARTICIPANT = "KEY_PARTICIPANT";
    private ParticipantsListAdapter adapter;
    private List<LeadParticipantItem> participantList;
    private TextView noResultsTv;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_add);
        setTitle(R.string.lead_add_participant_title);

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
        noResultsTv = (TextView) findViewById(R.id.tv_noResults);

        // TODO: 4/13/17 get real  data
        participantList = new ArrayList<LeadParticipantItem>();
        participantList.add(new LeadParticipantItem("http://loomlogic.ucoz.net/3.jpg", "Carl", "Snyder", Gender.MALE, "+5 (752) 915 25 20", "consectetud0@gmail.com", "Cherry Creek Mortgage0", LeadParticipantRole.LENDER));
        participantList.add(new LeadParticipantItem("http://loomlogic.ucoz.net/1.jpg", "Benjamin", "Carlson", Gender.MALE, "+5 (752) 915 25 21", "consectetud1@gmail.com", "Cherry Creek Mortgage1", LeadParticipantRole.AGENT));
        participantList.add(new LeadParticipantItem("", "William", "Miller", Gender.MALE, "+5 (752) 915 25 22", "consectetud2@gmail.com", "Cherry Creek Mortgage2", LeadParticipantRole.CLIENT));
        participantList.add(new LeadParticipantItem("http://loomlogic.ucoz.net/4.jpg", "Justin", "Keller", Gender.MALE, "+5 (752) 915 25 23", "consectetud3@gmail.com", "Cherry Creek Mortgage3", LeadParticipantRole.OWNER));
        participantList.add(new LeadParticipantItem("http://loomlogic.ucoz.net/3.jpg", "Carl", "Snyder", Gender.MALE, "+5 (752) 915 25 20", "consectetud0@gmail.com", "Cherry Creek Mortgage0", LeadParticipantRole.LENDER));
        participantList.add(new LeadParticipantItem("http://loomlogic.ucoz.net/1.jpg", "Benjamin", "Carlson", Gender.MALE, "+5 (752) 915 25 21", "consectetud1@gmail.com", "Cherry Creek Mortgage1", LeadParticipantRole.AGENT));
        participantList.add(new LeadParticipantItem("", "William", "Miller", Gender.MALE, "+5 (752) 915 25 22", "consectetud2@gmail.com", "Cherry Creek Mortgage2", LeadParticipantRole.CLIENT));
        participantList.add(new LeadParticipantItem("http://loomlogic.ucoz.net/4.jpg", "Justin", "Keller", Gender.MALE, "+5 (752) 915 25 23", "consectetud3@gmail.com", "Cherry Creek Mortgage3", LeadParticipantRole.OWNER));


        adapter = new ParticipantsListAdapter(this);
        adapter.setParticipantsList(participantList);

        RecyclerView listStates = (RecyclerView) findViewById(R.id.list);
        listStates.setHasFixedSize(true);
        listStates.setLayoutManager(new LinearLayoutManager(this));
        listStates.setAdapter(adapter);

        SearchView searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.clearFocus();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.isEmpty()) {
            noResultsTv.setVisibility(View.GONE);
            adapter.setParticipantsList(participantList);
        } else {
            List<LeadParticipantItem> foundItems = findItems(newText);
            adapter.setParticipantsList(foundItems);
            noResultsTv.setVisibility(foundItems.isEmpty() ? View.VISIBLE : View.GONE);
        }
        return false;
    }

    private List<LeadParticipantItem> findItems(String newText) {
        List<LeadParticipantItem> foundItems = new ArrayList<>();
        for (LeadParticipantItem item : participantList) {
            String fullName = item.firstName.toLowerCase() + " " + item.lastName.toLowerCase();
            if (fullName.contains(newText.toLowerCase()) || fullName.contains(newText.toLowerCase())) {
                foundItems.add(item);
            }
        }
        return foundItems;
    }

    @Override
    public void onParticipantSelect(LeadParticipantItem item) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_PARTICIPANT, new Gson().toJson(item));
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
