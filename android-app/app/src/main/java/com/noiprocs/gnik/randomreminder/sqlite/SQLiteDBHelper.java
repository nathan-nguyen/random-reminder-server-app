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
    public static final String EDGE_ACTIVATE = "activate";

    public static final String LEAF_TABLE_NAME = "leaf";
    public static final String LEAF_TABLE_ID = "_id";
    public static final String LEAF_PARENT = "parent";
    public static final String LEAF_CONTENT = "content";
    public static final String LEAF_ACTIVATE = "activate";

    SQLiteDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + EDGE_TABLE_NAME + " (" +
                EDGE_PARENT + " TEXT, " +
                EDGE_CHILD + " TEXT, " +
                EDGE_ACTIVATE + " INTEGER DEFAULT 1, " +
                "PRIMARY KEY (" + EDGE_PARENT + ", " + EDGE_CHILD + "))");
        db.execSQL("CREATE TABLE " + LEAF_TABLE_NAME + " (" +
                LEAF_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LEAF_PARENT + " TEXT, " +
                LEAF_CONTENT + " TEXT, " +
                LEAF_ACTIVATE + " INTEGER DEFAULT 1)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EDGE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + LEAF_TABLE_NAME);
        onCreate(db);
    }
}

//[27 - algorithm - String KMP, 26 - bit-manipulation - bitwise-and-of-numbers-range-leetcode, 15 - design-pattern - Inversion of Control, 16 - design-pattern - Bridge Pattern, 33 - design-pattern - What  is Design Pattern, 34 - design-pattern - Singleton Pattern, 35 - design-pattern - Adapter Pattern, 36 - design-pattern - Observer Pattern, 39 - design-pattern - Proxy Pattern, 40 - design-pattern - Builder Pattern, 41 - design-pattern - Factory pattern, 42 - design-pattern - Abstract Factory Pattern, 43 - design-pattern - Filter Pattern, 47 - design-pattern - Strategy Pattern, 44 - diagram - Class diagram, 6 - dynamic-programming - Coin change problem, 7 - dynamic-programming - Knapsack problem, 50 - java - Static Binding vs Dynamic Binding, 51 - java - Thread: wait() vs sleep(), 52 - java - Iterator vs ListIterator, 53 - java - HashMap vs IdentityHashMap, 8 - memory - Think about 1 character, 46 - network - Open system interconnection (OSI), 3 - programming - Big Endian vs Small Endian, 4 - programming - C - macro, 2 - schedule - Drink Water, 32 - sdlc - Agile and SCRUM, 54 - shell - Disk Remaining command, 55 - shell - List process which is listening to a specific PORT, 56 - shell - List files which are modified 10 mins ago, 57 - shell - Awk Reverse String - File, 20 - to-do - Learn Java 8 - Lambda, 37 - to-do - Pickup 1 leetcode contest, 48 - to-do - Reply advance ai email, 58 - to-do - Lawrence: Chaitu + dessert, 22 - tolearn - Java generic, 23 - tolearn - Java 8 Lambda expression, 38 - work - Deployment documentation, algorithm - dynamic-programming, algorithm - bit-manipulation, root - schedule, root - interview, root - programming, root - to-do, root - algorithm, root - memory, root - design-pattern, root - tolearn, root - sdlc, root - work, root - diagram, root - network, root - java, root - shell]