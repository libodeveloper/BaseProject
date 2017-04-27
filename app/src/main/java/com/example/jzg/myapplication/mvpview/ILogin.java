package com.example.jzg.myapplication.mvpview;

import com.example.jzg.myapplication.base.IBaseView;
import com.example.jzg.myapplication.bean.User;

/**
 * Created by 李波 on 2017/1/20.
 *
 * mvp 模式接口
 */
public interface ILogin extends IBaseView {
    void loginSucceed(User user);
    void loginFailed();

    void getTokenSucceed();
    void getTokenFailed();
}
