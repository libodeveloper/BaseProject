package com.example.jzg.myapplication.upload;

import android.os.Bundle;
import android.view.View;

import com.example.jzg.myapplication.R;
import com.example.jzg.myapplication.base.BaseActivity;
import com.example.jzg.myapplication.bean.User;
import com.example.jzg.myapplication.mvpview.IUpload;
import com.example.jzg.myapplication.presenter.UploadBigFilePresenter;
import com.example.jzg.myapplication.presenter.UploadPresenter;
import com.example.jzg.myapplication.utils.MyToast;

/**
 * Created by libo on 2017/4/1.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 大文件上传（因过大，不能一次性传，所以需要切片上传，以便断后可以接着传后续的切片）
 */
public class UploadBigFileDemoActivity extends BaseActivity<UploadBigFilePresenter> implements IUpload {

    @Override
    protected UploadBigFilePresenter createPresenter() {
        return new UploadBigFilePresenter(this);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_upload_big_file_layout);
    }

    @Override
    protected void setData() {
        setTitle("大文件上传");
    }

    public void upload(View view){
        MyToast.showShort("大文件切片上传");
        //不管是最开始 还是续传都是调用此两步方法
//        mPresenter.initData("666");
//        mPresenter.zipUpload(this);
    }

    @Override
    public void uploadSucceed(User user) {

    }

    @Override
    public void uploadFail() {

    }

}
