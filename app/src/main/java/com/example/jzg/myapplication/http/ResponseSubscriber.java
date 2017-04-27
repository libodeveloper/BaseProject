package com.example.jzg.myapplication.http;

import android.text.TextUtils;


import com.example.jzg.myapplication.app.SysApplication;
import com.example.jzg.myapplication.base.IBaseView;
import com.example.jzg.myapplication.global.Constants;
import com.example.jzg.myapplication.utils.LogUtil;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by 李波 on 2017/3/24.
 * Rxjava 观察者基类
 */
public abstract class ResponseSubscriber<T extends ResponseJson> extends Subscriber<T> {
    private IBaseView view;
    private boolean showLoading;
    private String loadingText;

    /**
     * Created by 李波 on 2017/3/24.
     * 默认显示dialog
     */
    public ResponseSubscriber(IBaseView view) {
        this.view = view;
        this.showLoading = true;
    }

    public ResponseSubscriber(IBaseView view, boolean showLoading) {
        this(view);
        this.showLoading = showLoading;
    }

    public ResponseSubscriber(IBaseView view, boolean showLoading, String loadingText) {
        this(view, showLoading);
        this.loadingText = loadingText;
    }

    @Override
    public void onCompleted() {
        if (showLoading)
            view.dismissLoading();
    }

    /**
     * Created by 李波 on 2017/3/24.
     * 请求异常通用提示，如果需特殊处理，覆写此方法即可
     */
    @Override
    public void onError(Throwable e) {
        view.dismissLoading();
        String error;
        if (e instanceof ResponseErrorException) {
            error = e.getMessage();
        } else if (e instanceof IOException) {
            error = "请检查您的网络后重试";
        } else if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            error = httpException.getMessage();
        } else {
            error = "服务器响应异常，请稍后重试";
            e.printStackTrace();
            LogUtil.e("RequestFailedAction", e.getMessage());
        }
        view.showError(error);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!SysApplication.networkAvailable) {
            throw new ResponseErrorException("网络不可用，请检测后重试");
        }
        if (showLoading) {
            if (!TextUtils.isEmpty(loadingText)) {
                view.showLoading(loadingText);
            } else {
                view.showLoading();
            }
        }
    }

    @Override
    public void onNext(T t) {
        int status = t.getStatus();
        if (status == Constants.SUCCESS_STATUS_CODE) {
            onSuccess(t);
        } else {
            throw new ResponseErrorException(t.getMsg());
        }
    }

    /**
     * Created by 李波 on 2017/3/24.
     * 请求成功的处理
     */
    public abstract void onSuccess(T response);
}
