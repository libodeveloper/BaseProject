package com.example.jzg.myapplication.broadcast;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.jzg.myapplication.utils.MyToast;

/**
 * Created by libo on 2018/7/12.
 *
 * @Email: libo@jingzhengu.com
 * @Description:
 */
public class MyReceiver extends BroadcastReceiver {

    private static final String ACTION_NAME = "testBrodcast";
    private static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals(ACTION_NAME)){
            MyToast.showShort("testBrodcast");
        }

        if(action.equals(ACTION_BOOT)){
            MyToast.showShort("开机了");
            Log.i("MyReceiver", "onReceive: 开机了");
        }


        if (Intent.ACTION_SCREEN_ON.equals(action)) { // 开屏
            Log.i("MyReceiver", "onReceive: 开屏");

        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) { // 锁屏
            Log.i("MyReceiver", "onReceive: 锁屏锁屏");

        } else if (Intent.ACTION_USER_PRESENT.equals(action)) { // 解锁
            Log.i("MyReceiver", "onReceive: 解锁解锁解锁");

        }



    }
}
