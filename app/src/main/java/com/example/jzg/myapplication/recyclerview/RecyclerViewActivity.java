package com.example.jzg.myapplication.recyclerview;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.jzg.myapplication.R;

import java.util.ArrayList;

/**
 * Created by 李波 on 2017/1/20.
 *
 * recyclerView 示例 ，附带了上拉加载 ，下拉刷新
 */
public class RecyclerViewActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    HeaderViewRecyclerAdapter headerViewRecyclerAdapter;
    private RvAdapter rvAdapter;
    ArrayList<String> list;
//    RecyclerView.LayoutManager layoutManager;
    private LinearLayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i+"");
        }
        recyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);

        //设置下拉刷新  -> 李波 on 2017/1/20.
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);//SwipeRefreshLayout.OnRefreshListener
        swipeRefreshLayout.setColorSchemeResources(
                R.color.google_blue,
                R.color.google_green,
                R.color.google_red,
                R.color.google_yellow
        );
        //Listview 效果
//        layoutManager = new LinearLayoutManager(this);

        //GridView 效果
        layoutManager = new GridLayoutManager(this,2);

         //横向滑动效果

//      layoutManager = new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.HORIZONTAL);


        rvAdapter = new RvAdapter(this,list);

        rvAdapter.setOnItemClickLitener(new RvAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(RecyclerViewActivity.this,"onItemClick"+position,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                rvAdapter.removeData(position);
                Toast.makeText(RecyclerViewActivity.this,"onItemLongClick"+position,Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setLayoutManager(layoutManager);  //设置布局管理器
//        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL)); //设置分割线
        recyclerView.setItemAnimator(new DefaultItemAnimator()); //设置删除item是的动画


        /**
         * 添加 header 和 footer 的核心代码
         * 核心代码：RecyclerView.Adapter的adapter作为参数传入初始化的HeaderViewRecyclerAdapter里
         * 然后headerViewRecyclerAdapter 作为 RecyclerView的adapter，也是在这一步完成绑定headerView 和 footerView
         */
//        headerViewRecyclerAdapter = new HeaderViewRecyclerAdapter(rvAdapter);
//        createLoadMoreView();
//        recyclerView.setAdapter(headerViewRecyclerAdapter);
        recyclerView.setAdapter(rvAdapter);



        /**
         * Created by 李波 on 2017/1/20.
         *
         * 设置上拉加载更多的监听
         */
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && scrollToBottom() /*&& !isRefresh*/) {
//                    isRefresh = true;
//                    if (mOnStateChangeLstener != null) {
//                        setAdapterLastState(RefreshRecycleView.STATE_LOADING);
//                        mOnStateChangeLstener.onBottom();
//                    }
                    loadMore();
                    System.out.println("loadmore.............................................");
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }
    //判断是否到了底部
    private boolean scrollToBottom(){
        if (layoutManager != null && layoutManager.canScrollVertically()) {
            if(layoutManager.getItemCount()>9) { //9 代表滑动到那个位置开始加载，一般如果一页10个，那么久滑动到10的时候就加载下一页
                return !recyclerView.canScrollVertically(1);
            }else{
                return false;
            }
        } else {
            return !recyclerView.canScrollHorizontally(1);
        }
    }

    @Override
    public void onRefresh() {
        reFresh();

    }

    /**
     * Created by 李波 on 2017/1/20.
     *
     * 下啦刷新
     */
    private void reFresh(){
        Toast.makeText(this,"下拉刷新",Toast.LENGTH_SHORT).show();
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(2000);
                    list.add(0, "下拉刷新出来的数据");
                }catch (Exception e){

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false); //刷新后 关闭circleview 加载动画
                        rvAdapter.notifyDataSetChanged();
                    }
                });

            }
        }.start();

    }

    /**
     * Created by 李波 on 2017/1/20.
     *
     * 上拉加载更多
     */
    private void loadMore(){

        Toast.makeText(RecyclerViewActivity.this,"上拉加载更多",Toast.LENGTH_SHORT).show();
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {

                    list.add(""+(list.size()+1));
                    Thread.sleep(2000);
                }catch (Exception e){

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        rvAdapter.notifyDataSetChanged();
                    }
                });

            }
        }.start();

    }

    /**
     * Created by 李波 on 2017/1/20.
     *
     * 初始化 headerView 和 FooterView
     */
    private void createLoadMoreView() {
        View loadMoreView = LayoutInflater
                .from(RecyclerViewActivity.this)
                .inflate(R.layout.rv_more_progress, recyclerView, false);
        headerViewRecyclerAdapter.addFooterView(loadMoreView);

    }

}
