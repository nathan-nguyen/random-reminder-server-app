package com.noiprocs.gnik.randomreminder;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.noiprocs.gnik.randomreminder.adapter.DataAdapter;
import com.noiprocs.gnik.randomreminder.core.MemoryAiderException;
import com.noiprocs.gnik.randomreminder.service.RandomReminderReceiver;
import com.noiprocs.gnik.randomreminder.sqlite.RandomReminder;

import java.util.List;

public class MainReminderActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private RandomReminder mMemoryAider;
    private Button mRandomButton;
    private AutoCompleteTextView mParentEditText;
    private EditText mChildContentEditText;
    private Button mAddLeafButton;
    private Button mAddTagButton;
    private RecyclerView mRecycleView;
    private List<String> mDataSet;
    private DataAdapter mDataAdapter;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        toggleUI(item.getItemId());
        return true;
    };

    private void toggleUI(int mode){
        if (mode == R.id.navigation_home) {
            mTextMessage.setVisibility(View.VISIBLE);
            mTextMessage.setText("Random a note");
            mRandomButton.setVisibility(View.VISIBLE);
            this.loadMemoryAider();
        }
        else {
            mRandomButton.setVisibility(View.GONE);
            mTextMessage.setVisibility(View.GONE);
        }

        if (mode == R.id.navigation_add_leaf || mode == R.id.navigation_add_tag) {
            mParentEditText.setVisibility(View.VISIBLE);
            mChildContentEditText.setVisibility(View.VISIBLE);
            mParentEditText.setAdapter(new ArrayAdapter<>(this, android.R.layout.select_dialog_item, mMemoryAider.getTagList()));
        }
        else {
            mParentEditText.setVisibility(View.GONE);
            mChildContentEditText.setVisibility(View.GONE);
        }

        if (mode == R.id.navigation_add_leaf) {
            mAddLeafButton.setVisibility(View.VISIBLE);
        }
        else {
            mAddLeafButton.setVisibility(View.GONE);
        }

        if (mode == R.id.navigation_add_tag) {
            mAddTagButton.setVisibility(View.VISIBLE);
        }
        else {
            mAddTagButton.setVisibility(View.GONE);
        }

        if (mode == R.id.navigation_notifications) {
            mRecycleView.setVisibility(View.VISIBLE);
            reloadRecycleView();
        }
        else {
            mRecycleView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_reminder);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mTextMessage = findViewById(R.id.message);
        mParentEditText = findViewById(R.id.parentEditText);
        mChildContentEditText = findViewById(R.id.childContentEditText);

        mRandomButton = findViewById(R.id.randomButton);
        mAddLeafButton = findViewById(R.id.addLeafButton);
        mAddTagButton = findViewById(R.id.addTagButton);
        mRecycleView = findViewById(R.id.recycleView);

        toggleUI(R.id.navigation_home);

        if (!loadMemoryAider()) return;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecycleView.setLayoutManager(linearLayoutManager);
        mDataSet = mMemoryAider.getData();
        mDataAdapter = new DataAdapter(mDataSet);
        mRecycleView.setAdapter(mDataAdapter);

        this.registerEvent();
        this.setupService();
    }

    private void registerEvent() {
        mRandomButton.setOnClickListener((v) -> {
            try {
                mTextMessage.setText(mMemoryAider.getRandomLeaf());
            } catch (MemoryAiderException e) {
                mTextMessage.setText(e.toString());
            }
        });

        mAddLeafButton.setOnClickListener((v) -> {
            try {
                long rowId = mMemoryAider.addContent(mParentEditText.getText().toString(), mChildContentEditText.getText().toString());
                if (rowId > 0) {
                    Toast.makeText(getApplicationContext(), "Note added successfully!", Toast.LENGTH_SHORT).show();
                    mParentEditText.getText().clear();
                    mChildContentEditText.getText().clear();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Cannot insert note into database!", Toast.LENGTH_SHORT).show();
                }
            } catch (MemoryAiderException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Tag does not exist! Please create tag before adding note", Toast.LENGTH_SHORT).show();
            }
            this.hideKeyboard();
        });

        mAddTagButton.setOnClickListener((v) -> {
            long rowId = mMemoryAider.addTag(mParentEditText.getText().toString(), mChildContentEditText.getText().toString());
            if (rowId > 0) {
                Toast.makeText(getApplicationContext(), "Tag added successfully!", Toast.LENGTH_SHORT).show();
                mParentEditText.getText().clear();
                mChildContentEditText.getText().clear();
            }
            else {
                Toast.makeText(getApplicationContext(), "Cannot insert duplicate primary key parent-child", Toast.LENGTH_SHORT);
            }
            this.hideKeyboard();
        });

        mDataAdapter.setOnButtonClick((v) -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete")
                    .setMessage("Do you really want to delete:\n" + v[v.length - 2] + " - " + v[v.length - 1])
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> deleteData(v))
                    .setNegativeButton(android.R.string.no, null).show();
        });
    }

    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void reloadRecycleView(){
        mDataSet.clear();
        mDataSet.addAll(mMemoryAider.getData());
        mDataAdapter.notifyDataSetChanged();
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

    private void setupService() {
        Intent notifyIntent = new Intent(this,RandomReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast
                (getApplicationContext(), 0 /* Request code */, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,  System.currentTimeMillis(),
                1000 * 60 * 15, pendingIntent);
    }

    private void deleteData(String[] v) {
        int result = -1;
        if (v.length == 2) {
            result = mMemoryAider.deleteEdge(v);
        }
        else if (v.length == 3) {
            result = mMemoryAider.deleteLeaf(v[0], v[1]);
        }

        if (result > 0) {
            Toast.makeText(getApplicationContext(), "Record deleted successfully!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "Error when trying to delete record!", Toast.LENGTH_SHORT).show();
        }

        reloadRecycleView();
    }
}
