package com.codeboy.qianghongbao.webview;

import android.content.res.Resources;

import com.codeboy.qianghongbao.QiangHongBaoService;

import org.w3c.dom.Document;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by snsoft on 26/7/2017.
 */

public class EventSpeechRuleProcessor {
    /**
     * Context for accessing resources.
     */
    private final QiangHongBaoService mContext;
    /** A lazily-constructed shared instance of a document builder. */
    private DocumentBuilder mDocumentBuilder;
    /** Mapping from package name to speech rules for that package. */
    private final Map<String, List<EventSpeechRule>> mPackageNameToSpeechRulesMap = new HashMap<>();
    public EventSpeechRuleProcessor(QiangHongBaoService context) {
        mContext = context;
    }

    public void addSpeechStrategy(int resourceId) {
        final Resources res = mContext.getResources();
        final String speechStrategy = res.getResourceName(resourceId);
        final InputStream inputStream = res.openRawResource(resourceId);
        final Document document = parseSpeechStrategy(inputStream);
        final ArrayList<EventSpeechRule> speechRules = EventSpeechRule.createSpeechRules(
                mContext, document);

        final int added = addSpeechStrategy(speechRules);

//        LogUtils.log(EventSpeechRuleProcessor.class, Log.INFO, "%d speech rules appended from: %s",
//                added, speechStrategy);
    }
    private Document parseSpeechStrategy(InputStream inputStream) {
        try {
            final DocumentBuilder builder = getDocumentBuilder();
            return builder.parse(inputStream);
        } catch (Exception e) {
//            LogUtils.log(EventSpeechRuleProcessor.class, Log.ERROR,
//                    "Could not open speechstrategy xml file\n%s", e.toString());
        }

        return null;
    }
    public int addSpeechStrategy(Iterable<EventSpeechRule> speechRules) {
        int count = 0;

        synchronized (mPackageNameToSpeechRulesMap) {
            for (EventSpeechRule speechRule : speechRules) {
                if (addSpeechRuleLocked(speechRule)) {
                    count++;
                }
            }
        }

        return count;
    }
    /**
     * @return A lazily-constructed shared instance of a document builder.
     * @throws ParserConfigurationException
     */
    private DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
        if (mDocumentBuilder == null) {
            mDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        }

        return mDocumentBuilder;
    }
    /**
     * Adds a <code>speechRule</code>.
     */
    private boolean addSpeechRuleLocked(EventSpeechRule speechRule) {
        final String packageName = speechRule.getPackageName();

        List<EventSpeechRule> packageSpeechRules = mPackageNameToSpeechRulesMap.get(packageName);

        if (packageSpeechRules == null) {
            packageSpeechRules = new LinkedList<>();
            mPackageNameToSpeechRulesMap.put(packageName, packageSpeechRules);
        }

        return packageSpeechRules.add(speechRule);
    }
}
