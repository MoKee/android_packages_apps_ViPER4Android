package com.vipercn.viper4android_v2.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class SwitchCompat extends android.support.v7.widget.SwitchCompat {
    public SwitchCompat(Context context) {
        super(context);
    }

    public SwitchCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwitchCompat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
