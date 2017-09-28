package com.example.jzg.myapplication.convenientbanner;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jzg.myapplication.R;
import com.example.jzg.myapplication.bean.BannerBean;
import com.example.jzg.myapplication.view.ExtendedViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 李波 on 2017/5/3.
 * 浏览大图（包括单张 和 多张） 支持放大缩小
 */
public class PhotoForNetActivity extends AppCompatActivity {


    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.extendedVP)
    ExtendedViewPager extendedVP;

    ArrayList<BannerBean> picList;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager
                .LayoutParams.FLAG_FULLSCREEN); //去掉状态栏，让界面全屏

        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);
        picList = (ArrayList<BannerBean>) getIntent().getSerializableExtra("piclists");
        position = getIntent().getIntExtra("position", 0);
        extendedVP.setAdapter(new ExtendedViewPager1Adapter(this, picList));
        extendedVP.setCurrentItem(position);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        extendedVP.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setShoePerNum(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setShoePerNum(position);
    }

    private void setShoePerNum(int num) {
        num++;
        if (num <= 0) num = 1;
        title.setText(num + "/" + picList.size());
    }

    public void goBack(View view){
        finish();
    }

}
