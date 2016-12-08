//
//  ShortVideoViewController.m
//  ShortVideoDemo
//
//  Created by 刘子文 on 16/11/28.
//  Copyright © 2016年 yuanyinguoji. All rights reserved.
//

#import "ShortVideoViewController.h"
#import "PBJVision.h"
#import "CircleView.h"
#import "PBJVisionUtilities.h"
#import <AVFoundation/AVFoundation.h>
#import <GLKit/GLKit.h>
#import <ImageIO/ImageIO.h>
#import <Photos/Photos.h>


static NSString * const PBJViewControllerPhotoAlbum = @"PBJVision";

@interface ShortVideoViewController ()<PBJVisionDelegate, CircleDelegate>
{
    AVCaptureVideoPreviewLayer *_previewLayer;
    GLKViewController *_effectsViewController;
    
    __block NSDictionary *_currentVideo;   // 录制完后  视屏信息存储信息
    CircleView *_circleView;     // 视屏录制按钮
}
@property (nonatomic, strong) UIView *videoView; // 视频录制层面
@property (nonatomic, strong) UIButton *changeCameraBtn;

@property (nonatomic, strong) UIView *playView;
@property (nonatomic, strong) AVPlayer *player;
@property (nonatomic, strong) AVPlayerItem *playerItem;
@property (nonatomic, strong) UIButton *deleteBtn;
@property (nonatomic, strong) UIButton *saveBtn;
@property (nonatomic, strong) UIImageView *isPlayImage;
@property (nonatomic, assign) BOOL isPlay; // 判断播放还是暂停
@property (nonatomic, assign) BOOL isShow;  //时否展示提示框；
@property (nonatomic, strong) UIView *messageView;
@property(nonatomic,strong)UIButton *contentView;
@property(nonatomic,strong)UILabel *messageLab;


@end

@implementation ShortVideoViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.isShow  = NO;
    [self addRecordView];
    self.title = @"短视频";
    self.view.backgroundColor = [UIColor blackColor];
    self.navigationController.navigationBar.backgroundColor = [UIColor blackColor];
    self.navigationController.navigationBar.titleTextAttributes = @{NSForegroundColorAttributeName: [UIColor whiteColor]};
    self.navigationController.navigationBar.barTintColor = [UIColor blackColor];
    [[UIApplication sharedApplication] setStatusBarStyle:UIStatusBarStyleLightContent animated:YES];
    self.navigationItem.leftBarButtonItem = [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed: @"backImage"] style:UIBarButtonItemStylePlain target:self action:@selector(backAction)];
    self.navigationController.navigationBar.tintColor =[UIColor whiteColor];
}
- (void)backAction{
    [self dismissViewControllerAnimated:YES completion:nil];
}
- (void)playVideoView{
    
    self.playView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, [UIScreen mainScreen].bounds.size.width, [UIScreen mainScreen].bounds.size.height)];
    _playView.backgroundColor = [UIColor blackColor];
    [self.view addSubview:_playView];
    
    [self playerAction];
    UIImageView *imageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, 80, 80)];
    imageView.image = [UIImage imageNamed:@"videoStop"];
    imageView.center = self.view.center;
    imageView.userInteractionEnabled = YES;
    [self.playView addSubview:imageView];
    _isPlayImage = imageView;
    
    UIButton *deleteBtn = [UIButton buttonWithType:(UIButtonTypeCustom)];
    self.deleteBtn = deleteBtn;
    deleteBtn.frame = CGRectMake(CGRectGetWidth(self.view.frame)/3.0 - 16, CGRectGetHeight(self.view.frame) - 115, 32, 35);
    [deleteBtn setImage:[UIImage imageNamed:@"deleteVideo"] forState:UIControlStateNormal];
    [deleteBtn addTarget:self action:@selector(isDeleteVideoAction) forControlEvents:UIControlEventTouchUpInside];
    [self.playView addSubview:deleteBtn];
    
    
    UIButton *saveBtn = [UIButton buttonWithType:(UIButtonTypeCustom)];
    self.saveBtn = saveBtn;
    saveBtn.frame = CGRectMake(CGRectGetWidth(self.view.frame)*2/3.0 - 19, CGRectGetHeight(self.view.frame) - 118, 38, 41);
    [saveBtn setImage:[UIImage imageNamed:@"saveVideo"] forState:UIControlStateNormal];
    [saveBtn addTarget:self action:@selector(saveVideo) forControlEvents:UIControlEventTouchUpInside];
    [self.playView addSubview:saveBtn];


    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(startPlay)];
    [self.playView addGestureRecognizer:tap];
    [_circleView clearData];
}

// 播放器初始化
- (void)playerAction{
    
    NSString *videoPath = [_currentVideo  objectForKey:PBJVisionVideoPathKey];
    [self.delegate stopRecordedVideoWithUrl:videoPath];
    //    [self.videoView removeFromSuperview];
    NSURL *url = [NSURL fileURLWithPath:videoPath];
    AVPlayerItem *playerItem=[AVPlayerItem playerItemWithURL:url];
    self.playerItem = playerItem;
    self.player = [AVPlayer playerWithPlayerItem:playerItem];
    //设置播放页面
    AVPlayerLayer *layer = [AVPlayerLayer playerLayerWithPlayer:_player];
    layer.frame = CGRectMake(0, 0, [UIScreen mainScreen].bounds.size.width, [UIScreen mainScreen].bounds.size.height);
    layer.backgroundColor = [UIColor blackColor].CGColor;
    layer.videoGravity = AVLayerVideoGravityResizeAspectFill;
    //添加播放视图到self.view
    [self.playView.layer addSublayer:layer];
    //设置播放的默认音量值
    self.player.volume = 1.0f;
    self.isPlay = NO;
    // 监听视屏播放结束
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(playEndAction) name:AVPlayerItemDidPlayToEndTimeNotification object:playerItem];
}

#pragma mark --- AVplayer
- (void)startPlay{
    if (_isPlay == NO) {
        _isPlay = YES;
        [self.player play];
        _isPlayImage.hidden = YES;
    }else{
        _isPlay = NO;
        _isPlayImage.hidden = NO;
        _isPlayImage.image = [UIImage imageNamed:@"Videopause"];
        [self.player pause];
    }
}
// 视屏播放结束
- (void)playEndAction{
    _isPlayImage.hidden = NO;
    _isPlayImage.image = [UIImage imageNamed:@"videoStop"];
    [self removePlayAction];
    [self playVideoView];
}

- (void)removePlayAction{
    [[NSNotificationCenter defaultCenter] removeObserver:self name:AVPlayerItemDidPlayToEndTimeNotification object:self.playerItem];
    self.playerItem = nil;
    self.player = nil;
    [self.playView removeFromSuperview];
    self.playView = nil;
}

- (void)addRecordView{
    self.videoView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, CGRectGetWidth(self.view.frame), CGRectGetHeight(self.view.frame))];
    [self.view addSubview:_videoView];
    
    _previewLayer = [[PBJVision sharedInstance] previewLayer];
    _previewLayer.frame = _videoView.bounds;
    _previewLayer.videoGravity = AVLayerVideoGravityResizeAspectFill;
    [_videoView.layer addSublayer:_previewLayer];
    
        _effectsViewController = [[GLKViewController alloc] init];
        _effectsViewController.preferredFramesPerSecond = 60;
        GLKView *view = (GLKView *)_effectsViewController.view;
        CGRect viewFrame = _videoView.bounds;
        view.frame = viewFrame;
        view.context = [[PBJVision sharedInstance] context];
        view.contentScaleFactor = [[UIScreen mainScreen] scale];
        view.alpha = 0.5f;
        view.hidden = YES;
        [[PBJVision sharedInstance] setPresentationFrame:_videoView.frame];
        [_videoView addSubview:_effectsViewController.view];
    
    
    if ([[PBJVision sharedInstance] supportsVideoFrameRate:120]) {
    }
    [self resetCapture];
    [[PBJVision sharedInstance] startPreview];
    
}

- (void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];
    
    [self addCircleView];
    self.changeCameraBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    self.changeCameraBtn.frame = CGRectMake(CGRectGetWidth(self.view.frame) * 0.85, CGRectGetHeight(self.view.frame) * 0.83, 40, 40);
    [_changeCameraBtn addTarget:self action:@selector(changeCameraAction:) forControlEvents:UIControlEventTouchUpInside];
    [_changeCameraBtn setImage:[UIImage imageNamed:@"exchange"] forState:UIControlStateNormal];
    [self.view addSubview:_changeCameraBtn];
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [[PBJVision sharedInstance] stopPreview];
    [self messageLabClear];

}

// 圆圈动画
- (void)addCircleView{
    _circleView = [[CircleView alloc] initWithFrame:CGRectMake( (CGRectGetWidth(self.view.frame)- 100)/2.0, CGRectGetHeight(self.view.frame) - 100 -50, 100, 100)];
    _circleView.delegate = self;
       //    circleView.noSliderFillColor = [UIColor orangeColor];
    _circleView.backgroundColor = [UIColor clearColor];
    [self.view addSubview:_circleView];
    
    UILongPressGestureRecognizer *longPress = [[UILongPressGestureRecognizer alloc] initWithTarget:self action:@selector(longPressAction:)];
    longPress.minimumPressDuration = 0.05f;
    longPress.allowableMovement = 10.0f;
    [_circleView addGestureRecognizer:longPress];
    
}

- (void)longPressAction:(UILongPressGestureRecognizer *)gestureRecognizer{
    switch (gestureRecognizer.state) {
        case UIGestureRecognizerStateBegan:
        {
            [self startVideo:nil];
            break;
        }
        case UIGestureRecognizerStateEnded:
        case UIGestureRecognizerStateCancelled:
        case UIGestureRecognizerStateFailed:
        {
            [self stopVideo:nil];
            break;
        }
        default:
            break;
    }
    
    
}
#pragma mark --- CircleDelegate
- (void)timerEndAction{
    [self stopVideo:nil];
}

- (void)saveVideo{
    NSString *videoPath = [_currentVideo  objectForKey:PBJVisionVideoPathKey];
    [self.delegate saveVideoWithPath:videoPath];
    [self showViewWithMesssage:@"上传中..."];
}
- (void)showViewWithMesssage:(NSString *)text{
    UIFont *font = [UIFont boldSystemFontOfSize:16];
    NSDictionary * dict=[NSDictionary dictionaryWithObject: font forKey:NSFontAttributeName];
    CGRect rect=[text boundingRectWithSize:CGSizeMake(250,CGFLOAT_MAX) options:NSStringDrawingTruncatesLastVisibleLine|NSStringDrawingUsesFontLeading|NSStringDrawingUsesLineFragmentOrigin attributes:dict context:nil];
    UILabel *textLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, 0,rect.size.width + 40, rect.size.height+ 20)];
    [_messageView removeFromSuperview];
    _messageView = nil;
    _messageView = [[UIView alloc] initWithFrame:CGRectMake(0, 64, CGRectGetWidth(self.view.bounds), CGRectGetHeight([UIScreen mainScreen].bounds) - 64)];
    _messageLab = textLabel;
    textLabel.layer.masksToBounds = YES;
    textLabel.layer.cornerRadius = 8;
    textLabel.center = CGPointMake(self.view.center.x, CGRectGetHeight(self.view.bounds)/3.0);
    textLabel.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.75];
    textLabel.textColor = [UIColor whiteColor];
    textLabel.textAlignment = NSTextAlignmentCenter;
    textLabel.font = font;
    textLabel.text = text;
    textLabel.numberOfLines = 0;
    [_messageView addSubview:textLabel];
    [[UIApplication sharedApplication].keyWindow addSubview:_messageView];
    self.isShow = YES;
    __weak typeof (self)temp = self;
    self.clickAction = ^(BOOL is){
        if (is == YES) {
            _messageLab.text = @"";
            [temp.messageView removeFromSuperview];
            temp.messageView = nil;
            [temp clearVideo];
            NSLog(@"succcess video");
            [temp dismissViewControllerAnimated:YES completion:nil];
        }else{
            NSLog(@"fail video");
            [_messageView removeFromSuperview];
            [temp saveVideoFailAction];
        }
    };
}

- (void)saveVideoFailAction{
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:nil message:@"上传视屏失败，是否重新上传" preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *action1 = [UIAlertAction actionWithTitle:@"是" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
            NSLog(@"继续上传");
        [self saveVideo];

    }];
    UIAlertAction *action2 = [UIAlertAction actionWithTitle:@"否" style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
        NSLog(@"取消上传");
    }];
    [alert addAction:action2];
    [alert addAction:action1];
    [self presentViewController:alert animated:YES completion:^{

    }];
}
- (void)messageLabClear{
    [_messageView removeFromSuperview];
    _messageView = nil;
}

- (void)clearPromptView{
    if (self.isShow == YES) {
        [self messageLabClear];
        self.isShow = NO;
    }
}


- (void)isDeleteVideoAction{
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:nil message:@"您是否要删除当前录制的短视频，删除之后不可恢复。" preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *action1 = [UIAlertAction actionWithTitle:@"确定" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        if ([self clearVideo]) {
            NSLog(@"删除成功");
            [self showViewWithMesssage:@"删除成功"];
            [self performSelector:@selector(clearPromptView) withObject:nil afterDelay:1.2];
            [self removePlayAction];
            
        }else{
            NSLog(@"删除失败");
            [self showViewWithMesssage:@"删除失败"];
            [self performSelector:@selector(clearPromptView) withObject:nil afterDelay:1.2];

        }
    }];
    UIAlertAction *action2 = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
        
    }];
    [alert addAction:action2];
    [alert addAction:action1];
    [self presentViewController:alert animated:YES completion:^{
        
    }];
}
// 清除沙盒中的视屏
- (BOOL)clearVideo{
    NSString *videoPath = [_currentVideo  objectForKey:PBJVisionVideoPathKey];
    NSFileManager *mgr = [NSFileManager defaultManager];
    return [mgr removeItemAtPath:videoPath error:nil];
}



- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)resetCapture{
    PBJVision *vision = [PBJVision sharedInstance];
    vision.delegate = self;
    
    if ([vision isCameraDeviceAvailable:PBJCameraDeviceBack]) {
        vision.cameraDevice = PBJCameraDeviceBack;
    } else {
        vision.cameraDevice = PBJCameraDeviceFront;
    }
    
    vision.cameraMode = PBJCameraModeVideo;
    //vision.cameraMode = PBJCameraModePhoto; // PHOTO: uncomment to test photo capture
    vision.cameraOrientation = PBJCameraOrientationPortrait;
    vision.focusMode = PBJFocusModeContinuousAutoFocus;
    vision.outputFormat = PBJOutputFormatSquare;
    vision.videoRenderingEnabled = YES;
    vision.additionalCompressionProperties = @{AVVideoProfileLevelKey : AVVideoProfileLevelH264Baseline30};
    [self.delegate camereStart];
    
}

#pragma mark ---- 交换摄像头
- (void)changeCameraAction:(UIButton *)sender{
    PBJVision *vision = [PBJVision sharedInstance];
    vision.cameraDevice = vision.cameraDevice == PBJCameraDeviceBack ? PBJCameraDeviceFront : PBJCameraDeviceBack;
}

- (void)startVideo:(UIButton *)sender{
    [_circleView startAnimation];
    [UIApplication sharedApplication].idleTimerDisabled = YES;
    [[PBJVision sharedInstance] startVideoCapture];
    [self.delegate startRecordingVideo];
}
- (void)stopVideo:(UIButton *)sender{
    [_circleView stopAnimation];
    [UIApplication sharedApplication].idleTimerDisabled = NO;
    [[PBJVision sharedInstance] resumeVideoCapture];
    [[PBJVision sharedInstance] endVideoCapture];
}

- (void)pauseVideo{
    [[PBJVision sharedInstance] pauseVideoCapture];
}

#pragma mark - PBJVisionDelegate

// session

- (void)visionSessionWillStart:(PBJVision *)vision
{
}

- (void)visionSessionDidStart:(PBJVision *)vision
{
}

- (void)visionSessionDidStop:(PBJVision *)vision
{
}

// preview

- (void)visionSessionDidStartPreview:(PBJVision *)vision
{
    NSLog(@"Camera preview did start");
    
}

- (void)visionSessionDidStopPreview:(PBJVision *)vision
{
    NSLog(@"Camera preview did stop");
}

// device

- (void)visionCameraDeviceWillChange:(PBJVision *)vision
{
    NSLog(@"Camera device will change");
}

- (void)visionCameraDeviceDidChange:(PBJVision *)vision
{
    NSLog(@"Camera device did change");
}
- (void)visionCameraModeWillChange:(PBJVision *)vision
{
    NSLog(@"Camera mode will change");
}

- (void)visionCameraModeDidChange:(PBJVision *)vision
{
}
- (void)visionOutputFormatWillChange:(PBJVision *)vision
{
    NSLog(@"Output format will change");
}

- (void)visionOutputFormatDidChange:(PBJVision *)vision
{
    NSLog(@"Output format did change");
}

- (void)vision:(PBJVision *)vision didChangeCleanAperture:(CGRect)cleanAperture
{
}

// focus / exposure

- (void)visionWillStartFocus:(PBJVision *)vision
{
}

- (void)visionDidStopFocus:(PBJVision *)vision
{
}

- (void)visionWillChangeExposure:(PBJVision *)vision
{
}

- (void)visionDidChangeExposure:(PBJVision *)vision
{
}

// flash

- (void)visionDidChangeFlashMode:(PBJVision *)vision
{
    NSLog(@"Flash mode did change");
}

// photo

- (void)visionWillCapturePhoto:(PBJVision *)vision
{
}

- (void)visionDidCapturePhoto:(PBJVision *)vision
{
}

- (void)vision:(PBJVision *)vision capturedPhoto:(NSDictionary *)photoDict error:(NSError *)error
{
    if (error) {
        // handle error properly
        return;
    }
}

#pragma mark --- 视屏开始捕捉
- (void)visionDidStartVideoCapture:(PBJVision *)vision
{
    //    [_strobeView start];
    //    _recording = YES;
}
#pragma mark --- 视屏暂停
- (void)visionDidPauseVideoCapture:(PBJVision *)vision
{
    //    [_strobeView stop];
}
#pragma mark --- 视屏从新开始
- (void)visionDidResumeVideoCapture:(PBJVision *)vision
{
    //    [_strobeView start];
}
#pragma mark -- 停止播放后回调视屏
- (void)vision:(PBJVision *)vision capturedVideo:(NSDictionary *)videoDict error:(NSError *)error
{
    //    _recording = NO;
    //
    //    if (error && [error.domain isEqual:PBJVisionErrorDomain] && error.code == PBJVisionErrorCancelled) {
    //        NSLog(@"recording session cancelled");
    //        return;
    //    } else if (error) {
    //        NSLog(@"encounted an error in video capture (%@)", error);
    //        return;
    //    }
    //
    //    _currentVideo = videoDict;
    //    [[PHPhotoLibrary sharedPhotoLibrary] performChanges:^{
    //        NSString *videoPath = [_currentVideo  objectForKey:PBJVisionVideoPathKey];
    //        [PHAssetChangeRequest creationRequestForAssetFromVideoAtFileURL:[NSURL URLWithString:videoPath]];
    //    } completionHandler:^(BOOL success, NSError * _Nullable error1) {
    //        if (success) {
    //            dispatch_async(dispatch_get_main_queue(), ^{
    //                UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"Video Saved!" message:@"Saved to the camera roll." preferredStyle:UIAlertControllerStyleAlert];
    //                UIAlertAction *ok = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:nil];
    //                [alertController addAction:ok];
    //                [self presentViewController:alertController animated:YES completion:nil];
    //            });
    //        } else if (error1) {
    //            NSLog(@"error: %@", error1);
    //        }
    //    }];
    
    _currentVideo = videoDict;
    [self playVideoView];  // 视屏层
}



#pragma mark ---- 录制中走的事件
- (void)vision:(PBJVision *)vision didCaptureVideoSampleBuffer:(CMSampleBufferRef)sampleBuffer
{
}

- (void)vision:(PBJVision *)vision didCaptureAudioSample:(CMSampleBufferRef)sampleBuffer
{
    
    
}


/*
 #pragma mark - Navigation
 
 // In a storyboard-based application, you will often want to do a little preparation before navigation
 - (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
 // Get the new view controller using [segue destinationViewController].
 // Pass the selected object to the new view controller.
 }
 */

@end
