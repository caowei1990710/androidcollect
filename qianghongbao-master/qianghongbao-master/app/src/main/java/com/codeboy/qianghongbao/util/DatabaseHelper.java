package com.codeboy.qianghongbao.util;


import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.codeboy.qianghongbao.model.Depositrecord;
import com.codeboy.qianghongbao.model.Detail;
import com.codeboy.qianghongbao.model.Listdeposit;

import java.util.ArrayList;
import java.util.List;

//参考：http://blog.csdn.net/liuhe688/article/details/6715983

public class DatabaseHelper extends SQLiteOpenHelper// 继承SQLiteOpenHelper类
{

    // 数据库版本号
    private static final int DATABASE_VERSION = 1;
    // 数据库名
    private static final String DATABASE_NAME = "TestDB.db";

    // 数据表名，一个数据库中可以有多个表（虽然本例中只建立了一个表）
    public static final String TABLE_NAME = "PersonTable";

    // 构造函数，调用父类SQLiteOpenHelper的构造函数
    public DatabaseHelper(Context context, String name, CursorFactory factory,
                          int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);

    }

    public DatabaseHelper(Context context, String name, CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
        // SQLiteOpenHelper的构造函数参数：
        // context：上下文环境
        // name：数据库名字
        // factory：游标工厂（可选）
        // version：数据库模型版本号
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        // 数据库实际被创建是在getWritableDatabase()或getReadableDatabase()方法调用时
        Log.d("sqlite", "DatabaseHelper Constructor");
        // CursorFactory设置为null,使用系统默认的工厂类
    }

    // 继承SQLiteOpenHelper类,必须要覆写的三个方法：onCreate(),onUpgrade(),onOpen()
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 调用时间：数据库第一次创建时onCreate()方法会被调用

        // onCreate方法有一个 SQLiteDatabase对象作为参数，根据需要对这个对象填充表和初始化数据
        // 这个方法中主要完成创建数据库后对数据库的操作

        Log.d("sqlite", "DatabaseHelper onCreate");

        // 构建创建表的SQL语句（可以从SQLite Expert工具的DDL粘贴过来加进StringBuffer中） DATETIME DEFAULT (datetime(CURRENT_TIMESTAMP,'localtime'))
        StringBuffer sBuffer = new StringBuffer();

        sBuffer.append("CREATE TABLE [" + TABLE_NAME + "] (");
        sBuffer.append("[_id]INTEGER  primary key autoincrement, ");
        sBuffer.append("[createtime] DATETIME DEFAULT (datetime(CURRENT_TIMESTAMP,'localtime')),");
        sBuffer.append("[paytime] TEXT,");
        sBuffer.append("[beforemoney] TEXT,");
        sBuffer.append("[amout] TEXT,");
        sBuffer.append("[note] TEXT,");
        sBuffer.append("[aftermoney] TEXT,");
        sBuffer.append("[changtype] TEXT DEFAULT('cashin'),");
        sBuffer.append("[mynote] TEXT,");
        sBuffer.append("[othernote] TEXT,");
        sBuffer.append("[type] INTEGER DEFAULT(0),");
        sBuffer.append("[nowmoney] TEXT)");

        // 执行创建表的SQL语句
        db.execSQL(sBuffer.toString());

        // 即便程序修改重新运行，只要数据库已经创建过，就不会再进入这个onCreate方法

    }

    public void inSertdata(Detail detail) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("insert into " + TABLE_NAME + "(paytime,beforemoney,amout,aftermoney) values('" + detail.getPaytime() + "','" + detail.getBeforemoney() + "','" + detail.getAmout() + "','" + detail.getAftermoney() + "')");
//        db.setTransactionSuccessful();
//        db.endTransaction();
        db.close();
    }

    public void inSertdata(String date, double amount, String note) {
        List<Detail> list = querydata(1, "_id desc");
        double befroemoney = 0.00;
        if (list.size() > 0) {
            befroemoney = Double.parseDouble(list.get(0).getAftermoney());
        }
//        Double aftermoney = (Double.parseDouble((int) (befroemoney * 100 + amount * 100)) / 100;
        Double aftermoney = ((double) ((int) befroemoney * 100) - (int) (amount * 100)) / 100;
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("insert into " + TABLE_NAME + "(paytime,beforemoney,amout,aftermoney,note) values('" + date + "','" + befroemoney + "','" + amount + "','" + aftermoney + "','" + note + "')");
//        db.setTransactionSuccessful();
//        db.endTransaction();
        db.close();
    }

    public void inSertdata(String date, double amount, String note, String mynote, String othernote, String nowmoney) {
        List<Detail> list = querydata(1, "_id desc");
        double befroemoney = 0.00;
        if (list.size() > 0) {
            befroemoney = Double.parseDouble(list.get(0).getAftermoney());
        }
//        Double aftermoney = (Double.parseDouble((int) (befroemoney * 100 + amount * 100)) / 100;
        Double aftermoney = (double) (int) (befroemoney * 100 + amount * 100) / 100;
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("insert into " + TABLE_NAME + "(paytime,beforemoney,amout,aftermoney,note,mynote,othernote,nowmoney) values('" + date + "','" + befroemoney + "','" + amount + "','" + aftermoney + "','" + note + "','" + mynote + "','" + othernote + "','" + nowmoney + "')");
//        db.setTransactionSuccessful();
//        db.endTransaction();
        db.close();
    }

    public boolean setDeafult() {
        List<Detail> list = querydata(1, "_id desc");
        if (list.size() > 0) {
            BitmapUtils.beginString = list.get(0).getNote();
            BitmapUtils.beginCreatetime = list.get(0).getPaytime();
        } else {
            BitmapUtils.beginString = "累计收款金额￥0.02，收款笔数1笔";
            BitmapUtils.beginCreatetime = "2017-03-29 11:26:18";
        }
        return true;
    }

    public Listdeposit querylist(Context context) {
        String begin = BitmapUtils.getKey(context, "_id").equals("") ? "0" : BitmapUtils.getKey(context, "_id");
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " where _id > " + begin + " order by _id desc",
                null);
        ArrayList<Depositrecord> persons = new ArrayList<Depositrecord>();
        while (cursor.moveToNext()) {
            Depositrecord person = new Depositrecord();
            person.setId(cursor.getInt(cursor.getColumnIndex("_id")) + "");
            person.setBalance(cursor.getString(cursor.getColumnIndex("nowmoney")));
            person.setBeneficiaryComment(cursor.getString(cursor.getColumnIndex("othernote")));
            person.setSenderComment(cursor.getString(cursor.getColumnIndex("mynote")));
            person.setTransferAmount(cursor.getString(cursor.getColumnIndex("amout")));
            person.setTransferTime(cursor.getString(cursor.getColumnIndex("paytime")));
//            person.setPaytime(cursor.getString(cursor.getColumnIndex("paytime")));
//            person.setChangtype(cursor.getString(cursor.getColumnIndex("changtype")));
//            person.setNote(cursor.getString(cursor.getColumnIndex("note")));
//            person.setMynote(cursor.getString(cursor.getColumnIndex("mynote")));
//            person.setOthernote(cursor.getString(cursor.getColumnIndex("othernote")));
//            person.setNowmoney(cursor.getString(cursor.getColumnIndex("nowmoney")));
//            person.setType(cursor.getInt(cursor.getColumnIndex("type")));
//            person.setCreatetime(cursor.getString(cursor.getColumnIndex("name")));
//            person.setCreatetime(cursor.getString(cursor.getColumnIndex("name")));
            persons.add(person);
        }
        Listdeposit listdeposit = new Listdeposit(BitmapUtils.name, persons);
//        String string = JsonUtil.objectToString(listdeposit);
//        Log.e("string", string);
        return listdeposit;
    }

    public List<Detail> querydata() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " order by _id desc",
                null);
        ArrayList<Detail> persons = new ArrayList<Detail>();
        while (cursor.moveToNext()) {
            Detail person = new Detail();
            person.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            person.setCreatetime(cursor.getString(cursor.getColumnIndex("createtime")));
            person.setBeforemoney(cursor.getString(cursor.getColumnIndex("beforemoney")));
            person.setAmout(cursor.getString(cursor.getColumnIndex("amout")));
            person.setAftermoney(cursor.getString(cursor.getColumnIndex("aftermoney")));
            person.setPaytime(cursor.getString(cursor.getColumnIndex("paytime")));
            person.setChangtype(cursor.getString(cursor.getColumnIndex("changtype")));
            person.setNote(cursor.getString(cursor.getColumnIndex("note")));
            person.setMynote(cursor.getString(cursor.getColumnIndex("mynote")));
            person.setOthernote(cursor.getString(cursor.getColumnIndex("othernote")));
            person.setNowmoney(cursor.getString(cursor.getColumnIndex("nowmoney")));
            person.setType(cursor.getInt(cursor.getColumnIndex("type")));
//            person.setCreatetime(cursor.getString(cursor.getColumnIndex("name")));
//            person.setCreatetime(cursor.getString(cursor.getColumnIndex("name")));
            persons.add(person);
        }
        cursor.close();
//        db.setTransactionSuccessful();
//        db.endTransaction();
        db.close();
        return persons;
    }

    public List<Detail> querydata(int number, String order) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " order by " + order + ",_id limit 0,1",
                null);
        ArrayList<Detail> persons = new ArrayList<Detail>();
        while (cursor.moveToNext()) {
            Detail person = new Detail();
            person.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            person.setCreatetime(cursor.getString(cursor.getColumnIndex("createtime")));
            person.setBeforemoney(cursor.getString(cursor.getColumnIndex("beforemoney")));
            person.setAmout(cursor.getString(cursor.getColumnIndex("amout")));
            person.setAftermoney(cursor.getString(cursor.getColumnIndex("aftermoney")));
            person.setNote(cursor.getString(cursor.getColumnIndex("note")));
            person.setPaytime(cursor.getString(cursor.getColumnIndex("paytime")));
            person.setChangtype(cursor.getString(cursor.getColumnIndex("paytime")));
            person.setMynote(cursor.getString(cursor.getColumnIndex("paytime")));
            person.setOthernote(cursor.getString(cursor.getColumnIndex("paytime")));
            person.setNowmoney(cursor.getString(cursor.getColumnIndex("nowmoney")));
            person.setType(cursor.getInt(cursor.getColumnIndex("type")));
//            person.setCreatetime(cursor.getString(cursor.getColumnIndex("name")));
//            person.setCreatetime(cursor.getString(cursor.getColumnIndex("name")));
            persons.add(person);
        }
        cursor.close();
//        db.setTransactionSuccessful();
//        db.endTransaction();
        db.close();
        return persons;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 调用时间：如果DATABASE_VERSION值被改为别的数,系统发现现有数据库版本不同,即会调用onUpgrade

        // onUpgrade方法的三个参数，一个 SQLiteDatabase对象，一个旧的版本号和一个新的版本号
        // 这样就可以把一个数据库从旧的模型转变到新的模型
        // 这个方法中主要完成更改数据库版本的操作

        Log.d("sqlite", "DatabaseHelper onUpgrade");

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        // 上述做法简单来说就是，通过检查常量值来决定如何，升级时删除旧表，然后调用onCreate来创建新表
        // 一般在实际项目中是不能这么做的，正确的做法是在更新数据表结构时，还要考虑用户存放于数据库中的数据不丢失

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        // 每次打开数据库之后首先被执行

        Log.d("sqlite", "DatabaseHelper onOpen");
    }

}