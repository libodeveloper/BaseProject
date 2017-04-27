package com.example.jzg.myapplication.view;
/**
 * author: gcc
 * date: 2016/11/1 17:20
 * email:
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.utils.SizeUtils;
import com.example.jzg.myapplication.R;

import java.util.ArrayList;


public class MyVerticalItem extends LinearLayout {

    private LinearLayout ll_hrz;
    private Context context;
    private ArrayList<View> views;


    public ArrayList<View> getViews() {
        return views;
    }

    public MyVerticalItem(Context context) {
        this(context, null);
    }
    public MyVerticalItem(Context context, int viewCount) {
        this(context, null);
        addItemView(viewCount);
    }

    public MyVerticalItem(Context context, AttributeSet attrs) {
        this(context, null, 0);
    }

    public MyVerticalItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        View view = View.inflate(context, R.layout.vertical_list_item, this);
        ll_hrz = (LinearLayout) view.findViewById(R.id.ll_hrz);
    }

    public LinearLayout getLl_hrz() {
        return ll_hrz;
    }

    public void setLl_hrz(LinearLayout ll_hrz) {
        this.ll_hrz = ll_hrz;
    }
    public void addItemView(int count){
        views = new ArrayList<>();
        views.clear();
        for(int i=0;i<count;i++){
            View textView = View.inflate(context, R.layout.vertical_item, null);

            int width = getResources().getDimensionPixelSize(R.dimen.carTypeRightWid);
            int height = getResources().getDimensionPixelSize(R.dimen.ItmeHeightS);

            width = SizeUtils.px2dp(width);
            height = SizeUtils.px2dp(height);

            LayoutParams layoutParams = new LayoutParams(SizeUtils.dp2px(width),
                    SizeUtils.dp2px(height));
            layoutParams.leftMargin = SizeUtils.dp2px(1);
            layoutParams.topMargin = SizeUtils.dp2px(1);
            textView.setLayoutParams(layoutParams);
            views.add(textView);
            ll_hrz.addView(textView);
        }
    }

}
