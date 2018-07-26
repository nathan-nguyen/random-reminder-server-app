package com.noiprocs.gnik.randomreminder.layout.setting;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.Switch;

import com.noiprocs.gnik.randomreminder.R;
import com.noiprocs.gnik.randomreminder.RandomReminderConstant;
import com.noiprocs.gnik.randomreminder.RandomReminderUtil;
import com.noiprocs.gnik.randomreminder.service.RandomReminderReceiver;

public class SettingConstraintLayout extends ConstraintLayout {

    private AlarmManager mAlarmManager;
    private PendingIntent mPendingIntent;
    private IntervalNumberPicker mIntervalNumberPicker;

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
        Switch mSwitch = findViewById(R.id.main_setting_display_edge_switch);
        Switch mInactivateNodeSwitch = findViewById(R.id.main_setting_display_inactivate_node_switch);
        Switch mVibrationSwitch = findViewById(R.id.main_setting_vibration_node_switch);
        mIntervalNumberPicker = findViewById(R.id.main_setting_notification_numberPicker);

        mIntervalNumberPicker.setMinValue(RandomReminderConstant.MIN_NOTIFICATION_INTERVAL);
        mIntervalNumberPicker.setMaxValue(RandomReminderConstant.MAX_NOTIFICATION_INTERVAL);
        int currentInterval = RandomReminderUtil.getInt(getResources().getString(R.string.key_notification_interval));
        if (currentInterval == 0) {
            currentInterval = RandomReminderConstant.DEFAULT_MINUTE_NOTIFICATION;
        }
        mIntervalNumberPicker.setValue(currentInterval);

        // Set initial value for mSwitch and mInactivateNodeSwitch
        mSwitch.setChecked(RandomReminderUtil.getBoolean(getResources().getString(R.string.key_display_edge)));
        mInactivateNodeSwitch.setChecked(RandomReminderUtil.getBoolean(getResources().getString(R.string.key_display_inactivate_node)));
        mVibrationSwitch.setChecked(RandomReminderUtil.getBoolean(getResources().getString(R.string.key_vibration)));

        this.setupService();

        mSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> RandomReminderUtil.setBoolean(getResources().getString(R.string.key_display_edge), isChecked));
        mInactivateNodeSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> RandomReminderUtil.setBoolean(getResources().getString(R.string.key_display_inactivate_node), isChecked));
        mVibrationSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> RandomReminderUtil.setBoolean(getResources().getString(R.string.key_vibration), isChecked));

        mIntervalNumberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            RandomReminderUtil.setInt(getResources().getString(R.string.key_notification_interval), newVal);
            mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), RandomReminderConstant.MINUTE_COUNT * newVal, mPendingIntent);
        });
    }

    private void setupService() {
        Intent notifyIntent = new Intent(getContext(), RandomReminderReceiver.class);
        mPendingIntent = PendingIntent.getBroadcast(getContext(), 0 /* Request code */, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                RandomReminderConstant.MINUTE_COUNT * mIntervalNumberPicker.getValue(), mPendingIntent);
    }
}
