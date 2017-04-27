package com.example.jzg.myapplication.http;

import android.text.TextUtils;


import com.example.jzg.myapplication.base.IBaseView;
import com.example.jzg.myapplication.utils.LogUtil;
import com.example.jzg.myapplication.utils.MyToast;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.functions.Action1;

/**
 * Created by 李波 on 2017/1/20.
 * 网络请求失败的处理，基于 RxJava 的模式
 * 由 ResponseSubscriber 统一封装 暂不使用
 */
public abstract class RequestFailedAction implements Action1<Throwable>{

    @Override
    public void call(Throwable throwable) {
        String error = "";
        if(throwable instanceof ResponseErrorException){

            error = throwable.getMessage();
//            error = "服务器响应异常，请稍后重试";
        }else if(throwable instanceof IOException){
            error = "请检查您的网络后重试";
        }else if(throwable instanceof HttpException){
            HttpException httpException = (HttpException) throwable;
            error = httpException.getMessage();
        }else{
            error = "未知错误";
            throwable.printStackTrace();
            LogUtil.e("RequestFailedAction",throwable.getMessage());
        }

        if (TextUtils.isEmpty(error))
            error = "服务器响应异常，请稍后重试";

        onFailed(error);

    }

    public abstract void onFailed(String errorInfo);
}
