package com.codeboy.qianghongbao.demoActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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
import com.codeboy.qianghongbao.Adapter.ItemAdatpter;
import com.codeboy.qianghongbao.Config;
import com.codeboy.qianghongbao.QHBApplication;
import com.codeboy.qianghongbao.QiangHongBaoService;
import com.codeboy.qianghongbao.R;
import com.codeboy.qianghongbao.alipyActivity;
import com.codeboy.qianghongbao.model.AliDetailitem;
import com.codeboy.qianghongbao.model.Alidetail;
import com.codeboy.qianghongbao.model.AlipayItem;
import com.codeboy.qianghongbao.model.Detail;
import com.codeboy.qianghongbao.model.Return;
import com.codeboy.qianghongbao.util.AccessibilityHelper;
import com.codeboy.qianghongbao.util.AutoUpdateTask;
import com.codeboy.qianghongbao.util.BitmapUtils;
import com.codeboy.qianghongbao.util.JsonUtil;
import com.codeboy.qianghongbao.util.NetworkUtil;
import com.codeboy.qianghongbao.util.NotifyHelper;
import com.codeboy.qianghongbao.widget.Alipaylist;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.codeboy.qianghongbao.util.AccessibilityHelper.findNodeInfosByText;


public class DemoActivity extends Activity implements View.OnClickListener {
    private Button start,restop, receive, stop, getState, root, updateState, sure, edit, clear, ip, wechat, bankreceive, makesrc, bulu, playbulu, checkimg, CEB, PSBC, hongbao, NX, CS, makesure, jsreceive;
    private NotifyHelper notifyHelper;
    public static String money, limitString, withrowamount, belongbankCard, bankState, plaftfrom, itemmoney;
    public static String account;
    private EditText getAccount, limit, roottest, ipcontent;
    private boolean startRecrive, toWithrow;
    public static int number = 0;
    private ItemAdatpter itemAdatpter;
    public static int versions = 0;
    private Alipaylist alipaylist;
    private int times = 50000;
    private AlipayItemList alipayItemList;
    public static boolean toDemo;
    AliDetailitem aliDetailitem;
    public static List<Alidetail> alilist;
    public static List<AlipayItem> aliList;
    private Boolean clearString = false, innernet = false, updown, allcan;
    private AccessibilityNodeInfo detail, nodeInfo, qianbao, Withdraw, h5_pc_container, webview, birdnestContainer, withdrawAll, my;
    //    public static String url = "http://midpaydemo.com:8080/";
//    public static String url = "http://47.75.144.122:8089/";
    public static String url = "http://api.wandapay88.com/";
    //    public static String url = "http://10.10.32.153:8081/";
    //    public static String url = "http://103.205.121.9:8081/";
    private List<AccessibilityNodeInfo> listitem, timeInfo1, timeInfo2, billName, billAmount;
    private TextView wechatName, wechatAmount, wechatState, version, dayAmount, newcompany;
    private List<Alidetail> list = new ArrayList<>();
    private CheckBox withrow, innernetwork, qrCheck, orderby;
    private String wihtrowDeposit, withorwAmount, withrowFee = "0";
    //    private List<AlipayItem> alipayitemlist = new ArrayList<>();
    private ListView listview;
    private String realName;
    private int clickIndex;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.e("nodeInfo:", nodeInfo + "");
            if (QiangHongBaoService.service != null)
                nodeInfo = QiangHongBaoService.service.getRootInActiveWindow();//com.alipay.mobile.bill.list:id/bill_list_view
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Log.e("number", number + "");
                    if (number > 5) {
                        number = 0;
                        bankState = "DISABLED";
                        Intent intentstate = new Intent(Config.UPDATESTATE);
                        DemoActivity.this.sendBroadcast(intentstate);
                        AccessibilityHelper.performBack(QiangHongBaoService.service);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.eg.android.AlipayGphone");
                                startActivity(LaunchIntent);
                                handler.sendEmptyMessageDelayed(0, 1000);
                            }
                        }, 2000);
                    } else {
                        try {
                            List<AccessibilityNodeInfo> list = AccessibilityHelper.findNodeInfosByTexts(nodeInfo, "元");
                            qianbao = list.get(list.size() - 1);
//                            if (qianbao.getText().toString().indexOf("今日") != -1)
//                                qianbao = AccessibilityHelper.findNodeInfosByTexts(nodeInfo, "元").get(1);
                            if (qianbao != null) {
                                money = qianbao.getText().toString().replace(",", "").replace(" 元", "");
                                Toast.makeText(DemoActivity.this, money, Toast.LENGTH_LONG).show();
                                if (Double.parseDouble(money) > Double.parseDouble(limitString)) {
                                    if (aliDetailitem.getState().equals("DISABLED")) {
                                        notifyHelper.click(handler, qianbao.getParent(), 1000);
                                        sendEmptyMessageDelayed(2, 3000);
                                        return;
                                    } else {
                                        bankState = "NORMAL";
                                        Intent intentstate = new Intent(Config.UPDATESTATE);
                                        DemoActivity.this.sendBroadcast(intentstate);
                                    }
                                } else {
                                    if (NotifyHelper.player != null)
                                        NotifyHelper.player.stop();
                                    NotifyHelper.startlimit = false;
                                }
                            }
                        } catch (Exception e) {
                        }
                        detail = findNodeInfosByText(nodeInfo, "账单");
                        if (detail != null)
                            notifyHelper.click(handler, detail.getParent(), 3000);
                        sendEmptyMessageDelayed(1, 10000);
                    }
                    break;
                case 1:
                    listitem = AccessibilityHelper.findNodeInfosByIds(nodeInfo, "com.alipay.mobile.bill.list:id/listItem");
                    timeInfo1 = AccessibilityHelper.findNodeInfosByIds(nodeInfo, "com.alipay.mobile.bill.list:id/timeInfo1");
                    timeInfo2 = AccessibilityHelper.findNodeInfosByIds(nodeInfo, "com.alipay.mobile.bill.list:id/timeInfo2");
                    billName = AccessibilityHelper.findNodeInfosByIds(nodeInfo, "com.alipay.mobile.bill.list:id/billName");
                    billAmount = AccessibilityHelper.findNodeInfosByIds(nodeInfo, "com.alipay.mobile.bill.list:id/billAmount");
                    if (listitem == null) {
                        sendEmptyMessageDelayed(0, 2000);
                        return;
                    }
                    int length = listitem.size() > 8 ? 8 : listitem.size();
                    alipaylist = new Alipaylist();
                    list = new ArrayList<>();
                    alipaylist.setAlipayAccount(account);
                    clickIndex = length - 1;
                    for (int i = 0; i < length; i++) {
                        Alidetail detail = new Alidetail();
                        String amount = billAmount.get(i).getText().toString();
                        String content = billName.get(i).getText().toString();
                        String day = timeInfo1.get(i).getText().toString();
                        String time = timeInfo2.get(i).getText().toString();
                        if (amount.indexOf("+") != -1 && Double.parseDouble(amount.replace(",", "").replace("+", "")) > 0) {
                            detail.setAmount(amount.replace(",", "").replace("+", ""));
                            detail.setWechatName(account);
//                            detail.setTranTime(new Date(getTimeByCalendar(day) + " " + time + ":00").getTime() + "");
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date d = new Date();
                            try {
                                d = sdf.parse(getTimeByCalendar(day) + " " + time + ":00");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            detail.setTranTime(d.getTime() + "");
//                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                            if (content.split("-").length > 1) {
                                detail.setNickName(content.split("-")[1].replaceAll("[^\\u4e00-\\u9fa5\\w]", ""));
                                detail.setNote(content.split("-")[0].replaceAll("[^\\u4e00-\\u9fa5\\w]", ""));
                            } else {
                                detail.setNote(content.split("-")[0].replaceAll("[^\\u4e00-\\u9fa5\\w]", ""));
                            }

                            detail.setDepositNumber(time.replace("-", "").replace(" ", "").replace(":", "") + account + amount.replace(",", "").replace("+", ""));
                            detail.setCreateUser("auto");
                            list.add(detail);
                        }
                    }
                    alipaylist.setDepositRecords(list);
                    alipaylist.setAlipayAccount(account);
                    Intent intent = new Intent(Config.BEGININSERTTASK);
                    DemoActivity.this.sendBroadcast(intent);
                    AccessibilityHelper.performBack(QiangHongBaoService.service);
                    sendEmptyMessageDelayed(0, 5000);
                    number++;
                    break;
                case 2:
                    Withdraw = AccessibilityHelper.findNodeInfosByText(nodeInfo, "提现");
                    notifyHelper.click(handler, Withdraw.getParent(), 1000);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    nodeInfo = QiangHongBaoService.service.getRootInActiveWindow();
                                    Log.e("nodeInfo", nodeInfo + "");
                                    BitmapUtils.execShellCmd("input tap 400 400");
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            withrowamount = limitString + "";
                                            BitmapUtils.execShellCmd("input text " + withrowamount);
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    BitmapUtils.execShellCmd("input tap 600 700");
                                                }
                                            }, 2000);
                                        }
                                    }, 500);

//                                    confirm_btn = AccessibilityHelper.findNodeInfosById(nodeInfo, "android.widget.Button");
//                                    amount_edit = AccessibilityHelper.findNodeInfosByClassName(nodeInfo, "android.widget.EditText");
//                                    arguments = new Bundle();
//                                    arguments.putCharSequence(
//                                            AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
//                                            "20");
//                                    amount_edit.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
//                                    notifyHelper.click(handler, confirm_btn.getParent(), 2000);
//
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
//                                            notifyHelper.click(handler, confirmBtn.getParent(), 1000);
//                                            BitmapUtils.execShellCmd("input tap 500 1200");
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    BitmapUtils.execShellCmd("input tap 200 900");
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
//                                                            BitmapUtils.execShellCmd("input tap 600 1100");
                                                            BitmapUtils.execShellCmd("input tap 600 1000");
                                                            handler.postDelayed(new Runnable() {
                                                                @Override
                                                                public void run() {
//                                                                    BitmapUtils.execShellCmd("input tap 400 1200");
                                                                    BitmapUtils.execShellCmd("input tap 400 1100");
                                                                    handler.postDelayed(new Runnable() {
                                                                        @Override
                                                                        public void run() {
//                                                                            BitmapUtils.execShellCmd("input tap 200 1100");
                                                                            BitmapUtils.execShellCmd("input tap 400 1100");
                                                                            handler.postDelayed(new Runnable() {
                                                                                @Override
                                                                                public void run() {
//                                                                                    BitmapUtils.execShellCmd("input tap 200 900");
                                                                                    BitmapUtils.execShellCmd("input tap 600 1000");
                                                                                    handler.postDelayed(new Runnable() {
                                                                                        @Override
                                                                                        public void run() {
//                                                                                            BitmapUtils.execShellCmd("input tap 400 1200");
                                                                                            BitmapUtils.execShellCmd("input tap 200 900");
                                                                                            handler.sendEmptyMessageDelayed(3, 6000);

                                                                                        }
                                                                                    }, 2000);
                                                                                }
                                                                            }, 2000);
                                                                        }
                                                                    }, 2000);
                                                                }
                                                            }, 2000);
                                                        }
                                                    }, 2000);
                                                }
                                            }, 2000);
                                        }
                                    }, 5000);
                                }
                            }, 5000);
                        }
                    }, 3000);
//                    BitmapUtils.execShellCmd();
                    break;
                case 3:
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AccessibilityNodeInfo result = AccessibilityHelper.findNodeInfosById(QiangHongBaoService.service.getRootInActiveWindow(), "com.alipay.mobile.ui:id/title_bar_title");
                            if (result != null) {
                                Log.e("result", result.toString());
                                if (result.getText().toString().equals("结果详情")) {
                                    Log.e("result", result.getText().toString() + "");
                                    Intent intentstate = new Intent(Config.UPDATEWITHROW);
                                    DemoActivity.this.sendBroadcast(intentstate);
                                }
                            }
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            AccessibilityHelper.performBack(QiangHongBaoService.service);
                                        }
                                    }, 2000);
                                    AccessibilityHelper.performBack(QiangHongBaoService.service);
                                    handler.sendEmptyMessageDelayed(8, 2000);
                                }
                            }, 3000);
                        }
                    }, 1000);
                    break;
                //获取内容(回到首页)
                case 8:
                    Log.e("number", number + "");
                    if (number > 5) {
                        number = 0;
                        AccessibilityHelper.performBack(QiangHongBaoService.service);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.eg.android.AlipayGphone");
                                startActivity(LaunchIntent);
                                handler.sendEmptyMessageDelayed(8, 1000);
                            }
                        }, 2000);
                    } else {
                        if (nodeInfo == null) {
                            AccessibilityHelper.performBack(QiangHongBaoService.service);
                            handler.sendEmptyMessageDelayed(8, 1000);
                            number++;
                            return;
                        }
                        List<AccessibilityNodeInfo> mylist = AccessibilityHelper.findNodeInfosByTexts(nodeInfo, "我的");
                        if (mylist.size() != 0)
                            AccessibilityHelper.performClick(mylist.get(mylist.size() - 1));
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    List<AccessibilityNodeInfo> list = AccessibilityHelper.findNodeInfosByTexts(nodeInfo, "元");
                                    qianbao = list.get(list.size() - 1);
                                    if (qianbao != null) {
                                        money = qianbao.getText().toString().replace(",", "").replace(" 元", "");
                                        Toast.makeText(DemoActivity.this, money + "", Toast.LENGTH_LONG).show();
                                        if (money != null && toWithrow && Double.parseDouble(itemmoney) == Double.parseDouble(money) && Double.parseDouble(itemmoney) >= Double.parseDouble(limitString)) {
                                            notifyHelper.click(handler, qianbao.getParent(), 15000);
                                            sendEmptyMessageDelayed(14, 3000);
                                            return;
                                        }
                                        if (Double.parseDouble(money) >= Double.parseDouble(aliDetailitem.getDaylimit())) {
                                            Intent intenteas = new Intent(Config.ACTION_NOTIFY_LISTENER_SERVICE_CONNECT);
                                            DemoActivity.this.sendBroadcast(intenteas);
                                        } else {
                                            if (NotifyHelper.player != null)
                                                NotifyHelper.player.stop();
                                            NotifyHelper.startlimit = false;
                                        }
                                    }
                                } catch (Exception e) {
                                }
                            }
                        }, 1000);
                        detail = findNodeInfosByText(nodeInfo, "账单");
                        intent = new Intent(Config.GETLIST);
                        DemoActivity.this.sendBroadcast(intent);
                        intent = new Intent(Config.GETSTATE);
                        DemoActivity.this.sendBroadcast(intent);
                        if (detail != null) {
                            notifyHelper.click(handler, detail.getParent(), 10000);
                            sendEmptyMessageDelayed(9, 12000);
                        } else {
                            sendEmptyMessageDelayed(8, 2000);
                            number++;
                        }
                    }
                    break;
                //过滤去重
                case 9:
                    listitem = AccessibilityHelper.findNodeInfosByIds(nodeInfo, "com.alipay.mobile.bill.list:id/listItem");
                    timeInfo1 = AccessibilityHelper.findNodeInfosByIds(nodeInfo, "com.alipay.mobile.bill.list:id/timeInfo1");
                    timeInfo2 = AccessibilityHelper.findNodeInfosByIds(nodeInfo, "com.alipay.mobile.bill.list:id/timeInfo2");
                    billName = AccessibilityHelper.findNodeInfosByIds(nodeInfo, "com.alipay.mobile.bill.list:id/billName");
                    billAmount = AccessibilityHelper.findNodeInfosByIds(nodeInfo, "com.alipay.mobile.bill.list:id/billAmount");
                    if (listitem == null || listitem.size() == 0) {
                        AccessibilityHelper.performBack(QiangHongBaoService.service);
                        sendEmptyMessageDelayed(8, 2000);
                        number++;
                        return;
                    }
                    sendEmptyMessageDelayed(12, 2000);
                    break;
                //选择点击
                case 10:
                    realName = null;
                    if (clickIndex < 0) {
                        number++;
                        AccessibilityHelper.performBack(QiangHongBaoService.service);
                        handler.sendEmptyMessageDelayed(8, 500);
                    } else {
                        if (billAmount.get(clickIndex).getText().toString().indexOf("-") != -1 || (billAmount.get(clickIndex).getText().toString().indexOf("+") == -1 && billName.get(clickIndex).getText().toString().indexOf("余额提现") == -1) || (billName.get(clickIndex).getText().toString().indexOf("余额宝") != -1)) {
                            handler.sendEmptyMessageDelayed(10, 500);
                            clickIndex--;
                        } else {
                            AccessibilityHelper.performClick(listitem.get(clickIndex));
                            handler.sendEmptyMessageDelayed(11, 5000);
                        }
                    }
                    break;
                //获取内容
                case 11:
//                    try {
                    birdnestContainer = AccessibilityHelper.findNodeInfosById(nodeInfo, "com.alipay.mobile.bill.detail:id/birdnestContainer");
                    if (birdnestContainer != null) {
                        BitmapUtils.accesslist = new ArrayList<AccessibilityNodeInfo>();
                        BitmapUtils.list = new ArrayList<String>();
                        BitmapUtils.getNodeInfo(birdnestContainer, "android.widget.TextView", BitmapUtils.accesslist);
                        for (int i = 0; i < BitmapUtils.accesslist.size(); i++) {
                            if (BitmapUtils.accesslist.get(i).getContentDescription() != null && !BitmapUtils.accesslist.get(i).getContentDescription().toString().equals(""))
                                BitmapUtils.list.add(BitmapUtils.accesslist.get(i).getContentDescription().toString());
                            else if (BitmapUtils.accesslist.get(i).getText() != null && !BitmapUtils.accesslist.get(i).getText().toString().equals(""))
                                BitmapUtils.list.add(BitmapUtils.accesslist.get(i).getText().toString());
                        }
                        Log.e("list", BitmapUtils.list.toString());
                    } else {
                        h5_pc_container = AccessibilityHelper.findNodeInfosById(nodeInfo, "com.alipay.mobile.nebula:id/h5_pc_container");
                        if (h5_pc_container == null || h5_pc_container.getChild(0).getChild(0).getChildCount() == 0) {
                            AccessibilityHelper.performBack(QiangHongBaoService.service);
                            sendEmptyMessageDelayed(10, 2000);
                            return;
                        } else {
                            webview = h5_pc_container.getChild(0).getChild(0).getChild(0);
                            BitmapUtils.accesslist = new ArrayList<AccessibilityNodeInfo>();
                            BitmapUtils.list = new ArrayList<String>();
                            BitmapUtils.getNodeInfo(webview, "android.view.View", BitmapUtils.accesslist);
                            for (int i = 0; i < BitmapUtils.accesslist.size(); i++) {
                                if (BitmapUtils.accesslist.get(i).getContentDescription() != null && !BitmapUtils.accesslist.get(i).getContentDescription().toString().equals("")) {
                                    BitmapUtils.list.add(BitmapUtils.accesslist.get(i).getContentDescription().toString());
                                } else if (BitmapUtils.accesslist.get(i).getText() != null && BitmapUtils.accesslist.get(i).getText().toString().equals("")) {
                                    BitmapUtils.list.add(BitmapUtils.accesslist.get(i).getText().toString());
                                }
                            }
                            Log.e("list", BitmapUtils.list.toString());
                        }
                    }
                    alipayItemList = new AlipayItemList();
                    AlipayItem alipayItem = new AlipayItem();
                    if (BitmapUtils.list.size() < 6) {
                        AccessibilityHelper.performBack(QiangHongBaoService.service);
                        handler.sendEmptyMessageDelayed(10, 2000);
                    }
                    alipayItem.setPlatfrom(plaftfrom);
                    alipayItem.setCreateUser("auto");
                    if (BitmapUtils.list.get(0).indexOf("+") != -1) {
                        realName = "你好";
                        if (realName == null) {
                            AccessibilityHelper.performClick(webview.getChild(0).getChild(0));
                            handler.sendEmptyMessageDelayed(16, 5000);
                            return;
                        }
                        alipayItem.setAmount(Double.parseDouble(BitmapUtils.list.get(0).replace(",", "").replace("+", "")));
                        alipayItem.setWechatName(account);
                        alipayItem.setRealName(realName);
                        for (int i = 0; i < BitmapUtils.list.size(); i++) {
                            if (BitmapUtils.list.get(i).equals("对方账户"))
                                alipayItem.setPayAccount(BitmapUtils.list.get(i + 1).replaceAll("[^\\u4e00-\\u9fa5\\w\\*]", ""));
                            if (BitmapUtils.list.get(i).equals("收款理由") || BitmapUtils.list.get(i).equals("商品说明"))
                                alipayItem.setNote(BitmapUtils.list.get(i + 1));
                            if (BitmapUtils.list.get(i).equals("转账备注")) {
                                if (!innernet && "1".equals(QHBApplication.platfrom))
                                    alipayItem.setNote(BitmapUtils.list.get(i + 1));
                                else
                                    alipayItem.setNickName(BitmapUtils.list.get(i + 1));
                            }
                            if (BitmapUtils.list.get(i).equals("订单号"))
                                alipayItem.setDepositNumber(BitmapUtils.list.get(i + 1));
                            if (BitmapUtils.list.get(i).equals("创建时间"))
                                alipayItem.setTransferTime(BitmapUtils.list.get(i + 1));
                        }
                        List alipayitemlist = new ArrayList();
                        alipayItemList.setAlipayAccount(account);
                        if (alipayItem.getNote() == null)
                            alipayItem.setNote(alipayItem.getNickName());
                        alipayitemlist.add(alipayItem);
                        alipayItemList.setDepositRecords(alipayitemlist);
//                        if(allcan){
//                            AccessibilityNodeInfo clickName = AccessibilityHelper.findNodeInfosByClassName(nodeInfo, "android.widget.Button");
//                            AccessibilityHelper.performClick(clickName);
//                        }else{

                        intent = new Intent(Config.BEGININSERTTASK);
                        DemoActivity.this.sendBroadcast(intent);
                        AccessibilityHelper.performBack(QiangHongBaoService.service);
                        handler.sendEmptyMessageDelayed(10, 5000);
                        number++;
//                        }
                    } else if (BitmapUtils.list.get(1).indexOf("+") != -1) {
                        alipayItem.setAmount(Double.parseDouble(BitmapUtils.list.get(1).replace(",", "").replace("+", "")));
                        alipayItem.setPayAccount(BitmapUtils.list.get(0));
                        alipayItem.setWechatName(account);
                        for (int i = 0; i < BitmapUtils.list.size(); i++) {
                            if (BitmapUtils.list.get(i).equals("商品说明") || BitmapUtils.list.get(i).equals("收款理由"))
                                alipayItem.setNote(BitmapUtils.list.get(i + 1));
                            if (BitmapUtils.list.get(i).equals("转账备注")) {
                                if (!innernet && "1".equals(QHBApplication.platfrom))
                                    alipayItem.setNote(BitmapUtils.list.get(i + 1));
                                else
                                    alipayItem.setNickName(BitmapUtils.list.get(i + 1));
                            }
                            if (BitmapUtils.list.get(i).equals("订单号"))
                                alipayItem.setDepositNumber(BitmapUtils.list.get(i + 1));
                            if (BitmapUtils.list.get(i).equals("创建时间"))
                                alipayItem.setTransferTime(BitmapUtils.list.get(i + 1));
                            if (BitmapUtils.list.get(i).equals("对方账户"))
                                alipayItem.setPayAccount(BitmapUtils.list.get(i + 1).replaceAll("[^\\u4e00-\\u9fa5\\w\\*]", ""));
                        }
                        List alipayitemlist = new ArrayList();
                        alipayItemList.setAlipayAccount(account);
                        if (alipayItem.getNote() == null)
                            alipayItem.setNote(alipayItem.getNickName());
                        alipayitemlist.add(alipayItem);
                        alipayItemList.setDepositRecords(alipayitemlist);
                        intent = new Intent(Config.BEGININSERTTASK);
                        DemoActivity.this.sendBroadcast(intent);
                        AccessibilityHelper.performBack(QiangHongBaoService.service);
                        handler.sendEmptyMessageDelayed(10, 5000);
                        number++;

                    } else {
                        for (int i = 0; i < BitmapUtils.list.size(); i++) {
                            withorwAmount = Double.parseDouble(BitmapUtils.list.get(1).replace(",", "").replace("+", "")) + "";
                            if (BitmapUtils.list.get(i).equals("订单号"))
                                wihtrowDeposit = BitmapUtils.list.get(i + 1);
                            if (BitmapUtils.list.get(i).equals("服务费"))
                                withrowFee = BitmapUtils.list.get(i + 1);
                        }
                        intent = new Intent(Config.UPDATEWITHROW);
                        DemoActivity.this.sendBroadcast(intent);
                        AccessibilityHelper.performBack(QiangHongBaoService.service);
                        handler.sendEmptyMessageDelayed(10, 5000);
                        number++;
                    }
//                    } catch (Exception e) {
//                        AccessibilityHelper.performBack(QiangHongBaoService.service);
//                        sendEmptyMessageDelayed(10, 2000);
//                        number++;
//                    }
                    break;
                case 12:
                    length = listitem.size() > 8 ? 8 : listitem.size();
                    alipaylist = new Alipaylist();
                    list = new ArrayList<>();
                    alipaylist.setAlipayAccount(account);
                    clickIndex = length - 1;
                    for (int i = 0; i < length; i++) {
                        if (clickIndex != length - 1)
                            break;
                        String amount = billAmount.get(i).getText().toString();
                        String content = billName.get(i).getText().toString().replaceAll(" ", "");
                        String time = timeInfo2.get(i).getText().toString();
                        if (amount.indexOf("+") != -1 && Double.parseDouble(amount.replace(",", "").replace("+", "")) > 0) {
                            for (int j = 0; j < aliList.size(); j++) {
                                Boolean index = false;
                                if (!("1".equals(QHBApplication.platfrom) || innernet)) {
                                    index = (aliList.get(j).getAmount() == Double.parseDouble(amount.replace(",", "").replace("+", "")) && content.indexOf(aliList.get(j).getNote().replaceAll(" ", "")) != -1 && aliList.get(j).getTransferTime().indexOf(time) != -1);
                                }
//                                else if (updown)
////                                    index = (aliList.get(j).getAmount() == Double.parseDouble(amount.replace(",", "").replace("+", "")) && content.indexOf(aliList.get(j).getNote().replaceAll(" ", "")) != -1 && aliList.get(j).getTransferTime().indexOf(time) != -1);
//                                    index = (aliList.get(j).getAmount() == Double.parseDouble(amount.replace(",", "").replace("+", "")) && content.indexOf(aliList.get(j).getPayAccount().replaceAll(" ", "")) != -1 && aliList.get(j).getTransferTime().indexOf(time) != -1);
                                else
                                    index = (aliList.get(j).getAmount() == Double.parseDouble(amount.replace(",", "").replace("+", "")) && aliList.get(j).getTransferTime().indexOf(time) != -1);
//                                if (aliList.get(j).getAmount() == Double.parseDouble(amount.replace(",", "").replace("+", "")) && content.indexOf(aliList.get(j).getNote().replaceAll(" ", "")) != -1 && aliList.get(j).getTransferTime().indexOf(time) != -1) {
//                                if (aliList.get(j).getAmount() == Double.parseDouble(amount.replace(",", "").replace("+", "")) && aliList.get(j).getTransferTime().indexOf(time) != -1) {
                                if (index) {
                                    clickIndex = i;
                                    break;
                                }
                            }
                        }
                    }
                    Log.e("clickIndex", clickIndex + " 和" + listitem.size());
                    getDiff(clickIndex);
                    if (aliList.size() != 0)
                        clickIndex--;
                    handler.sendEmptyMessageDelayed(10, 0);
                    break;
                case 14:
                    Withdraw = AccessibilityHelper.findNodeInfosByText(nodeInfo, "提现");
                    notifyHelper.click(handler, Withdraw.getParent(), 1000);
//                    nodeInfo = QiangHongBaoService.service.getRootInActiveWindow();
//                    Log.e("nodeInfo", nodeInfo + "");

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    withdrawAll = AccessibilityHelper.findNodeInfosById(nodeInfo, "com.alipay.android.phone.wealth.banlance:id/withdrawAll");
                                    notifyHelper.click(handler, withdrawAll, 1000);
                                    BitmapUtils.execShellCmd("input tap 600 570");
//                                    BitmapUtils.execShellCmd("input tap 600 550");

//                                    handler.postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            if (Double.parseDouble(money) % 100 < 10)
//                                                withrowamount = (Double.parseDouble(money) - Double.parseDouble(money) % 100) + "";
//                                            else
//                                                withrowamount = (Double.parseDouble(money) - 10) + "";
//
//                                            BitmapUtils.execShellCmd("input text " + withrowamount);
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            BitmapUtils.execShellCmd("input tap 600 700");
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    BitmapUtils.execShellCmd("input tap 500 750");
                                                }
                                            }, 2000);

                                        }
                                    }, 1000);
//                                        }
//                                    }, 500);

                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
//                                                    BitmapUtils.execShellCmd("input tap 200 900");
                                                    BitmapUtils.execShellCmd("input tap 200 900");
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
//                                                            BitmapUtils.execShellCmd("input tap 600 1100");
                                                            BitmapUtils.execShellCmd("input tap 600 1000");
                                                            handler.postDelayed(new Runnable() {
                                                                @Override
                                                                public void run() {
//                                                                    BitmapUtils.execShellCmd("input tap 400 1200");
                                                                    BitmapUtils.execShellCmd("input tap 400 1100");
                                                                    handler.postDelayed(new Runnable() {
                                                                        @Override
                                                                        public void run() {
//                                                                            BitmapUtils.execShellCmd("input tap 200 1100");
                                                                            BitmapUtils.execShellCmd("input tap 400 1100");
                                                                            handler.postDelayed(new Runnable() {
                                                                                @Override
                                                                                public void run() {
//                                                                                    BitmapUtils.execShellCmd("input tap 200 900");
                                                                                    BitmapUtils.execShellCmd("input tap 600 1000");
                                                                                    handler.postDelayed(new Runnable() {
                                                                                        @Override
                                                                                        public void run() {
//                                                                                            BitmapUtils.execShellCmd("input tap 400 1200");
                                                                                            BitmapUtils.execShellCmd("input tap 200 900");
//                                                                                            handler.sendEmptyMessageDelayed(3, 6000);
                                                                                            handler.postDelayed(new Runnable() {
                                                                                                @Override
                                                                                                public void run() {
                                                                                                    AccessibilityHelper.performBack(QiangHongBaoService.service);
                                                                                                    handler.postDelayed(new Runnable() {
                                                                                                        @Override
                                                                                                        public void run() {
                                                                                                            AccessibilityHelper.performBack(QiangHongBaoService.service);
//                                                                                                            Withdraw = AccessibilityHelper.findNodeInfosByText(nodeInfo, "提现");
//                                                                                                            if (Withdraw != null) {
//                                                                                                                handler.postDelayed(new Runnable() {
//                                                                                                                    @Override
//                                                                                                                    public void run() {
                                                                                                            handler.postDelayed(new Runnable() {
                                                                                                                @Override
                                                                                                                public void run() {
                                                                                                                    AccessibilityHelper.performBack(QiangHongBaoService.service);
                                                                                                                    handler.sendEmptyMessageDelayed(8, 2000);
                                                                                                                }
                                                                                                            }, 2000);
                                                                                                        }
                                                                                                    }, 3000);
                                                                                                }
                                                                                            }, 5000);
                                                                                        }
                                                                                    }, 2000);
                                                                                }
                                                                            }, 2000);
                                                                        }
                                                                    }, 2000);
                                                                }
                                                            }, 2000);
                                                        }
                                                    }, 2000);
                                                }
                                            }, 2000);
                                        }
                                    }, 5000);
                                }
                            }, 5000);
                        }
                    }, 5000);
                    break;
                case 16://点击名字
//                    List<AccessibilityNodeInfo> clickName = AccessibilityHelper.findNodeInfosByClassName("android.widget.Button");
                    handler.sendEmptyMessageDelayed(11, 8000);
                    AccessibilityNodeInfo realnameNodeInfo = AccessibilityHelper.findNodeInfosById(QiangHongBaoService.service.getRootInActiveWindow(), "com.alipay.mobile.transferapp:id/tf_receiveNameTextView");
                    realName = "未知";
                    if (realnameNodeInfo != null)
                        realName = realnameNodeInfo.getText().toString().substring(realnameNodeInfo.getText().toString().indexOf("（") + 1, realnameNodeInfo.getText().toString().indexOf("）"));
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AccessibilityHelper.performBack(QiangHongBaoService.service);
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    AccessibilityHelper.performBack(QiangHongBaoService.service);
                                }
                            }, 2000);
                        }
                    }, 3000);


                    break;
                //开始微信
                case 21:
                    Log.e("number", number + "");
                    if (number > 5) {
                        number = 0;
                        AccessibilityHelper.performBack(QiangHongBaoService.service);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
                                startActivity(LaunchIntent);
                                handler.sendEmptyMessageDelayed(21, 1000);
                            }
                        }, 2000);
                    } else {
                        DemoActivity.alilist = new ArrayList<>();
                        DemoActivity.aliList = new ArrayList<>();
                        BitmapUtils.infolist = new ArrayList<>();
//                        notifyHelper.click(handler, AccessibilityHelper.findNodeInfosByText(nodeInfo,"微信支付"), 500);
                        List<AccessibilityNodeInfo> nodelist = AccessibilityHelper.findNodeInfosByIds(nodeInfo, "com.tencent.mm:id/as6");
                        for (int i = 0; i < nodelist.size(); i++) {
                            AccessibilityNodeInfo clickitem = nodelist.get(i);
                            if (clickitem.getText() != null && clickitem.getText().toString().equals("微信支付")) {
                                notifyHelper.click(handler, clickitem, 0);
                                break;
                            }
                        }
//                        notifyHelper.click(handler, AccessibilityHelper.findNodeInfosById(nodeInfo, "com.tencent.mm:id/as6"), 0);
//                        notifyHelper.click(handler, AccessibilityHelper.findNodeInfosByText(nodeInfo, "微信支付"), 500);
                        handler.sendEmptyMessageDelayed(22, 5000);
                        number++;
//                        intent = new Intent(Config.GETLIST);
//                        DemoActivity.this.sendBroadcast(intent);
                    }
                    break;
                case 22:
                    aliList = new ArrayList<>();
//                    BitmapUtils.getWechat(handler, AccessibilityHelper.findNodeInfosById(nodeInfo, "com.tencent.mm:id/ab_"), DemoActivity.this);
                    BitmapUtils.getNewWechat(handler, AccessibilityHelper.findNodeInfosById(nodeInfo, "com.tencent.mm:id/ab_"), DemoActivity.this);
                    Log.e("result", DemoActivity.aliList.toString());
                    alipayItemList = new AlipayItemList();
                    alipayItemList.setAlipayAccount(account);
//                    alipayitemlist.add(alipayItem);

                    alipayItemList.setDepositRecords(DemoActivity.aliList);
                    intent = new Intent(Config.BEGININSERTTASK);
                    DemoActivity.this.sendBroadcast(intent);
                    AccessibilityHelper.performBack(QiangHongBaoService.service);
                    handler.sendEmptyMessageDelayed(21, 5000);
                    break;
                //关大银行
                case 23:
                    money = "";
                    getCEBFromPhone();
                    break;
                //邮政银行
                case 24:
                    money = "";
                    getPSBCFromPhone();
                    break;
                //银行卡收录
                case 25:
                    money = "";
//                    getNX();
                    getMoreNx();
//                    getMORENX();
                    break;
                case 26:
                    getMoreCs();
                    break;
                case 27:
                    getJXPhone();
                    break;
                case 30:
                    money = "";
                    getAbFromPhone();
                    break;
                case 31:
                    AccessibilityNodeInfo collect = AccessibilityHelper.findNodeInfosByText(nodeInfo, "收钱");
                    notifyHelper.click(handler, collect, 2000);
                    handler.sendEmptyMessageDelayed(32, 5000);
                    break;
                case 32:
                    AccessibilityNodeInfo setMoney = AccessibilityHelper.findNodeInfosById(nodeInfo, "com.alipay.mobile.payee:id/payee_QRCodePayModifyMoney");
                    notifyHelper.click(handler, setMoney, 2000);
                    handler.sendEmptyMessageDelayed(33, 5000);
                    break;
                case 33:
                    AccessibilityNodeInfo clickResult = AccessibilityHelper.findNodeInfosById(nodeInfo, "com.alipay.mobile.payee:id/payee_QRAddBeiZhuLink");
                    notifyHelper.click(handler, clickResult, 2000);
                    handler.sendEmptyMessageDelayed(34, 5000);
                    break;
                case 34:
                    List<AccessibilityNodeInfo> transfer = AccessibilityHelper.findNodeInfosByIds(nodeInfo, "com.alipay.mobile.ui:id/content");
                    Bundle arguments = new Bundle();
                    arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, "100");
                    transfer.get(0).performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                    arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, "cs01");
                    transfer.get(1).performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
//                    AccessibilityNodeInfo transfer = AccessibilityHelper.findNodeInfosById(nodeInfo, "com.alipay.mobile.payee:id/payee_QRmoneySetInput");
//                    Bundle arguments = new Bundle();
//                    arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, "100");
//                    transfer.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                    AccessibilityNodeInfo sure = AccessibilityHelper.findNodeInfosById(nodeInfo, "com.alipay.mobile.payee:id/payee_NextBtn");
                    notifyHelper.click(handler, sure, 5000);
                    handler.sendEmptyMessageDelayed(35, 8000);
                    break;
                case 35:
                    AccessibilityNodeInfo picsure = AccessibilityHelper.findNodeInfosById(nodeInfo, "com.alipay.mobile.ui:id/title_bar_generic_button");
                    notifyHelper.click(handler, picsure, 2000);
                    handler.sendEmptyMessageDelayed(36, 5000);
                    break;
                case 36:
                    AccessibilityNodeInfo picto = AccessibilityHelper.findNodeInfosByText(nodeInfo, "保存二维码到相册");
                    notifyHelper.click(handler, picto, 2000);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            BitmapUtils.getImageFromCamera(DemoActivity.this);
                        }
                    }, 5000);
                    break;
                case 40:
                    h5_pc_container = AccessibilityHelper.findNodeInfosById(nodeInfo, "com.alipay.mobile.nebula:id/h5_pc_container");
                    if (h5_pc_container == null || h5_pc_container.getChild(0).getChild(0).getChildCount() == 0) {
//                        AccessibilityHelper.performBack(QiangHongBaoService.service);
//                        sendEmptyMessageDelayed(10, 2000);
                        return;
                    } else {
                        webview = h5_pc_container.getChild(0).getChild(0).getChild(0);
                        BitmapUtils.accesslist = new ArrayList<AccessibilityNodeInfo>();
                        BitmapUtils.list = new ArrayList<String>();
                        BitmapUtils.getNodeInfo(webview, "android.view.View", BitmapUtils.accesslist);
                        for (int i = 0; i < BitmapUtils.accesslist.size(); i++) {
                            if (BitmapUtils.accesslist.get(i).getContentDescription() != null && !BitmapUtils.accesslist.get(i).getContentDescription().toString().equals(""))
                                BitmapUtils.list.add(BitmapUtils.accesslist.get(i).getContentDescription().toString());
                            else if (BitmapUtils.accesslist.get(i).getText() != null && BitmapUtils.accesslist.get(i).getText().toString().equals(""))
                                BitmapUtils.list.add(BitmapUtils.accesslist.get(i).getText().toString());
                        }
                        Log.e("list", BitmapUtils.list.toString());
                        AlipayItem alipayItems = new AlipayItem();
                        alipayItems.setPlatfrom(plaftfrom);
                        alipayItems.setCreateUser("auto");
//                        if (BitmapUtils.list.get(0).indexOf("+") != -1) {
                        alipayItems.setAmount(Double.parseDouble(BitmapUtils.list.get(2).toString()));
                        alipayItems.setWechatName(account);
                        alipayItems.setNote(BitmapUtils.list.get(19).toString());
                        alipayItems.setPayAccount(BitmapUtils.list.get(0).toString());
                        alipayItems.setTransferTime(BitmapUtils.list.get(9).toString());
                        alipayItems.setDepositNumber(BitmapUtils.list.get(21).toString());
//                        for (int i = 0; i < BitmapUtils.list.size(); i++) {
//                            if (BitmapUtils.list.get(i).equals("对方账户"))
//                                alipayItems.setPayAccount(BitmapUtils.list.get(i + 1).replaceAll("[^\\u4e00-\\u9fa5\\w\\*]", ""));
//                            if (BitmapUtils.list.get(i).equals("转账备注") || BitmapUtils.list.get(i).equals("收款理由") || BitmapUtils.list.get(i).equals("商品说明"))
//                                alipayItems.setNote(BitmapUtils.list.get(i + 1));
//                            if (BitmapUtils.list.get(i).equals("订单号"))
//                                alipayItems.setDepositNumber(BitmapUtils.list.get(i + 1));
//                            if (BitmapUtils.list.get(i).equals("创建时间"))
//                                alipayItems.setTransferTime(BitmapUtils.list.get(i + 1));
//                        }
                        List alipayitemlist = new ArrayList();
                        alipayItemList = new AlipayItemList();
                        alipayItemList.setAlipayAccount(account);
                        alipayitemlist.add(alipayItems);
                        alipayItemList.setDepositRecords(alipayitemlist);
                        Log.e("alipayitemlist", alipayitemlist.toString());
                        intent = new Intent(Config.BEGININSERTTASK);
                        DemoActivity.this.sendBroadcast(intent);
//                        AccessibilityHelper.performBack(QiangHongBaoService.service);
//                            handler.sendEmptyMessageDelayed(10, 5000);
//                            number++;
//                        }
                    }
                    break;
                case 89:
                    AccessibilityNodeInfo getHongbao = AccessibilityHelper.findNodeInfosByText(nodeInfo, "[红包]");
                    if (getHongbao != null) {
                        clickText("[红包]");
                    } else {
                        BitmapUtils.execShellCmd("input tap 450 500");
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                nodeInfo = QiangHongBaoService.service.getRootInActiveWindow();
//                                clickAll();
//                                handler.sendEmptyMessageDelayed(89, times);
//                            }
//                        }, 5000);
//                        handler.sendEmptyMessageDelayed(89, 5000);
                    }
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            nodeInfo = QiangHongBaoService.service.getRootInActiveWindow();
                            clickAll();
                            handler.sendEmptyMessageDelayed(89, times);
                        }
                    }, 5000);
//                    for(int i = 0 ; i < 2 ;i++){
//                    BitmapUtils.execShellCmd("input tap 450 500");
//                    clickAll();
//                    AccessibilityNodeInfo getHongbao = AccessibilityHelper.findNodeInfosByText(nodeInfo, "[红包]");
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            BitmapUtils.execShellCmd("input tap 450 500");
//                        }
//                    }, 20000);
//                    }
//                    AccessibilityHelper.performClick(getHongbao);
                    break;
                case 90:
                    Intent intentlist = new Intent(Config.GETLIST);
                    DemoActivity.this.sendBroadcast(intentlist);
                    handler.sendEmptyMessageDelayed(90, 10000);
                    break;

                default:
                    break;
            }
        }
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
//        if (NetworkUtil.isNetworkAvailable(DemoActivity.this)) {
//            new AutoUpdateTask(this).execute();
//        }
        toDemo = true;
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.BEGININSERTTASK);
        filter.addAction(Config.GETFINISHTASK);
        filter.addAction(Config.GETSTATE);
        filter.addAction(Config.GETLIST);
        filter.addAction(Config.UPDATESTATE);
        filter.addAction(Config.CHECKIMG);
        filter.addAction(Config.GETIP);
        filter.addAction(Config.UPDATEWITHROW);
        filter.addAction(Config.ACTION_NOTIFY_LISTENER_SERVICE_CONNECT);
        registerReceiver(qhbConnectReceiver, filter);
        QHBApplication.activityStartMain(DemoActivity.this);
    }

    private int getDiff(int begin) {
        if (begin == listitem.size() - 1)
            return begin;
        if (timeInfo2.get(begin).getText().toString().equals(timeInfo2.get(begin + 1).getText().toString()) && billAmount.get(begin).getText().toString().equals(billAmount.get(begin + 1).getText().toString())) {
            getDiff(begin + 1);
        }
        return begin;
    }

    private void clickText(String content) {
        AccessibilityNodeInfo getHongbao = AccessibilityHelper.findNodeInfosByText(QiangHongBaoService.service.getRootInActiveWindow(), content);
        Rect rect = new Rect();
        if (getHongbao != null) {
//            AccessibilityHelper.performClick(getHongbao);
            getHongbao.getBoundsInScreen(rect);
            Log.e("rect", rect.toString());
            BitmapUtils.execShellCmd("input tap " + (rect.left + 50) + " " + (rect.top + 30));
        }
    }

    private void clickitem() {
        AccessibilityNodeInfo outHongbao = AccessibilityHelper.findNodeInfosByText(nodeInfo, "[红包]");
        if (outHongbao == null)
            return;
        clickText("[红包]");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        clickAll();
                    }
                }, 3000);
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        clickitem();
//                    }
//                }, 10000);
            }
        }, 1000);
    }

    private void clickAll() {
        AccessibilityNodeInfo getHongbao = AccessibilityHelper.findNodeInfosByText(QiangHongBaoService.service.getRootInActiveWindow(), "领取红包");
        if (getHongbao == null) {
            times = 20000;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AccessibilityHelper.performBack(QiangHongBaoService.service);
                }
            }, 3000);
            return;
        }
        times = 50000;
        Log.e("开始时间", "领取红包");
        clickText("领取红包");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        AccessibilityNodeInfo open = AccessibilityHelper.findNodeInfosById(nodeInfo, "com.alipay.android.phone.discovery.envelope:id/action_chai");
//                        AccessibilityHelper.performClick(open);
                        BitmapUtils.execShellCmd("input tap 400 850");
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (QiangHongBaoService.service.getRootInActiveWindow() == null) {
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            sendhttp();
                                        }
                                    }, 2000);
                                } else {
                                    sendhttp();
                                }
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
//                                        List<AccessibilityNodeInfo> accesslist = new ArrayList<AccessibilityNodeInfo>();
//                                        BitmapUtils.getNodeInfo(QiangHongBaoService.service.getRootInActiveWindow(), "android.widget.TextView", accesslist);
//                                        List<String> content = new ArrayList<String>();
//                                        for (int i = 0; i < accesslist.size(); i++) {
//                                            if (null != accesslist.get(i).getText() && !"".equals(accesslist.get(i).getText().toString()))
//                                                content.add(accesslist.get(i).getText().toString());
//                                        }
//                                        Log.e("123456", content.toString());
                                        AccessibilityHelper.performBack(QiangHongBaoService.service);
                                        clickAll();
                                    }
                                }, 3000);
                            }
                        }, 10000);
                    }
                }, 3000);
            }
        }, 5000);
    }

    private Uri SMS_INBOX = Uri.parse("content://sms/");

    private void sendhttp() {
        AccessibilityNodeInfo amount = AccessibilityHelper.findNodeInfosById(QiangHongBaoService.service.getRootInActiveWindow(), "com.alipay.android.phone.discovery.envelope:id/coupon_amount");
        AccessibilityNodeInfo remark = AccessibilityHelper.findNodeInfosById(QiangHongBaoService.service.getRootInActiveWindow(), "com.alipay.android.phone.discovery.envelope:id/creator_remark");
        AccessibilityNodeInfo deposit = AccessibilityHelper.findNodeInfosById(QiangHongBaoService.service.getRootInActiveWindow(), "com.alipay.android.phone.discovery.envelope:id/coupon_crowd_no");
        alipayItemList = new AlipayItemList();
        alipayItemList.setAlipayAccount(account);
        aliList = new ArrayList<>();
        //alipayItemList
//        for (int i = 0; i < alilist.size(); i++) {
        AlipayItem alipayItem = new AlipayItem();
//            if (!alilist.get(i).getSenderNickname().equals("代付"))
//                continue;
        alipayItem.setPlatfrom(plaftfrom);
        alipayItem.setWechatName(account);
        //红包编号：201902110206302200000000150036692416
        alipayItem.setAmount(Double.parseDouble(amount.getText().toString()));
        alipayItem.setPayAccount("无");
        alipayItem.setDepositNumber(deposit.getText().toString().split("：")[1]);
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        System.out.println("当前时间：" + sdf.format(d));
        String times = sdf.format(d);
        alipayItem.setTransferTime(times);
        alipayItem.setNote(remark.getText().toString());
        alipayItem.setNickName("无");
        aliList.add(alipayItem);
//        }
        alipayItemList.setDepositRecords(aliList);
        Intent intents = new Intent(Config.BEGININSERTTASK);
        sendBroadcast(intents);
//        handler.sendEmptyMessageDelayed(23, 10000);
    }

    //光大银行
    public void getCEBFromPhone() {
        ContentResolver cr = getContentResolver();
        String[] projection = new String[]{"body"};//"_id", "address", "person",, "date", "type
//        String where = " address = '1066321332' AND date >  "
//                + (System.currentTimeMillis() - 10 * 60 * 1000);
        String where = "";
        alilist = new ArrayList<>();
        Cursor cur = cr.query(SMS_INBOX, projection, "date > " + (System.currentTimeMillis() - 5 * 60 * 60 * 1000) + " and address = '95595'", null, " date desc");
//        Cursor cur = cr.query(SMS_INBOX, projection, "date > " + (System.currentTimeMillis() - 18 * 60 * 60 * 1000) + " and address = '95595'", null, " date desc");
//        Cursor cur = cr.query(SMS_INBOX, projection, " address = '95595'", null, " date desc");
        if (null != cur) {
            while (cur.moveToNext()) {
                String body = cur.getString(cur.getColumnIndex("body"));
                Log.e("body", body);
                if (body.indexOf("支付宝转账") != -1 || body.indexOf("银联入账") != -1) {
                    String time = body.substring(body.indexOf("账户") + 2, body.indexOf("存入"));
                    String amount = body.substring(body.indexOf("存入") + 2, body.indexOf("元"));
                    String banlace = body.substring(body.indexOf("余额") + 2, body.lastIndexOf("元"));
                    Date d = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    System.out.println("当前时间：" + sdf.format(d));
                    String times = sdf.format(d) + " " + time;
                    Alidetail alidetail = new Alidetail();
                    alidetail.setSenderAccount("支付宝");
                    if (body.indexOf("支付宝转账") != -1)
                        alidetail.setSenderNickname("代付");
                    else if (body.indexOf("银联入账") != -1)
                        alidetail.setSenderNickname("银行卡转账");
                    alidetail.setTransferAmount(amount);
                    alidetail.setTransferTime(times);
                    alidetail.setSenderName("0");
                    alidetail.setSenderComment(banlace);
                    alidetail.setId(time + account + amount + banlace);
                    alilist.add(alidetail);
//                Log.e("result", "time:" + sdf.format(d)) + " " + time + " amount:" + amount + " banlace:" + banlace);
                }
//                if(){
//
//                }
            }
        }

        alipayItemList = new AlipayItemList();
        alipayItemList.setAlipayAccount(account);
        aliList = new ArrayList<>();
        //alipayItemList
        for (int i = 0; i < alilist.size(); i++) {
            AlipayItem alipayItem = new AlipayItem();
            if (!(alilist.get(i).getSenderNickname().equals("代付") || alilist.get(i).getSenderNickname().equals("银行卡转账")))
                continue;
            alipayItem.setPlatfrom(plaftfrom);
            alipayItem.setWechatName(account);
            alipayItem.setAmount(Double.parseDouble(alilist.get(i).getTransferAmount()));
            alipayItem.setPayAccount(alilist.get(i).getSenderAccount());
            alipayItem.setDepositNumber(alilist.get(i).getId());
            alipayItem.setTransferTime(alilist.get(i).getTransferTime());
            alipayItem.setNote(alilist.get(i).getSenderNickname());
            alipayItem.setNickName(alilist.get(i).getSenderComment());
            aliList.add(alipayItem);
        }
        alipayItemList.setDepositRecords(aliList);
        Log.e("detail", alipayItemList.toString());
        Intent intents = new Intent(Config.BEGININSERTTASK);
        sendBroadcast(intents);
        handler.sendEmptyMessageDelayed(23, 10000);
    }

    //农信银行
    public void getNX() {
        ContentResolver cr = getContentResolver();
        String[] projection = new String[]{"body"};//"_id", "address", "person",, "date", "type
//        String where = " address = '1066321332' AND date >  "
//                + (System.currentTimeMillis() - 10 * 60 * 1000);
        String where = "";
        alilist = new ArrayList<>();
//        Cursor cur = cr.query(SMS_INBOX, projection, "date > " + (System.currentTimeMillis() - 5 * 60 * 60 * 1000) + " and address = '106900079096518'", null, " date desc");
        Cursor cur = cr.query(SMS_INBOX, projection, "date > " + (System.currentTimeMillis() - 5 * 60 * 60 * 1000) + " and address = '106910096518'", null, " date desc");
//        Cursor cur = cr.query(SMS_INBOX, projection, "date > " + (System.currentTimeMillis() - 5 * 60 * 60 * 1000) + " and address = '106980096518'", null, " date desc");
//        Cursor cur = cr.query(SMS_INBOX, projection, " address = '95580'", null, " date desc");
        if (null != cur) {
            while (cur.moveToNext()) {
                String body = cur.getString(cur.getColumnIndex("body"));
                Log.e("body", body);
                //【湖南农信】您尾号3749的卡于05月17日12时51分转入人民币10.00元，当前余额为10.00元。如有疑问请详询0731-96518。
                if (body.indexOf("转入人民币") != -1) {
                    String month = body.substring(body.indexOf("于") + 1, body.indexOf("月"));
                    String day = body.substring(body.indexOf("月") + 1, body.indexOf("日"));
                    String time = body.substring(body.indexOf("日") + 1, body.indexOf("时"));
                    String minute = body.substring(body.indexOf("时") + 1, body.indexOf("分"));
                    String amount = body.substring(body.indexOf("转入人民币") + 5, body.indexOf("元"));
                    String banlace = body.substring(body.indexOf("余额为") + 3, body.lastIndexOf("元"));
                    String times = "2019-" + month + "-" + day + " " + time + ":" + minute;
                    Alidetail alidetail = new Alidetail();
                    alidetail.setSenderAccount("支付宝");
                    alidetail.setSenderNickname("代付");
                    alidetail.setTransferAmount(amount);
                    alidetail.setTransferTime(times);
                    alidetail.setSenderName("0");
                    alidetail.setSenderComment(banlace);
                    alidetail.setId(time + account + amount + banlace);
                    alilist.add(alidetail);
                }
            }
        }
        Cursor morecur = cr.query(SMS_INBOX, projection, "date > " + (System.currentTimeMillis() - 5 * 60 * 60 * 1000) + " and address = '106900079096518'", null, " date desc");
//        Cursor cur = cr.query(SMS_INBOX, projection, "date > " + (System.currentTimeMillis() - 5 * 60 * 60 * 1000) + " and address = '106980096518'", null, " date desc");
//        Cursor cur = cr.query(SMS_INBOX, projection, " address = '95580'", null, " date desc");
        if (null != morecur) {
            while (morecur.moveToNext()) {
                String body = morecur.getString(morecur.getColumnIndex("body"));
                Log.e("body", body);
                //【湖南农信】您尾号3749的卡于05月17日12时51分转入人民币10.00元，当前余额为10.00元。如有疑问请详询0731-96518。
                if (body.indexOf("转入人民币") != -1) {
                    String month = body.substring(body.indexOf("于") + 1, body.indexOf("月"));
                    String day = body.substring(body.indexOf("月") + 1, body.indexOf("日"));
                    String time = body.substring(body.indexOf("日") + 1, body.indexOf("时"));
                    String minute = body.substring(body.indexOf("时") + 1, body.indexOf("分"));
                    String amount = body.substring(body.indexOf("转入人民币") + 5, body.indexOf("元"));
                    String banlace = body.substring(body.indexOf("余额为") + 3, body.lastIndexOf("元"));
                    String times = "2019-" + month + "-" + day + " " + time + ":" + minute;
                    Alidetail alidetail = new Alidetail();
                    alidetail.setSenderAccount("支付宝");
                    alidetail.setSenderNickname("代付");
                    alidetail.setTransferAmount(amount);
                    alidetail.setTransferTime(times);
                    alidetail.setSenderName("0");
                    alidetail.setSenderComment(banlace);
                    alidetail.setId(time + account + amount + banlace);
                    alilist.add(alidetail);
                }
            }
        }
        alipayItemList = new AlipayItemList();
        alipayItemList.setAlipayAccount(account);
        aliList = new ArrayList<>();
        //alipayItemList
        for (int i = 0; i < alilist.size(); i++) {
            AlipayItem alipayItem = new AlipayItem();
//            if (!alilist.get(i).getSenderNickname().equals("代付"))
//                continue;
            alipayItem.setPlatfrom(plaftfrom);
            alipayItem.setWechatName(account);
            alipayItem.setAmount(Double.parseDouble(alilist.get(i).getTransferAmount()));
            alipayItem.setPayAccount(alilist.get(i).getSenderAccount());
            alipayItem.setDepositNumber(alilist.get(i).getId());
            alipayItem.setTransferTime(alilist.get(i).getTransferTime());
            alipayItem.setNote(alilist.get(i).getSenderNickname());
            alipayItem.setNickName(alilist.get(i).getSenderComment());
            aliList.add(alipayItem);
        }
        alipayItemList.setDepositRecords(aliList);
        Intent intents = new Intent(Config.BEGININSERTTASK);
        sendBroadcast(intents);
        handler.sendEmptyMessageDelayed(25, 10000);
    }

    public void getMoreNx() {
        String[] teles = {"106900079096518", "106910096518", "106980096518", "106920096518"};
        alilist = new ArrayList<>();
        for (int k = 0; k < teles.length; k++) {
            String phone = teles[k];
            ContentResolver cr = getContentResolver();
            String[] projection = new String[]{"body"};//"_id", "address", "person",, "date", "type
            Cursor cur = cr.query(SMS_INBOX, projection, "date > " + (System.currentTimeMillis() - 5 * 60 * 60 * 1000) + " and address = '" + phone + "'", null, " date desc");
//            Cursor cur = cr.query(SMS_INBOX, projection, "date > " + (System.currentTimeMillis() - 24 * 60 * 60 * 1000) + " and address = '" + phone + "'", null, " date desc");
            if (null != cur) {
                while (cur.moveToNext()) {
                    String body = cur.getString(cur.getColumnIndex("body"));
                    Log.e("body", body);
                    //【湖南农信】您尾号3749的卡于05月17日12时51分转入人民币10.00元，当前余额为10.00元。如有疑问请详询0731-96518。
                    if (body.indexOf("转入人民币") != -1) {
                        String month = body.substring(body.indexOf("于") + 1, body.indexOf("月"));
                        String day = body.substring(body.indexOf("月") + 1, body.indexOf("日"));
                        String time = body.substring(body.indexOf("日") + 1, body.indexOf("时"));
                        String minute = body.substring(body.indexOf("时") + 1, body.indexOf("分"));
                        String amount = body.substring(body.indexOf("转入人民币") + 5, body.indexOf("元"));
                        String banlace = body.substring(body.indexOf("余额为") + 3, body.lastIndexOf("元"));
                        String times = "2019-" + month + "-" + day + " " + time + ":" + minute;
                        Alidetail alidetail = new Alidetail();
                        alidetail.setSenderAccount("支付宝");
                        alidetail.setSenderNickname("代付");
                        alidetail.setTransferAmount(amount);
                        alidetail.setTransferTime(times);
                        alidetail.setSenderName("0");
                        alidetail.setSenderComment(banlace);
                        alidetail.setId(time + account + amount + banlace);
                        alilist.add(alidetail);
                    }
                }
            }
            alipayItemList = new AlipayItemList();
            alipayItemList.setAlipayAccount(account);
            aliList = new ArrayList<>();
            //alipayItemList
            for (int i = 0; i < alilist.size(); i++) {
                AlipayItem alipayItem = new AlipayItem();
                alipayItem.setPlatfrom(plaftfrom);
                alipayItem.setWechatName(account);
                alipayItem.setAmount(Double.parseDouble(alilist.get(i).getTransferAmount()));
                alipayItem.setPayAccount(alilist.get(i).getSenderAccount());
                alipayItem.setDepositNumber(alilist.get(i).getId());
                alipayItem.setTransferTime(alilist.get(i).getTransferTime());
                alipayItem.setNote(alilist.get(i).getSenderNickname());
                alipayItem.setNickName(alilist.get(i).getSenderComment());
                aliList.add(alipayItem);
            }

//
        }
        alipayItemList.setDepositRecords(aliList);
        Intent intents = new Intent(Config.BEGININSERTTASK);
        sendBroadcast(intents);
        handler.sendEmptyMessageDelayed(25, 10000);
    }

    //长沙银行
    public void getMoreCs() {
        String[] teles = {"1069800096511"};
        alilist = new ArrayList<>();
        for (int k = 0; k < teles.length; k++) {
            String phone = teles[k];
            ContentResolver cr = getContentResolver();
            String[] projection = new String[]{"body"};//"_id", "address", "person",, "date", "type
            Cursor cur = cr.query(SMS_INBOX, projection, "date > " + (System.currentTimeMillis() - 5 * 60 * 60 * 1000) + " and address = '" + phone + "'", null, " date desc");
//            Cursor cur = cr.query(SMS_INBOX, projection, "date > " + (System.currentTimeMillis() - 24 * 60 * 60 * 1000) + " and address = '" + phone + "'", null, " date desc");
            if (null != cur) {
                while (cur.moveToNext()) {
                    String body = cur.getString(cur.getColumnIndex("body"));
                    Log.e("body", body);
                    //尊敬的客户，您尾号5311的人民币账户，于2019年06月06日14:05:54支付宝付款存入0.10元，余额0.10元。【长沙银行】
                    //尊敬的客户，您尾号5311的人民币账户，于2019年06月06日14:17:43支付宝付款存入1.00元，余额1.10元。【长沙银行】
                    //【湖南农信】您尾号3749的卡于05月17日12时51分转入人民币10.00元，当前余额为10.00元。如有疑问请详询0731-96518。
                    if (body.indexOf("支付宝付款存入") != -1) {
                        String month = body.substring(body.indexOf("年") + 1, body.indexOf("月"));
                        String day = body.substring(body.indexOf("月") + 1, body.indexOf("日"));
                        String time = body.substring(body.indexOf("日") + 1, body.indexOf("支付宝付款"));
//                        String minute = body.substring(body.indexOf("时") + 1, body.indexOf("分"));
                        String amount = body.substring(body.indexOf("支付宝付款存入") + 7, body.indexOf("元"));
                        String banlace = body.substring(body.indexOf("余额") + 2, body.lastIndexOf("元"));
                        String times = "2019-" + month + "-" + day + " " + time;
                        Alidetail alidetail = new Alidetail();
                        alidetail.setSenderAccount("支付宝");
                        alidetail.setSenderNickname("代付");
                        alidetail.setTransferAmount(amount);
                        alidetail.setTransferTime(times);
                        alidetail.setSenderName("0");
                        alidetail.setSenderComment(banlace);
                        alidetail.setId(time + account + amount + banlace);
                        alilist.add(alidetail);
                    }
                }
            }
            alipayItemList = new AlipayItemList();
            alipayItemList.setAlipayAccount(account);
            aliList = new ArrayList<>();
            //alipayItemList
            for (int i = 0; i < alilist.size(); i++) {
                AlipayItem alipayItem = new AlipayItem();
                alipayItem.setPlatfrom(plaftfrom);
                alipayItem.setWechatName(account);
                alipayItem.setAmount(Double.parseDouble(alilist.get(i).getTransferAmount().replace(",", "")));
                alipayItem.setPayAccount(alilist.get(i).getSenderAccount());
                alipayItem.setDepositNumber(alilist.get(i).getId());
                alipayItem.setTransferTime(alilist.get(i).getTransferTime());
                alipayItem.setNote(alilist.get(i).getSenderNickname());
                alipayItem.setNickName(alilist.get(i).getSenderComment());
                aliList.add(alipayItem);
            }

//
        }
        alipayItemList.setDepositRecords(aliList);
        Intent intents = new Intent(Config.BEGININSERTTASK);
        sendBroadcast(intents);
        handler.sendEmptyMessageDelayed(26, 10000);
    }

    //    public void getMORENX() {
//        ContentResolver cr = getContentResolver();
//        String[] projection = new String[]{"body"};//"_id", "address", "person",, "date", "type
////        String where = " address = '1066321332' AND date >  "
////                + (System.currentTimeMillis() - 10 * 60 * 1000);
//        String where = "";
//        alilist = new ArrayList<>();
//        Cursor cur = cr.query(SMS_INBOX, projection, "date > " + (System.currentTimeMillis() - 5 * 60 * 60 * 1000) + " and address = '106900079096518'", null, " date desc");
////        Cursor cur = cr.query(SMS_INBOX, projection, "date > " + (System.currentTimeMillis() - 5 * 60 * 60 * 1000) + " and address = '106910096518'", null, " date desc");
////        Cursor cur = cr.query(SMS_INBOX, projection, "date > " + (System.currentTimeMillis() - 5 * 60 * 60 * 1000) + " and address = '106980096518'", null, " date desc");
////        Cursor cur = cr.query(SMS_INBOX, projection, " address = '95580'", null, " date desc");
//        if (null != cur) {
//            while (cur.moveToNext()) {
//                String body = cur.getString(cur.getColumnIndex("body"));
//                Log.e("body", body);
//                //【湖南农信】您尾号3749的卡于05月17日12时51分转入人民币10.00元，当前余额为10.00元。如有疑问请详询0731-96518。
//                if (body.indexOf("转入人民币") != -1) {
//                    String month = body.substring(body.indexOf("于") + 1, body.indexOf("月"));
//                    String day = body.substring(body.indexOf("月") + 1, body.indexOf("日"));
//                    String time = body.substring(body.indexOf("日") + 1, body.indexOf("时"));
//                    String minute = body.substring(body.indexOf("时") + 1, body.indexOf("分"));
//                    String amount = body.substring(body.indexOf("转入人民币") + 5, body.indexOf("元"));
//                    String banlace = body.substring(body.indexOf("余额为") + 3, body.lastIndexOf("元"));
//                    String times = "2019-" + month + "-" + day + " " + time + ":" + minute;
//                    Alidetail alidetail = new Alidetail();
//                    alidetail.setSenderAccount("支付宝");
//                    alidetail.setSenderNickname("代付");
//                    alidetail.setTransferAmount(amount);
//                    alidetail.setTransferTime(times);
//                    alidetail.setSenderName("0");
//                    alidetail.setSenderComment(banlace);
//                    alidetail.setId(time + account + amount + banlace);
//                    alilist.add(alidetail);
//                }
//            }
//        }
//        alipayItemList = new AlipayItemList();
//        alipayItemList.setAlipayAccount(account);
//        aliList = new ArrayList<>();
//        //alipayItemList
//        for (int i = 0; i < alilist.size(); i++) {
//            AlipayItem alipayItem = new AlipayItem();
////            if (!alilist.get(i).getSenderNickname().equals("代付"))
////                continue;
//            alipayItem.setPlatfrom(plaftfrom);
//            alipayItem.setWechatName(account);
//            alipayItem.setAmount(Double.parseDouble(alilist.get(i).getTransferAmount()));
//            alipayItem.setPayAccount(alilist.get(i).getSenderAccount());
//            alipayItem.setDepositNumber(alilist.get(i).getId());
//            alipayItem.setTransferTime(alilist.get(i).getTransferTime());
//            alipayItem.setNote(alilist.get(i).getSenderNickname());
//            alipayItem.setNickName(alilist.get(i).getSenderComment());
//            aliList.add(alipayItem);
//        }
//        alipayItemList.setDepositRecords(aliList);
//        Intent intents = new Intent(Config.BEGININSERTTASK);
//        sendBroadcast(intents);
////        handler.sendEmptyMessageDelayed(25, 10000);
//    }
    //邮政银行
    public void getPSBCFromPhone() {
        ContentResolver cr = getContentResolver();
        String[] projection = new String[]{"body"};//"_id", "address", "person",, "date", "type
//        String where = " address = '1066321332' AND date >  "
//                + (System.currentTimeMillis() - 10 * 60 * 1000);
        String where = "";
        alilist = new ArrayList<>();
        Cursor cur = cr.query(SMS_INBOX, projection, "date > " + (System.currentTimeMillis() - 5 * 60 * 60 * 1000) + " and address = '95580'", null, " date desc");
//        Cursor cur = cr.query(SMS_INBOX, projection, " address = '95580'", null, " date desc");
        if (null != cur) {
            while (cur.moveToNext()) {
                String body = cur.getString(cur.getColumnIndex("body"));
                Log.e("body", body);
                if (body.indexOf("提现金额") != -1 || body.indexOf("银联入账") != -1) {
                    if (body.indexOf("提现金额") != -1) {
                        String month = body.substring(body.indexOf("年") + 1, body.indexOf("月"));
                        String day = body.substring(body.indexOf("月") + 1, body.indexOf("日"));
                        String time = body.substring(body.indexOf("日") + 1, body.indexOf("您"));
                        String amount = body.substring(body.indexOf("提现金额") + 4, body.indexOf("元"));
                        String banlace = body.substring(body.indexOf("余额") + 2, body.lastIndexOf("元"));
                        String times = "2019-" + month + "-" + day + " " + time;
                        Alidetail alidetail = new Alidetail();
                        alidetail.setSenderAccount("支付宝");
                        alidetail.setSenderNickname("代付");
                        alidetail.setTransferAmount(amount);
                        alidetail.setTransferTime(times);
                        alidetail.setSenderName("0");
                        alidetail.setSenderComment(banlace);
                        alidetail.setId(time + account + amount + banlace);
                        alilist.add(alidetail);
                    } else {
                        String month = body.substring(body.indexOf("年") + 1, body.indexOf("月"));
                        String day = body.substring(body.indexOf("月") + 1, body.indexOf("日"));
                        String time = body.substring(body.indexOf("日") + 1, body.indexOf("您"));
                        String amount = body.substring(body.indexOf("银联入账金额") + 6, body.indexOf("元"));
                        String banlace = body.substring(body.indexOf("余额") + 2, body.lastIndexOf("元"));
                        String times = "2019-" + month + "-" + day + " " + time;
                        Alidetail alidetail = new Alidetail();
                        alidetail.setSenderAccount("支付宝");
                        alidetail.setSenderNickname("代付");
                        alidetail.setTransferAmount(amount);
                        alidetail.setTransferTime(times);
                        alidetail.setSenderName("0");
                        alidetail.setSenderComment(banlace);
                        alidetail.setId(time + account + amount + banlace);
                        alilist.add(alidetail);
                    }
                }
            }
        }
        alipayItemList = new AlipayItemList();
        alipayItemList.setAlipayAccount(account);
        aliList = new ArrayList<>();
        //alipayItemList
        for (int i = 0; i < alilist.size(); i++) {
            AlipayItem alipayItem = new AlipayItem();
            if (!alilist.get(i).getSenderNickname().equals("代付"))
                continue;
            alipayItem.setPlatfrom(plaftfrom);
            alipayItem.setWechatName(account);
            alipayItem.setAmount(Double.parseDouble(alilist.get(i).getTransferAmount()));
            alipayItem.setPayAccount(alilist.get(i).getSenderAccount());
            alipayItem.setDepositNumber(alilist.get(i).getId());
            alipayItem.setTransferTime(alilist.get(i).getTransferTime());
            alipayItem.setNote(alilist.get(i).getSenderNickname());
            alipayItem.setNickName(alilist.get(i).getSenderComment());
            aliList.add(alipayItem);
        }
        alipayItemList.setDepositRecords(aliList);
        Intent intents = new Intent(Config.BEGININSERTTASK);
        sendBroadcast(intents);
        handler.sendEmptyMessageDelayed(24, 10000);
    }

    //建设银行
    public void getJXPhone() {
        ContentResolver cr = getContentResolver();
        String[] projection = new String[]{"body"};//"_id", "address", "person",, "date", "type
//        String where = " address = '1066321332' AND date >  "
//                + (System.currentTimeMillis() - 10 * 60 * 1000);

        String where = "";
        alilist = new ArrayList<>();
        Cursor cur = cr.query(SMS_INBOX, projection, "date > " + (System.currentTimeMillis() - 6 * 60 * 60 * 1000) + " and address = '95533'", null, " date desc");
        if (null != cur) {
            while (cur.moveToNext()) {
                String body = cur.getString(cur.getColumnIndex("body"));

                //贵公司尾号1542的账户6月23日9时38分手机转账收入人民币11.00元，余额31231.70元。对方户名:曹伟。[建设银行]
                if (body.indexOf("收入人民币") != -1) {
                    Log.e("body", body);
                    if(body.indexOf("贵公司尾号")!=-1){
                        String endaccount = body.substring(body.indexOf("尾号") + 2, body.indexOf("尾号") + 6);
                        String banlance = body.substring(body.indexOf("余额") + 2, body.lastIndexOf("元"));
                        String amount = body.substring(body.indexOf("收入人民币") + 5, body.indexOf("元"));
                        String payer = body.substring(body.indexOf("对方户名:") + 5, body.lastIndexOf("。"));
                        String time = body.substring(body.indexOf("账户") + 2, body.indexOf("分")+1);
                        String month = time.substring(0, time.indexOf("月"));
                        String day = time.substring(time.indexOf("月")+1, time.indexOf("日"));
                        String hour = time.substring(time.indexOf("日")+1, time.indexOf("时"));
                        String minute = time.substring(time.indexOf("时")+1, time.indexOf("分"));
                        String times = "2019-" + month + "-" + day + " " + hour + ":" + minute + ":00";
                        String comment = body.substring(body.lastIndexOf("分") + 1, body.lastIndexOf("收入人民币"));
                        Alidetail alidetail = new Alidetail();
                        alidetail.setSenderAccount(payer);
                        alidetail.setSenderNickname(comment);
                        alidetail.setTransferAmount(amount);
                        alidetail.setTransferTime(times);
                        alidetail.setSenderName("0");
                        if (account.length() < 5) {
                            if (getAccount.getText().toString().length() < 5)
                                Toast.makeText(DemoActivity.this, "请重试设置账号", Toast.LENGTH_LONG).show();
                            else
                                account = getAccount.getText().toString();
//                        getAccount.getText().toString();
                        }
                        if (account.indexOf(endaccount) == -1) {
                            continue;
                        }

//                if(alidetail.get)account.substring(account.length() - 4, account.length()) + amount + banlance
                        alidetail.setId(times.replace("-", "").replace(" ", "").replace(":", "") + account.substring(account.length() - 4, account.length()) + amount + banlance);
                        alilist.add(alidetail);
                    }else{
                        String endaccount = body.substring(body.indexOf("尾号") + 2, body.indexOf("尾号") + 6);
                        String banlance = body.substring(body.indexOf("余额") + 2, body.lastIndexOf("元"));
                        String amount = body.substring(body.indexOf("收入人民币") + 5, body.indexOf("元"));
//                    String payer = body.substring(body.indexOf("对方户名:") + 5, body.lastIndexOf("。"));
                        String payer = body.substring(0, body.indexOf("月") - 1);
                        String time = body.substring(body.indexOf("月") - 1, body.indexOf("分") + 1);
                        String month = time.substring(0, time.indexOf("月"));
                        String day = time.substring(time.indexOf("月") + 1, time.indexOf("日"));
                        String hour = time.substring(time.indexOf("日") + 1, time.indexOf("时"));
                        String minute = time.substring(time.indexOf("时") + 1, time.indexOf("分"));
                        String times = "2019-" + month + "-" + day + " " + hour + ":" + minute + ":00";
                        String comment = "转入";
                        Alidetail alidetail = new Alidetail();
                        alidetail.setSenderAccount(payer);
                        alidetail.setSenderNickname(comment);
                        alidetail.setTransferAmount(amount);
                        alidetail.setTransferTime(times);
                        alidetail.setSenderName("0");
                        if (account.length() < 5) {
                            if (getAccount.getText().toString().length() < 5)
                                Toast.makeText(DemoActivity.this, "请重试设置账号", Toast.LENGTH_LONG).show();
                            else
                                account = getAccount.getText().toString();
//                        getAccount.getText().toString();
                        }
                        if (account.indexOf(endaccount) == -1) {
                            continue;
                        }

//                if(alidetail.get)account.substring(account.length() - 4, account.length()) + amount + banlance
                        alidetail.setId(times.replace("-", "").replace(" ", "").replace(":", "") + account.substring(account.length() - 4, account.length()) + amount + banlance);
                        alilist.add(alidetail);
                    }
                }
            }
        }
        alipayItemList = new AlipayItemList();
        alipayItemList.setAlipayAccount(account);
        aliList = new ArrayList<>();
        //alipayItemList
        for (int i = 0; i < alilist.size(); i++) {
            AlipayItem alipayItem = new AlipayItem();
//            if (!alilist.get(i).getSenderNickname().equals("代付"))
//                continue;
            alipayItem.setPlatfrom(plaftfrom);
            alipayItem.setWechatName(account);
            alipayItem.setAmount(Double.parseDouble(alilist.get(i).getTransferAmount()));
            alipayItem.setPayAccount(alilist.get(i).getSenderAccount());
            alipayItem.setDepositNumber(alilist.get(i).getId());
            alipayItem.setTransferTime(alilist.get(i).getTransferTime());
            alipayItem.setNote(alilist.get(i).getSenderNickname());
            alipayItem.setNickName(alilist.get(i).getSenderComment());
            aliList.add(alipayItem);
        }
        alipayItemList.setDepositRecords(aliList);
        Intent intents = new Intent(Config.BEGININSERTTASK);
        sendBroadcast(intents);
        handler.sendEmptyMessageDelayed(27, 10000);
    }

    //农业银行
    public void getAbFromPhone() {
        ContentResolver cr = getContentResolver();
        String[] projection = new String[]{"body"};//"_id", "address", "person",, "date", "type
//        String where = " address = '1066321332' AND date >  "
//                + (System.currentTimeMillis() - 10 * 60 * 1000);
        String where = "";
        alilist = new ArrayList<>();
        Cursor cur = cr.query(SMS_INBOX, projection, "date > " + (System.currentTimeMillis() - 5 * 60 * 60 * 1000) + " and address = '95599'", null, " date desc");
//        Cursor cur = cr.query(SMS_INBOX, projection, " address = '95599'", null, " date desc");
        if (null != cur) {
            while (cur.moveToNext()) {
                String body = cur.getString(cur.getColumnIndex("body"));

                Log.e("body", body);

//                if (money.equals("")) {
//                    if (body.indexOf("余额") != -1) {
//                        money = body.substring(body.indexOf("余额") + 2, body.length() - 1).toString();
////                        play(money);
//                        Intent intenteas = new Intent(Config.ACTION_NOTIFY_LISTENER_SERVICE_CONNECT);
//                        DemoActivity.this.sendBroadcast(intenteas);
//                    }【中国农业银行】尊敬的客户：曹伟于06月23日08:27向贵公司的农行账户（尾号1165）完成转入交易，金额为11.00，余额为13.96。
//                }
                if (body.indexOf("转入交易") != -1) {
                    String endaccount = body.substring(body.indexOf("尾号") + 2, body.indexOf("尾号") + 6);

//                    String message = "【中国农业银行】尊敬的客户：曹伟于06月23日08:27向贵公司的农行账户（尾号1165）完成转入交易，金额为11.00，余额为13.96。";
                    String amount = body.substring(body.indexOf("金额为") + 3, body.lastIndexOf("，"));
                    String banlance = body.substring(body.indexOf("余额为") + 3, body.lastIndexOf("。"));
                    String time = body.substring(body.indexOf("于") + 1, body.indexOf("向"));
                    String month = (time.substring(0, time.indexOf("月")).length() < 2) ?
                            "0" + time.substring(0, time.indexOf("月")) : time.substring(0, time.indexOf("月"));
                    String day = (time.substring(time.indexOf("月") + 1, time.indexOf("日")).length() < 2) ?
                            "0" + time.substring(time.indexOf("月") + 1, time.indexOf("日")) : time.substring(time.indexOf("月") + 1, time.indexOf("日"));
                    String hours = time.substring(time.indexOf("日") + 1, time.length());
                    String times = "2019-" + month + "-" + day + " " + hours;
                    String comment = body.substring(body.indexOf("完成") + 2, body.indexOf("交易"));
                    String payer = body.substring(body.indexOf("：") + 1, body.indexOf("于"));
                    Alidetail alidetail = new Alidetail();
                    alidetail.setSenderAccount(payer);
                    alidetail.setSenderNickname(comment);
                    alidetail.setTransferAmount(amount);
                    alidetail.setTransferTime(times);
                    alidetail.setSenderName("0");
//                    if (platfrom.equals("MSGREEN"))
//                        alidetail.setPlafrom("pms");
//                    else
//                        alidetail.setPlafrom("oa");
//                if(alidetail.get)
                    if (comment.indexOf("支付宝") != -1)
                        alidetail.setSenderComment("ALIPAYDIRECT");
                    else if (comment.indexOf("转存") != -1)
                        alidetail.setSenderComment("NETPAY");
                    else if (comment.indexOf("现存") != -1)
                        alidetail.setSenderComment("ATM");
                    else if (comment.replace(" ", "").indexOf("转账到X") != -1) {
                        alidetail.setSenderComment("WECHATDIRECT");
                    } else
                        alidetail.setSenderComment("OTHERS");
                    Log.e("String", "account:"+account +" endaccount:"+endaccount);
                    if (account.length() < 5) {
                        if (getAccount.getText().toString().length() < 5)
                            Toast.makeText(DemoActivity.this, "请重试设置账号", Toast.LENGTH_LONG).show();
                        else
                            account = getAccount.getText().toString();
//                        getAccount.getText().toString();
                    }

                    if (account.indexOf(endaccount) == -1) {
                        continue;
                    }
                    alidetail.setId(times.replace("-", "").replace(" ", "").replace(":", "") + account.substring(account.length() - 4, account.length()) + amount + banlance);
                    alilist.add(alidetail);
                }
                if ((body.indexOf("交易人民币") != -1 && body.indexOf("交易人民币-") == -1) || body.indexOf("银联入账") != -1) {
                    if (body.indexOf("银联入账") != -1) {
                        //您尾号1270账户04月22日00:15完成银联入账交易人民币1898.10，余额5812.58。
                        String amount = body.substring(body.indexOf("人民币") + 3, body.indexOf("，"));
                        String time = "";
                        String payer = "";
                    } else {
                        String endaccount = body.substring(body.indexOf("尾号") + 2, body.indexOf("尾号") + 6);
                        //【中国农业银行】熊星星于03月06日17:19向您尾号1370账户完成转存交易人民币11.00，余额11.12。 银联扫码转账于03月06日17:13向您尾号1370账户完成代付交易人民币11.00，余额11.12。
                        String amount = body.substring(body.indexOf("人民币") + 3, body.indexOf("，"));
                        String time = "";
                        String payer = "";
                        if (body.indexOf("于") != -1) {
                            time = body.substring(body.indexOf("于") + 1, body.indexOf("向"));
                            payer = body.substring(body.indexOf("】") + 1, body.indexOf("于"));
                        } else {
                            time = body.substring(body.indexOf("账户") + 2, body.indexOf("完成"));
                            payer = body.substring(body.indexOf("完成") + 2, body.indexOf("交易"));
                        }
                        String comment = body.substring(body.indexOf("完成") + 2, body.indexOf("交易"));
                        String month = (time.substring(0, time.indexOf("月")).length() < 2) ?
                                "0" + time.substring(0, time.indexOf("月")) : time.substring(0, time.indexOf("月"));
                        String day = (time.substring(time.indexOf("月") + 1, time.indexOf("日")).length() < 2) ?
                                "0" + time.substring(time.indexOf("月") + 1, time.indexOf("日")) : time.substring(time.indexOf("月") + 1, time.indexOf("日"));
                        String hours = time.substring(time.indexOf("日") + 1, time.length());
                        String times = "2019-" + month + "-" + day + " " + hours;
                        Alidetail alidetail = new Alidetail();
                        alidetail.setSenderAccount(payer);
                        alidetail.setSenderNickname(comment);
                        alidetail.setTransferAmount(amount);
                        alidetail.setTransferTime(times);
                        alidetail.setSenderName("0");
//                    if (platfrom.equals("MSGREEN"))
//                        alidetail.setPlafrom("pms");
//                    else
//                        alidetail.setPlafrom("oa");
//                if(alidetail.get)
                        if (comment.indexOf("支付宝") != -1)
                            alidetail.setSenderComment("ALIPAYDIRECT");
                        else if (comment.indexOf("转存") != -1)
                            alidetail.setSenderComment("NETPAY");
                        else if (comment.indexOf("现存") != -1)
                            alidetail.setSenderComment("ATM");
                        else if (comment.replace(" ", "").indexOf("转账到X") != -1) {
                            alidetail.setSenderComment("WECHATDIRECT");
                        } else
                            alidetail.setSenderComment("OTHERS");
                        Log.e("String", "account:"+account +" endaccount:"+endaccount);
                        if (account.length() < 5) {
                            if (getAccount.getText().toString().length() < 5)
                                Toast.makeText(DemoActivity.this, "请重试设置账号", Toast.LENGTH_LONG).show();
                            else
                                account = getAccount.getText().toString();
//                        getAccount.getText().toString();
                        }

                        if (account.indexOf(endaccount) == -1) {
                            continue;
                        }
                        alidetail.setId(times.replace("-", "").replace(" ", "").replace(":", "") + account + amount);
                        alilist.add(alidetail);
                    }

//                Log.e("body", body + " amount:"+amount+ " payer:"+payer+ " time:"+time+ " comment:"+comment);
                }
            }
        }
        alipayItemList = new AlipayItemList();
        alipayItemList.setAlipayAccount(account);
        aliList = new ArrayList<>();
        //alipayItemList
        for (int i = 0; i < alilist.size(); i++) {
            AlipayItem alipayItem = new AlipayItem();
//            if (!alilist.get(i).getSenderNickname().equals("代付"))
//                continue;
            alipayItem.setPlatfrom(plaftfrom);
            alipayItem.setWechatName(account);
            alipayItem.setAmount(Double.parseDouble(alilist.get(i).getTransferAmount()));
            alipayItem.setPayAccount(alilist.get(i).getSenderAccount());
            alipayItem.setDepositNumber(alilist.get(i).getId());
            alipayItem.setTransferTime(alilist.get(i).getTransferTime());
            alipayItem.setNote(alilist.get(i).getSenderNickname());
            alipayItem.setNickName(alilist.get(i).getSenderComment());
            aliList.add(alipayItem);
        }
        alipayItemList.setDepositRecords(aliList);
        Intent intents = new Intent(Config.BEGININSERTTASK);
        sendBroadcast(intents);
        handler.sendEmptyMessageDelayed(30, 10000);
    }

    private BroadcastReceiver qhbConnectReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (isFinishing()) {
                return;
            }
            String action = intent.getAction();

            Log.e("MainActivity", "receive-->" + action);
            if (Config.BEGININSERTTASK.equals(action)) {
//                url = "http://47.52.77.234:8081/";
                url = "http://47.91.212.208:8081/";
                String urltest = url + "Depositlist";
//                if (innernet)
////                    urltest = "http://192.168.1.74:8081/Depositinner";
//                    urltest = "http://192.168.1.253:8081/Depositinner";
//                String urltest = "http://10.10.45.147:8081/deposit";
                JSONObject paramstest = new JSONObject();
                try {
                    paramstest.put("item", JsonUtil.objectToString(alipayItemList));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String param = new Gson().toJson(alipayItemList);
                Log.e("String", JsonUtil.objectToString(param));
                StringRequest request = new StringRequest(Request.Method.POST, urltest, new Response.Listener<String>() {
                    public void onResponse(String response) {
                        Log.e("response", response);
                        Return retrun = (Return) JsonUtil.stringToObject(response, Return.class);
                        try {
                            Toast.makeText(DemoActivity.this, ((LinkedTreeMap) retrun.getObject()).get("msg") + "", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(DemoActivity.this, "成功", Toast.LENGTH_LONG).show();
                        }
                        if (retrun.getCode() == 200)
                            clickIndex--;
                    }

                }, new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DemoActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        Log.e("error:", error + "");
                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try {
                            return param == null ? null : param.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", param, "utf-8");
                            return null;
                        }
                    }

                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                        String responseText = "";
                        try {
                            responseText = new String(response.data, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        return Response.success(responseText, HttpHeaderParser.parseCacheHeaders(response));
                    }
                };

                request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 2, 1.0f));
                QHBApplication.getHttpQueues().add(request);
            } else if (Config.GETIP.equals(action)) {
//               htt http://47.75.144.122:8089/wechat?name=&belongbank=&ip=SF010&realname&type=&state=&page=1&size=500&wechatId=1
                final StringRequest request = new StringRequest(Request.Method.GET, url + "wechat?name=&belongbank=&ip=" + ipcontent.getText().toString() + "&realname&type=&state=&page=1&size=500&wechatId=1", new Response.Listener<String>() {
                    public void onResponse(String response) {
                        try {
                            Log.e("response", response);
                            Return retrun = (Return) JsonUtil.stringToObject(response, Return.class);
                            if (retrun.getCode() == 200) {
                                if (retrun.getObject() == null)
                                    return;
                                if (retrun.getObject().getClass().getName().equals("java.lang.String")) {
                                    startRecrive = false;
                                    Toast.makeText(DemoActivity.this, retrun.getObject().toString(), Toast.LENGTH_LONG).show();
                                } else {
                                    startRecrive = true;
                                    aliDetailitem = new AliDetailitem();
                                    aliDetailitem.setAmount(((LinkedTreeMap) ((ArrayList) retrun.getObject()).get(0)).get("amount") + "");
                                    aliDetailitem.setNickName(((LinkedTreeMap) ((ArrayList) retrun.getObject()).get(0)).get("nickName") + "");
                                    aliDetailitem.setState(((LinkedTreeMap) ((ArrayList) retrun.getObject()).get(0)).get("state") + "");
                                    aliDetailitem.setWechatName(((LinkedTreeMap) ((ArrayList) retrun.getObject()).get(0)).get("wechatName") + "");
                                    aliDetailitem.setDayamount(((LinkedTreeMap) ((ArrayList) retrun.getObject()).get(0)).get("dayamount") + "");
                                    aliDetailitem.setDaylimit(((LinkedTreeMap) ((ArrayList) retrun.getObject()).get(0)).get("daylimit") + "");
                                    aliDetailitem.setType(((LinkedTreeMap) ((ArrayList) retrun.getObject()).get(0)).get("type") + "");
                                    belongbankCard = ((LinkedTreeMap) ((ArrayList) retrun.getObject()).get(0)).get("belongbankCard") + "";
                                    plaftfrom = ((LinkedTreeMap) ((ArrayList) retrun.getObject()).get(0)).get("plaftfrom") + "";
                                    limitString = ((LinkedTreeMap) ((ArrayList) retrun.getObject()).get(0)).get("autowithrow") + "";
                                    itemmoney = ((LinkedTreeMap) ((ArrayList) retrun.getObject()).get(0)).get("amount") + "";
                                    withrow.setChecked(Double.parseDouble(limitString) > 0);
                                    limit.setText(limitString);
                                    Log.e("aliDetailitem:", aliDetailitem.toString());
                                    BitmapUtils.setKey(DemoActivity.this, "account", aliDetailitem.getWechatName());
                                    getAccount.setText(aliDetailitem.getWechatName());
                                    ipcontent.setText(((LinkedTreeMap) ((ArrayList) retrun.getObject()).get(0)).get("ip") + "");
                                    wechatState.setText(aliDetailitem.getState());
                                    toWithrow = Double.parseDouble(limitString) > 0;
                                    wechatAmount.setText(aliDetailitem.getAmount());
                                    dayAmount.setText(aliDetailitem.getDayamount());
//                                    wechatName.setText(aliDetailitem.getWechatName());
                                }
                            }
                        } catch (Exception e) {
                        }
                    }
                }, new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DemoActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        Log.e("error:", error + "");
                    }
                }) {
                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                        String responseText = "";
                        try {
                            responseText = new String(response.data, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        return Response.success(responseText, HttpHeaderParser.parseCacheHeaders(response));
                    }
                };

                request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 2, 1.0f));
                QHBApplication.getHttpQueues().add(request);
            } else if (Config.UPDATEWITHROW.equals(action)) {
                String withrow = url + "outputlist";
                JSONObject paramstest = new JSONObject();
                Withrow newwithorw = new Withrow();
                newwithorw.setFromBankType("1");
                newwithorw.setFromBank(aliDetailitem.getWechatName());
                newwithorw.setDestBankType("0");
                newwithorw.setDestBank(belongbankCard);
                newwithorw.setPayfee(withrowFee);
                newwithorw.setAmount(withorwAmount + "");
                newwithorw.setCreateUser("auto");
                newwithorw.setDepositNumber(wihtrowDeposit);
                try {
                    paramstest.put("item", JsonUtil.objectToString(newwithorw));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String param = new Gson().toJson(newwithorw);
                Log.e("String", JsonUtil.objectToString(param));
                final StringRequest request = new StringRequest(Request.Method.POST, withrow, new Response.Listener<String>() {
                    public void onResponse(String response) {
                        Return retrun = (Return) JsonUtil.stringToObject(response, Return.class);
                        Toast.makeText(DemoActivity.this, retrun.getMsg(), Toast.LENGTH_SHORT).show();
                        if (retrun.getCode() == 200 || retrun.getCode() == 400 || retrun.getCode() == 401)
                            clickIndex--;
                    }
                }, new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error:", error + "");
                    }
                }) {
                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                        String responseText = "";
                        try {
                            responseText = new String(response.data, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        return Response.success(responseText, HttpHeaderParser.parseCacheHeaders(response));
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try {
                            return param == null ? null : param.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", param, "utf-8");
                            return null;
                        }
                    }
                };
                request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 2, 1.0f));
                QHBApplication.getHttpQueues().add(request);
            } else if (Config.ACTION_NOTIFY_LISTENER_SERVICE_CONNECT.equals(action)) {
                NotifyHelper.soundlimt(DemoActivity.this);
            } else if (Config.GETSTATE.equals(action)) {
                account = getAccount.getText().toString();
                Log.e("url", url + "wechatstate?wechatName=" + account);
                final StringRequest request = new StringRequest(Request.Method.GET, url + "wechatstate?wechatName=" + account, new Response.Listener<String>() {
                    public void onResponse(String response) {
                        try {
                            Log.e("response", response);
                            Return retrun = (Return) JsonUtil.stringToObject(response, Return.class);
                            if (retrun.getCode() == 200) {
                                if (retrun.getObject() == null)
                                    return;
                                if (retrun.getObject().getClass().getName().equals("java.lang.String")) {
                                    startRecrive = false;
                                    Toast.makeText(DemoActivity.this, retrun.getObject().toString(), Toast.LENGTH_LONG).show();
                                } else {
                                    BitmapUtils.setKey(DemoActivity.this, "account", account);
                                    startRecrive = true;
                                    aliDetailitem = new AliDetailitem();
                                    aliDetailitem.setAmount(((LinkedTreeMap) retrun.getObject()).get("amount") + "");
                                    aliDetailitem.setNickName(((LinkedTreeMap) retrun.getObject()).get("nickName") + "");
                                    aliDetailitem.setState(((LinkedTreeMap) retrun.getObject()).get("state") + "");
                                    aliDetailitem.setWechatName(((LinkedTreeMap) retrun.getObject()).get("wechatName") + "");
                                    aliDetailitem.setDayamount(((LinkedTreeMap) retrun.getObject()).get("dayamount") + "");
                                    aliDetailitem.setDaylimit(((LinkedTreeMap) retrun.getObject()).get("daylimit") + "");
                                    aliDetailitem.setType(((LinkedTreeMap) retrun.getObject()).get("type") + "");
                                    belongbankCard = ((LinkedTreeMap) retrun.getObject()).get("belongbankCard") + "";
                                    plaftfrom = ((LinkedTreeMap) retrun.getObject()).get("plaftfrom") + "";
                                    limitString = ((LinkedTreeMap) retrun.getObject()).get("autowithrow") + "";
                                    itemmoney = ((LinkedTreeMap) retrun.getObject()).get("amount") + "";
                                    withrow.setChecked(Double.parseDouble(limitString) > 0);
                                    limit.setText(limitString);
                                    ipcontent.setText(((LinkedTreeMap) retrun.getObject()).get("ip") + "");
                                    Log.e("aliDetailitem:", aliDetailitem.toString());
                                    wechatState.setText(aliDetailitem.getState());
                                    toWithrow = Double.parseDouble(limitString) > 0;
                                    wechatAmount.setText(aliDetailitem.getAmount());
                                    dayAmount.setText(aliDetailitem.getDayamount());
//                                    wechatName.setText(aliDetailitem.getWechatName());
                                }
                            }
                        } catch (Exception e) {
                        }
                    }
                }, new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DemoActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        Log.e("error:", error + "");
                    }
                }) {
                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                        String responseText = "";
                        try {
                            responseText = new String(response.data, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        return Response.success(responseText, HttpHeaderParser.parseCacheHeaders(response));
                    }
                };

                request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 2, 1.0f));
                QHBApplication.getHttpQueues().add(request);


            } else if (Config.GETLIST.equals(action)) {
                account = getAccount.getText().toString();
//                String newurl = url + "deposit?wechatName=" + account + "&page=1&size=5&state=&beginTime=0&endTime=0&beginTranstime=0&endTranstime=0&beginExcuteTime=0&endExcuteTime=0&depositNumber=&billNo=";
                String newurl = url + "cachedeposit?wechatName=" + account;
                if ("1".equals(QHBApplication.platfrom))
                    newurl = url + "deposit?wechatName=" + account + "&page=1&size=5&state=&beginTime=0&endTime=0&beginTranstime=0&endTranstime=0&beginExcuteTime=0&endExcuteTime=0&depositNumber=&billNo=";
                Log.e("url", newurl);
                final StringRequest request = new StringRequest(Request.Method.GET, newurl, new Response.Listener<String>() {
                    public void onResponse(String response) {
                        try {
                            Log.e("response", response);
                            Return retrun = (Return) JsonUtil.stringToObject(response, Return.class);
                            if (retrun.getCode() == 200) {
                                List list = (List) retrun.getObject();
                                aliList.clear();
                                for (int i = 0; i < list.size(); i++) {
                                    AlipayItem aliDetailitems = new AlipayItem();
                                    aliDetailitems.setWechatName(((LinkedTreeMap) list.get(i)).get("wechatName") + "");
                                    aliDetailitems.setAmount(Double.parseDouble(((LinkedTreeMap) list.get(i)).get("amount") + ""));
                                    aliDetailitems.setTransferTime(((LinkedTreeMap) list.get(i)).get("transferTime") + "");
                                    aliDetailitems.setDepositNumber(((LinkedTreeMap) list.get(i)).get("depositNumber") + "");
                                    if (((LinkedTreeMap) list.get(i)).get("nickName") != null)
                                        aliDetailitems.setNickName(((LinkedTreeMap) list.get(i)).get("nickName") + "");
                                    aliDetailitems.setNote(((LinkedTreeMap) list.get(i)).get("note") + "");
                                    aliDetailitems.setPayAccount(((LinkedTreeMap) list.get(i)).get("payAccount") + "");
                                    aliList.add(aliDetailitems);
                                }
//                            aliList = arrayList;
                                itemAdatpter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                        }

                    }
                }, new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
//                        handler.sendEmptyMessageDelayed(2, 2000);
                        Toast.makeText(DemoActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        Log.e("error:", error + "");
                    }
                }) {
                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                        String responseText = "";
                        try {
                            responseText = new String(response.data, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        return Response.success(responseText, HttpHeaderParser.parseCacheHeaders(response));
                    }
                };

                request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 2, 1.0f));
                QHBApplication.getHttpQueues().add(request);
            } else if (Config.UPDATESTATE.equals(action)) {
                String changurl = url + "wechatstate?wechatName=" + account + "&state=DISABLED";
                if (bankState.equals("DISABLED"))
                    changurl = url + "wechatstate?wechatName=" + account + "&state=NORMAL";
                if (clearString)
                    changurl = url + "wechatstate?wechatName=" + account + "&dayamount=0";
                Log.e("changurl", changurl);
                final StringRequest request = new StringRequest(Request.Method.GET, changurl, new Response.Listener<String>() {
                    public void onResponse(String response) {
                        clearString = false;
                        Log.e("response", response);
                        Return retrun = (Return) JsonUtil.stringToObject(response, Return.class);
                        try {
                            if (retrun.getCode() == 200) {
                                if (retrun.getObject().getClass().getName().equals("java.lang.String")) {
                                    startRecrive = false;
                                    Toast.makeText(DemoActivity.this, retrun.getObject().toString(), Toast.LENGTH_LONG).show();
                                } else {
                                    startRecrive = true;
                                    aliDetailitem = new AliDetailitem();
                                    aliDetailitem.setAmount(((LinkedTreeMap) retrun.getObject()).get("amount") + "");
                                    aliDetailitem.setNickName(((LinkedTreeMap) retrun.getObject()).get("nickName") + "");
                                    aliDetailitem.setState(((LinkedTreeMap) retrun.getObject()).get("state") + "");
                                    aliDetailitem.setWechatName(((LinkedTreeMap) retrun.getObject()).get("wechatName") + "");
                                    aliDetailitem.setDayamount(((LinkedTreeMap) retrun.getObject()).get("dayamount") + "");
                                    aliDetailitem.setDaylimit(((LinkedTreeMap) retrun.getObject()).get("daylimit") + "");
                                    aliDetailitem.setType(((LinkedTreeMap) retrun.getObject()).get("type") + "");
                                    plaftfrom = ((LinkedTreeMap) retrun.getObject()).get("plaftfrom") + "";
                                    ipcontent.setText(((LinkedTreeMap) retrun.getObject()).get("ip") + "");
                                    Log.e("aliDetailitem:", aliDetailitem.toString());
                                    wechatState.setText(aliDetailitem.getState());
                                    wechatAmount.setText(aliDetailitem.getAmount());
                                    dayAmount.setText(aliDetailitem.getDayamount());
//                                    wechatName.setText(aliDetailitem.getWechatName());
                                }
                            }
                        } catch (Exception e) {

                        }

                    }
                }, new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        clearString = false;
                        Log.e("error:", error + "");
                    }
                }) {
                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                        String responseText = "";
                        try {
                            responseText = new String(response.data, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        return Response.success(responseText, HttpHeaderParser.parseCacheHeaders(response));
                    }
                };

                request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 2, 1.0f));
                QHBApplication.getHttpQueues().add(request);
            } else if (Config.CHECKIMG.equals(action)) {
                String imgurl = url + "wechatitem?name=" + account + "&amount=0&nickName=&ip=&type=id&state=&qrurl=default&page=1&size=500&note=1";
                final StringRequest request = new StringRequest(Request.Method.GET, imgurl, new Response.Listener<String>() {
                    public void onResponse(String response) {
                        Log.e("response", response);
                        Return retrun = (Return) JsonUtil.stringToObject(response, Return.class);
                        Toast.makeText(DemoActivity.this, retrun.getTotalnumber() + "", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        clearString = false;
                        Log.e("error:", error + "");
                    }
                });
                request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 2, 1.0f));
                QHBApplication.getHttpQueues().add(request);
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        findbyId();
        init();

        aliList = new ArrayList<AlipayItem>();
        itemAdatpter = new ItemAdatpter(DemoActivity.this, R.layout.activity_item, aliList);
        listview.setAdapter(itemAdatpter);
        notifyHelper = new NotifyHelper();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Config.GETSTATE);
                DemoActivity.this.sendBroadcast(intent);
            }
        }, 5000);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }


    private void findbyId() {
        start = (Button) findViewById(R.id.start);
        restop = (Button) findViewById(R.id.restop);
        wechat = (Button) findViewById(R.id.wechat);
        sure = (Button) findViewById(R.id.sure);
        receive = (Button) findViewById(R.id.receive);
        bankreceive = (Button) findViewById(R.id.bankreceive);
        jsreceive = (Button) findViewById(R.id.jsreceive);
        checkimg = (Button) findViewById(R.id.checkimg);
        stop = (Button) findViewById(R.id.stop);
        clear = (Button) findViewById(R.id.clear);
        edit = (Button) findViewById(R.id.edit);
        getState = (Button) findViewById(R.id.getState);
        updateState = (Button) findViewById(R.id.updateState);
        root = (Button) findViewById(R.id.root);
//        makesrc = (Button) findViewById(R.id.makesrc);
        sure = (Button) findViewById(R.id.sure);
        wechatAmount = (TextView) findViewById(R.id.wechatAmount);
        wechatState = (TextView) findViewById(R.id.wechatState);
        version = (TextView) findViewById(R.id.version);
        dayAmount = (TextView) findViewById(R.id.dayAmount);
        getAccount = (EditText) findViewById(R.id.account);
        limit = (EditText) findViewById(R.id.limit);
        roottest = (EditText) findViewById(R.id.roottest);
        listview = (ListView) findViewById(R.id.listview);
        withrow = (CheckBox) findViewById(R.id.withrow);
        qrCheck = (CheckBox) findViewById(R.id.updown);
        orderby = (CheckBox) findViewById(R.id.orderby);
        newcompany = (TextView) findViewById(R.id.newcompany);
        innernetwork = (CheckBox) findViewById(R.id.innernetwork);
        makesure = (Button) findViewById(R.id.makesure);
        ip = (Button) findViewById(R.id.ip);
        bulu = (Button) findViewById(R.id.bulu);
        playbulu = (Button) findViewById(R.id.playbulu);
        CEB = (Button) findViewById(R.id.CEB);
        PSBC = (Button) findViewById(R.id.PSBC);
        hongbao = (Button) findViewById(R.id.hongbao);
        ipcontent = (EditText) findViewById(R.id.ipcontent);
        NX = (Button) findViewById(R.id.NX);
        CS = (Button) findViewById(R.id.CS);
    }

    private void init() {
        start.setOnClickListener(this);
        wechat.setOnClickListener(this);
        bankreceive.setOnClickListener(this);
        receive.setOnClickListener(this);
        stop.setOnClickListener(this);
        getState.setOnClickListener(this);
        sure.setOnClickListener(this);
        edit.setOnClickListener(this);
        root.setOnClickListener(this);
        clear.setOnClickListener(this);
        ip.setOnClickListener(this);
        bulu.setOnClickListener(this);
        restop.setOnClickListener(this);
        updateState.setOnClickListener(this);
        playbulu.setOnClickListener(this);
        checkimg.setOnClickListener(this);
        CEB.setOnClickListener(this);
        PSBC.setOnClickListener(this);
        hongbao.setOnClickListener(this);
        orderby.setOnClickListener(this);
        NX.setOnClickListener(this);
        CS.setOnClickListener(this);
        makesure.setOnClickListener(this);
        jsreceive.setOnClickListener(this);
        if (!"1".equals(QHBApplication.platfrom)) {
            qrCheck.setVisibility(View.VISIBLE);
            newcompany.setVisibility(View.VISIBLE);
        }

//        makesrc.setOnClickListener(this);
        account = "";
        bankState = "";
//        updown = qrCheck.
        if (!BitmapUtils.getKey(DemoActivity.this, "account").equals(""))
            getAccount.setText(BitmapUtils.getKey(DemoActivity.this, "account"));
//        int versions = 0;
        Context context = DemoActivity.this;
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            versions = info.versionCode;

        } catch (Exception e) {
        }
        version.setText("版本号:" + versions + "");
        innernetwork.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                innernet = isChecked;
                if (innernet)
                    BitmapUtils.setKey(DemoActivity.this, "innernet", "1");
                else
                    BitmapUtils.setKey(DemoActivity.this, "innernet", "0");
            }
        });
        qrCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updown = isChecked;
                if (updown) {
                    BitmapUtils.setKey(DemoActivity.this, "updown", "1");
                    url = "http://47.52.77.234:8081/";
                } else {
                    url = "http://api.wandapay88.com/";
                    BitmapUtils.setKey(DemoActivity.this, "updown", "0");
                }

            }
        });
        orderby.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                allcan = isChecked;
                if (allcan)
                    BitmapUtils.setKey(DemoActivity.this, "allcan", "1");
                else
                    BitmapUtils.setKey(DemoActivity.this, "allcan", "0");
            }
        });


        innernet = "1".equals(BitmapUtils.getKey(DemoActivity.this, "innernet"));
        updown = "1".equals(BitmapUtils.getKey(DemoActivity.this, "updown"));
        allcan = "1".equals(BitmapUtils.getKey(DemoActivity.this, "allcan"));
        innernetwork.setChecked(innernet);
        qrCheck.setChecked(updown);
        orderby.setChecked(allcan);
        if ("1".equals(QHBApplication.platfrom))
//            url = "http://zhifukaui111.info:8081/";
            url = "http://47.75.33.197:8081/";
        else if (updown)
            url = "http://47.52.77.234:8081/";
        else
            url = "http://api.wandapay88.com/";
//            url = "http://zhifudemo.com:8081/";
//            url = "http://103.205.121.9:8081/";
//        innernet = innernetwork.isChecked();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.receive:
                if (!aliDetailitem.getType().equals("1")) {
                    Toast.makeText(DemoActivity.this, "只能启动支付宝收录", Toast.LENGTH_LONG).show();
                } else if (QiangHongBaoService.isRunning() && startRecrive) {
                    Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.eg.android.AlipayGphone");
                    QiangHongBaoService.service.requestWebScripts(true);
                    startActivity(LaunchIntent);
                    handler.sendEmptyMessageDelayed(8, 3000);
                } else {
                    Toast.makeText(DemoActivity.this, "服务未启动或账号填错", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.makesure:
                account = getAccount.getText().toString();
                BitmapUtils.setKey(DemoActivity.this, "account", account);
                Toast.makeText(DemoActivity.this, "账号设置成功:" + account, Toast.LENGTH_LONG).show();
                getAccount.setEnabled(false);
                ipcontent.setEnabled(false);
                break;
            case R.id.start:
                try {
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivity(intent);
                    Toast.makeText(DemoActivity.this, R.string.tips, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.stop:
            case R.id.restop:
                String lime = null;
                lime.toString();
                break;
            case R.id.edit:
                getAccount.setEnabled(true);
                ipcontent.setEnabled(true);
                break;
            case R.id.getState:
                getAccount.setEnabled(false);
                ipcontent.setEnabled(false);

                Intent intent = new Intent(Config.GETSTATE);
                DemoActivity.this.sendBroadcast(intent);
                break;
            case R.id.root:
                BitmapUtils.execShellCmd("input tap 700 800");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BitmapUtils.execShellCmd("input text root");
                    }
                }, 1000);
                break;
            case R.id.ip:
                //GETIP
                getAccount.setEnabled(false);
                ipcontent.setEnabled(false);
                Intent getIP = new Intent(Config.GETIP);
                DemoActivity.this.sendBroadcast(getIP);
                break;
            case R.id.sure:
                break;

            case R.id.updateState:
                if (aliDetailitem == null) {
                    Toast.makeText(DemoActivity.this, "找不到账号", Toast.LENGTH_LONG).show();
                    return;
                }
                bankState = aliDetailitem.getState();
                Intent update = new Intent(Config.UPDATESTATE);
                DemoActivity.this.sendBroadcast(update);
                break;
            case R.id.roottest:
                break;
            case R.id.clear:
                clearString = true;
                Intent clearamount = new Intent(Config.UPDATESTATE);
                DemoActivity.this.sendBroadcast(clearamount);
                break;
            case R.id.checkimg:
//                Toast.makeText(DemoActivity.this, "测试图片", Toast.LENGTH_LONG).show();
                Intent checkimg = new Intent(Config.CHECKIMG);
                DemoActivity.this.sendBroadcast(checkimg);
                break;
            case R.id.wechat:
                if (!aliDetailitem.getType().equals("2")) {
                    Toast.makeText(DemoActivity.this, "只能启动微信收录", Toast.LENGTH_LONG).show();
                } else if (QiangHongBaoService.isRunning() && startRecrive) {
                    Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
                    QiangHongBaoService.service.requestWebScripts(true);
                    startActivity(LaunchIntent);
                    handler.sendEmptyMessageDelayed(21, 3000);
                } else {
                    Toast.makeText(DemoActivity.this, "服务未启动或账号填错", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.bankreceive:
//                if (!aliDetailitem.getType().equals("0")) {
//                    Toast.makeText(DemoActivity.this, "只能启动银行卡收录", Toast.LENGTH_LONG).show();
//                } else if (QiangHongBaoService.isRunning() && startRecrive) {
                handler.sendEmptyMessageDelayed(30, 1000);
//                } else {
//                    Toast.makeText(DemoActivity.this, "服务未启动或账号填错", Toast.LENGTH_LONG).show();
//                }
                break;
            case R.id.bulu:
                if (account.equals("")) {
                    Toast.makeText(DemoActivity.this, "账号错误", Toast.LENGTH_LONG).show();
                } else if (QiangHongBaoService.isRunning() && startRecrive) {
                    Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.eg.android.AlipayGphone");
                    QiangHongBaoService.service.requestWebScripts(true);
                    startActivity(LaunchIntent);
                    handler.sendEmptyMessageDelayed(11, 3000);
                } else {
                    Toast.makeText(DemoActivity.this, "服务未启动或账号填错", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.playbulu:
                if (account.equals("")) {
                    Toast.makeText(DemoActivity.this, "账号错误", Toast.LENGTH_LONG).show();
                } else if (QiangHongBaoService.isRunning() && startRecrive) {
                    Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.eg.android.AlipayGphone");
                    QiangHongBaoService.service.requestWebScripts(true);
                    startActivity(LaunchIntent);
                    handler.sendEmptyMessageDelayed(40, 3000);
                } else {
                    Toast.makeText(DemoActivity.this, "服务未启动或账号填错", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.CEB:
//                Toast.makeText(DemoActivity.this, "光大银行", Toast.LENGTH_LONG).show();
                if (!aliDetailitem.getType().equals("0")) {
                    Toast.makeText(DemoActivity.this, "只能启动银行卡收录", Toast.LENGTH_LONG).show();
                } else if (QiangHongBaoService.isRunning() && startRecrive) {
                    handler.sendEmptyMessageDelayed(23, 1000);
                } else {
                    Toast.makeText(DemoActivity.this, "服务未启动或账号填错", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.PSBC:
                if (!aliDetailitem.getType().equals("0")) {
                    Toast.makeText(DemoActivity.this, "只能启动银行卡收录", Toast.LENGTH_LONG).show();
                } else if (QiangHongBaoService.isRunning() && startRecrive) {
                    handler.sendEmptyMessageDelayed(24, 1000);
                } else {
                    Toast.makeText(DemoActivity.this, "服务未启动或账号填错", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.NX:
                handler.sendEmptyMessageDelayed(25, 1000);
                break;
            case R.id.CS:
                handler.sendEmptyMessageDelayed(26, 1000);
                break;
            case R.id.jsreceive:
                handler.sendEmptyMessageDelayed(27, 1000);
                break;
//            case R.id.makesrc:
//                Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.eg.android.AlipayGphone");
//                QiangHongBaoService.service.requestWebScripts(true);
//                startActivity(LaunchIntent);
//                handler.sendEmptyMessageDelayed(31, 5000);
//                break;
            case R.id.hongbao:
                if (!aliDetailitem.getType().equals("1")) {
                    Toast.makeText(DemoActivity.this, "只能启动支付宝收录", Toast.LENGTH_LONG).show();
                } else if (QiangHongBaoService.isRunning() && startRecrive) {
                    Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.eg.android.AlipayGphone");
                    QiangHongBaoService.service.requestWebScripts(true);
                    startActivity(LaunchIntent);
                    handler.sendEmptyMessageDelayed(89, 3000);
                } else {
                    Toast.makeText(DemoActivity.this, "服务未启动或账号填错", Toast.LENGTH_LONG).show();
                }
                break;

            default:
                break;
        }
    }

    public void getAccount(String account) {

    }

    private String getFormateDay(Long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date(date));
        return time;
    }

    public String getTimeByCalendar(String daytext) {
        if (daytext.equals("今天")) {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);//获取年份
            int month = cal.get(Calendar.MONTH);//获取月份
            int day = cal.get(Calendar.DATE);//获取日
            return year + "-" + ((month + 1) < 10 ?
                    ("0" + (month + 1)) : (month + 1)) + "-" + (day < 10 ?
                    ("0" + day) : day);
        } else if (daytext.equals("昨天")) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(new Date().getTime() - 24 * 60 * 60 * 1000));
            int year = cal.get(Calendar.YEAR);//获取年份
            int month = cal.get(Calendar.MONTH);//获取月份
            int day = cal.get(Calendar.DATE);//获取日
            return year + "-" + ((month + 1) < 10 ?
                    ("0" + (month + 1)) : (month + 1)) + "-" + (day < 10 ?
                    ("0" + day) : day);
        } else {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);//获取年份
            return (year + 1900) + "-" + daytext;
        }

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Demo Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    class AlipayItemList {
        private String alipayAccount;
        private List<AlipayItem> depositRecords;

        public String getAlipayAccount() {
            return alipayAccount;
        }

        public void setAlipayAccount(String alipayAccount) {
            this.alipayAccount = alipayAccount;
        }

        public List<AlipayItem> getDepositRecords() {
            return depositRecords;
        }

        public void setDepositRecords(List<AlipayItem> depositRecords) {
            this.depositRecords = depositRecords;
        }

        public AlipayItemList() {
        }

    }

    class Withrow {
        private String fromBankType;
        private String fromBank;
        private String destBankType;
        private String destBank;
        private String payfee;
        private String amount;
        private String createUser;
        private String depositNumber;

        public String getFromBankType() {
            return fromBankType;
        }

        public void setFromBankType(String fromBankType) {
            this.fromBankType = fromBankType;
        }

        public String getFromBank() {
            return fromBank;
        }

        public void setFromBank(String fromBank) {
            this.fromBank = fromBank;
        }

        public String getDestBankType() {
            return destBankType;
        }

        public void setDestBankType(String destBankType) {
            this.destBankType = destBankType;
        }

        public String getDestBank() {
            return destBank;
        }

        public void setDestBank(String destBank) {
            this.destBank = destBank;
        }

        public String getPayfee() {
            return payfee;
        }

        public void setPayfee(String payfee) {
            this.payfee = payfee;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCreateUser() {
            return createUser;
        }

        public void setCreateUser(String createUser) {
            this.createUser = createUser;
        }

        public String getDepositNumber() {
            return depositNumber;
        }

        public void setDepositNumber(String depositNumber) {
            this.depositNumber = depositNumber;
        }

        public Withrow() {
        }
    }
}
