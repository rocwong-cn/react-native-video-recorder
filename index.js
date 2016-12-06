/**
 * Created by Roc on 2016/11/30.
 */

import { NativeModules ,NativeAppEventEmitter} from 'react-native';

const VideoRecorderModule = NativeModules.VideoRecorder;

var subscription;
export default class index {

    static navToVideoRecorder(){
        VideoRecorderModule.navToVideoRecorder();
    }
    static onVideoSave(cb){
        subscription = NativeAppEventEmitter.addListener(
            'onVideoSave',
            (reminder) => cb(reminder.path)
        );
    }
    static removeRecordListener(){
        subscription.remove();
    }

    /**
     * ios
     * @param flag:bool
     */
    static hideLoading(flag){
        VideoRecorderModule.hideLoading(flag);
    }

    /**
     * ios
     * @param cb
     */
    static onVideoRecordEnd(cb){
        subscription = NativeAppEventEmitter.addListener(
            'onVideoRecordEnd',
            (reminder) => cb(reminder.path)
        );
    }




}