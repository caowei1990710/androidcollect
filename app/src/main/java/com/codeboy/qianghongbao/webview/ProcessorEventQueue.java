package com.codeboy.qianghongbao.webview;

import android.view.accessibility.AccessibilityEvent;

import com.codeboy.qianghongbao.R;
import com.codeboy.qianghongbao.QiangHongBaoService;

/**
 * Created by snsoft on 26/7/2017.
 */

public class ProcessorEventQueue implements AccessibilityEventListener {
    /**
     * Processor for {@link AccessibilityEvent}s that populates
     * {@link Utterance}s.
     */
    private EventSpeechRuleProcessor mEventSpeechRuleProcessor;
    private QiangHongBaoService mContext;
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }
    public ProcessorEventQueue( QiangHongBaoService context) {

        mEventSpeechRuleProcessor = new EventSpeechRuleProcessor(context);
        mContext = context;

        loadDefaultRules();
    }

    private void loadDefaultRules() {
        // Add version-specific speech strategies for semi-bundled apps.
//        mEventSpeechRuleProcessor.addSpeechStrategy(R.raw.speechstrategy_apps);
//        mEventSpeechRuleProcessor.addSpeechStrategy(R.raw.speechstrategy_googletv);
//
//        // Add platform-specific speech strategies for bundled apps.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            mEventSpeechRuleProcessor.addSpeechStrategy(R.raw.speechstrategy_kitkat);
//        }

        // Add generic speech strategy. This should always be added last so that
        // the app-specific rules above can override the generic rules.
//        mEventSpeechRuleProcessor.addSpeechStrategy(R.raw.speechstrategy);
    }
}
