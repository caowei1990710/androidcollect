//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.codeboy.qianghongbao;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;
import com.codeboy.qianghongbao.BuildConfig;
import com.codeboy.qianghongbao.Config;
import com.codeboy.qianghongbao.IStatusBarNotification;
import com.codeboy.qianghongbao.QHBNotificationService;
import com.codeboy.qianghongbao.job.AccessbilityJob;
import com.codeboy.qianghongbao.job.WechatAccessbilityJob;
import com.codeboy.qianghongbao.webview.AccessibilityEventListener;
import com.codeboy.qianghongbao.webview.AccessibilityEventProcessor;
import com.codeboy.qianghongbao.webview.ProcessorEventQueue;
import com.codeboy.qianghongbao.webview.ProcessorWebContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class QiangHongBaoService extends AccessibilityService {
    private static final String TAG = "QiangHongBao";
    private static final Class[] ACCESSBILITY_JOBS = new Class[]{WechatAccessbilityJob.class};
    public static QiangHongBaoService service;
    private List<AccessbilityJob> mAccessbilityJobs;
    private HashMap<String, AccessbilityJob> mPkgAccessbilityJobMap;
    private AccessibilityEventProcessor mAccessibilityEventProcessor;
    public QiangHongBaoService() {
    }

    public void onCreate() {
        super.onCreate();
        mAccessibilityEventProcessor = new AccessibilityEventProcessor(this);
        this.mAccessbilityJobs = new ArrayList();
        this.mPkgAccessbilityJobMap = new HashMap();
        Class[] var1 = ACCESSBILITY_JOBS;
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            Class clazz = var1[var3];

            try {
                Object e = clazz.newInstance();
                if(e instanceof AccessbilityJob) {
                    AccessbilityJob job = (AccessbilityJob)e;
                    job.onCreateJob(this);
                    this.mAccessbilityJobs.add(job);
                    this.mPkgAccessbilityJobMap.put(job.getTargetPackageName(), job);
                }
            } catch (Exception var7) {
                var7.printStackTrace();
            }
        }
//        initializeInfrastructure();
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d("QiangHongBao", "qianghongbao service destory");
        if(this.mPkgAccessbilityJobMap != null) {
            this.mPkgAccessbilityJobMap.clear();
        }

        if(this.mAccessbilityJobs != null && !this.mAccessbilityJobs.isEmpty()) {
            Iterator intent = this.mAccessbilityJobs.iterator();

            while(intent.hasNext()) {
                AccessbilityJob job = (AccessbilityJob)intent.next();
                job.onStopJob();
            }

            this.mAccessbilityJobs.clear();
        }

        service = null;
        this.mAccessbilityJobs = null;
        this.mPkgAccessbilityJobMap = null;
        Intent intent1 = new Intent("com.codeboy.qianghongbao.ACCESSBILITY_DISCONNECT");
        this.sendBroadcast(intent1);
    }

    public void onInterrupt() {
        Log.d("QiangHongBao", "qianghongbao service interrupt");
    }
    private void initializeInfrastructure() {
        ProcessorEventQueue processorEventQueue = new ProcessorEventQueue(this);
        mAccessibilityEventProcessor.addAccessibilityEventListener(processorEventQueue);
        addEventListener(new ProcessorWebContent(this));
//        processorEventQueue.setTestingListener(mAccessibilityEventProcessor.getTestingListener());
//        mAccessibilityEventProcessor.setProcessorEventQueue(processorEventQueue);
//
//        addEventListener(processorEventQueue);
//        addEventListener(new ProcessorScrollPosition(mFullScreenReadController,
//                mSpeechController, mCursorController, this));
//        addEventListener(new ProcessorAccessibilityHints(this, mSpeechController));
//        addEventListener(new ProcessorPhoneticLetters(this, mSpeechController));
//        mProcessorScreen = new ProcessorScreen(this);
//        addEventListener(mProcessorScreen);
        final AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        info.feedbackType |= AccessibilityServiceInfo.FEEDBACK_SPOKEN;
        info.feedbackType |= AccessibilityServiceInfo.FEEDBACK_AUDIBLE;
        info.feedbackType |= AccessibilityServiceInfo.FEEDBACK_HAPTIC;
        info.flags |= AccessibilityServiceInfo.DEFAULT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            info.flags |= AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY;
            info.flags |= AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS;
            info.flags |= AccessibilityServiceInfo.FLAG_REQUEST_FILTER_KEY_EVENTS;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            info.flags |= AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS;
        }
        info.notificationTimeout = 0;

        setServiceInfo(info);
    }
    protected void onServiceConnected() {
        super.onServiceConnected();
        service = this;
//        Intent intent = new Intent("com.codeboy.qianghongbao.ACCESSBILITY_CONNECT");
//        this.sendBroadcast(intent);
        requestWebScripts(true);
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void requestWebScripts(boolean requestedState) {
        final AccessibilityServiceInfo info = getServiceInfo();
        if (info == null) {
            return;
        }

        final boolean currentState = (
                (info.flags & AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY)
                        != 0);
        if (currentState == requestedState) {
            return;
        }

        if (requestedState) {
            info.flags |= AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY;
        } else {
            info.flags &= ~AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY;
        }

        setServiceInfo(info);
    }
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if(BuildConfig.DEBUG) {
            Log.d("QiangHongBao", "事件--->" + event);
        }

        String pkn = String.valueOf(event.getPackageName());
        if(this.mAccessbilityJobs != null && !this.mAccessbilityJobs.isEmpty()) {
            if(!this.getConfig().isAgreement()) {
                return;
            }

            Iterator var3 = this.mAccessbilityJobs.iterator();

            while(var3.hasNext()) {
                AccessbilityJob job = (AccessbilityJob)var3.next();
                if(pkn.equals(job.getTargetPackageName()) && job.isEnable()) {
                    job.onReceiveJob(event);
                }
            }
        }
        mAccessibilityEventProcessor.onAccessibilityEvent(event);
    }

    public Config getConfig() {
        return Config.getConfig(this);
    }

    @TargetApi(18)
    public static void handeNotificationPosted(IStatusBarNotification notificationService) {
        if(notificationService != null) {
            if(service != null && service.mPkgAccessbilityJobMap != null) {
                String pack = notificationService.getPackageName();
                AccessbilityJob job = (AccessbilityJob)service.mPkgAccessbilityJobMap.get(pack);
                if(job != null) {
                    job.onNotificationPosted(notificationService);
                }
            }
        }
    }

    @TargetApi(16)
    public static boolean isRunning() {
        return  service !=null;
//        if(service == null) {
//            return false;
//        } else {
//            AccessibilityManager accessibilityManager = (AccessibilityManager)service.getSystemService("accessibility");
//            AccessibilityServiceInfo info = service.getServiceInfo();
//            if(info == null) {
//                return false;
//            } else {
//                List list = accessibilityManager.getEnabledAccessibilityServiceList(16);
//                Iterator iterator = list.iterator();
//                boolean isConnect = false;
//
//                while(iterator.hasNext()) {
//                    AccessibilityServiceInfo i = (AccessibilityServiceInfo)iterator.next();
//                    if(i.getId().equals(info.getId())) {
//                        isConnect = true;
//                        break;
//                    }
//                }
//
//                return isConnect;
//            }
//        }
    }

    public static boolean isNotificationServiceRunning() {
        if(VERSION.SDK_INT < 18) {
            return false;
        } else {
            try {
                return QHBNotificationService.isRunning();
            } catch (Throwable var1) {
                return false;
            }
        }
    }
    public void addEventListener(AccessibilityEventListener listener) {
        mAccessibilityEventProcessor.addAccessibilityEventListener(listener);
    }
}
