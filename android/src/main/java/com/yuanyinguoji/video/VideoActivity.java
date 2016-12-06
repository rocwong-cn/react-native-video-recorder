package com.yuanyinguoji.video;

import android.app.Activity;
import android.os.Bundle;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.yuanyinguoji.video.View.BaseView;
import com.yuanyinguoji.video.View.VideoAndPlayer;

/**
 * Created by wa on 2016/11/21.
 */

public class VideoActivity extends Activity{
    private BaseView mVideoView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVideoView = new VideoAndPlayer(this);
        setContentView(mVideoView);
        mVideoView.setOnVideoStateListener(new BaseView.onVideoStateListener() {
            @Override
            public void onRecorded(String output) {
                WritableMap params = Arguments.createMap();
                params.putString("path",output);
                VideoModule.setEvent("onVideoSave",params);
                finish();
            }
        });
    }
}
