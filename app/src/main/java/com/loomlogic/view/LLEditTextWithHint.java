package com.loomlogic.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.loomlogic.R;

import java.lang.reflect.Field;

/**
 * Created by alex on 4/12/17.
 */

public class LLEditTextWithHint extends TextInputLayout {
    private boolean isError;
    private LLEditText editText;
    private TextInputLayout textInput;
    private View divider;
    private boolean enabled;

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

    private void initViews(AttributeSet attrs) {
        inflate(getContext(), R.layout.view_edittext_hint, this);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LLEditTextWithHint);

        String hint = "";
        int inputType = InputType.TYPE_CLASS_TEXT;
        int imeOptions = EditorInfo.IME_ACTION_NEXT;
        int maxLength = -1;
        Drawable drawableRight = null;
        enabled = true;

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

        divider = findViewById(R.id.editTextDivider);

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

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                removeError();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    public void setText(String text) {
        editText.setText(text);
    }

    public void setHint(@StringRes int hint) {
        setHint(getResources().getString(hint));
    }

    public void setHint(String hint) {
        textInput.setHint(hint);
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

    public void setError() {
        isError = true;
        //  textInput.setHintTextAppearance(R.style.EditTextError);
        //  editText.setHintTextColor(ContextCompat.getColor(getContext(), R.color.colorMainEditTextError));
        setInputTextLayoutColor(R.color.colorMainEditTextError);
        divider.getBackground().mutate().setColorFilter(ContextCompat.getColor(getContext(), R.color.colorMainEditTextError), PorterDuff.Mode.SRC_ATOP);

      //  if (!enabled) {
            requestFocus(FOCUS_UP);
            clearFocus();
           // getParent().requestLayout();//requestFocus();
       // }

//        if (hasFocus()) {
//            clearFocus();
//            requestFocus();
//        }
        // requestFocus();

        divider.invalidate();
    }

    public boolean removeError() {
        if (isError) {
            setInputTextLayoutColor(R.color.colorMainEditTextHint);
            divider.getBackground().mutate().setColorFilter(ContextCompat.getColor(getContext(), R.color.colorMainEditTextDivider), PorterDuff.Mode.SRC_ATOP);

            divider.invalidate();
            isError = false;
            return true;
        }
        return false;
    }

    public String getText() {
        return editText.getText().toString();
    }

    public void setInputTextLayoutColor(@ColorRes int resColor) {
        try {
            Field fDefaultTextColor = TextInputLayout.class.getDeclaredField("mDefaultTextColor");
            fDefaultTextColor.setAccessible(true);
            fDefaultTextColor.set(textInput, new ColorStateList(new int[][]{{0}}, new int[]{ContextCompat.getColor(getContext(), resColor)}));

            Field fFocusedTextColor = TextInputLayout.class.getDeclaredField("mFocusedTextColor");
            fFocusedTextColor.setAccessible(true);
            fFocusedTextColor.set(textInput, new ColorStateList(new int[][]{{0}}, new int[]{ContextCompat.getColor(getContext(), resColor)}));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
