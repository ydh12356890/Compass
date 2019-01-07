package com.example.administrator.myapplication1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpenHelper extends SQLiteOpenHelper {
    public static final String CREATE_TABLE_SQL = "create table userlist( id integer primary key autoincrement,username text,password text)";
    private Context mcontext;

    public MyOpenHelper(Context context,String name,SQLiteDatabase.CursorFactory cursorFactory,int version) {
        super(context, name,cursorFactory, version);
        mcontext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
