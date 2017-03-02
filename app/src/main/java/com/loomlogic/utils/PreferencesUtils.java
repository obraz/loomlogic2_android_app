package com.loomlogic.utils;

import android.content.SharedPreferences;

import com.loomlogic.base.LoomLogicApp;

/**
 * Created by alex on 3/2/17.
 */

public class PreferencesUtils {

    protected static SharedPreferences getSharedPreferences() {
        return LoomLogicApp.getSharedContext().getSharedPreferences(LoomLogicApp.getSharedContext().getPackageName(), LoomLogicApp.getSharedContext().MODE_PRIVATE);
    }

}
