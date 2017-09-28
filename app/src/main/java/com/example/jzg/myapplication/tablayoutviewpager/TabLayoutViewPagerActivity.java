package com.example.jzg.myapplication.tablayoutviewpager;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jzg.myapplication.R;
import com.example.jzg.myapplication.app.AppManager;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by libo on 2016/12/30.
 *
 * @Email: libo@jingzhengu.com
 * @Description:  tablayout + viewpager 轻松实现 viewpagerindicator 第三方框架功能，还能实现以前RidaoGroup的底部导航功能
 */
public class TabLayoutViewPagerActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FmTabPagerAdapter fmTabPagerAdapter;
    private ArrayList<Fragment> fragments;
    private ArrayList<String> titles;
    private TextView textView;
    private int Item[] = {R.drawable.home_find,R.drawable.home_gps,R.drawable.home_renping,R.drawable.home_stroll};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablayout_viewpager);
        AppManager.getAppManager().addActivity(this);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        titles = new ArrayList<>();
        fragments = new ArrayList<>();
        TabViewPagerFragment ft1 = new TabViewPagerFragment();
        TabViewPagerFragment ft2 = new TabViewPagerFragment();
        TabViewPagerFragment ft3 = new TabViewPagerFragment();
        TabViewPagerFragment ft4 = new TabViewPagerFragment();
        //fragment初始化数据
        ft1.setmPage(1);
        ft2.setmPage(2);
        ft3.setmPage(3);
        ft4.setmPage(4);

        fragments.add(ft1);
        fragments.add(ft2);
        fragments.add(ft3);
        fragments.add(ft4);

        //设置viewpager每页对应的标题
        titles.add("1");
        titles.add("2");
        titles.add("3");
        titles.add("4");

        fmTabPagerAdapter = new FmTabPagerAdapter(getSupportFragmentManager(),fragments,titles);
        viewPager.setAdapter(fmTabPagerAdapter);

        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(4); //缓冲好4个来回切换时不必再重复创建

        tabLayout.setupWithViewPager(viewPager); //一步实现tablayout 与 viewpager的联动关联

//=============打开这里即可实现以前RidaoGroup的底部导航功能  app:tabIndicatorHeight="0dp" 下划线为高度设置 0
       //-----------自定义tablayout的tabview-----------------------------
//        for (int i = 0; i < tabLayout.getTabCount(); i++) {
//            TabLayout.Tab tab = tabLayout.getTabAt(i);
//            RelativeLayout parentView = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.custom_tablayout_item, null);
//            ImageView iv = (ImageView) parentView.findViewById(R.id.iv);
//            TextView  tv = (TextView) parentView.findViewById(R.id.tv);
//             Drawable drawable = getResources().getDrawable(Item[i]);
//                    iv.setBackgroundDrawable(drawable);
//
//            if (i==3) {
//                textView = tv;
//                tv.setVisibility(View.VISIBLE);
//            }
//            tab.setCustomView(parentView);
//        }
        //----------------------------------------

        //设置tablayout的 select 监听
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//               int  curIndex = tab.getPosition();
//                pager.setCurrentItem(tab.getPosition(), true);

                if (textView!=null) {
                    Random random = new Random();
                    int ra = random.nextInt(10) + 1;
                    textView.setText("" + ra);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }
}
