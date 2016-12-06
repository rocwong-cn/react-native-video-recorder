package com.yuanyinguoji.video.View;

import android.content.Context;
import android.widget.LinearLayout;

import com.yuanyinguoji.video.R;

/**
 * Created by ckh on 2016/12/5.
 */

public class TestView extends LinearLayout{
    public TestView(Context context) {
        super(context);
        inflate(context, R.layout.view_test,this);
    }
}
