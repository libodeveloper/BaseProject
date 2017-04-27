package com.example.jzg.myapplication.view;

import android.content.Context;
import android.util.AttributeSet;

public class MyXRecyclerView extends com.jcodecraeer.xrecyclerview.XRecyclerView{

	public MyXRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyXRecyclerView(Context context) {
        super(context);
    }

    public MyXRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);       
    }       
      
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {       
      
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);       
    }

}
