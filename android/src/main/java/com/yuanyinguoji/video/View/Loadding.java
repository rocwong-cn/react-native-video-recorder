package com.yuanyinguoji.video.View;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import com.yuanyinguoji.video.R;

/**
 * Created by ckh on 2016/12/6.
 */

public class Loadding extends Dialog {
    private Context mContext;
    private static Loadding sLoadding;

    public Loadding(Context context) {
        super(context);
        initview();
    }

    private void initview() {
        setContentView(R.layout.view_loadding);
        setCancelable(false);
    }

    public static void showLoadding(Activity context, OnDismissListener onDismissListener) {
        if (sLoadding == null) {
            sLoadding = new Loadding(context);
        }
        if (!context.isFinishing()) {
            sLoadding.show();
            sLoadding.setOnDismissListener(onDismissListener);
        }
    }

    public static void dismissLoadding() {
        if (sLoadding != null && sLoadding.isShowing()) {
            sLoadding.dismiss();
            sLoadding =null;
        }
    }

}
