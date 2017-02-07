package com.yuanyinguoji.video.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.yuanyinguoji.video.utils.ScreenUtils;

/**
 * Created by wa on 2016/11/21.
 */

public class CircleBar extends View {
    private Context mContext;

    private Paint mUnTouchPaint;//灰色背景画笔
    private Paint mOnTouchPaint;//黄色进度条画笔

    private int unTouchColor = 0xff81899b;//没有点击时候的颜色,默认为灰色
    private int onTounhColor = 0xfffabe01;//点击以后进度条颜色，默认为黄色

    private float defWidth;//默认的宽度，高度与宽度相同
    private float radius;//页面半径
    private float cx = 0;//圆心x坐标
    private float cy = 0;//圆心y坐标


    private long mTime = 60 * 1000;//时间进度条时间,默认60秒
    private long refreshTime = 100;//页面刷新频率，单位毫秒
    private long mCurrTime = 0;//当前进行了多长时间，单位毫秒
    private TimeCount mTimeCount;//计时器，动态刷新页面

    private RectF reactF;//进度条的大小

    private boolean isStart = false;

    private OnTouchListener mTouchListener;

    public CircleBar(Context context) {
        super(context);
        mContext = context;
        init(null);
    }

    public CircleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        defWidth = ScreenUtils.getScreenWidth(mContext) / 5;
        radius = defWidth / 2;//直径是屏幕宽度的6分之一
        isStart = false;
        initLocation();
        initPaint();
        mTimeCount = new TimeCount(mTime, 100);//初始化定时器
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension((int) defWidth + 20, (int) defWidth + 20);//初始化控件大小，现在为固定值
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    private void initPaint() {
        // mUnTouchPaint初始化
        mUnTouchPaint = new Paint();
        mUnTouchPaint.setAntiAlias(true);                       //设置画笔为无锯齿
        mUnTouchPaint.setColor(unTouchColor);                    //设置画笔颜色
        mUnTouchPaint.setStrokeWidth((float) 15.0);              //线宽
        mUnTouchPaint.setStyle(Paint.Style.STROKE);                   //空心效果
        // mOnTouchPaint初始化
        mOnTouchPaint = new Paint();
        mOnTouchPaint.setAntiAlias(true);                       //设置画笔为无锯齿
        mOnTouchPaint.setColor(onTounhColor);                    //设置画笔颜色
        mOnTouchPaint.setStrokeWidth((float) 15.0);              //线宽
        mOnTouchPaint.setStyle(Paint.Style.STROKE);                   //空心效果
        //初始化进度条
        reactF = new RectF(cx - radius, cy - radius, cx + radius, cy + radius);
    }

    //初始化位置，固定在屏幕下方中间位置
    private void initLocation() {
        cx = radius + 10;
        cy = radius + 10;
    }

    /**
     * 进度条时间
     *
     * @param time 持续时间，不添默认为60秒
     */
    public void setTime(long time) {
        mTime = time;
        if (mTimeCount != null) {
            mTimeCount.cancel();
        }
        mTimeCount = new TimeCount(mTime, 100);
        mCurrTime = 0;
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(cx, cy, radius, mUnTouchPaint);//绘制背景灰色圆
        float sweepAngle = 360 * mCurrTime / mTime;//进度条角度
        canvas.drawArc(reactF, 0, sweepAngle, false, mOnTouchPaint);//绘制进度条
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("TAG", "ACTION_DOWN");
                cancelTimer();
                mTimeCount.start();
                if (mTouchListener != null) {
                    mTouchListener.onStart();
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.e("TAG", "ACTION_UP");
                cancelTimer();
                if (mTouchListener != null) {
                    mTouchListener.onStop();
                }
//                if(!isStart){
//                    cancelTimer();
//                    mTimeCount.start();
//                    if(mTouchListener!=null){
//                        mTouchListener.onStart();
//                    }
//                    isStart=true;
//                }else{
//                    cancelTimer();
//                    if(mTouchListener!=null){
//                        mTouchListener.onStop();
//                    }
//                }
                break;
        }
        return true;
    }

    //取消定时器，并初始化部分数据
    private void cancelTimer() {
        mTimeCount.cancel();
        mCurrTime = 0;
        postInvalidate();
    }


    /**
     * 点击监听
     */
    public interface OnTouchListener {
        void onStart();

        void onStop();
    }

    /**
     * 设置按钮监听
     *
     * @param touchListener
     */
    public void setOnViewTouchListener(OnTouchListener touchListener) {
        mTouchListener = touchListener;
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            cancelTimer();
            isStart = false;
            if (mTouchListener != null) {
                mTouchListener.onStop();
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            postInvalidate();
            mCurrTime += refreshTime;//增加页面角度
        }
    }
}
