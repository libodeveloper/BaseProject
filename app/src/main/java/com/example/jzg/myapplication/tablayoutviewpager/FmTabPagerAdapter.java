package com.example.jzg.myapplication.tablayoutviewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by libo on 2016/12/30.
 *
 * @Email: libo@jingzhengu.com
 * @Description:
 */
public class FmTabPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragmentList;
    private ArrayList<String>   titleList;

    public FmTabPagerAdapter(FragmentManager fm,ArrayList<Fragment> fragmentList,ArrayList<String> titleList) {
        super(fm);
        this.fragmentList = fragmentList;
        this.titleList = titleList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList == null ? 0 : fragmentList.size();
    }

    //设置tabLayout的tab内容
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
