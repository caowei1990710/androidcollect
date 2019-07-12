package com.codeboy.qianghongbao.webview;

import android.view.accessibility.AccessibilityEvent;

import com.codeboy.qianghongbao.QiangHongBaoService;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

/**
 * Created by snsoft on 26/7/2017.
 */

public class EventSpeechRule {
    // Node names.
    private static final String NODE_NAME_METADATA = "metadata";
    private static final String NODE_NAME_FILTER = "filter";
    private static final String NODE_NAME_FORMATTER = "formatter";
    private static final String NODE_NAME_CUSTOM = "custom";

    // Property names.
    private static final String PROPERTY_EVENT_TYPE = "eventType";
    private static final String PROPERTY_PACKAGE_NAME = "packageName";
    private static final String PROPERTY_CLASS_NAME = "className";
    private static final String PROPERTY_CLASS_NAME_STRICT = "classNameStrict";
    private static final String PROPERTY_TEXT = "text";
    private static final String PROPERTY_BEFORE_TEXT = "beforeText";
    private static final String PROPERTY_CONTENT_DESCRIPTION = "contentDescription";
    private static final String PROPERTY_CONTENT_DESCRIPTION_OR_TEXT = "contentDescriptionOrText";
    private static final String PROPERTY_NODE_DESCRIPTION_OR_FALLBACK = "nodeDescriptionOrFallback";
    private static final String PROPERTY_EVENT_TIME = "eventTime";
    private static final String PROPERTY_ITEM_COUNT = "itemCount";
    private static final String PROPERTY_CURRENT_ITEM_INDEX = "currentItemIndex";
    private static final String PROPERTY_FROM_INDEX = "fromIndex";
    private static final String PROPERTY_TO_INDEX = "toIndex";
    private static final String PROPERTY_SCROLLABLE = "scrollable";
    private static final String PROPERTY_SCROLL_X = "scrollX";
    private static final String PROPERTY_SCROLL_Y = "scrollY";
    private static final String PROPERTY_RECORD_COUNT = "recordCount";
    private static final String PROPERTY_CHECKED = "checked";
    private static final String PROPERTY_ENABLED = "enabled";
    private static final String PROPERTY_FULL_SCREEN = "fullScreen";
    private static final String PROPERTY_PASSWORD = "password";
    private static final String PROPERTY_ADDED_COUNT = "addedCount";
    private static final String PROPERTY_REMOVED_COUNT = "removedCount";
    private static final String PROPERTY_QUEUING = "queuing";
    private static final String PROPERTY_EARCON = "earcon";
    private static final String PROPERTY_VIBRATION = "vibration";
    private static final String PROPERTY_CUSTOM_EARCON = "customEarcon";
    private static final String PROPERTY_CUSTOM_VIBRATION = "customVibration";
    private static final String PROPERTY_VERSION_CODE = "versionCode";
    private static final String PROPERTY_VERSION_NAME = "versionName";
    private static final String PROPERTY_PLATFORM_RELEASE = "platformRelease";
    private static final String PROPERTY_PLATFORM_SDK = "platformSdk";
    private static final String PROPERTY_SKIP_DUPLICATES = "skipDuplicates";

    // Property types.
    private static final int PROPERTY_TYPE_UNKNOWN = 0;
    private static final int PROPERTY_TYPE_BOOLEAN = 1;
    private static final int PROPERTY_TYPE_FLOAT = 2;
    private static final int PROPERTY_TYPE_INTEGER = 3;
    private static final int PROPERTY_TYPE_STRING = 4;
    /** The context in which this speech rule operates. */
    private final QiangHongBaoService mContext;
    /** The package targeted by this rule. */
    private String mPackageName = UNDEFINED_PACKAGE_NAME;
    private static final String UNDEFINED_PACKAGE_NAME = "undefined_package_name";

    public String getPackageName() {
        return mPackageName;
    }

    public void setmPackageName(String mPackageName) {
        this.mPackageName = mPackageName;
    }

    /**
     * The DOM node that defines this speech rule, or {@code null} if the node
     * is no longer needed.
     */
    private Node mNode;
    public static ArrayList<EventSpeechRule> createSpeechRules(QiangHongBaoService context,
                                                               Document document) throws IllegalStateException {
        final ArrayList<EventSpeechRule> speechRules = new ArrayList<>();

        if (document == null || context == null) {
            return speechRules;
        }

        final NodeList children = document.getDocumentElement().getChildNodes();

        for (int i = 0, count = children.getLength(); i < count; i++) {
            final Node child = children.item(i);

            if (child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            try {
                final EventSpeechRule rule = new EventSpeechRule(context, child, i);

                speechRules.add(rule);
            } catch (Exception e) {
                e.printStackTrace();

//                LogUtils.log(EventSpeechRule.class, Log.WARN, "Failed loading speech rule: %s",
//                        getTextContent(child));
            }
        }

        return speechRules;
    }

    private EventSpeechRule(QiangHongBaoService context, Node node, int ruleIndex) {
        mContext = context;
        mNode = node;

        AccessibilityEventFilter filter = null;
        AccessibilityEventFormatter formatter = null;

        // Avoid call to Document#getNodesByTagName, since it traverses the
        // entire document.
        final NodeList children = node.getChildNodes();

        for (int i = 0, count = children.getLength(); i < count; i++) {
            final Node child = children.item(i);

            if (child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            final String nodeName = getUnqualifiedNodeName(child);

//            if (NODE_NAME_METADATA.equalsIgnoreCase(nodeName)) {
//                populateMetadata(child);
//            } else if (NODE_NAME_FILTER.equals(nodeName)) {
//                filter = createFilter(child);
//            } else if (NODE_NAME_FORMATTER.equals(nodeName)) {
//                formatter = createFormatter(child);
//            }
        }

        if (formatter instanceof ContextBasedRule) {
            ((ContextBasedRule) formatter).initialize(context);
        }

        if (filter instanceof ContextBasedRule) {
            ((ContextBasedRule) filter).initialize(context);
        }

//        mFilter = filter;
//        mFormatter = formatter;
//        mRuleIndex = ruleIndex;
    }
    /**
     * Returns the unqualified <code>node</code> name i.e. without the prefix.
     *
     * @param node The node.
     * @return The unqualified name.
     */
    private static String getUnqualifiedNodeName(Node node) {
        String nodeName = node.getNodeName();
        int colonIndex = nodeName.indexOf(":");
        if (colonIndex > -1) {
            nodeName = nodeName.substring(colonIndex + 1);
        }
        return nodeName;
    }
    /**
     * This interface defines the contract for writing filters. A filter either
     * accepts or rejects an {@link AccessibilityEvent}.
     */
    public interface AccessibilityEventFilter {

        /**
         * Check if the filter accepts a given <code>event</code>.
         *
         * @param event The event.
         * @param context The context to be used for loading resources etc.
         * @return True if the event is accepted, false otherwise.
         */
        boolean accept(AccessibilityEvent event, QiangHongBaoService context);
    }
    public interface AccessibilityEventFormatter {
        /**
         * Formats an <code>utterance</code> form given <code>event</code>.
         *
         * @param event The event.
         * @param context The context to be used for loading resources etc.
         * @param utterance The utterance instance to populate.
         * @return {@code true} if the formatter produced output. Accepting an
         *         event in an {@link AccessibilityEventFilter} and returning
         *         {@code false} from an {@link AccessibilityEventFormatter}
         *         will cause the event to be dropped entirely.
         */
        public boolean format(AccessibilityEvent event, QiangHongBaoService context,
                              Utterance utterance);
    }
    /**
     * This interface is implemented by rules that need initialization.
     */
    public interface ContextBasedRule {
        /**
         * Initializes the rule with the specified context.
         *
         * @param context The parent service.
         */
        public void initialize(QiangHongBaoService context);
    }
}
