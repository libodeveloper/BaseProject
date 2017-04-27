package com.example.jzg.myapplication.http;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 李波 on 2017/1/20.
 *
 * 接受网络请求返回的 base对象
 * MemberValue：实体数据
 * status：状态码
 * msg：状态码对应的提示信息
 *
 * 返回json格式如下：
 * {
     "MemberValue": {
     "UserId": 10,
     "NickName": "王晨",
     "CityId": 201,
     "HeadPic": "http://192.168.0.140:8080/image/image9.jpg"
     },
     "status": 100,
     "msg": ""
     }
 *
 */
public class ResponseJson<T> {
    @SerializedName("MemberValue")
    private T MemberValue;
    @SerializedName("status")
    private int status;
    @SerializedName("msg")
    private String msg;

    public T getMemberValue() {
        return MemberValue;
    }

    public void setMemberValue(T memberValue) {
        MemberValue = memberValue;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
