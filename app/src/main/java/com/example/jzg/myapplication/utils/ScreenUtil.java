package com.example.jzg.myapplication.utils;

import android.content.Context;

/**
 * Created by libo on 2017/5/5.
 *
 * @Email: libo@jingzhengu.com
 * @Description:
 */
public class ScreenUtil {

    /**
     * 获取状态栏高度
     *
     * @param context 上下文
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources()
                .getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
