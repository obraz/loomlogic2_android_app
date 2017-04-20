package com.loomlogic.network.managers;

import android.content.Context;
import android.content.SharedPreferences;

import com.kt.http.base.SharedGson;
import com.loomlogic.base.LoomLogicApp;
import com.loomlogic.network.responses.UserData;

enum ProfileDataStorage {
    INSTANCE;

    private final static String PROFILE_PREFERENCES_NAME = "com.loomlogic.profile.preferences";
    private final static String PROFILE_PREFERENCE_NAME = "com.loomlogic.profile.preferences.profile";

    private SharedPreferences profilePreferences;

    ProfileDataStorage() {
        profilePreferences = LoomLogicApp.getSharedContext().getSharedPreferences(PROFILE_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public void saveUserData(UserData response) {

        String jsonData;
        if (response != null) {
            jsonData = SharedGson.getGson().toJson(response);
        } else {
            jsonData = null;
        }
        putString(PROFILE_PREFERENCE_NAME, jsonData);
    }

    public UserData readUserData() {
        String jsonData = getString(PROFILE_PREFERENCE_NAME);
        if (jsonData != null) {
            return SharedGson.getGson().fromJson(jsonData, UserData.class);
        }
        return null;
    }

    private void putString(String key, String value) {
        if (value == null) {
            profilePreferences.edit().remove(key).apply();
        } else {
            profilePreferences.edit().putString(key, value).apply();
        }
    }

    private String getString(String key) {
        return profilePreferences.getString(key, null);
    }
}
