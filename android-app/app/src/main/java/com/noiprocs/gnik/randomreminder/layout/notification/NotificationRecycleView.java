package com.noiprocs.gnik.randomreminder.layout.notification;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.Toast;

import com.noiprocs.gnik.randomreminder.R;
import com.noiprocs.gnik.randomreminder.RandomReminderUtil;
import com.noiprocs.gnik.randomreminder.adapter.DataAdapter;
import com.noiprocs.gnik.randomreminder.sqlite.RandomReminder;

import java.util.List;

public class NotificationRecycleView extends RecyclerView {

    private RandomReminder mRandomReminder;
    private List<String> mDataSet;
    private DataAdapter mDataAdapter;

    public NotificationRecycleView(Context context) {
        super(context);
    }

    public NotificationRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NotificationRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void initializeLayout(RandomReminder randomReminder) {
        this.mRandomReminder = randomReminder;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        this.setLayoutManager(linearLayoutManager);

        mDataSet = mRandomReminder.getData(RandomReminderUtil.getBoolean(getResources().getString(R.string.key_display_edge)));
        mDataAdapter = new DataAdapter(mDataSet);
        this.setAdapter(mDataAdapter);

        this.registerEvent();
    }

    private void registerEvent() {
        mDataAdapter.setOnDeleteButtonClick((v) -> new AlertDialog.Builder(this.getContext())
                    .setTitle("Delete")
                    .setMessage("Do you really want to delete:\n" + v[v.length - 2] + " - " + v[v.length - 1])
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> deleteData(v))
                    .setNegativeButton(android.R.string.no, null).show());
    }

    private void deleteData(String[] v) {
        int result = -1;
        if (v.length == 2) {
            result = mRandomReminder.deleteEdge(v);
        } else if (v.length == 3) {
            result = mRandomReminder.deleteLeaf(v[0], v[1]);
        }

        if (result > 0) {
            Toast.makeText(getContext(), "Record deleted successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Error when trying to delete record!", Toast.LENGTH_SHORT).show();
        }

        reloadRecycleView();
    }

    public void reloadRecycleView() {
        mDataSet.clear();
        mDataSet.addAll(mRandomReminder.getData(RandomReminderUtil.getBoolean(getResources().getString(R.string.key_display_edge))));
        mDataAdapter.notifyDataSetChanged();
    }
}
