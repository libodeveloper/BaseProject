package com.example.jzg.myapplication.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jzg.myapplication.R;
import com.example.jzg.myapplication.app.AppManager;
import com.example.jzg.myapplication.app.SysApplication;
import com.example.jzg.myapplication.dialog.DialogUtil;
import com.example.jzg.myapplication.utils.MyToast;
import com.example.jzg.myapplication.utils.StatusBarCompat;
import com.trello.rxlifecycle.components.support.RxFragmentActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


public abstract class BaseActivity<T> extends AppCompatActivity implements IBaseView{
    @Nullable
    @BindView(R.id.ivLeft)
    protected ImageView ivLeft;
    @Nullable
    @BindView(R.id.tvTitle)
    protected TextView tvTitle;
    @Nullable
    @BindView(R.id.ivRight)
    protected ImageView ivRight;

    protected String TAG = getClass().getName();
    public SysApplication appContext;
    protected T mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        appContext = SysApplication.getAppContext();
        mPresenter = createPresenter();
//        setStatusBarColor();
        initViews(savedInstanceState);
        ButterKnife.bind(this);
        setData();
    }

    protected abstract T createPresenter();

    /**
     * 初始化布局和控件
     */
    protected abstract void initViews(Bundle savedInstanceState);

    /**
     * 设置相关数据
     */
    protected abstract void setData();

    public void goBack(View view) {
        finish();
    }

    public void showBack(boolean show) {
        if (!show && ivLeft != null)
            ivLeft.setVisibility(View.GONE);
    }


    public void setIvRight(int resId) {
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setImageResource(resId);
    }


    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setTitle(int resId) {
        tvTitle.setText(resId);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        ShowDialogTool.dismissLoadingDialog();
        //        ShowDialogTool.showLoadingDialog(this);
    }


    /**
     * 着色状态栏（4.4以上系统有效）
     */
    protected void setStatusBarColor() {
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary));
    }

    /**
     * 着色状态栏（4.4以上系统有效）
     */
    protected void setStatusBarColor(int color) {
        StatusBarCompat.setStatusBarColor(this, color);
    }

    /**
     * 沉浸状态栏（4.4以上系统有效）
     */
    protected void setTranslanteBar() {
        StatusBarCompat.translucentStatusBar(this);
    }


    @Override
    public void showError(String error){
        MyToast.showShort(error);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void dismissLoading() {
        DialogUtil.dismissDialog();
    }
}
