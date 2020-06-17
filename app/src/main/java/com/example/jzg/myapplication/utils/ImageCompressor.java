package com.example.jzg.myapplication.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;


import com.blankj.utilcode.utils.ImageUtils;
import com.example.jzg.myapplication.interfaces.OnCompressListener;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by 李波 on 2017/5/8.
 * 压缩
 */
public class ImageCompressor {
    public static final int FIRST_GEAR = 1;
    public static final int THIRD_GEAR = 3;
    public static final int SELF_GEAR = 4;

    private static final String TAG = "ImageCompressor";
    private static String DEFAULT_DISK_CACHE_DIR = "compress_cache";

    private static volatile ImageCompressor INSTANCE;

    private final File mCacheDir;

    private OnCompressListener compressListener;
    private File mFile;
    private int gear = THIRD_GEAR;
    private String filename;

    private ImageCompressor(File cacheDir) {
        mCacheDir = cacheDir;
    }

    /**
     * Returns a directory with a default name in the private cache directory of the application to use to store
     * retrieved media and thumbnails.
     *
     * @param context A context.
     * @see #getPhotoCacheDir(Context, String)
     */
    private static synchronized File getPhotoCacheDir(Context context) {
        return getPhotoCacheDir(context, DEFAULT_DISK_CACHE_DIR);
    }

    /**
     * Returns a directory with the given name in the private cache directory of the application to use to store
     * retrieved media and thumbnails.
     *
     * @param context   A context.
     * @param cacheName The name of the subdirectory in which to store the cache.
     * @see #getPhotoCacheDir(Context)
     */
    private static File getPhotoCacheDir(Context context, String cacheName) {
        File tmpPath = new File(Environment.getExternalStorageDirectory(), cacheName);
        LogUtil.e(TAG,"tmpPath:"+tmpPath);
        if(!tmpPath.exists()){
            tmpPath.mkdirs();
        }
        return tmpPath;
    }

    public static ImageCompressor get(Context context) {
        if (INSTANCE == null)
            INSTANCE = new ImageCompressor(getPhotoCacheDir(context));
        return INSTANCE;
    }

    public ImageCompressor launch() {
        checkNotNull(mFile, "the image file cannot be null, please call .load() before this method!");

        if (compressListener != null) compressListener.onStart();

        if (gear == FIRST_GEAR)
            Observable.just(mFile)
                    .map(new Func1<File, File>() {
                        @Override
                        public File call(File file) {
                            return firstCompress(file);
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            if (compressListener != null) compressListener.onError(throwable);
                        }
                    })
                    .onErrorResumeNext(Observable.<File>empty())
                    .filter(new Func1<File, Boolean>() {
                        @Override
                        public Boolean call(File file) {
                            return file != null;
                        }
                    })
                    .subscribe(new Action1<File>() {
                        @Override
                        public void call(File file) {
                            if (compressListener != null) compressListener.onSuccess(file);
                        }
                    });
        else if (gear == THIRD_GEAR)
            Observable.just(mFile)
                    .map(new Func1<File, File>() {
                        @Override
                        public File call(File file) {
                            return thirdCompress(file);
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            if (compressListener != null) compressListener.onError(throwable);
                        }
                    })
                    .onErrorResumeNext(Observable.<File>empty())
                    .filter(new Func1<File, Boolean>() {
                        @Override
                        public Boolean call(File file) {
                            return file != null;
                        }
                    })
                    .subscribe(new Action1<File>() {
                        @Override
                        public void call(File file) {
                            if (compressListener != null) compressListener.onSuccess(file);
                        }
                    });
        else if(gear == SELF_GEAR)
            Observable.just(mFile)
                    .map(new Func1<File, File>() {
                        @Override
                        public File call(File file) {
                            return compressImage(file);
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            if (compressListener != null) compressListener.onError(throwable);
                        }
                    })
                    .onErrorResumeNext(Observable.<File>empty())
                    .filter(new Func1<File, Boolean>() {
                        @Override
                        public Boolean call(File file) {
                            return file != null;
                        }
                    })
                    .subscribe(new Action1<File>() {
                        @Override
                        public void call(File file) {
                            if (compressListener != null) compressListener.onSuccess(file);
                        }
                    });

        return this;
    }

    public ImageCompressor load(File file) {
        mFile = file;
        return this;
    }
    public ImageCompressor load(String filePath) {
        mFile = new File(filePath);
        return this;
    }

    public ImageCompressor setCompressListener(OnCompressListener listener) {
        compressListener = listener;
        return this;
    }

    public ImageCompressor putGear(int gear) {
        this.gear = gear;
        return this;
    }

    /**
     * @deprecated
     */
    public ImageCompressor setFilename(String filename) {
        this.filename = filename;
        return this;
    }

    public Observable<File> asObservable() {
        if (gear == FIRST_GEAR)
            return Observable.just(mFile).map(new Func1<File, File>() {
                @Override
                public File call(File file) {
                    return firstCompress(file);
                }
            });
        else if (gear == THIRD_GEAR)
            return Observable.just(mFile).map(new Func1<File, File>() {
                @Override
                public File call(File file) {
                    return thirdCompress(file);
                }
            });
        else return Observable.empty();
    }

    private File thirdCompress(@NonNull File file) {
        String thumb = mCacheDir.getAbsolutePath() + File.separator +(TextUtils.isEmpty(filename) ? System.currentTimeMillis() : filename) + ".jpg";
        double size;
        String filePath = file.getAbsolutePath();



        int angle = getImageSpinAngle(filePath);
        int width = getImageSize(filePath)[0];
        int height = getImageSize(filePath)[1];

        //宽度和高度为偶数
        int thumbW = width % 2 == 1 ? width + 1 : width;
        int thumbH = height % 2 == 1 ? height + 1 : height;

        //宽度要小于高度
        width = thumbW > thumbH ? thumbH : thumbW;
        height = thumbW > thumbH ? thumbW : thumbH;

        double scale = ((double) width / height);

        if (scale <= 1 && scale > 0.5625) {
            if (height < 1664) {
                if (file.length() / 1024 < 150) return file;

                size = (width * height) / Math.pow(1664, 2) * 150;
                size = size < 60 ? 60 : size;
            } else if (height >= 1664 && height < 4990) {
                thumbW = width / 2;
                thumbH = height / 2;
                size = (thumbW * thumbH) / Math.pow(2495, 2) * 300;
                size = size < 60 ? 60 : size;
            } else if (height >= 4990 && height < 10240) {
                thumbW = width / 4;
                thumbH = height / 4;
                size = (thumbW * thumbH) / Math.pow(2560, 2) * 300;
                size = size < 100 ? 100 : size;
            } else {
                int multiple = height / 1280 == 0 ? 1 : height / 1280;
                thumbW = width / multiple;
                thumbH = height / multiple;
                size = (thumbW * thumbH) / Math.pow(2560, 2) * 300;
                size = size < 100 ? 100 : size;
            }
        } else if (scale <= 0.5625 && scale > 0.5) {
            if (height < 1280 && file.length() / 1024 < 200) return file;

            int multiple = height / 1280 == 0 ? 1 : height / 1280;
            thumbW = width / multiple;
            thumbH = height / multiple;
            size = (thumbW * thumbH) / (1440.0 * 2560.0) * 400;
            size = size < 100 ? 100 : size;
        } else {
            int multiple = (int) Math.ceil(height / (1280.0 / scale));
            thumbW = width / multiple;
            thumbH = height / multiple;
            size = ((thumbW * thumbH) / (1280.0 * (1280 / scale))) * 500;
            size = size < 100 ? 100 : size;
        }

        return compress(filePath, thumb, thumbW, thumbH, angle, (long) size);
    }

    private File firstCompress(@NonNull File file) {
        int minSize = 60;
        int longSide = 720;
        int shortSide = 1280;

        String filePath = file.getAbsolutePath();
        String thumbFilePath = mCacheDir.getAbsolutePath() + File.separator +
                (TextUtils.isEmpty(filename) ? System.currentTimeMillis() : filename) + ".jpg";

        long size = 0;
        long maxSize = file.length() / 5;

        int angle = getImageSpinAngle(filePath);
        int[] imgSize = getImageSize(filePath);
        int width = 0, height = 0;
        if (imgSize[0] <= imgSize[1]) {
            double scale = (double) imgSize[0] / (double) imgSize[1];
            if (scale <= 1.0 && scale > 0.5625) {
                width = imgSize[0] > shortSide ? shortSide : imgSize[0];
                height = width * imgSize[1] / imgSize[0];
                size = minSize;
            } else if (scale <= 0.5625) {
                height = imgSize[1] > longSide ? longSide : imgSize[1];
                width = height * imgSize[0] / imgSize[1];
                size = maxSize;
            }
        } else {
            double scale = (double) imgSize[1] / (double) imgSize[0];
            if (scale <= 1.0 && scale > 0.5625) {
                height = imgSize[1] > shortSide ? shortSide : imgSize[1];
                width = height * imgSize[0] / imgSize[1];
                size = minSize;
            } else if (scale <= 0.5625) {
                width = imgSize[0] > longSide ? longSide : imgSize[0];
                height = width * imgSize[1] / imgSize[0];
                size = maxSize;
            }
        }

        return compress(filePath, thumbFilePath, width, height, angle, size);
    }

    /**
     * obtain the image's width and height
     *
     * @param imagePath the path of image
     */
    public int[] getImageSize(String imagePath) {
        int[] res = new int[2];

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 1;
        BitmapFactory.decodeFile(imagePath, options);

        res[0] = options.outWidth;
        res[1] = options.outHeight;

        return res;
    }

    /**
     * obtain the thumbnail that specify the size
     *
     * @param imagePath the target image path
     * @param width     the width of thumbnail
     * @param height    the height of thumbnail
     * @return {@link Bitmap}
     */
    private Bitmap compress(String imagePath, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);

        int outH = options.outHeight;
        int outW = options.outWidth;
        int inSampleSize = 1;

        if (outH > height || outW > width) {
            int halfH = outH / 2;
            int halfW = outW / 2;

            while ((halfH / inSampleSize) > height && (halfW / inSampleSize) > width) {
                inSampleSize *= 2;
            }
        }

        options.inSampleSize = inSampleSize;

        options.inJustDecodeBounds = false;

        int heightRatio = (int) Math.ceil(options.outHeight / (float) height);
        int widthRatio = (int) Math.ceil(options.outWidth / (float) width);

        if (heightRatio > 1 || widthRatio > 1) {
            if (heightRatio > widthRatio) {
                options.inSampleSize = heightRatio;
            } else {
                options.inSampleSize = widthRatio;
            }
        }

        //by zj  小图片小压缩
        int max = options.outHeight>options.outWidth ? options.outHeight : options.outWidth;
        if(max<=1920&&options.inSampleSize>2){
            options.inSampleSize = 2;
        }

        options.inJustDecodeBounds = false;


        try {
            return BitmapFactory.decodeFile(imagePath, options);
        }catch (OutOfMemoryError e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * obtain the image rotation angle
     *
     * @param path path of target image
     */
    private int getImageSpinAngle(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
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
        return degree;
    }

    /**
     * 指定参数压缩图片
     * create the thumbnail with the true rotate angle
     *
     * @param largeImagePath the big image path
     * @param thumbFilePath  the thumbnail path
     * @param width          width of thumbnail
     * @param height         height of thumbnail
     * @param angle          rotation angle of thumbnail
     * @param size           the file size of image
     */
    private File compress(String largeImagePath, String thumbFilePath, int width, int height, int angle, long size) {
        Bitmap thbBitmap = compress(largeImagePath, width, height);

        thbBitmap = rotatingImage(angle, thbBitmap);

        return saveImage(thumbFilePath, thbBitmap, size+130);//加100是为了提高照片质量
    }

    /**
     * 旋转图片
     * rotate the image with specified angle
     *
     * @param angle  the angle will be rotating 旋转的角度
     * @param bitmap target image               目标图片
     */
    private static Bitmap rotatingImage(int angle, Bitmap bitmap) {
        //rotate image
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);

        //create a new image
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 保存图片到指定路径
     * Save image with specified size
     *
     * @param filePath the image file deleteItem path 储存路径
     * @param bitmap   the image what be deleteItem   目标图片
     * @param size     the file size of image   期望大小
     */
    private File saveImage(String filePath, Bitmap bitmap, long size) {
        checkNotNull(bitmap, TAG + "bitmap cannot be null");

        File result = new File(filePath.substring(0, filePath.lastIndexOf("/")));

        if (!result.exists() && !result.mkdirs()) return null;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int options = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, options, stream);

        while (stream.toByteArray().length / 1024 > size && options > 6) {
            stream.reset();
            options -= 6;
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, stream);
        }

        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(stream.toByteArray());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new File(filePath);
    }

    static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param reference    an object reference
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *                     string using {@link String#valueOf(Object)}
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    static <T> T checkNotNull(T reference, @Nullable Object errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
        return reference;
    }

    public static  Bitmap getImageThumbnail(String imagePath, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    /**
     * 保存图片
     * @param bmp
     */
    public static boolean saveImage(Bitmap bmp,String thumbnailPath) {
        File file = new File(thumbnailPath);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    private File compressImage(@NonNull File file) {
        String thumb = mCacheDir.getAbsolutePath() + File.separator +(TextUtils.isEmpty(filename) ? System.currentTimeMillis() : filename) + ".jpg";
        String imagePath = file.getAbsolutePath();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

        checkNotNull(bitmap, TAG + "bitmap cannot be null");
        File result = new File(thumb.substring(0, thumb.lastIndexOf("/")));
        if (!result.exists() && !result.mkdirs()) return null;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int quality = 100;
        while(baos.toByteArray().length/1024>500){//循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            quality -=3;//每次都减少6
            bitmap.compress(Bitmap.CompressFormat.JPEG,quality,baos);//这里压缩options%，把压缩后的数据存放到baos中
        }

        try {
            FileOutputStream fos = new FileOutputStream(thumb);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new File(thumb);
    }


    /***
     * 从相册选取照片
     * @param filePath
     * @return
     */
    public static Bitmap loadBitmapFile(String filePath){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 1;
        BitmapFactory.decodeFile(filePath,options);
        LogUtil.e(TAG,"src:"+(options.outWidth+"x"+options.outHeight));
        int inSampleSize = calculateInSampleSize(options, 3264); // 以3264分辨率为基准缩放
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;

/*******************************************************************************************************/

        //当前分配给APP剩余内存
        float memorysurplus = MemoryUtil.getMemoryInfo();

        //计算生成当前bitmap需要多大内存 长 * 宽 * 4Byte （默认ARGB_8888 模式 一个像素点占8+8+8+8=32位，4个字节）
        float srcbitmapmemorySize = (float) (options.outWidth * options.outHeight * 4 * 1.0/ (1024 * 1024));

        float bitmapmemorySize = (float) ((options.outWidth/inSampleSize) * ( options.outHeight/inSampleSize) * 4 * 1.0/ (1024 * 1024));

        LogUtil.e(TAG, "原始bitmap需要内存 == "+ srcbitmapmemorySize);
        LogUtil.e(TAG, "缩放系数inSampleSize == "+ inSampleSize);
        LogUtil.e(TAG, "当前剩余内存 == "+memorysurplus+"  生成bitmap需要内存 = "+bitmapmemorySize);
        //当需要内存 超过 剩余内存就会引发OOM，但是！！！这只能是在程序持续不断的运行中才会发生。
        //什么叫持续运行，比如点击选择照片后，不再进行任何人手操作，程序自己在进行压缩缩放旋转等等一系列运行过程中，这个时候如果需要内存大于剩余内存就会OOM
        //反之如果连续点击N个照片选择，虽然到了一定数量打印出剩余内存小于所需了，但因为手动操作有间隙，哪怕有毫秒级的间隙，系统也可能回收了部分内存
        //所以这个时候虽然打印出剩余内存小于所需了，但也不会OOM
/*******************************************************************************************************/

        //根据缩放系数生成不OOM的bitmap
        Bitmap src = BitmapFactory.decodeFile(filePath,options);

        LogUtil.e(TAG,"缩放前 = "+src.getWidth()+" x "+src.getHeight());
        float scaledRate = getScaledRate(src,3264);
        if(scaledRate!=1){
            LogUtil.e(TAG,"缩放至 = "+src.getWidth()*scaledRate+" x "+src.getHeight()*scaledRate+" 缩放系数"+scaledRate);
            src = scaleImage(src,(int)(src.getWidth()*scaledRate),(int)(src.getHeight()*scaledRate));
        }
        LogUtil.e(TAG,"scaled:"+(src.getWidth()+"x"+src.getHeight())+","+(ImageUtils.bitmap2Bytes(src, Bitmap.CompressFormat.JPEG).length/1024+"kb =============================="));
//        loadBitmapFile(filePath);

        return src;
    }

    /**
     * Created by 李波 on 2020/6/16.
     * 计算缩放系数
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,int largerSideMaxLength) {
        int inSampleSize = 1;
        int lagerSide = Math.max(options.outWidth,options.outHeight);//宽高中较大的一边的长度(像素)
        if(lagerSide>largerSideMaxLength){
            int lagerSideHalf = lagerSide/2;
            while (lagerSideHalf / inSampleSize>largerSideMaxLength) {
                inSampleSize *= 2;
            }

            //如果缩放后的大小还比限定最大值大1000以上，那就继续缩放，为了防止现在越来越大的照片生成bitmap时吃太多内存
            //以1亿像素图片举列，9024*12032 * 4 * 1.0/ (1024 * 1024) = 414.18MB
            //假设当前设备最大分配app内存为192MB，那么直接加载自然会OOM
            //按上面的缩放计算出 inSampleSize = 2, 最终的结果是 4512 * 6016，小了4倍，也就是100多MB，一次就吃100M内存
            //100多M < 192M 虽然这样生成一次bitmap不会OOM，但如果持续不断的这样生成 要不了几次就会OOM了
            //所以必须尽量的控制缩放的系数，这里6016 比 3264限定值快大两倍了，所以我们把它缩放到比较接近的值这样生成bitmap所需内存就小了
            //虽然可以在 AndroidManifest.xml  <application 下加上 android:largeHeap="true" 来申请最大分配内存，实际这样确实也减少很多OOM
            //但不能因为这样就不优化内存的使用，性能始终第一嘛
            while (lagerSide/inSampleSize - largerSideMaxLength>1000){
                LogUtil.e(TAG," calculateSide= "+lagerSide/inSampleSize);
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }


    /**
     * Created by 李波 on 2020/6/16.
     * 缩放宽高到限定的值
     * 当根据 inSampleSize 生成bitmap后，如果宽高有大于限定值的就缩放至限定值
     */
    public static Bitmap scaleImage(Bitmap src, int scaleWidth, int scaleHeight) {
        if (src == null) return null;
        Bitmap res = null;
        try {

 /*******************************************************************************************************/

            float memorysurplus = MemoryUtil.getMemoryInfo();

            //计算生成当前bitmap需要多大内存 长 * 宽 * 4Byte （默认ARGB_8888 模式 一个像素点占8+8+8+8=32位，4个字节）
            float bitmapmemorySize = (float) (scaleWidth*scaleHeight * 4 * 1.0/ (1024 * 1024));

            LogUtil.e("MemoryUtil", "当前剩余内存 == "+memorysurplus+"  bitmap需要内存 = "+bitmapmemorySize);

/*******************************************************************************************************/

            res = Bitmap.createScaledBitmap(src, scaleWidth, scaleHeight, false);

        }catch (Error error){

            LogUtil.e(TAG,error.getMessage());
            MemoryUtil.getMemoryInfo();
        }

        if (res == null) {
            return src;
        }
        if (res != src && !src.isRecycled()) {
            src.recycle();
        }
        return res;
    }

    /***
     * 计算图片的宽高调整比
     * @param src 需要处理的图片
     * @param rule 参考值
     * @return
     */
    public static float getScaledRate(Bitmap src, int rule){
        float rate = 1.0f;
        if(src==null)
            return  rate;
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        if(srcWidth>=srcHeight){
            if(srcWidth>rule){
                rate = rule*1.0f/srcWidth;
            }
        }else if(srcWidth<srcHeight){
            if(srcHeight>rule){
                rate = rule*1.0f/srcHeight;
            }
        }
        return rate;
    }

    /**
     * 旋转图片
     *
     * @param src     源图片
     * @param degrees 旋转角度
     */
    public static Bitmap rotateBitmap(Bitmap src, int degrees) {
        if (src == null || degrees == 0) return src;
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees, src.getWidth() / 2, src.getHeight() / 2);
        Bitmap res = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        if (!src.isRecycled()) src.recycle();
        return res;
    }

    /**
     * 保存图片
     *
     * @param src      源图片
     * @param filePath 要保存到的文件
     * @param format   格式
     * @param quality  压缩质量
     * @return {@code true}: 成功<br>{@code false}: 失败
     */
    public static boolean save(Bitmap src, String filePath, Bitmap.CompressFormat format,int quality) {
        if (src==null || TextUtils.isEmpty(filePath))
            return false;
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(filePath,false));
            return src.compress(format, quality, bos);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                bos.flush();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
