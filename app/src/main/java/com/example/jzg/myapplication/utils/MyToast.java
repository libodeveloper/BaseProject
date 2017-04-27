package com.example.jzg.myapplication.utils;

import android.widget.Toast;

/**
 * @author: voiceofnet
 * email: pengkun@jingzhengu.com
 * phone:18101032717
 * @time: 2016/5/23 15:41
 * @desc:
 */
public class MyToast {
	private static Toast toast;
	public static void showLong(String content){
		if(toast==null){
			toast = Toast.makeText(UIUtils.getContext(), content, Toast.LENGTH_LONG);
		}else {
			toast.setText(content);
		}
		toast.show();

	}
	public static void showShort(int resId){
		if(toast==null){
			toast = Toast.makeText(UIUtils.getContext(), resId, Toast.LENGTH_SHORT);
		}else{
			toast.setText(resId);
		}
		toast.show();

	}

	public static void showShort(String content){
		if(toast==null){
			toast = Toast.makeText(UIUtils.getContext(), content, Toast.LENGTH_SHORT);
		}else{
			toast.setText(content);
		}
		toast.show();
	}
	public static void showLong(int resId){
		if(toast==null){
			toast = Toast.makeText(UIUtils.getContext(), resId, Toast.LENGTH_LONG);
		}else{
			toast.setText(resId);
		}
		toast.show();
	}
}
