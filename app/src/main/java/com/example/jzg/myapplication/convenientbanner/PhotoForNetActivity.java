package com.example.jzg.myapplication.convenientbanner;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.ImageUtils;
import com.blankj.utilcode.utils.ScreenUtils;
import com.example.jzg.myapplication.R;
import com.example.jzg.myapplication.bean.BannerBean;
import com.example.jzg.myapplication.view.ExtendedViewPager;
import com.example.jzg.myapplication.view.SetPolyToPoly;
import com.example.jzg.myapplication.view.SingleTouchView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.photodraweeview.PhotoDraweeView;

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
    @BindView(R.id.btSave)
    Button btSave;
    @BindView(R.id.poly)
    SetPolyToPoly poly;
    @BindView(R.id.SingleTouchView)
    SingleTouchView SingleTouchView;
    @BindView(R.id.btDang)
    Button btDang;
    private int position;

    int topH;
    int num = 0;
    ExtendedViewPager1Adapter extendedViewPager1Adapter;

    float tempX;
    float tempY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //去掉状态栏，让界面全屏

        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);

        picList = (ArrayList<BannerBean>) getIntent().getSerializableExtra("piclists");
        position = getIntent().getIntExtra("position", 0);
        extendedViewPager1Adapter = new ExtendedViewPager1Adapter(this, picList);
        extendedVP.setAdapter(extendedViewPager1Adapter);

        extendedVP.setCurrentItem(position);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        poly.setTestPoint(4);

        extendedVP.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                PhotoForNetActivity.this.position = position;
                setShoePerNum(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setShoePerNum(position);
    }

    @OnClick({R.id.btSave, R.id.btDang})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btSave:
                Bitmap bitmap  = BitmapFactory.decodeResource(getResources(),
                        picList.get(position).getBannerPath());
//                Bitmap bitmap  = ((BitmapDrawable)iv.getDrawable()).getBitmap();
//                Bitmap bitmap2= ImageUtils.view2Bitmap(SingleTouchView);
//                topH = (1920 - bitmap.getHeight())/2;

                double bitmapsrcW = (double) bitmap.getWidth();
                double bitmapsrcH = (double) bitmap.getHeight();
                double screenH = (double) ScreenUtils.getScreenHeight();
                double screenW = (double) ScreenUtils.getScreenWidth();

                double sacle = screenW/bitmapsrcW;

                double dtsH  = bitmapsrcH * sacle;

                topH = (int)((screenH - dtsH)/2);

                int left = 0;
                int top  = topH;
                int right = (int) screenW;
                int bottom = (int)(dtsH);

                saveImage(this,left,top,right,bottom);

//                Bitmap bitmap1 = ImageUtils.view2Bitmap(SingleTouchView);
//                mergeBitmap(bitmap, bitmap1);


//                mergeBitmap2(bitmap,(float)sacle);
//                int w = extendedViewPager1Adapter.getImageView().getWidth();
//                int h = extendedViewPager1Adapter.getImageView().getHeight();
//
//                Log.i("wh", "w = " + w + "  h = " + h);
//                Log.i("wh", "bitmap w = " + bitmap.getWidth() + " bitmap h = " + bitmap.getWidth());
//                rl.setVisibility(View.GONE);
                break;
            case R.id.btDang:
                SingleTouchView.setVisibility(View.VISIBLE);
//                extendedViewPager1Adapter.loadResDrawablePic(this,photoImg,picList.get(position).getBannerPath());
                break;

        }
    }

    private void setShoePerNum(int num) {
        num++;
        if (num <= 0) num = 1;
        title.setText(num + "/" + picList.size());
    }

    public void goBack(View view) {
        finish();
    }

    private Bitmap mergeBitmap(Bitmap firstBitmap, Bitmap secondBitmap) {
        Bitmap bitmap = Bitmap.createBitmap(firstBitmap.getWidth(), firstBitmap.getHeight(), firstBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(firstBitmap, new Matrix(), null);
        canvas.drawBitmap(secondBitmap, SingleTouchView.getLeft(), SingleTouchView.getTop()-topH ,null);
        //将合并后的bitmap3保存为png图片到本地
        try {
            FileOutputStream out = new FileOutputStream(Environment.getExternalStorageDirectory() + File.separator + "0000000.png");
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {

        }

        return bitmap;
    }

    private Bitmap mergeBitmap2(Bitmap firstBitmap,float sacle) {
        Bitmap bitmap = Bitmap.createBitmap(firstBitmap.getWidth(), firstBitmap.getHeight(), firstBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(firstBitmap, new Matrix(), null);

        float ts =1.75f;

       float tx = extendedViewPager1Adapter.getImageView().getTranslationX();
       float ty = extendedViewPager1Adapter.getImageView().getTranslationY();

//        float tempSacle = sacle * 3f;
        float tempSacle = sacle * ts;

        double dtsH  = firstBitmap.getHeight() * tempSacle;
        double screenH = (double) ScreenUtils.getScreenHeight() ;

        topH = (int)((screenH - dtsH)/2);

        Path path =new Path();
        float x =  SingleTouchView.getLeft()+tx/tempSacle;
        float y = (SingleTouchView.getTop()-topH)/tempSacle;
        path.moveTo(x,y);
        path.lineTo(x+100f,y);
        path.lineTo(x+100f,y+100f);
        path.lineTo(x,y+100f);
//        path.lineTo(200f,100f);
        path.close();

        Paint  pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setStrokeWidth(50);
        pointPaint.setColor(0xffd19165);
        pointPaint.setStrokeCap(Paint.Cap.ROUND);

        canvas.drawPath(path, pointPaint);

        //将合并后的bitmap3保存为png图片到本地
        try {
            FileOutputStream out = new FileOutputStream(Environment.getExternalStorageDirectory() + File.separator + "11111111.png");
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {

        }

        return bitmap;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                 tempX = event.getRawX();
                 tempY = event.getRawY();
                break;
        }
        return super.onTouchEvent(event);
    }


//    //(x,y)是否在view的区域内
//    private boolean isTouchPointInView(View view, int x, int y) {
//        if (view == null) {
//            return false;
//        }
//        int[] location = new int[2];
//        view.getLocationOnScreen(location);
//        int left = location[0];
//        int top = location[1];
//        int right = left + view.getMeasuredWidth();
//        int bottom = top + view.getMeasuredHeight();
//        //view.isClickable() &&
//        if (y >= top && y <= bottom && x >= left
//                && x <= right) {
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        int x = (int) ev.getRawX();
//        int y = (int) ev.getRawY();
//        if (isTouchPointInView(poly, x, y)) {
////            iosInterceptFlag = true;
//            return super.dispatchTouchEvent(ev);
//        }else {
//            return false;
//        }
//        //do something
//    }

    /**
     * 屏幕指定区域截屏生成图片
     * 耗时操作单开线程
     * @param activity 当前Activity
     */
//    public  void saveImage(final Activity activity,final View v) {
    public  void saveImage(final Activity activity, final int left, final int top, final int right, final int bottom) {

        new Thread() {
            public void run() {
                Bitmap bitmap;

                //测试路径
//                String pathSD = Environment.getExternalStorageDirectory()+"/0";
//                if (!FileUtils.isFileExists(pathSD)){
//                    File file = new File(pathSD);
//                    file.mkdirs();
//                }
//                String path =  pathSD + File.separator + "BMWFACADE.png";

                //正式路径
                String path =  Environment.getExternalStorageDirectory() + File.separator + "aaaaaaaaaaaaaa.png";
                View view = activity.getWindow().getDecorView();
                view.setDrawingCacheEnabled(true);
                view.buildDrawingCache();
                bitmap = view.getDrawingCache();
                Rect frame = new Rect();
                activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                int[] location = new int[2];
//                v.getLocationOnScreen(location);

                try {
//                    bitmap = Bitmap.createBitmap(bitmap, location[0], location[1], v.getWidth(), v.getHeight());
                    bitmap = Bitmap.createBitmap(bitmap, left,top,right,bottom);
                    FileOutputStream fout = new FileOutputStream(path);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fout);


                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("IMAGE", "生成预览图片失败：" + e);
                    if (num<2) {
                        num++;
//                        saveImage(PhotoForNetActivity.this, v);
                        saveImage(PhotoForNetActivity.this, left,top,right,bottom);
                    }else
                        Log.e("IMAGE", "已超过 3 次" + e);

                }finally {
                    // 清理缓存
                    view.destroyDrawingCache();
//                    rl.setVisibility(View.GONE);
                }
            };
        }.start();

    }

}
