package com.codeboy.qianghongbao.webview;


import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.codeboy.qianghongbao.QiangHongBaoService;

/**
 * Created by snsoft on 27/7/2017.
 */

public class ProcessorWebContent  implements AccessibilityEventListener{
    private QiangHongBaoService QiangHongBaoService;
    private AccessibilityNodeInfoCompat mLastNode;
    public ProcessorWebContent(QiangHongBaoService QiangHongBaoService) {
        this.QiangHongBaoService = QiangHongBaoService;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        final AccessibilityRecordCompat record = AccessibilityEventCompat.asRecord(event);
        final AccessibilityNodeInfoCompat source = record.getSource();
        mLastNode = source;
        Log.e("mLastNode:",mLastNode.toString());
    }
}
