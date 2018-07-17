package com.noiprocs.gnik.randomreminder.layout.setting;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.NumberPicker;

public class IntervalNumberPicker extends NumberPicker {
    public IntervalNumberPicker(Context context) {
        super(context);
    }

    public IntervalNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IntervalNumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public IntervalNumberPicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
