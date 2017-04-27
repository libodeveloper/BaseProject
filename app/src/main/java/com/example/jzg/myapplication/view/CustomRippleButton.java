package com.example.jzg.myapplication.view;/**
 * Created by voiceofnet on 2016/6/21.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;


import com.example.jzg.myapplication.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author: voiceofnet
 * email: pengkun@jingzhengu.com
 * phone:18101032717
 * @time: 2016/6/21 19:59
 * @desc:
 */
public class CustomRippleButton extends Button {

    private int mWidth, mHeight;
    private int mUnPressedColor;
    private int mRoundRadius, mBtnRadius;
    private int mShapeType;
    private int mRippleColor;
    private int mRippleDuration;
    private int mRippleRadius;
    private float pointX, pointY;
    private Paint textPaint;

    private Paint mPaint, mRipplePaint;
    private RectF mRectF;
    private Path mPath;
    private Timer mTimer;
    private TimerTask mTask;
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_DRAW_COMPLETE){
                invalidate();
            }
        }
    };


    private int mRippleAlpha;
    private final static int RIPPLR_ALPHE = 47;
    private final static int MSG_DRAW_COMPLETE = 101;

    public CustomRippleButton(Context context){
        super(context);
    }

    public CustomRippleButton(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context, attrs);
    }

    public CustomRippleButton(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(final Context context, final AttributeSet attrs){
        if (isInEditMode()){
            return;
        }
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomButton);
        mUnPressedColor = typedArray.getColor(R.styleable.CustomButton_unpressed_color,
                getResources().getColor(R.color.colorPrimary));
        mRippleColor = typedArray.getColor(R.styleable.CustomButton_ripple_color,
                getResources().getColor(R.color.colorPrimaryDark));
        mRippleAlpha = typedArray.getInteger(R.styleable.CustomButton_ripple_alpha,
                RIPPLR_ALPHE);
        mRippleDuration = typedArray.getInteger(R.styleable.CustomButton_ripple_duration,
                1000);
        mShapeType = typedArray.getInt(R.styleable.CustomButton_shape_type, 1);
        mRoundRadius = typedArray.getDimensionPixelSize(R.styleable.CustomButton_round_radius,
                getResources().getDimensionPixelSize(R.dimen.round_radius));
        typedArray.recycle();
        mRipplePaint = new Paint();
        mRipplePaint.setColor(mRippleColor);
        mRipplePaint.setAlpha(mRippleAlpha);
        mRipplePaint.setStyle(Paint.Style.FILL);
        mRipplePaint.setAntiAlias(true);
        mPaint = new Paint();
        mPaint.setColor(mUnPressedColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        this.setWillNotDraw(false);
        this.setDrawingCacheEnabled(true);
        this.setClickable(true);
        //this.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mPath = new Path();
        mRectF = new RectF();
        pointY = pointX = -1;
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mBtnRadius = mWidth / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPaint == null){
            return;
        }
        if (mShapeType == 0){// draw cirle button
            canvas.drawCircle(mWidth / 2, mHeight / 2, mBtnRadius, mPaint);
        }else{// draw rectangle button
            mRectF.set(0, 0, mWidth, mHeight);
            canvas.drawRoundRect(mRectF, mRoundRadius, mRoundRadius, mPaint);
        }
        drawFillCircle(canvas);
        drawText(canvas);
    }

    private void drawText(Canvas canvas){
        mRectF.set(0, 0, mWidth, mHeight);
        String text = getText().toString();
        int textColor = getCurrentTextColor();
        float textSize = getTextSize();
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        float baseline = (mRectF.bottom + mRectF.top - fontMetrics.bottom - fontMetrics.top) / 2;
        textPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, mRectF.centerX(), baseline, textPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            pointX = event.getX();
            pointY = event.getY();
            onStartDrawRipple();
        }
        return super.onTouchEvent(event);
    }

    /** Draw ripple effect*/
    private void drawFillCircle(Canvas canvas){
        if (canvas != null && pointX >= 0 && pointY >= 0){
            int rbX = canvas.getWidth();
            int rbY = canvas.getHeight();
            float longDis = Math.max(pointX, pointY);
            longDis = Math.max(longDis, Math.abs(rbX - pointX));
            longDis = Math.max(longDis, Math.abs(rbY - pointY));
            if (mRippleRadius > longDis) {
                onCompleteDrawRipple();
                return;
            }
            final float drawSpeed = longDis / mRippleDuration * 35;
            mRippleRadius += drawSpeed;

            canvas.save();
//            canvas.translate(0, 0);//保持原点
            mPath.reset();
            canvas.clipPath(mPath);
            if (mShapeType == 0){
                mPath.addCircle(rbX / 2, rbY / 2, mBtnRadius, Path.Direction.CCW);
            }else {
                mPath.addRoundRect(mRectF, mRoundRadius, mRoundRadius, Path.Direction.CCW);
            }
            canvas.clipPath(mPath, Region.Op.REPLACE);
            canvas.drawCircle(pointX, pointY, mRippleRadius, mRipplePaint);
            canvas.restore();
        }
    }

    /** Start draw ripple effect*/
    private void onStartDrawRipple(){
        onCompleteDrawRipple();
        mTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(MSG_DRAW_COMPLETE);
            }
        };
        mTimer = new Timer();
        mTimer.schedule(mTask, 0, 30);
    }

    /** Stop draw ripple effect*/
    private void onCompleteDrawRipple(){

        mHandler.removeMessages(MSG_DRAW_COMPLETE);

        if (mTimer != null){
            if (mTask != null){
                mTask.cancel();
            }
            mTimer.cancel();
            mTask = null;
        }
        mRippleRadius = 0;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        onCompleteDrawRipple();
    }
}
