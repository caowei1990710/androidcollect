package com.codeboy.qianghongbao;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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
import com.codeboy.qianghongbao.demoActivity.DemoActivity;
import com.codeboy.qianghongbao.model.Alidetail;
import com.codeboy.qianghongbao.model.Detail;
import com.codeboy.qianghongbao.model.InfoItem;
import com.codeboy.qianghongbao.model.Infolist;
import com.codeboy.qianghongbao.model.Item;
import com.codeboy.qianghongbao.model.NewReturn;
import com.codeboy.qianghongbao.model.Record;
import com.codeboy.qianghongbao.model.Return;
import com.codeboy.qianghongbao.model.Syncmoney;
import com.codeboy.qianghongbao.util.AccessibilityHelper;
import com.codeboy.qianghongbao.util.AlipayAdapter;
import com.codeboy.qianghongbao.util.AutoUpdateTask;
import com.codeboy.qianghongbao.util.BitmapUtils;
import com.codeboy.qianghongbao.util.DatabaseHelper;
import com.codeboy.qianghongbao.util.JsonUtil;
import com.codeboy.qianghongbao.util.NetworkUtil;
import com.codeboy.qianghongbao.util.NotifyHelper;
import com.codeboy.qianghongbao.util.WebScoketeRequ;
import com.codeboy.qianghongbao.widget.Alipaylist;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;


import static com.codeboy.qianghongbao.util.AccessibilityHelper.findNodeInfosByClassName;
import static com.codeboy.qianghongbao.util.AccessibilityHelper.findNodeInfosById;
import static com.codeboy.qianghongbao.util.AccessibilityHelper.findNodeInfosByText;
import static com.codeboy.qianghongbao.util.WebScoketeRequ.webSocket;

/**
 * Created by snsoft on 28/3/2017.
 */
public class alipyActivity extends Activity {

    private Button begain, noti, webchat, insert, selects, update, ailipy, qr, change, stop;
    public static TextView textstate, nowmoney, textbankcard, textplat, version;
    private AccessibilityNodeInfo nodeInfo, node, listview, amount, name, weixin, qianbao, my, detail, realName, webchatmy, titlenodeinfo;
    private List<AccessibilityNodeInfo> listitem, timeInfo, timeInfo1, amountInfo, nameInfo;
    private String money = "0.00", newSelect = "";
    ;
    public static Handler handler;
    private int Delay = 2000;
    private NotifyHelper notifyHelper;
    public String s;
    public static boolean wechatnote;
    private Timer timer;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private AlipayAdapter detailAdapter;
    private Record record;
    private List<Alidetail> list;
    public AppCompatSpinner network;
    public static WebView webview;
    public static int number = 0, index = -2;
    private Bundle arguments;
    public static String token;
    List<Alidetail> alilist;
    public static float credit;
    private Alipaylist alipaylist;//Apr 8, 2017 1:04:00 PM
    private Syncmoney syncmoney;
    private ArrayList<String> platlist;
    private ArrayList<String> namelist;
    private String devicestring;
    public static String billName = "", billAfterName = "", billAmount = "", billAfterAmount = "", timeInfo2 = "", timeAfterInfo2 = "";
    public static Long begintime, endtime;
    public static boolean startsmooth;
    public static int acount_text = 300, account_times = 1;
    public WebScoketeRequ websocket;
    public static Alidetail alidetail;
    private static boolean flag = true;
    public static List<Item> itemlist;
    public static String bankcard = "x820056@163.com", platfrom, limitString, depart;
    public static int state = 1;//1:正常，2:禁用
    //    public static String loginName = "autoapily", password = "abcd1234", operatorName = "autoapily";
    //    public static String loginName = "admin", password = "test12345", operatorName = "admin";
    public List<InfoItem> infolist;
    public String httpurl = "http://www.autopay8.me";
    //    public String httpurl = "http://192.168.11.128:8080";
    public static String pmshttp = "http://papi-pacnet.pms8.me:8220/http/pss/getBankCards?";//获取卡额度接口
    public static String pmsupdate = "http://papi-pacnet.pms8.me:8220/http/pss/setBankCardState?";//更新卡
    public static String oahttp = "https://server3.oav2.me/api/getNetpay.do?";
    public static String oaupdatehttp = "https://server3.oav2.me/api/updateNetpayStatus.do";
    //    public static String innerNeturl = "http://10.10.10.202:8081";
    public static String innerNeturl = "http://192.168.0.57:8081";
    public static String ipeakNeturl = "http://10.10.10.202:8081";
    public static String anxNeturl = "http://anxeus.autopay8.me:8080";
    public static String hashNeturl = "http://hashtech.autopay8.me:8080";

    //    public static String oahttp = "http://depositor88.87998.net/api/getNetpay.do?";
//    public static String oaupdatehttp = "http://depositor88.87998.net/api/updateNetpayStatus.do";
    public CheckBox statechange;
    private boolean ispms;
    private EditText limit;
    private Button sure, reflush;


    private class UIHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.e("nodeInfo:", nodeInfo + "");
            if (QiangHongBaoService.service != null)
                nodeInfo = QiangHongBaoService.service.getRootInActiveWindow();//com.alipay.mobile.bill.list:id/bill_list_view
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    my = findNodeInfosById(nodeInfo, "com.alipay.android.phone.wealth.home:id/tab_description");
//                    my = AccessibilityHelper.findNodeInfosByIds(nodeInfo, "com.alipay.android.phone.wealth.home:id/container").get(1);
                    notifyHelper.click(handler, my, 1000);
                    handler.sendEmptyMessageDelayed(11, 3000);
                    break;
                case 1:
//                    try {
                    Intent intentes;
                    if (("yes").equals(newSelect))
                        intentes = new Intent(Config.NEWBEGINTASK);
                    else
                        intentes = new Intent(Config.BEGINTASK);
//                    Intent intentes = new Intent(Config.BEGINTASK);
                    alipyActivity.this.sendBroadcast(intentes);
                    alilist = new ArrayList();
                    index = -2;
                    alidetail = null;
                    alipaylist = new Alipaylist();
                    amount = findNodeInfosByText(nodeInfo, "Transactions");
                    if (amount == null)
                        amount = findNodeInfosByText(nodeInfo, "账单");
                    if (amount != null)
                        notifyHelper.click(handler, amount.getParent(), 500);
                    try {
                        List<AccessibilityNodeInfo> list = AccessibilityHelper.findNodeInfosByTexts(nodeInfo, "CNY");
//                        qianbao = AccessibilityHelper.findNodeInfosByText(nodeInfo, "CNY");
                        if (list.size() == 0)
                            list = AccessibilityHelper.findNodeInfosByTexts(nodeInfo, "元");
                        if (list.size() > 0) {
                            qianbao = list.get(list.size() - 1);
                            Toast.makeText(alipyActivity.this, qianbao.getText().toString(), Toast.LENGTH_LONG).show();
                            if (qianbao != null) {
                                money = qianbao.getText().toString().replace(",", "").replace(" CNY", "");
                                Log.e("money", money);
                                if (Double.parseDouble(money) > Double.parseDouble(limitString)) {
                                    Intent intenteas = new Intent(Config.ACTION_NOTIFY_LISTENER_SERVICE_CONNECT);
                                    alipyActivity.this.sendBroadcast(intenteas);
                                } else {
                                    if (NotifyHelper.player != null)
                                        NotifyHelper.player.stop();
                                    NotifyHelper.startlimit = false;
                                }
                            }
                        }
                    } catch (Exception e) {

                    }
                    break;
                case 2:
//                    try {
                    number++;
                    Log.e("number", number + "");
                    if (number > 10) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                AccessibilityHelper.performBack(QiangHongBaoService.service);
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        AccessibilityHelper.performBack(QiangHongBaoService.service);
                                        number = 1;
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.eg.android.AlipayGphone");
                                                startActivity(LaunchIntent);
                                                handler.sendEmptyMessageDelayed(0, 1000);
                                                if (flag && !"正常".equals(textstate.getText().toString())) {
                                                    Intent intentes = new Intent(Config.BEGINUPSATE);
                                                    alipyActivity.this.sendBroadcast(intentes);
                                                }
                                            }
                                        }, 5000);
                                    }
                                }, 2000);
                            }
                        }, 2000);
                    } else {
                        listitem = AccessibilityHelper.findNodeInfosByIds(nodeInfo, "com.alipay.mobile.bill.list:id/listItem");//com.alipay.mobile.bill.detail:id/birdnestContainer
                        timeInfo = AccessibilityHelper.findNodeInfosByIds(nodeInfo, "com.alipay.mobile.bill.list:id/timeInfo2");
                        timeInfo1 = AccessibilityHelper.findNodeInfosByIds(nodeInfo, "com.alipay.mobile.bill.list:id/timeInfo1");
                        amountInfo = AccessibilityHelper.findNodeInfosByIds(nodeInfo, "com.alipay.mobile.bill.list:id/billAmount");
                        nameInfo = AccessibilityHelper.findNodeInfosByIds(nodeInfo, "com.alipay.mobile.bill.list:id/billName");

                        if (amountInfo == null) {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    AccessibilityHelper.performBack(QiangHongBaoService.service);
                                    handler.sendEmptyMessageDelayed(1, 3000);
                                }
                            }, 2000);
                            return;
                        }
                        int length = 5;
                        if (amountInfo.size() < 6) {
                            length = amountInfo.size();
                        }
                        for (int i = 0; i < length; i++) {
                            String amount = amountInfo.get(i).getText().toString().replace(",", "");
                            Log.e("amount", amount);
//                            if (!timeInfo1.get(i).getText().toString().equals("今天")) {
//                                //往上收录
//                                index = i;
//                                break;
//                            }
                            if (amountInfo.get(i).getText().toString().indexOf("+") == -1) {
                                continue;
                            }
                            amount = amount.substring(amount.indexOf("+") + 1, amount.length());
                            Log.e("err", nameInfo.get(i).getText().toString() + " " + billName + " " + billAmount + " " + amount + " " + timeInfo2 + " " + timeInfo.get(i).getText().toString());
                            if (infolist == null) {
                                infolist = new ArrayList<InfoItem>();
                                InfoItem infoItem = new InfoItem();
                                infoItem.setPayerNickname(billAfterName);
                                infoItem.setTransferTime(timeAfterInfo2);
                                infoItem.setTransferAmount(billAfterAmount);
                                infolist.add(infoItem);
//                                infolist.add(new)
                            }
                            for (int j = 0; j < infolist.size(); j++) {
                                if (infolist.get(j).getPayerNickname() != null &&
//                                        !infolist.get(j).getPayerNickname().equals("未知") && infolist.get(j).getPayerNickname().indexOf("*") == -1)
                                        !infolist.get(j).getPayerNickname().equals("未知"))
                                    billName = infolist.get(j).getPayerNickname();
                                else
                                    billName = "-" + nameInfo.get(i).getText().toString();
                                billAmount = infolist.get(j).getTransferAmount();
                                if (billAmount.equals(""))
                                    billAmount = "0";
                                timeInfo2 = infolist.get(j).getTransferTime();
                                if ((nameInfo.get(i).getText().toString().replaceAll("[^\\u4e00-\\u9fa5\\w]", "").indexOf(billName) != -1 && Float.parseFloat(billAmount) == (Float.parseFloat(amount)) && timeInfo2.indexOf(timeInfo.get(i).getText().toString()) != -1)) {
                                    index = i;
                                    break;
                                }
                            }
                            if (index != -2) {
                                break;
                            }
                        }
                        if (index == -2) {
                            index = 1;
                        }
                        Log.e("index", index + "");
                        for (int i = index - 1; i > -1; i--) {
                            if (i == 0 && amountInfo.get(i).getText().toString().indexOf("+") == -1) {
                                index = 0;
                            }
                            if (amountInfo.get(i).getText().toString().indexOf("+") != -1) {
                                index = i + 1;
                                break;
                            }
                        }
                        if (index > 0) {
//                            if (amountInfo.get(index - 1).getText().toString().indexOf("+") != -1) {
//                                index -= 1;
//                            }

                            notifyHelper.click(handler, listitem.get(index - 1), 500);
                            handler.sendEmptyMessageDelayed(3, 8000);
                        } else {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    AccessibilityHelper.performBack(QiangHongBaoService.service);
                                    handler.sendEmptyMessageDelayed(1, 3000);
                                }
                            }, 2000);
                        }
                    }
                    break;
                case 3:
//                    try {
                    listview = findNodeInfosById(nodeInfo, "com.alipay.mobile.bill.detail:id/birdnestContainer");
                    if (listview == null) {
                        nodeInfo = QiangHongBaoService.service.getRootInActiveWindow();
                        listview = nodeInfo.getChild(2).getChild(0);
                        if (listview.getChildCount() == 0) {
                            Toast.makeText(alipyActivity.this, "内容获取失败", Toast.LENGTH_LONG).show();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    AccessibilityHelper.performBack(QiangHongBaoService.service);
                                    alipyActivity.handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            AccessibilityHelper.performBack(QiangHongBaoService.service);
                                            handler.sendEmptyMessageDelayed(1, 3000);
                                        }
                                    }, 1000);


                                }
                            }, 1000);
                            return;
                        }
                        BitmapUtils.list = new ArrayList<>();
                        for (int i = 0; i < listview.getChild(0).getChildCount(); i++) {
                            BitmapUtils.list.add(listview.getChild(0).getChild(i).getContentDescription().toString());
                        }
                        Log.e("BitmapUtils.list", BitmapUtils.list.toString());
                        alidetail = new Alidetail();
                        alidetail.setSenderName(BitmapUtils.list.get(0).replaceAll("[^\\u4e00-\\u9fa5\\w]", ""));
//                        notifyHelper.click(handler,listview.getChild(0),500);
                    } else {
//                    System.out.println(webView.toString());

                        BitmapUtils.list = new ArrayList<String>();
                        BitmapUtils.getDetail(listview);
                        Log.e("BitmapUtils.list", BitmapUtils.list.toString());
                        if (BitmapUtils.list.size() < 10 || BitmapUtils.list.get(1).indexOf("+") == -1) {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    AccessibilityHelper.performBack(QiangHongBaoService.service);
                                    alipyActivity.handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            AccessibilityHelper.performBack(QiangHongBaoService.service);
                                            handler.sendEmptyMessageDelayed(1, 3000);
                                        }
                                    }, 1000);

                                }
                            }, 1000);
                            return;
                        }
                    }
                    if (alidetail != null) {
                        for (int i = 0; i < BitmapUtils.list.size(); i++) {
                            if (BitmapUtils.list.get(i).matches("^\\d+$") && BitmapUtils.list.get(i).length() > 25) {
                                alidetail.setId(BitmapUtils.list.get(i));
                            }
                            if (BitmapUtils.list.get(i).matches("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}")) {
                                alidetail.setTransferTime(BitmapUtils.list.get(i) + ":00");
                            }
                        }

                        alidetail.setSenderNickname(BitmapUtils.list.get(0).replaceAll("[^\\u4e00-\\u9fa5\\w]", ""));
//                        if (BitmapUtils.list.get(0).indexOf('*') != -1) {
//                            alidetail.setSenderNickname("*" + BitmapUtils.list.get(0).replaceAll("[^\\u4e00-\\u9fa5\\w]", ""));
//                        } else {
                        alidetail.setSenderNickname(BitmapUtils.list.get(0).replaceAll("[^\\u4e00-\\u9fa5\\w]", ""));
//                        }
//                        if (BitmapUtils.list.get(0).matches("^[-\\u4e00-\\u9fa5\\w\\,\\.]+$")) {
//                            alidetail.setSenderNickname(BitmapUtils.list.get(0));
//                        } else {
//                            alidetail.setSenderNickname("未知");
//                        }
                        if (BitmapUtils.list.get(BitmapUtils.list.size() - 7).equals("对方账户")) {
                            alidetail.setSenderAccount((BitmapUtils.list.get(BitmapUtils.list.size() - 6).substring(BitmapUtils.list.get(BitmapUtils.list.size() - 6).lastIndexOf(" ") + 1, BitmapUtils.list.get(BitmapUtils.list.size() - 6).length())).replace(",", ""));
                            if (BitmapUtils.list.get(7).equals("转账备注"))
                                alidetail.setSenderComment(BitmapUtils.list.get(8));
                            else
                                alidetail.setSenderComment(BitmapUtils.list.get(6));
                        } else {
//                            alidetail.setSenderAccount((BitmapUtils.list.get(BitmapUtils.list.size() - 6).substring(BitmapUtils.list.get(BitmapUtils.list.size() - 6).indexOf(" ") + 1, BitmapUtils.list.get(BitmapUtils.list.size() - 6).length())).replace(",", ""));
                            alidetail.setSenderAccount("(无)");
                            alidetail.setSenderComment(BitmapUtils.list.get(4));
                        }

                        alidetail.setBalance(money);
                        if (platfrom.equals("MSGREEN"))
                            alidetail.setPlafrom("pms");
                        else
                            alidetail.setPlafrom("oa");
//                        alidetail.setId(BitmapUtils.list.get(10));
                        alidetail.setTransferAmount(BitmapUtils.list.get(1).substring(BitmapUtils.list.get(1).indexOf("+") + 1, BitmapUtils.list.get(1).length()).replace(",", ""));
                        alilist.add(alidetail);
                        alipaylist.setAlipayAccount(bankcard);
                        alipaylist.setDepositRecords(alilist);

                        Intent intent = new Intent(Config.BEGININSERT);
                        alipyActivity.this.sendBroadcast(intent);
                        Log.e("payer", BitmapUtils.payer + "");
                        Log.e("list", BitmapUtils.list + "");
                    } else {
                        detail = listview.getChild(0).getChild(0).getChild(0).getChild(0);
                        notifyHelper.click(handler, detail, 2000);
                        handler.sendEmptyMessageDelayed(4, 5000);
                    }

//                    } catch (Exception e) {
//                        Log.e("Exception", e.toString());
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                AccessibilityHelper.performBack(QiangHongBaoService.service);
//                                alipyActivity.handler.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        AccessibilityHelper.performBack(QiangHongBaoService.service);
//                                        handler.sendEmptyMessageDelayed(1, 2000);
//                                    }
//                                }, 1000);
//
//                            }
//                        }, 1000);
//                        return;
//                    }

                    break;
                case 4:
                    alidetail = new Alidetail();
                    //com.alipay.mobile.ui:id/title_bar_title
                    titlenodeinfo = findNodeInfosById(nodeInfo, "com.alipay.mobile.ui:id/title_bar_title");
                    String title = "账单详情";
                    if (titlenodeinfo != null)
                        title = titlenodeinfo.getText().toString();
                    if (!title.equals("账单详情")) {
                        try {
                            realName = findNodeInfosById(nodeInfo, "com.alipay.mobile.transferapp:id/tf_receiveNameTextView");
                            if (realName != null) {
                                String realnick = realName.getText().toString();
                                Log.e("realName", realName.getText().toString());
                                alidetail.setSenderName(realnick.substring(realnick.indexOf("（") + 1, realnick.indexOf("）")));
                            } else {
                                alidetail.setSenderName("未实名");
                            }
                        } catch (Exception e) {
                            alidetail.setSenderName("未实名");
                        }

//                    alidetail.setSenderNickname(realnick.substring(0, realnick.indexOf("（")));
                        AccessibilityHelper.performBack(QiangHongBaoService.service);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                AccessibilityHelper.performBack(QiangHongBaoService.service);
                                handler.sendEmptyMessageDelayed(3, 1000);
                            }
                        }, 1000);
                    } else {
                        handler.sendEmptyMessageDelayed(3, 1000);
//                        try {
//                            alidetail.setSenderName("*" + BitmapUtils.list.get(0).replaceAll("[^\\u4e00-\\u9fa5\\w]", ""));
//                        } catch (Exception e) {
                        alidetail.setSenderName("未实名");
//                        }

                    }
//                    if(realName == null){
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {

//                            }
//                        },3000);
//                    }
//                    weixin = findNodeInfosById(nodeInfo, "com.alipay.mobile.bill.list:id/bill_list_view");
//                    weixin.performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
                    break;
                case 5:
                    timeInfo2 = findNodeInfosById(nodeInfo, "com.alipay.mobile.bill.list:id/timeInfo2").getText().toString();
                    billAmount = findNodeInfosById(nodeInfo, "com.alipay.mobile.bill.list:id/billAmount").getText().toString();
                    billName = findNodeInfosById(nodeInfo, "com.alipay.mobile.bill.list:id/billName").getText().toString();
                    break;
                case 6:
                    notifyHelper.click(handler, AccessibilityHelper.findNodeInfosByTexts(nodeInfo, "提现").get(1), 500);
                    Log.e("提现", AccessibilityHelper.findNodeInfosByTexts(nodeInfo, "提现").get(1).toString());
                    handler.sendEmptyMessageDelayed(7, 2000);
                    break;
                case 7:
                    AccessibilityNodeInfo transfer = AccessibilityHelper.findNodeInfosById(nodeInfo, "com.alipay.mobile.ui:id/content");
                    arguments = new Bundle();
                    arguments.putCharSequence(
                            AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                            "1");
                    transfer.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                    handler.sendEmptyMessageDelayed(8, 2000);
                    break;
                case 8:
                    AccessibilityNodeInfo confirm_btn = AccessibilityHelper.findNodeInfosById(nodeInfo, "com.alipay.android.phone.wealth.banlance:id/confirm_btn");
                    Log.e("确定", confirm_btn.toString());
                    notifyHelper.click(handler, confirm_btn, 500);
                    handler.sendEmptyMessageDelayed(9, 5000);
                    break;
                case 9:
                    AccessibilityNodeInfo mini_spwd_rl_1 = AccessibilityHelper.findNodeInfosById(nodeInfo, "com.alipay.android.phone.safepaybase:id/mini_spwd_rl_1");
                    Log.e("按键", mini_spwd_rl_1.toString());

                    for (int i = 0; i < 10; i++) {
                        notifyHelper.click(handler, mini_spwd_rl_1, 1000);
                    }
                    arguments = new Bundle();
                    arguments.putCharSequence(
                            AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                            "1");
                    mini_spwd_rl_1.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                    break;
                case 10:
                    AccessibilityNodeInfo web_my = AccessibilityHelper.findNodeInfosById(nodeInfo, "com.tencent.mm:id/bue");
                    notifyHelper.click(handler, web_my, 500);
                    handler.sendEmptyMessageDelayed(11, 1000);
                    break;
                case 11:
//                    AccessibilityNodeInfo web_qianbao = AccessibilityHelper.findNodeInfosById(nodeInfo, "com.tencent.mm:id/buez");
//                    notifyHelper.click(handler,web_my,500);
                    AccessibilityNodeInfo useraccount = AccessibilityHelper.findNodeInfosById(nodeInfo, "com.alipay.android.phone.wealth.home:id/user_account");
                    if (useraccount == null) {
                        handler.sendEmptyMessageDelayed(1, 1000);
                    } else if (bankcard.indexOf("c") != -1) {
                        if (bankcard.equals(useraccount.getText().toString()))
                            handler.sendEmptyMessageDelayed(1, 1000);
                        else {
                            Toast.makeText(alipyActivity.this, "account error", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    } else {
                        String before = useraccount.getText().toString().substring(0, 3);
                        String after = useraccount.getText().toString().substring(7, 11);
                        if (bankcard.indexOf(before) != -1 && bankcard.indexOf(after) != -1) {
                            Toast.makeText(alipyActivity.this, "bankcard:" + bankcard + " before:" + before + " after:" + after, Toast.LENGTH_LONG).show();
                            handler.sendEmptyMessageDelayed(1, 1000);
                        } else {
                            Toast.makeText(alipyActivity.this, "account error", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
//                    com.tencent.mm:id/buz
                    break;
                case 50:
                    textstate.setText("禁用");
                    break;
                case 98:
                    list.clear();
                    list.addAll(databaseHelper.querylist(alipyActivity.this, "alipay"));

                    detailAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alipy);
        newSelect = getIntent().getStringExtra("newSelect");
        findbyId();
        platlist = new ArrayList<String>();
        platlist.add("pacnet");
        platlist.add("globe");
        platlist.add("wtt");
        platlist.add("pccw");
        namelist = new ArrayList<String>();
        namelist.add("pacnet");
        namelist.add("globe");
        namelist.add("wtt");
        namelist.add("pccw");
        MyAdapter myAdapter = new alipyActivity.MyAdapter(namelist, alipyActivity.this);
        Context context = alipyActivity.this;
        network.setAdapter(myAdapter);
        PackageManager manager = context.getPackageManager();
        network.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pmshttp = "http://papi-" + platlist.get(position) + ".pms8.me:8220/http/pss/getBankCards?";
                pmsupdate = "http://papi-" + platlist.get(position) + ".pms8.me:8220/http/pss/setBankCardState?";
                Toast.makeText(alipyActivity.this, platlist.get(position), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        int versions = 0;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            versions = info.versionCode;

        } catch (Exception e) {
        }
        version.setText(versions + "");
//        int
        statechange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                flag = isChecked;
            }
        });

//        websocket.send();
//        NotifyHelper.sound(alipyActivity.this);
        //(new Date((new Date("Apr 8, 2017 1:04:00 PM")).getTime() + 8 * 3600 * 1000))
        SimpleDateFormat date = new SimpleDateFormat("MM-dd HH:mm");
        Log.e("seconds", date.format(new Date(new Date("Apr 8, 2017 1:04:00 PM").getTime() + 8 * 3600 * 1000)));
        QHBApplication.activityStartMain(alipyActivity.this);
        databaseHelper = new DatabaseHelper(alipyActivity.this);
        db = databaseHelper.getWritableDatabase();
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ArrayList<String> list = null;
//                list.add("23400");
                Intent intentes = new Intent(Config.BEGINUPSATE);
                alipyActivity.this.sendBroadcast(intentes);
//                textstate.setText("禁用");
            }
        });
        bankcard = BitmapUtils.getKey(alipyActivity.this, "bankcard");
        platfrom = BitmapUtils.getKey(alipyActivity.this, "platfrom");
        depart = BitmapUtils.getKey(alipyActivity.this, "depart");
        if ("hashtech".equals(depart))
            innerNeturl = hashNeturl;
        else if ("anxeuz".equals(depart))
            innerNeturl = anxNeturl;
        else
            innerNeturl = ipeakNeturl;
        if (NetworkUtil.isNetworkAvailable(alipyActivity.this)) {
            new AutoUpdateTask(this).execute();
        }
        textbankcard.setText(bankcard);
        textplat.setText(platfrom);
        ispms = platfrom.equals("MSGREEN");
//        devicestring = BitmapUtils.getKey(alipyActivity.this, "devicename");
//        db.execSQL("cre");
        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.ACTION_QIANGHONGBAO_SERVICE_CONNECT);
        filter.addAction(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT);
        filter.addAction(Config.ACTION_NOTIFY_LISTENER_SERVICE_DISCONNECT);
        filter.addAction(Config.ACTION_NOTIFY_LISTENER_SERVICE_CONNECT);
        filter.addAction(Config.NOTIFYINSERT);
        filter.addAction(Config.BEGINTASK);
        filter.addAction(Config.NEWBEGINTASK);
        filter.addAction(Config.BEGINUPSATE);
        filter.addAction(Config.BEGINGETSATE);
        filter.addAction(Config.GETOBJECT);
        filter.addAction(Config.BEGININSERT);
        filter.addAction(Config.BEGININSERTTASK);
        registerReceiver(qhbConnectReceiver, filter);
        limitString = limit.getText().toString();
        /**
         * 版本更新
         *
         * @param context
         * @param versionCode   版本号
         * @param url           apk下载地址
         * @param updateMessage 更新内容
         * @param isForced      是否强制更新
         */
//        BaseAndroid.checkUpdate(alipyActivity.this,500,
//                "http://pms-ftp.neweb.me/record.apk",
//                "更新了XXX\n修复OOO", false);
    }


    @SuppressLint("JavascriptInterface")
    private void findbyId() {
        begain = (Button) findViewById(R.id.begin);
        noti = (Button) findViewById(R.id.noti);
        webchat = (Button) findViewById(R.id.webchat);
        ailipy = (Button) findViewById(R.id.ailipy);
        qr = (Button) findViewById(R.id.qr);
        change = (Button) findViewById(R.id.change);
        textstate = (TextView) findViewById(R.id.state);
        nowmoney = (TextView) findViewById(R.id.nowmoney);
        textplat = (TextView) findViewById(R.id.plat);
        version = (TextView) findViewById(R.id.version);
        textbankcard = (TextView) findViewById(R.id.bankcard);
        list = new ArrayList<Alidetail>();
        list.add(new Alidetail());
        handler = new UIHandler();
        notifyHelper = new NotifyHelper();
        detailAdapter = new AlipayAdapter(list, alipyActivity.this);
        statechange = (CheckBox) findViewById(R.id.statechange);
        limit = (EditText) findViewById(R.id.limit);
        sure = (Button) findViewById(R.id.sure);
        reflush = (Button) findViewById(R.id.reflush);
        stop = (Button) findViewById(R.id.stop);
        network = (AppCompatSpinner) findViewById(R.id.network);
        //        websocket = new WebScoketeRequ(alipyActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intentes = new Intent(Config.BEGINGETSATE);
        alipyActivity.this.sendBroadcast(intentes);
    }

    private void beginTask() {
        if (wechatnote) {
            Log.e("任务已开始", "任务已开始");
            return;
        }
        account_times++;
        Log.e("震动了", "震动了");
        NotifyHelper.sound(alipyActivity.this);
        NotifyHelper.getSceen(alipyActivity.this);
        wechatnote = true;
        Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
        startActivity(LaunchIntent);
        BitmapUtils.sendHandler(handler, 0, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(qhbConnectReceiver);
        } catch (Exception e) {
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        begain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivity(intent);
                    Toast.makeText(alipyActivity.this, R.string.tips, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
                    startActivity(intent);
                    Toast.makeText(alipyActivity.this, R.string.tips, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        reflush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentes = new Intent(Config.BEGINGETSATE);
                alipyActivity.this.sendBroadcast(intentes);
            }
        });
        webchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (QiangHongBaoService.isRunning()) {
                    Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
                    startActivity(LaunchIntent);
                    handler.sendEmptyMessageDelayed(0, 1000);
                } else {
                    Toast.makeText(alipyActivity.this, "服务未启动", Toast.LENGTH_LONG).show();
                }
            }
        });
        ailipy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intentes = new Intent(Config.BEGINTASK);
//                alipyActivity.this.sendBroadcast(intentes);

                if (QiangHongBaoService.isRunning()) {
                    Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.eg.android.AlipayGphone");
                    QiangHongBaoService.service.requestWebScripts(true);
                    startActivity(LaunchIntent);
                    handler.sendEmptyMessageDelayed(0, 1000);
                } else {
                    Toast.makeText(alipyActivity.this, "服务未启动", Toast.LENGTH_LONG).show();
                }
            }
        });
        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(alipyActivity.this, QrActivity.class);
                startActivity(intent);
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(alipyActivity.this, limit.getText().toString(), Toast.LENGTH_LONG).show();
                limitString = limit.getText().toString();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = null;
                Toast.makeText(alipyActivity.this, string.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 4) {
            Double moneychange = Double.parseDouble(data.getStringExtra("bian"));
            db = databaseHelper.getReadableDatabase();
            List<Detail> list = databaseHelper.querydata(1, "_id desc");
            Double Beforemoney = 0.00;
            if (list.size() > 0) {
                Beforemoney = Double.parseDouble(list.get(0).getBeforemoney());
            }
            Double Aftermoney = ((Double) (Beforemoney * 100 + moneychange * 100)) / 100;
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            databaseHelper.inSertdata(NotifyHelper.getDatefomate(""), moneychange, "");
        }
    }

    public void sendData() {
        String url = "http://192.168.12.121:8080/api/addWechatDepositRecord";
        Log.e("url:", url);
//        Listdeposit listdeposit = databaseHelper.querylist(alipyActivity.this);
        JSONObject params = new JSONObject();
        try {
//            String wechat = listdeposit.getWechatAccount();
//                    params.put("String", JsonUtil.objectToString(wechat.substring(wechat.indexOf("：")+1, wechat.length())));
//                    params.put("depositRecords", JsonUtil.objectToString(listdeposit.getDeposit()));
            params.put("item", JsonUtil.objectToString(alipaylist));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("String", JsonUtil.objectToString(alipaylist));
        final String param = new Gson().toJson(alipaylist);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            public void onResponse(String response) {
                Log.e("response", response);
                Return retrun = (Return) JsonUtil.stringToObject(response, Return.class);
//                        retrun =
                Toast.makeText(alipyActivity.this, retrun.getReturnMessage(), Toast.LENGTH_LONG).show();
                if (retrun.getReturnCode() == 200) {
                    List<Detail> list = databaseHelper.querydata(1, "_id desc");
                    BitmapUtils.setKey(alipyActivity.this, "_id", list.get(0).getId() + "");
                }
            }

        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
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
                        /*
                        // String responseString = "";
                        if (response != null) {
                            responseString = String.valueOf(response.statusCode);
                            // can get more details such as response.headers
                        }
                        */
                return Response.success(responseText, HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 2, 1.0f));
        QHBApplication.getHttpQueues().add(request);
    }

    private BroadcastReceiver qhbConnectReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isFinishing()) {
                return;
            }
            String action = intent.getAction();

            Log.e("MainActivity", "receive-->" + action);
            if (Config.GETOBJECT.equals(action)) {
                Toast.makeText(alipyActivity.this, "心跳", Toast.LENGTH_LONG).show();
            } else if (Config.BEGINUPSATE.equals(action)) {
                if (ispms) {
                    String url = pmsupdate + "cardno=" + bankcard;
                    if (state == 1) {
                        url += "&state=2";
                    } else {
                        url += "&state=1";
                    }
                    JSONObject params = new JSONObject();
                    record = new Record();
                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        public void onResponse(String response) {
                            Log.e("response", response);
                            if (response.equals("success")) {
                                Intent intentes = new Intent(Config.BEGINGETSATE);
                                alipyActivity.this.sendBroadcast(intentes);
                            }
                        }

                    }, new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
//                        handler.sendEmptyMessageDelayed(2, 2000);
                            Toast.makeText(alipyActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                            Log.e("error:", error + "");
                        }
                    }) {
                        @Override
                        public String getBodyContentType() {
                            return "application/json; charset=utf-8";
                        }
                    };

                    request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 2, 1.0f));
                    QHBApplication.getHttpQueues().add(request);
                } else {
                    String url = oaupdatehttp + "?accountNo=" + bankcard + "&product=" + platfrom + "&timestamp=" + new Date().getTime() + "&status=" + state;
                    try {
                        String signature = BitmapUtils.EncoderByMd5(bankcard + platfrom + state + new Date().getTime() + "0ef275367d1d4b60890591a9506a4e5c");
                        url += "&signature=" + signature;
                        Log.e("url", url);
                    } catch (Exception e) {

                    }
                    final StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        public void onResponse(String response) {
                            Log.e("response", response);
                            Return retrun = (Return) JsonUtil.stringToObject(response, Return.class);
                            if (retrun.getStatus() == 200) {
                                Toast.makeText(alipyActivity.this, retrun.getMsg(), Toast.LENGTH_LONG).show();
                                Intent intentes = new Intent(Config.BEGINGETSATE);
                                alipyActivity.this.sendBroadcast(intentes);
                            }
                        }
                    }, new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
//                        handler.sendEmptyMessageDelayed(2, 2000);
                            Toast.makeText(alipyActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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
                        /*
                        // String responseString = "";
                        if (response != null) {
                            responseString = String.valueOf(response.statusCode);
                            // can get more details such as response.headers
                        }
                        */
                            return Response.success(responseText, HttpHeaderParser.parseCacheHeaders(response));
                        }
                    };

                    request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 2, 1.0f));
                    QHBApplication.getHttpQueues().add(request);
                }

            } else if (Config.BEGINGETSATE.equals(action)) {
                String url = "";
                if (ispms) {
                    url = pmshttp + "cardno=" + bankcard;
                } else {
                    url = oahttp + "&accountNo=" + bankcard + "&product=" + platfrom + "&timestamp=" + new Date().getTime();
                    try {
                        String signature = BitmapUtils.EncoderByMd5(alipyActivity.bankcard + alipyActivity.platfrom + new Date().getTime() + "0ef275367d1d4b60890591a9506a4e5c");
                        url += "&signature=" + signature;
                        Log.e("url", url);
                    } catch (Exception e) {

                    }
                }
                JSONObject params = new JSONObject();
                record = new Record();
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    public void onResponse(String response) {
                        Log.e("response", response);
                        if (ispms) {
                            List<bankcard> retrun = (List<bankcard>) JsonUtil.stringToList(response, bankcard.class);
                            if (retrun.size() > 0) {
                                if (retrun.get(0).getFlag().equals("DISABLED")) {
                                    textstate.setText("禁用");
                                    state = 2;
                                } else if (retrun.get(0).getFlag().equals("NORMAL")) {
                                    textstate.setText("正常");
                                    state = 1;
                                } else {
                                    textstate.setText("关闭");
                                    state = 2;
                                }
                                nowmoney.setText(retrun.get(0).getCredit());
                                textbankcard.setText(retrun.get(0).getBankCard());
                            }
                        } else {
                            oabankcard retrun = (oabankcard) JsonUtil.stringToObject(response, oabankcard.class);
                            if (retrun.getStatus() == 200) {
                                if (retrun.getDto().getFlag() == 0) {
                                    textstate.setText("正常");
                                    state = 1;
                                } else if (retrun.getDto().getFlag() == 1) {
                                    textstate.setText("禁用");
                                    state = 0;
                                } else {
                                    textstate.setText("关闭");
                                    state = 0;
                                }
                            }
                        }
                    }

                }, new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(alipyActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        Log.e("error:", error + "");
                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }
                };

                request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 2, 1.0f));
                QHBApplication.getHttpQueues().add(request);
            } else if (Config.NEWBEGINTASK.equals(action)) {
//                final StringRequest request = new StringRequest(Request.Method.GET, "http://192.168.11.128:8081/record?account=" + bankcard + "&page=1&size=10", new Response.Listener<String>() {
                Log.e("获取记录", innerNeturl + "/record?account=" + bankcard + "&page=1&size=10");
                final StringRequest request = new StringRequest(Request.Method.GET, innerNeturl + "/record?account=" + bankcard + "&page=1&size=10", new Response.Listener<String>() {
                    public void onResponse(String response) {
                        Log.e("response", response);
                        NewReturn retrun = (NewReturn) JsonUtil.stringToObject(response, NewReturn.class);
//                        retrun =
                        Toast.makeText(alipyActivity.this, retrun.getMsg(), Toast.LENGTH_LONG).show();
                        if (retrun.getCode() == 200) {
                            //
//                            Infolist infoList = (Infolist) JsonUtil.stringToObject(response, Infolist.class);
//                            infolist = infoList.getInfoList();
                            infolist = retrun.getData().getInfoList();
                        }
                        handler.sendEmptyMessageDelayed(2, 2000);
                    }
                }, new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
//                        dialog.dismiss();
//                        handler.sendEmptyMessageDelayed(2, 2000);
                        Toast.makeText(alipyActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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
            } else if (Config.BEGINTASK.equals(action)) {
                Log.e("我要任务", "我要任务");
                String url = httpurl + "/api/getDepositRecordInfos";
                JSONObject params = new JSONObject();
                record = new Record();
                try {
                    record.setClientName(bankcard);
                    record.setAccountType("7");
                    record.setAccount(bankcard);
                    record.setQuerySize("10");
                    params.put("item", JsonUtil.objectToString(record));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("record", JsonUtil.objectToString(record));
                final String param = new Gson().toJson(record);
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    public void onResponse(String response) {
                        Log.e("response", response);
                        Return retrun = (Return) JsonUtil.stringToObject(response, Return.class);
//                        retrun =
                        Toast.makeText(alipyActivity.this, retrun.getReturnMessage(), Toast.LENGTH_LONG).show();
                        if (retrun.getReturnCode() == 200) {
                            //
                            Infolist infoList = (Infolist) JsonUtil.stringToObject(response, Infolist.class);
                            infolist = infoList.getInfoList();
//                            if (infoList.getInfoList().size() > 0) {
//                                billName = infoList.getInfoList().get(0).getPayerNickname();
//                                billAmount = infoList.getInfoList().get(0).getTransferAmount();
//                                timeInfo2 = infoList.getInfoList().get(0).getTransferTime();
//                            }
//                            infoList.getInfoList();
//                            response.infoList
                        }
                        handler.sendEmptyMessageDelayed(2, 2000);
                    }

                }, new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        handler.sendEmptyMessageDelayed(2, 2000);
                        Toast.makeText(alipyActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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
                        /*
                        // String responseString = "";
                        if (response != null) {
                            responseString = String.valueOf(response.statusCode);
                            // can get more details such as response.headers
                        }
                        */
                        return Response.success(responseText, HttpHeaderParser.parseCacheHeaders(response));
                    }
                };

                request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 2, 1.0f));
                QHBApplication.getHttpQueues().add(request);
//                if(nodeInfo!=null) {
//                beginTask();
            } else if (Config.NOTIFYINSERT.equals(action)) {
                Log.e("开始登陆", "登陆");
                String url = httpurl + "/api/syncAccountStatus";
                JSONObject params = new JSONObject();
                syncmoney = new Syncmoney();
                try {
                    syncmoney.setAccount(bankcard);
                    syncmoney.setAccountType("99");
                    syncmoney.setRealTimeBalance(money);
                    syncmoney.setRealTimeBalance((new Date()).getTime() + "");
                    syncmoney.setClientName(devicestring);
                    Log.e("JSON", JsonUtil.objectToString(syncmoney));
                    params.put("item", JsonUtil.objectToString(syncmoney));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("String", JsonUtil.objectToString(alipaylist));
                final String param = new Gson().toJson(alipaylist);
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    public void onResponse(String response) {
                        Log.e("response", response);
                        Return retrun = (Return) JsonUtil.stringToObject(response, Return.class);
                        Toast.makeText(alipyActivity.this, retrun.getReturnMessage(), Toast.LENGTH_LONG).show();
//                        if (retrun.getReturnCode() == 200) {
//                        }
                    }

                }, new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(alipyActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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
            } else if (Config.ACTION_NOTIFY_LISTENER_SERVICE_CONNECT.equals(action)) {
                NotifyHelper.soundlimt(alipyActivity.this);
            } else if (Config.BEGININSERTTASK.equals(action)) {
                String urltest = httpurl + "/api/addAlipayDepositRecord";
                JSONObject paramstest = new JSONObject();
                alipaylist = new Alipaylist();
                alipaylist.setAlipayAccount(bankcard);
                List<Alidetail> depositRecordstest = new ArrayList<Alidetail>();
                Alidetail alidetailtest = new Alidetail();
                alidetailtest.setId("20170509200040011100110003433620");
                alidetailtest.setTransferAmount("30");
                alidetailtest.setPlafrom("oa");
                alidetailtest.setTransferTime("2017-07-07 10:12:00");
                alidetailtest.setSenderNickname("静静");
                alidetailtest.setSenderName("*俊");
                alidetailtest.setSenderComment("收款");
                alidetailtest.setSenderAccount("280***@qq.com");
                depositRecordstest.add(alidetailtest);
                alipaylist.setDepositRecords(depositRecordstest);
                try {
                    Log.e("JSON", JsonUtil.objectToString(alipaylist));
                    paramstest.put("item", JsonUtil.objectToString(alipaylist));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String param = new Gson().toJson(alipaylist);
                StringRequest request = new StringRequest(Request.Method.POST, urltest, new Response.Listener<String>() {
                    public void onResponse(String response) {
                        Log.e("response", response);
                        Return retrun = (Return) JsonUtil.stringToObject(response, Return.class);
                        Toast.makeText(alipyActivity.this, retrun.getReturnMessage(), Toast.LENGTH_LONG).show();
                    }

                }, new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(alipyActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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
            } else if (Config.BEGININSERT.equals(action)) {
                String url = httpurl + "/api/addAlipayDepositRecord";
                if (!("yes").equals(newSelect))
                    url = httpurl + "/api/addWechatDepositRecord";
                else {
                    url = innerNeturl + "/record";
//                    for (int i = 0; i < alilist.size(); i++) {
//                        alilist.get(i).setTransferTime(new Date(alilist.get(i).getTransferTime()).getTime() + "");
//                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date d = new Date();
                    for (int i = 0; i < alilist.size(); i++) {
                        try {
                            d = sdf.parse(alilist.get(i).getTransferTime());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
//                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                        alilist.get(i).setTransferTime(d.getTime() + "");
                        Log.e("time:", (new Date()) + " 和 " + d);
                    }
                }
                JSONObject params = new JSONObject();
                try {
                    Log.e("JSON", JsonUtil.objectToString(alipaylist));
                    params.put("item", JsonUtil.objectToString(alipaylist));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("String", JsonUtil.objectToString(alipaylist));
                final String param = new Gson().toJson(alipaylist);
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    public void onResponse(String response) {
                        Log.e("response", response);
                        Return retrun = (Return) JsonUtil.stringToObject(response, Return.class);
                        if ("yes".equals(newSelect)) {
                            if (retrun.getCode() == 200) {
                                billAfterAmount = alidetail.getTransferAmount();
                                billAfterName = alidetail.getSenderNickname();
                                timeAfterInfo2 = alidetail.getTransferTime();
                                if (retrun.getMsg().indexOf("成功") != -1)
                                    Toast.makeText(alipyActivity.this, "successfully created", Toast.LENGTH_LONG).show();
//                                else if (retrun.getMsg().indexOf("此批次进款记录已存在") != -1)
//                                    Toast.makeText(alipyActivity.this, "created already", Toast.LENGTH_LONG).show();
                                else
                                    Toast.makeText(alipyActivity.this, "unkonw error", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(alipyActivity.this, "添加失败", Toast.LENGTH_LONG).show();
//                            Toast.makeText(alipyActivity.this, "successfully created", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            if (retrun.getReturnCode() == 200) {
                                billAfterAmount = alidetail.getTransferAmount();
                                billAfterName = alidetail.getSenderNickname();
                                timeAfterInfo2 = alidetail.getTransferTime();
                                if (retrun.getReturnMessage().indexOf("此批次进款记录同步成功") != -1)
                                    Toast.makeText(alipyActivity.this, "successfully created", Toast.LENGTH_LONG).show();
                                else if (retrun.getReturnMessage().indexOf("此批次进款记录已存在") != -1)
                                    Toast.makeText(alipyActivity.this, "created already", Toast.LENGTH_LONG).show();
                                else
                                    Toast.makeText(alipyActivity.this, "unkonw error", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(alipyActivity.this, "添加失败", Toast.LENGTH_LONG).show();
//                            Toast.makeText(alipyActivity.this, "successfully created", Toast.LENGTH_LONG).show();
                            }
                        }
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                AccessibilityHelper.performBack(QiangHongBaoService.service);
                                alipyActivity.handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        AccessibilityHelper.performBack(QiangHongBaoService.service);
                                        handler.sendEmptyMessageDelayed(1, 2000);
//77                                        websocket.send(3);
                                    }
                                }, 1000);

                            }
                        }, 1000);
                    }

                }, new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(alipyActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        Log.e("error:", error + "");
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                AccessibilityHelper.performBack(QiangHongBaoService.service);
                                alipyActivity.handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        AccessibilityHelper.performBack(QiangHongBaoService.service);
                                        handler.sendEmptyMessageDelayed(1, 2000);
                                    }
                                }, 1000);

                            }
                        }, 1000);
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
            }
        }
    };

    public static class Update {
        private int status;
        private String accountNo;
        private String product;
        private Long timestamp;
        private String signature;

        public Update(int status, String accountNo, String product, Long timestamp, String signature) {
            this.status = status;
            this.accountNo = accountNo;
            this.product = product;
            this.timestamp = timestamp;
            this.signature = signature;
        }

        public Update() {
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getAccountNo() {
            return accountNo;
        }

        public void setAccountNo(String accountNo) {
            this.accountNo = accountNo;
        }

        public String getProduct() {
            return product;
        }

        public void setProduct(String product) {
            this.product = product;
        }

        public Long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }
    }

    public static class oabankcard {
        private int status;
        private String msg;
        private dto dto;

        public oabankcard(int status, String msg, oabankcard.dto dto) {
            this.status = status;
            this.msg = msg;
            this.dto = dto;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public oabankcard.dto getDto() {
            return dto;
        }

        public void setDto(oabankcard.dto dto) {
            this.dto = dto;
        }

        class dto {
            private String bussinessnumber;
            private int flag;

            public String getBussinessnumber() {
                return bussinessnumber;
            }

            public void setBussinessnumber(String bussinessnumber) {
                this.bussinessnumber = bussinessnumber;
            }

            public int getFlag() {
                return flag;
            }

            public void setFlag(int flag) {
                this.flag = flag;
            }
        }
    }

    /**
     * 更新快速读取通知的设置
     */
    public static class bankcard {
        private String bankCard;
        private String credit;
        private String promptCredit;
        private String flag;

        public String getBankCard() {
            return bankCard;
        }

        public void setBankCard(String bankCard) {
            this.bankCard = bankCard;
        }

        public String getCredit() {
            return credit;
        }

        public void setCredit(String credit) {
            this.credit = credit;
        }

        public String getPromptCredit() {
            return promptCredit;
        }

        public void setPromptCredit(String promptCredit) {
            this.promptCredit = promptCredit;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }
    }

    private static final class Parameter {
        public String wechatAccount;
        public List<DepositRecord> depositRecords = new ArrayList<DepositRecord>();

        @Override
        public String toString() {
            return "{ wechatAccount=" + wechatAccount + ", depositRecords=" + depositRecords + " }";
        }

        private static final class DepositRecord {
            public Integer id;
            public Double balance;
            public Double transferAmount;
            public String senderComment;
            public String beneficiaryComment;

            public DepositRecord(Integer id, Double balance, Double transferAmount, String senderComment, String beneficiaryComment) {
                super();
                this.id = id;
                this.balance = balance;
                this.transferAmount = transferAmount;
                this.senderComment = senderComment;
                this.beneficiaryComment = beneficiaryComment;
            }

            @Override
            public String toString() {
                return "{ id=" + id + ", balance=" + balance + ", transferAmount=" + transferAmount + ", senderComment="
                        + senderComment + ", beneficiaryComment=" + beneficiaryComment + " }";
            }
        }
    }

    class MyAdapter extends BaseAdapter {
        private ArrayList<String> list;
        private Context context;

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public MyAdapter(ArrayList<String> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView iv = new TextView(context);//针对外面传递过来的Context变量，
            iv.setText(list.get(position));
//            Log.i("magc", String.valueOf(imgs[position]));
            iv.setLayoutParams(new LinearLayoutCompat.LayoutParams(200, 80));//设置Gallery中每一个图片的大小为80*80。
//            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            return iv;
        }
    }
//    publiJ
}