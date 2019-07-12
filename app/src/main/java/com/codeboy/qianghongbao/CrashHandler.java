package com.codeboy.qianghongbao;

/**
 * Created by snsoft on 22/5/2017.
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.codeboy.qianghongbao.demoActivity.DemoActivity;
import com.codeboy.qianghongbao.model.AliDetailitem;
import com.codeboy.qianghongbao.model.Record;
import com.codeboy.qianghongbao.model.Return;
import com.codeboy.qianghongbao.util.BitmapUtils;
import com.codeboy.qianghongbao.util.JsonUtil;
import com.codeboy.qianghongbao.util.NotifyHelper;
import com.google.gson.internal.LinkedTreeMap;

import org.json.JSONObject;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 *
 * @author user
 */
public class CrashHandler implements UncaughtExceptionHandler {

    public static final String TAG = "CrashHandler";

    //系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    //CrashHandler实例
    private static CrashHandler INSTANCE = new CrashHandler();
    //程序的Context对象
    private Context mContext;
    //用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String>();

    //用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Log.e(TAG, "error : ", e);
            }
            //退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        //使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                NotifyHelper.sound(mContext);
                String platfrom = BitmapUtils.getKey(mContext, "platfrom");
                Log.e("platfrom", platfrom);
//                mContext
                if (platfrom.equals("MSGREEN")) {
//                    String url = alipyActivity.pmsupdate + "cardno=" + alipyActivity.bankcard + "&state=2";
//                    JSONObject params = new JSONObject();
//                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//                        public void onResponse(String response) {
//                            Log.e("response", response);
//                        }
//
//                    }, new Response.ErrorListener() {
//                        public void onErrorResponse(VolleyError error) {
//                            Log.e("error:", error + "");
//                        }
//                    }) {
//                        @Override
//                        public String getBodyContentType() {
//                            return "application/json; charset=utf-8";
//                        }
//                    };
//
//                    request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 2, 1.0f));
//                    QHBApplication.getHttpQueues().add(request);
                } else if (!DemoActivity.toDemo) {
//                    int state = 1;
////                    if ( == 1) {
////                        state = 0;
////                    }
//                    String url = alipyActivity.oaupdatehttp + "?accountNo=" + alipyActivity.bankcard + "&product=" + alipyActivity.platfrom + "&timestamp=" + new Date().getTime() + "&status=" + state;
//                    try {
//                        String signature = BitmapUtils.EncoderByMd5(alipyActivity.bankcard + alipyActivity.platfrom + state + new Date().getTime() + "0ef275367d1d4b60890591a9506a4e5c");
//                        url += "&signature=" + signature;
//                        Log.e("url", url);
//                    } catch (Exception e) {
//
//                    }
//                    final StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//                        public void onResponse(String response) {
//                            Log.e("response", response);
//                            Return retrun = (Return) JsonUtil.stringToObject(response, Return.class);
//                            if (retrun.getStatus() == 200) {
//                                Toast.makeText(mContext, retrun.getMsg(), Toast.LENGTH_LONG).show();
////                                Intent intentes = new Intent(Config.BEGINGETSATE);
////                                mContext.sendBroadcast(intentes);
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                        public void onErrorResponse(VolleyError error) {
////                        handler.sendEmptyMessageDelayed(2, 2000);
//                            Toast.makeText(mContext, error.toString(), Toast.LENGTH_LONG).show();
//                            Log.e("error:", error + "");
//                        }
//                    }) {
//                        @Override
//                        public String getBodyContentType() {
//                            return "application/json; charset=utf-8";
//                        }
//
//                    };
//
//                    request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 2, 1.0f));
//                    QHBApplication.getHttpQueues().add(request);
//                    Log.e("线程异常", "线程异常");
                } else {
//                    Log.e("wrongurl", DemoActivity.url + "wechatstate?wechatName=" + DemoActivity.account + "&state=DISABLED");
//                    final StringRequest request = new StringRequest(Request.Method.GET, DemoActivity.url + "wechatstate?wechatName=" + DemoActivity.account + "&state=DISABLED", new Response.Listener<String>() {
//                        public void onResponse(String response) {
//                            Log.e("response", response);
//                        }
//                    }, new Response.ErrorListener() {
//                        public void onErrorResponse(VolleyError error) {
//                            Log.e("error:", error + "");
//                        }
//                    }) {
//                        @Override
//                        protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                            String responseText = "";
//                            try {
//                                responseText = new String(response.data, "UTF-8");
//                            } catch (UnsupportedEncodingException e) {
//                                e.printStackTrace();
//                            }
//                            return Response.success(responseText, HttpHeaderParser.parseCacheHeaders(response));
//                        }
//                    };
//
//                    request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 2, 1.0f));
//                    QHBApplication.getHttpQueues().add(request);
                }
//                Intent intentes = new Intent(Config.BEGINUPDATE);
//                mContext.sendBroadcast(intentes);
//                Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
        //收集设备参数信息
        collectDeviceInfo(mContext);
        //保存日志文件
        saveCrashInfo2File(ex);
        return true;
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private String saveCrashInfo2File(Throwable ex) {

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String fileName = "crash-" + time + "-" + timestamp + ".log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = "/sdcard/crash/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName);
                fos.write(sb.toString().getBytes());
                fos.close();
            }
            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
        }
        return null;
    }
}
