package com.example.jzg.myapplication.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

import java.util.Calendar;

/**
 * Created by zealjiang on 2017/1/23 16:44.
 * Email: zealjiang@126.com
 */

public class ScrollBottomScrollView extends ScrollView {

    private OnScrollBottomListener _listener;
    private int _calCount;
    public static final int MIN_CLICK_DELAY_TIME = 20;
    private long lastClickTime = 0;

    public interface OnScrollBottomListener {
        void srollToBottom();
    }

    public void registerOnScrollViewScrollToBottom(OnScrollBottomListener l) {
        _listener = l;
    }

    public void unRegisterOnScrollViewScrollToBottom() {
        _listener = null;
    }

    public ScrollBottomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        View view = this.getChildAt(0);
        if (this.getHeight() + this.getScrollY() == view.getHeight()) {
            _calCount++;
            if (_calCount == 1) {
                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                    lastClickTime = currentTime;
                    if (_listener != null) {
                        _listener.srollToBottom();
                    }
                }

            }
        } else {
            _calCount = 0;
        }



    }
}
