package com.example.jzg.myapplication.base;


/**
 * Created by 李波 on 2017/1/20.
 */
public interface IBaseView {

    /**
     * 显示错误信息
     *
     * @param error 错误信息
     */
    void showError(String error);

    void showLoading();

    void showLoading(String msg);

    void dismissLoading();


}
