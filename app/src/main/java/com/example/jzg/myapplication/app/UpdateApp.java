package com.example.jzg.myapplication.app;

import java.io.Serializable;

/**
 * 应用程序更新实体类
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
@SuppressWarnings("serial")
public class UpdateApp implements Serializable {
	
	public final static String UTF8 = "UTF-8";
	
	/**
     * status : 100
     * versionCode : 42
     * updateLog : 更新日志：修复动弹点赞问题修复图片保存图库未刷新BUG安装包大小：5.50MB
     * downloadUrl : http://www.jingzhengu.com/app/jzg.apk
     * versionName : V2.1.1
     * msg : 成功！
     */
    private int status;
    private int versionCode;
    private String updateLog;
    private String downloadUrl;
    private String versionName;
    private String msg;
    private String UpdateForce;//是否强制更新

    public String getUpdateForce() {
        return UpdateForce;
    }

    public void setUpdateForce(String updateForce) {
        UpdateForce = updateForce;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public void setUpdateLog(String updateLog) {
        this.updateLog = updateLog;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public String getUpdateLog() {
        return updateLog;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getVersionName() {
        return versionName;
    }

    public String getMsg() {
        return msg;
    }
	
	
}
