package com.example.jzg.myapplication.http;



import com.example.jzg.myapplication.bean.Upload;
import com.example.jzg.myapplication.bean.User;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by 李波 on 2017/1/20.
 *
 * 本App相关网络请求接口（ 基于 rxjava + okhttp3 + retrofit 架构下的 ）
 */
public interface ApiServer {


    @FormUrlEncoded
    @POST("/api/FeedBack/Add")
    public Observable<ResponseJson<Object>> reqCorrector(@FieldMap Map<String, String> params);
    //-----------------------


    @FormUrlEncoded
    @POST("/api/user/login")
    public Observable<ResponseJson<User>> login(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/api/user/token")
    public Observable<ResponseJson<User>> token(@FieldMap Map<String, String> params);

    /**
     * Created by 李波 on 2017/4/1.
     * 上传图片
     */
    @Multipart
    @POST("/App/UserHandler.ashx")
    Observable<ResponseJson<User>> upLoadSingle(@PartMap Map<String, RequestBody> params);

    /**
     * Created by 李波 on 2017/4/1.
     * 上传大文件
     */
    @POST("/api/PictrueIdent/UploadFileStep")
    @Multipart
//    Observable<ResponseJson<String>> uploadFileInfo(@FieldMap Map<String, String> options) ;
    Observable<Upload> uploadFileInfo(@QueryMap Map<String, String> options,
                                      @PartMap Map<String, RequestBody> externalFileParameters) ;
}
