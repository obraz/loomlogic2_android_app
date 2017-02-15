package com.loomlogic.signin.signup;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by alex on 2/15/17.
 */

public class SignUpFragmentAdapter extends FragmentStatePagerAdapter {
    public SignUpFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SignUpFirstStepFragment();
            case 1:
                return new SignUpSecondStepFragment();
        }

        return new Fragment();
    }

    @Override
    public int getCount() {
        return 2;
    }
}
