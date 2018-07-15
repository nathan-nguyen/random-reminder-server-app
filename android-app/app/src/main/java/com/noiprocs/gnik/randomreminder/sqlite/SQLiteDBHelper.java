package com.noiprocs.gnik.randomreminder.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "memory_aider_database";

    public static final String EDGE_TABLE_NAME = "edge";
    public static final String EDGE_PARENT = "parent";
    public static final String EDGE_CHILD = "child";

    public static final String LEAF_TABLE_NAME = "leaf";
    public static final String LEAF_PARENT = "parent";
    public static final String LEAF_CONTENT = "content";

    public SQLiteDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + EDGE_TABLE_NAME + " (" +
                EDGE_PARENT + " TEXT, " +
                EDGE_CHILD + " TEXT" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EDGE_TABLE_NAME);
        onCreate(db);
    }
}
