package com.example.jzg.myapplication.app;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.blankj.utilcode.utils.ScreenUtils;
import com.blankj.utilcode.utils.Utils;
import com.example.jzg.myapplication.bean.User;
import com.example.jzg.myapplication.db.DBManager;
import com.example.jzg.myapplication.dialog.DialogUtil;
import com.example.jzg.myapplication.dialogactivity.DialogActivity;
import com.example.jzg.myapplication.global.Constants;
import com.example.jzg.myapplication.http.ApiServer;
import com.example.jzg.myapplication.http.CustomerOkHttpClient;
import com.example.jzg.myapplication.utils.ACache;
import com.example.jzg.myapplication.utils.FrescoImageLoader;
import com.example.jzg.myapplication.utils.MyToast;
import com.github.pwittchen.reactivenetwork.library.ConnectivityStatus;
import com.github.pwittchen.reactivenetwork.library.ReactiveNetwork;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Method;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by 李波 on 2017/1/20.
 *
 *
 */
public class SysApplication extends Application {
    private static int mMainThreadId = -1;
    private static Thread mMainThread;
    private static Handler mMainThreadHandler;
    private static Looper mMainLooper;
    private static SysApplication app;
    private DBManager dbManager;
    private static User user;
    /**
     * 判断是否有网络
     */
    public static boolean networkAvailable = true;
    /**
     * 判断是否是wifi还是移动网络
     */
    public static NetStatus isWifi = NetStatus.WIFI;

    /**
     * 网络状态 分别代表wifi、wifi无网络、运营商网络
     */
    public enum NetStatus {
        WIFI,
        WIFI_NO_INTERNET,
        MOBILE_INTERNET
    }

    AlertDialog dialog;



    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        Utils.init(this);
        mMainThreadId = android.os.Process.myTid();
        mMainThread = Thread.currentThread();
        mMainThreadHandler = new Handler();
        mMainLooper = getMainLooper();
        getScreenSize();

        /**
         * Created by 李波 on 2017/3/29.
         * 初始化Fresco
         * 当遇到大量加载图片时 调用 FrescoImageLoader.displayImage 避免加载原图卡顿问题
         */
        FrescoImageLoader.FrescoInit(this);
//        Fresco.initialize(this);
        initNetworkStatusDetector();
        CustomActivityOnCrash.install(this);  //崩溃日志
        //初始化Logger日志
//        Logger.init("SysApplication") .methodCount(3).logTool(new AndroidLogTool()); // custom log tool, optional


    }


    public static SysApplication getApp(){
        return app;
    }

    /**
     * 初始化网络监听
     */
    public void initNetworkStatusDetector() {
        ReactiveNetwork reactiveNetwork = new ReactiveNetwork();
        reactiveNetwork.observeConnectivity(this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ConnectivityStatus>() {
                    @Override
                    public void call(ConnectivityStatus connectivityStatus) {
                        //判断当前网络状态是否有效
                        if (connectivityStatus.toString().equals(ConnectivityStatus.OFFLINE.toString()) || connectivityStatus.toString().equals(ConnectivityStatus.WIFI_CONNECTED_HAS_NO_INTERNET.toString())) {
                            Toast.makeText(getApplicationContext(), "没有网络", Toast.LENGTH_SHORT).show();
                            networkAvailable = false;
                        } else {
                            networkAvailable = true;
                        }

                        EventBus.getDefault().post(networkAvailable);

                        //判断当前网络状态
                        if ((connectivityStatus.toString().equals(ConnectivityStatus.WIFI_CONNECTED.toString()) && connectivityStatus.toString().equals(ConnectivityStatus.WIFI_CONNECTED_HAS_NO_INTERNET.toString()))
                                || connectivityStatus.toString().equals(ConnectivityStatus.OFFLINE.toString())) {
                            isWifi = NetStatus.WIFI_NO_INTERNET; //连接了wifi,且无线不能用 || 网络不可用
                        } else if (connectivityStatus.toString().equals(ConnectivityStatus.MOBILE_CONNECTED.toString())) {
                            isWifi = NetStatus.MOBILE_INTERNET; //连接了移动网络
                        } else {
                            isWifi = NetStatus.WIFI;//wifi网络
                        }
                    }
                });
    }

    public static SysApplication getAppContext() {
        return app;
    }
    public static int getMainThreadId() {
        return mMainThreadId;
    }
    public static Thread getMainThread() {
        return mMainThread;
    }
    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }
    public static Looper getMainThreadLooper() {
        return mMainLooper;
    }

    /**
     * 获取屏幕宽高，返回 0 表示获取失败
     * @return
     */
    public static int  getScreenSize(){
        if (Constants.ScreenWidth==0||Constants.ScreenHeight==0) {
            Constants.ScreenHeight = ScreenUtils.getScreenHeight();
            Constants.ScreenWidth = ScreenUtils.getScreenWidth();
        }
        return Constants.ScreenWidth * Constants.ScreenHeight;
    }


    /**
     * 获取用户对象
     *
     * @author zealjiang
     * @time 2016/6/28 11:05
     */
    public static User getUser() {
        if (user == null) {
            user = (User) ACache.get(app).getAsObject(Constants.KEY_ACACHE_USER);
        }
        return user;
    }

    public static void setUser(User user) {
        SysApplication.user = user;
    }

    /**
     * Created by 李波 on 2017/9/25.
     * 显示全局Dialog，需要当前的Activity 入栈 退出时 出栈
     */
    public void showDialog(){
        Activity activity = AppManager.getAppManager().currentActivity();
        Intent intent = new Intent(activity, DialogActivity.class);
        activity.startActivity(intent);
    }


}
