package com.codeboy.qianghongbao.webview;

/**
 * Created by snsoft on 26/7/2017.
 */

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

import com.codeboy.qianghongbao.QiangHongBaoService;

import java.util.LinkedList;
import java.util.List;


public class AccessibilityEventProcessor {
    private AccessibilityManager mAccessibilityManager;
    private final QiangHongBaoService mService;
    private List<AccessibilityEventListener> mAccessibilityEventListeners = new LinkedList<>();
    private DelayedEventHandler mHandler = new DelayedEventHandler();
    public AccessibilityEventProcessor(QiangHongBaoService service) {
        mAccessibilityManager =
                (AccessibilityManager) service.getSystemService(Context.ACCESSIBILITY_SERVICE);

        mService = service;
        initDumpEventMask();
    }
    /**
     * Read dump event configuration from preferences.
     */
    private void initDumpEventMask() {
        int[] eventTypes = AccessibilityEventUtils.getAllEventTypes();

    }

    /**
     * Passes the event to all registered {@link AccessibilityEventListener}s in the order
     * they were added.
     *
     * @param event The current event.
     */
    private void processEvent(AccessibilityEvent event) {
        for (AccessibilityEventListener eventProcessor : mAccessibilityEventListeners) {
            eventProcessor.onAccessibilityEvent(event);
        }
    }

    public void onAccessibilityEvent(AccessibilityEvent event) {
        mHandler.postProcessEvent(event);
    }

//    public void addAccessibilityEventListener(ProcessorEventQueue processorEventQueue) {
//        mAccessibilityEventListeners.add(processorEventQueue);
//    }
    public void addAccessibilityEventListener(AccessibilityEventListener listener) {
        mAccessibilityEventListeners.add(listener);
    }

    private class DelayedEventHandler extends Handler {

        public static final int MESSAGE_WHAT_PROCESS_EVENT = 1;

        @Override
        public void handleMessage(Message message) {
            if (message.what != MESSAGE_WHAT_PROCESS_EVENT || message.obj == null) {
                return;
            }

            AccessibilityEvent event = (AccessibilityEvent) message.obj;
            processEvent(event);
            event.recycle();
        }

        public void postProcessEvent(AccessibilityEvent event) {
            AccessibilityEvent eventCopy = AccessibilityEvent.obtain(event);
            Message msg = obtainMessage(MESSAGE_WHAT_PROCESS_EVENT, eventCopy);
            sendMessageDelayed(msg, 150);
        }

    }
}
