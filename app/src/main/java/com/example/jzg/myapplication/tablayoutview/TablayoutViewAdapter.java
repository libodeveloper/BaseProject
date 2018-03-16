package com.example.jzg.myapplication.tablayoutview;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by libo on 2016/12/30.
 *
 * @Email: libo@jingzhengu.com
 * @Description: tab页adapter
 */
public class TablayoutViewAdapter extends PagerAdapter {

    private ArrayList<View> viewList;

    public TablayoutViewAdapter(ArrayList<View> viewList){
        this.viewList = viewList;
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    //展示的view
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //获得展示的view
        View view=viewList.get(position);
        //添加到容器
        container.addView(view);
        //返回显示的view
        return view;
    }
    //销毁view
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //从容器中移除view
        container.removeView((View) object);
    }
}
