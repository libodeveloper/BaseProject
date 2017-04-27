package com.example.jzg.myapplication.bean;

/**
 * 郑有权
 *
 */
public class Upload {


    /**
     * MemberValue : null
     * status : 504
     * msg : 文件MD5加密错误
     */

    private Object MemberValue;
    private int status;
    private String msg;

    public Object getMemberValue() {
        return MemberValue;
    }

    public void setMemberValue(Object MemberValue) {
        this.MemberValue = MemberValue;
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
