# react-native-video-recorder
`to be continued`

A module for react-native to record video by system camera ,  will return a local path ends with '.mp4' when the end of the recording ; Also support exchange camera ；

## USAGE

        npm install --save react-native-video-recorder
        rnpm link react-native-video-recorder

## SUPORT

Both IOS and android.

## METHODS

* navToVideoRecoder()

    Push a native page in react-native route and ready to record video .
    
* onVideoRecordEnd(path=>{//TODO})

    Return a *path* value of the video local path.
    
* onVideoSave(path=>{//TODO})

    When the save button clicked,call this method.And a path string will return;
    
* removeRecordListener()

    Add this method in your *componentWillUnmount* if you called one of *onVideoRecordEnd* and *onVideoSave* .
    
* hideLoading(bool)
    
    Hide or not the loading toast judge by your bool value.Call this method when your video save or upload successfully.
   
