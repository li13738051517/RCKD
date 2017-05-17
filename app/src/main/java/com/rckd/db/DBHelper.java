package com.rckd.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import timber.log.Timber;

import static android.R.attr.tag;

/**
 * Created by LiZheng on 2017/5/13 0013.
 * DBHelper dbHelper = new DBHelper(this);
 * dbHelper.createDataBase(); SQLiteDatabase db =
 * dbHelper.getWritableDatabase(); Cursor cursor = db.query()
 * db.execSQL(sqlString); 注意：execSQL不支持带;的多条SQL语句，只能一条一条的执行，晕了很久才明白
 * 见execSQL的源码注释 (Multiple statements separated by ;s are not
 * supported.) 将把assets下的数据库文件直接复制到DB_PATH，但数据库文件大小限制在1M以下
 * 如果有超过1M的大文件，则需要先分割为N个小文件，然后使用copyBigDatabase()替换copyDatabase()
 */

public class DBHelper extends SQLiteOpenHelper {

    private static String tag = DBHelper.class.getName();
    // 用户数据库文件的版本
    public static final int DB_VERSION = 4;//数据库版本只能单向向上升级
    // 数据库文件目标存放路径为系统默认位置，com.droid 是你的包名
    public static String DB_PATH = "/data/data/com.rckd/databases/";
    /*
     * //如果你想把数据库文件存放在SD卡的话 private static String DB_PATH =
     * android.os.Environment.getExternalStorageDirectory().getAbsolutePath() +
     * "/arthurcn/drivertest/packfiles/";
     */
    private static String DB_NAME = "rckd_cities.db";
    private static String ASSETS_NAME = "rckd_cities.db";
    public final static String TABLE_CITY = "city";//rckd_cities.db 数据库的表  city

    private SQLiteDatabase myDataBase = null;
    private final Context myContext;

    /**
     * 如果数据库文件较大，使用FileSplit分割为小于1M的小文件 此例中分割为 hello.db.101 hello.db.102
     * hello.db.103
     */
    // 第一个文件名后缀
    private static final int ASSETS_SUFFIX_BEGIN = 101;
    // 最后一个文件名后缀
    private static final int ASSETS_SUFFIX_END = 103;

    /**
     * 在SQLiteOpenHelper的子类当中，必须有该构造函数
     *
     * @param context 上下文对象
     * @param name    数据库名称
     * @param factory 一般都是null
     * @param version 当前数据库的版本，值必须是整数并且是递增的状态
     */
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                    int version) {
        // 必须通过super调用父类当中的构造函数
        super(context, name, null, version);
        this.myContext = context;
        android.util.Log.e(tag, tag + " DBHelper Constuctor Init 4");
    }

    public DBHelper(Context context, String name, int version) {
        this(context, name, null, version);
        android.util.Log.e(tag, tag + " DBHelper Constuctor Init 3");
    }

    public DBHelper(Context context, String name) {
        this(context, name, DB_VERSION);
        android.util.Log.e(tag, tag + " DBHelper Constuctor Init 2");
    }

    public DBHelper(Context context) {
        this(context, DB_PATH + DB_NAME);
        android.util.Log.e(tag, tag + " DBHelper Constuctor Init 1");
    }

    public void createDataBase() throws IOException {
        Timber.e(tag + "  createDataBase start  ", tag);
        boolean dbExist = checkDataBase();//检查数据库是否有效
        if (dbExist) {
            // 数据库已存在，do nothing.
            android.util.Log.e(tag, tag + " 数据库已存在 ");
        } else {
            // 创建数据库
            android.util.Log.e(tag, tag + " 数据库不存在 start ");
            try {
                File dir = new File(DB_PATH);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File dbf = new File(DB_PATH + DB_NAME);
                if (dbf.exists()) {
                    dbf.delete();
                }
                SQLiteDatabase.openOrCreateDatabase(dbf, null);
                // 复制asseets中的db文件到DB_PATH下
                copyDataBase();
                android.util.Log.e(tag, tag + " 数据库不存在 over ");
            } catch (IOException e) {
                android.util.Log.e(tag, tag + "数据库创建失败 ");
                throw new Error("数据库创建失败");
            }
        }
        Timber.e(tag + "  createDataBase over  ", tag);
    }

    // 检查数据库是否有效
    private boolean checkDataBase() {
        android.util.Log.e(tag, tag + " checkDataBase() ");
        SQLiteDatabase checkDB = null;
        String myPath = DB_PATH + DB_NAME;
        android.util.Log.e(tag, tag + " checkDataBase() + myPath = " + myPath);
        try {
            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            // database does't exist yet.
            android.util.Log.e(tag, tag + " checkDataBase() +database does't exist yet ");
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created
     * empty database in the system folder, from where it can be accessed and
     * handled. This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException {
        // Open your local db as the input stream
        android.util.Log.e(tag, tag + " copyDataBase ");
        InputStream myInput = myContext.getAssets().open(ASSETS_NAME);
        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;
        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);
        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    // 复制assets下的大数据库文件时用这个
    //因为数据文件限制在1M左右
    @SuppressWarnings("unused")
    private void copyBigDataBase() throws IOException {
        InputStream myInput;
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        for (int i = ASSETS_SUFFIX_BEGIN; i < ASSETS_SUFFIX_END + 1; i++) {
            myInput = myContext.getAssets().open(ASSETS_NAME + "." + i);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myInput.close();
        }
        myOutput.close();
    }

    @Override
    public synchronized void close() {
        if (myDataBase != null) {
            myDataBase.close();
        }
        super.close();
    }

    /**
     * 该函数是在第一次创建的时候执行， 实际上是第一次得到SQLiteDatabase对象的时候才会调用这个方法,即获得对象的时候才会去创建
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据库表
//        db.execSQL("CREATE TABLE IF NOT EXISTS city (id integer primary key autoincrement, name varchar(40) NOT NUL, pinyin  varchar(40) NOT NULL ) ");
        android.util.Log.e(tag, tag + " create  ");
    }

    /**
     * 数据库表结构有变化时采用//3    4
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        android.util.Log.e(tag, tag + "  onUpgrade  ");
    }

}
