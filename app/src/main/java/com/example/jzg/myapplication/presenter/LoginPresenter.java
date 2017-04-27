package com.example.jzg.myapplication.presenter;
import com.example.jzg.myapplication.base.BasePresenter;
import com.example.jzg.myapplication.bean.User;
import com.example.jzg.myapplication.http.ApiManager;
import com.example.jzg.myapplication.http.ResponseJson;
import com.example.jzg.myapplication.http.ResponseSubscriber;
import com.example.jzg.myapplication.http.RxThreadUtil;
import com.example.jzg.myapplication.mvpview.ILogin;
import com.example.jzg.myapplication.utils.LogUtil;
import com.example.jzg.myapplication.utils.MD5Utils;
import com.example.jzg.myapplication.utils.UIUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by 李波 on 2017/1/20.
 *
 * mvp 模式 Presenter层的网络请求
 */

public class LoginPresenter extends BasePresenter<ILogin> {
    public LoginPresenter(ILogin from) {
        super(from);
    }

    //登录
    public void login(final String username, final String password, final boolean isShowDialog){
        Map<String,String> params = new HashMap<>();
        params.put("username",username);
        params.put("password",password);
        params = MD5Utils.encryptParams(params);
        LogUtil.e(TAG, UIUtils.getUrl(params));

        ApiManager.getApiServer().login(params)
                .compose(RxThreadUtil.<ResponseJson<User>>networkSchedulers())
                .subscribe(new ResponseSubscriber<ResponseJson<User>>(baseView) {
                    @Override
                    public void onSuccess(ResponseJson<User> response) {
                        User user = response.getMemberValue();
                        baseView.loginSucceed(user);
                    }
                });

        /*
            传入不完整定义观察者的示例 暂不用
        SysApplication.getApiServer().login(params)
                .compose(RxThreadUtil.<ResponseJson<User>>networkSchedulers())
                .subscribe(new RequestSuccessAction<ResponseJson<User>>(){
                    @Override
                    public void onSuccess(ResponseJson<User> response) {
                        User user = response.getMemberValue();
                        baseView.loginSucceed(user);
                    }
                }, new RequestFailedAction() {
                    @Override
                    public void onFailed(String errorInfo) {
                        baseView.loginFailed();
                    }
                });*/

    }

    //获取token
    public void getToken(final String username, final String password, final boolean isShowDialog){
        Map<String,String> params = new HashMap<>();
        params.put("username",username);
        params.put("password",password);
        params = MD5Utils.encryptParams(params);
        LogUtil.e(TAG, UIUtils.getUrl(params));

        ApiManager.getApiServer().token(params)
                .compose(RxThreadUtil.<ResponseJson<User>>networkSchedulers())
                .subscribe(new ResponseSubscriber<ResponseJson<User>>(baseView) {
                    @Override
                    public void onSuccess(ResponseJson<User> response) {
                        User user = response.getMemberValue();
                        baseView.getTokenSucceed();
                    }

                    @Override
                    public void onError(Throwable e) { //请求失败的单独处理
                        super.onError(e);
                        baseView.getTokenFailed();
                    }
                });

    }
}
