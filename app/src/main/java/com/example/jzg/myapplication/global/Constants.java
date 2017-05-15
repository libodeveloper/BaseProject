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
    public static final String APP_EXTERNAL_PATH = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/uploadTEMP";

    public static final int STICKER_BTN_HALF_SIZE = 30;
    //车系头部信息位置
    public static final String DateActivityTowaitActivity = "DateActivityTowaitActivity";
    public static final int waitActivityToDateActivity = 0x00000020;
    public static final String ROOT_DIR = Environment.getExternalStorageDirectory()+"/CameraPhoto";

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

    //-----------车型选择-------------
    public static final String KEY_CARBRANDLIST = "brandList";
    /**
     * 确定列表标题和内容是否有重复标记
     */
    public static final String IS_TITLE = "istitle";



    /**
     * 屏幕宽高以备适配使 用
     */
    public static int ScreenWidth;
    public static int ScreenHeight;
    //请求成功状态码 100
    public static final int SUCCESS_STATUS_CODE=100;
    //被其他人认领 304
    public static final int CLAIM_STATUS_CODE=304;

    //-----------Acache-------------
    public static final String KEY_ACACHE_USER = "acache_user";

    //-----附加信息editText保留的小数位数
    public static final int MAXLENGTH = 1;


    public static final int STATUS_NORMAL=0;
    public static final int STATUS_OK=1;
    public static final int STATUS_ABNORMAL=2;

    public static final int CAPTURE_TYPE_SINGLE = 10081;//单拍
    public static final int CAPTURE_TYPE_MULTI = 10082;//连拍
    public static final int CAPTURE_TYPE_MAX = 10083;//无限连拍
    public static final String CAPTURE_TYPE = "capture_type";//拍摄模式
    public static final String TEMP_TAKE_PHOTO_DIR = ".PHOTO";//拍照后保存的位置



    public static final String DATA_TYPE_PLAN="DETECTION_PLAN";//方案数据
    public static final String DATA_TYPE_USE_TASK="DETECTION_TASK";//用户任务数据
    public static final String DATA_TYPE_PROCEDURE="DATA_TYPE_PROCEDURE";//手续信息标识
    public static final String DATA_TYPE_CAR_TYPE="DATA_TYPE_CAR_TYPE";//车型选择标识
    public static final String DATA_TYPE_SUBJOININFO="DATA_TYPE_SUBJOININFO";//附加信息需要提交标识
    public static final String OTHER_INFROMATION="OTHER_INFROMATION";//附加信息初始化数据

    public static final int PROCEDURE_PHOTO_REFRESH = 1600;//手续信息照片更新


    public static Map<String ,String> photoMaps = new HashMap<String ,String>(){{
        put("1","前排座椅");
        put("2","仪表盘");
        put("3","左侧底大边");
        put("4","左前45°");
        put("5","发动机舱");
        put("6","发动机舱左侧");
        put("7","发动机舱右侧");
        put("8","右侧底大边");
        put("9","后排座椅");
        put("10","中控台");
        put("11","右后45°");
        put("12","左后翼子板封胶");
        put("13","右后翼子板封胶");
        put("14","行李箱底板");
        put("15","行李箱底板底部");
        put("16","铭牌");
        put("17","车架VIN");
        put("D001","轻微变形");
        put("D002","变形");
        put("D003","剐蹭");
        put("D004","色差");
        put("D005","划痕");
        put("D006","锈蚀");
        put("D007","破损");
        put("D008","老化");
        put("D009","磨损");
        put("D010","脏污");
        put("D011","异物");
        put("D012","进水");
        put("D013","缝隙不匀");
        put("D014","使用极限");
        put("D015","异常磨损");
        put("D016","爆漆");
        put("D017","流漆");
        put("D018","掉漆");
        put("D019","改装");
        put("D020","缺失");
        put("D021","同轴花纹不符");
        put("D022","烧焊");
        put("D023","切焊");
        put("D024","封边不整");
        put("D025","拆卸痕迹");
        put("D026","胶体异常");
        put("D027","更换");
        put("D028","喷漆");
        put("D029","钣金修复");
        put("D030","拆卸更换");
        put("D031","故障");
        put("D032","卡滞");
        put("D033","异响");
        put("D034","渗漏");
        put("D035","怠速抖动");
        put("D036","负荷抖动");
        put("D037","怠速熄火");
        put("D038","负荷熄火");
        put("D039","冒蓝烟");
        put("D040","冒黑烟");
        put("D041","冒白烟");
        put("D042","液位异常");
        put("D043","液体变质");
        put("D062","故障灯常亮");
        put("D044","发霉异味");
        put("D045","水印");
        put("D046","泥沙");
        put("D053","多处锈蚀");
        put("D054","霉斑");
        put("D047","焦糊异味");
        put("D048","烟熏");
        put("D049","碳化");
        put("D050","受热变形");
        }
    };

    public static final String ADMIXEDCONTRASTACTIVITY = "admixedcontrastactivity";
}
