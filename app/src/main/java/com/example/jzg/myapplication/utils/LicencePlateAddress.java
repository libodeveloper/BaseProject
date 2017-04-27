package com.example.jzg.myapplication.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zealjiang on 2016/11/17 20:18.
 * Email: zealjiang@126.com
 */

public class LicencePlateAddress {

    private static LicencePlateAddress licencePlateAddress;
    private static HashMap<String,String> map = new HashMap<>();
    private static ArrayList<String> listSimpleName;

    private LicencePlateAddress() {
        map.put("浙江省","浙");
        map.put("福建省","闽");
        map.put("广东省","粤");
        map.put("北京市","京");
        map.put("天津市","津");
        map.put("河北省","冀");
        map.put("山西省","晋");
        map.put("内蒙古自治区","蒙");
        map.put("辽宁省","辽");
        map.put("吉林省","吉");
        map.put("黑龙江省","黑");
        map.put("上海市","沪");
        map.put("江苏省","苏");
        map.put("安徽省","皖");
        map.put("江西省","赣");
        map.put("山东省","鲁");
        map.put("河南省","豫");
        map.put("湖北省","鄂");
        map.put("湖南省","湘");
        map.put("广西壮族自治区","桂");
        map.put("四川省","川");
        map.put("贵州省","贵");
        map.put("云南省","云");
        map.put("西藏自治区","藏");
        map.put("陕西省","陕");
        map.put("甘肃省","甘");
        map.put("青海省","青");
        map.put("宁夏回族自治区","宁");
        map.put("新疆维吾尔自治区","新");

    }

    public static LicencePlateAddress getInstance(){
        if(licencePlateAddress==null){
            licencePlateAddress = new LicencePlateAddress();
        }
        return licencePlateAddress;
    }

    /**
     * 获取省市自治区的简称
     * @author zealjiang
     * @time 2016/11/18 9:47
     */
    public ArrayList<String> getSimpleName(){
        if(null==listSimpleName) {
            listSimpleName = new ArrayList<>();
        }
        if(listSimpleName.size()==0) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                listSimpleName.add(entry.getValue());
            }
        }
        return listSimpleName;
    }
}
