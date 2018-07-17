package com.noiprocs.gnik.randomreminder.setting;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.Switch;

import com.noiprocs.gnik.randomreminder.R;
import com.noiprocs.gnik.randomreminder.RandomReminderUtil;

public class SettingConstraintLayout extends ConstraintLayout {

    private Switch mSwitch;

    public SettingConstraintLayout(Context context) {
        super(context);
    }

    public SettingConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SettingConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initializeLayout() {
        mSwitch = findViewById(R.id.main_setting_display_edge_switch);
        mSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            RandomReminderUtil.setBoolean(getResources().getString(R.string.key_display_edge), isChecked);
        });
    }
}
