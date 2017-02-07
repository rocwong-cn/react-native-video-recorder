package com.yuanyinguoji.video;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.yuanyinguoji.video.View.BaseView;
import com.yuanyinguoji.video.View.Loadding;
import com.yuanyinguoji.video.View.VideoAndPlayer;
import com.yuanyinguoji.video.utils.FileUtils;

import java.io.File;

/**
 * Created by wa on 2016/11/21.
 */

public class VideoActivity extends Activity{
    private BaseView mVideoView;
    private String mOutput;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVideoView = new VideoAndPlayer(this);
        setContentView(mVideoView);
        mVideoView.setOnVideoStateListener(new BaseView.onVideoStateListener() {
            @Override
            public void onRecorded(String output) {
                Loadding.showLoadding(VideoActivity.this, new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        Log.e("tag", "loading is dismiss");
                        if (VideoModule.isFinish){
                            finish();
                        }

                    }
                });
                mOutput=output;
                File file = new File(output);
                WritableMap params = Arguments.createMap();
                String contentUrl = String.valueOf(FileUtils.getImageContentUri(VideoActivity.this,file));
                params.putString("path",contentUrl);
                Log.e("TAG","contentUrl:"+contentUrl);
                VideoModule.setEvent("onVideoSave",params);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileUtils.delFile(mOutput);//退出插件删除视频文件
    }
}
