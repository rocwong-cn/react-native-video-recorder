//
//  ShortVideoViewController.h
//  ShortVideoDemo
//
//  Created by 刘子文 on 16/11/28.
//  Copyright © 2016年 yuanyinguoji. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol ShortVideoDelegate <NSObject>

- (void)camereStart;
- (void)startRecordingVideo;
- (void)stopRecordedVideoWithUrl:(NSString *)urlString;
- (void)saveVideoWithPath:(NSString *)urlString;

@end

@interface ShortVideoViewController : UIViewController

@property (nonatomic, assign)id<ShortVideoDelegate>delegate;
@property(nonatomic,copy)void(^clickAction)(BOOL);

@end
