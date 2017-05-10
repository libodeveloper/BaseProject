package com.example.jzg.myapplication.cameracustom;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.jzg.myapplication.R;
import com.example.jzg.myapplication.bean.PictureItem;
import com.example.jzg.myapplication.dialog.MyUniversalDialog;
import com.example.jzg.myapplication.utils.FrescoImageLoader;
import com.example.jzg.myapplication.view.MyPhotoView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.photodraweeview.PhotoDraweeView;


/**
 * Created by libo on 2016/11/14.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 图片浏览界面 手势可放大缩小
 */
public class PictureZoomActivity extends Activity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.vPager)
    MyPhotoView vPager;
    @BindView(R.id.tvRecapture)
    TextView tvRecapture;
    @BindView(R.id.tvDel)
    TextView tvDel;
    @BindView(R.id.tv_title)
    TextView tv_title;
    private String url;//显示的url或者filePath
    private boolean showRecapture;  //是否需要显示重拍按钮
    private boolean isdel = false;  //是否需要显示删除按钮

    private Context mContext;


    private LayoutInflater inflater;
    private View item;
    private MyViewPagerAdapter picAdapter;
    /**
     * adapter里填充的view集合
     */
    private List<View> list = new ArrayList<View>();

    /**
     * 图片地址集合完整
     */
    private List<PictureItem> items = new ArrayList<PictureItem>();
    /**
     * 图片显示地址集合
     */
    private List<PictureItem> showItems = new ArrayList<PictureItem>();

    //当前点击查看大图位置
    private int curPosition = 0;

    //当前显示位置
    private int curShowPosition  = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        //去除title 标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_picture_zoom);
        ButterKnife.bind(this);
        url = getIntent().getStringExtra("url");
        showRecapture = getIntent().getBooleanExtra("showRecapture", false);
        isdel = getIntent().getBooleanExtra("isDel", false);
        items = getIntent().getParcelableArrayListExtra("pictureItems");
        curPosition = getIntent().getIntExtra("curPosition", 0);

        if (showRecapture) {
            tvRecapture.setVisibility(View.VISIBLE);
        } else {
            tvRecapture.setVisibility(View.GONE);
        }
        if(isdel){
            tvDel.setVisibility(View.VISIBLE);
        }else{
            tvDel.setVisibility(View.GONE);
        }




        if (items != null && items.size()>0) {

            inflater = LayoutInflater.from(getApplicationContext());

            //查询出有图片的地址，放入显示的集合中
            showItems.addAll(items);
            for(int i = 0; i < items.size(); i++){
                if(TextUtils.isEmpty(items.get(i).getPicPath())){
                    showItems.remove(items.get(i));
                }


            }

            for (int i = 0; i < showItems.size(); i++) {
                item = inflater.inflate(R.layout.viewpager_big_pics_item, null);
                list.add(item);
            }
            //如果图片id是数字则排序
//            if(StringHelper.isNumeric(showItems.get(0).getPicId())){
//                Collections.sort(showItems);
//            }

            //遍历显示集合，查询传入的点击位置对应的显示位置
            for (int i = 0;i<showItems.size();i++){
                if(showItems.get(i).getPicId().equals(items.get(curPosition).getPicId())){
                    curShowPosition = i;
                    break;
                }
            }

            picAdapter = new MyViewPagerAdapter(list, mContext, showItems);
            vPager.setAdapter(picAdapter);
            vPager.setCurrentItem(curShowPosition);
            vPager.addOnPageChangeListener(this);
            tv_title.setText(showItems.get(curShowPosition).getPicName());

        }else{
            finish();
        }

    }

    PhotoDraweeView image;

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        curShowPosition = position;
        tv_title.setText(showItems.get(curShowPosition).getPicName());

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class MyViewPagerAdapter extends PagerAdapter {

        private List<View> mList;
        private List<PictureItem> mUrls;
        private Context mContext;

        public MyViewPagerAdapter(List<View> mList, Context mContext, List<PictureItem> mUrls) {
            this.mList = mList;
            this.mContext = mContext;
            this.mUrls = mUrls;
        }

        @Override
        public int getCount() {
            return mUrls.size();
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            container.removeView(mList.get(position));

        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        /**
         * Create the page for the given position.
         */
        @SuppressLint("NewApi")
        @Override
        public Object instantiateItem(final ViewGroup container,
                                      final int position) {

            View view = mList.get(position);
            image = ((PhotoDraweeView) view.findViewById(R.id.viewpager_photoDraweeView));
            FrescoImageLoader.displayImageBig(mContext, image, mUrls.get(position).getPicPath());

            container.removeView(mList.get(position));
            container.addView(mList.get(position));
            return mList.get(position);

        }


    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    private int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     */
    private void rotate(Uri uri, SimpleDraweeView simpleDraweeView) {

        ImageRequest build = ImageRequestBuilder.newBuilderWithSource(uri)
                .setAutoRotateEnabled(true)
                .build();
        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setImageRequest(build)
                .build();
        simpleDraweeView.setController(controller);
    }

    @OnClick({R.id.ivBack,R.id.tvRecapture,R.id.tvDel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.tvRecapture:
                //点击显示的图片与传入集合对应的图片位置
                for (int i = 0;i<items.size();i++){
                    if(showItems.get(curShowPosition).getPicId().equals(items.get(i).getPicId())){
                        curPosition = i;
                        break;
                    }
                }
                Intent intent = getIntent();
                intent.putExtra("recapture", true);
                intent.putExtra("isDel",false);
                //重拍位置
                intent.putExtra("recapturePosition",curPosition);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.tvDel:
                for (int i = 0;i<items.size();i++){
                    if(showItems.get(curShowPosition).getPicId().equals(items.get(i).getPicId())){
                        curPosition = i;
                        break;
                    }
                }
                showConfirmTask();
                break;

        }
    }

    /**
     * 确认任务信息
     */
    public void showConfirmTask(){
        final MyUniversalDialog myUniversalDialog = new MyUniversalDialog(this);
        View view = myUniversalDialog.getLayoutView(R.layout.dialog);
       TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setVisibility(View.GONE);

        TextView tvMsg = (TextView) view.findViewById(R.id.tv_content);
        tvMsg.setText("请确认是否需要删除此照片？");
        Button tvleft = (Button) view.findViewById(R.id.bt_cancle);
        tvleft.setText("否");
        Button tvright = (Button) view.findViewById(R.id.bt_confrim);
        tvright.setText("是");
        myUniversalDialog.setLayoutView(view);
        myUniversalDialog.show();
        tvleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myUniversalDialog.cancel();
            }
        });
        tvright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myUniversalDialog.cancel();
                Intent intent1 = getIntent();
                //删除位置
                intent1.putExtra("recapturePosition",curPosition);
                intent1.putExtra("isDel",true);
                setResult(RESULT_OK,intent1);
                finish();
            }
        });

    }
}
