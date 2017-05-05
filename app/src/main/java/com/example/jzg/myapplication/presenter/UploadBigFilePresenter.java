package com.example.jzg.myapplication.presenter;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.example.jzg.myapplication.R;
import com.example.jzg.myapplication.app.SysApplication;
import com.example.jzg.myapplication.base.BasePresenter;
import com.example.jzg.myapplication.bean.Upload;
import com.example.jzg.myapplication.bean.UploadFile;
import com.example.jzg.myapplication.bean.User;
import com.example.jzg.myapplication.db.DBBase;
import com.example.jzg.myapplication.db.DBManager;
import com.example.jzg.myapplication.dialog.DialogUtil;
import com.example.jzg.myapplication.global.Constants;
import com.example.jzg.myapplication.http.ApiManager;
import com.example.jzg.myapplication.interfaces.ProgressListener;
import com.example.jzg.myapplication.mvpview.IUpload;
import com.example.jzg.myapplication.upload.UploadFileRequestBody;
import com.example.jzg.myapplication.utils.FileAccessI;
import com.example.jzg.myapplication.utils.FileUtils;
import com.example.jzg.myapplication.utils.MD5Utils;
import com.example.jzg.myapplication.utils.MyToast;
import com.example.jzg.myapplication.utils.NetworkExceptionUtils;
import com.example.jzg.myapplication.utils.ZipUtils;
import com.example.jzg.myapplication.view.CommonProgressDialog;
import com.google.gson.Gson;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by 李波 on 2017/1/20.
 * mvp 模式 Presenter层的网络请求
 * 说明：
 *  不管是最开始 还是续传都是调用此两步方法，其他什么都不用管，内部已封装好。仅仅是大文件和压缩zip的存储路径需自调而已。
 *         mPresenter.initData("666"); 传入当前传输任务标识 taskId
 *         mPresenter.zipUpload(this);
 */

public class UploadBigFilePresenter extends BasePresenter<IUpload> implements ProgressListener{
    public UploadBigFilePresenter(IUpload from) {
        super(from);
    }

    private String TaskId = "";

    Context context;

    UploadFile uploadFileInfo;          //记录上传文件的各项参数主要就是进度保存
    private String saveMda5 = "";       //校验文件是否更改，更改就重新切割
    private int uploadSuccessNo = 0;    //上传成功块数 断点续传的根据

    /**
     * Created by 李波 on 2017/5/5.
     * 基本要更改的就这三个路径 其他变量 根据情况而变动
     * 三个路径 其实要更改的就 bigFilePath zipCatalogPath 两个 最后一个自动生成
     */
    private String bigFilePath;         //照片(N多文件或者一个大文件)保存位置
    private String zipCatalogPath;      //压缩文件保存目录 里面就是 zipAbsolutePath
    private String zipAbsolutePath;     //压缩文件绝对地址

    File uploadFile;                //上传的文件
    private String  fileMd5 = "";           //压缩后MD5值
    private long curSection = 1024*1024*5;  //切块大小5M
    private List<File> files;   //切割文件总数 真正上传的就是这里面装的 一个个小片文件

    CommonProgressDialog mDialog;
    //最大重新请求次数
    private int MaxRequest = 3;
    //上传失败错误次数
    private int requestErrorCount = 0;

    /**
     * Created by 李波 on 2017/5/4.
     * 第一步：初始化要传递文件的信息
     */
    public void initData(String mTaskId ){
        this.TaskId = mTaskId;
        List<DBBase> lists =  DBManager.getInstance().query(TaskId,"File", SysApplication.getUser().getUserId());
        if(lists !=  null){
            if(lists.size()>0){
                DBBase dBBase = lists.get(0);
                String filejson = dBBase.getJson();
                Gson gson  = new Gson();
                uploadFileInfo = gson.fromJson(filejson,UploadFile.class);
            }else{
                uploadFileInfo = new UploadFile(TaskId, zipAbsolutePath,saveMda5,uploadSuccessNo);
            }

        }else{
            uploadFileInfo = new UploadFile(TaskId, zipAbsolutePath,saveMda5,uploadSuccessNo);
        }
    }

    /**
     * 第二步：@_1
     * 压缩切割文件 然后上传
     * N多文件或（一个大文件）压缩成zip包 ，然后对zip包进行切片上传
     */
    public void zipUpload(Context context){
        this.context = context;

        saveMda5 =  uploadFileInfo.getMD5();

        //获取缓存保存的上传进度节点 初始化 或 继续上传的依据
        uploadSuccessNo = uploadFileInfo.getUploadSuccessNo();

        //获取存储大文件或者N多文件的 文件夹目录
        bigFilePath = Constants.bigFilePath + TaskId;
        File file = new File(bigFilePath);

        //如果目录下有文件 就开始压缩 - - 切割 最后上传。
        if (file.exists() && file.list().length > 0) {
            DialogUtil.showDialog(context,"压缩文件","正在压缩...",false,null);
            new Thread(new Runnable() {
                @Override
                public void run() {

                    //创建压缩文件夹目录
                    zipCatalogPath = Constants.zipCatalogPath + TaskId;
                    File ziPathh1file = new File(zipCatalogPath);
                    if(!ziPathh1file.exists()){
                        ziPathh1file.mkdirs();
                    }

                    //定义压缩文件 .zip
                    zipAbsolutePath = zipCatalogPath+"/" + TaskId + ".zip";

                    //开始压缩
                    long startTime1 = System.currentTimeMillis();
                    ZipUtils.zip(bigFilePath, zipAbsolutePath); //传入大文件根目录，压缩成zip包
                    long endTime1 = System.currentTimeMillis();
                    System.out.println("压缩用时" + (endTime1 - startTime1));

                    uploadFile = new File(zipAbsolutePath); //压缩完毕，创建上传文件

                    fileMd5 = MD5Utils.getFileMd5(zipAbsolutePath);
                    //判断文件是否改变，改变则删除切割文件,重新切割
                    if (!saveMda5.equals(fileMd5)) {
                        FileAccessI.delTempFile(zipAbsolutePath, curSection);
                        //如果文件不一样，则重新从0块开始上传
                        uploadSuccessNo = 0;
                    }

                    //切割文件
                    long startTime = System.currentTimeMillis();
                    files = FileAccessI.returnTempFile(zipAbsolutePath, curSection);//把压缩好的zip包 切割成小片文件
                    long endTime = System.currentTimeMillis();
                    System.out.println("切片用时" + (endTime - startTime));

                    //切片完成上传文件
                    mHandler.sendEmptyMessage(0);
                }
            }).start();
        }else{
//            MyToast.showLong("文件不存在");
        }
    }

    /**
     *  第二步：@_2
     *  压缩切割完成开始上传文件
     */
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            DialogUtil.dismissDialog();
            if(files != null){
                if(uploadSuccessNo == files.size()){
                    uploadSuccessNo= 0;
                }
                if(files.size()>0){
                    upLoad(files.get(uploadSuccessNo)); //开始上传切片文件
                }
            }
        }
    };


    /**
     *  第二步：@_3
     *  开始上传文件
     */
    public void upLoad(File file){
        if(SysApplication.networkAvailable){
            upLoadshowDialog(context);
            uploadFile(file);
        }
    }

    //显示正在上传dialog
    public void upLoadshowDialog(Context context){
        mDialog = new CommonProgressDialog(context, R.style.MyUploadDialogStyleTop);
        mDialog.setMessage("正在上传"+"("+ (uploadSuccessNo+1) +"/"+files.size() +")");
        mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mDialog.setCancelable(false);
        mDialog.show();
        mDialog.setCanceledOnTouchOutside(false);
    }

    /**
     *  第二步：@_4
     *  上传文件 - - 单个文件
     */
    public void uploadFile(File file) {

        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        UploadFileRequestBody fileRequestBody = new UploadFileRequestBody(file, this);
        requestBodyMap.put("file\"; filename=\"" + file.getName(), fileRequestBody);

        ApiManager.getApiServer().uploadFileInfo(getUploadParams(),requestBodyMap)
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(new Observer<Upload>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e != null && baseView != null) {
                            mDialog.cancel();
                            baseView.uploadFail();
                            baseView.dismissLoading();
                            baseView.showError(NetworkExceptionUtils.getErrorByException(e));
                        }
                    }
                    @Override
                    public void onNext(Upload response) {
                        baseView.dismissLoading();
                        if (response.getStatus() == 100) {
                            succeed();
                        } else {
                            fail();
                            baseView.uploadFail();
                            baseView.showError(response.getMsg());
                        }
                    }
                });

    }

    /**
     *  第三步：@_1
     *  上传成功，继续上传
     */
    public void succeed(){

        mDialog.cancel();
        requestErrorCount = 0;
        uploadSuccessNo++; //递增一次，上传下一个切片文件
        if(uploadSuccessNo == files.size()){
            MyToast.showLong("上传成功");
            //全部上传成功后清除本地文件 这一步会把本地原始大文件 和 切片文件全部清除
            clearUploadFile();
            baseView.uploadSucceed(new User());
        }else if(uploadSuccessNo<files.size()){
            upLoad(files.get(uploadSuccessNo));  //继续上传下一个切片文件
        }
    }

    /**
     *  第三步：@_2
     *  上传失败，重新请求上传失败的切片文件，如果多次请求还是失败那就最终放弃并保存当前进度
     */
    public void fail(){
        MyToast.showLong("上传失败");
        mDialog.cancel();
        requestErrorCount ++;
        //重新请求次数小于最大请求次数。重新上传
        if(requestErrorCount<MaxRequest){
            //上传失败，继续上传。
            upLoad(files.get(uploadSuccessNo));
        }

        //为避免上传过程中的突发异常事件，最好还是一旦失败就保存下进度。
            saveUpload();
    }





    /**
     * Created by 李波 on 2017/5/5.
     * 封装上传参数
     */
    public Map<String, String> getUploadParams() {
        Map<String, String> params = new HashMap<String,String>();
        params.put("userid", SysApplication.getUser().getUserId()+"");
        params.put("fileName",uploadFile.getName());
        params.put("nPos","0");
        params.put("nPosTotal",uploadFile.length()+"");
        params.put("md5",fileMd5);
        params.put("taskId",TaskId);
        params.put("delPicID","");
        params = MD5Utils.encryptParams(params);
        return params;
    }

    @Override
    public void onProgress(long hasWrittenLen, long totalLen, boolean hasFinish) {
        mDialog.setMax((int)(totalLen/1024));
        mDialog.setProgress((int)(hasWrittenLen/1024));
    }


    /**
     * 保存本地上传进度
     */
    public void saveUpload(){
        uploadFileInfo.setTaskId(TaskId);
        uploadFileInfo.setFileName(zipAbsolutePath);
        uploadFileInfo.setMD5(fileMd5);
        uploadFileInfo.setUploadSuccessNo(uploadSuccessNo); //进度
//        System.out.println("uploadSuccessNo====="+uploadSuccessNo);
        Gson gson = new Gson();
        String mUploadFileJson =gson.toJson(uploadFileInfo);
//        System.out.println(mUploadFileJson);
        if(DBManager.getInstance().isExist("File",TaskId,SysApplication.getUser().getUserId())){
            DBManager.getInstance().update(SysApplication.getUser().getUserId(),TaskId,"File",mUploadFileJson);
        }else{
            DBManager.getInstance().add(mUploadFileJson,"File",TaskId,SysApplication.getUser().getUserId());
        }


    }


    /**
     * 上传成功后清除图片和缓存
     */
    public void clearUploadFile(){
        if(DBManager.getInstance().isExist("File",TaskId,SysApplication.getUser().getUserId())){
            DBManager.getInstance().deleteAfterSubmit(SysApplication.getUser().getUserId(),TaskId);
        }
        if(!TextUtils.isEmpty(bigFilePath)){
            File file = new File(bigFilePath);
            deleteFile(file);
        }
        if(!TextUtils.isEmpty(zipCatalogPath)){
            File ziPathhUrlfile = new File(zipCatalogPath);
            deleteFile(ziPathhUrlfile);
        }
    }
    /**
     * 上传成功后清除图片和缓存
     */
    public void clearUploadFile(String fileTaskId){

        if(DBManager.getInstance().isExist("File",TaskId,SysApplication.getUser().getUserId())){
            DBManager.getInstance().deleteAfterSubmit(SysApplication.getUser().getUserId(),TaskId);
        }

        String picURL = FileUtils.SDCARD_PAHT+ "/JzgPad2/" + fileTaskId;
        if(!TextUtils.isEmpty(picURL)){
            File file = new File(picURL);
            deleteFile(file);
        }
        String ziPathhURL = FileUtils.SDCARD_PAHT + "/JzgPad2/Upload/"+fileTaskId;
        if(!TextUtils.isEmpty(ziPathhURL)){
            File ziPathhUrlfile = new File(ziPathhURL);
            deleteFile(ziPathhUrlfile);
        }


    }

    public void deleteFile(File file) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    this.deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
        } else {
//            MyToast.showLong("文件不存在！");
        }
    }



}
