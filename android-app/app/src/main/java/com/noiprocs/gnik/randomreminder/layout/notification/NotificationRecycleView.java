package com.noiprocs.gnik.randomreminder.layout.notification;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.Switch;
import android.widget.Toast;

import com.noiprocs.gnik.randomreminder.R;
import com.noiprocs.gnik.randomreminder.RandomReminderUtil;
import com.noiprocs.gnik.randomreminder.adapter.DataAdapter;
import com.noiprocs.gnik.randomreminder.model.Edge;
import com.noiprocs.gnik.randomreminder.model.Leaf;
import com.noiprocs.gnik.randomreminder.model.Node;
import com.noiprocs.gnik.randomreminder.sqlite.RandomReminder;

import java.util.List;

public class NotificationRecycleView extends RecyclerView {

    private RandomReminder mRandomReminder;
    private List<Node> mDataSet;
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

        mDataSet = mRandomReminder.getData(RandomReminderUtil.getBoolean(getResources().getString(R.string.key_display_edge)), RandomReminderUtil.getBoolean(getResources().getString(R.string.key_display_inactivate_node)));
        mDataAdapter = new DataAdapter(mDataSet);
        this.setAdapter(mDataAdapter);

        this.registerEvent();
    }

    private void registerEvent() {
        mDataAdapter.setOnDeleteButtonClick(this::showDeleteItemDialog);

        mDataAdapter.setOnViewItemClick(this::displayItemInfoDialog);
    }

    private void deleteData(Node node) {
        int result = -1;
        if (node instanceof Edge) {
            result = mRandomReminder.deleteEdge((Edge) node);
        }
        else if (node instanceof Leaf) {
            result = mRandomReminder.deleteLeaf((Leaf) node);
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
        mDataSet.addAll(mRandomReminder.getData(RandomReminderUtil.getBoolean(getResources().getString(R.string.key_display_edge)), RandomReminderUtil.getBoolean(getResources().getString(R.string.key_display_inactivate_node))));
        mDataAdapter.notifyDataSetChanged();
    }

    private void showDeleteItemDialog(Node node) {
        new AlertDialog.Builder(this.getContext())
                .setTitle(node.getParent())
                .setMessage("Do you want to delete:\n" + node.getValue())
                .setIcon(R.drawable.ic_delete_forever_black_24dp)
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> deleteData(node))
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void displayItemInfoDialog(Node node) {
        Dialog dialog = new AlertDialog.Builder(this.getContext())
                .setView(R.layout.main_notification_item_dialog).setTitle(node.getParent())
                .setMessage(node.getValue())
                .setIcon(R.drawable.ic_label_black_24dp).show();

        ((Switch) dialog.findViewById(R.id.main_notification_item_dialog_switch)).setChecked(node.isActivate());
        ((Switch) dialog.findViewById(R.id.main_notification_item_dialog_switch)).setOnCheckedChangeListener((compoundButton, isChecked) -> {
            node.setActivate(isChecked ? 1 : 0);
            // TODO: Change doesn't reflex immediately in DAG graph
            mRandomReminder.updateNode(node);
            reloadRecycleView();
        });
        dialog.findViewById(R.id.main_notification_item_dialog_delete_button).setOnClickListener((v) -> {
            showDeleteItemDialog(node);
            dialog.dismiss();
        });
    }
}
