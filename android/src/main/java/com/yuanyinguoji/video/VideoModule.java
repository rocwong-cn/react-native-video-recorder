package com.yuanyinguoji.video;

import android.content.Intent;
import android.support.annotation.Nullable;

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
        Intent intent = new Intent(mContext,VideoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    @ReactMethod
    public void hideLoading(boolean flag){
        if(mLodding==null){
            mLodding= new Loadding(mContext);
        }
        if(flag){
            mLodding.show();
        }else{
            if(mLodding.isShowing()){
                mLodding.dismiss();
            }
        }
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
