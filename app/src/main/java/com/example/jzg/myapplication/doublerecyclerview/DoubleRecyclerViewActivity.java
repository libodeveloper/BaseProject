package com.example.jzg.myapplication.doublerecyclerview;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.jzg.myapplication.R;
import com.example.jzg.myapplication.recyclerview.HeaderViewRecyclerAdapter;
import com.example.jzg.myapplication.recyclerview.RvAdapter;
import com.example.jzg.myapplication.view.CustomRecyclerView;

import java.util.ArrayList;

/**
 * Created by 李波 on 2017/9/4.
 *
 * recyclerView 双列表联动流畅最终效果 核心，滚动监听实现联动
 */
public class DoubleRecyclerViewActivity extends AppCompatActivity {

    RecyclerView contentListViewLeft,contentListViewRight;
    private RvAdapter rvAdapterLeft;
    private RvAdapter rvAdapterRight;
    ArrayList<String> list;
    private LinearLayoutManager layoutManagerLeft;
    private LinearLayoutManager layoutManagerRight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_double_recyclerview);
        list = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            list.add(i + "");
        }
        contentListViewLeft = (RecyclerView) findViewById(R.id.contentListViewLeft);
        contentListViewRight = (RecyclerView) findViewById(R.id.contentListViewRight);

        layoutManagerLeft = new LinearLayoutManager(this);
        layoutManagerRight = new LinearLayoutManager(this);


        rvAdapterLeft = new RvAdapter(this, list);
        rvAdapterRight = new RvAdapter(this, list);


        contentListViewLeft.setLayoutManager(layoutManagerLeft);  //设置布局管理器
        contentListViewRight.setLayoutManager(layoutManagerRight);  //设置布局管理器
//        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL)); //设置分割线


        contentListViewLeft.setAdapter(rvAdapterLeft);
        contentListViewRight.setAdapter(rvAdapterRight);


        syncScroll(contentListViewLeft, contentListViewRight);


    }

    /**
     * 实现左右列表联动效果
     * 当一个列表滑动的时候 禁止另个一列表触碰滑动，否则会造成崩溃
     * @param leftList
     * @param rightList
     */
    private void syncScroll(final RecyclerView leftList, final RecyclerView rightList) {

        leftList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    rightList.scrollBy(dx, dy);
                }

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING){
                    ((CustomRecyclerView)rightList).isOnTouch=false;
                }else if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    ((CustomRecyclerView)rightList).isOnTouch=true;
                }

            }
        });

        rightList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    leftList.scrollBy(dx, dy);
                }

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING){
                    ((CustomRecyclerView)leftList).isOnTouch=false;
                }else if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    ((CustomRecyclerView)leftList).isOnTouch=true;
                }


            }
        });

    }

}