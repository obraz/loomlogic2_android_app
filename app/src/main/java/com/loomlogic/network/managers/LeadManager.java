package com.loomlogic.network.managers;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.kt.http.base.BaseRequest;
import com.kt.http.base.ResponseData;
import com.kt.http.base.client.KTClient;
import com.loomlogic.leads.create.LeadAction;
import com.loomlogic.network.requests.LeadRequestBuilder;
import com.loomlogic.network.requests.data.LeadRequestData;
import com.loomlogic.network.responses.UserData;


/**
 * Created by alex on 5/5/17.
 */

public class LeadManager extends BaseItemManager<UserData, Bundle, LeadAction> {
    public static final String KEY_LEAD_DATA = "KEY_LEAD_DATA";

    public LeadManager(@NonNull KTClient client) {
        super(client);
    }

    @Override
    protected BaseRequest getFetchRequest(KTClient client, Bundle requestParams) {
        switch (getTagFromBundle(requestParams)) {
            case CREATE:
                return new LeadRequestBuilder(getLeadDataFromBundle(requestParams)).create();
            case EDIT:
            default:
                return null;
        }
    }

    @Override
    protected LeadAction getEntityRequestTag(Bundle params) {
        return getTagFromBundle(params);
    }

    @Override
    protected UserData readResponseFromRequest(BaseRequest request, ResponseData data, LeadAction tag) {
        return null;
    }

    @Override
    protected boolean storeResponse(UserData response, LeadAction tag) {
        return false;
    }

    @Override
    protected UserData restoreResponse(LeadAction tag) {
        return null;
    }

    public void createNewLead(LeadRequestData leadRequestData) {
        fetchData(getCreateNewLeadBundle(leadRequestData));
    }

    private Bundle getCreateNewLeadBundle(LeadRequestData leadRequestData) {
        Bundle result = new Bundle();
        result.putString(KEY_LEAD_DATA, new Gson().toJson(leadRequestData));
        result.putSerializable(KEY_TAG, LeadAction.CREATE);
        return result;
    }

    private LeadAction getTagFromBundle(Bundle bundle) {
        return (LeadAction) bundle.getSerializable(KEY_TAG);
    }

    private LeadRequestData getLeadDataFromBundle(Bundle bundle) {
        return new Gson().fromJson(bundle.getString(KEY_LEAD_DATA), LeadRequestData.class);
    }
}
