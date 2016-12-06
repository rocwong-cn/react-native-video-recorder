package com.yuanyinguoji.video.View;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import com.yuanyinguoji.video.R;
import com.yuanyinguoji.video.media.CameraManager;
import com.yuanyinguoji.video.media.VideoManager;

/**
 * Created by wa on 2016/11/21.
 */

public class RecordVideo extends FrameLayout {
    private Context mContext;

    private CircleBar mVideoBtn;
    private SurfaceView mSurfaceView;
    private CameraManager mCameraManager;
    private VideoManager mVideManager;

    public RecordVideo(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public RecordVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        inflate(mContext, R.layout.view_record_video, this);
        mVideoBtn = (CircleBar) findViewById(R.id.circlebar);
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        mCameraManager = CameraManager.getInstance(mContext,mSurfaceView);
        mCameraManager.switchoverCamera();
        Log.e("TAG","camera:"+mCameraManager.getCamera());
        Log.e("TAG","mSurfaceView:"+mSurfaceView.getHolder());
        mVideManager = new VideoManager(mCameraManager.getCamera(),mSurfaceView.getHolder());
        mVideoBtn.setOnViewTouchListener(new CircleBar.OnTouchListener() {
            @Override
            public void onStart() {
               mCameraManager.startVideo();
            }

            @Override
            public void onStop() {
                mCameraManager.stopVideo();
            }
        });
    }


}
