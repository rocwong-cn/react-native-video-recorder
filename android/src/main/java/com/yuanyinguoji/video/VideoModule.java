package com.yuanyinguoji.video;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.yuanyinguoji.video.View.Loadding;

/**
 * Created by ckh on 2016/12/5.
 */

public class VideoModule extends ReactContextBaseJavaModule{
    private static ReactApplicationContext mContext;
    public static boolean isFinish =false;//是否关闭模块
    private Loadding mLodding;
    public VideoModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mContext = reactContext;
    }

    @Override
    public String getName() {
        return "VideoRecorder";
    }

    @ReactMethod
    public void navToVideoRecorder(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR2){
            Intent intent = new Intent(mContext,VideoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }else{
            Toast.makeText(mContext, "暂时不支持此机型录制视频", Toast.LENGTH_SHORT).show();
        }
    }

//    @ReactMethod
//    public void hideLoading(boolean flag){
//        if(mLodding==null){
//            mLodding= new Loadding(mContext);
//        }
//        if(flag){
//            mLodding.show();
//        }else{
//            if(mLodding.isShowing()){
//                mLodding.dismiss();
//            }
//        }
//    }

    @ReactMethod
    public void hideLoading(boolean flag){
        if(!flag){
            Toast.makeText(mContext, "上传视频失败", Toast.LENGTH_SHORT).show();
        }
        isFinish = flag;
        Loadding.dismissLoadding();
    }

    private static void sendEvent(ReactContext reactContext,
                           String eventName,
                           @Nullable WritableMap params) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    public static void setEvent(String eventName,WritableMap params){
        sendEvent(mContext,eventName,params);
    }


}
