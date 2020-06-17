package com.example.jzg.myapplication.cameradetails;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.jzg.myapplication.R;
import com.example.jzg.myapplication.utils.ImageCompressor;
import com.example.jzg.myapplication.dialog.ActionSheet;
import com.example.jzg.myapplication.dialog.DialogUtil;
import com.example.jzg.myapplication.global.Constants;
import com.example.jzg.myapplication.utils.FileUtils;
import com.example.jzg.myapplication.utils.ImageUtil;
import com.example.jzg.myapplication.utils.LogUtil;
import com.example.jzg.myapplication.utils.MyToast;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;

import rx.functions.Action1;

/**
 * Created by libo on 2020/5/19.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 相机Demo
 */
public class DemoActivity extends AppCompatActivity implements ActionSheet.OnActionSheetSelected {

    private static final String TAG = DemoActivity.class.getSimpleName();
    ImageView iv1,iv2;

    private final int ONE = 111;
    private final int TWO = 222;

    private String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +"000"+ File.separator;


    private int flag = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        iv1 = (ImageView) findViewById(R.id.iv1);
        iv2 = (ImageView) findViewById(R.id.iv2);


        if (Build.VERSION.SDK_INT >= 23) { //如果系统版本号大于等于23 也就是6.0，就必须动态请求敏感权限（也要配置清单）
            RxPermissions.getInstance(this).request(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA).subscribe(new Action1<Boolean>() {
                @Override
                public void call(Boolean granted) {
                    if (granted) { //请求获取权限成功后的操作

                    } else {
                        MyToast.showShort("需要获取SD卡读取权限来保存图片");
                    }
                }
            });
        } else { //否则如果是6.0以下系统不需要动态申请权限直接配置清单就可以了

        }


        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 0;
                ActionSheet.showSheet(DemoActivity.this, DemoActivity.this, "从相册选取", "拍照", "取消");

            }
        });

        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 1;
                ActionSheet.showSheet(DemoActivity.this, DemoActivity.this, "从相册选取", "拍照", "取消");
            }
        });

    }

    private void  camera(String path, int tag) {
        Intent intent = new Intent(DemoActivity.this, CameraActivity.class);
        intent.putExtra("picPath", path);
        startActivityForResult(intent, tag);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {

            String picPath = data.getStringExtra("picPath");

            switch (requestCode) {
                case Constants.PIC_PHOTO:
                    DialogUtil.showDialog(this,"正在压缩图片...");
                    Uri uri = data.getData();
                    String path = FileUtils.getRealFilePath(this,uri);

                    long time = System.currentTimeMillis();
                    String savePath =  dirPath+"xc_"+time+".jpg";

                    //获取角度
                    int degree = ImageUtil.readPictureDegree(path);
                    LogUtil.e(TAG,"degree:"+degree+",picPath:"+path);

                    Bitmap processedBitmap = ImageCompressor.loadBitmapFile(path);
                    if(degree!=0){
                        processedBitmap = ImageCompressor.rotateBitmap(processedBitmap,180-degree);
                    }

                    int  quality = 80;//默认压缩系数

                    boolean saved = ImageCompressor.save(processedBitmap, savePath, Bitmap.CompressFormat.JPEG, quality);

                    if (saved){
                        MyToast.showLong("相册图片保存成功");

                        if (flag == 0){
                            iv1.setImageURI(Uri.fromFile(new File(savePath)));
                        }else if (flag == 1){
                            iv2.setImageURI(Uri.fromFile(new File(savePath)));
                        }

                    }

                    DialogUtil.dismissDialog();

                    break;
                case ONE:
                    iv1.setImageURI(Uri.fromFile(new File(picPath)));
                    break;
                case TWO:
                    iv2.setImageURI(Uri.fromFile(new File(picPath)));
                    break;
            }
        }


    }

    @Override
    public void onWhichClick(int whichButton) {
        switch (whichButton) {
            case ActionSheet.PHOTO: //相册选择  -> 李波 on 2017/3/29.

                String status = Environment.getExternalStorageState();
                if (status.equals(Environment.MEDIA_MOUNTED)) {

                    Intent in = new Intent(Intent.ACTION_PICK, null);
                    in.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Constants
                            .IMAGE_UNSPECIFIED);
                    startActivityForResult(in, Constants.PIC_PHOTO);
                } else {
                    Toast.makeText(this, "没有储存卡", Toast.LENGTH_LONG).show();
                }

                break;
            case ActionSheet.CAMERA: //拍照  -> 李波 on 2017/3/29.

                String path = "";
                int tag = 0;

                long time = System.currentTimeMillis();

                if (flag == 0){
                    path = dirPath+"iv1_"+time+".jpg";
                    tag = ONE;
                }else if (flag == 1){
                    path = dirPath+"iv2_"+time+".jpg";
                    tag = TWO;
                }

                camera(path, tag);

                break;
        }
    }
}
