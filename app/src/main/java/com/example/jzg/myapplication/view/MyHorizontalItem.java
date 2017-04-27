package com.example.jzg.myapplication.view;
/**
 * author: gcc
 * date: 2016/11/1 16:10
 * email:
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jzg.myapplication.R;


/**
 * author: guochen
 * date: 2016/11/1 16:10
 * email:
 */
public class MyHorizontalItem extends LinearLayout implements View.OnClickListener {

    private AutoFitTextView  tv_chexing;
    private TextView tv_close;
    private TextView tv_ok;
    private int position = -1;
    private TextView tv_zhidaojia;

    public MyHorizontalItem(Context context) {
        this(context, null);
    }

    public MyHorizontalItem(Context context, AttributeSet attrs) {
        this(context, null, 0);
    }

    public MyHorizontalItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = View.inflate(context, R.layout.my_horizontal_item, this);
        tv_chexing = (AutoFitTextView ) view.findViewById(R.id.tv_chexing);
        tv_zhidaojia = (TextView) view.findViewById(R.id.tv_zhidaojia);
        tv_close = (TextView) view.findViewById(R.id.tv_close);
        tv_ok = (TextView) view.findViewById(R.id.tv_ok);
        tv_close.setOnClickListener(this);
        tv_ok.setOnClickListener(this);
    }

    /**
     * 设置指导价
     */
    public void setGuidePrice(String price) {
        tv_zhidaojia.setText("指导价:"+price);
    }

    /**
     * 设置车型车系
     * @param content
     */
    public void setContent(String content) {
        tv_chexing.setText(content);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public AutoFitTextView  getTv_chexing() {
        return tv_chexing;
    }

    public void setTv_chexing(AutoFitTextView  tv_chexing) {
        this.tv_chexing = tv_chexing;
    }

    public TextView getTv_close() {
        return tv_close;
    }

    public void setTv_close(TextView tv_close) {
        this.tv_close = tv_close;
    }

    public TextView getTv_ok() {
        return tv_ok;
    }

    public void setTv_ok(TextView tv_ok) {
        this.tv_ok = tv_ok;
    }

    public void setMyOkOnClickListener(MyOkOnClickListener myOkOnClickListener) {
        this.myOkOnClickListener = myOkOnClickListener;
    }

    public void setMyCloseOnClickListener(MyCloseOnClickListener myCloseOnClickListener) {
        this.myCloseOnClickListener = myCloseOnClickListener;
    }

    private MyOkOnClickListener myOkOnClickListener;
    private MyCloseOnClickListener myCloseOnClickListener;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_close:
                if (myCloseOnClickListener != null) {
                    myCloseOnClickListener.onCloseClickListener(v, position);
                }
                break;
            case R.id.tv_ok:
                if (myOkOnClickListener != null) {
                    myOkOnClickListener.onOkClickListener(v, position);
                }
                break;
            default:

                break;
        }
    }

    public interface MyOkOnClickListener {
        void onOkClickListener(View view, int position);
    }

    public interface MyCloseOnClickListener {
        void onCloseClickListener(View view, int position);
    }

}
