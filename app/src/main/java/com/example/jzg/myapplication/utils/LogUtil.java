package com.example.jzg.myapplication.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * @author: voiceofnet
 * email: pengkun@jingzhengu.com
 * phone:18101032717
 * @time: 2016/5/23 15:41
 * @desc:
 */
public class LogUtil {
	public static boolean isDebug = true;

	public static void v(String tag, String msg) {
		if (isDebug)
			Log.v(tag, TextUtils.isEmpty(msg) ? "日志为空"
					: TextUtils.isEmpty(msg) ? "日志为空" : msg);
	}

	public static void d(String tag, String msg) {
		if (isDebug)
			Log.d(tag, TextUtils.isEmpty(msg) ? "日志为空" : msg);
	}

	public static void i(String tag, String msg) {
		if (isDebug)
			Log.i(tag, TextUtils.isEmpty(msg) ? "日志为空" : msg);
	}

	public static void w(String tag, String msg) {
		if (isDebug)
			Log.w(tag, TextUtils.isEmpty(msg) ? "日志为空" : msg);
	}

	public static void e(String tag, String msg) {
		if (isDebug)
			Log.e(tag, TextUtils.isEmpty(msg) ? "日志为空" : msg);
	}

	public static void println(String tag, String msg) {
		if (isDebug)
			System.out.println(tag + "____" + msg);
	}

	public static void f(Object msg) {
		if (isDebug) {
			String s = msg + "";
			Log.i("fast", TextUtils.isEmpty(s) ? "日志为空" : s);
		}
	}
}
