package com.example.jzg.myapplication.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


import com.example.jzg.myapplication.app.SysApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李波 on 2017/1/20.
 */
public class DBManager implements IDB{
    private static final String TAG = "DBManager";
    private static DBManager instance;
    private DBManager(){}
    private static PadDBHelper helper;
    private static SQLiteDatabase db;

    public static DBManager getInstance(){
        if(instance==null){
            synchronized (DBManager.class){
                if(instance==null){
                    instance = new DBManager();
                    helper = new PadDBHelper(SysApplication.getAppContext());
                }
            }
        }
        db = helper.getWritableDatabase();
        return instance;
    }


    /***
     * 根据查询是否存在某条记录
     * 注意：记得调用closeDB()关闭数据库
     * @param dataType
     * @param taskId
     * @param userId
     * @return
     */
    public boolean isExist(String dataType, String taskId, int userId){
        boolean exist = false;
        String[] columns = null;
        String selection = COL_TASK_ID + "=? AND " + COL_USER_ID + "=? AND " + COL_DATA_TYPE + "=?";
        String[] selectionArgs = {taskId, String.valueOf(userId),dataType};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()){
            exist = true;
        }
        cursor.close();
        return exist;
    }
    /***
     * 根据任务id查询是否存在某条记录
     * 注意：记得调用closeDB()关闭数据库
     * @param id
     * @return
     */
    public boolean isExistTask(String id){
        boolean exist = false;
        String[] columns = null;
        String selection = COL_TASK_ID + "=?";
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()){
            exist = true;
        }
        return exist;
    }
    /***
     * 根据任务id查询是否存在某条记录
     * 注意：记得调用closeDB()关闭数据库
     * @param id
     * @return
     */
    public boolean isExistTaskUpload(String id, int userId, String dataType){
        boolean exist = false;
        String[] columns = null;
        String selection = COL_TASK_ID + "=? AND " + COL_USER_ID + "=? AND " + COL_DATA_TYPE + "=?";
        String[] selectionArgs = {id, String.valueOf(userId),dataType};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()){
            exist = true;
        }
        return exist;
    }


    /***
     * @param data 需要入库的json数据
     * @param dataType 数据类型
     * @param taskId taskId
     */
    public boolean add(String data, String dataType, String taskId, int userId){
        ContentValues values = new ContentValues();
        values.put(COL_TASK_ID,taskId);
        values.put(COL_DATA_TYPE,dataType);
        values.put(COL_USER_ID,userId);
        values.put(COL_DATA,data);
        values.put(COL_UPDATE_TIME, System.currentTimeMillis());
        long row = -1;
        try {
            row = db.insert(TABLE_NAME,null,values);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            closeDB();
            if(row == -1){
                return false;
            }else{
                return true;
            }
        }
    }

    /***
     * 提交后删除
     * @param userId
     * @param taskId taskId
     * @return
     */
    public boolean deleteAfterSubmit(int userId,String taskId){
        int cols = db.delete(TABLE_NAME, COL_TASK_ID + "=? AND " + COL_USER_ID + "=?", new String[]{taskId, String.valueOf(userId)});
        closeDB();
        return cols>0;
    }

    /***
     * 根据三个参数更新数据
     * @param userId
     * @param taskId taskId
     * @param dataType
     * @param data
     * @return
     */
    public boolean update(int userId, String taskId, String dataType, String data){
        ContentValues values = new ContentValues();
        values.put(COL_DATA,data);
        values.put(COL_UPDATE_TIME, String.valueOf(System.currentTimeMillis()));
        int cols = db.update(TABLE_NAME, values, COL_TASK_ID + "=? AND " + COL_USER_ID + "=? AND " + COL_DATA_TYPE + "=?", new String[]{taskId, String.valueOf(userId), dataType});
        closeDB();
        return cols>0;
    }

    /***
     * 查询
     * @param taskId taskId
     * @param dataType 数据类型
     * @param userId
     * @return
     */
    public List<DBBase> query(String taskId, String dataType, int userId){
        List<DBBase> resultList = new ArrayList<>();
        String[] columns = null;
        String selection = COL_TASK_ID + "=? AND " + COL_USER_ID + "=? AND " + COL_DATA_TYPE + "=?";
        String[] selectionArgs = {taskId, String.valueOf(userId),dataType};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(COL_ID));
            long updateTime = cursor.getInt(cursor.getColumnIndex(COL_UPDATE_TIME))*1000;
            String data = cursor.getString(cursor.getColumnIndex(COL_DATA));
            DBBase item = new DBBase(id,dataType,taskId,userId);
            item.setUpdateTime(updateTime);
            item.setJson(data);
            resultList.add(item);
        }
        cursor.close();
        closeDB();
        return resultList;
    }

    /**
     * zealjiang
     * 根据用户 id 和 taskId 保存用户数据
     * 如果存在就更新，不存在就插入
     * @param dataType
     * @param taskId
     * @param userId
     * @param data
     * @return true表示成功，false表示失败
     */
    public boolean updateOrInsert(String dataType, String taskId, int userId, String data){
        boolean isExist = isExist(dataType,taskId,userId);
        if(isExist){
            //update
            return update(userId,taskId,dataType,data);
        }else{
            //insert
            return add(data,dataType,taskId,userId);
        }
    }

    /**
     * Created by 李波 on 2016/11/29.
     * 查询用户对应的检测任务数据
     */
    public String queryLocalUseTask(String taskId, String dataType, int useId){
        String json= null;
        String selection = COL_TASK_ID + "=? AND "+ COL_DATA_TYPE + "=? AND " + COL_USER_ID + "=?";
        String[] selectionArgs = {taskId,dataType, String.valueOf(useId)};
        Cursor cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()){
            json = cursor.getString(cursor.getColumnIndex(COL_DATA));
        }
        cursor.close();
        closeDB();
        return json;
    }

    /**==================================以下三个方法与方案存取相关===============================*/

    /***
     * 如果本地不存在某个检测方案，则保存一份，跟具体用户无关
     * @param planId 检测方案id
     * @param json 检测方案json数据
     * @param dataType Constants.DATA_TYPE_PLAN
     * @return
     */
    public boolean insertPlan(String planId, String json, String dataType){
        long id = -1l;
        ContentValues values = new ContentValues();
        values.put(COL_TASK_ID,planId);
        values.put(COL_DATA_TYPE,dataType);
        values.put(COL_DATA,json);
        values.put(COL_UPDATE_TIME, System.currentTimeMillis());
        try {
            id = db.insert(TABLE_NAME, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDB();
        return id>=0;
    }

    /***
     * 通过planId查询某个方案具体内容
     * @param planId 检测方案id
     * @param dataType  Constants.DATA_TYPE_PLAN
     * @return
     */
    public String queryLocalPlan(String planId, String dataType){
        String json= null;
        String selection = COL_TASK_ID + "=? AND "+ COL_DATA_TYPE + "=?";
        String[] selectionArgs = {planId,dataType};
        Cursor cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()){
            json = cursor.getString(cursor.getColumnIndex(COL_DATA));
        }
        cursor.close();
        closeDB();
        return json;
    }

    /***
     * 根据planId查询检测方案是否存在
     * @param planId  检测方案id
     * @param dataType Constants.DATA_TYPE_PLAN
     * @return
     */
    public boolean isPlanExist(String planId, String dataType){
        boolean exist = false;
        String[] columns = null;
        String selection = COL_TASK_ID + "=? AND "+ COL_DATA_TYPE + "=?";
        String[] selectionArgs = {planId,dataType};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()){
            exist = true;
        }
        cursor.close();
        closeDB();
        return exist;
    }


    public void closeDB(){
        db.close();
    }




}
