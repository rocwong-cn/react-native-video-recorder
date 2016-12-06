package com.yuanyinguoji.video.media;

import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.SurfaceHolder;

import com.yuanyinguoji.video.utils.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by wa on 2016/11/22.
 */

public class VideoManager implements MediaRecorder.OnErrorListener {
    private MediaRecorder mMediaRecorder;
    private Camera mCamera;
    private SurfaceHolder mSurfaceHolder;
    private File videoFile;

    public VideoManager(Camera camera, SurfaceHolder surfaceHolder){
        this.mSurfaceHolder = surfaceHolder;
    }

    /**
     * 释放摄像头资源
     *
     * @author liuyinjun
     * @date 2015-2-5
     */
    private void freeCameraResource() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.lock();
            mCamera.release();
            mCamera = null;
        }
    }

    private void initRecord() throws IOException {
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.reset();
        if (mCamera != null){
            mCamera.unlock();
            mMediaRecorder.setCamera(mCamera);
        }
        mMediaRecorder.setOnErrorListener(this);
        mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);// 视频源
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 音频源
//        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);// 视频输出格式
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);// 音频格式
        mMediaRecorder.setVideoSize(1920, 1080);// 设置分辨率：
        // mMediaRecorder.setVideoFrameRate(16);// 这个我把它去掉了，感觉没什么用
        mMediaRecorder.setVideoEncodingBitRate(1 * 1024 * 512);// 设置帧频率，然后就清晰了
        if(CameraManager.getcameraPosition()== Camera.CameraInfo.CAMERA_FACING_BACK){
            mMediaRecorder.setOrientationHint(270);// 输出旋转90度，保持竖屏
            Log.e("TAG","前摄像头");
        }else{
            Log.e("TAG","后摄像头");
            mMediaRecorder.setOrientationHint(90);// 输出旋转90度，保持竖屏录制
        }
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);// 视频录制格式
        // mediaRecorder.setMaxDuration(Constant.MAXVEDIOTIME * 1000);
        videoFile = FileUtils.getFile();
        mMediaRecorder.setOutputFile(videoFile.getAbsolutePath());
//        mMediaRecorder.setOrientationHint(CameraUtils.getDisplayOrientation(CameraUtils.getDisplayRotation((Activity) mContext), cameraPosition));
        mMediaRecorder.prepare();
        try {
            mMediaRecorder.start();
        } catch (IllegalStateException e) {
            mCamera.lock();
            e.printStackTrace();
        } catch (RuntimeException e) {
            mCamera.lock();
            e.printStackTrace();
        } catch (Exception e) {
            mCamera.lock();
            e.printStackTrace();
        }
    }
    public void start(Camera camera){
        mCamera = camera;
        try {
            initRecord();
        } catch (IOException e) {
            mCamera.lock();
            e.printStackTrace();
        }

    }


    @Override
    public void onError(MediaRecorder mediaRecorder, int i, int i1) {

    }

    /**
     * 停止录制
     *
     * @author liuyinjun
     * @date 2015-2-5
     */
    public void stopRecord() {
        if (mMediaRecorder != null) {
            // 设置后不会崩
            mMediaRecorder.setOnErrorListener(null);
            mMediaRecorder.setPreviewDisplay(null);
            try {
                mMediaRecorder.stop();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (RuntimeException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 停止拍摄
     *
     * @author liuyinjun
     * @date 2015-2-5
     */
    public void stop() {
        stopRecord();
        releaseRecord();
        freeCameraResource();
        playVideo();
    }

    private void playVideo(){
        MediaPlayer player=new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setDisplay(mSurfaceHolder);
        //设置显示视频显示在SurfaceView上
        try {
            player.setDataSource(videoFile.getAbsolutePath());
            player.prepare();
            player.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放资源
     *
     * @author liuyinjun
     * @date 2015-2-5
     */
    private void releaseRecord() {
        if (mMediaRecorder != null) {
            mMediaRecorder.setOnErrorListener(null);
            try {
                mMediaRecorder.release();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mMediaRecorder = null;
    }
}
