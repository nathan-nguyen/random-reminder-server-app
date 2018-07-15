package com.noiprocs.gnik.randomreminder;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.noiprocs.gnik.randomreminder.core.MemoryAiderException;
import com.noiprocs.gnik.randomreminder.sqlite.RandomReminder;

public class MainReminder extends AppCompatActivity {

    private TextView mTextMessage;
    private RandomReminder mMemoryAider;
    private Button mRandomButton;
    private EditText mParentEditText;
    private EditText mChildContentEditText;
    private Button mAddLeafButton;
    private Button mAddTagButton;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                toggleUI(item.getItemId());
                return true;
            };

    private void toggleUI(int mode){
        if (mode == R.id.navigation_home || mode == R.id.navigation_notifications) {
            mTextMessage.setVisibility(View.VISIBLE);
        }
        else {
            mTextMessage.setVisibility(View.GONE);
        }

        if (mode == R.id.navigation_home) {
            mTextMessage.setText("Random a note");
            mRandomButton.setVisibility(View.VISIBLE);
        }
        else {
            mRandomButton.setVisibility(View.GONE);
        }

        if (mode == R.id.navigation_add_leaf || mode == R.id.navigation_add_tag) {
            mParentEditText.setVisibility(View.VISIBLE);
            mChildContentEditText.setVisibility(View.VISIBLE);
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
            mTextMessage.setText("Querying Data ...");
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

        toggleUI(R.id.navigation_home);

        try {
            this.getBaseContext().deleteDatabase("memory_aider_database");
            mMemoryAider = new RandomReminder(this.getApplicationContext());
            mMemoryAider.loadData();
        } catch (Exception e) {
            mTextMessage.setText(e.toString());
            return;
        }

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
    }

    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
