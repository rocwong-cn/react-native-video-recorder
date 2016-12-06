package com.yuanyinguoji.video.media;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.MediaCodec;
import android.os.Build;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by wa on 2016/11/22.
 */

public class CameraManager implements SurfaceHolder.Callback,Camera.PreviewCallback {

    private static CameraManager sCameraManager;
    private Context mContext;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private Camera.Parameters parameters;
    private static int cameraPosition = Camera.CameraInfo.CAMERA_FACING_FRONT;//默认前置？

    private VideoManager mVideManager;

    private MediaCodec mediaCodec;

    private CameraManager(Context context, SurfaceView surfaceView) {
        this.mContext = context;
        mSurfaceView = surfaceView;
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mVideManager = new VideoManager(mCamera, mSurfaceHolder);
    }

    public static CameraManager getInstance(Context context, SurfaceView surfaceView) {
        if (sCameraManager == null) {
            sCameraManager = new CameraManager(context, surfaceView);
        }
        return sCameraManager;
    }

    public static int getcameraPosition() {
        return cameraPosition;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        switchoverCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        initCamera();
        mSurfaceHolder = null;
        mSurfaceView = null;
        mCamera.lock();
    }

    /**
     * 切换摄像头
     */
    public void switchoverCamera() {
        initCamera();
        openCamera();
        autoFocus();
        cameraPosition = cameraPosition == Camera.CameraInfo.CAMERA_FACING_FRONT ? Camera.CameraInfo.CAMERA_FACING_BACK : Camera.CameraInfo.CAMERA_FACING_FRONT;
    }

    //初始化摄像头状态（清空状态）
    private void initCamera() {
        if (mCamera == null) {
            return;
        }
        mCamera.stopPreview();//停掉原来摄像头的预览
        mCamera.release();//释放资源
        mCamera = null;//取消原来摄像头
    }

    //打开摄像头
    private void openCamera() {
        if (mCamera == null) {
            mCamera = Camera.open(cameraPosition);
            try {
                mCamera.setPreviewDisplay(mSurfaceHolder);//通过surfaceview显示取景画面
                mCamera.setDisplayOrientation(90);
                mCamera.setPreviewCallback(this);
                mCamera.startPreview();//开始预览
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //实现自动对焦
    private void autoFocus() {
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if (success) {
                    parameters = mCamera.getParameters();
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//1连续对焦
                    mCamera.setParameters(parameters);
                    mCamera.startPreview();
                    mCamera.cancelAutoFocus();//只有加上了这一句，才会自动对焦。
                }
            }
        });
    }


    public Camera getCamera() {
        return mCamera;
    }

    public void startVideo() {
        mVideManager.start(mCamera);
    }

    public void stopVideo() {
        mVideManager.stop();
    }

    /**
     * 图片反转
     *
     * @param bmp
     * @param flag 0为水平反转，1为垂直反转
     * @return
     */
    public static Bitmap reverseBitmap(Bitmap bmp, int flag) {
        float[] floats = null;
        switch (flag) {
            case 0: // 水平反转
                floats = new float[]{-1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f};
                break;
            case 1: // 垂直反转
                floats = new float[]{1f, 0f, 0f, 0f, -1f, 0f, 0f, 0f, 1f};
                break;
        }

        if (floats != null) {
            Matrix matrix = new Matrix();
            matrix.setValues(floats);
            return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
        }

        return null;
    }

    private void initMediaCode() throws IOException {

    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
//        MediaMuxer me = new MediaMuxer("",1);

    }
}
