package com.example.jzg.myapplication.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by 李波 on 2017/5/5.
 * 相机照片数据
 */
public class PictureItem implements Parcelable,Serializable{


    private String PicId;
    private String PicName;
    private String PicPath;//大图路径，提交和查看大图用
    private String thumbnailPath;//缩略图路径，显示用
    private boolean isSmallCompress;//是否为少量压缩，默认为false

    public String getPicId() {
        return PicId;
    }

    public void setPicId(String PicId) {
        this.PicId = PicId;
    }

    public String getPicName() {
        return PicName;
    }

    public void setPicName(String PicName) {
        this.PicName = PicName;
    }

    public String getPicPath() {
        return PicPath;
    }

    public void setPicPath(String picPath) {
        PicPath = picPath;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }


    public boolean isSmallCompress() {
        return isSmallCompress;
    }

    public void setSmallCompress(boolean smallCompress) {
        isSmallCompress = smallCompress;
    }

    public PictureItem() {
    }

    public PictureItem(String picId, String picName, String picPath) {
        PicId = picId;
        PicName = picName;
        PicPath = picPath;
    }
    public PictureItem(String picId, String picName, String picPath, String thumbnailPath) {
        this(picId,picName,picPath);
        this.thumbnailPath = thumbnailPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.PicId);
        dest.writeString(this.PicName);
        dest.writeString(this.PicPath);
        dest.writeString(this.thumbnailPath);
    }

    protected PictureItem(Parcel in) {
        this.PicId = in.readString();
        this.PicName = in.readString();
        this.PicPath = in.readString();
        this.thumbnailPath = in.readString();
    }

    public static final Creator<PictureItem> CREATOR = new Creator<PictureItem>() {
        @Override
        public PictureItem createFromParcel(Parcel source) {
            return new PictureItem(source);
        }

        @Override
        public PictureItem[] newArray(int size) {
            return new PictureItem[size];
        }
    };

}
