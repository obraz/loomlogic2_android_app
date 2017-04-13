package com.loomlogic.leads.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loomlogic.R;
import com.loomlogic.base.BaseActivity;
import com.loomlogic.leads.entity.LeadParticipantItem;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 3/29/17.
 */

public class ParticipantsListActivity extends BaseActivity implements ParticipantsListAdapter.ParticipantRemoveListener {
    public static final String KEY_PARTICIPANT_LIST = "KEY_PARTICIPANT_LIST";
    private static final int RESULT_ADD_PARTICIPANT = 736;
    private List<LeadParticipantItem> listItems;
    private ParticipantsListAdapter adapter;

    public static Intent getParticipantsListActivityIntent(Context context, ArrayList<LeadParticipantItem> participantList) {
        Intent intent = new Intent(context, ParticipantsListActivity.class);
        intent.putExtra(KEY_PARTICIPANT_LIST, new Gson().toJson(participantList));
        return intent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participants_list);
        setTitle(R.string.lead_edit_participants_title);

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
        adapter = new ParticipantsListAdapter(this);

        Type listType = new TypeToken<ArrayList<LeadParticipantItem>>() {
        }.getType();
        listItems = new Gson().fromJson(getIntent().getStringExtra(KEY_PARTICIPANT_LIST), listType);

        adapter.setParticipantsList(listItems);

        RecyclerView listView = (RecyclerView) findViewById(R.id.list);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(adapter);

        findViewById(R.id.fab_new).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ParticipantsListActivity.this, ParticipantAddActivity.class), RESULT_ADD_PARTICIPANT);
            }
        });
    }


    @Override
    public void onParticipantRemove(LeadParticipantItem item) {
        listItems.remove(item);
        adapter.notifyDataSetChanged();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_ADD_PARTICIPANT:
                    participantPicked(data);
                    break;
            }
        }
    }

    private void participantPicked(Intent data) {
        LeadParticipantItem item = new Gson().fromJson(data.getExtras().getString(ParticipantAddActivity.KEY_PARTICIPANT), LeadParticipantItem.class);
        listItems.add(item);
        adapter.notifyDataSetChanged();
    }
}
