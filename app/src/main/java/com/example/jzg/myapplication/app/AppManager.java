/**
 * Project Name:JZGPingGuShi
 * File Name:AppManager.java
 * Package Name:com.gc.jzgpinggushi.app
 * Date:2014-9-1上午10:18:48
 * Copyright (c) 2014, wangyd523@gmail.com All Rights Reserved.
 */

package com.example.jzg.myapplication.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.Stack;

/**
 * Created by 李波 on 2017/1/20.
 *
 * app管理类负责创建 Activity 界面的栈内存
 * 视情况而用
 */
public class AppManager {
    private static Stack<Activity> activityStack;

    private static AppManager instance;

    private AppManager() {
    }

    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    //入栈加入集合
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    //获取当前Activity
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    //出栈移出集合
    public synchronized void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }

    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }


    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
        }
    }

    public void clearActivitys(){
        if(activityStack!=null){
            activityStack.clear();
        }
    }
}
