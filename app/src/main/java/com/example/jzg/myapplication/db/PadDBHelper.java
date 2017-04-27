package com.example.jzg.myapplication.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.jzg.myapplication.utils.LogUtil;


/**
 * Created by 李波 on 2017/1/20.
 */
public class PadDBHelper extends SQLiteOpenHelper implements IDB{
    private static final int DATABASE_VERSION = 2;
    private static final String TAG = "PadDBHelper";


    public PadDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //数据库第一次被创建时onCreate会被调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " +TABLE_NAME+
                "("+COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+ COL_TASK_ID +" VARCHAR, "+COL_DATA_TYPE+" INTEGER,"+COL_USER_ID+" INTEGER, "+COL_DATA+" TEXT, "+COL_UPDATE_TIME+ " INTEGER" +")";
        LogUtil.e(TAG,"create table:"+sql);
        db.execSQL(sql);
    }

    //如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER table "+TABLE_NAME+" ADD COLUMN "+COL_UPDATE_TIME+" INTEGER");
    }
}