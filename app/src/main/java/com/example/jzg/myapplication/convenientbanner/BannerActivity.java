package com.example.jzg.myapplication.convenientbanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.blankj.utilcode.utils.ScreenUtils;
import com.example.jzg.myapplication.R;
import com.example.jzg.myapplication.bean.BannerBean;
import com.example.jzg.myapplication.utils.FrescoImageLoader;
import com.example.jzg.myapplication.view.ClearableEditText;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 李波 on 2017/4/25.
 * banner轮播图
 */
public class BannerActivity extends AppCompatActivity {


    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ivLeft)
    ImageView ivLeft;
    @BindView(R.id.ivRight)
    ImageView ivRight;
    @BindView(R.id.etModify)
    ClearableEditText etModify;

    @BindView(R.id.convenientBanner)
    ConvenientBanner convenientBanner;

    ArrayList<BannerBean> picList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        ButterKnife.bind(this);
        tvTitle.setText("轮播图");

        initView();
        initData();
    }

    private void initView() {
        ViewGroup.LayoutParams params = convenientBanner.getLayoutParams();
        params.width = RadioGroup.LayoutParams.MATCH_PARENT;
        params.height = ScreenUtils.getScreenWidth() * 9 / 16;
        convenientBanner.setLayoutParams(params);

    }

    private void initData() {
        picList.add(new BannerBean("test0",R.drawable.ic_test_0));
        picList.add(new BannerBean("test1",R.drawable.ic_test_1));
        picList.add(new BannerBean("test2",R.drawable.ic_test_2));
        picList.add(new BannerBean("test3",R.drawable.ic_test_3));
        picList.add(new BannerBean("test4",R.drawable.ic_test_4));
        picList.add(new BannerBean("test5",R.drawable.ic_test_5));
        picList.add(new BannerBean("test6",R.drawable.ic_test_6));

        setPicAdapter();
    }

    private void setPicAdapter() {
        convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, picList);

        convenientBanner
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);

        convenientBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {//查看大图
                Intent intent = new Intent(BannerActivity.this, PhotoForNetActivity.class);
                //图片集合
                intent.putExtra("piclists", picList);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
    }


    /**
     * Created by 李波 on 2017/5/3.
     * 轮播图适配器
     */
    class NetworkImageHolderView implements Holder<BannerBean> {
        private SimpleDraweeView imageView;
        private TextView title;

        @Override
        public View createView(Context context) {
            //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
            View view = LayoutInflater.from(context).inflate(R.layout.taskdetail_photo, null);
            imageView = (SimpleDraweeView) view.findViewById(R.id.photo_img);
            title = (TextView) view.findViewById(R.id.title);
            return view;
        }

        @Override
        public void UpdateUI(Context context, int position, BannerBean data) {
            int tag = position + 1;
            title.setText(data.getBannerName() + "(" + tag + "/" + picList.size() + ")");
//            imageView.setImageURI(data.getBannerPath());
            FrescoImageLoader.loadResDrawablePic(BannerActivity.this,imageView,data.getBannerPath());

        }
    }

    public  void goBack(View view){
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        //开启轮播
        convenientBanner.startTurning(2000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //停止轮播
        convenientBanner.stopTurning();
    }

}