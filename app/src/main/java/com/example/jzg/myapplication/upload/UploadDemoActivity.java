package com.example.jzg.myapplication.upload;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.jzg.myapplication.R;
import com.example.jzg.myapplication.app.SysApplication;
import com.example.jzg.myapplication.base.BaseActivity;
import com.example.jzg.myapplication.bean.User;
import com.example.jzg.myapplication.dialog.ActionSheet;
import com.example.jzg.myapplication.global.Constants;
import com.example.jzg.myapplication.mvpview.IUpload;
import com.example.jzg.myapplication.presenter.UploadPresenter;
import com.example.jzg.myapplication.utils.FrescoCacheHelper;
import com.example.jzg.myapplication.utils.MyToast;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by libo on 2017/3/29.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 图片选择上传
 */
public class UploadDemoActivity extends BaseActivity<UploadPresenter> implements ActionSheet
        .OnActionSheetSelected, IUpload {

    @BindView(R.id.btUpload)
    Button btUpload;
    @BindView(R.id.ivAvatar)
    SimpleDraweeView ivAvatar;

    private String tempPicPath;     //拍照保存的临时图片路径
    private String tempZoomPicPath; //拍照保存压缩后的临时图片路径

    @Override
    protected UploadPresenter createPresenter() {
        return new UploadPresenter(this);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_upload);

    }

    @Override
    protected void setData() {
        setTitle("上传图片");
        createMkdirs();

        tempPicPath = Constants.APP_EXTERNAL_PATH + "/temp.jpg";
        tempZoomPicPath = Constants.APP_EXTERNAL_PATH + "/tempZoom.jpg";
    }

    private void createMkdirs() {
        //初始化图片存储文件夹
        if (Build.VERSION.SDK_INT >= 23) { //如果系统版本号大于等于23 也就是6.0，就必须动态请求敏感权限（也要配置清单）
            RxPermissions.getInstance(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(new Action1<Boolean>() {
                @Override
                public void call(Boolean granted) {
                    if (granted) { //请求获取权限成功后的操作
                        mkdirs();
                    } else {
                        MyToast.showShort("需要获取SD卡读取权限来保存图片");
                    }
                }
            });
        } else { //否则如果是6.0以下系统不需要动态申请权限直接配置清单就可以了
            mkdirs();
        }
    }

    private void mkdirs() {
        File file = new File(Constants.APP_EXTERNAL_PATH);
        if (!file.exists()) file.mkdirs();
    }

    public void upload(View view) {
        ActionSheet.showSheet(this, this, "从相册选取", "拍照", "取消");

    }

    @Override
    public void onWhichClick(int whichButton) {
        switch (whichButton) {
            case ActionSheet.PHOTO: //相册选择  -> 李波 on 2017/3/29.

                String status = Environment.getExternalStorageState();
                if (status.equals(Environment.MEDIA_MOUNTED)) {

                    createMkdirs();

                    Intent in = new Intent(Intent.ACTION_PICK, null);
                    in.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Constants
                            .IMAGE_UNSPECIFIED);
                    startActivityForResult(in, Constants.PIC_PHOTO);
                } else {
                    Toast.makeText(this, "没有储存卡", Toast.LENGTH_LONG).show();
                }

                break;
            case ActionSheet.CAMERA: //拍照  -> 李波 on 2017/3/29.

                createMkdirs();

                //Manifest.permission.CAMERA 相机权限
                //Manifest.permission.WRITE_EXTERNAL_STORAGE SD卡读写权限
                if (Build.VERSION.SDK_INT >= 23) { //如果系统版本号大于等于23 也就是6.0，就必须动态请求敏感权限（也要配置清单）
                    RxPermissions.getInstance(this).request(Manifest.permission.CAMERA).subscribe
                            (new Action1<Boolean>() {
                        @Override
                        public void call(Boolean granted) {
                            if (granted) { //请求获取权限成功后的操作
                                cameraPic();
                            } else {
                                MyToast.showShort("需要获取SD卡读取权限来保存图片");
                            }
                        }
                    });
                } else { //否则如果是6.0以下系统不需要动态申请权限直接配置清单就可以了
                    cameraPic();
                }

                break;
        }
    }


    /**
     * Created by 李波 on 2017/3/29.
     * 启动相机拍照
     */
    private void cameraPic() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 指定照片保存路径（SD卡），temp.jpg为一个临时文件，每次拍照后这个图片都会被替换
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(tempPicPath)));
        startActivityForResult(cameraIntent, Constants.PIC_CAMERA);
    }


    /**
     * Created by 李波 on 2017/3/29.
     * 裁剪照片
     */
    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent();
        intent.setAction("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");// mUri是已经选择的图片Uri
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);// 输出图片大小
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        //intent.putExtra("return-data", true);
        intent.putExtra("return-data", false);
        //设置压缩后的临时图片路径  -> 李波 on 2017/3/29.
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(tempZoomPicPath)));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, Constants.PIC_ZOOM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Constants.PIC_PHOTO:
                // 从相册传递
                startPhotoZoom(data.getData());
                break;
            case Constants.PIC_CAMERA:
                startPhotoZoom(Uri.fromFile(new File(tempPicPath)));
                break;
            case Constants.PIC_ZOOM:
                //裁切图片后上传图片 上传的是压缩后的图片
//                mPresenter.upLoadImage("6",tempZoomPicPath);
                FrescoCacheHelper.clearSingleCacheByUrl(tempZoomPicPath, false);
                ivAvatar.setImageURI("file://" + tempZoomPicPath);
                break;
        }
    }

    @Override
    public void uploadSucceed(User user) {
//        ivAvatar.setImageURI(user.getHeadPic());
    }

    @Override
    public void uploadFail() {

    }

}
