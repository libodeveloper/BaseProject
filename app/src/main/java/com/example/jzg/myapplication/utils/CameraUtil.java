package com.example.jzg.myapplication.utils;

/**
 * Created by yue on 2015/8/13.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.media.ExifInterface;
import android.os.Build;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.example.jzg.myapplication.app.SysApplication;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class CameraUtil {
    private static final String TAG= "CameraUtil";
    //降序
    private static CameraDropSizeComparator dropSizeComparator = new CameraDropSizeComparator();
    //升序
    private CameraAscendSizeComparator ascendSizeComparator = new CameraAscendSizeComparator();
    private static CameraUtil myCamPara = null;

    private CameraUtil() {

    }

    public static CameraUtil getInstance() {
        if (myCamPara == null) {
            myCamPara = new CameraUtil();
            return myCamPara;
        } else {
            return myCamPara;
        }
    }

    /**
     * 保证预览方向正确
     *
     * @param activity
     * @param cameraId
     * @param camera
     */
    public void setCameraDisplayOrientation(Activity activity,
                                            int cameraId, Camera camera) {
        Camera.CameraInfo info =
                new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }


    public Bitmap setTakePicktrueOrientation(int id, Bitmap bitmap) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(id, info);
        bitmap = rotaingImageView(id, info.orientation, bitmap);
        return bitmap;
    }

    public static Bitmap adjustBitmapDegree(String path){
        return rotateBitmapByDegree(path ,getBitmapDegree(path));
    }

    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogUtil.e(TAG,"degree:"+degree);
        return degree;
    }

    public static Bitmap rotateBitmapByDegree(String path, int degree) {
        Bitmap bm = BitmapFactory.decodeFile(path);
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    /**
     * 把相机拍照返回照片转正
     *
     * @param angle 旋转角度
     * @return bitmap 图片
     */
    public Bitmap rotaingImageView(int id, int angle, Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        //加入翻转 把相机拍照返回照片转正
        if (id == 1) {
            matrix.postScale(-1, 1);
        }
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 获取所有支持的预览尺寸
     *
     * @param list
     * @param minWidth
     * @return
     */
    public Size getPropPreviewSize(List<Size> list, int minWidth) {
        Collections.sort(list, ascendSizeComparator);

        int i = 0;
        for (Size s : list) {
            if ((s.width >= minWidth)) {
                break;
            }
            i++;
        }
        if (i == list.size()) {
            i = 0;//如果没找到，就选最小的size
        }
        return list.get(i);
    }

    /**
     * 获取所有支持的返回图片尺寸
     *
     * @param list
     * @param minWidth
     * @return
     */
    public Size getPropPictureSize(List<Size> list, int minWidth) {
        Collections.sort(list, ascendSizeComparator);

        int i = 0;
        for (Size s : list) {
            if ((s.width >= minWidth)) {
                break;
            }
            i++;
        }
        if (i == list.size()) {
            i = 0;//如果没找到，就选最小的size
        }
        return list.get(i);
    }

    public boolean equalRate(Size s, float rate) {
        float r = (float) (s.width) / (float) (s.height);
        return Math.abs(r - rate) <= 0.03;
    }

    //降序
    public static class CameraDropSizeComparator implements Comparator<Size> {
        public int compare(Size lhs, Size rhs) {
            if (lhs.width == rhs.width) {
                return 0;
            } else if (lhs.width < rhs.width) {
                return 1;
            } else {
                return -1;
            }
        }

    }

    //升序
    public class CameraAscendSizeComparator implements Comparator<Size> {
        public int compare(Size lhs, Size rhs) {
            if (lhs.width == rhs.width) {
                return 0;
            } else if (lhs.width > rhs.width) {
                return 1;
            } else {
                return -1;
            }
        }

    }

    /**
     * 打印支持的previewSizes
     *
     * @param params
     */
    public void printSupportPreviewSize(Camera.Parameters params) {
        List<Size> previewSizes = params.getSupportedPreviewSizes();
        for (int i = 0; i < previewSizes.size(); i++) {
            Size size = previewSizes.get(i);
        }

    }

    /**
     * 打印支持的pictureSizes
     *
     * @param params
     */
    public void printSupportPictureSize(Camera.Parameters params) {
        List<Size> pictureSizes = params.getSupportedPictureSizes();
        for (int i = 0; i < pictureSizes.size(); i++) {
            Size size = pictureSizes.get(i);
        }
    }

    /**
     * 打印支持的聚焦模式
     *
     * @param params
     */
    public void printSupportFocusMode(Camera.Parameters params) {
        List<String> focusModes = params.getSupportedFocusModes();
        for (String mode : focusModes) {
        }
    }

    /**
     * 打开闪关灯
     *
     * @param mCamera
     */
    public void turnLightOn(Camera mCamera) {
        if (mCamera == null) {
            return;
        }

        Camera.Parameters parameters = null;
        try {
            parameters = mCamera.getParameters();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (parameters == null) {
            return;
        }
        List<String> flashModes = parameters.getSupportedFlashModes();
        // Check if camera flash exists
        if (flashModes == null) {
            // Use the screen as a flashlight (next best thing)
            return;
        }
        String flashMode = parameters.getFlashMode();
        if (!Camera.Parameters.FLASH_MODE_ON.equals(flashMode)) {
            // Turn on the flash
            if (flashModes.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                mCamera.setParameters(parameters);
            } else {
            }
        }
    }


    /**
     * 自动模式闪光灯
     *
     * @param mCamera
     */
    public void turnLightAuto(Camera mCamera) {
        if (mCamera == null) {
            return;
        }

        Camera.Parameters parameters = null;
        try {
            parameters = mCamera.getParameters();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (parameters == null) {
            return;
        }
        List<String> flashModes = parameters.getSupportedFlashModes();
        // Check if camera flash exists
        if (flashModes == null) {
            // Use the screen as a flashlight (next best thing)
            return;
        }
        String flashMode = parameters.getFlashMode();
        if (!Camera.Parameters.FLASH_MODE_AUTO.equals(flashMode)) {
            // Turn on the flash
//            if (flashModes.contains(Camera.Parameters.FLASH_MODE_AUTO)) {
//                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
//                mCamera.setParameters(parameters);
//            } else {
//            }
            if (flashModes.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                mCamera.setParameters(parameters);
            } else {
            }
        }
    }


    /**
     * 关闭闪光灯
     *
     * @param mCamera
     */
    public void turnLightOff(Camera mCamera) {
        if (mCamera == null) {
            return;
        }

        Camera.Parameters parameters = null;
        try {
            parameters = mCamera.getParameters();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (parameters == null) {
            return;
        }
        List<String> flashModes = parameters.getSupportedFlashModes();
        String flashMode = parameters.getFlashMode();
        // Check if camera flash exists
        if (flashModes == null) {
            return;
        }
        if (!Camera.Parameters.FLASH_MODE_OFF.equals(flashMode)) {
            // Turn off the flash
            if (flashModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                mCamera.setParameters(parameters);
            } else {
            }
        }
    }

    /***
     * 初始化输出图偏分辨率，遍历Camera可选的输出分辨率，按照从大到小的顺序将宽高相乘，取所有宽为1xxx的分辨率进行匹配，设置给PictureSize
     */
    public static int[] initPictureSize(List<Size> sizeList, int screenWidth, int screenHeight) {
        int pictureWidth = 0;
        int pictureHeight = 0;
        if (sizeList.size() > 0) {
            if (sizeList.get(0).width >sizeList.get(sizeList.size() - 1).width)//如果是从大到小，则反序
                Collections.reverse(sizeList);
            for (Size size : sizeList) {
                LogUtil.e(TAG, "PictureSize-->" + size.width + "x" + size.height);
                if(size.height / 1000 == 0){
                    continue;
                }else if (size.height / 1000 == 1) {//拿到所有宽为1xxx的分辨率
                    pictureWidth = size.width;
                    pictureHeight = size.height;
                    if(size.width>1900 && size.width<2000 && size.height>1000 &&size.height<1100){//2、查找跟1920*1080最接近的分辨率
                        break;
                    }
                    if(size.width==screenHeight && size.height==screenWidth){//1、先查找跟屏幕分辨率一致的输出分辨率
                        break;
                    }
                    if(Math.abs(pictureWidth*1.0f/pictureHeight-screenHeight*1.0f/screenWidth)<0.1){//3、查找跟屏幕分辨率宽高比更接近的分辨率
                        break;
                    }
                }else if(size.height / 1000==2){
                    break;
                }
            }

            if (pictureWidth == 0 || pictureHeight == 0) {
                for (int i=0;i<sizeList.size();i++) {
                    Size size = sizeList.get(i);
                    if(size.height>1000 && size.height<2000 && size.width!=size.height){//去height 1000~2000中间最小的一个
                        pictureWidth = size.width;
                        pictureHeight = size.height;
                        break;
                    }
                }
            }
        } else {
            pictureWidth = screenWidth;
            pictureHeight = screenHeight;
            LogUtil.e(TAG, "输出图片大小取屏幕分辨率");
        }
        LogUtil.e(TAG, "final PictureSize++>" + pictureWidth + "*" + pictureHeight);
        return new int[]{pictureWidth,pictureHeight};
    }

    /***
     * 初始化预览图分辨率，遍历Camera可选的预览分辨率，按照从大到小的顺序将宽高相乘，取第一个>100W像素的宽高，设置给PreviewSize
     */
    public static int[] initPreviewSize(List<Size> sizeList, int screenWidth, int screenHeight) {
        int previewWidth = 0;
        int previewHeight = 0;
        if (sizeList.size() > 0) {
            if (sizeList.get(0).width < sizeList.get(sizeList.size() - 1).width) {//从小到大，则反序
                Collections.reverse(sizeList);
            }
            List<Size> tempList = new ArrayList<>();
            for (Size size : sizeList) {
                LogUtil.e(TAG, "PreviewSize-->" + size.width + "x" + size.height);
                if (size.height / 1000 == 1) {
                    tempList.add(size);
                    previewWidth = size.width;
                    previewHeight = size.height;
                    if(Math.abs(previewWidth-screenHeight)<30 && Math.abs(previewHeight-screenWidth)<30){//1、查找跟屏幕分辨率比较接近的分辨率
                        break;
                    }
                    if(Math.abs(previewWidth*1.0f/previewHeight-screenHeight*1.0f/screenWidth)<0.1 && previewWidth<=screenHeight && previewHeight<=screenWidth){//2、查找跟屏幕分辨率宽高比更接近但是比屏幕分辨率小的分辨率
                        break;
                    }
                    if(size.width>1900 && size.width<2000 && size.height>1000 &&size.height<1100){//2、查找跟1920*1080最接近的分辨率
                        break;
                    }
                }else if(size.width / 1000==0){
                    break;
                }
            }
            if(previewWidth==previewHeight && previewHeight!=0){//如果上一次循环最终的宽高相等，则循环tempList，取第一个宽高不相等的值
                for (Size size : tempList) {
                    if(size.width!=size.height){
                        previewWidth = size.width;
                        previewHeight = size.height;
                        break;
                    }
                }
            }
            if (previewWidth == 0 || previewHeight == 0) {//如果没有符合条件的，则取可选宽高最大值
                for (int i=sizeList.size()-1;i>=0;i--) {//这一步是为了防止出现没有1000-1999区间的分辨率，那就拿2000及以上最小的分辨率
                    Size size = sizeList.get(i);
                    if(size.width>1000 && size.width!=size.height){
                        previewWidth = size.width;
                        previewHeight = size.height;
                        break;
                    }
                }
            }
        } else {
            previewWidth = screenWidth;
            previewHeight = screenHeight;
            LogUtil.e(TAG, "预览图片大小取屏幕分辨率");
        }
        LogUtil.e(TAG, "final PreviewSize-->" + previewWidth + "*" + previewHeight);
        return new int[]{previewWidth,previewHeight};
    }

    /***
     * 初始化输出图偏分辨率，遍历Camera可选的输出分辨率，按照从大到小的顺序将宽高相乘，取所有宽为1xxx的分辨率进行匹配，设置给PictureSize
     */
    public static int[] initPictureSize(List<Size> sizeList) {
        int pictureWidth = 0;
        int pictureHeight = 0;
        float whRatio = 1.3333f;
        if (sizeList.size() > 0) {
            List<Size> tempList = new ArrayList<>();
            Collections.sort(sizeList, dropSizeComparator);
            for (Size size : sizeList) {
                LogUtil.e(TAG, "PictureSize-->" + size.width + "x" + size.height);
                if (Math.abs(whRatio - size.width * 1.0f / size.height) <= 0.05) {//如果高宽比差不多是4:3，则放入候选
                    tempList.add(size);
                    if (size.width == 3264 && size.height == 2448) {//如果正好有合适的，则break
                        pictureWidth = size.width;
                        pictureHeight = size.height;
                        break;
                    }
                }
            }

            if ((pictureWidth == 0 || pictureHeight == 0) && tempList.size() > 0) {//如果遍历完没有最合适那个
                for (Size size : tempList) {
                    if (Math.abs(size.width - 3264) <= 100) {
                        pictureWidth = size.width;
                        pictureHeight = size.height;
                        break;
                    }
                }
                if (pictureWidth == 0 || pictureHeight == 0) {
                    //取分辨率前判断下如果超过4000就跳过，这里非常关键！！！因为随着手机相机像素越来越高，照片越来越大，如果不限制的话，容易造成OOM
                    for (int i = 0; i <tempList.size() ; i++) {
                        if(tempList.get(i).width>4000){
                            continue;
                        }else {
                            pictureWidth = tempList.get(i).width;
                            pictureHeight = tempList.get(i).height;
                            break;
                        }

                    }

                    //如果还是没有值 取与屏幕分辨率接近的一个值，tempList倒序，取第一个比屏幕分辨率小的值
                    if(pictureWidth==0){
                        int[] wh =setScreenSize(SysApplication.getAppContext());
                        int screenWidth = wh[0];
                        int screenHeight = wh[1];
                        for (Size size : tempList) {
                            if (size.width - screenHeight< 0) {
                                pictureWidth = size.width;
                                pictureHeight = size.height;
                                break;
                            }
                        }

                    }

                    //如果到这里都没值那说明最小分辨率都比屏幕大，那就取最小值（当然这只是以防万一，这种情况基本不存在）
                    if (pictureWidth == 0){
                        pictureWidth = tempList.get(tempList.size()-1).width;
                        pictureHeight = tempList.get(tempList.size()-1).height;
                    }

                }
            }
        }
        LogUtil.e(TAG, "final PictureSize++>" + pictureWidth + "*" + pictureHeight);
        return new int[]{pictureWidth, pictureHeight};
    }

    public static boolean flag = true;

    /***
     * 初始化预览图分辨率，遍历Camera可选的预览分辨率，按照从大到小的顺序将宽高相乘，取第一个>100W像素的宽高，设置给PreviewSize
     * 相机默认是横屏的，所以预览的宽高默认是对应的横屏的，所以如果我们是相机是竖屏 那么预览的宽对应高，预览高对应宽
     * 预览分辨率，越高预览越清晰
     */
    public static int[] initPreviewSize(Camera.Parameters parameters, int screenWidth) {
        List<Size> sizeList = parameters.getSupportedPreviewSizes();
        int previewWidth = 0;
        int previewHeight = 0;
        float whRatio = 1.3333f;// 宽高比 4:3
        // 早期屏幕宽一般为480P 720P 1080P 这里做一个兼容设置 最小宽的控制-> 李波 on 2020/5/28.
        boolean needCompatible = screenWidth<1000 || Build.VERSION.SDK_INT<21;//
        int minResolution = needCompatible?480:720;
        if (sizeList.size() > 0) {

            if (sizeList.get(0).width >sizeList.get(sizeList.size() - 1).width) {//从大到小，则反序
                Collections.reverse(sizeList);
            }

            List<Size> okList  = new ArrayList<>();//全部4:3的分辨率的预览尺寸集合
            List<Size> exList  = new ArrayList<>();//setParameters failed的分辨率
            for (Size size : sizeList) {

                LogUtil.e(TAG, "PreviewSize-->" + size.width + "x" + size.height);

//                预览高size.height 对应竖屏模式下的宽
                if (size.height / minResolution >= 1) { //预览尺寸大于等于最小宽才接着走
                    if(Math.abs(whRatio-size.width*1.0f/size.height)<=0.05){ //遍历4:3的尺寸误差小于等于0.05 装入okList
                        okList.add(size);
                        try {
                            parameters.setPreviewSize(size.width,size.height);
                        }catch (RuntimeException e){
                            e.printStackTrace();
                            exList.add(size);
                        }
                    }
                }
            }
            okList.removeAll(exList);//剔除不符合的
            if(okList.size()>0){
                for (Size size : okList) {
                    //  预览高size.height 对应竖屏模式下的宽
                    if (size.height == screenWidth) {//查找其中有没有跟当前屏幕对应的分辨率，有则取之
                        previewWidth = size.width;
                        previewHeight = size.height;
                        break;
                    }
                }
                if (previewWidth == 0 || previewHeight == 0) {//如果经过上轮循环还是取不到
//                    previewWidth = okList.get(0).width;
//                    previewHeight = okList.get(0).height;
                    //预览分辨率，越高预览越清晰 取最大的一个
                    previewWidth = okList.get(okList.size()-1).width;
                    previewHeight = okList.get(okList.size()-1).height;
                }
            }


        }
        LogUtil.e(TAG, "final PreviewSize-->" + previewWidth + "*" + previewHeight);
        return new int[]{previewWidth,previewHeight};
    }

    //控制图像的正确显示方向
    public static void setDisplay(Camera.Parameters parameters, Camera camera) {
        if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
            setDisplayOrientation(camera, 90);
        } else {
            parameters.setRotation(90);
        }

    }

    //实现的图像的正确显示
    public static void setDisplayOrientation(Camera camera, int i) {
        Method downPolymorphic;
        try {
            downPolymorphic = camera.getClass().getMethod("setDisplayOrientation", int.class);
            if (downPolymorphic != null) {
                downPolymorphic.invoke(camera, i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap scaleBitmap(Bitmap saveBitmap, int pictureWidth, int pictureHeight) {
        if (saveBitmap.getWidth() < saveBitmap.getHeight())
            saveBitmap = Bitmap.createScaledBitmap(saveBitmap, pictureHeight, pictureWidth, true);
        else
            saveBitmap = Bitmap.createScaledBitmap(saveBitmap, pictureWidth, pictureHeight, true);
        return saveBitmap;
    }

    public static Bitmap rotateBitmap(Bitmap saveBitmap, int mCameraId, int picDegree, String imgPath) {
        if (mCameraId == 0) {
            if (picDegree == 0){
                saveBitmap = CameraUtil.rotateBitmapByDegree(imgPath, 90);
            }else if(picDegree == 90){
                saveBitmap = CameraUtil.rotateBitmapByDegree(imgPath, 180);
            }else if(picDegree == 180){
                saveBitmap = CameraUtil.rotateBitmapByDegree(imgPath, 270);
            }else if (picDegree == 270){
                saveBitmap = CameraUtil.rotateBitmapByDegree(imgPath, 0);
            }
        } else {
            if (picDegree == 0){
                saveBitmap = CameraUtil.rotateBitmapByDegree(imgPath, -90);
            }else if(picDegree == 90){
                saveBitmap = CameraUtil.rotateBitmapByDegree(imgPath, 180);
            }else if(picDegree == 180){
                saveBitmap = CameraUtil.rotateBitmapByDegree(imgPath, 90);
            }else if (picDegree == 270){
                saveBitmap = CameraUtil.rotateBitmapByDegree(imgPath, 0);
            }

        }
        return saveBitmap;
    }

    public static int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            return bitmap.getByteCount();
        }
        // 在低版本中用一行的字节x高度
        return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
    }



    @SuppressLint("NewApi")
    public static int[] setScreenSize(Context context){
        int x, y;
        WindowManager wm = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE));
        Display display = wm.getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2){
            Point screenSize = new Point();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
                display.getRealSize(screenSize);
                x = screenSize.x;
                y = screenSize.y;
            } else{
                display.getSize(screenSize);
                x = screenSize.x;
                y = screenSize.y;
            }
        } else{
            x = display.getWidth();
            y = display.getHeight();
        }
        return new int[]{x,y};
    }

    /**
     * 设置 surfaceView 的宽高
     * @param previewWidth    预览设置的宽
     * @param previewHeight   预览设置的高
     * @param sideValue       确定的一边的值，比如竖屏模式下要求预览界面撑满屏幕的宽，那么 surfaceViewWidth = sideValue = 屏幕宽
     * @param surfaceView
     * @param worh sideValue  值是宽 还是 高， 0 - 宽，1 - 高
     * @param screenOrientation  相机界面的屏幕模式， 0 - 竖屏，1 - 横屏
     *
     * 为了不混淆混乱  sideValue 传入进来的宽高值 一律相对于竖屏来说！！！
     *
     * previewWidth > previewHeight
     */
    public static void setSurfaceViewWH(int previewWidth, int previewHeight, SurfaceView surfaceView, int sideValue, int worh , int screenOrientation){
        int surfaceViewWidth = 0;
        int surfaceViewHeight = 0;

        //计算预览宽高比例
        float ratio = (float) previewWidth /(float)previewHeight;

        LogUtil.e("ratio", "ratio=" + ratio);

        //根据 worh 的值来计算 surfaceView 的另一边值
        switch (worh){
            case 0: // sideValue 竖屏宽
                if (screenOrientation == 0) { //当前相机为竖屏 那么 surfaceViewHeight 必须 大于 surfaceViewWidth
                    surfaceViewWidth = sideValue;
                    surfaceViewHeight = (int)((float)surfaceViewWidth * ratio);

                }else if(screenOrientation == 1){ //当前相机为横屏 那么 surfaceViewWidth 必须 大于 surfaceViewHeight
                    surfaceViewHeight = sideValue;
                    surfaceViewWidth  = (int)((float)surfaceViewHeight * ratio);
                }
            break;

            case 1: // sideValue 竖屏高
                if (screenOrientation == 0) {
                    surfaceViewHeight = sideValue;
                    surfaceViewWidth  = (int)((float)surfaceViewHeight / ratio);

                }else if(screenOrientation == 1){
                    surfaceViewWidth = sideValue;
                    surfaceViewHeight = (int)((float)surfaceViewWidth / ratio);
                }
            break;
        }



        if (surfaceViewWidth !=0 && surfaceViewHeight !=0){
            FrameLayout.LayoutParams  layoutParams = (FrameLayout.LayoutParams) surfaceView.getLayoutParams();
            layoutParams.width  = surfaceViewWidth;
            layoutParams.height = surfaceViewHeight;
            surfaceView.setLayoutParams(layoutParams);
        }

    }

}
