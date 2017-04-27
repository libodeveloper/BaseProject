package com.example.jzg.myapplication.mvpview;


import com.example.jzg.myapplication.base.IBaseView;
import com.example.jzg.myapplication.bean.AdmixedData;

/**
 * 车辆参配
 * Created by zealjiang on 2016/12/6 14:29.
 * Email: zealjiang@126.com
 */

public interface IAdmixedInf extends IBaseView {
    void succeed(AdmixedData admixedData);
}
