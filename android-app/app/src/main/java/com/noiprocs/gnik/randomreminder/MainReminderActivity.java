package com.noiprocs.gnik.randomreminder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.noiprocs.gnik.randomreminder.core.MemoryAiderException;
import com.noiprocs.gnik.randomreminder.layout.notification.NotificationRecycleView;
import com.noiprocs.gnik.randomreminder.layout.setting.SettingConstraintLayout;
import com.noiprocs.gnik.randomreminder.sqlite.RandomReminder;

public class MainReminderActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private RandomReminder mMemoryAider;
    private Button mRandomButton;
    private AutoCompleteTextView mParentEditText;
    private EditText mChildContentEditText;
    private Button mAddLeafButton;
    private Button mAddTagButton;
    private NotificationRecycleView mNotificationRecycleView;
    private SettingConstraintLayout mSettingConstraintLayout;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        toggleUI(item.getItemId());
        return true;
    };

    private void toggleUI(int mode) {
        if (mode == R.id.navigation_home) {
            mTextMessage.setVisibility(View.VISIBLE);
            mTextMessage.setText(getResources().getText(R.string.main_home_info_label_default_text));
            mRandomButton.setVisibility(View.VISIBLE);
            this.loadMemoryAider();
        } else {
            mRandomButton.setVisibility(View.GONE);
            mTextMessage.setVisibility(View.GONE);
        }

        if (mode == R.id.navigation_add_leaf || mode == R.id.navigation_add_tag) {
            mParentEditText.setVisibility(View.VISIBLE);
            mChildContentEditText.setVisibility(View.VISIBLE);
            mParentEditText.setAdapter(new ArrayAdapter<>(this, android.R.layout.select_dialog_item, mMemoryAider.getTagList()));
        } else {
            mParentEditText.setVisibility(View.GONE);
            mChildContentEditText.setVisibility(View.GONE);
        }

        if (mode == R.id.navigation_add_leaf) {
            mAddLeafButton.setVisibility(View.VISIBLE);
        } else {
            mAddLeafButton.setVisibility(View.GONE);
        }

        if (mode == R.id.navigation_add_tag) {
            mAddTagButton.setVisibility(View.VISIBLE);
        } else {
            mAddTagButton.setVisibility(View.GONE);
        }

        if (mode == R.id.navigation_notifications) {
            mNotificationRecycleView.setVisibility(View.VISIBLE);
            mNotificationRecycleView.reloadRecycleView();
        } else {
            mNotificationRecycleView.setVisibility(View.GONE);
        }

        if (mode == R.id.navigation_setting) {
            mSettingConstraintLayout.setVisibility(View.VISIBLE);
        } else {
            mSettingConstraintLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_reminder);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        RandomReminderUtil.initializedPerference(this);

        mTextMessage = findViewById(R.id.message);
        mParentEditText = findViewById(R.id.parentEditText);
        mChildContentEditText = findViewById(R.id.childContentEditText);

        mRandomButton = findViewById(R.id.randomButton);
        mAddLeafButton = findViewById(R.id.addLeafButton);
        mAddTagButton = findViewById(R.id.addTagButton);
        mNotificationRecycleView = findViewById(R.id.recycleView);
        mSettingConstraintLayout = findViewById(R.id.main_setting_layout);

        toggleUI(R.id.navigation_home);

        if (!loadMemoryAider()) return;

        mNotificationRecycleView.initializeLayout(mMemoryAider);
        mSettingConstraintLayout.initializeLayout();

        this.registerEvent();
    }

    @SuppressLint("ShowToast")
    private void registerEvent() {
        mRandomButton.setOnClickListener((v) -> {
            try {
                mTextMessage.setText(mMemoryAider.getRandomLeaf());
            } catch (MemoryAiderException e) {
                mTextMessage.setText(e.toString());
            }
        });

        mAddLeafButton.setOnClickListener((v) -> {
            long rowId = mMemoryAider.addContent(mParentEditText.getText().toString(), mChildContentEditText.getText().toString());
            if (rowId > 0) {
                Toast.makeText(getApplicationContext(), "Note added successfully!", Toast.LENGTH_SHORT).show();
                mParentEditText.getText().clear();
                mChildContentEditText.getText().clear();
            } else {
                Toast.makeText(getApplicationContext(), "Cannot insert note into database!", Toast.LENGTH_SHORT).show();
            }
            this.hideKeyboard();
        });

        mAddTagButton.setOnClickListener((v) -> {
            long rowId = mMemoryAider.addTag(mParentEditText.getText().toString(), mChildContentEditText.getText().toString());
            if (rowId > 0) {
                Toast.makeText(getApplicationContext(), "Tag added successfully!", Toast.LENGTH_SHORT).show();
                mParentEditText.getText().clear();
                mChildContentEditText.getText().clear();
            } else {
                Toast.makeText(getApplicationContext(), "Cannot insert duplicate primary key parent-child", Toast.LENGTH_SHORT);
            }
            this.hideKeyboard();
        });


    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view != null && imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private boolean loadMemoryAider() {
        try {
            mMemoryAider = new RandomReminder(this.getApplicationContext());
            mMemoryAider.loadData();
            return true;
        } catch (Exception e) {
            mTextMessage.setText(e.toString());
            return false;
        }
    }
}
