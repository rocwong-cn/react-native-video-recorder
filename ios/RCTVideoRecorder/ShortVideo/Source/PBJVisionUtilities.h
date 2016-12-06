//
//  ShortVideoDemo
//
//  Created by 刘子文 on 16/11/28.
//  Copyright © 2016年 yuanyinguoji. All rights reserved.
//



#import <Foundation/Foundation.h>
#import <AVFoundation/AVFoundation.h>

@interface PBJVisionUtilities : NSObject

// devices and connections

+ (AVCaptureDevice *)captureDeviceForPosition:(AVCaptureDevicePosition)position;
+ (AVCaptureDevice *)audioDevice;
+ (AVCaptureConnection *)connectionWithMediaType:(NSString *)mediaType fromConnections:(NSArray *)connections;

// sample buffers

+ (CMSampleBufferRef)createOffsetSampleBufferWithSampleBuffer:(CMSampleBufferRef)sampleBuffer withTimeOffset:(CMTime)timeOffset;

// orientation

+ (CGFloat)angleOffsetFromPortraitOrientationToOrientation:(AVCaptureVideoOrientation)orientation;

// storage

+ (uint64_t)availableStorageSpaceInBytes;

@end

@interface NSString (PBJExtras)

+ (NSString *)PBJformattedTimestampStringFromDate:(NSDate *)date;

@end
