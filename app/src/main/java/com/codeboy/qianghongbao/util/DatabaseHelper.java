package com.codeboy.qianghongbao.util;


import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.codeboy.qianghongbao.model.Alidetail;
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
    public static final String Alipay = "alipay";

    public List<Detail> insertlist = new ArrayList<Detail>();

    // 构造函数，调用父类SQLiteOpenHelper的构造函数
    public DatabaseHelper(Context context, String name, CursorFactory factory,
                          int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);

    }

    public Context context;

    public DatabaseHelper(Context context, String name, CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
        this.context = context;
        // SQLiteOpenHelper的构造函数参数：
        // context：上下文环境
        // name：数据库名字
        // factory：游标工厂（可选）
        // version：数据库模型版本号
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
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
        sBuffer = new StringBuffer();

        sBuffer.append("CREATE TABLE [" + Alipay + "] (");
        sBuffer.append("[_id]INTEGER  primary key autoincrement, ");
        sBuffer.append("[createtime] DATETIME DEFAULT (datetime(CURRENT_TIMESTAMP,'localtime')),");
        sBuffer.append("[payer] TEXT,");
        sBuffer.append("[depositAddress] TEXT,");
        sBuffer.append("[amount] TEXT,");
        sBuffer.append("[remark] TEXT,");
        sBuffer.append("[depositTime] TEXT,");
        sBuffer.append("[numberOrder] TEXT)");
        // 执行创建表的SQL语句
        db.execSQL(sBuffer.toString());
        Log.e("sBuffer",sBuffer.toString());
        // 即便程序修改重新运行，只要数据库已经创建过，就不会再进入这个onCreate方法

    }

    public void inSertdata(Detail detail) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("insert into " + TABLE_NAME + "(paytime,beforemoney,amout,aftermoney) values('" + detail.getPaytime() + "','" + detail.getBeforemoney() + "','" + detail.getAmout() + "','" + detail.getAftermoney() + "')");
//        db.setTransactionSuccessful();
//        db.endTransaction();
        db.close();
    }

    public void inSertdatalist(List<String> listnote, List<String> amount) {
        for (int i = 0; i < listnote.size() / 2; i++) {
            if (listnote.get(i).equals(listnote.get(i + 2)) && listnote.get(i + 1).equals(listnote.get(i + 3))) {
                listnote.remove(i + 2);
                listnote.remove(i + 3);
            }
        }
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

    public static void insertAlldata(DatabaseHelper databaseHelper) {
        databaseHelper.setInsertlist(new ArrayList<Detail>());
        databaseHelper.getItem(BitmapUtils.list_a34.size(), BitmapUtils.list_amount.size() - 1);
        List<Detail> detail = databaseHelper.querydata(1, "_id desc");
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int beginindex = -1;
        Double beforeMoney = 0.00;
        if (detail.size() == 0) {
            Double intaoal = 0.00;
            for (int i = BitmapUtils.list_amount.size() - 1; i > -1; i--) {
                String amount = BitmapUtils.getMoney(BitmapUtils.list_amount.get(i), "￥");
                Log.e("amount:", BitmapUtils.getMoney(BitmapUtils.list_amount.get(i), "￥"));
                intaoal = BitmapUtils.getValue(intaoal + "", amount, 1);
//                intaoal += Double.parseDouble(BitmapUtils.getMoney(BitmapUtils.list_amount.get(i), "￥"));
            }
            String money = BitmapUtils.getMoney(BitmapUtils.money, "¥");
            Log.e("value", "money:" + money + " intaoal:" + intaoal);
            beforeMoney = BitmapUtils.getValue(money, intaoal + "", -1);
        } else {
            beforeMoney = Double.parseDouble(detail.get(0).getAftermoney());
        }
        for (int i = databaseHelper.getInsertlist().size() - 1; i > -1; i--) {
            if (detail.size() != 0) {
                if (detail.get(0).getNote().equals(databaseHelper.getInsertlist().get(i).getNote()) && detail.get(0).getPaytime().equals(databaseHelper.getInsertlist().get(i).getPaytime())) {
                    databaseHelper.getInsertlist().get(i).setBeforemoney(detail.get(0).getBeforemoney());
                    databaseHelper.getInsertlist().get(i).setAftermoney(detail.get(0).getAftermoney());
                    beginindex = i;
                    break;
                }
            }
//        db.setTransactionSuccessful();
//        db.endTransaction();
        }
        for (int i = beginindex + 1; i < databaseHelper.getInsertlist().size(); i++) {
            if (i != 0)
                beforeMoney = Double.parseDouble(databaseHelper.getInsertlist().get(i - 1).getAftermoney());
            databaseHelper.getInsertlist().get(i).setBeforemoney(beforeMoney + "");
            Log.e("beforemoney", "beforemoney:" + beforeMoney);
            Double afterMoney = BitmapUtils.getValue(beforeMoney + "", databaseHelper.getInsertlist().get(i).getAmout(), 1);//(double) (int) (Double.parseDouble(databaseHelper.getInsertlist().get(i).getAmout()) * 100 + Double.parseDouble(databaseHelper.getInsertlist().get(i).getBeforemoney()) * 100) / 100 + "";
            databaseHelper.getInsertlist().get(i).setAftermoney(afterMoney + "");
            Log.e("item", databaseHelper.getInsertlist().get(i).toString());
            db.execSQL("insert into " + TABLE_NAME + "(paytime,beforemoney,amout,aftermoney,note,mynote,othernote,nowmoney) values('" + databaseHelper.getInsertlist().get(i).getPaytime() + "','" + databaseHelper.getInsertlist().get(i).getBeforemoney() + "','"
                    + databaseHelper.getInsertlist().get(i).getAmout() + "','" + databaseHelper.getInsertlist().get(i).getAftermoney() + "','" + databaseHelper.getInsertlist().get(i).getNote() + "','" + databaseHelper.getInsertlist().get(i).getMynote() + "','" + databaseHelper.getInsertlist().get(i).getOthernote()
                    + "','" + databaseHelper.getInsertlist().get(i).getNowmoney() + "')");
        }
        db.close();
//        for (int i = 0; i < BitmapUtils.list_a34.size(); i++) {
//
//        }
    }

    public List<Detail> getInsertlist() {
        return insertlist;
    }

    public void setInsertlist(List<Detail> insertlist) {
        this.insertlist = insertlist;
    }

    public void getItem(int k, int j) {
        if (k < 2) {
            return;
        }
        Detail Detail = new Detail();
        int endk = 0;
        for (int i = k - 1; i > 0; i--) {
            if (BitmapUtils.list_a34.get(i).indexOf("收款成功") != -1) {
                endk = i;
                break;
            }
        }
        String note = "";
//        if (endk > -1) {
        note = BitmapUtils.list_a34.get(endk + 1);
//        }
        String paytime = "";
//        if (k - endk - 2 > -1) {
        paytime = BitmapUtils.list_a34.get(endk + 2);
//        }
        String othernote = "";
        String mynote = "";
        if (k - endk == 5) {
            othernote = BitmapUtils.list_a34.get(endk + 4);
            mynote = BitmapUtils.list_a34.get(endk + 3);
        } else if (k - endk == 4)
            othernote = BitmapUtils.list_a34.get(endk + 3);


        String amount = BitmapUtils.getMoney(BitmapUtils.list_amount.get(j), "￥");
        Log.e("信息", " paytime:" + paytime + " note:" + note + " amount:" + amount + " mynote:" + mynote + " othernote:" + othernote + " endk:" + endk);
        Detail detail = new Detail();
        detail.setPaytime(paytime);
        detail.setNote(note);
        detail.setAmout(amount);
        detail.setMynote(mynote);
        detail.setOthernote(othernote);
        detail.setNowmoney(BitmapUtils.money);
        insertlist.add(detail);
//        inSertdata(paytime, amount, note, mynote, othernote, BitmapUtils.getMoney(BitmapUtils.money, "¥"));
        getItem(endk, j - 1);
//        return detail;
    }

    public List<Alidetail> querylist(Context context, String tablename) {
        List<Alidetail> list = new ArrayList();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + tablename + " order by _id desc",
                null);
//        while (cursor.moveToNext()) {
//            Alidetail person = new Alidetail();
//            person.setId(cursor.getInt(cursor.getColumnIndex("_id")) + "");
//            person.setAmount(cursor.getString(cursor.getColumnIndex("amount")));
//            person.setDepositAddress(cursor.getString(cursor.getColumnIndex("depositAddress")));
//            person.setDepositTime(cursor.getString(cursor.getColumnIndex("depositTime")));
//            person.setNumberOrder(cursor.getString(cursor.getColumnIndex("numberOrder")));
//            person.setPayer(cursor.getString(cursor.getColumnIndex("payer")));
//            person.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
//            list.add(person);
//        }
        return list;
    }

    public void inSertdata(Alidetail alidetail) {
//        Double aftermoney = (Double.parseDouble((int) (befroemoney * 100 + amount * 100)) / 100;
        SQLiteDatabase db = getWritableDatabase();
//        db.execSQL("insert into " + Alipay + "(amount,depositAddress,depositTime,numberOrder,payer,remark) values('" + alidetail.getAmount() + "','" + alidetail.getDepositAddress() + "','" + alidetail.getDepositTime() + "','" + alidetail.getNumberOrder() + "','" + alidetail.getPayer() + "','" + alidetail.getRemark() + "')");
//        db.setTransactionSuccessful();
//        db.endTransaction();
        db.close();
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
            person.setTransferComment(cursor.getString(cursor.getColumnIndex("note")));
            person.setBeforeAmount(cursor.getString(cursor.getColumnIndex("beforemoney")));
            person.setAfterAmount(cursor.getString(cursor.getColumnIndex("aftermoney")));
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
        if (persons.size() > 0)
            Log.e("最后一笔", persons.get(0).toString());
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
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion,String tablename) {
        // 调用时间：如果DATABASE_VERSION值被改为别的数,系统发现现有数据库版本不同,即会调用onUpgrade

        // onUpgrade方法的三个参数，一个 SQLiteDatabase对象，一个旧的版本号和一个新的版本号
        // 这样就可以把一个数据库从旧的模型转变到新的模型
        // 这个方法中主要完成更改数据库版本的操作

        Log.d("sqlite", "DatabaseHelper onUpgrade");

        db.execSQL("DROP TABLE IF EXISTS " + tablename);
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