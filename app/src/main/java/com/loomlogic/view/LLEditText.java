package com.loomlogic.view;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by alex on 4/13/17.
 */

public class LLEditText extends TextInputEditText {
    public LLEditText(Context context) {
        super(context);
    }

    public LLEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(attrs);
    }

    public LLEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(attrs);
    }

    private void initViews(AttributeSet attrs) {
    }

    @Override
    public View focusSearch(int direction) {
        View v = super.focusSearch(direction);
        if (v != null) {
            if (v.isEnabled()) {
                return v;
            } else {
                // keep searching
                return v.focusSearch(direction);
            }
        }
        return v;
    }
}
