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
 *
 * mvp 模式 Presenter层的网络请求
 */

public class UploadBigFilePresenter extends BasePresenter<IUpload> implements ProgressListener{
    public UploadBigFilePresenter(IUpload from) {
        super(from);
    }

    private String TaskId = "";

    Context context;

    UploadFile mUploadFile;
    private String saveMda5 = "";
    private int uploadSuccessNo = 0;    //上传成功块数 断点续传的根据

    private String  picURl;      //照片(N多文件或者一个大文件)保存位置
    private String ziPathh;         //压缩文件地址
    private String ziPathhUrl;      //压缩文件保存位置文件夹

    File uploadFile;  //上传的文件
    private String  fileMd5 = "";           //压缩后MD5值
    private long curSection = 1024*1024*5;  //切块大小5M
    private List<File> files;   //切割文件总数

    CommonProgressDialog mDialog;
    //最大重新请求次数
    private int MaxRequest = 3;
    //上传失败错误次数
    private int requestErrorCount = 0;

    public void initData(String mTaskId ){
        this.TaskId = mTaskId;
        List<DBBase> lists =  DBManager.getInstance().query(TaskId,"File", SysApplication.getUser().getUserId());
        if(lists !=  null){
            if(lists.size()>0){
                DBBase dBBase = lists.get(0);
                String filejson = dBBase.getJson();
                Gson gson  = new Gson();
                mUploadFile = gson.fromJson(filejson,UploadFile.class);
            }else{
                mUploadFile = new UploadFile(TaskId,ziPathh,saveMda5,uploadSuccessNo);
            }

        }else{
            mUploadFile = new UploadFile(TaskId,ziPathh,saveMda5,uploadSuccessNo);
        }
    }

    /**
     * 压缩切割文件
     * N多文件或（一个大文件）压缩成zip包 ，然后对zip包进行切片上传
     */
    public void zipUpload(Context context){
        this.context = context;

        saveMda5 =  mUploadFile.getMD5();
        //获取缓存保存的上传节点
//        uploadSuccessNo = 0;
        uploadSuccessNo = mUploadFile.getUploadSuccessNo();


        picURl = FileUtils.SDCARD_PAHT+ "/JzgPad2/" + TaskId;
        File file = new File(picURl);

        /**
         * 删除图片不上传。暂时先不用。
         */
//        if(file.exists()){
//            File mfiles[] = file.listFiles(); // 声明目录下所有的文件 files[];
//            for (int i = 0; i < mfiles.length; i++) { // 遍历目录下所有的文件
//                if(!isSubmitFile(mfiles[i].getName())){
//                    mfiles[i].delete();
//                }
//            }
//        }



        if (file.exists() && file.list().length > 0) {
            //正在压缩
//            ShowDialogTool.showLoadingDialog(context,"正在压缩……");
            DialogUtil.showDialog(context,"压缩文件","正在压缩...",false,null);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ziPathhUrl = FileUtils.SDCARD_PAHT + "/JzgPad2/Upload/"+TaskId;
                    File ziPathh1file = new File(ziPathhUrl);
                    if(!ziPathh1file.exists()){
                        ziPathh1file.mkdirs();
                    }
                    ziPathh = FileUtils.SDCARD_PAHT + "/JzgPad2/Upload/"+TaskId+"/" + TaskId + "" +
                            ".zip";
                    //压缩
                    long startTime1 = System.currentTimeMillis();
                    ZipUtils.zip(picURl, ziPathh);
                    long endTime1 = System.currentTimeMillis();
                    System.out.println("压缩用时" + (endTime1 - startTime1));

                    uploadFile = new File(ziPathh);
                    fileMd5 = MD5Utils.getFileMd5(ziPathh);
                    //判断文件是否改变，改变则删除切割文件,重新切割
                    if (!saveMda5.equals(fileMd5)) {
                        FileAccessI.delTempFile(ziPathh, curSection);
                        //如果文件不一样，则重新从0块开始上传
                        uploadSuccessNo = 0;
                    }
                    long startTime = System.currentTimeMillis();

                    //切割文件
                    files = FileAccessI.returnTempFile(ziPathh, curSection);
                    long endTime = System.currentTimeMillis();
                    System.out.println("切片用时" + (endTime - startTime));

                    //上传
                    mHandler.sendEmptyMessage(0);
                }
            }).start();
        }
        else{
//            MyToast.showLong("文件不存在");
        }


    }



    /**
     * 上传文件
     */
//    public void uploadFile(List<File> files) {
    public void uploadFile(File file) {


        Map<String, RequestBody> requestBodyMap = new HashMap<>();
//        for (File file : files) {
        UploadFileRequestBody fileRequestBody = new UploadFileRequestBody(file, this);
        requestBodyMap.put("file\"; filename=\"" + file.getName(), fileRequestBody);
//        }

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

    //上传成功，继续上传
    public void succeed(){

        mDialog.cancel();
        requestErrorCount = 0;
        uploadSuccessNo++;
        if(uploadSuccessNo == files.size()){
            MyToast.showLong("上传成功");
            //全部上传成功
            clearUploadFile();
            baseView.uploadSucceed(new User());
        }else if(uploadSuccessNo<files.size()){
            upLoad(files.get(uploadSuccessNo));
        }
    }

    public void fail(){
        MyToast.showLong("上传失败");
        mDialog.cancel();
        requestErrorCount ++;
        //重新请求次数小于最大请求次数。重新上传
        if(requestErrorCount<MaxRequest){
            //上传失败，继续上传。
            upLoad(files.get(uploadSuccessNo));
        }

        saveUpload();
    }



    //上传文件
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





    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            MyToast.showLong("压缩分割完成");
            DialogUtil.dismissDialog();
            if(files != null){
                if(uploadSuccessNo == files.size()){
                    uploadSuccessNo= 0;
                }
                if(files.size()>0){
                    upLoad(files.get(uploadSuccessNo));
                }
            }
        }
    };


    /**
     * 保存本地上传进度
     */
    public void saveUpload(){
        mUploadFile.setTaskId(TaskId);
        mUploadFile.setFileName(ziPathh);
        mUploadFile.setMD5(fileMd5);
        mUploadFile.setUploadSuccessNo(uploadSuccessNo); //进度
//        System.out.println("uploadSuccessNo====="+uploadSuccessNo);
        Gson gson = new Gson();
        String mUploadFileJson =gson.toJson(mUploadFile);
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
        if(!TextUtils.isEmpty(picURl)){
            File file = new File(picURl);
            deleteFile(file);
        }
        if(!TextUtils.isEmpty(ziPathhUrl)){
            File ziPathhUrlfile = new File(ziPathhUrl);
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
