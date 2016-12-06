package com.yuanyinguoji.video.View;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Switch;
import android.widget.Toast;

import com.yuanyinguoji.video.R;
import com.yuanyinguoji.video.VideoRecdeView;
import com.yuanyinguoji.video.utils.FileUtils;
import com.yuanyinguoji.video.utils.ScreenUtils;

import java.io.IOException;

/**
 * Created by ckh on 2016/12/5.
 */

public class VideoAndPlayer extends BaseView {
    private Context mContext;
    private PlayerView mPlayerView;
    private BaseView mVideoView;
    private String output = "/storage/emulated/0/com.mvideo/Video/2016-12-05-15-06-42.mp4";

    public VideoAndPlayer(Context context) {
        super(context);
        mContext = context;
        initview();
        setListener();
    }

    private void initview() {
       inflate(mContext,R.layout.view_video_recode,this);
        mPlayerView  = (PlayerView) findViewById(R.id.player);
        mVideoView = (BaseView) findViewById(R.id.video);
        mPlayerView.scrollTo(ScreenUtils.getScreenWidth(mContext),0);
    }

    private void setListener(){
        mVideoView.setOnVideoStateListener(new BaseView.onVideoStateListener() {
            @Override
            public void onRecorded(String output) {
                mVideoView.scrollTo(ScreenUtils.getScreenWidth(mContext)*2,0);
                mPlayerView.scrollTo(0,0);
                try {
                    Thread.sleep(500);//0.5秒后把值传给播放器
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mPlayerView.setVideoPath(output);
            }
        });
        mPlayerView.setonPlayerClick(new PlayerView.onPlayerClick() {
            @Override
            public void onDel(String output) {
                switch (FileUtils.delFile(output)){
                    case 0:
                        Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                    case 1:
                        Toast.makeText(mContext,"文件不存在",Toast.LENGTH_SHORT).show();
                    case 2:
                        Toast.makeText(mContext, "文件路径为空", Toast.LENGTH_SHORT).show();
                        mPlayerView.scrollTo(ScreenUtils.getScreenWidth(mContext),0);//隐藏播放view
                        mVideoView.scrollTo(0,0);
                        break;
                    case 3:
                        Toast.makeText(mContext, "删除失败", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onSave(String output) {
                if(videoListener!=null){
                    videoListener.onRecorded(output);
                }
            }
        });
    }
    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

}
