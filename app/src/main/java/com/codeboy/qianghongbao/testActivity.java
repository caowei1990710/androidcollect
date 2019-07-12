package com.codeboy.qianghongbao;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.webkit.WebView;
import android.widget.Button;
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
import com.codeboy.qianghongbao.model.Detail;
import com.codeboy.qianghongbao.model.Listdeposit;
import com.codeboy.qianghongbao.model.Return;
import com.codeboy.qianghongbao.util.AccessibilityHelper;
import com.codeboy.qianghongbao.util.BitmapUtils;
import com.codeboy.qianghongbao.util.DatabaseHelper;
import com.codeboy.qianghongbao.util.DetailAdapter;
import com.codeboy.qianghongbao.util.JsonUtil;
import com.codeboy.qianghongbao.util.NotifyHelper;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * Created by snsoft on 28/3/2017.
 */
public class testActivity extends Activity {

    private Dialog mTipsDialog;
    private Button begain, noti, webchat, insert, selects, update;
    private TextView count, times;
    private ListView listView;
    private AccessibilityNodeInfo nodeInfo, node, listitem, listview, amount, name, weixin, qianbao;
    private String money = "￥0.00";
    private WebView webview;
    private Handler handler;
    private clockHanlder clockhanlder = new clockHanlder();
    private int Delay = 2000;
    private NotifyHelper notifyHelper;
    public static boolean wechatnote;
    private Timer timer;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private DetailAdapter detailAdapter;
    private List<Detail> list;
    public static boolean startsmooth;
    public static int acount_text = 300, account_times = 1;

    public class clockHanlder extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            sendEmptyMessageDelayed(97, 1000);
            count.setText(acount_text + "");
            if (acount_text == 1) {
                wechatnote = false;
                Intent intent = new Intent(Config.BEGINTASK);
                testActivity.this.sendBroadcast(intent);
                return;
            } else
                acount_text--;
        }
    }

    private class UIHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.e("nodeInfo:", nodeInfo + "");
            if (QiangHongBaoService.service != null)
                nodeInfo = QiangHongBaoService.service.getRootInActiveWindow();
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    account_times++;
                    times.setText("收录次：" + account_times + "");
                    if(nodeInfo == null){
                        Toast.makeText(testActivity.this, "线程为空", Toast.LENGTH_LONG).show();
                        return;
                    }
//                    nodeInfo = QiangHongBaoService.service.getRootInActiveWindow();
                    if (AccessibilityHelper.findNodeInfosByText(nodeInfo, "我") == null) {
                        Toast.makeText(testActivity.this, "我为空", Toast.LENGTH_LONG).show();
                        return;
                    }
                    notifyHelper.click(handler, (AccessibilityHelper.findNodeInfosByText(nodeInfo, "我")).getParent(), 100);
                    BitmapUtils.sendHandler(handler, 1, Delay);
                    break;
                case 1:
                    name = AccessibilityHelper.findNodeInfosById(nodeInfo, "com.tencent.mm:id/bur");
//                    Log.e("name",name.getText().toString());
                    BitmapUtils.name = name.getText().toString();
                    notifyHelper.click(handler, (AccessibilityHelper.findNodeInfosByText(nodeInfo, "钱包")).getParent(), 500);
                    BitmapUtils.sendHandler(handler, 2, 2500);
                    break;
                case 2:
//                    AccessibilityHelper.findNodeInfosById(nodeInfo, "com.tencent.mm:id/bmn");
//                    Log.e("money", AccessibilityHelper.findNodeInfosById(nodeInfo, "com.tencent.mm:id/bmn") + "");
                    if (AccessibilityHelper.findNodeInfosById(nodeInfo, "com.tencent.mm:id/bmn") == null) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if (AccessibilityHelper.findNodeInfosById(nodeInfo, "com.tencent.mm:id/bmn") != null) {
                                    money = (AccessibilityHelper.findNodeInfosById(nodeInfo, "com.tencent.mm:id/bmn").getText().toString());
                                }
                                AccessibilityHelper.performBack(QiangHongBaoService.service);
                                BitmapUtils.sendHandler(handler, 3, 1000);
                            }
                        }, 2000);
                    } else {
                        money = (AccessibilityHelper.findNodeInfosById(nodeInfo, "com.tencent.mm:id/bmn").getText().toString());
                        AccessibilityHelper.performBack(QiangHongBaoService.service);
                        BitmapUtils.sendHandler(handler, 3, 1000);
                    }
//                    Log.e("money", money + "");
                    break;
                case 3:
//                    Log.e("money", money + "");
                    BitmapUtils.money = BitmapUtils.getMoney(money, "¥");
                    Log.e("money", BitmapUtils.money + "");
                    notifyHelper.click(handler, (AccessibilityHelper.findNodeInfosById(nodeInfo, "com.tencent.mm:id/brd")).getParent(), 500);
//                    weixin = AccessibilityHelper.findNodeInfosByText(nodeInfo, "微信");
                    BitmapUtils.sendHandler(handler, 5, 1000);
                    break;
                case 4:
//                    AccessibilityHelper.performClick(weixin.getParent());
                    BitmapUtils.sendHandler(handler, 5, Delay);
                    break;
                case 5:
                    listitem = AccessibilityHelper.findNodeInfosById(nodeInfo, "com.tencent.mm:id/aft");
                    Log.i("TAG", "-->listitem:" + listitem);
                    if (listitem != null) {
//                    Log.i("TAG,child", "-->listitem:" + listitem.getChild(0));
                        BitmapUtils.sendHandler(handler, 6, Delay * 2);
                    }

                    break;
                case 6:
                    notifyHelper.click(handler, listitem, 0);
//                    AccessibilityHelper.performClick(listitem);
                    BitmapUtils.sendHandler(handler, 7, Delay);
                    break;
                case 7:
                    BitmapUtils.nodeInfo = nodeInfo;
                    BitmapUtils.handler = handler;
                    BitmapUtils.forward = false;
                    BitmapUtils.service = QiangHongBaoService.service;
//                    BitmapUtils.beginString = "累计收款金额￥0.02，收款笔数1笔";
//                    BitmapUtils.beginCreatetime = "2017-03-29 11:26:18";
                    databaseHelper.setDeafult();
//                    if (startsmooth) {
//                        return;
//                    }
//                    startsmooth = true;
//                    BitmapUtils.getAllpay(BitmapUtils.beginString, databaseHelper);
                    BitmapUtils.getNewallpay(AccessibilityHelper.findNodeInfosByIds(nodeInfo, "com.tencent.mm:id/a34"), databaseHelper);
//                    listview = AccessibilityHelper.findNodeInfosById(nodeInfo, "com.tencent.mm:id/a2i");
//                    Log.e("TAG", "-->listview:" + listview.getChildCount());
////                    listview.performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
////                    listview.performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
////                listview.performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
////                listview.performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
////                listview.performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
//                    amount = AccessibilityHelper.findNodeInfosById(nodeInfo, "com.tencent.mm:id/a4s");
////                note1 = AccessibilityHelper.findNodeInfosById(nodeInfo, "com.tencent.mm:id/a34");
//                    List<AccessibilityNodeInfo> list = AccessibilityHelper.findNodeInfosByIds(nodeInfo, "com.tencent.mm:id/a34");
////                amountdetail = AccessibilityHelper.findNodeInfosById(nodeInfo, "com.tencent.mm:id/a34");
//                    BitmapUtils.nodeInfo = nodeInfo;
//                    BitmapUtils.handler = handler;
////                Log.e("TAG", "-->amount:" + amount.getText());
//                    String message = "message:";
//                    for(int i = 0; i < list.size() ; i++){
//                        message+=list.get(i).getText()+",";
//                    }
//                    Log.e("message", "-->message:amount:" + amount.getText() + message);
//                    AccessibilityHelper.performBack(QiangHongBaoService.service);
                    break;
                case 96:
                    AccessibilityHelper.performBack(QiangHongBaoService.service);
                    break;
                case 97:
                    count.setText(acount_text + "");
                    if (acount_text < 1) {
                        Intent intent = new Intent(Config.BEGINTASK);
                        testActivity.this.sendBroadcast(intent);
                        return;
                    }
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (acount_text > 0)
                                acount_text--;
                            Message message = new Message();
                            message.what = 97;

                            handler.sendMessage(message);
                        }
                    }, 1000);
                    break;
                case 98:
                    if (databaseHelper != null) {
                        db = databaseHelper.getReadableDatabase();
                        Log.e("db", db + "");
                        list.clear();
                        list.addAll(databaseHelper.querydata());
                        Log.e("list", list.toString());
                        detailAdapter.notifyDataSetChanged();
                    }
                    break;
                case 99:
                    Log.e("开始任务", "开始任务");

                    beginTask();
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        findbyId();
        clockhanlder.sendEmptyMessageDelayed(97, 1000);
        QHBApplication.activityStartMain(testActivity.this);
        databaseHelper = new DatabaseHelper(testActivity.this);
        db = databaseHelper.getWritableDatabase();
//        db.execSQL("cre");
        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.ACTION_QIANGHONGBAO_SERVICE_CONNECT);
        filter.addAction(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT);
        filter.addAction(Config.ACTION_NOTIFY_LISTENER_SERVICE_DISCONNECT);
        filter.addAction(Config.ACTION_NOTIFY_LISTENER_SERVICE_CONNECT);
        filter.addAction(Config.BEGINTASK);
        filter.addAction(Config.BEGININSERT);
        registerReceiver(qhbConnectReceiver, filter);
//        Intent intent = new Intent();

    }

    private void findbyId() {
        begain = (Button) findViewById(R.id.begin);
        noti = (Button) findViewById(R.id.noti);
        webchat = (Button) findViewById(R.id.webchat);
        insert = (Button) findViewById(R.id.insert);
        selects = (Button) findViewById(R.id.selects);
        update = (Button) findViewById(R.id.update);
        listView = (ListView) findViewById(R.id.list);
        count = (TextView) findViewById(R.id.count);
        times = (TextView) findViewById(R.id.times);
        webview = (WebView) findViewById(R.id.webview);
        list = new ArrayList<Detail>();
        list.add(new Detail());
        handler = new UIHandler();
        notifyHelper = new NotifyHelper();
        detailAdapter = new DetailAdapter(list, testActivity.this);
        listView.setAdapter(detailAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void beginTask() {
        if (wechatnote) {
            Log.e("任务已开始", "任务已开始");
            return;
        }
        account_times++;
        Log.e("震动了", "震动了");
        NotifyHelper.sound(testActivity.this);
        NotifyHelper.getSceen(testActivity.this);
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
        mTipsDialog = null;
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
                    Toast.makeText(testActivity.this, R.string.tips, Toast.LENGTH_LONG).show();
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
                    Toast.makeText(testActivity.this, R.string.tips, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        webchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (databaseHelper.querydata(1, "_id desc").size() == 0)//开始
//                    databaseHelper.inSertdata("2017-04-06 20:28:02", 0.16, "累计收款金额￥3.16，收款笔数17笔", "", "", "0.16");
//                if (databaseHelper.querydata(1, "_id desc").size() == 0)
//                    databaseHelper.inSertdata("2017-04-06 20:18:52", 2.08, "累计收款金额￥3.08，收款笔数2笔", "", "", "5.06");
                if (QiangHongBaoService.isRunning()) {
//                    Timer timer = new Timer();
//                    timer.schedule(new TimerTask() {
//                        @Override
//                        public void run() {
                    wechatnote = false;
                    Intent intent = new Intent(Config.BEGINTASK);
                    testActivity.this.sendBroadcast(intent);
//                        }
//                    }, 2000, 180000);
//                    Message message = new Message();
//                    message.what = 99;
//                    handler.sendMessage(message);
//
                } else {
                    Toast.makeText(testActivity.this, "服务未启动", Toast.LENGTH_LONG).show();
                }
            }
        });
        insert.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                BitmapUtils.setKey(testActivity.this, "_id", "");
                Intent intent = new Intent(Config.BEGININSERT);
                testActivity.this.sendBroadcast(intent);
//                VolleyRequest volleyRequest = new VolleyRequest(Request.Method.POST, url,params, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.e("response:",response);
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("error:",error+"");
//                    }
//                });

//                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
//                    public void onResponse(JSONObject response) {
//                        Log.e("response:", JsonUtil.objectToString(response));
//                        Return retrun = (Return) JsonUtil.stringToObject(JsonUtil.objectToString(response), Return.class);
////                        retrun =
//                        if (retrun.getReturnCode() == 200) {
//                            List<Detail> list = databaseHelper.querydata(1, "_id desc");
//                            BitmapUtils.setKey(testActivity.this, "_id", list.get(0).getId() + "");
//                        }
//                    }
//                }, new Response.ErrorListener() {
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("error:", error + "");
//                    }
//                });

//                startActivityForResult(new Intent(testActivity.this,
//                        PayItemAcitivity.class), 1);
//                db = databaseHelper.getWritableDatabase();
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                // public final String format(Date date)
//                String date = sdf.format(new Date());
//                databaseHelper.inSertdata(db,new Detail(1,date,"12",1.00,"14"));
            }
        });
        ((Button) findViewById(R.id.selects)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(testActivity.this, "CTS", Toast.LENGTH_LONG).show();
                Message message = new Message();
                message.what = 98;
                handler.sendMessage(message);
//                detailAdapter.notifyDataSetChanged();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(testActivity.this, "CTS", Toast.LENGTH_LONG).show();
                Message message = new Message();
                message.what = 98;
                handler.sendMessage(message);
                db = databaseHelper.getWritableDatabase();
                databaseHelper.onUpgrade(db, 1, 1);
//                detailAdapter.notifyDataSetChanged();
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
        Listdeposit listdeposit = databaseHelper.querylist(testActivity.this);
        JSONObject params = new JSONObject();
        try {
            String wechat = listdeposit.getWechatAccount();
//                    params.put("String", JsonUtil.objectToString(wechat.substring(wechat.indexOf("：")+1, wechat.length())));
//                    params.put("depositRecords", JsonUtil.objectToString(listdeposit.getDeposit()));
            params.put("item", JsonUtil.objectToString(listdeposit));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("String", JsonUtil.objectToString(listdeposit));
        final String param = new Gson().toJson(listdeposit);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            public void onResponse(String response) {
                Log.e("response", response);
                Return retrun = (Return) JsonUtil.stringToObject(response, Return.class);
//                        retrun =
                Toast.makeText(testActivity.this, retrun.getReturnMessage(), Toast.LENGTH_LONG).show();
                if (retrun.getReturnCode() == 200) {
                    List<Detail> list = databaseHelper.querydata(1, "_id desc");
                    BitmapUtils.setKey(testActivity.this, "_id", list.get(0).getId() + "");
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
            if (Config.ACTION_QIANGHONGBAO_SERVICE_CONNECT.equals(action)) {

//
//            }else{
//                    Toast.makeText(testActivity.this,"nodeInfo:为空",Toast.LENGTH_LONG).show();
//                }
//                if (mTipsDialog != null) {
//                    mTipsDialog.dismiss();
//                }
            } else if (Config.BEGINTASK.equals(action)) {
                Log.e("我要任务", "我要任务");

//                if(nodeInfo!=null) {
                beginTask();
            } else if (Config.ACTION_NOTIFY_LISTENER_SERVICE_CONNECT.equals(action)) {
            } else if (Config.ACTION_NOTIFY_LISTENER_SERVICE_DISCONNECT.equals(action)) {
            } else if (Config.BEGININSERT.equals(action)) {
                String url = "http://192.168.12.121:8080/api/addWechatDepositRecord";
                Log.e("url:", url);
                Listdeposit listdeposit = databaseHelper.querylist(testActivity.this);
                JSONObject params = new JSONObject();
                try {
                    String wechat = listdeposit.getWechatAccount();
//                    params.put("String", JsonUtil.objectToString(wechat.substring(wechat.indexOf("：")+1, wechat.length())));
//                    params.put("depositRecords", JsonUtil.objectToString(listdeposit.getDeposit()));
                    params.put("item", JsonUtil.objectToString(listdeposit));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("String", JsonUtil.objectToString(listdeposit));
                final String param = new Gson().toJson(listdeposit);
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    public void onResponse(String response) {
                        Log.e("response", response);
                        Return retrun = (Return) JsonUtil.stringToObject(response, Return.class);
//                        retrun =
                        Toast.makeText(testActivity.this, retrun.getReturnMessage(), Toast.LENGTH_LONG).show();
                        if (retrun.getReturnCode() == 200) {
                            List<Detail> list = databaseHelper.querydata(1, "_id desc");
                            BitmapUtils.setKey(testActivity.this, "_id", list.get(0).getId() + "");
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
        }
    };

    /**
     * 更新快速读取通知的设置
     */


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

//    publiJ
}