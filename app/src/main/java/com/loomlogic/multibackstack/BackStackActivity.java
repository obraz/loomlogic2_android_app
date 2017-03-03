package com.loomlogic.multibackstack;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.util.Log;

import com.loomlogic.base.BaseActivity;

public abstract class BackStackActivity extends BaseActivity {
    private static final String STATE_BACK_STACK_MANAGER = "back_stack_manager";

    protected BackStackManager backStackManager;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        backStackManager = new BackStackManager();
    }

    @Override
    protected void onDestroy() {
        backStackManager = null;
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STATE_BACK_STACK_MANAGER, backStackManager.saveState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        backStackManager.restoreState(savedInstanceState.getParcelable(STATE_BACK_STACK_MANAGER));
    }

    /**
     * @return false if failed to put fragment in back stack. Relates to issue:
     * java.lang.IllegalStateException: Fragment is not currently in the FragmentManager at
     * android.support.v4.app.FragmentManagerImpl.saveFragmentInstanceState(FragmentManager.java:702)
     */
    protected boolean pushFragmentToBackStack(int hostId, @NonNull Fragment fragment) {
        try {
            BackStackEntry entry = BackStackEntry.create(getSupportFragmentManager(), fragment);
            backStackManager.push(hostId, entry);
            return true;
        } catch (Exception e) {
            Log.e("MultiBackStack", "Failed to add fragment to back stack", e);
            return false;
        }
    }

    @Nullable
    protected Fragment popFragmentFromBackStack(int hostId) {
        BackStackEntry entry = backStackManager.pop(hostId);
        return entry != null ? entry.toFragment(this) : null;
    }

    @Nullable
    protected Pair<Integer, Fragment> popFragmentFromBackStack() {
        Pair<Integer, BackStackEntry> pair = backStackManager.pop();
        return pair != null ? Pair.create(pair.first, pair.second.toFragment(this)) : null;
    }

    /**
     * @return false if back stack is missing.
     */
    protected boolean resetBackStackToRoot(int hostId) {
        return backStackManager.resetToRoot(hostId);
    }

    /**
     * @return false if back stack is missing.
     */
    protected boolean clearBackStack(int hostId) {
        return backStackManager.clear(hostId);
    }

}