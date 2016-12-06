package com.yuanyinguoji.video.View;

import android.app.Dialog;
import android.content.Context;

import com.yuanyinguoji.video.R;

/**
 * Created by ckh on 2016/12/6.
 */

public class Loadding extends Dialog{
    private Context mContext;
    public Loadding(Context context) {
        super(context);
        initview();
    }

    private void initview(){
        setContentView(R.layout.view_loadding);
        setCancelable(false);
    }
}
