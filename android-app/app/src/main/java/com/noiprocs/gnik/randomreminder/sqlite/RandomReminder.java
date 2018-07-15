package com.noiprocs.gnik.randomreminder.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.noiprocs.gnik.randomreminder.core.MemoryAider;
import com.noiprocs.gnik.randomreminder.core.MemoryAiderException;

public class RandomReminder extends MemoryAider {
    private final String TAG = RandomReminder.class.getCanonicalName();

    private final Context mContext;

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

                Log.i(TAG, "Construct structure: " + parent + " " + child);
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
                String parent = leafCursor.getString(leafCursor.getColumnIndex(SQLiteDBHelper.LEAF_PARENT));
                String content = leafCursor.getString(leafCursor.getColumnIndex(SQLiteDBHelper.LEAF_CONTENT));

                Log.i(TAG, "Content: " + parent + " " + content);
                this.addLeaf(parent, content);
            }
        }
    }

    public void addTag(String parent, String child){
        SQLiteDatabase database = new SQLiteDBHelper(mContext).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQLiteDBHelper.EDGE_PARENT, parent);
        values.put(SQLiteDBHelper.EDGE_CHILD, child);

        database.insert(SQLiteDBHelper.EDGE_TABLE_NAME, null, values);

        this.addParentChild(parent, child);
    }

    public void addContent(String parent, String content) throws MemoryAiderException{
        SQLiteDatabase database = new SQLiteDBHelper(mContext).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQLiteDBHelper.LEAF_PARENT, parent);
        values.put(SQLiteDBHelper.LEAF_CONTENT, content);

        database.insert(SQLiteDBHelper.LEAF_TABLE_NAME, null, values);

        this.addLeaf(parent, content);
    }
}
