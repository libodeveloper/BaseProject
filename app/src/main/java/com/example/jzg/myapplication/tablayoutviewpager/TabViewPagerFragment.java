package com.example.jzg.myapplication.tablayoutviewpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jzg.myapplication.R;

/**
 * Created by libo on 2016/12/30.
 *
 * @Email: libo@jingzhengu.com
 * @Description:
 */
public class TabViewPagerFragment extends Fragment {

    private int mPage;

    public void setmPage(int mPage) {
        this.mPage = mPage;
    }


    public TabViewPagerFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_viewpager,container,false);
        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText("第"+mPage+"页");
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
