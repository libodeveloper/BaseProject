package com.example.jzg.myapplication;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.jzg.myapplication.app.SysApplication;
import com.example.jzg.myapplication.app.UpdateManager;
import com.example.jzg.myapplication.base.BaseActivity;
import com.example.jzg.myapplication.bean.User;
import com.example.jzg.myapplication.mvpview.ILogin;
import com.example.jzg.myapplication.presenter.LoginPresenter;
import com.example.jzg.myapplication.utils.MyToast;
import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by 李波 on 2017/1/20.
 * rxjava+okhttp3+retrofit 请求网络接口示例
 */
public class DemoActivity extends BaseActivity<LoginPresenter> implements ILogin {

    @BindView(R.id.etUser)
    EditText etUser;
    @BindView(R.id.etPassword)
    EditText etPassword;
    private String userName;
    private String password;


    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);

    }


    @Override
    protected void setData() {
        init();
    }


    public void init() {
        tvTitle.setText("登录");

        userName = "BJPGS3";
        password = "666666";

        //请求网络
        mPresenter.login(userName, password, true);

        //检测版本更新
        checkUpdata();
    }


    //登录接口：网络请求成功处理
    @Override
    public void loginSucceed(User user) {

    }

    //登录接口：网络请求失败
    @Override
    public void loginFailed() {

    }

    @Override
    public void getTokenSucceed() {

    }

    @Override
    public void getTokenFailed() {

    }

    @OnClick({R.id.ivRight})
    public void onClick(View view) {
        //存数据库
//        String json = new Gson().toJson(DetectMainActivity.detectMainActivity
// .getLocalDetectionData());
//        DBManager.getInstance().updateOrInsert(Constants.DATA_TYPE_USE_TASK, taskId,
// SysApplication.getUser().getUserId(), json);

        //取数据库
//        String json = DBManager.getInstance().queryLocalPlan(item.getPlanId(), Constants
// .DATA_TYPE_PLAN);


        //Manifest.permission.CAMERA 相机权限
        //Manifest.permission.WRITE_EXTERNAL_STORAGE SD卡读写权限
        if (Build.VERSION.SDK_INT >= 23) { //如果系统版本号大于等于23 也就是6.0，就必须动态请求敏感权限（也要配置清单）
            RxPermissions.getInstance(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA).subscribe(new Action1<Boolean>() {
                @Override
                public void call(Boolean granted) {
                    if (granted) { //请求获取权限成功后的操作

                    } else {
                        MyToast.showShort("需要获取SD卡读取权限来保存图片");
                    }
                }
            });
        } else { //否则如果是6.0以下系统不需要动态申请权限直接配置清单就可以了

        }
    }

    /**
     * Created by 李波 on 2017/5/15.
     * 检测版本更新
     */
    private void checkUpdata(){
        if (SysApplication.networkAvailable)
            UpdateManager.getUpdateManager().checkAppUpdate(this, false);
    }

}
