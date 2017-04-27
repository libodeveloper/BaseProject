package com.example.jzg.myapplication.mvpview;

import com.example.jzg.myapplication.base.IBaseView;
import com.example.jzg.myapplication.bean.User;

/**
 * Created by 李波 on 2017/1/20.
 * 上传接口
 * mvp 模式接口
 */
public interface IUpload extends IBaseView {
    void uploadSucceed(User user);
    void uploadFail();

}
