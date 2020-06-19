package com.example.jzg.myapplication.cameradetails;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blankj.utilcode.utils.ImageUtils;
import com.blankj.utilcode.utils.ScreenUtils;
import com.example.jzg.myapplication.R;
import com.example.jzg.myapplication.dialog.DialogUtil;
import com.example.jzg.myapplication.http.RxThreadUtil;
import com.example.jzg.myapplication.utils.BitmapUtils;
import com.example.jzg.myapplication.utils.CameraUtil;
import com.example.jzg.myapplication.utils.FileUtils;
import com.example.jzg.myapplication.utils.FrescoImageLoader;
import com.example.jzg.myapplication.utils.ImageUtil;
import com.example.jzg.myapplication.utils.LogUtil;
import com.example.jzg.myapplication.utils.MyToast;
import com.example.jzg.myapplication.view.FocusImageView;
import com.example.jzg.myapplication.view.VerticalSeekBar;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.photodraweeview.PhotoDraweeView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by libo on 2020/5/19.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 自定义相机 （竖屏）
 */
public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback,SensorEventListener {
    protected String TAG = getClass().getSimpleName();
    @BindView(R.id.fl_surfaceView)
    RelativeLayout flSurfaceView;
    @BindView(R.id.ivBigPhoto)
    PhotoDraweeView ivBigPhoto;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ivOutline)
    SimpleDraweeView ivOutline;
    @BindView(R.id.iv_focus)
    FocusImageView focusImageView;
    @BindView(R.id.ivPreview)
    SimpleDraweeView ivPreview;
    @BindView(R.id.tvPicName)
    TextView tvPicName;
    @BindView(R.id.rlPreviewLayout)
    RelativeLayout rlPreviewLayout;
    @BindView(R.id.ivCapture)
    ImageView ivCapture;
    @BindView(R.id.ivFlashToggle)
    ImageView ivFlashToggle;
    @BindView(R.id.ivSwitchCamera)
    ImageView ivSwitchCamera;
    @BindView(R.id.rlCaptureLayout)
    RelativeLayout rlCaptureLayout;
    @BindView(R.id.tvRecapture)
    TextView tvRecapture;
    @BindView(R.id.tvConfirm)
    TextView tvConfirm;
    @BindView(R.id.rlControl)
    RelativeLayout rlControl;
    @BindView(R.id.llBottom)
    LinearLayout llBottom;
    @BindView(R.id.rlRoot)
    RelativeLayout rlRoot;
    @BindView(R.id.vSeekBar)
    VerticalSeekBar vSeekBar;

    /**
     * SurfaceView在子线程刷新不会阻塞主线程，适用于界面频繁更新、对帧率要求较高的情况,所以用于摄像头的取景框
     * 通过它的 SurfaceHolder 来建立连接与camera的连接
     */
    @BindView(R.id.surfaceView)
    SurfaceView surfaceView;     //取景预览框
    private SurfaceHolder mSurfaceHolder;

    private Camera mCamera;
    private int mCameraId = 0;//0-后置摄像头，1-前置摄像头
    private Camera.Parameters parameters;
    int screenWidth;
    int screenHeight;
    private boolean isFlashOn = false; //闪光灯

    //预览宽高，注意预览框 surfaceView 的宽高比例需与这里一致，否则就会出现预览变形
    private int previewWidth;
    private int previewHeight;

    //拍摄后图片的分辨率宽高，这里将直接影响图片的大小，所以是需要控制的，避免过大导致OOM
    private int pictureWidth;
    private int pictureHeight;

    //拍摄后照片的角度用于旋转角度
    private int picDegree = 0;

    //拍摄方向监听
    private OrientationEventListener mOrEventListener;


    private String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +"000"+ File.separator;

    //拍摄后照片的临时地址，这期间还需要一些处理加工等等...最终到储存地址
    private String imgPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +"000"+ File.separator+"temp.jpg";

    //照片最终的储存地址
    private String picPath;

    private BitmapUtils bitmapUtils;

    private SensorManager mSensroMgr;//传感器管理类 这里是指手机上的光线传感器，不是摄像头的亮度传感器

    //===========摄像头感知光线亮度==================================
    //上次记录的时间戳
    long lastRecordTime = System.currentTimeMillis();

    //上次记录的索引
    int darkIndex = 0;

    //一个历史记录的数组，255是代表亮度最大值
    long[] darkList = new long[]{255, 255, 255, 255};

    //扫描间隔
    int waitScanTime = 300;

    //亮度低的阀值
    int darkValue = 80;


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (!focusImageView.isGone){
                focusImageView.setVisibility(View.GONE);
                vSeekBar.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏模式也就是隐藏了状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera_demo);
        ButterKnife.bind(this);
        initData();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mSensroMgr.registerListener(this, mSensroMgr.getDefaultSensor(Sensor.TYPE_LIGHT),
                SensorManager.SENSOR_DELAY_NORMAL);//开启监听传感器

    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensroMgr.unregisterListener(this);//断开监听传感器
    }

    private void initData(){
        com.blankj.utilcode.utils.FileUtils.createOrExistsDir(dirPath);
        screenWidth  = ScreenUtils.getScreenWidth();
        screenHeight = ScreenUtils.getScreenHeight();
        bitmapUtils = new BitmapUtils();
        picPath = getIntent().getStringExtra("picPath");
        //1、取景框准备 surfaceView 获取 SurfaceHolder
        mSurfaceHolder = surfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        setManualFocuslisten();
        mSensroMgr = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

    }




    private void initCamera(){

        //2、打开相机
        mCamera = Camera.open(mCameraId);

        //3、获取相机参数 并设置参数
        parameters = mCamera.getParameters();

        //设置预览分辨率，越高预览越清晰
        int[] previewResult = CameraUtil.initPreviewSize(parameters, screenWidth);
        previewWidth = previewResult[0];
        previewHeight = previewResult[1];
        parameters.setPreviewSize(previewWidth, previewHeight);

        //根据预览的比例来设置 SurfaceView 的宽高值，保证比例一致，预览不变形
        float whRatio = previewWidth*1.0f/previewHeight;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) flSurfaceView.getLayoutParams();
        layoutParams.width = screenWidth;
        layoutParams.height = (int) (screenWidth * whRatio);
        flSurfaceView.setLayoutParams(layoutParams);


        //设置拍摄后图片的尺寸，这里就直接影响到照片的大小，所以得根据情况控制下否则生成bitmap时容易OOM
        int[] pictureResult = CameraUtil.initPictureSize(parameters.getSupportedPictureSizes());
        pictureWidth = pictureResult[0];
        pictureHeight = pictureResult[1];
        parameters.setPictureSize(pictureWidth, pictureHeight);

        //设图片格式
        parameters.setPictureFormat(PixelFormat.JPEG);

        //设置质量
        parameters.set("jpeg-quality", 100);
        if (mCameraId == 0) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }
        LogUtil.e(TAG,"相机预览大小："+parameters.getPreviewSize().width+","+
                parameters.getPreviewSize().height
        );
        LogUtil.e(TAG,"相机图片大小："+parameters.getPictureSize().width+","+
                parameters.getPictureSize().height
        );

        //控制图像的正确显示方向，因为相机默认是横屏模式 所以有个90度的方向控制
        CameraUtil.setDisplay(parameters, mCamera);

        try {
            //4、设置参数
            mCamera.setParameters(parameters);

            //5、设置取景框
            mCamera.setPreviewDisplay(mSurfaceHolder);

            //亲测的一个方法 基本覆盖所有手机 将预览矫正
            CameraUtil.getInstance().setCameraDisplayOrientation(this, mCameraId, mCamera);

            //6、开始预览
            mCamera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }

        startOrientationChangeListener();

        //7、打开相机先自动对焦下
        autoFocus();

        //启动摄像头对环境亮度的监听，较暗时提示打开闪光灯
        mCamera.setPreviewCallback(previewCallback);

    }


    /**
     * Created by 李波 on 2020/5/21.
     * 监听手机方向 用于后续照片拍照后的角度旋转
     */
    public void startOrientationChangeListener() {
        if (mOrEventListener == null) {
            mOrEventListener = new OrientationEventListener(this) {
                @Override
                public void onOrientationChanged(int rotation) {
                    if (((rotation >= 0) && (rotation <= 45)) || (rotation > 315)) {
                        rotation = 0;
                    } else if ((rotation > 45) && (rotation <= 135)) {
                        rotation = 90;
                    } else if ((rotation > 135) && (rotation <= 225)) {
                        rotation = 180;
                    } else if ((rotation > 225) && (rotation <= 315)) {
                        rotation = 270;
                    } else {
                        rotation = 0;
                    }
                    if (rotation == picDegree) return;
                    picDegree = rotation;
                    LogUtil.e(TAG, "mOrientation=" + picDegree);
                }
            };
        }
        mOrEventListener.enable();
    }

    @OnClick({R.id.ivCapture, R.id.ivFlashToggle, R.id.ivSwitchCamera, R.id.tvRecapture, R.id.tvConfirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivCapture:  //拍照
                takePicture();
                break;
            case R.id.ivFlashToggle:  //切换闪光灯
                changeFlashOn();
                break;
            case R.id.ivSwitchCamera: //切换前后摄像头
                switchCamera();
                break;
            case R.id.tvRecapture:    //重拍
                mCamera.stopPreview();
                mCamera.startPreview();
                FrescoImageLoader.clearSingleCacheByUrl(imgPath,true);
                ivBigPhoto.setPhotoUri(Uri.parse(""));
                llBottom.setVisibility(View.VISIBLE);
                ivBigPhoto.setVisibility(View.GONE);
                rlControl.setVisibility(View.GONE);
                break;
            case R.id.tvConfirm:  //确定
                savePic2Local(imgPath);
                break;
        }
    }

    //SurfaceView 创建时调用
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        LogUtil.e(TAG, "surfaceCreated");
        initCamera();
    }

    //SurfaceView 尺寸发生变化时调用，比如横竖屏切换。
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        LogUtil.e(TAG, "surfaceChanged");
        LogUtil.e("surfaceView", "width = "+ width +"  height = "+height);

    }

    //SurfaceView 销毁时调用
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        LogUtil.e(TAG, "surfaceDestroyed 销毁" );
        releaseCamera();
    }


    //自动对焦
    private void autoFocus(){
        mCamera.autoFocus(autoFocusCallback);
    }


    //拍照
    public void takePicture(){
        if(mCamera!=null){
            //拍照前先再次对焦成功后在拍照 保证图片的清晰
            mCamera.autoFocus(autoFocusCallbackTakePicture);
        }
    }

    //对焦
    AutoFocusCallback autoFocusCallback = new AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            //对焦成功
            if (success){
                focusImageView.onFocusSuccess();
            }else {
                focusImageView.onFocusFailed();
            }
        }
    };

    //拍照前对焦
    AutoFocusCallback autoFocusCallbackTakePicture = new AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            //对焦成功
            if (success){
                mCamera.takePicture(null, null,pictureCallback);
            }else {
                MyToast.showLong("对焦失败请重新拍照");
            }
        }
    };


    //拍照后的数据回调
    PictureCallback pictureCallback = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            //data字节数组就是拍照后的照片数据
            if (data!=null){
                double fileSizeKb = data.length / 1024;
                LogUtil.e(TAG, (data.length / 1024) + "KB," + "fileSizeKb=" + fileSizeKb);
                llBottom.setVisibility(View.GONE);
                ivBigPhoto.setVisibility(View.VISIBLE);
                rlControl.setVisibility(View.VISIBLE);

                DialogUtil.showDialog(CameraActivity.this);

                Observable.create((Observable.OnSubscribe<Bitmap>) (Subscriber<? super Bitmap> subscriber) -> {
                    subscriber.onNext(CameraActivity.this.saveBitmapBytes(data)); //先保存拍照所得图片
                    subscriber.onCompleted();
                }).subscribeOn(Schedulers.io()).map(new Func1<Bitmap, Boolean>() {
                    @Override
                    public Boolean call(Bitmap bitmap) {
                        return rotateAndSave(bitmap, picDegree, imgPath); //接着旋转图片到正常角度
                    }
                }).observeOn(AndroidSchedulers.mainThread()).subscribe((Boolean result)-> {
                    if (result) {
                        ivBigPhoto.setPhotoUri(Uri.parse("file://" + imgPath));
                        DialogUtil.dismissDialog();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });

            }
        }
    };


    /***
     * 拍照后的处理
     * 指定分辨率输出，所以不需要对分辨率进行调整，只需要处理照片质量即可
     * @param data 照片的字节数组
     * @return
     */
    private Bitmap saveBitmapBytes(byte[] data) {
        boolean saved = false;
        double fileSizeKb = data.length / 1024;
        LogUtil.e(TAG, (data.length / 1024) + "KB," + "fileSizeKb=" + fileSizeKb);
        //先判断Bitmap大小，如果bitmap大于3xxx将它压缩至3xxx左右
        Bitmap bitmap1 = BitmapFactory.decodeByteArray(data, 0, data.length);

        LogUtil.e(TAG, "data分辨率+++" + bitmap1.getWidth() + "," + bitmap1.getHeight());
        LogUtil.e(TAG, "data分辨率fileSizeKb+++=" + BitmapUtils.getBitmapSize(bitmap1) / (1024 * 1024) + "MB");

        //保存原图
        FileUtils.saveFile(data, imgPath);

        //这里之所以直接加载原图得到bitmap，那是因为拍摄之前就已经定义了图片的分辨率大小，所以没oom
         Bitmap bitmap = BitmapFactory.decodeFile(imgPath);

        return bitmap;
    }


    //旋转图片角度并保存
    public boolean rotateAndSave(Bitmap saveBitmap, int picDegree, String imgPath) {
        Bitmap bitmap = CameraUtil.rotateBitmap(saveBitmap, mCameraId, picDegree, imgPath);
        boolean result = ImageUtils.save(bitmap, imgPath, Bitmap.CompressFormat.JPEG);
        if (bitmap != null || !bitmap.isRecycled()) bitmap.recycle();
        return result;
    }

    //保存照片到本地
    public void savePic2Local(final String imgPath) {
        File file = new File(imgPath);
        if (file.exists() && file.canRead() && file.length() > 0) {
            DialogUtil.showDialog(CameraActivity.this);
            Observable.create((Observable.OnSubscribe<String>) subscriber -> {

                //70 压缩系数
                boolean isPicSave =  bitmapUtils.saveImage(imgPath, picPath, 70);

                if (isPicSave){

                    //通知相册刷新 同步到相册
                    ImageUtil.insertToGallery(getApplicationContext(), new File(picPath), null);

                    subscriber.onNext(picPath);
                }
            }).compose(RxThreadUtil.networkSchedulers()).subscribe(new Subscriber<String>() {
                @Override
                public void onCompleted() {
                    DialogUtil.dismissDialog();
                }

                @Override
                public void onError(Throwable e) {
                    DialogUtil.dismissDialog();
                }

                @Override
                public void onNext(String picPath) {
                    FrescoImageLoader.clearSingleCacheByUrl(imgPath,true);
                    ivBigPhoto.setPhotoUri(Uri.parse(""));
                    DialogUtil.dismissDialog();
//                    FileUtils.deleteFile(imgPath);
                    Intent intentResult = getIntent();
                    intentResult.putExtra("picPath", picPath);
                    setResult(RESULT_OK, intentResult);
                    finish();

                }
            });
        } else {
            MyToast.showShort("读取图片失败");
        }
    }

    /**
     * Created by 李波 on 2020/5/28.
     * 切换前后摄像头
     */
    public void switchCamera() {
        releaseCamera();
        mCameraId = (mCameraId + 1) % Camera.getNumberOfCameras();
        if (ivFlashToggle != null) {//闪光灯，如果是前置摄像头隐藏闪光灯
            if (mCameraId == 0) {//后置摄像头
                ivFlashToggle.setVisibility(View.VISIBLE);
            } else {//前置摄像头
                ivFlashToggle.setVisibility(View.INVISIBLE);
            }
        }
        initCamera();
    }

    /**
     * Created by 李波 on 2020/5/28.
     * 切换闪光灯
     */
    public void changeFlashOn() {

        this.isFlashOn=!isFlashOn;
        if (isFlashOn) {
            CameraUtil.getInstance().turnLightOn(mCamera);
        } else {
            CameraUtil.getInstance().turnLightOff(mCamera);
        }
        if (ivFlashToggle != null) {
            ivFlashToggle.setImageResource(isFlashOn ? R.drawable.ic_flash_on : R.drawable.ic_flash_off);
        }

    }

    /**
     * Created by 李波 on 2020/6/17.
     * 释放相机
     */
    private void releaseCamera(){
        if (mCamera != null) {
            try {
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
                mCamera.release();
            } catch (Exception e) {
                e.printStackTrace();
            }

            mCamera = null;
            if (mOrEventListener != null) mOrEventListener.disable();
        }
    }

    /**
     * 手动对焦
     * 1、先定义对焦和测光的区域大小
     * 2、计算相对于Camera的对焦区域
     */
    private void manualFocus(int x,int y){
        //设置对焦区域大小,100即表示 100*100 的正方形区域
        int Area = 100;
        List<Camera.Area> areas = new ArrayList<Camera.Area>();//对焦区域
        Rect rect = new Rect(x - 100, y - 100, x + 100, y + 100);
        int left = rect.left * 2000 / surfaceView.getWidth() - 1000;
        int top = rect.top * 2000 / surfaceView.getHeight() - 1000;
        int right = rect.right * 2000 / surfaceView.getWidth() - 1000;
        int bottom = rect.bottom * 2000 / surfaceView.getHeight() - 1000;
        // 如果超出了(-1000,1000)到(1000, 1000)的范围，则会导致相机崩溃
        left = left < -1000 ? -1000 : left;
        top = top < -1000 ? -1000 : top;
        right = right > 1000 ? 1000 : right;
        bottom = bottom > 1000 ? 1000 : bottom;
        Rect areaFocus = new Rect(left, top, right, bottom);// 最终得到Camera的对焦区域
        //只有一个感光区，直接设置权重为1000了
        areas.add(new Camera.Area(areaFocus, 1000));

        if (parameters.getMaxNumMeteringAreas() > 0) {
            if (!parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                return; //cannot autoFocus
            }
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            parameters.setFocusAreas(areas);
            parameters.setMeteringAreas(areas);
            try {
                mCamera.cancelAutoFocus(); // 每次对焦前，需要先取消对焦
                mCamera.setParameters(parameters);
                mCamera.autoFocus(autoFocusCallback);//开始对焦，当然走的都是自动对焦，只是自定义区域而已
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Created by 李波 on 2020/6/1.
     * 计算手动对焦框的显示位置
     */
    private void calculateFocusView(int x,int y){
        int focusImageViewHalfSize = focusImageView.getWidth()/2;
        int minX = focusImageViewHalfSize;
        int minY = focusImageViewHalfSize;
        int maxX = surfaceView.getWidth() - focusImageViewHalfSize;
        int maxY = surfaceView.getHeight() - focusImageViewHalfSize;
        if (x<=minX)x = minX;
        else if(x>=maxX)x = maxX;

        if (y<=minY)y = minY;
        else if(y>=maxY)y = maxY;

        Point point = new Point(x,y);
        focusImageView.startFocus(point);
        vSeekBar.setVisibility(View.VISIBLE);
        focusImageView.setSeekBar(vSeekBar);

        vSeekBar.setMax(parameters.getMaxExposureCompensation() * 2);

        //每次手动对焦恢复默认值
        vSeekBar.setProgress(parameters.getMaxExposureCompensation());
        parameters.setExposureCompensation(0);
        mCamera.setParameters(parameters);
    }

    private boolean isTouchSeekBar;

    //手动对焦监听
    private void setManualFocuslisten(){
        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!isTouchSeekBar){
                    mHandler.removeMessages(0);
                    focusImageView.isGone=true;

                    int x = (int) event.getX();
                    int y = (int) event.getY();
                    manualFocus(x,y);
                    calculateFocusView(x,y);

                }

                return false;
            }
        });

        //调节曝光度
        vSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int exposure = progress - parameters.getMaxExposureCompensation();
                LogUtil.e(TAG,"曝光度  = "+exposure);
                if (mCamera == null) {
                    return;
                }
                parameters.setExposureCompensation(progress - parameters.getMaxExposureCompensation());
                mCamera.setParameters(parameters);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        vSeekBar.setIseekBarTouchListener(new VerticalSeekBar.IseekBarTouchListener() {
            @Override
            public void touchDown() {
                mHandler.removeMessages(0);
                LogUtil.e("seekbar","touchDown");
                isTouchSeekBar = true;
                focusImageView.isGone = false;
            }

            @Override
            public void touchup() {
                LogUtil.e("seekbar","touchup");
                isTouchSeekBar = false;
                //对焦后发送延时消息3.5秒隐藏对焦框。
                mHandler.sendEmptyMessageDelayed(0, 3500);
            }
        });

    }


    //这里是指手机上的光线传感器，不是摄像头的亮度传感器，也就是接电话时感光自动息屏那个
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            float light_strength = event.values[0];//光线强度
            if (light_strength < 50) {
                LogUtil.e(TAG,"光线强度< 50 "+light_strength);
            } else {
                LogUtil.e(TAG,"光线强度> 50 "+light_strength);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    //摄像头感应环境亮度的回调，当亮度低时提示打开闪光灯
    Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastRecordTime < waitScanTime) {
                return;
            }
            lastRecordTime = currentTime;
            int width = camera.getParameters().getPreviewSize().width;
            int height = camera.getParameters().getPreviewSize().height;

            //像素点的总亮度
            long pixelLightCount = 0L;

            //像素点的总数
            long pixeCount = width * height;

            //采集步长，因为没有必要每个像素点都采集，可以跨一段采集一个，减少计算负担，必须大于等于1。
            int step = 10;
            //data.length - allCount * 1.5f的目的是判断图像格式是不是YUV420格式，只有是这种格式才相等
            //因为int整形与float浮点直接比较会出问题，所以这么比
            if (Math.abs(data.length - pixeCount * 1.5f) < 0.00001f) {
                for (int i = 0; i < pixeCount; i += step) {
                    //如果直接加是不行的，因为data[i]记录的是色值并不是数值，byte的范围是+127到—128，
                    // 而亮度FFFFFF是11111111是-127，所以这里需要先转为无符号unsigned long参考Byte.toUnsignedLong()
                    pixelLightCount += ((long) data[i]) & 0xffL;
                }
                //平均亮度
                long cameraLight = pixelLightCount / (pixeCount / step);
                //更新历史记录
                int lightSize = darkList.length;
                darkList[darkIndex = darkIndex % lightSize] = cameraLight;
                darkIndex++;
                boolean isDarkEnv = true;
                //判断在时间范围waitScanTime * lightSize内是不是亮度过暗
                for (int i = 0; i < lightSize; i++) {
                    if (darkList[i] > darkValue) {
                        isDarkEnv = false;
                    }
                }
                Log.e(TAG, "摄像头环境亮度为 ： " + cameraLight);
                if (!isFinishing()) {
                    //亮度过暗就提醒
                    if (isDarkEnv) {
                        Log.e(TAG, "轻触照亮！");
                        MyToast.showLong("轻触照亮！");
//                        CameraUtil.getInstance().turnLightOn(mCamera);
                    } else {
//                        CameraUtil.getInstance().turnLightOff(mCamera);

                    }
                }
            }
        }

    };

}
