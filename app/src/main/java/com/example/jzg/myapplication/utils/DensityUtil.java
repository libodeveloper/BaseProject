package com.example.jzg.myapplication.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;

public class DensityUtil {
	/** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  

    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
    /**
     * 将px值转换为sp值，保证文字大小不变
     * 
     * @param pxValue
     * @param
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */ 
    public static int px2sp(Context context, float pxValue) { 
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
        return (int) (pxValue / fontScale + 0.5f); 
    } 
    
    /**
     * 将sp值转换为px值，保证文字大小不变
     * 
     * @param spValue
     * @param
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */ 
    public static int sp2px(Context context, float spValue) { 
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
        return (int) (spValue * fontScale + 0.5f); 
    } 
    
    /**
     * 判断手机是否有虚拟导航栏
     * @param activity
     * @return
     */
    public static boolean checkDeviceHasNavigationBar(Context activity) {  
    	  
        //通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar  
        boolean hasMenuKey = ViewConfiguration.get(activity)  
                .hasPermanentMenuKey();  
        boolean hasBackKey = KeyCharacterMap  
                .deviceHasKey(KeyEvent.KEYCODE_BACK);  
  
        if (!hasMenuKey && !hasBackKey) {  
            // 做任何你需要做的,这个设备有一个导航栏  
        	Log.i("TTT", "有虚拟导航栏");
            return true;  
        }  
        Log.i("TTT", "meimeimei有虚拟导航栏");
        return false;  
    }  
    
    /**
     * 计算虚拟导航栏的高度
     * @param activity
     * @return
     */
    public static int getNavigationBarHeight(Activity activity) {  
        Resources resources = activity.getResources();  
        int resourceId = resources.getIdentifier("navigation_bar_height",  
                "dimen", "android");  
        //获取NavigationBar的高度  
        int height = resources.getDimensionPixelSize(resourceId);  
        return height;  
    }
}
