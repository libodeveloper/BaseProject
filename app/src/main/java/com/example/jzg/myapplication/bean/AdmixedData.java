package com.example.jzg.myapplication.bean;
/**
 * author: gcc
 * date: 2016/11/3 11:30
 * email:
 */

import java.io.Serializable;
import java.util.List;

/**
 * author: guochen
 * date: 2016/11/3 11:30
 * email: 
 */
public class AdmixedData implements Serializable {

    private List<ShowDataBean> ShowData;
    private List<?> ParamData;
    private List<PhotoDataBean> PhotoData;
    private List<CarDataBean> CarData;

    public List<ShowDataBean> getShowData() {
        return ShowData;
    }

    public void setShowData(List<ShowDataBean> ShowData) {
        this.ShowData = ShowData;
    }

    public List<?> getParamData() {
        return ParamData;
    }

    public void setParamData(List<?> ParamData) {
        this.ParamData = ParamData;
    }

    public List<PhotoDataBean> getPhotoData() {
        return PhotoData;
    }

    public void setPhotoData(List<PhotoDataBean> PhotoData) {
        this.PhotoData = PhotoData;
    }

    public List<CarDataBean> getCarData() {
        return CarData;
    }

    public void setCarData(List<CarDataBean> CarData) {
        this.CarData = CarData;
    }

    public static class ShowDataBean implements Serializable {
        /**
         * DataType : 1
         * PropertyName : 厂商
         * PropertyValue : ["绅宝","绅宝","绅宝"]
         */

        private int DataType;
        private String PropertyName;
        private List<String> PropertyValue;

        public int getDataType() {
            return DataType;
        }

        public void setDataType(int DataType) {
            this.DataType = DataType;
        }

        public String getPropertyName() {
            return PropertyName;
        }

        public void setPropertyName(String PropertyName) {
            this.PropertyName = PropertyName;
        }

        public List<String> getPropertyValue() {
            return PropertyValue;
        }

        public void setPropertyValue(List<String> PropertyValue) {
            this.PropertyValue = PropertyValue;
        }
    }

    public static class PhotoDataBean implements Serializable {
        /**
         * DataType : 1
         * PropertyName : 左前45°
         * PropertyValue : ["-","-","-"]
         */

        private int DataType;
        private String PropertyName;
        private List<String> PropertyValue;

        public int getDataType() {
            return DataType;
        }

        public void setDataType(int DataType) {
            this.DataType = DataType;
        }

        public String getPropertyName() {
            return PropertyName;
        }

        public void setPropertyName(String PropertyName) {
            this.PropertyName = PropertyName;
        }

        public List<String> getPropertyValue() {
            return PropertyValue;
        }

        public void setPropertyValue(List<String> PropertyValue) {
            this.PropertyValue = PropertyValue;
        }
    }

    public static class CarDataBean implements Serializable {
        /**
         * StyleID : 113333
         * NowMsrp : 5.58
         * StyleName : 绅宝 绅宝X25 2015款 1.5L 手动 标准型
         */

        private int StyleID;
        private double NowMsrp;
        private String StyleName;

        public int getStyleID() {
            return StyleID;
        }

        public void setStyleID(int StyleID) {
            this.StyleID = StyleID;
        }

        public double getNowMsrp() {
            return NowMsrp;
        }

        public void setNowMsrp(double NowMsrp) {
            this.NowMsrp = NowMsrp;
        }

        public String getStyleName() {
            return StyleName;
        }

        public void setStyleName(String StyleName) {
            this.StyleName = StyleName;
        }
    }
}
