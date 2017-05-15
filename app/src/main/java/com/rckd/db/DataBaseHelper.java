package com.rckd.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by LiZheng on 2017/5/13 0013.
 */
public class DataBaseHelper extends SQLiteOpenHelper {


    // 类没有实例化,是不能用作父类构造器的参数,必须声明为静态
    private static final String name = "city"; // 数据库名称
    private static final int version = 1; // 数据库版本

    private static String tag = DataBaseHelper.class.getName();

    public DataBaseHelper(Context context) {
        super(context, name, null, version);
        android.util.Log.e(tag,tag+ "  DataBaseHelper  Constuctor Init ,sql.db  name =  " +name);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //  id   ,name   ,data
        db.execSQL("CREATE TABLE IF NOT EXISTS recentcity (id integer primary key autoincrement, name varchar(40), date INTEGER)");
        android.util.Log.e(tag,tag+ " create table sql.db ,table =   recentcity ");
    }


    //本地数据库可以考虑不升级
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        android.util.Log.e(tag, tag+"  onUpgrade  ");
    }
}
