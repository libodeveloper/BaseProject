package com.example.jzg.myapplication.popWindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.blankj.utilcode.utils.ScreenUtils;
import com.example.jzg.myapplication.R;
import com.example.jzg.myapplication.tablayoutview.TablayoutViewAdapter;
import com.example.jzg.myapplication.utils.DensityUtil;

import java.util.ArrayList;


/**
 * Created by 李波 on 2017/1/20.
 *
 * popwindow 范例类
 */

/*View rootView = View.inflate(context, R.layout.fragment_detection_important, null);
		popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
		popupWindow.setOutsideTouchable(true);
		popupWindow.setAnimationStyle(R.style.AnimRight);
		popupWindow.showAtLocation(rootView, Gravity.RIGHT, 0, 0);*/

public class PopwindowUtils {
	public static PopupWindow popupWindow=null;
	public static final int SLIDINGMENU_LEFT = 0;
	public static final int SLIDINGMENU_RIGHT = 1;


	/**
	 * Created by 李波 on 2018/3/16.
	 * 从底部弹出的popwindow
	 * tablayout + viewpager + view（轻量级模式 简单界面不必fragment）
	 */
	public static  void popWindowBottomToTop(final Context context,View view) {

		if (popupWindow == null) {

			//加载popwindow布局
			View contentView = View.inflate(context,
					R.layout.popwindow_leveldescription, null);
			//设置布局里各种控件功能
			TabLayout tabLayout = (TabLayout) contentView.findViewById(R.id.tabLayout);
			ViewPager viewPager = (ViewPager) contentView.findViewById(R.id.viewPager);
			ImageView imageView = (ImageView) contentView.findViewById(R.id.iv_close);


			imageView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dimssWindow();
				}
			});

			//初始化view  -> 李波 on 2018/3/16.
			View view1 = ((Activity)context).getLayoutInflater().inflate(R.layout.view1_layout, null);
			View view2 = ((Activity)context).getLayoutInflater().inflate(R.layout.view2_layout, null);


			ArrayList<String> titles = new ArrayList<>();
			ArrayList<View> views = new ArrayList<>();

			views.add(view1);
			views.add(view2);


			//设置viewpager每页对应的标题
			titles.add("事故等级说明");
			titles.add("综合车况等级说明");

			TablayoutViewAdapter tablayoutViewAdapter = new TablayoutViewAdapter(views);
			viewPager.setAdapter(tablayoutViewAdapter);

			viewPager.setCurrentItem(0);
			viewPager.setOffscreenPageLimit(2); //缓冲好4个来回切换时不必再重复创建

			//此步有坑 会清除tabitem 如果用fragment模式 在adapter里 必须复写getPageTitle方法设置tabitem 否则 会不显示tabitem  -> 李波 on 2018/3/16.
			tabLayout.setupWithViewPager(viewPager); //一步实现tablayout 与 viewpager的联动关联


			//重点，如果是view模式 就必须在 tabLayout.setupWithViewPager(viewPager) 下面重新为tabitem赋值 否则会不显示 tabitem -> 李波 on 2018/3/16.
			for (int i=0;i<tabLayout.getTabCount();i++) {
				tabLayout.getTabAt(i).setText(titles.get(i));
			}


			int height = ScreenUtils.getScreenHeight()*3/4;
			//初始化pop 注意：popwindow最好指定固定大小，否则无法显示，不能以为布局设置了大小就没事了。
			//因为布局这时还没加载不知道大小,如果设置成-2 包裹 将造成无法显示问题
			popupWindow = new PopupWindow(contentView, -1,height, true);
			//注意了，popupwindow 播放动画的话，需要加上背景
			popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

			//设置pop获取焦点，否则嵌套在它里面的listview的setOnItemClickListener无法获取焦点响应
			popupWindow.setFocusable(true);

			//设置滑动时的动画包括关闭时的动画
			popupWindow.setAnimationStyle(R.style.AnimDown); //弹出时从底部往上弹出
			//取出坐标，设置popupwindow的位置
			popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);


			//设置popwindow关闭时的监听
			popupWindow.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss() {
					if (popupWindow != null) {
						popupWindow.dismiss();
						popupWindow = null;
					}
					//让屏幕回复不透明状态
					backgroundAlpha((Activity) context, 1f);
				}
			});

			//显示popwindow时让屏幕半透明，到达遮罩层的效果
			backgroundAlpha((Activity) context, 0.5f);

		}
	}

	//侧滑菜单的popwindow
	public static  void popWindowSlidingMenu(final Context context,View view,int SlidingMenu) {

		if (popupWindow == null) {

			//加载popwindow布局
			View contentView = View.inflate(context,
					R.layout.popwindow, null);
			//设置布局里各种控件功能

			int popwidth = DensityUtil.dip2px(context, 300);
			//初始化pop 注意：popwindow最好指定固定大小，否则无法显示，不能以为布局设置了大小就没事了。
			//因为布局这时还没加载不知道大小,如果设置成-2 包裹 将造成无法显示问题
			popupWindow = new PopupWindow(contentView, popwidth,
					-1, true);
			//注意了，popupwindow 播放动画的话，需要加上背景
			popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

			//设置pop获取焦点，否则嵌套在它里面的listview的setOnItemClickListener无法获取焦点响应
			popupWindow.setFocusable(true);

			switch (SlidingMenu) {
				case SLIDINGMENU_LEFT:
					//设置侧滑时的动画包括关闭时的动画
					popupWindow.setAnimationStyle(R.style.AnimLeft);
//					popupWindow.setAnimationStyle(R.style.AnimDown); 从下往上
					//取出坐标，设置popupwindow的位置
					popupWindow.showAtLocation(view, Gravity.LEFT, 0, 0);
					break;
				case SLIDINGMENU_RIGHT:
					//设置侧滑时的动画包括关闭时的动画
					popupWindow.setAnimationStyle(R.style.AnimRight);
					//取出坐标，设置popupwindow的位置
					popupWindow.showAtLocation(view, Gravity.RIGHT, 0, 0);
					break;
			}


			//设置popwindow关闭时的监听
			popupWindow.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss() {
					if (popupWindow != null) {
						popupWindow.dismiss();
						popupWindow = null;
					}
					//让屏幕回复不透明状态
					backgroundAlpha((Activity) context, 1f);
				}
			});

			//显示popwindow时让屏幕半透明，到达遮罩层的效果
			backgroundAlpha((Activity) context, 0.5f);

		}
	}

	//popwindow 示例 移植以此为基准
	public static  void popwindowDemo(final Context context,View view){
		//加载popwindow布局
		View contentView = View.inflate(context,R.layout.popwindow, null);

		//设置布局里各种控件功能
//		TextView tv_photo_album = (TextView) contentView.findViewById(R.id.tv_photo_album);
//		TextView tv_camera = (TextView) contentView.findViewById(R.id.tv_camera);
//		TextView tv_cancle = (TextView) contentView.findViewById(R.id.tv_cancle);

//		tv_photo_album.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				sp.photoAlbum();
//				dimssWindow();
//			}
//		});
//
//		tv_camera.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				sp.camera();
//				dimssWindow();
//			}
//		});
//
//		tv_cancle.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				dimssWindow();
//			}
//		});
		int popheigt = DensityUtil.dip2px(context, 180);
		//初始化pop 注意：popwindow最好指定固定大小，否则无法显示，不能以为布局设置了大小就没事了。
		//因为布局这时还没加载不知道大小,如果设置成-2 包裹 将造成无法显示问题，比如加载listview 拿出长度乘以item高度 计算出最终popwindow高度
		popupWindow = new PopupWindow(contentView, -1,popheigt,true);

		//注意了，popupwindow 播放动画的话，需要加上背景
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


		//设置pop获取焦点，否则嵌套在它里面的listview的setOnItemClickListener无法获取焦点响应
		popupWindow.setFocusable(true);

		// 坐标，把view的坐标设置到传递进来的数组里  location[0]，location[1]，分别代表传入view的 x，y 坐标（左上角）
		int[] location = new int[2];
		view.getLocationInWindow(location);


		//取出坐标，设置popupwindow的位置（考虑的时候要算上状态栏，因为是以******全屏做为基础来算的绝对位置*******）
		//location[0], location[1] + view.getHeight() 代表弹出的位置在传入 View 的正下方（y坐标 + view的高）
		popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1] + view.getHeight());

		//设置popwindow关闭时的监听
		popupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				//让屏幕回复不透明状态
					popupWindow = null;
				backgroundAlpha((Activity)context, 1f);
			}
		});

		//显示popwindow时让屏幕半透明，到达遮罩层的效果
		backgroundAlpha((Activity)context, 0.5f);

		// 渐变动画
		AlphaAnimation aa = new AlphaAnimation(0.2f, 1.0f);
		aa.setDuration(500);
		/*
		// 拉伸动画
		ScaleAnimation sa = new ScaleAnimation(0.5f, 1.0f, 0.5f,
				1.0f, Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0.5f);
		sa.setDuration(500);



		contentView.startAnimation(set);*/
		TranslateAnimation translateAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF,0.0f,
				Animation.RELATIVE_TO_SELF,0.0f,
				Animation.RELATIVE_TO_SELF,1.0f,
				Animation.RELATIVE_TO_SELF,0.0f
		);
		translateAnimation.setDuration(500);
//       translateAnimation.setInterpolator(new BounceInterpolator());回弹插值器
		contentView.startAnimation(translateAnimation);
	}


	/**
	 * 消掉popupWindow
	 */
	public static void dimssWindow() {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
			popupWindow = null;
		}
	}

	/**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public static void backgroundAlpha(Activity context, float bgAlpha)
    {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }


}
