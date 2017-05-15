package com.example.jzg.myapplication.cameracustom;

import android.content.Context;
import android.graphics.Rect;
import android.hardware.Camera;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;


import com.example.jzg.myapplication.utils.LogUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 要求：拍照比例宽高比为4:3
 * A basic Camera preview class
 * Created by zealjiang on 2016/9/12 15:47.
 * Email: zealjiang@126.com
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback ,Camera.AutoFocusCallback{
    private final String TAG = "CameraPreview";
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private CameraActivity activity;
    private boolean isPreviewing = false;

    public boolean isPreviewing() {
        return isPreviewing;
    }

    public CameraPreview(Context context, CameraActivity activity) {
        super(context);
        Log.e(TAG, "CameraPreview()");

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        this.activity = activity;
    }

    public void setIsPreviewing(boolean isPreviewing){
        this.isPreviewing = isPreviewing;
    }

    protected boolean getIsPreviewing(){
        return this.isPreviewing;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        Log.e(TAG, "surfaceCreated()" + mCamera);
        if(mCamera==null){
//            Toast.makeText(activity,"初始化相机预览失败",Toast.LENGTH_SHORT).show();
            return;
        }
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            //此处在屏幕旋转锁屏解锁后Camera.release()会调用，所以在此前检查是否Camera是否release了
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
            isPreviewing = true;
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.e(TAG, "surfaceDestroyed()" + mCamera);
        isPreviewing = false;
        // empty. Take care of releasing the Camera preview in your activity.
        isPreviewing = false;
        activity.releaseCamera();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        Log.e(TAG, "surfaceChanged()");
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            isPreviewing = false;
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
            isPreviewing = true;
        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }


    public void setCamera(Camera camera) {
        mCamera = camera;
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
            isPreviewing = true;
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }


    public void handleFocus(MotionEvent event,int realPreWidth,int realPreHeight) {
        Camera.Parameters params = mCamera.getParameters();
        Camera.Size previewSize = params.getPreviewSize();//注意todo：用的是屏幕尺寸 w*h: 1920*1080,因为屏幕有系统titleBar和触控条，实际预览区没屏幕尺寸大，有出入
        Rect focusRect = calculateTapArea(event.getX(), event.getY(), 1f, realPreWidth,realPreHeight);
        Rect meteringRect = calculateTapArea(event.getX(), event.getY(),1.5f,realPreWidth,realPreHeight);

        params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        if (params.getMaxNumFocusAreas() > 0) {
            List<Camera.Area> focusAreas = new ArrayList<>();
            focusAreas.add(new Camera.Area(focusRect, 1000));
            params.setFocusAreas(focusAreas);
        } else {
            LogUtil.e(TAG, "focus areas not supported 不持区域对焦");
        }

        if (params.getMaxNumMeteringAreas() > 0) {
            List<Camera.Area> meteringAreas = new ArrayList<>();
            meteringAreas.add(new Camera.Area(meteringRect, 1000));
            params.setMeteringAreas(meteringAreas);
        }


        mCamera.setParameters(params);
        try {
            mCamera.autoFocus(this);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private  Rect calculateTapArea(float x, float y, float coefficient,int previewWidth,int previewHeight) {
        float focusAreaSize = 300;
        int areaSize = Float.valueOf(focusAreaSize * coefficient).intValue();

        int centerX = (int) (x / previewWidth * 2000 - 1000);
        int centerY = (int) (y / previewHeight * 2000 - 1000);

        int left = clamp(centerX - areaSize / 2, -1000, 1000);
        int top = clamp(centerY - areaSize / 2, -1000, 1000);
        int right = clamp(left + areaSize, -1000, 1000);
        int bottom = clamp(top + areaSize, -1000, 1000);

        return new Rect(left, top,right,bottom);
    }

    protected int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        if (x < min) {
            return min;
        }
        return x;
    }


    @Override
    public void onAutoFocus(boolean success, Camera camera) {

    }
}
