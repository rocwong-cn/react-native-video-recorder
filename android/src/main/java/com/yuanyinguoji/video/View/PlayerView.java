package com.yuanyinguoji.video.View;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.yuanyinguoji.video.R;
import com.yuanyinguoji.video.utils.ScreenUtils;


/**
 * Created by ckh on 2016/12/5.
 */

public class PlayerView extends BaseView {
    private Context mContext;
    private android.widget.VideoView mVideoView;

    private ImageView mPlayerControl;
    private ImageView mDel;
    private ImageView mSave;

    private onPlayerClick mPlayerClick;
    private String output;


    public PlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
        initListener();
    }

    public PlayerView(Context context) {
        super(context);
        mContext = context;
        initView();
        initListener();

    }

    private void initView() {
        inflate(mContext, R.layout.view_player, this);
        mVideoView = (android.widget.VideoView) findViewById(R.id.videoView);
        mPlayerControl = (ImageView) findViewById(R.id.iv_player);
        mVideoView.getHolder().setFixedSize(ScreenUtils.getScreenWidth(mContext), ScreenUtils.getScreenWidth(mContext));
        mSave = (ImageView) findViewById(R.id.iv_save);
        mDel = (ImageView) findViewById(R.id.iv_del);
        setFullScreen();
    }
    private void initListener(){
        mDel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPlayerClick!=null)
                    mPlayerClick.onDel(output);

            }
        });
        mSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPlayerClick!=null)
                    mPlayerClick.onSave(output);
            }
        });

        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                Log.e("TAG", "aaaaaaaaaa");
//                mediaPlayer.start();
                return false;
            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mPlayerControl.setBackgroundResource(R.drawable.videostop);
            }
        });
        mPlayerControl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mVideoView.isPlaying()) {
                    mPlayerControl.setBackgroundResource(R.drawable.videostop);
                    mVideoView.pause();
                } else {
                    mPlayerControl.setBackgroundResource(R.drawable.videopause);
                    mVideoView.start();
                }
            }
        });
        mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
                return false;
            }
        });
    }
    private void setFullScreen(){
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mVideoView.setLayoutParams(layoutParams);
    }
    public void setonPlayerClick(onPlayerClick playerClick){
        mPlayerClick = playerClick;
    }

    public interface onPlayerClick{
        void onDel(String output);
        void onSave(String output);
    }

    public void setVideoPath(String path) {
        Log.e("TAG","setvideoPath"+path);
        output = path;
        mVideoView.setVideoPath(output);
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }
}
