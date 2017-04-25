package com.loomlogic;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loomlogic.base.BaseActivity;

/**
 * Created by alex on 2/14/17.
 */

public class Test extends BaseActivity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setText("RESET PASSWORD");
        addContentView(tv, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        String token = getIntent().getData().getQueryParameter("token");
        Log.e("onCreate: ",""+token );
    }


}
