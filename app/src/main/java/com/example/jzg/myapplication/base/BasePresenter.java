package com.example.jzg.myapplication.base;

/**
 * Created by 李波 on 2017/1/20.
 */
public abstract class BasePresenter<T> {
    protected String TAG = getClass().getName();
    protected T baseView;

    public BasePresenter(T from) {
        this.baseView = from;
    }
}
