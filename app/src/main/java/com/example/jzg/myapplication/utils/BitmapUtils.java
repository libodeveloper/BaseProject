package com.example.jzg.myapplication.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Build;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by jzg on 2016/3/2.
 */
public class BitmapUtils {
    private static String TAG = "BitmapUtils";

    // 读取图像的旋转度
    public int readBitmapDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
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
        return degree;
    }

    // 旋转图片
    public Bitmap rotateBitmap(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        return resizedBitmap;
//        return bitmap;
    }

    /***
     *
     * @param path 图片源文件路径
     * @param spath 压缩后保存路径
     * @param yaSuoBi 压缩比
     * @return
     */
    public boolean saveImage(String path, String spath, int yaSuoBi) {
        BufferedOutputStream bos = null;
        Bitmap photo = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(spath, false));
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, opt);
            // 获取到这个图片的原始宽度和高度
            int picWidth = opt.outWidth;
            int picHeight = opt.outHeight;
            LogUtil.e(TAG, "src picWidth=" + picWidth + ",picHeight=" + picHeight);
            opt.inSampleSize = 1;
            opt.inJustDecodeBounds = false;
            photo = BitmapFactory.decodeFile(path, opt);
            LogUtil.e(TAG, "final picWidth=" + picWidth + ",picHeight=" + picHeight);
            photo.compress(Bitmap.CompressFormat.JPEG, yaSuoBi, bos);
            bos.flush();
            photo.recycle();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (bos != null)
                    bos.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public boolean saveImage(String path, String spath) {
        BufferedOutputStream bos = null;
        try {

            bos = new BufferedOutputStream(new FileOutputStream(spath, false));
            Bitmap photo = BitmapFactory.decodeFile(path);
            photo.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            photo.recycle();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (bos != null)
                    bos.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    //获取bitmap大小
    public static int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            return bitmap.getByteCount();
        }
        return 0;
    }

    //根据屏幕分辨率压缩
    public static Bitmap compress(byte[] data, int desiredWidth, int desiredHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        int width = options.outWidth;
        int height = options.outHeight;
        LogUtil.e("TAG", "图片到大小" + width + "," + height);
        if (width > height) {
            width = height + width;
            height = width - height;
            width = width - height;
        }
//        int desiredWidth = 1080;
//        int desiredHeight = 1920;
        int inSampleSize = 1;
        if (width > desiredWidth || height > desiredHeight) {
            int halfWidth = width / 2;
            int halfHeight = height / 2;

            while ((halfWidth / inSampleSize) >= desiredWidth &&
                    (halfHeight / inSampleSize) >= desiredHeight) {
                inSampleSize *= 2;
            }
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        LogUtil.e("TAG", "压缩后图片的大小" + (bitmap.getByteCount() / 1024 / 1024) + "M宽度为" + bitmap.getWidth() + "高度为" + bitmap.getHeight());
        return bitmap;
    }

}
