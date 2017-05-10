package com.example.jzg.myapplication.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * zyq
 */
public class MyPhotoView extends ViewPager {

    public MyPhotoView(Context context) {
        super(context);
    }

    public MyPhotoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    //重写viewpager 解决
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            return super .dispatchTouchEvent(ev);
        } catch (IllegalArgumentException ignored) {
        } catch (ArrayIndexOutOfBoundsException e) {
        }

        return false ;

    }

}
