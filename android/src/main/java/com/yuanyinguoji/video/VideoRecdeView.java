package com.yuanyinguoji.video;

import android.content.Context;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.yuanyinguoji.video.View.BaseView;
import com.yuanyinguoji.video.View.VideoAndPlayer;

/**
 * Created by ckh on 2016/12/4.
 */

public class VideoRecdeView extends SimpleViewManager<View> implements LifecycleEventListener{
    private Context mContext;
    private BaseView mVideoView;
    private BaseView mPlayerView;


    private PowerManager powerManager = null;
    private PowerManager.WakeLock wakeLock = null;
    @Override
    public String getName() {
        return "VideoView";
    }
    public VideoRecdeView(Context context){
        mContext =context;
    }

    @Override
    protected View createViewInstance(final ThemedReactContext reactContext) {
        Log.e("TAG","视频加载中....");
        VideoAndPlayer view = new VideoAndPlayer(reactContext);

        return view;
    }

    private void wackLock(Context context){
        this.powerManager = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
        this.wakeLock = this.powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
        this.wakeLock.acquire();
    }


    private void registerPermission(){

    }

//    @Override
//    public void onDropViewInstance(BaseView view) {
//        super.onDropViewInstance(view);
//        if(mVideoView!=null)
//            mVideoView.onPause();
//
//        Timer timer = new Timer();
//        TimerTask tast = new TimerTask() {
//            @Override
//            public void run() {
//                if (wakeLock.isHeld())
//                    wakeLock.release();
//            }
//        };
//        timer.schedule(tast,10*1000);//10秒后熄屏
//    }

    @Override
    public void onHostResume() {
        if(mVideoView!=null)
            mVideoView.onResume();
    }

    @Override
    public void onHostPause() {
        if(mVideoView!=null)
            mVideoView.onPause();
    }

    @Override
    public void onHostDestroy() {

    }
}
