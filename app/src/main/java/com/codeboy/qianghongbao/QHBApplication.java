package com.codeboy.qianghongbao;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * <p>Created 16/1/16 上午1:15.</p>
 * <p><a href="mailto:730395591@qq.com">Email:730395591@qq.com</a></p>
 * <p><a href="http://www.happycodeboy.com">LeonLee Blog</a></p>
 *
 * @author LeonLee
 */
public class QHBApplication extends Application {
    public static RequestQueue queues;
    public static String platfrom;

    @Override
    public void onCreate() {
        super.onCreate();
        queues = Volley.newRequestQueue(getApplicationContext());
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
//        platfrom = "1";//1为shunfupay,GREENBELT5
//        platfrom = "2";//2为新部门,GREENBELT5
    }

    public static void showShare(final Activity activity) {

    }

    public static RequestQueue getHttpQueues() {
        return queues;
    }

    /**
     * 显示分享
     */
    public static void showShare(final Activity activity, final String shareUrl) {
    }

    /**
     * 检查更新
     */
    public static void checkUpdate(Activity activity) {

    }

    /**
     * 首个activity启动调用
     */
    public static void activityStartMain(Activity activity) {

    }

    /**
     * 每个activity生命周期里的onCreate
     */
    public static void activityCreateStatistics(Activity activity) {

    }

    /**
     * 每个activity生命周期里的onResume
     */
    public static void activityResumeStatistics(Activity activity) {

    }

    /**
     * 每个activity生命周期里的onPause
     */
    public static void activityPauseStatistics(Activity activity) {

    }

    /**
     * 事件统计
     */
    public static void eventStatistics(Context context, String event) {

    }

    /**
     * 事件统计
     */
    public static void eventStatistics(Context context, String event, String tag) {

    }
}
