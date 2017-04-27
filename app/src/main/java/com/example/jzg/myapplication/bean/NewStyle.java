package com.example.jzg.myapplication.bean;

import java.io.Serializable;

/**
 * Created by JZG on 2016/7/7.
 */
public class NewStyle implements Serializable {
    private String year;
    private String Mark;
    private String Name;
    private String PosString;
    private String StyleId;
    private String NowMsrp;
    private boolean isCancel;

    public NewStyle() {
    }

    public String getNowMsrp() {
        return NowMsrp;
    }

    public void setNowMsrp(String nowMsrp) {
        NowMsrp = nowMsrp;
    }

    @Override
    public String toString() {
        return "NewStyle{" +
                "year='" + year + '\'' +
                ", Mark='" + Mark + '\'' +
                ", Name='" + Name + '\'' +
                ", PosString='" + PosString + '\'' +
                ", StyleId='" + StyleId + '\'' +
                ", NowMsrp='" + NowMsrp + '\'' +
                '}';
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMark() {
        return Mark;
    }

    public void setMark(String mark) {
        Mark = mark;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPosString() {
        return PosString;
    }

    public void setPosString(String posString) {
        PosString = posString;
    }

    public String getStyleId() {
        return StyleId;
    }

    public void setStyleId(String styleId) {
        StyleId = styleId;
    }

    public boolean isCancel() {
        return isCancel;
    }

    public void setCancel(boolean cancel) {
        isCancel = cancel;
    }

}
