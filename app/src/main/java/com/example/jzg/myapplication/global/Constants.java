package com.example.jzg.myapplication.global;

import android.os.Environment;

import com.example.jzg.myapplication.utils.FileUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 李波 on 2017/1/20.
 *
 * 全局类保存一些全局变量
 */
public  class Constants {

    /**
     * Created by 李波 on 2017/5/15.
     * 文件上传的临时地址
     */
    public static final String APP_EXTERNAL_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/uploadTEMP";

    /**
     * Created by 李波 on 2017/5/15.
     * apk更新下载路径
     */
    public static final String UpdataApkPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/BaseProject/Update/";

    public static final String errr_net = "网络请求失败，请检查您的网络";

    /**
     * Created by 李波 on 2017/5/5.
     * 大文件上传
     * 大文件的原始 储存目录 往下还有层 taskid 目录 根据传值动态改变
     */
    public static final String bigFilePath    = FileUtils.SDCARD_PAHT + "/Upload/bigFilePath/";

    /**
     * Created by 李波 on 2017/5/5.
     * 大文件上传
     * zip压缩包的 存储目录 往下还有层 taskid 目录 根据传值动态改变
     */
    public static final String zipCatalogPath = FileUtils.SDCARD_PAHT + "/Upload/zipCatalogPath/";

    /**
     * Created by 李波 on 2016/12/2.
     * frescol 缩略图压缩系数 数值越小 缩略图的大小就越小
     */
    public static final int FRESCOL_COMPRESSION_COEFFICIENT = 100;

    /**
     * Created by 李波 on 2017/3/29.
     * 选择照片相关
     */
    public static final String IMAGE_UNSPECIFIED = "image/*";
    public static final int PIC_PHOTO = 0x00000400;
    public static final int PIC_CAMERA = 0x00000401;
    public static final int PIC_ZOOM = 0x00000402;

    /**
     * 屏幕宽高以备适配使用
     */
    public static int ScreenWidth;
    public static int ScreenHeight;
    //请求成功状态码 100
    public static final int SUCCESS_STATUS_CODE=100;
    //被其他人认领 304
    public static final int CLAIM_STATUS_CODE=304;

    //-----------Acache-------------
    public static final String KEY_ACACHE_USER = "acache_user";


    //---------- Camera -----------------------
    public static final int CAPTURE_TYPE_SINGLE = 10081;//单拍
    public static final int CAPTURE_TYPE_MULTI = 10082;//连拍
    public static final int CAPTURE_TYPE_MAX = 10083;//无限连拍
    public static final String CAPTURE_TYPE = "capture_type";//拍摄模式
    /**
     * Created by 李波 on 2017/5/15.
     * 拍照后的原图保存目录
     * /Pictures/PHOTO/
     */
    public static final String TEMP_TAKE_PHOTO_DIR = "PHOTO";//拍照后保存的位置


    /**
     * Created by 李波 on 2017/5/15.
     * 相机拍摄 压缩后的路径
     */
    public static final String ROOT_DIR = Environment.getExternalStorageDirectory()+"/CameraPhoto";



}
