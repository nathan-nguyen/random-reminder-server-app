package com.noiprocs.gnik.randomreminder;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.noiprocs.gnik.randomreminder.core.MemoryAider;
import com.noiprocs.gnik.randomreminder.core.MemoryAiderException;

import java.io.InputStream;

public class MainReminder extends AppCompatActivity {

    private TextView mTextMessage;
    private MemoryAider mMemoryAider;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_reminder);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mTextMessage = findViewById(R.id.message);
        Button randomButton = findViewById(R.id.randomButton);

        try {
            mMemoryAider = new MemoryAider();
            InputStream applicationProperties = getBaseContext().getAssets().open(getString(R.string.app_preference));
            mMemoryAider.initializeProperties(applicationProperties);
            applicationProperties.close();

            InputStream dataFileInputStream = getBaseContext().getAssets().open("memoryAider.dat");
            mMemoryAider.loadData(dataFileInputStream);
            dataFileInputStream.close();
        } catch (Exception e) {
            mTextMessage.setText(e.toString());
            return;
        }

        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mTextMessage.setText(mMemoryAider.getRandomLeaf());
                } catch (MemoryAiderException e) {
                    mTextMessage.setText(e.toString());
                }
            }
        });
    }

}
