package com.codeboy.qianghongbao.util;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by colincao on 4/6/2017.
 */
class CustomTouchView extends View {

    public CustomTouchView(Context context) {
        super(context);
    }

    boolean mDownTouch = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        // Listening for the down and up touch events
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownTouch = true;
                return true;

            case MotionEvent.ACTION_UP:
                if (mDownTouch) {
                    mDownTouch = false;
                    performClick(); // Call this method to handle the response, and
                    // thereby enable accessibility services to
                    // perform this action for a user who cannot
                    // click the touchscreen.
                    return true;
                }
        }
        return false; // Return false for other touch events
    }

    @Override
    public boolean performClick() {
        // Calls the super implementation, which generates an AccessibilityEvent
        // and calls the onClick() listener on the view, if any
        super.performClick();

        // Handle the action for the custom click here

        return true;
    }
}
