package com.example.jzg.myapplication.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

/**
 * Created by libo on 2020/6/2.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 竖直方向的SeekBar
 */
public class VerticalSeekBar extends SeekBar {
    public VerticalSeekBar(Context context) {
        super(context);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //将宽高进行对调
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, w, oldh, oldw);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.rotate(-90);
        canvas.translate(-getHeight(), 0);
        super.onDraw(canvas);

    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);
        onSizeChanged(getWidth(), getHeight(), 0, 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                iseekBarTouchListener.touchDown();
                break;
            case MotionEvent.ACTION_MOVE:
                setProgress(getMax() - (int) (getMax() * event.getY() / getHeight()));

                break;
            case MotionEvent.ACTION_UP:
                iseekBarTouchListener.touchup();
                setProgress(getMax() - (int) (getMax() * event.getY() / getHeight()));
                break;

            case MotionEvent.ACTION_CANCEL:
                break;
        }

        return true;
    }

    IseekBarTouchListener iseekBarTouchListener;

    public void setIseekBarTouchListener(IseekBarTouchListener iseekBarTouchListener){
        this.iseekBarTouchListener = iseekBarTouchListener;
    }

    public interface IseekBarTouchListener{
        void touchDown();
        void touchup();
    }

}
