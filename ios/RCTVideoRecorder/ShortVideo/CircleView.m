//
//  CircleView.m
//  ShortVideoDemo
//
//  Created by 刘子文 on 16/11/28.
//  Copyright © 2016年 yuanyinguoji. All rights reserved.
//

#import "CircleView.h"


#define ValueTime 60

@interface CircleView ()
{
    NSTimer *_timer;
    CGFloat _num;
}
@property (nonatomic, strong) CAShapeLayer *shapeLayer;

@end

@implementation CircleView

- (instancetype)initWithFrame:(CGRect)frame{
    if (self = [super initWithFrame:frame]) {
        self.noSliderFillColor = [UIColor colorWithRed:129.0/255.0 green:137.0/255.0 blue:155.0/255.0 alpha:1];
        self.sliderCircleColor = [UIColor colorWithRed:250.0/255.0 green:190.0/255.0 blue:1.0/255.0 alpha:1];
        self.minimumValue = 0;
        self.maximumValue = 60;
        self.sliderWidth = 6;
        self.value = 0;
        _num = 0;
    }
    return self;
}

- (void)drawRect:(CGRect)rect{
    
    self.layer.cornerRadius = CGRectGetWidth(self.frame)/2.0;
    self.layer.masksToBounds = YES;
    CGAffineTransform transform = CGAffineTransformIdentity;
    self.transform = CGAffineTransformRotate(transform, -M_PI / 2);
    self.shapeLayer = [CAShapeLayer layer];
    self.shapeLayer.frame = CGRectMake(0, 0, CGRectGetWidth(self.frame)-self.sliderWidth, CGRectGetHeight(self.frame)-self.sliderWidth);//设置shapeLayer的尺寸和位置
    _shapeLayer.lineCap = kCALineCapRound;
    self.shapeLayer.fillColor = [UIColor clearColor].CGColor;
    
    //设置线条的宽度和颜色
    self.shapeLayer.lineWidth = self.sliderWidth;
    self.shapeLayer.strokeColor = self.sliderCircleColor.CGColor;
    
    CAShapeLayer *layer = [CAShapeLayer layer];
    layer.frame = CGRectMake(0, 0, CGRectGetWidth(self.frame), CGRectGetHeight(self.frame));
    layer.fillColor = [UIColor clearColor].CGColor;//[UIColor colorWithRed:38.0/255.0 green:42.0/255.0 blue:43.0/255.0 alpha:1].CGColor;//填充颜色为ClearColor
    layer.lineWidth = self.sliderWidth;
    layer.strokeColor = self.noSliderFillColor.CGColor;
    UIBezierPath *pragress1 = [UIBezierPath bezierPathWithOvalInRect:CGRectMake(self.sliderWidth/2.0, self.sliderWidth/2.0, CGRectGetWidth(self.frame)- self.sliderWidth, CGRectGetHeight(self.frame)-self.sliderWidth)];
    layer.path = pragress1.CGPath;
    layer.strokeStart = 0;
    layer.strokeEnd = 1;

    UIBezierPath *pragress = [UIBezierPath bezierPathWithOvalInRect:CGRectMake(self.sliderWidth/2.0, self.sliderWidth/2.0, CGRectGetWidth(self.frame)- self.sliderWidth, CGRectGetHeight(self.frame)- self.sliderWidth)];
    pragress.lineCapStyle = kCGLineCapRound;
    pragress.lineJoinStyle = kCGLineCapRound;
    self.shapeLayer.path = pragress.CGPath;
    self.shapeLayer.strokeStart = 0;
    self.shapeLayer.strokeEnd = 0;
    [self.layer addSublayer:layer];
    [self.layer addSublayer:self.shapeLayer];
}

- (void)startAnimation{
    self.shapeLayer.strokeEnd = 0;
    self.value = 0;
    _timer = [NSTimer scheduledTimerWithTimeInterval:0.1 target:self selector:@selector(changeValueWithSlider) userInfo:nil repeats:YES];
}

- (void)changeValueWithSlider{
    self.shapeLayer.strokeEnd = (self.value)/(self.maximumValue -self.minimumValue);
    self.value += 0.1;
    _num += 0.1;
    if (_num >ValueTime || self.value > self.maximumValue) {
        [_timer invalidate];
        _timer = nil;
        self.value =0;
        _num = 0;
        [self.delegate timerEndAction];
    }
}

- (void)stopAnimation{
    
    [_timer invalidate];
    _timer = nil;
}

- (void)clearData{
    self.shapeLayer.strokeStart = 0;
    self.shapeLayer.strokeEnd = 0;
}

- (void)removeFromSuperview{
    [_timer invalidate];
    _timer = nil;
}




@end
