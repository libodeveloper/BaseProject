package com.example.jzg.myapplication.http;


import com.example.jzg.myapplication.global.Constants;
import com.example.jzg.myapplication.utils.MyToast;

import rx.functions.Action1;

/**
 * Created by 李波 on 2017/1/20.
 *
 * 网络请求成功的处理，基于 Rxjava 模式
 *
 * 由 ResponseSubscriber 统一封装 暂不使用
 */
public abstract class RequestSuccessAction<T extends ResponseJson> implements Action1<T>{
    @Override
    public void call(T t) {
        int status = t.getStatus();
        //郑有权。勿删
        if(status == Constants.CLAIM_STATUS_CODE){
            onSuccess(t);
            return;
        }else if(status!= Constants.SUCCESS_STATUS_CODE){
            MyToast.showLong(t.getMsg());
            throw new ResponseErrorException(t.getMsg());
        }
        onSuccess(t);
    }

    public abstract void onSuccess(T response);
}
