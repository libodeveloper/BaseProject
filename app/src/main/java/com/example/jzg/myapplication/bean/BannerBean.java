package com.example.jzg.myapplication.bean;

import java.io.Serializable;

/**
 * Created by libo on 2017/5/3.
 *
 * @Email: libo@jingzhengu.com
 * @Description:
 */
public class BannerBean implements Serializable{

    private String bannerName;
    private int bannerPath;   //本地图片资源 drawable

    public BannerBean(String bannerName, int bannerPath) {
        this.bannerName = bannerName;
        this.bannerPath = bannerPath;
    }

    public String getBannerName() {
        return bannerName;
    }

    public void setBannerName(String bannerName) {
        this.bannerName = bannerName;
    }

    public int getBannerPath() {
        return bannerPath;
    }

    public void setBannerPath(int bannerPath) {
        this.bannerPath = bannerPath;
    }



}
