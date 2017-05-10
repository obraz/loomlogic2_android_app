package com.loomlogic.leads.create;

import android.telephony.PhoneNumberFormattingTextWatcher;

/**
 * Created by alex on 5/10/17.
 */

public class CustomPhoneTextWatcher extends PhoneNumberFormattingTextWatcher {
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        super.onTextChanged(s, start, before, count);
    }
}
