package com.example.jzg.myapplication.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;


import com.example.jzg.myapplication.R;

import java.lang.ref.WeakReference;

/**
 * author: guochen
 * date: 2016/12/13 14:21
 * email: 
 */
@SuppressLint("AppCompatCustomView")
public class FocusImageView extends ImageView {
    public final static String TAG="FocusImageView";
    private static final int NO_ID=-1;
    private int mFocusImg=NO_ID;
    private int mFocusSucceedImg=NO_ID;
    private int mFocusFailedImg=NO_ID;
    private Animation mAnimation;
    private Handler mHandler = new OptimizeHandler(this);
    public static boolean isGone=true;

    private static SeekBar seekBar;

    /**
     * 优化handler内存泄漏，通过静态内部类 + 弱引用外部类的实例对象
     */
    private static class OptimizeHandler extends Handler {
        private WeakReference<FocusImageView> reference;

        public OptimizeHandler(FocusImageView imageView) {
            reference = new WeakReference<FocusImageView>(imageView);
        }

        @Override
        public void handleMessage(Message msg) {
            FocusImageView imageView = (FocusImageView) reference.get();
            if (imageView == null) return;

            if (isGone){
                imageView.setVisibility(View.GONE);
                if (seekBar!=null)
                    seekBar.setVisibility(GONE);
            }
        }
    }

    public FocusImageView(Context context) {
        super(context);
        mAnimation= AnimationUtils.loadAnimation(getContext(), R.anim.focusview_show);
        setVisibility(View.GONE);
    }

    public FocusImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mAnimation= AnimationUtils.loadAnimation(getContext(), R.anim.focusview_show);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FocusImageView);
        mFocusImg = a.getResourceId(R.styleable.FocusImageView_focus_focusing_id, NO_ID);
        mFocusSucceedImg=a.getResourceId(R.styleable.FocusImageView_focus_success_id, NO_ID);
        mFocusFailedImg=a.getResourceId(R.styleable.FocusImageView_focus_fail_id, NO_ID);
        a.recycle();

        //聚焦图片不能为空
        if (mFocusImg==NO_ID||mFocusSucceedImg==NO_ID||mFocusFailedImg==NO_ID)
            throw new RuntimeException("Animation is null");
    }

    /**
     *   显示聚焦图案
     *   触屏的x坐标
     *   触屏的y坐标
     */
    public void startFocus(Point point){
        if (mFocusImg==NO_ID||mFocusSucceedImg==NO_ID||mFocusFailedImg==NO_ID)
            throw new RuntimeException("focus image is null");

        //因为手动对焦有可能快速持续的点击，所以每次点击对焦先取消掉上一次发送的 handler延时消息，否则会连续不停的发消息
        mHandler.removeMessages(0);

        //根据触摸的坐标设置聚焦图案的位置
        RelativeLayout.LayoutParams params=(RelativeLayout.LayoutParams) getLayoutParams();
        params.topMargin= point.y-getHeight()/2;
        params.leftMargin=point.x-getWidth()/2;
        setLayoutParams(params);
        //设置控件可见，并开始动画
        setVisibility(View.VISIBLE);
        setImageResource(mFocusImg);
        startAnimation(mAnimation);

        //对焦后发送延时消息3.5秒隐藏对焦框。
        mHandler.sendEmptyMessageDelayed(0, 3500);
    }

    /**
     *   聚焦成功回调
     */
    public void onFocusSuccess(){
        setImageResource(mFocusSucceedImg);
        //移除在startFocus中设置的callback
        mHandler.removeCallbacks(null, null);

    }

    /**
     *   聚焦失败回调
     */
    public void onFocusFailed(){
        setImageResource(mFocusFailedImg);
        //移除在startFocus中设置的callback
        mHandler.removeCallbacks(null, null);
    }


    public void setSeekBar(SeekBar seekBar) {
        this.seekBar = seekBar;
    }

    public void onDestroy() {
        if (mHandler != null) {
            mHandler.removeMessages(0);
        }
    }
}
