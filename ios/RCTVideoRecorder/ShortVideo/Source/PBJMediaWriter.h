//
//  ShortVideoDemo
//
//  Created by 刘子文 on 16/11/28.
//  Copyright © 2016年 yuanyinguoji. All rights reserved.
//



#import <Foundation/Foundation.h>
#import <AVFoundation/AVFoundation.h>

@protocol PBJMediaWriterDelegate;
@interface PBJMediaWriter : NSObject

- (id)initWithOutputURL:(NSURL *)outputURL;

@property (nonatomic, weak) id<PBJMediaWriterDelegate> delegate;

@property (nonatomic, readonly) NSURL *outputURL;
@property (nonatomic, readonly) NSError *error;

// configure settings before writing

@property (nonatomic, readonly, getter=isAudioReady) BOOL audioReady;
@property (nonatomic, readonly, getter=isVideoReady) BOOL videoReady;

- (BOOL)setupAudioWithSettings:(NSDictionary *)audioSettings;

- (BOOL)setupVideoWithSettings:(NSDictionary *)videoSettings withAdditional:(NSDictionary *)additional;

// write methods, time durations

@property (nonatomic, readonly) CMTime audioTimestamp;
@property (nonatomic, readonly) CMTime videoTimestamp;

- (void)writeSampleBuffer:(CMSampleBufferRef)sampleBuffer withMediaTypeVideo:(BOOL)video;
- (void)finishWritingWithCompletionHandler:(void (^)(void))handler;

@end

@protocol PBJMediaWriterDelegate <NSObject>
@optional
// authorization status provides the opportunity to prompt the user for allowing capture device access
- (void)mediaWriterDidObserveAudioAuthorizationStatusDenied:(PBJMediaWriter *)mediaWriter;
- (void)mediaWriterDidObserveVideoAuthorizationStatusDenied:(PBJMediaWriter *)mediaWriter;

@end
