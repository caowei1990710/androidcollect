package com.codeboy.qianghongbao.util;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.codeboy.qianghongbao.QiangHongBaoService;
import com.codeboy.qianghongbao.testActivity;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * <p>Created 16/2/7 上午1:37.</p>
 * <p><a href="mailto:730395591@qq.com">Email:730395591@qq.com</a></p>
 * <p><a href="http://www.happycodeboy.com">LeonLee Blog</a></p>
 *
 * @author LeonLee
 */
public class BitmapUtils {

    private BitmapUtils() {
    }

    public static AccessibilityNodeInfo nodeInfo;
    public static String beginString;
    public static String beginCreatetime;
    public static String name = "微信号：shuaifeng_1988", money, beforemoney, aftermoney;
    public static Boolean forward = false;
    public static Handler handler;
    public static QiangHongBaoService service;
    public static boolean ishome;
    private NotifyHelper notifyHelper;

    public static boolean saveBitmap(Context context, File output, Bitmap bitmap) {
        if (output.exists()) {
            return false;
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(output);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            insertMedia(context, output, "image/jpeg");
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                }

            }
        }


    }

    /**
     * 加入到系统的图库中
     */
    private static void insertMedia(Context context, File output, String mime) {
        try {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Video.Media.DATA, output.getAbsolutePath());
            values.put(MediaStore.Video.Media.MIME_TYPE, mime);
            //记录到系统媒体数据库，通过系统的gallery可以即时查看
            context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
            //通知系统去扫描
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(output)));
        } catch (Exception e) {
        }
    }

    public static void execShellCmd(String cmd) {

        try {
            // 申请获取root权限，这一步很重要，不然会没有作用
            Process process = Runtime.getRuntime().exec("su");
            // 获取输出流
            OutputStream outputStream = process.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(
                    outputStream);
            dataOutputStream.writeBytes(cmd);
            dataOutputStream.flush();
            dataOutputStream.close();
            outputStream.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * 发送handler
     */
    public static void sendHandler(Handler handler, int number, int delay) {
        Message message = new Message();
        message.what = number;
        handler.sendMessageDelayed(message, delay);
    }

    public static void setKey(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("test",
                Activity.MODE_PRIVATE);
        //实例化SharedPreferences.Editor对象
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //用putString的方法保存数据
        editor.putString(key, value);
        //提交当前数据
        editor.apply();
    }

    public static String getKey(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("test",
                Activity.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
        //提交当前数据
    }

    public static void getAllpay(final String number, final DatabaseHelper databaseHelper) {

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<AccessibilityNodeInfo> title = AccessibilityHelper.findNodeInfosByIds(nodeInfo, "com.tencent.mm:id/a4n");
                if (title == null) {
                    Toast.makeText(service.getBaseContext(), "走错路", Toast.LENGTH_LONG).show();
                    return;
                }
                if (title.size() == 0) {

                    return;
                }
                if (!title.get(title.size() - 1).getText().toString().equals("面对面收款到账通知")) {
                    return;
                }

                AccessibilityNodeInfo listview = AccessibilityHelper.findNodeInfosById(nodeInfo, "com.tencent.mm:id/a2i");
                AccessibilityNodeInfo amount = AccessibilityHelper.findNodeInfosById(nodeInfo, "com.tencent.mm:id/a4s");
                List<AccessibilityNodeInfo> list = AccessibilityHelper.findNodeInfosByIds(nodeInfo, "com.tencent.mm:id/a34");
                String value = (list.get(list.size() - 2).getText().toString());
                String beginTime = (list.get(list.size() - 3).getText().toString());
                String message = "message:";
                for (int i = 0; i < list.size(); i++) {
                    message += list.get(i).getText() + ",";
                }
                Log.e("message", "name:" + name + "-->message:" + message + "amount:" + amount.getText() + "money:" + money);
                Log.e("message", "value:" + value);
                //  if (!value.equals("累计收款金额￥0.16，收款笔数5笔") && !forward) {
                if (value.equals(beginString) && beginTime.equals(beginCreatetime)) {
                    if (forward) {
                        AccessibilityHelper.performBack(service);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                AccessibilityHelper.performBack(service);
                            }
                        }, 1000);
                        forward = false;
                        BitmapUtils.ishome = true;
                        testActivity.wechatnote = false;
                        Log.e("message", "停止");
//                        Intent intent = new Intent(Config.BEGININSERT);
//                        service.getBaseContext().sendBroadcast(intent);
                        return;
                    }
                    listview.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                    Log.e("message", "前进");
                    //title.get(1).getText().toString().equals("面对面收款到账通知")￥0.10
//                    Double moneyitem = Double.parseDouble(amount.getText().toString().substring(amount.getText().toString().indexOf("￥") + 1, amount.getText().toString().length()));
//                    money = money.substring(money.indexOf("¥") + 1, money.length());
////                        beforemoney = ((double)((int)(Double.parseDouble(money)*100) - (int)(moneyitem*100)))/100 + "";
//                    SQLiteDatabase db = databaseHelper.getWritableDatabase();
////                        databaseHelper.inSertdata(db, new Detail(1, beginTime, beforemoney, moneyitem, money));
//                    databaseHelper.inSertdata(beginTime, moneyitem, value);
                    forward = true;
                } else {
                    if (forward) {
                        if (value.equals(number)) {
                            AccessibilityHelper.performBack(service);
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    AccessibilityHelper.performBack(service);
                                }
                            }, 1000);
//                            AccessibilityHelper.performBack(service);
                            forward = false;
                            BitmapUtils.ishome = true;
                            testActivity.wechatnote = false;
                            Log.e("message", "停止");
//                            NotifyHelper.setSceen(service.getBaseContext());
                            return;
                        }
                        String othernote = "";
                        String mynote = "";
                        List<AccessibilityNodeInfo> listtitles = AccessibilityHelper.findNodeInfosByIds(nodeInfo, "com.tencent.mm:id/a33");
                        Log.e("listtitles", listtitles.size() + "");
                        for (int i = listtitles.size() - 1; i > 0; i--) {
                            if (listtitles.get(i).getText().toString().indexOf("收款方备注") != -1 && mynote.equals("")) {
                                mynote = list.get(i).getText().toString();
                            }
                            if (listtitles.get(i).getText().toString().indexOf("付款方留言") != -1 && othernote.equals("")) {
                                othernote = list.get(i).getText().toString();
                            }
                        }
//                        if (listtitles.size() == 4) {
//                            if (listtitles.get(3).getText().toString().indexOf("付款方留言") != -1)
//                                othernote = list.get(0).getText().toString();
//                            else
//                                mynote = list.get(0).getText().toString();
//                        } else if (listtitles.size() == 5) {
//                            othernote = list.get(1).getText().toString();
//                            mynote = list.get(0).getText().toString();
//                        }
                        Double moneyitem = Double.parseDouble(amount.getText().toString().substring(amount.getText().toString().indexOf("￥") + 1, amount.getText().toString().length()));
                        money = money.substring(money.indexOf("¥") + 1, money.length());
                        beforemoney = ((double) ((int) (Double.parseDouble(money) * 100) - (int) (moneyitem * 100))) / 100 + "";
                        SQLiteDatabase db = databaseHelper.getWritableDatabase();
//                        databaseHelper.inSertdata(beginTime, moneyitem, value);
                        databaseHelper.inSertdata(beginTime, moneyitem, value, mynote, othernote, money);
//                            databaseHelper.inSertdata(db, new Detail(1, beginTime, beforemoney, moneyitem, money));
                        listview.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                        Log.e("message", "前进");
                    } else {
                        listview.performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
                        Log.e("message", "回退" + "value:" + value + "beginTime:" + beginTime + " beginString:" + beginString + " beginCreatetime:" + beginCreatetime);
//                        if(value.equals(number)){
//                            String othernote = "";
//                            String mynote = "";
//                            List<AccessibilityNodeInfo> listtitles = AccessibilityHelper.findNodeInfosByIds(nodeInfo, "com.tencent.mm:id/a33");
//                            Log.e("listtitles", listtitles.size()+"");
//                            for(int i = listtitles.size()-1 ; i > 0 ; i--){
//                                if(listtitles.get(i).getText().toString().indexOf("收款方备注")!=-1 &&mynote.equals("")){
//                                    mynote = list.get(i).getText().toString();
//                                }
//                                if(listtitles.get(i).getText().toString().indexOf("付款方留言")!=-1 &&othernote.equals("")){
//                                    othernote = list.get(i).getText().toString();
//                                }
//                            }
////                        if (listtitles.size() == 4) {
////                            if (listtitles.get(3).getText().toString().indexOf("付款方留言") != -1)
////                                othernote = list.get(0).getText().toString();
////                            else
////                                mynote = list.get(0).getText().toString();
////                        } else if (listtitles.size() == 5) {
////                            othernote = list.get(1).getText().toString();
////                            mynote = list.get(0).getText().toString();
////                        }
//                            Double moneyitem = Double.parseDouble(amount.getText().toString().substring(amount.getText().toString().indexOf("￥") + 1, amount.getText().toString().length()));
//                            money = money.substring(money.indexOf("¥") + 1, money.length());
//                            beforemoney = ((double) ((int) (Double.parseDouble(money) * 100) - (int) (moneyitem * 100))) / 100 + "";
//                            SQLiteDatabase db = databaseHelper.getWritableDatabase();
////                        databaseHelper.inSertdata(beginTime, moneyitem, value);
//                            databaseHelper.inSertdata(beginTime, moneyitem, value,mynote,othernote,money);
////                            databaseHelper.inSertdata(db, new Detail(1, beginTime, beforemoney, moneyitem, money));
////                            listview.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
//                            Log.e("message", "前进");
//                            BitmapUtils.beginString = value;
//                            BitmapUtils.beginCreatetime = beginTime;
//                            forward = true;
//                        }
                    }
                }
//                if (!value.equals(beginString) && !forward) {
//                    listview.performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
////                    listview.performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
//                    Log.e("message", "回退");
//                } else if (value.equals(number)) {
//                    Log.e("message", "停止");
//                    return;
//                } else if (value.equals(beginString)) {
//                    listview.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
////                    listview.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
//                    Log.e("message", "前进");
//                    forward = true;
//                }
                getAllpay(value, databaseHelper);
            }
        }, 2000);

//        value.indexOf("收款笔数")

    }
}
