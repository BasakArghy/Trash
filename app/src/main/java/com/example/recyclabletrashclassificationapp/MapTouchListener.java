package com.example.recyclabletrashclassificationapp;

// MapTouchListener.java
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class MapTouchListener extends FrameLayout {

    public MapTouchListener(Context context) {
        super(context);
    }

    public MapTouchListener(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MapTouchListener(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // Disallow parent views to intercept touch events
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }
}

