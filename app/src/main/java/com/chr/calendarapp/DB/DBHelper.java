package com.chr.calendarapp.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    final static String TAG="SQLiteDBTest";

    public DBHelper(Context context) {
        super(context, UserContract.DB_NAME, null, UserContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG,getClass().getName()+".onCreate()");
        db.execSQL(UserContract.Users.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.i(TAG,getClass().getName() +".onUpgrade()");
        db.execSQL(UserContract.Users.DELETE_TABLE);
        onCreate(db);
    }


    public long insertUserByMethod(String title, String place, String year, String month, String date, String time, String time_start, String time_end, String latitude, String longitude, String memo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(UserContract.Users.KEY_TITLE, title);
        values.put(UserContract.Users.KEY_PLACE, place);
        values.put(UserContract.Users.KEY_YEAR, year);
        values.put(UserContract.Users.KEY_MONTH,month);
        values.put(UserContract.Users.KEY_DATE, date);
        values.put(UserContract.Users.KEY_TIME, time);
        values.put(UserContract.Users.KEY_TIME_START, time_start);
        values.put(UserContract.Users.KEY_TIME_END, time_end);
        values.put(UserContract.Users.KEY_LATITUDE, latitude);
        values.put(UserContract.Users.KEY_LONGITUDE, longitude);
        values.put(UserContract.Users.KEY_MEMO, memo);

        return db.insert(UserContract.Users.TABLE_NAME,null,values);
    }

    public Cursor getAllUsersByMethod() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(UserContract.Users.TABLE_NAME,null,null,null,null,null,null);
    }

    public long deleteUserByMethod(String _id) {
        SQLiteDatabase db = getWritableDatabase();

        String whereClause = UserContract.Users._ID +" = ?";
        String[] whereArgs ={_id};
        return db.delete(UserContract.Users.TABLE_NAME, whereClause, whereArgs);
    }

    public long updateUserByMethod(String _id, String title, String place, String year, String month, String date, String time, String time_start, String time_end, String latitude, String longitude, String memo) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserContract.Users.KEY_TITLE, title);
        values.put(UserContract.Users.KEY_PLACE, place);
        values.put(UserContract.Users.KEY_YEAR, year);
        values.put(UserContract.Users.KEY_MONTH,month);
        values.put(UserContract.Users.KEY_DATE, date);
        values.put(UserContract.Users.KEY_TIME, time);
        values.put(UserContract.Users.KEY_TIME_START, time_start);
        values.put(UserContract.Users.KEY_TIME_END, time_end);
        values.put(UserContract.Users.KEY_LATITUDE, latitude);
        values.put(UserContract.Users.KEY_LONGITUDE, longitude);
        values.put(UserContract.Users.KEY_MEMO, memo);

        String whereClause = UserContract.Users._ID +" = ?";
        String[] whereArgs ={_id};

        return db.update(UserContract.Users.TABLE_NAME, values, whereClause, whereArgs);
    }


    public Cursor getAllUsersBySQL() {
        String sql = "Select * FROM " + UserContract.Users.TABLE_NAME;
        return getReadableDatabase().rawQuery(sql,null);
    }
}