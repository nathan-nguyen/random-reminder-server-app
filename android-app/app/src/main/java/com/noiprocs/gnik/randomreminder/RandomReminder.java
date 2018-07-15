package com.noiprocs.gnik.randomreminder;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.noiprocs.gnik.randomreminder.core.MemoryAider;
import com.noiprocs.gnik.randomreminder.core.MemoryAiderException;
import com.noiprocs.gnik.randomreminder.sqlite.SQLiteDBHelper;

public class RandomReminder extends MemoryAider {
    public void loadData(Context context) throws MemoryAiderException {
        SQLiteDatabase database = new SQLiteDBHelper(context).getReadableDatabase();

        Cursor structureCursor = database.rawQuery("select * from " + SQLiteDBHelper.EDGE_TABLE_NAME, null);

        if (structureCursor.moveToFirst()) {
            while (!structureCursor.isAfterLast()) {
                String parent = structureCursor.getString(structureCursor.getColumnIndex(SQLiteDBHelper.EDGE_PARENT));
                String child = structureCursor.getString(structureCursor.getColumnIndex(SQLiteDBHelper.EDGE_CHILD));

                this.addParentChild(parent, child);
                structureCursor.moveToNext();
            }
        }

        Cursor leafCursor = database.rawQuery("select * from " + SQLiteDBHelper.LEAF_TABLE_NAME, null);
        if (leafCursor.moveToFirst()) {
            while (!leafCursor.isAfterLast()) {
                String parent = leafCursor.getString(structureCursor.getColumnIndex(SQLiteDBHelper.LEAF_PARENT));
                String content = leafCursor.getString(structureCursor.getColumnIndex(SQLiteDBHelper.LEAF_CONTENT));

                this.addLeaf(parent, content);
            }
        }
    }

}
