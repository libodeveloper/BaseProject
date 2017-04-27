package com.example.jzg.myapplication.presenter;
import com.example.jzg.myapplication.base.BasePresenter;
import com.example.jzg.myapplication.bean.User;
import com.example.jzg.myapplication.http.ApiManager;
import com.example.jzg.myapplication.http.ResponseJson;
import com.example.jzg.myapplication.http.ResponseSubscriber;
import com.example.jzg.myapplication.http.RxThreadUtil;
import com.example.jzg.myapplication.mvpview.IUpload;
import com.example.jzg.myapplication.utils.LogUtil;
import com.example.jzg.myapplication.utils.MD5Utils;
import com.example.jzg.myapplication.utils.UIUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;


/**
 * Created by 李波 on 2017/1/20.
 *
 * mvp 模式 Presenter层的网络请求
 */

public class UploadPresenter extends BasePresenter<IUpload> {
    public UploadPresenter(IUpload from) {
        super(from);
    }

    //上传图片
    public void upLoadImage(String userid, String path) {

        File imgFile = new File(path);
        Map<String, String> params = new HashMap();
        params.put("userid", userid);
        params.put("op", "editHeadPic");
        params.put("tokenid", "6");
        Map<String, Object> signMap = new HashMap();
        signMap.putAll(params);

        //封装上传文件----------（多图片上传继续往下封装上传文件）--------------------------------------------------------------
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), imgFile);
//        RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), imgFile1);
//        RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), imgFile2);

        //封装上传附带参数
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), userid);
        RequestBody p = RequestBody.create(MediaType.parse("text/plain"), "editHeadPic");
        RequestBody sign = RequestBody.create(MediaType.parse("text/plain"), MD5Utils.getMD5Sign(signMap));
        //-----------------------------------------------------------------------------------------

        Map<String, RequestBody> params1 = new HashMap();
        params1.put("image\"; filename=\"" + imgFile.getName() + "", fileBody);
        params1.put("userId", userId);
        params1.put("op", p);
        params1.put("sign", sign);
        LogUtil.e(TAG, UIUtils.getUrl(params));

        ApiManager.getApiServer().upLoadSingle(params1).compose(RxThreadUtil
                .<ResponseJson<User>>networkSchedulers()).subscribe(
                new ResponseSubscriber<ResponseJson<User>>(baseView) {
            @Override
            public void onSuccess(ResponseJson<User> response) {
                User user = response.getMemberValue();
                baseView.uploadSucceed(user);
            }
        });

    }




}
