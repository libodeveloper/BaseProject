package com.example.jzg.myapplication.db;

/**
 * Created by 李波 on 2017/1/20.
 *
 */
public class DBBase {
    private long id;//主键
    private String json;//数据
    private String dataType;//数据类型
    private String taskId;//任务单号,在保存检测方案时是planId
    private int userId;//用户id
    private long updateTime;

    public DBBase(long id, String dataType, int userId) {
        this.id = id;
        this.dataType = dataType;
        this.userId = userId;
    }
    public DBBase(long id, String dataType, String taskId, int userId) {
        this(id,dataType,userId);
        this.taskId = taskId;
    }
    public DBBase(long id, String dataType, String taskId, String json, int userId) {
        this(id,dataType,taskId,userId);
        this.json = json;
    }
    public DBBase() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getJson() {
        return this.json;
    }
    public void setJson(String json) {
        this.json = json;
    }
    public String getDataType() {
        return this.dataType;
    }
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
    public String getTaskId() {
        return this.taskId;
    }
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    public int getUserId() {
        return this.userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
