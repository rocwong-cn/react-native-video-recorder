//
//  CircleView.h
//  ShortVideoDemo
//
//  Created by 刘子文 on 16/11/28.
//  Copyright © 2016年 yuanyinguoji. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol CircleDelegate <NSObject>
// 定时器几时结束的回调事件
- (void)timerEndAction;

@end

@interface CircleView : UIView

@property (nonatomic, strong)UIColor *sliderCircleColor;
@property (nonatomic, strong)UIColor *noSliderFillColor;

// 最小值   默认是 0
@property (nonatomic, assign)CGFloat minimumValue;
// 最大值   默认是 60
@property (nonatomic, assign)CGFloat maximumValue;
// 默认值   默认为0
@property (nonatomic, assign)CGFloat value;

// 滑动条的宽度  默认是  6
@property (nonatomic, assign)CGFloat sliderWidth;

@property (nonatomic, weak)id<CircleDelegate>delegate;

- (void)startAnimation;
- (void)stopAnimation;
- (void)clearData;

@end
