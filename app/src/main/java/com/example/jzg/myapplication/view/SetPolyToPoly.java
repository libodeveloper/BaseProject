package com.example.jzg.myapplication.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.jzg.myapplication.R;


/**
 * Author: GcsSloop
 * <p>
 * Created Date: 16/8/26
 * <p>
 * Copyright (C) 2016 GcsSloop.
 * <p>
 * GitHub: https://github.com/GcsSloop
 */
public class SetPolyToPoly extends View{
    private static final String TAG = "SetPolyToPoly";

    private int testPoint = 0;
    private int triggerRadius = 180;    // 触发半径为180px

    private Bitmap mBitmap;             // 要绘制的图片
    private Matrix mPolyMatrix;         // 测试setPolyToPoly用的Matrix

    private float[] src = new float[8];
    private float[] dst = new float[8];



    private Paint pointPaint;

    public SetPolyToPoly(Context context) {
        this(context, null);
    }

    public SetPolyToPoly(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SetPolyToPoly(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBitmapAndMatrix();
    }

    private void initBitmapAndMatrix() {
        mBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.car_img);

        float[] temp = {0, 0,                                    // 左上
                mBitmap.getWidth(), 0,                          // 右上
                mBitmap.getWidth(), mBitmap.getHeight(),        // 右下
                0, mBitmap.getHeight()};                        // 左下
        src = temp.clone();
        dst = temp.clone();

        pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setStrokeWidth(50);
        pointPaint.setColor(0xffd19165);
        pointPaint.setStrokeCap(Paint.Cap.ROUND);

        mPolyMatrix = new Matrix();
        mPolyMatrix.setPolyToPoly(src, 0, src, 0, 4);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                float tempX = event.getX();
                float tempY = event.getY();

                // 根据触控位置改变dst
                for (int i=0; i<testPoint*2; i+=2 ) {
                    if (Math.abs(tempX - dst[i]) <= triggerRadius && Math.abs(tempY - dst[i+1]) <= triggerRadius){
                        dst[i]   = tempX-100;
                        dst[i+1] = tempY-100;
                        pointBorder(i);
                        break;  // 防止两个点的位置重合
                    }
                }

                resetPolyMatrix(testPoint);
                invalidate();
                break;
        }

        return true;
    }


    /**
     * Created by 李波 on 2018/1/11.
     * 四个点拖动时的边界限制：包括相邻两个点 和 相邻两个点之间的连接线
     *
     * dst数组里一共4个点，一组 x,y 坐标对应一个点
     * 根据 x 坐标在 dst 数组里的角标位置，自然能定位出是 4个 点中的哪个点
     *
     * @param  index 某一个点的 x 坐标在 dst 数组里的角标位置
     */
    private void pointBorder(int index){

//        float[] temp = {0, 0,                                    // 左上
//                mBitmap.getWidth(), 0,                          // 右上
//                mBitmap.getWidth(), mBitmap.getHeight(),        // 右下
//                0, mBitmap.getHeight()};                        // 左下

        switch (index){
            case 0:    //左上点  边界限制点对应 - 左下点，右上点，以及它们连接线
            // 拿取左下点的 y 坐标值，为 y 轴边界限制
                float leftBottomY = dst[7];

            //拿取右上点的 x 坐标值，为 x 轴边界限制
                float rightUpX = dst[2];

            //计算当前点与相邻两点连线的距离，不能越过连线
                double  distance  = pointToLine(dst[6],dst[7],dst[2],dst[3],dst[index],dst[index+1]);


                if (dst[index]>=rightUpX-50) {
                    Log.i(TAG, "pointBorder: 超过 XX 边界");
                    dst[index] = rightUpX-50;

                }
                if (dst[index+1]>leftBottomY-50) {
                    Log.i(TAG, "pointBorder: 超过 Y 边界");
                    dst[index+1] = leftBottomY-50;
                }


                Log.i(TAG, "pointBorder: 边界距离 = " + distance);
                float tempX=dst[index];
                float tempY=dst[index+1];
                if (distance<200&&distance>100) {
                    tempX = dst[index];
                    tempY = dst[index+1];
                }
//                if (distance<150&&distance>0) {
//                    dst[index] = tempX;
//                    dst[index+1] = tempY;
//                }
                if (distance<100) {
                    dst[index] = tempX;
                    dst[index+1] = tempY;
                }



                break;
            case 2:    //右上点

                break;
            case 4:    //右下点

                break;
            case 6:    //左下点

                break;
        }
    }

    public void resetPolyMatrix(int pointCount){
        mPolyMatrix.reset();
        // 核心要点
        mPolyMatrix.setPolyToPoly(src, 0, dst, 0, pointCount);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(100,100);

        // 绘制坐标系
//        CanvasAidUtils.setCoordinateLen(900, 0, 1200, 0);
//        CanvasAidUtils.drawCoordinateSpace(canvas);

        // 根据Matrix绘制一个变换后的图片
        canvas.drawBitmap(mBitmap, mPolyMatrix, null);

        float[] dst = new float[8];
        mPolyMatrix.mapPoints(dst,src);

        // 绘制触控点
        for (int i=0; i<testPoint*2; i+=2 ) {
            canvas.drawPoint(dst[i], dst[i+1],pointPaint);
        }
    }

    public void setTestPoint(int testPoint) {
        this.testPoint = testPoint > 4 || testPoint < 0 ? 4 : testPoint;
        dst = src.clone();
        resetPolyMatrix(this.testPoint);
        invalidate();
    }


    // 点到直线的最短距离的判断 点（x0,y0） 到由两点组成的线段（x1,y1） ,( x2,y2 )
    private double pointToLine(float x1, float y1, float x2, float y2, float x0,float y0) {

        double space = 0;

        double a, b, c;

        a = lineSpace(x1, y1, x2, y2);// 线段的长度

        b = lineSpace(x1, y1, x0, y0);// (x1,y1)到点的距离

        c = lineSpace(x2, y2, x0, y0);// (x2,y2)到点的距离

        if (c <= 0.000001 || b <= 0.000001) {

            space = 0;

            return space;

        }

        if (a <= 0.000001) {

            space = b;

            return space;

        }

        if (c * c >= a * a + b * b) {

            space = b;

            return space;

        }

        if (b * b >= a * a + c * c) {

            space = c;

            return space;

        }

        double p = (a + b + c) / 2;// 半周长

        double s = Math.sqrt(p * (p - a) * (p - b) * (p - c));// 海伦公式求面积

        space = 2 * s / a;// 返回点到线的距离（利用三角形面积公式求高）

        return space;

    }


    // 计算两点之间的距离

    private double lineSpace(float x1, float y1, float x2, float y2) {

        double lineLength = 0;

        lineLength = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2)* (y1 - y2));

        return lineLength;



    }

    public Bitmap getmBitmap(){
        return mBitmap;
    }


}

