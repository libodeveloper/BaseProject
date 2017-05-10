package com.example.jzg.myapplication.cameracustom;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jzg.myapplication.R;
import com.example.jzg.myapplication.bean.PictureItem;
import com.example.jzg.myapplication.global.Constants;
import com.example.jzg.myapplication.utils.FrescoCacheHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by libo on 2017/5/8.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 拍照演示Demo
 */
public class DemoCameraActivity extends AppCompatActivity {
    public static final int PHOTO_REQUEST = 10;
    public static final int PHOTO_BIG_PHOTO = 11;
    public int capture_type; //拍照模式
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ivLeft)
    ImageView ivLeft;
    @BindView(R.id.ivRight)
    ImageView ivRight;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.bt_single)
    Button btSingle;
    @BindView(R.id.bt_more)
    Button btMore;
    @BindView(R.id.bt_max)
    Button btMax;

    private int curPosition;
    private LinearLayoutManager layoutManager;
    DemoCameraAdapter demoCameraAdapter;
    private ArrayList<PictureItem> picDatas = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_camera_layout);
        ButterKnife.bind(this);
        tvTitle.setText("拍照测试");
        initData();
        setListener();

    }

    public void goBack(View view) {
        finish();
    }

    private void initData() {
        capture_type = Constants.CAPTURE_TYPE_MULTI;  //默认连拍
        //GridView 效果
        layoutManager = new GridLayoutManager(this, 3);

        //构造照片数据
        for (int i = 0; i < 10; i++) {
            picDatas.add(new PictureItem(i + "", "第" + i + "张", ""));
        }

        demoCameraAdapter = new DemoCameraAdapter(this, picDatas);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(demoCameraAdapter);
    }

    private void setListener() {
        demoCameraAdapter.setOnItemClickLitener(new DemoCameraAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                curPosition = position;
                String path = picDatas.get(position).getPicPath();

                //如果当前图片的地址为空说明没拍照，启动拍照，否则浏览大图  -> 李波 on 2017/5/9.
                if (TextUtils.isEmpty(path)) {
                    costumCamera(curPosition, capture_type, "111", picDatas);
                } else {
                    showBigPic();
                }

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    /**
     * Created by 李波 on 2017/5/9.
     * 显示大图
     */
    private void showBigPic() {
        Intent intent = new Intent(this, PictureZoomActivity.class);
        intent.putExtra("pictureItems", picDatas);
        intent.putExtra("curPosition", curPosition);
        intent.putExtra("url", picDatas.get(curPosition).getPicPath());
        intent.putExtra("showRecapture", true); //连拍情况下 预留大图时，是否显示重拍按钮
        startActivityForResult(intent, PHOTO_BIG_PHOTO);
    }


    @OnClick({R.id.bt_single, R.id.bt_more,R.id.bt_max})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_single: //单拍
                capture_type = Constants.CAPTURE_TYPE_SINGLE;
                break;
            case R.id.bt_more:  //连拍
                capture_type = Constants.CAPTURE_TYPE_MULTI;
                break;
            case R.id.bt_max:  //无限连拍
                capture_type = Constants.CAPTURE_TYPE_MAX;
                costumCamera(0,capture_type,"111",null);
                break;
        }
    }

    /**
     * 跳转自定义相机
     *
     * @param position     当前点击的图片位置
     * @param capture_type 拍摄模式，是单拍还是连拍
     * @param taskId       当前任务 Id 用于生成拍照后的存储目录
     * @param pictureItems 当前点击图片的图片集合
     */
    public void costumCamera(int position, int capture_type, String taskId,
                             ArrayList<PictureItem> pictureItems) {
        Intent intent = new Intent(this, CameraActivity.class);
        intent.putExtra("showGallery", true);//是否显示从相册选取的图标
        intent.putExtra("taskId", taskId);
        intent.putExtra(Constants.CAPTURE_TYPE, capture_type);//拍摄模式，是单拍还是连拍

        if (capture_type == Constants.CAPTURE_TYPE_MULTI) {   //连拍
            intent.putExtra("picList", pictureItems);
            intent.putExtra("position", position);
        } else if (capture_type == Constants.CAPTURE_TYPE_SINGLE){ //单拍
            ArrayList<PictureItem> singleList = new ArrayList<>();
            PictureItem pictureItem = new PictureItem();
            pictureItem.setPicId(pictureItems.get(position).getPicId());
            pictureItem.setPicPath(pictureItems.get(position).getPicPath());
            pictureItem.setPicName(pictureItems.get(position).getPicName());
            singleList.add(pictureItem);
            intent.putExtra("picList", singleList);
            intent.putExtra("position", 0);
        }else if (capture_type == Constants.CAPTURE_TYPE_MAX){  //无限连拍

        }
        startActivityForResult(intent, PHOTO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PHOTO_REQUEST:
                if (data != null) {
                    int captureType = data.getIntExtra(Constants.CAPTURE_TYPE, Constants
                            .CAPTURE_TYPE_MULTI);
                    ArrayList<PictureItem> picList = data.getParcelableArrayListExtra("picList");
                    if (picList != null && picList.size() > 0) {
                        if (captureType == Constants.CAPTURE_TYPE_MULTI) {  //连拍
                            //拍照成功后清理Fresco缓存否始终会显示缓存图片
                            for (int i = 0; i < picList.size(); i++) {
                                FrescoCacheHelper.clearSingleCacheByUrl(picList.get(i).getPicPath
                                        (), false);
                            }
                            picDatas = picList;
                            demoCameraAdapter.setData(picDatas);
                        } else { //单拍
                            FrescoCacheHelper.clearSingleCacheByUrl(picList.get(0).getPicPath(),
                                    false);
                            picDatas.get(curPosition).setPicPath(picList.get(0).getPicPath());
                            demoCameraAdapter.notifyItem(curPosition, picDatas);
                        }
                    }
                }
                break;
            case PHOTO_BIG_PHOTO:
                if (data != null) {
                    boolean recapture = data.getBooleanExtra("recapture", false);

                    int curPicPos = data.getIntExtra("recapturePosition", 0);
                    if (recapture) { //重拍某张
//                        FrescoCacheHelper.clearSingleCacheByUrl(picDatas.get(curPicPos)
// .getPicPath(),false);
//                        pictureItems.get(curPicPos).setPicPath("");
                        costumCamera(curPicPos, Constants.CAPTURE_TYPE_SINGLE, "111", picDatas);
                    }

                    break;
                }
        }
    }
}
