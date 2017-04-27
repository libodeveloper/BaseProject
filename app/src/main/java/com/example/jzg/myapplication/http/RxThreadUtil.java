package com.example.jzg.myapplication.http;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 李波 on 2017/1/20.
 *
 * 线程转换Util ，基于Rxjava
 */
public class RxThreadUtil {
    public static <T> Observable.Transformer<T, T> networkSchedulers() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> source) {
                return source.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

}
