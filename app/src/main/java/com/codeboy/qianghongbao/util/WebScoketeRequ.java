package com.codeboy.qianghongbao.util;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.codeboy.qianghongbao.Config;
import com.codeboy.qianghongbao.QiangHongBaoService;
import com.codeboy.qianghongbao.alipyActivity;
import com.codeboy.qianghongbao.model.Basemodel;
import com.codeboy.qianghongbao.model.Data;
import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.callback.DataCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by colincao on 4/17/2017.
 */

public class WebScoketeRequ {
    public static WebSocket webSocket;
    public Context context;

    public WebScoketeRequ(Context context) {
        this.context = context;
    }

    public void sendMessage(String msg) {
        Log.e("msg", msg);
        if (webSocket == null || !webSocket.isOpen()) {
            Log.e("重连了", "重连了");
            AsyncHttpClient.getDefaultInstance().websocket(
                    "ws://papi-wtt.pms8.me:8220/",// webSocket地址
//                    "ws://192.168.12.106:8080/",// webSocket地址
                    "8220", webSocketConnectCallback// 端口
//                    "8080", webSocketConnectCallback// 端口
            ).tryGet();
        } else {
            webSocket.send(msg);
        }
    }

    public final AsyncHttpClient.WebSocketConnectCallback webSocketConnectCallback = new AsyncHttpClient.WebSocketConnectCallback() {

        @Override
        public void onCompleted(Exception ex, WebSocket webSocket) {
            Log.e("ex", ex + "");
            if (ex != null) {
                ex.printStackTrace();
                return;
            }
            WebScoketeRequ.webSocket = webSocket;
            webSocket.send("{\"service\":\"heartBeat\",\"functionName\":\"heartBeat\",\"data\":{\"currentTime\":1492480862815},\"dialog\":false,\"token\":\"" + alipyActivity.token + "\",\"account\":\"coadmin\"}");// 发送消息的方法
//                        webSocket.send("{\"service\":\"user\",\"functionName\":\"login\",\"data\":{\"item\":{\"loginName\":\"inquire01\",\"password\":\"test123456\",\"operatorName\":\"ricoinquire\"}},\"dialog\":false,\"token\":\"" + alipyActivity.token + "\"}");// 发送消息的方法
//                        webSocket.send(new byte[10]);
//                        Log.i("心跳包", "onStringAvailable: ");
            webSocket.setStringCallback(new WebSocket.StringCallback() {
                public void onStringAvailable(String s) {
                    Log.i("qwe", "onStringAvailable: " + s);
                    final Basemodel basemodel = (Basemodel) JsonUtil.stringToObject(s, Basemodel.class);
                    if (s.indexOf("loadingBankCard") != -1 && s.indexOf("bankcard") != -1) {
                        if (basemodel.getData().getItem() != null && basemodel.getData().getItem().size() > 0) {
                            alipyActivity.credit = Float.parseFloat(basemodel.getData().getItem().get(0).getCredit());
//                            alipyActivity.state = basemodel.getData().getItem().get(0).getFlag();
                            alipyActivity.textstate.setText(alipyActivity.state + "");
                        }
                    } else if (s.indexOf("\"service\":\"user\"") != -1) {
                        alipyActivity.token = basemodel.getData().getToken();
                        send(5);
//                        sendMessage("{\"service\":\"heartBeat\",\"functionName\":\"heartBeat\",\"data\":{\"currentTime\":1492480862815},\"dialog\":false,\"token\":\"" + alipyActivity.token + "\",\"account\":\"coadmin\"}");// 发送消息的方法
                    } else if (s.indexOf("getBankCardByUser") != -1) {
                        Log.e("s", s);
                        if (basemodel.getData().getItem() != null && basemodel.getData().getItem().size() > 0) {
//                            alipyActivity.state = basemodel.getData().getItem().get(0).getFlag();
                            alipyActivity.textstate.setText(alipyActivity.state + "");
//                            alipyActivity.bankcard = basemodel.getData().getItem().get(0).getBankCard();
//                            alipyActivity.credit = Float.parseFloat(basemodel.getData().getItem().get(0).getCredit());
//                            new Date((new Date(value)).getTime() + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '');
                        }
//                        send(3);
                    } else if (s.indexOf("manualDeposit") != -1 && s.indexOf("操作成功") == -1) {
                        Log.e("s", s);
                        if (basemodel.getData().getItem() != null && basemodel.getData().getItem().size() > 0) {
                            alipyActivity.itemlist = basemodel.getData().getItem();
//                            alipyActivity.bankcard = "m747064@163.com";

//                            for (int i = 0; i < basemodel.getData().getItem().size(); i++) {
//                                if (!basemodel.getData().getItem().get(i).getStatus().equals("CANCEL")) {
//                                    // &&
//                                    alipyActivity.credit = Float.parseFloat(basemodel.getData().getItem().get(i).getBalance());
//                                    alipyActivity.billName = basemodel.getData().getItem().get(i).getPayer();
////                                    alipyActivity.billAmount = basemodel.getData().getItem().get(i).getAmount();
//                                  /*  if (alipyActivity.billAmount.equals(""))
//                                        alipyActivity.billAmount = "50.0";
//                                    else*/
//                                        alipyActivity.billAmount = basemodel.getData().getItem().get(i).getAmount();
////                                     basemodel.getData().getItem().get(i).getDepositTime();
//                                    SimpleDateFormat date = new SimpleDateFormat("HH:mm");
//                                    alipyActivity.timeInfo2 = date.format(new Date(new Date(basemodel.getData().getItem().get(i).getDepositTime()).getTime()));
////                                    if()
//                                    break;
//                                }
//                            }
                            alipyActivity.handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, alipyActivity.credit + "", Toast.LENGTH_LONG).show();
                                }
                            });
                            alipyActivity.handler.sendEmptyMessageDelayed(1, 2000);
                        }
                    } else if (s.indexOf("heartBeat") != -1) {

                        if (s.indexOf("token无效") != -1)
                            send(0);
//                        alipyActivity.handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                sendMessage("{\"service\":\"heartBeat\",\"functionName\":\"heartBeat\",\"data\":{\"currentTime\":1492480862815},\"dialog\":false,\"token\":\"" + alipyActivity.token + "\",\"account\":\"coadmin\"}");// 发送消息的方法
//                            }
//                        }, 10000);
                    } else if (s.indexOf("操作成功") != -1) {
//                        send(3);
//                        String msg = "操作失败";
//                        if (basemodel.getData().getStatus().equals("200")) {
//                            msg = "操作成功";
//                        }
                        alipyActivity.handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                AccessibilityHelper.performBack(QiangHongBaoService.service);
                                alipyActivity.handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        AccessibilityHelper.performBack(QiangHongBaoService.service);
                                        send(3);
                                    }
                                }, 1000);


                            }
                        }, 1000);
                        alipyActivity.handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "操作" + basemodel.getData().getStatus()
                                        , Toast.LENGTH_LONG).show();
                            }
                        });
                    }
//                                alipyActivity.webview.loadUrl("javascript:window.getObject("+s+")");
//                    Intent intent = new Intent(Config.GETOBJECT);
//                    intent.putExtra("json", s);
//                    context.sendBroadcast(intent);
                }
            });
            webSocket.setDataCallback(new DataCallback() {
                public void onDataAvailable(DataEmitter emitter, ByteBufferList byteBufferList) {
                    System.out.println("I got some bytes!");
                    // note that this data has been read
                    byteBufferList.recycle();
                }
            });
//                        webSocket.
        }
    };

    public void send(int type) {
        switch (type) {
            case 0:
                // sendMessage("{\"service\":\"user\",\"functionName\":\"login\",\"data\":{\"item\":{\"loginName\":\"anxali01\",\"password\":\"1234abcd\",\"operatorName\":\"ali_Jolly\"}},\"dialog\":false,\"token\":\"1492329791949\"}");// 发送消息的方法
//                sendMessage("{\"service\":\"user\",\"functionName\":\"login\",\"data\":{\"item\":{\"loginName\":\"" + alipyActivity.loginName + "\",\"password\":\"" + alipyActivity.password + "\",\"operatorName\":\"" + alipyActivity.operatorName + "\"}},\"dialog\":false,\"token\":\"" + alipyActivity.token + "\"}");// 发送消息的方法
//                break;
//            case 1:
//                if (alipyActivity.token == null)
//                    return;
////                sendMessage("{\"service\":\"bankcard\",\"functionName\":\"getBankCardByUser\",\"data\":{\"item\":{\"user\":{\"username\":\"anxali01\",\"password\":\"E10ADC3949BA59ABBE56E057F20F883E\",\"departmentId\":1,\"creator\":\"admin\",\"loginTimes\":465,\"nickname\":\"Inquire\",\"function\":\"#1,#1,1,#1,#1,1,1,#1,1,1,1,#1,1,#1,#1,1,1,1,1,1,\",\"roles\":[{\"name\":\"Inquire\",\"description\":\"Verify and create deposit in bank side\",\"id\":24,\"createDate\":\"Feb 13, 2017 3:20:22 PM\",\"lastModified\":\"Feb 13, 2017 3:20:22 PM\",\"rowState\":0}],\"platformId\":\"1\",\"id\":51,\"createDate\":\"Feb 13, 2017 3:06:30 PM\",\"lastModified\":\"Apr 17, 2017 5:39:39 PM\",\"rowState\":0}}},\"dialog\":false,\"token\":\"" + alipyActivity.token + "\"}");
//                // sendMessage("{\"service\":\"bankcard\",\"functionName\":\"getBankCardByUser\",\"data\":{\"item\":{\"user\":{\"username\":\"anxali01\",\"departmentId\":1,\"creator\":\"anxadmin\",\"loginTimes\":563,\"nickname\":\"alipay\",\"function\":\"#1,#1,1,#1,#1,1,1,#1,1,1,1,#1,1,#1,#1,1,1,1,1,1,\",\"roles\":[{\"name\":\"Inquire\",\"description\":\"Verify and create deposit in bank side\",\"id\":24,\"createDate\":\"Feb 13, 2017 3:20:22 PM\",\"lastModified\":\"Feb 13, 2017 3:20:22 PM\",\"rowState\":0}],\"platformId\":\"1\",\"id\":76,\"createDate\":\"Apr 6, 2017 10:10:47 AM\",\"lastModified\":\"Apr 20, 2017 3:49:02 PM\",\"rowState\":0}}},\"dialog\":false,\"token\":\"" + alipyActivity.token + "\"}");
//                sendMessage("{\"service\":\"bankcard\",\"functionName\":\"getBankCardByUser\",\"data\":{\"item\":{\"user\":{\"username\":\"anxali02\",\"departmentId\":1,\"creator\":\"admin\",\"loginTimes\":6,\"nickname\":\"alipay\",\"function\":\"#1,#1,1,#1,#1,1,1,#1,1,1,1,#1,1,#1,#1,1,1,1,1,1,\",\"roles\":[{\"name\":\"Inquire\",\"description\":\"Verify and create deposit in bank side\",\"id\":24,\"createDate\":\"Feb 13, 2017 3:20:22 PM\",\"lastModified\":\"Feb 13, 2017 3:20:22 PM\",\"rowState\":0}],\"platformId\":\"1\",\"id\":80,\"createDate\":\"Apr 21, 2017 3:25:50 PM\",\"lastModified\":\"Apr 21, 2017 3:40:53 PM\",\"rowState\":0}}},\"dialog\":false,\"token\":\"" + alipyActivity.token + "\"}");
//                break;
//            case 2:
//                Log.e("存款时间",alipyActivity.alidetail.getTransferTime());
//                //sendMessage("{\"service\": \"manualDeposit\",\"functionName\": \"add\",\"data\": {item\": {\"receiveBankCard\": \"" + alipyActivity.bankcard + "\",\"platformName\": \"1\",\"amount\": \"" + alipyActivity.alidetail.getTransferAmount() + "\",\"handCharge\": \"0\",\"balance\": \"" + ((Double) (Math.ceil(alipyActivity.credit * 100) + Math.ceil(Double.parseDouble(alipyActivity.alidetail.getTransferAmount()) * 100))) / 100 + "\",\"paymentType\": \"ATM\",\"payer\": \"" + alipyActivity.alidetail.getSenderAccount() + "\",\"depositAddress\": \"" + alipyActivity.alidetail.getSenderAccount() + "\",\"createtime\": \"" + alipyActivity.alidetail.getTransferTime() +"\"}}, \"dialog\": false,\"token\": \"+ alipyActivity.token + \",\"account\": \"ricoinquire\"}");
//                // sendMessage("{\"service\":\"manualDeposit\",\"functionName\":\"add\",\"data\":{\"item\":{\"receiveBankCard\":\"" + alipyActivity.bankcard + "\",\"platformName\":\"1\",\"amount\":\"" + alipyActivity.alidetail.getTransferAmount() + "\",\"handCharge\":\"0\",\"balance\":\"" + (double) (Math.round(alipyActivity.credit * 100) + Math.round(Float.parseFloat(alipyActivity.alidetail.getTransferAmount()) * 100)) / 100 + "\",\"paymentType\":\"ALIPAYDIRECT\",\"payer\":\"" + alipyActivity.alidetail.getSenderNickname() + "\",\"depositAddress\":\"" + alipyActivity.alidetail.getSenderAccount() + "\",\"createtime\":\"" + alipyActivity.alidetail.getTransferTime() + "\"}},\"dialog\":false,\"token\":\"" + alipyActivity.token + "\",\"account\":\"ali_Jolly\"}");
//                sendMessage("{\"service\":\"manualDeposit\",\"functionName\":\"add\",\"data\":{\"item\":{\"receiveBankCard\":\"" + alipyActivity.bankcard + "\",\"platformName\":\"1\",\"amount\":\"" + alipyActivity.alidetail.getTransferAmount() + "\",\"handCharge\":\"0\",\"balance\":\"" + (double) (Math.round(alipyActivity.credit * 100) + Math.round(Float.parseFloat(alipyActivity.alidetail.getTransferAmount()) * 100)) / 100 + "\",\"paymentType\":\"ALIPAYDIRECT\",\"payer\":\"" + alipyActivity.alidetail.getSenderNickname() + "\",\"depositAddress\":\"" + alipyActivity.alidetail.getSenderAccount() + "\",\"createtime\":\"" + alipyActivity.alidetail.getTransferTime() + "\"}},\"dialog\":false,\"token\":\"" + alipyActivity.token + "\",\"account\":\"" + alipyActivity.operatorName + "\"}");
//                break;
//            case 3:
//                sendMessage("{\"service\":\"manualDeposit\",\"functionName\":\"querryDeposit\",\"data\":{\"item\":{\"receiveBankCard\":\"" + alipyActivity.bankcard + "\"},\"size\":10,\"page\":1},\"dialog\":false,\"token\":\"" + alipyActivity.token + "\",\"account\":\"auto\"}");
//                break;
//            case 4:
//                sendMessage("{\"service\":\"bankcard\",\"functionName\":\"loadingBankCard\",\"data\":{\"page\":1,\"size\":\"500\",\"item\":{\"bankCard\":\"" + alipyActivity.bankcard + "\"}},\"dialog\":false,\"token\":\"" + alipyActivity.token + "\",\"account\":\"" + alipyActivity.operatorName + "\"}");
            case 5:
//                sendMessage("{\"service\":\"bankcard\",\"functionName\":\"updateBankCard\",\"data\":{\"item\":{\"bankId\":\"170\",\"bankCard\":\"m265975@126.com\",\"cardName\":\"程玖霞\",\"flag\":\"DISABLED\",\"credit\":0.77,\"promptCredit\":100000,\"type\":\"DEPOSIT\",\"lastLockTime\":\"2017-04-27 00:00:00\",\"platformId\":\"1\",\"user\":{\"username\":\"inquire01\",\"password\":\"E10ADC3949BA59ABBE56E057F20F883E\",\"departmentId\":1,\"creator\":\"admin\",\"loginTimes\":791,\"nickname\":\"Inquire\",\"function\":\"#1,#1,1,#1,#1,1,1,#1,1,1,1,#1,1,#1,#1,1,1,1,1,1,\",\"platformId\":\"1\",\"id\":\"51\",\"createDate\":\"Feb 13, 2017 3:06:30 PM\",\"lastModified\":\"May 19, 2017 11:52:59 AM\",\"rowState\":0},\"bankAddress\":\"123\",\"id\":10,\"createDate\":\"2017-04-27 17:59:29\",\"lastModified\":\"May 19, 2017 11:58:33 AM\"}},\"dialog\":false,\"token\":\"1495608054764\",\"account\":\"admin\"}");
                sendMessage("{\"service\":\"bankcard\",\"functionName\":\"updateBankCard\",\"data\":{\"item\":{\"bankId\":\"170\",\"bankCard\":\"m684347@163.com\",\"cardName\":\"黄成\",\"flag\":\"DISABLED\",\"promptCredit\":5000,\"type\":\"DEPOSIT\",\"platformId\":\"3\",\"telephone\":\"13012684347\",\"user\":{\"username\":\"anxali03\",\"departmentId\":2,\"creator\":\"anxadmin\",\"loginTimes\":0,\"nickname\":\"BYH\",\"function\":\"#1,#1,1,#1,#1,1,1,#1,1,1,1,#1,1,#1,#1,1,1,1,1,1,\",\"platformId\":\"3\",\"id\":\"91\",\"createDate\":\"May 19, 2017 7:52:31 PM\",\"lastModified\":\"May 19, 2017 7:52:31 PM\",\"rowState\":0},\"id\":52,\"createDate\":\"2017-05-19 18:32:43\",\"lastModified\":\"May 19, 2017 8:04:39 PM\"}},\"dialog\":false,\"token\":\"1495612811503\",\"account\":\"testadmin\"}");
                break;
            default:
                break;
        }
    }

    public void send() {
        AsyncHttpClient.getDefaultInstance().websocket(
                "ws://papi-pacnet.pms8.me:8220/pss",// webSocket地址
//                "ws://192.168.12.106:8080/pss",// webSocket地址
//                "8080", webSocketConnectCallback// 端口
                "8220", webSocketConnectCallback// 端口
        ).tryGet();
//    }

//    public static WebSocket send(final Context c, final String message) {
//        Log.e("message", message);
//        if (webSocket == null || !webSocket.isOpen()) {
//            if (webSocket != null) {
//                Log.d("WebScoketeRequ", "webSocket.isOpen: " + webSocket.isOpen());
//            }
//            final AsyncHttpClient.WebSocketConnectCallback webSocketConnectCallback = new AsyncHttpClient.WebSocketConnectCallback() {
//                @Override
//                public void onCompleted(Exception ex, WebSocket w) {
//                    if (ex != null) {
//                        ex.printStackTrace();
//                        return;
//                    }
//
//                    if (w.getSocket().isOpen()) {
//                        w.send(message);
//                        webSocket = w;
//
//                        webSocket.setStringCallback(new WebSocket.StringCallback() {
//                            public void onStringAvailable(String s) {
//                                Log.e("I got a string: ", s);
//                                String service;
//                                String function;
//                                String status;
//
//
//                            }
//                        });
//                    }
//
//
//                }
//            };
//            AsyncHttpClient.getDefaultInstance().websocket("ws://101.78.133.219:7010/pss", "7010", webSocketConnectCallback).tryGet();
//        } else {
//            webSocket.send(message);
//        }
//        return webSocket;
//    }

//AsyncHttpClient.getDefaultInstance().websocket(
//            "ws://192.168.250.38:8181",// webSocket地址
//                    "8181",// 端口
//                    new AsyncHttpClient.WebSocketConnectCallback() {
//        @Override
//        public void onCompleted(Exception ex, WebSocket webSocket) {
//            if (ex != null) {
//                ex.printStackTrace();
//                return;
//            }
//            webSocket.send("a string");// 发送消息的方法
//            webSocket.send(new byte[10]);
//            webSocket.setStringCallback(new WebSocket.StringCallback() {
//                public void onStringAvailable(String s) {
//                    Log.i(TAG, "onStringAvailable: " + s);
//                }
//            });
//            webSocket.setDataCallback(new DataCallback() {
//                public void onDataAvailable(DataEmitter emitter, ByteBufferList byteBufferList) {
//                    System.out.println("I got some bytes!");
//                    // note that this data has been read
//                    byteBufferList.recycle();
//                }
//            });
//        }

    }
}
