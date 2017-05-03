package com.example.jzg.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.jzg.myapplication.base.BaseActivity;
import com.example.jzg.myapplication.convenientbanner.BannerActivity;
import com.example.jzg.myapplication.horizontalverticalscroll.AdmixedContrastActivity;
import com.example.jzg.myapplication.popWindow.PopwindowUtils;
import com.example.jzg.myapplication.recyclerview.RecyclerViewActivity;
import com.example.jzg.myapplication.slidingmenu.DrawerLayoutActivity;
import com.example.jzg.myapplication.slidingmenu.ToolbarDrawerLayoutActivity;
import com.example.jzg.myapplication.tablayoutviewpager.TabLayoutViewPagerActivity;
import com.example.jzg.myapplication.tagflowlayout.TagFlowLayoutActivity;
import com.example.jzg.myapplication.upload.UploadBigFileDemoActivity;
import com.example.jzg.myapplication.upload.UploadDemoActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by libo on 2016/12/30.
 *
 * @Email: libo@jingzhengu.com
 * @Description:
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @BindView(R.id.bt_recyclerView)
    Button btRecyclerView;
    @BindView(R.id.bt_tabLayout_ViewPager)
    Button btTabLayoutViewPager;
    @BindView(R.id.bt_TagFlowLayout)
    Button btTagFlowLayout;
    @BindView(R.id.bt_left)
    Button btLeft;
    @BindView(R.id.bt_right)
    Button btRight;
    @BindView(R.id.bt_DrawerLayout_slidingmenu)
    Button btDrawerLayoutSlidingmenu;
    @BindView(R.id.bt_slidingmenu)
    Button btSlidingmenu;
    @BindView(R.id.bt_linkage)
    Button btLinkage;
    @BindView(R.id.bt_selecet_pic)
    Button btSelecetPic;
    @BindView(R.id.bt_bigFile_upload)
    Button btBigFileUpload;
    @BindView(R.id.bt_banner)
    Button btBanner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }


    @OnClick({R.id.bt_recyclerView, R.id.bt_tabLayout_ViewPager, R.id.bt_TagFlowLayout, R.id
            .bt_slidingmenu, R.id.bt_DrawerLayout_slidingmenu, R.id.bt_right, R.id.bt_left, R.id
            .bt_linkage, R.id.bt_selecet_pic, R.id.bt_bigFile_upload,R.id.bt_banner})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_recyclerView:
                Intent i = new Intent(this, RecyclerViewActivity.class);
                startActivity(i);
                break;
            case R.id.bt_tabLayout_ViewPager:
                Intent ii = new Intent(this, TabLayoutViewPagerActivity.class);
                startActivity(ii);
                break;
            case R.id.bt_TagFlowLayout:
                Intent iii = new Intent(this, TagFlowLayoutActivity.class);
                startActivity(iii);
                break;
            case R.id.bt_slidingmenu:
                Intent iiii = new Intent(this, ToolbarDrawerLayoutActivity.class);
                startActivity(iiii);
                break;
            case R.id.bt_DrawerLayout_slidingmenu:
                Intent iiiii = new Intent(this, DrawerLayoutActivity.class);
                startActivity(iiiii);
                break;
            case R.id.bt_right:
                View rootViewR = View.inflate(this, R.layout.activity_main, null);
                PopwindowUtils.popWindowSlidingMenu(this, rootViewR, PopwindowUtils
                        .SLIDINGMENU_RIGHT);
                break;
            case R.id.bt_left:
                View rootViewL = View.inflate(this, R.layout.activity_main, null);
                PopwindowUtils.popWindowSlidingMenu(this, rootViewL, PopwindowUtils
                        .SLIDINGMENU_LEFT);
                break;
            case R.id.bt_linkage:
                Intent i6 = new Intent(this, AdmixedContrastActivity.class);
                startActivity(i6);
                break;
            case R.id.bt_selecet_pic:
                Intent i7 = new Intent(this, UploadDemoActivity.class);
                startActivity(i7);
                break;
            case R.id.bt_bigFile_upload:
                Intent i8 = new Intent(this, UploadBigFileDemoActivity.class);
                startActivity(i8);
                break;
            case R.id.bt_banner:
                Intent i9 = new Intent(this, BannerActivity.class);
                startActivity(i9);
                break;
        }
    }

    float x = 0;
    float y = 0;

    float dx = 0;
    float dy = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                x = event.getRawX();
                y = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                dx = event.getRawX() - x;
                dy = event.getRawY() - y;

                float d = Math.abs(dx) - Math.abs(dy);
                if (d > 20) { //横向滑动
                    if (x >= 0 && x <= 100 && dx > 0 && Math.abs(dx) > 20) {   //左侧
                        View rootViewL = View.inflate(this, R.layout.activity_main, null);
                        PopwindowUtils.popWindowSlidingMenu(this, rootViewL, PopwindowUtils
                                .SLIDINGMENU_LEFT);
                    } else if (dx < 0 && Math.abs(dx) > 100) { //右侧
                        View rootViewR = View.inflate(this, R.layout.activity_main, null);
                        PopwindowUtils.popWindowSlidingMenu(this, rootViewR, PopwindowUtils
                                .SLIDINGMENU_RIGHT);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                x = 0;
                y = 0;

                dx = 0;
                dy = 0;

                break;
        }

        return super.onTouchEvent(event);
    }
}
