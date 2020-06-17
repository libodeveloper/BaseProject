package com.example.jzg.myapplication.utils;

import android.app.ActivityManager;

import com.example.jzg.myapplication.app.SysApplication;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by libo on 2020/6/16.
 *
 * @Email: libo@jingzhengu.com
 * @Description:
 */
public class MemoryUtil {


    private static final String TAG = "MemoryUtil";

    public static float getMemoryInfo(){
        ActivityManager activityManager = (ActivityManager) SysApplication.getAppContext().getSystemService(ACTIVITY_SERVICE);
        //以下所有单位  MB
        //最大分配内存
        int memory = activityManager.getMemoryClass();

        //android:largeHeap="true"时 能申请到的最大内存
        int largeMemoryClass = activityManager. getLargeMemoryClass();

        //最大分配内存获取方法2 android:largeHeap="true" 开启后 分配最大内存以这个为准
        float maxMemory = (float) (Runtime.getRuntime().maxMemory() * 1.0/ (1024 * 1024));

        //当前分配给APP的总内存   它会根据程序所需的使用情况一点点的去最大分配那里挖。
        float totalMemory = (float) (Runtime.getRuntime().totalMemory() * 1.0/ (1024 * 1024));

        //剩余内存  当前分配的总内存使用后还剩余的内存
        float freeMemory = (float) (Runtime.getRuntime().freeMemory() * 1.0/ (1024 * 1024));

        LogUtil.e(TAG, "memory == "+ memory);
        LogUtil.e(TAG, "largeMemoryClass == "+ largeMemoryClass);
        LogUtil.e(TAG, "maxMemory == "+ maxMemory);
        LogUtil.e(TAG, "totalMemory == "+ totalMemory);
        LogUtil.e(TAG, "freeMemory == "+ freeMemory);

        //当前分配给APP最终剩余内存
        float memorysurplus = memory - totalMemory + freeMemory;
        LogUtil.e(TAG, "当前剩余内存 == "+ memorysurplus);

        return memorysurplus;
    }

}
