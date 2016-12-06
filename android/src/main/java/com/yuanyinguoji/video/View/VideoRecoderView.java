package com.yuanyinguoji.video.View;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.yuanyinguoji.video.R;
import com.yuanyinguoji.video.encoder.MediaAudioEncoder;
import com.yuanyinguoji.video.encoder.MediaEncoder;
import com.yuanyinguoji.video.encoder.MediaMuxerWrapper;
import com.yuanyinguoji.video.encoder.MediaVideoEncoder;

import java.io.IOException;

/**
 * Created by ckh on 2016/12/4.
 * 最低使用版本18 android 4.3
 */

public class VideoRecoderView extends BaseView{
    private static final String TAG ="<<<<<<<<<<<VideoRecoderView4.3>>>>>>>>>>>>>>>";
    private Context mContext;

    private CameraGLView mCameraView;//预览页面
    private CircleBar mCirclebar;//圆形按钮
    private ImageView mSwicth;//切换摄像头

    private String outputPath;//视频保存路径

    private MediaMuxerWrapper mMuxer;//视频流保存类

    public VideoRecoderView(Context context) {
        super(context);
        mContext = context;
        initview();
        initListener();
//        mCameraView.swicthCamera();
    }
    public VideoRecoderView(Context context,AttributeSet attrs){
        super(context,attrs);
        mContext = context;
        initview();
        initListener();
//        mCameraView.swicthCamera();
    }


    private void initview(){
        inflate(mContext,R.layout.view_video,this);
        mCameraView = (CameraGLView)findViewById(R.id.cameraView);
        mCameraView.setVideoSize(640, 480);
        mCirclebar = (CircleBar) findViewById(R.id.circlebar);
        mSwicth = (ImageView) findViewById(R.id.imageView);
    }



    private void initListener(){
        mCirclebar.setOnViewTouchListener(new CircleBar.OnTouchListener() {
            @Override
            public void onStart() {
                startRecording();
                outputPath = mMuxer.getOutputPath();
                mSwicth.setVisibility(View.GONE);
            }

            @Override
            public void onStop() {
                stopRecording();
                Log.e("tag","outputPath:"+outputPath);

            }
        });
        mSwicth.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("TAG","onClick");
                mCameraView.swicthCamera();
            }
        });
    }

    /**
     * start resorcing
     * This is a sample project and call this on UI thread to avoid being complicated
     * but basically this should be called on private thread because prepareing
     * of encoder is heavy work
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void startRecording() {
        Log.e("tag", "startRecording:");
        try {
            mMuxer = new MediaMuxerWrapper(".mp4",mContext);	// if you record audio only, ".m4a" is also OK.
            if (true) {
                // for video capturing
                new MediaVideoEncoder(mMuxer, mMediaEncoderListener, mCameraView.getVideoWidth(), mCameraView.getVideoHeight());
            }
            if (true) {
                // for audio capturing
                new MediaAudioEncoder(mMuxer, mMediaEncoderListener);
            }
            mMuxer.prepare();
            mMuxer.startRecording();
        } catch (final IOException e) {
            Log.e("tag", "startCapture:", e);
        }
    }


    /**
     * request stop recording
     */
    private void stopRecording() {
        Log.e("TAG", "stopRecording:mMuxer=" + mMuxer);
        if (mMuxer != null) {
            mMuxer.stopRecording();
            mMuxer = null;
            // you should not wait here
            mSwicth.setVisibility(View.VISIBLE);
            if(videoListener!=null){
                videoListener.onRecorded(outputPath);
            }
        }
    }

    /**
     * callback methods from encoder
     */
    private final MediaEncoder.MediaEncoderListener mMediaEncoderListener = new MediaEncoder.MediaEncoderListener() {
        @Override
        public void onPrepared(final MediaEncoder encoder) {
            Log.e("TAG", "onPrepared:encoder=" + encoder);
            if (encoder instanceof MediaVideoEncoder)
                mCameraView.setVideoEncoder((MediaVideoEncoder)encoder);
        }

        @Override
        public void onStopped(final MediaEncoder encoder) {
            Log.e("TAG", "onStopped:encoder=" + encoder);
            if (encoder instanceof MediaVideoEncoder)
                mCameraView.setVideoEncoder(null);
        }
    };

    @Override
    public void onResume() {
        mCameraView.onResume();
    }

    @Override
    public void onPause() {
        stopRecording();
        mCameraView.onPause();
    }
}
