package com.loomlogic.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.loomlogic.R;

/**
 * Created by alex on 4/12/17.
 */

public class LLEditTextWithHint extends LinearLayout {
    private LLEditText editText;
    private TextInputLayout textInput;

    public LLEditTextWithHint(Context context) {
        super(context);
    }

    public LLEditTextWithHint(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(attrs);
    }

    public LLEditTextWithHint(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(attrs);
    }

    public LLEditTextWithHint(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(attrs);
    }

    private void initViews(AttributeSet attrs) {
        inflate(getContext(), R.layout.view_edittext_hint, this);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LLEditTextWithHint);

        String hint = "";
        int inputType = InputType.TYPE_CLASS_TEXT;
        int imeOptions = EditorInfo.IME_ACTION_NEXT;
        int maxLength = -1;
        Drawable drawableRight = null;
        boolean enabled = true;

        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.LLEditTextWithHint_android_hint:
                    hint = a.getString(attr);
                    break;
                case R.styleable.LLEditTextWithHint_android_inputType:
                    inputType = a.getInt(attr, EditorInfo.TYPE_TEXT_VARIATION_NORMAL);
                    break;
                case R.styleable.LLEditTextWithHint_android_imeOptions:
                    imeOptions = a.getInt(attr, 0);
                    break;
                case R.styleable.LLEditTextWithHint_android_maxLength:
                    maxLength = a.getInt(attr, -1);
                    break;
                case R.styleable.LLEditTextWithHint_android_drawableRight:
                    drawableRight = a.getDrawable(attr);
                    break;
                case R.styleable.LLEditTextWithHint_android_enabled:
                    enabled = a.getBoolean(attr, true);
                    break;
                default:
                    Log.d("TAG", "Unknown attribute for " + getClass().toString() + ": " + attr);
                    break;
            }
        }

        a.recycle();

        textInput = (TextInputLayout) findViewById(R.id.ti_textInput);
        textInput.setHint(hint);

        editText = (LLEditText) findViewById(R.id.et_editText);
        editText.setInputType(inputType);
        editText.setImeOptions(imeOptions);
        editText.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawableRight, null);
        editText.setEnabled(enabled);

        if (maxLength > -1) {
            InputFilter[] FilterArray = new InputFilter[1];
            FilterArray[0] = new InputFilter.LengthFilter(maxLength);
            editText.setFilters(FilterArray);
        }

        if (inputType == 131073) {
            textInput.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }


    public void setText(String text) {
        editText.setText(text);
    }

    public void setMaxSelection() {
        editText.setSelection(editText.length());
    }

    public void setSelection(int length) {
        editText.setSelection(length);
    }

    public void addTextChangedListener(TextWatcher textWatcher) {
        editText.addTextChangedListener(textWatcher);
    }

    public void removeTextChangedListener(TextWatcher textWatcher) {
        editText.removeTextChangedListener(textWatcher);
    }

    public EditText getEditText() {
        return editText;
    }

    public void setError(String error) {
        editText.setError(error);
    }

    public String getText() {
        return editText.getText().toString();
    }
}
