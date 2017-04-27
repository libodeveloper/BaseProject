package com.example.jzg.myapplication.http;

/**
 * Created by 李波 on 2017/1/20.
 *
 * 网络请求异常
 */
public class ResponseErrorException extends RuntimeException {
    public ResponseErrorException(String msg) {
        super(msg);
    }
}
