package com.noiprocs.gnik.randomreminder.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.noiprocs.gnik.randomreminder.core.MemoryAider;
import com.noiprocs.gnik.randomreminder.core.MemoryAiderException;
import com.noiprocs.gnik.randomreminder.model.Edge;
import com.noiprocs.gnik.randomreminder.model.Leaf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class RandomReminder extends MemoryAider {
    private final String TAG = RandomReminder.class.getCanonicalName();

    private final Context mContext;
    private List<Edge> edgeList = new ArrayList<>();
    private List<Leaf> leafList = new ArrayList<>();

    public RandomReminder(Context context){
        this.mContext = context;
    }

    @Override
    protected void loadStructureData() {
        SQLiteDatabase database = new SQLiteDBHelper(mContext).getReadableDatabase();

        Cursor structureCursor = database.rawQuery("select * from " + SQLiteDBHelper.EDGE_TABLE_NAME, null);

        if (structureCursor.moveToFirst()) {
            while (!structureCursor.isAfterLast()) {
                String parent = structureCursor.getString(structureCursor.getColumnIndex(SQLiteDBHelper.EDGE_PARENT));
                String child = structureCursor.getString(structureCursor.getColumnIndex(SQLiteDBHelper.EDGE_CHILD));

                Log.i(TAG, "Construct structure: " + parent + " - " + child);
                this.addParentChild(parent, child);
                structureCursor.moveToNext();
            }
        }
    }

    @Override
    protected void loadContentData() throws MemoryAiderException {
        SQLiteDatabase database = new SQLiteDBHelper(mContext).getReadableDatabase();
        Cursor leafCursor = database.rawQuery("select * from " + SQLiteDBHelper.LEAF_TABLE_NAME, null);
        if (leafCursor.moveToFirst()) {
            while (!leafCursor.isAfterLast()) {
                int id = leafCursor.getInt(leafCursor.getColumnIndex(SQLiteDBHelper.LEAF_TABLE_ID));
                String parent = leafCursor.getString(leafCursor.getColumnIndex(SQLiteDBHelper.LEAF_PARENT));
                String content = leafCursor.getString(leafCursor.getColumnIndex(SQLiteDBHelper.LEAF_CONTENT));

                Log.i(TAG, "Content: " + parent + " - " + content);
                this.addLeaf(parent, content);
                leafCursor.moveToNext();
            }
        }

        updateEdgeData();
        updateLeafData();
    }

    public long addTag(String parent, String child){
        SQLiteDatabase database = new SQLiteDBHelper(mContext).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQLiteDBHelper.EDGE_PARENT, parent);
        values.put(SQLiteDBHelper.EDGE_CHILD, child);

        long rowId = database.insert(SQLiteDBHelper.EDGE_TABLE_NAME, null, values);

        Log.i(TAG, "insert " + parent + " - " + child + " - Result: " + rowId);

        this.addParentChild(parent, child);

        return rowId;
    }

    public long addContent(String parent, String content) throws MemoryAiderException{
        SQLiteDatabase database = new SQLiteDBHelper(mContext).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQLiteDBHelper.LEAF_PARENT, parent);
        values.put(SQLiteDBHelper.LEAF_CONTENT, content);

        long rowId = database.insert(SQLiteDBHelper.LEAF_TABLE_NAME, null, values);

        this.addLeaf(parent, content);
        return rowId;
    }

    private void updateEdgeData() {
        edgeList.clear();
        SQLiteDatabase database = new SQLiteDBHelper(mContext).getReadableDatabase();
        Cursor structureCursor = database.rawQuery("select * from " + SQLiteDBHelper.EDGE_TABLE_NAME, null);
        if (structureCursor.moveToFirst()) {
            while (!structureCursor.isAfterLast()) {
                String parent = structureCursor.getString(structureCursor.getColumnIndex(SQLiteDBHelper.EDGE_PARENT));
                String child = structureCursor.getString(structureCursor.getColumnIndex(SQLiteDBHelper.EDGE_CHILD));

                edgeList.add(new Edge(parent, child));
                structureCursor.moveToNext();
            }
        }
    }

    private void updateLeafData() {
        leafList.clear();
        SQLiteDatabase database = new SQLiteDBHelper(mContext).getReadableDatabase();

        Cursor leafCursor = database.rawQuery("select * from " + SQLiteDBHelper.LEAF_TABLE_NAME, null);
        if (leafCursor.moveToFirst()) {
            while (!leafCursor.isAfterLast()) {
                int id = leafCursor.getInt(leafCursor.getColumnIndex(SQLiteDBHelper.LEAF_TABLE_ID));
                String parent = leafCursor.getString(leafCursor.getColumnIndex(SQLiteDBHelper.LEAF_PARENT));
                String content = leafCursor.getString(leafCursor.getColumnIndex(SQLiteDBHelper.LEAF_CONTENT));

                leafList.add(new Leaf(id, parent, content));
                leafCursor.moveToNext();
            }
        }
    }

    public List<String> getTagList() {
        HashSet<String> set = new HashSet<>();
        for (Edge e: edgeList) {
            set.add(e.getParent());
            set.add(e.getChild());
        }
        List<String> result = new ArrayList<>(set);
        return result;
    }

    public List<String> getData() {
        List<String> result = new ArrayList<>();

        Collections.sort(leafList, (u, v) -> {
            if (u.getParent().equals(v.getParent())) {
                return Integer.compare(u.getId(), v.getId());
            }
            else return u.getParent().compareTo(v.getParent());
        });
        for (Leaf l: leafList) {
            result.add(l.toString());
        }

        Collections.sort(edgeList, (u, v) -> u.getParent().compareTo(v.getParent()));

        for (Edge e: edgeList) {
            result.add(e.toString());
        }

        return result;
    }

    public int deleteEdge(String... data){
        SQLiteDatabase database = new SQLiteDBHelper(mContext).getReadableDatabase();
        int result = database.delete(SQLiteDBHelper.EDGE_TABLE_NAME, SQLiteDBHelper.EDGE_PARENT + " = ? AND " + SQLiteDBHelper.EDGE_CHILD + " = ?", data);

        updateEdgeData();
        return result;
    }

    public int deleteLeaf(String... data) {
        SQLiteDatabase database = new SQLiteDBHelper(mContext).getReadableDatabase();
        int result = database.delete(SQLiteDBHelper.LEAF_TABLE_NAME, SQLiteDBHelper.LEAF_TABLE_ID + " = ? AND " + SQLiteDBHelper.LEAF_PARENT + " = ?", data );

        updateLeafData();
        return result;
    }
}
