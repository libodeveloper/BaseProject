/*
 * Copyright (C) 2014 pengjianbo(pengjianbosoft@gmail.com), Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.example.jzg.myapplication.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import com.example.jzg.myapplication.R;
import com.example.jzg.myapplication.global.Constants;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import cn.finalteam.galleryfinal.widget.GFImageView;

/**
 * Desction:fresco image loader
 * Author:pengjianbo
 * Date:15/12/24 下午9:34
 */
public class FrescoImageLoader implements cn.finalteam.galleryfinal.ImageLoader {

    private Context context;

    private static ResizeOptions resizeOptions = new ResizeOptions(Constants.FRESCOL_COMPRESSION_COEFFICIENT, Constants.FRESCOL_COMPRESSION_COEFFICIENT);

    private static BaseControllerListener baseControllerListener = new BaseControllerListener<ImageInfo>();

    public FrescoImageLoader(Context context) {
        this(context, Bitmap.Config.RGB_565);
    }

    public FrescoImageLoader(Context context, Bitmap.Config config) {
        this.context = context;
        ImagePipelineConfig imagePipelineConfig = ImagePipelineConfig.newBuilder(context)
                .setBitmapsConfig(config)
                .build();
        Fresco.initialize(context, imagePipelineConfig);

    }


    @Override
    public void displayImage(Activity activity, String path, final GFImageView imageView, final Drawable defaultDrawable, int width, int height) {
        Resources resources = context.getResources();
        GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(resources)
                .setFadeDuration(300)
                .setPlaceholderImage(defaultDrawable)
                .setFailureImage(defaultDrawable)
                .setProgressBarImage(new ProgressBarDrawable())
                .build();

        final DraweeHolder<GenericDraweeHierarchy> draweeHolder = DraweeHolder.create(hierarchy, context);
        imageView.setOnImageViewListener(new GFImageView.OnImageViewListener() {
            @Override
            public void onDetach() {
                draweeHolder.onDetach();
            }

            @Override
            public void onAttach() {
                draweeHolder.onAttach();
            }

            @Override
            public boolean verifyDrawable(Drawable dr) {
                if (dr == draweeHolder.getHierarchy().getTopLevelDrawable()) {
                    return true;
                }
                return false;
            }

            @Override
            public void onDraw(Canvas canvas) {
                Drawable drawable = draweeHolder.getHierarchy().getTopLevelDrawable();
                if (drawable == null) {
                    imageView.setImageDrawable(defaultDrawable);
                } else {
                    imageView.setImageDrawable(drawable);
                }
            }
        });
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_FILE_SCHEME)
                .path(path)
                .build();
        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(uri)
                .setAutoRotateEnabled(true)
                .setResizeOptions(new ResizeOptions(width, height))//图片目标大小
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(draweeHolder.getController())
                .setImageRequest(imageRequest)
                .build();
        draweeHolder.setController(controller);
    }

    @Override
    public void clearMemoryCache() {

    }

    //------------当前使用以下加载图片保证加载的是缩略图不卡顿----------------------------------------------------  -> 李波 on 2016/12/2.

    /**
     * Created by 李波 on 2016/12/2.
     * 初始化配置Fresco，以便加载图片时加载缩略图不卡顿
     */
    public  static void FrescoInit(Context context) {
        ImagePipelineConfig imagePipelineConfig = ImagePipelineConfig.newBuilder(context)
                .setDownsampleEnabled(true)
                .build();
        Fresco.initialize(context, imagePipelineConfig);
    }

    /**
     * Created by 李波 on 2016/12/2.
     * Fresco 加载图片，保证加载的是缩略图，避免引起卡顿现象
     * Constants.FRESCOL_COMPRESSION_COEFFICIENT ----- 图片生成缩略图时的压缩系数
     * path : 图片地址，无需拼接如：file：// 等前缀，直接传地址
     */
    public static void displayImage(Context context, SimpleDraweeView draweeView, String path){
        Uri uri= Uri.parse("");   //初始化为空，如果地址为空，或地址错误就让其加载失败，刷新显示失败图片。
        if (draweeView == null)
            return;

        if (path!=null&&path.startsWith("http")){
            uri = Uri.parse(path);
        }else if(path!=null&&path.startsWith(Environment.getExternalStorageDirectory()+"")){
            uri = Uri.parse("file://"+path);
        }


        if (TextUtils.isEmpty(path)){
            loadResDrawablePic(context,draweeView, R.drawable.xiangji);
        }else {
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setResizeOptions(resizeOptions)
                    .setAutoRotateEnabled(true)
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(draweeView.getController())
                    .setControllerListener(baseControllerListener)
                    .build();
            draweeView.setController(controller);
        }
    }
    //----------------------------------------------------------------  -> 李波 on 2016/12/2.
    /**
     * 加载本地图片（drawable图片）
     * @param context
     * @param simpleDraweeView
     * @param id
     */
    public static void loadResDrawablePic(Context context, SimpleDraweeView simpleDraweeView, int id) {
        Uri uri = Uri.parse("res://" +
                context.getPackageName() +
                "/" + id);
        simpleDraweeView.setImageURI(uri);
    }

}
