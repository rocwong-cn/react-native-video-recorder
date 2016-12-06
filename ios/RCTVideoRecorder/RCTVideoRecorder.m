//
//  RCTVideoRecorder.m
//  RCTVideoRecorder
//
//  Created by RocWang on 2016/12/6.
//  Copyright © 2016年 RocWang. All rights reserved.
//

#import "RCTVideoRecorder.h"
#import "RCTLog.h"
#import "ShortVideoViewController.h"
#import "RCTBridge.h"
#import "RCTEventDispatcher.h"


@interface RCTVideoRecorder ()<ShortVideoDelegate>
{
    ShortVideoViewController *_viewController;
}

@end

@implementation RCTVideoRecorder


@synthesize bridge = _bridge;

RCT_EXPORT_MODULE();

//跳转到视频录制页面
RCT_EXPORT_METHOD(navToVideoRecorder)
{
    RCTLog(@">>>>>  start recording video  >>>>>");
    UIViewController *root = [UIApplication sharedApplication].keyWindow.rootViewController;
    _viewController = [[ShortVideoViewController alloc] init];
    _viewController.delegate = self;

    UINavigationController *nav = [[UINavigationController alloc] initWithRootViewController:(_viewController)];
    [root presentViewController:nav animated:YES completion:nil];
}

//保存按钮的点击事件
-(void) saveVideoWithPath:(NSString *)path
{
    [self.bridge.eventDispatcher sendAppEventWithName:@"onVideoSave" body:@{@"path": path}];
}

//保存提示的隐藏事件
RCT_EXPORT_METHOD(hideLoading:(BOOL) flag)
{
    _viewController.clickAction(flag);
}

#pragma mark ---- ShortVideoDelegate
// 相机调用开始
- (void)camereStart{

}
// 开始录制小视屏
- (void)startRecordingVideo{
}

/**
 小视频停止录制方法
 @return 小视屏地址
 */
- (void)stopRecordedVideoWithUrl:(NSString *)urlString{
    [self.bridge.eventDispatcher sendAppEventWithName:@"onVideoRecordEnd" body:@{@"path": urlString}];
}


@end
