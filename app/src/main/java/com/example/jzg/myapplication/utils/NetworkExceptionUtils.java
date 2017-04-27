package com.example.jzg.myapplication.utils;

import java.net.SocketTimeoutException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by wangyd on 16/6/28.
 * 异常处理类（如果要对非网络异常进行处理请自行重载方法）
 */
public final class NetworkExceptionUtils {

    private static final String TAG = "NetworkExceptionUtils";

    /**
     * 根据网络异常返回对象错误信息
     *
     * @param e RxJava异常信息
     * @return
     */
    public static String getErrorByException(Throwable e) {
        String error = null;
        if (e instanceof SocketTimeoutException) {
            error = "请求超时";
        }else if(e instanceof HttpException){
            int code = ((HttpException) e).code();
            LogUtil.e(TAG,e.getMessage()+"\r\n"+"code="+code);
            if(code ==401){
                error = "客户端没有访问权限";
            }else if(code ==404){
                error = "请求的资源不存在";
            }else if(code ==403){
                error = "服务器拒绝请求";
            }else if(code ==500){
                error = "请求错误";
            }else if(code==503){
                error = "服务器超时";
            }
        }
        if("请求超时".equals(error)){
            MyToast.showShort("网络超时，请稍后重试");
            error = null;
        }
        return error;
    }
}
