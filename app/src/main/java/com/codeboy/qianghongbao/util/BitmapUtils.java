package com.codeboy.qianghongbao.util;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.codeboy.qianghongbao.Config;
import com.codeboy.qianghongbao.QHBApplication;
import com.codeboy.qianghongbao.QiangHongBaoService;
import com.codeboy.qianghongbao.alipyActivity;
import com.codeboy.qianghongbao.demoActivity.DemoActivity;
import com.codeboy.qianghongbao.model.AliDetailitem;
import com.codeboy.qianghongbao.model.Alidetail;
import com.codeboy.qianghongbao.model.AlipayItem;
import com.codeboy.qianghongbao.model.Detail;
import com.codeboy.qianghongbao.model.InfoItem;
import com.codeboy.qianghongbao.model.Return;
import com.codeboy.qianghongbao.testActivity;
import com.google.gson.Gson;

import org.apache.commons.codec.binary.Hex;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>Created 16/2/7 上午1:37.</p>
 * <p><a href="mailto:730395591@qq.com">Email:730395591@qq.com</a></p>
 * <p><a href="http://www.happycodeboy.com">LeonLee Blog</a></p>
 *
 * @author LeonLee
 */
public class BitmapUtils {
    public static String payer = null;
    public static List<InfoItem> infolist;
    public static List<String> list = new ArrayList<String>();
    public static List<AccessibilityNodeInfo> accesslist = new ArrayList<AccessibilityNodeInfo>();

    private BitmapUtils() {
    }

    public static AccessibilityNodeInfo nodeInfo;
    public static String beginString;
    public static AccessibilityNodeInfo before_node, after_node;
    public static String beginCreatetime;
    public static String name = "微信号：shuaifeng_1988", money, beforemoney, aftermoney;
    public static Boolean forward = false, findalready = false, beginScroll = false;
    public static Handler handler;
    public static QiangHongBaoService service;
    public static boolean ishome;
    private NotifyHelper notifyHelper;
    public static List<String> list_a34 = new ArrayList<>();
    public static List<String> list_amount = new ArrayList<>();

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
//    public static void getNodeInfo(AccessibilityNodeInfo nodeInfo, String type, List<AccessibilityNodeInfo> accesslist) {
//        if (nodeInfo == null || nodeInfo.getChildCount() == 0)
//            return;
//        for (int i = 0; i < nodeInfo.getChildCount(); i++) {
//            Log.e("class", nodeInfo.getChild(i).getClassName().toString());
//            if (nodeInfo.getChild(i).getClassName().toString().equals(type)) {
//                accesslist.add(nodeInfo.getChild(i));
//
//            }
//            getNodeInfo(nodeInfo.getChild(i), type, accesslist);
//        }
//    }

    /**
     * 获取控件对象
     */
    public static void getDetail(AccessibilityNodeInfo nodeInfo) {
        Log.e("nodeInfo", nodeInfo + "");
        if (nodeInfo == null) {
            return;
        }
//        if (nodeInfo.getChildCount() > 0 && !nodeInfo.getChild(0).getText().toString().equals("收款到账通知"))
//            return;
        int length = nodeInfo.getChildCount();
        for (int i = 0; i < length; i++) {
            Rect rect = new Rect();
            nodeInfo.getChild(i).getBoundsInScreen(rect);
//            Log.e("rect", rect.toString() + " nodeInfo:" + nodeInfo.getChild(i).getText() + " i:" + i);
//            if (i == 0 && nodeInfo.getChild(i).getText() != null && payer != null)
//                payer = nodeInfo.getChild(i).getText().toString();
            if (nodeInfo.getChild(i).getText() != null)
                list.add(nodeInfo.getChild(i).getText().toString());
//            else if (i < 2 && nodeInfo.getChild(i).getContentDescription() != null)
//                list.add(nodeInfo.getChild(i).getContentDescription().toString());
            getDetail(nodeInfo.getChild(i));
        }
    }

    public static void getImageFromCamera(Activity context) {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            context.startActivityForResult(intent, -1);
//            context.startActivity(intent);
        } else {
            Toast.makeText(context.getApplicationContext(), "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 循环点击
     */
    public static void click_onebyone(List<String> id, Handler handler) {

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

    public static void setReplace(int i) {
        if (i == 1) {
            return;
        }
        Log.e("i", i + "");
        if (list_a34.get(i * 2 - 1).equals(list_a34.get(i * 2 - 3)) && list_a34.get(i * 2 - 2).equals(list_a34.get(i * 2 - 4))) {
            list_a34.remove(i * 2 - 1);
            list_a34.remove(i * 2 - 2);
            list_amount.remove(i - 1);
        }
        i -= 1;
        setReplace(i);
    }

    public static void addList(List<AccessibilityNodeInfo> list, List<AccessibilityNodeInfo> amountlist) {
        String begin = "";
        int beginindex = list.size();
        if (list_a34.size() > 0)
            begin = list_a34.get(list_a34.size() - 1);
        Log.e("list_a34", list_a34.toString());
        if (!begin.equals("")) {
            Log.e("begin", begin + "");
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getText().toString().equals(begin)) {
                    beginindex = i;
                    Log.e("beginindex", beginindex + "");
                    break;
                }
            }
        }
        Rect beginrect = new Rect();
        Rect amountrect = new Rect();
        if (beginindex != list.size() && beginindex > 0)
            list.get(beginindex - 1).getBoundsInScreen(beginrect);

        Log.e("下标开始：", beginrect.bottom + "");
        for (int i = beginindex - 1; i > -1; i--) {
            list_a34.add(list.get(i).getText().toString());
//                            int list_a34 = Log.e("list_a34", (amountlist.get(i).getBoundsInScreen()));
        }
        for (int i = amountlist.size() - 1; i > -1; i--) {
            amountlist.get(i).getBoundsInScreen(amountrect);
            if (beginindex == list.size()) {
                list_amount.add(amountlist.get(i).getText().toString());
            } else if (amountrect.bottom < beginrect.bottom) {
                list_amount.add(amountlist.get(i).getText().toString());
            }
        }
    }

    public static void getNodeInfo(AccessibilityNodeInfo nodeInfo, String type, List<AccessibilityNodeInfo> accesslist) {
        if (nodeInfo == null || nodeInfo.getChildCount() == 0)
            return;
        for (int i = 0; i < nodeInfo.getChildCount(); i++) {
            Log.e("class", nodeInfo.getChild(i).getClassName().toString());
            if (nodeInfo.getChild(i).getClassName().toString().equals(type)) {
                accesslist.add(nodeInfo.getChild(i));

            }
            getNodeInfo(nodeInfo.getChild(i), type, accesslist);
        }
    }

    public static void getNewWechat(final Handler handler, final AccessibilityNodeInfo listview, final Context context) {
        nodeInfo = QiangHongBaoService.service.getRootInActiveWindow();
//        DemoActivity.aliList
        List<AccessibilityNodeInfo> linearlists = AccessibilityHelper.findNodeInfosByIds(nodeInfo, "com.tencent.mm:id/ad0");
//        int length = DemoActivity.aliList.size();
        for (int i = linearlists.size(); i > 0; i--) {

            list = new ArrayList<>();
            AccessibilityNodeInfo wechatcontent = linearlists.get(i - 1);
            getDetail(wechatcontent);
            if (!list.get(0).equals("收款到账通知"))
                return;
            Log.e("list", list.toString());
            AlipayItem alipayItem = new AlipayItem();
//            alipayItem.setDepositNumber();
            alipayItem.setAmount(Double.parseDouble(list.get(3).replace("￥", "")));
//            String time = ((new Date()).getYear() + 1900) + "-" + list.get(1).split(",")[1].trim() + " " + list.get(1).split(",")[0] + ":00";
//            String time = ((new Date()).getYear() + 1900) + "-" + list.get(1).split(",")[1].trim().replace("月", "-").replace("日", "") + " " + list.get(1).split(",")[0] + ":00";
            String time = ((new Date()).getYear() + 1900) + "-" + list.get(1).split(" ")[0].trim().replace("月", "-").replace("日", "") + " " + list.get(1).split(" ")[1] + ":00";
            alipayItem.setTransferTime(time);
            alipayItem.setWechatName(DemoActivity.account);
            alipayItem.setCreateUser("auto");
            alipayItem.setPlatfrom(DemoActivity.plaftfrom);
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).equals("付款方备注"))
                    alipayItem.setNote(list.get(j + 1));
                if (list.get(j).equals("汇总"))
                    alipayItem.setNickName(list.get(j + 1).replace("今日", "").replace("收款，共计", ""));
            }
            alipayItem.setDepositNumber(((new Date()).getYear() + 1900) + list.get(1).split(" ")[0].trim().replace("月", "").replace("日", "") + DemoActivity.account + alipayItem.getNickName().substring(alipayItem.getNickName().indexOf("第") + 1, alipayItem.getNickName().indexOf("笔")));
//            alipayItem.setNote();
            DemoActivity.aliList.add(alipayItem);
        }
    }

    public static void getWechat(final Handler handler, final AccessibilityNodeInfo listview, final Context context) {
//        listview = AccessibilityHelper.findNodeInfosById(nodeInfo, "com.tencent.mm:id/a3e");
//        if()
//        List<AccessibilityNodeInfo> linearlists = AccessibilityHelper.findNodeInfosByIds(nodeInfo, "com.tencent.mm:id/a7o");
        nodeInfo = QiangHongBaoService.service.getRootInActiveWindow();
        List<AccessibilityNodeInfo> linearlists = AccessibilityHelper.findNodeInfosByIds(nodeInfo, "com.tencent.mm:id/ad0");
        int length = DemoActivity.aliList.size();
//        if(before_node!=null){
//            before_node.equals(linearlists.get(0).setR);
//        }
//        beforeLength = DemoActivity.alilist.size();
        if (linearlists != null && linearlists.size() > 0)
            before_node = linearlists.get(0);
        else {
            Log.e("状态", "没记录");
            Intent intents = new Intent(Config.BEGININSERTTASK);
            context.sendBroadcast(intents);
            return;
        }
//        Log.e("rect","before_node:"+before_node +" after_node:"+after_node);
        Log.e("rect", "before_node:" + before_node + " after_node:" + after_node);
//        if(before_node.equals(after_node)){
//            Log.e("状态", "滑到顶");
//            return;
//        }
        after_node = before_node;
        Log.e("rect", "before_node:" + before_node + " after_node:" + after_node);
        boolean include = false, havealredy = false;
        for (int i = linearlists.size(); i > 0; i--) {
            if (havealredy)
                break;
            if (linearlists.get(i - 1) == null)
                continue;
            list.clear();
            AccessibilityNodeInfo wechatcontent = linearlists.get(i - 1);
//            wechatcontent.getBoundsInParent(beforerect);
//            beforeString = wechatcontent.toString();
            Long beforetime = (new Date()).getTime();
            getDetail(wechatcontent);
            Log.e("time", (new Date().getTime() - beforetime) + "");
            String getContent = (new Date().getTime() - beforetime) + "";
//            String getContent = 52 + "";
//            Log.e("list", list.toString());
            Alidetail detail = new Alidetail();
            Log.e("list", list.toString());
            if (list.size() < 6) {
//                DemoActivity.alilist.add(detail);
            } else {
                String money = list.get(3).replace("￥", "");
                String time = ((new Date()).getYear() + 1900) + "-" + list.get(1).split(",")[1].trim() + " " + list.get(1).split(",")[0] + ":00";
                String before_time = list.get(1).split(",")[1].trim();
//                String time="2017-11-22 13:33:00";
                if (list.size() > 10) {
                    try {
                        detail.setSenderAccount(list.get(9).substring(list.get(9).indexOf("￥"), list.get(9).length()) + "-" + "数" + list.get(9).substring(list.get(9).indexOf("第") + 1, list.get(9).indexOf("笔") + 1));
                    } catch (Exception e) {
                        detail.setSenderAccount("数" + list.get(9).substring(list.get(9).length() - 3, list.get(9).length()));
                    }
                    try {
//                        detail.setId(EncoderByMd5(before_time + alipyActivity.bankcard + detail.getSenderAccount()));
//                        detail.setId(EncoderByMd5(before_time + alipyActivity.bankcard + detail.getSenderAccount()));
                        detail.setId(setId(time, money, alipyActivity.bankcard, list.get(9).substring(list.get(9).indexOf("第") + 1, list.get(9).indexOf("笔"))));
                    } catch (Exception e) {
                        detail.setId(setId(time, money, alipyActivity.bankcard, "101"));
                    }
                    detail.setTransferTime(time);
                    detail.setSenderNickname(list.get(7));
                    detail.setSenderComment(list.get(5));
                } else if (list.size() > 8) {
                    try {
                        detail.setSenderAccount(list.get(7).substring(list.get(7).indexOf("￥"), list.get(7).length()) + "-" + "数" + list.get(7).substring(list.get(7).indexOf("第") + 1, list.get(7).indexOf("笔") + 1));
                    } catch (Exception e) {
                        detail.setSenderAccount("数" + list.get(7).substring(list.get(7).length() - 3, list.get(7).length()));
                    }
                    try {
//                        detail.setId(EncoderByMd5(before_time + alipyActivity.bankcard + detail.getSenderAccount()));
//                        detail.setId(EncoderByMd5(before_time + alipyActivity.bankcard + detail.getSenderAccount()));
                        detail.setId(setId(time, money, alipyActivity.bankcard, list.get(7).substring(list.get(7).indexOf("第") + 1, list.get(7).indexOf("笔"))));
                    } catch (Exception e) {
//                        detail.setId(setId(time, money, alipyActivity.bankcard, ""));
                        detail.setId(setId(time, money, alipyActivity.bankcard, "101"));
                    }
                    detail.setTransferTime(time);
                    if (list.get(4).indexOf("付款方") != -1) {
                        detail.setSenderComment("(无)");
                        detail.setSenderNickname(list.get(5));
                    } else {
                        detail.setSenderComment(list.get(5));
                        detail.setSenderNickname("(无)");
                    }
//                    detail.setSenderComment(list.get(5));

                } else if (list.size() > 6) {
                    try {
                        detail.setSenderAccount(list.get(5).substring(list.get(5).indexOf("￥"), list.get(5).length()) + "-" + "数" + list.get(5).substring(list.get(5).indexOf("第") + 1, list.get(5).indexOf("笔") + 1));
                    } catch (Exception e) {
//                        setId(time,money,alipyActivity.bankcard,list.get(5).substring(list.get(5).indexOf("第") + 1, list.get(5).indexOf("笔")));
                        detail.setSenderAccount("数" + list.get(5).substring(list.get(5).length() - 3, list.get(5).length()));
                    }
                    try {
//                        detail.setId(EncoderByMd5(before_time + alipyActivity.bankcard + detail.getSenderAccount()));
                        detail.setId(setId(time, money, alipyActivity.bankcard, list.get(5).substring(list.get(5).indexOf("第") + 1, list.get(5).indexOf("笔"))));
                    } catch (Exception e) {
                        detail.setId(setId(time, money, alipyActivity.bankcard, "101"));
                    }
                    detail.setTransferTime(time);
                    detail.setSenderNickname("(无)");
                    detail.setSenderComment("(无)");
                }
//                if (alipyActivity.platfrom.equals("MSGREEN"))
//                    detail.setPlafrom("pms");
//                else
//                    detail.setPlafrom("oa");
                detail.setTransferAmount(money);
                detail.setBalance(DemoActivity.versions + "," + getContent);
                include = false;
                havealredy = false;
                if (DemoActivity.alilist.size() > 0) {
                    for (int j = 0; j < DemoActivity.alilist.size(); j++) {
                        if (DemoActivity.alilist.get(j).getId() != null && DemoActivity.alilist.get(j).getId().equals(detail.getId())) {
                            include = true;
                            break;
                        }
                    }
                    if (!include) {
//                    if(detail.getId().equals(BitmapUtils.infolist.get()))
                        for (int j = 0; j < DemoActivity.aliList.size(); j++) {
                            if (DemoActivity.aliList.get(j).getDepositNumber() != null && DemoActivity.aliList.get(j).getDepositNumber().equals(detail.getId())) {
                                havealredy = true;
                                break;
                            }
                        }
                        if (!havealredy) {
                            DemoActivity.alilist.add(detail);
                        }
                    }
                } else {
                    for (int j = 0; j < BitmapUtils.infolist.size(); j++) {
                        if (detail.getId().equals(DemoActivity.aliList.get(j).getDepositNumber())) {
                            havealredy = true;
                            break;
                        }
                    }
                    if (!havealredy) {
                        if (DemoActivity.alilist.size() < 3)
                            DemoActivity.alilist.add(detail);
                    }
                }
            }

//            DemoActivity.alilist =
//            Log.e("alilist:", DemoActivity.alilist.get(0).getId());
        }
        Log.e("alilist:", DemoActivity.alilist.toString());
        if (length == DemoActivity.alilist.size()) {
            Log.e("状态", "滑到顶");
            Intent intents = new Intent(Config.BEGININSERTTASK);
            context.sendBroadcast(intents);
            return;
        }
//        getDetail(linears);
//        if (before_list.equals(list) && list.size() > 0) {

//        }
//        before_list = list;
        Log.e("linears:", AccessibilityHelper.findNodeInfosByIds(nodeInfo, "com.tencent.mm:id/ad0").size() + "");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                Log.e("lienarid",linear.getViewIdResourceName()+"");
//                for(int i = 0 ; i < linear.getChildCount();i++){
//                    Log.e("")                }
//                Log.e("插入数据", "插入数据");
//                DatabaseHelper.insertAlldata(datahelp);

                listview.performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getWechat(handler, listview, context);
                    }
                }, 2000);

//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        listview.performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
//                    }
//                }, 2000);
            }
        }, 2000);
    }

    public static String setId(String time, String amount, String bankCard, String times) {
        String timeId = time.replace("-", "").replace(" ", "").replace(":", "");
        String amountId = amount;
        try {
            amountId = String.format("%04d", (int) Double.parseDouble(amount));
        } catch (Exception e) {

        }

        return timeId + amountId + bankCard + times;
    }

    public static void getNewallpay(final List<AccessibilityNodeInfo> notes, final DatabaseHelper datahelp) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("开始微信", "开始微信");
                findalready = false;
                AccessibilityNodeInfo listview = AccessibilityHelper.findNodeInfosById(nodeInfo, "com.tencent.mm:id/a2i");
                AccessibilityNodeInfo linearLayout = AccessibilityHelper.findNodeInfosById(nodeInfo, "com.tencent.mm:id/a3t");
                List<AccessibilityNodeInfo> amountlist = AccessibilityHelper.findNodeInfosByIds(nodeInfo, "com.tencent.mm:id/a4s");
                List<AccessibilityNodeInfo> list = AccessibilityHelper.findNodeInfosByIds(nodeInfo, "com.tencent.mm:id/a34");
//                Rect rect_parent = new Rect();
//                Rect rect = new Rect();
//                listview.getBoundsInParent(rect_parent);
//                if (list.size() > 1)
//                    linearLayout.getBoundsInParent(rect);
//                Log.e("距离","rect_parent:"+rect_parent.bottom + "rect"+rect.bottom);
                if (listview == null) {
                    Toast.makeText(service.getBaseContext(), "走错路", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getText().toString().equals(beginCreatetime)) {
                        if (i < list.size() - 1 && list.get(i + 1).getText().toString().equals(beginString)) {
                            findalready = true;
                        }
                    }
                }
//                forward = findalready;
                if (forward) {
                    if (notes.equals(list)) {
                        Log.e("停止", "停止");
                        beginScroll = false;
                        testActivity.wechatnote = false;
                        testActivity.acount_text = 300;

//                        Message message = new Message();
//                        message.what = 97;
//                        handler.sendMessageDelayed(message,5000);
//                        List<String> notes = list_a34;
//                        List<String> money = list_a34;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("插入数据", "插入数据");
                                DatabaseHelper.insertAlldata(datahelp);
                                AccessibilityHelper.performBack(service);
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        AccessibilityHelper.performBack(service);
                                    }
                                }, 2000);
                            }
                        }, 2000);
                        return;
                    } else {
//                        for (int i = 0; i < list.size(); i++) {
//                            list_a34.add(list.get(i).getText().toString());
//                        }
                        addList(list, amountlist);
                        listview.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                        Log.e("前进", "前进");
                    }
                } else {
                    addList(list, amountlist);
                    if (findalready || (notes.equals(list) && beginScroll)) {
//                        Log.e("已经找到", "已经找到");
                        Log.e("停止", "停止");
                        beginScroll = false;
                        testActivity.wechatnote = false;
                        testActivity.acount_text = 300;

//                        Message message = new Message();
//                        message.what = 97;
//                        handler.sendMessageDelayed(message,5000);
//                        List<String> notes = list_a34;
//                        List<String> money = list_a34;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("插入数据", "插入数据");
                                DatabaseHelper.insertAlldata(datahelp);
                                AccessibilityHelper.performBack(service);
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(Config.BEGININSERT);
                                        QiangHongBaoService.service.sendBroadcast(intent);
                                        AccessibilityHelper.performBack(service);
                                    }
                                }, 2000);
                            }
                        }, 2000);
                        return;
//                        for (int i = 0; i < list.size(); i++) {
//                            list_a34.add(list.get(i).getText().toString());
//                        }
//                        for (int i = 0; i < amountlist.size(); i++) {
//                            list_amount.add(amountlist.get(i).getText().toString());
//                        }
//                        listview.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
//                        Log.e("开始下滑", "开始下滑");
//                        forward = true;
                    } else {
                        if (notes.equals(list) && beginScroll) {
//                            forward = true;
                            Log.e("滑到顶", "滑到顶");
//                            listview.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                        } else {
                            listview.performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
                            Log.e("后退", "后退");
                            beginScroll = true;
                        }

                    }

                }
                getNewallpay(list, datahelp);
            }
        }, 2000);
    }

    /**
     * 利用MD5进行加密
     * 　　* @param str  待加密的字符串
     * 　　* @return  加密后的字符串
     * 　　* @throws NoSuchAlgorithmException  没有这种产生消息摘要的算法
     * 　　 * @throws UnsupportedEncodingException
     */


    public static String EncoderByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定计算方法
//        MessageDigest md5 = MessageDigest.getInstance("MD5");
//        BASE64Encoder base64en = new BASE64Encoder();
//        //加密后的字符串
//        String newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(str.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
//        Hex hex = new Hex();
//        newstr = hex.encodeHex(md5.digest(str.getBytes("utf-8")));
        return "";
    }

    public static String getMoney(String string, String index) {
        return string.substring(string.indexOf(index) + 1, string.length());
    }

    public static Double getValue(String before, String change, int type) {
        return (double) (Math.rint(Double.parseDouble(before) * 100) + type * Math.rint((Double.parseDouble(change) * 100))) / 100;
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
                    Toast.makeText(service.getBaseContext(), "标记不识别", Toast.LENGTH_LONG).show();
                    return;
                }
                findalready = false;
                AccessibilityNodeInfo listview = AccessibilityHelper.findNodeInfosById(nodeInfo, "com.tencent.mm:id/a2i");
                AccessibilityNodeInfo amount = AccessibilityHelper.findNodeInfosById(nodeInfo, "com.tencent.mm:id/a4s");
                List<AccessibilityNodeInfo> amountlist = AccessibilityHelper.findNodeInfosByIds(nodeInfo, "com.tencent.mm:id/a4s");
                List<AccessibilityNodeInfo> list = AccessibilityHelper.findNodeInfosByIds(nodeInfo, "com.tencent.mm:id/a34");
                String value = (list.get(list.size() - 2).getText().toString());
                String beginTime = (list.get(list.size() - 3).getText().toString());
                String message = "message:";
                for (int i = 0; i < list.size(); i++) {
                    message += list.get(i).getText() + ",";
                }
                Log.e("message", "name:" + name + "-->message:" + message + "amount:" + amount.getText() + "money:" + money);
                Log.e("message", "value:" + value);
//                for(int i = 0 ; i < list.size() ; i++){
//                    if(list.get(i).equals(beginTime)){
//                        if(i < list.size()-1 &&list.get(i+1).equals(beginString)){
//                            findalready = true;
//                        }
//                    }
//                }
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
                    String begin = "";
//                    if(list_a34.size() > 0){
//                        begin = list_a34.get(list_a34.size() -1);
//                    }

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
                            testActivity.startsmooth = false;
                            setReplace(list_a34.size() / 2);
                            Log.e("message", "停止");
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
                        Double moneyitem = Double.parseDouble(amount.getText().toString().substring(amount.getText().toString().indexOf("￥") + 1, amount.getText().toString().length()));
                        money = money.substring(money.indexOf("¥") + 1, money.length());
                        beforemoney = ((double) ((int) (Double.parseDouble(money) * 100) - (int) (moneyitem * 100))) / 100 + "";
//                        SQLiteDatabase db = databaseHelper.getWritableDatabase();
////                        databaseHelper.inSertdata(beginTime, moneyitem, value);
//                        databaseHelper.inSertdata(beginTime, moneyitem, value, mynote, othernote, money);
//                            databaseHelper.inSertdata(db, new Detail(1, beginTime, beforemoney, moneyitem, money));
                        listview.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getText().toString().indexOf("收款成功") == -1) {
                                list_a34.add(list.get(i).getText().toString());
                            }
                        }
                        for (int i = 0; i < amountlist.size(); i++) {
                            list_amount.add(amountlist.get(i).getText().toString());
                        }
                        Log.e("message", "前进开始收录");
                    } else {
                        listview.performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
                        Log.e("message", "回退" + "value:" + value + "beginTime:" + beginTime + " beginString:" + beginString + " beginCreatetime:" + beginCreatetime);
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
