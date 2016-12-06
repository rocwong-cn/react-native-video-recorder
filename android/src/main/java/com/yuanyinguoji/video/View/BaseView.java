package com.yuanyinguoji.video.View;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by ckh on 2016/12/4.
 */

public abstract class BaseView extends FrameLayout {
    protected onVideoStateListener videoListener;

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseView(Context context) {
        super(context);
    }

    public abstract void onResume();

    public abstract void onPause();

    public interface onVideoStateListener {
        /**
         * 录制完毕
         *
         * @param output 文件路径
         */
        void onRecorded(String output);

    }

    /**
     * 视频录制状态监听
     *
     * @param videoListener
     */
    public void setOnVideoStateListener(onVideoStateListener videoListener) {
        this.videoListener = videoListener;
    }
}
