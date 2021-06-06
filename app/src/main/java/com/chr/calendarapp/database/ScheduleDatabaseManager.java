package com.chr.calendarapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/* 일정 등록 데이터베이스 */
public class ScheduleDatabaseManager {
    static final String DB_SCHEDULE = "calendar.db";   //DB 이름
    static final String TABLE_SCHEDULE = "schedule"; //Table 이름
    static final int DB_VERSION = 1;			//DB 버전

    Context myContext = null;

    private static ScheduleDatabaseManager myDBManager = null;
    private SQLiteDatabase db = null;

    //ScheduleDatabaseManager 싱글톤 패턴으로 구현
    public static ScheduleDatabaseManager getInstance(Context context)
    {
        if(myDBManager == null)
        {
            myDBManager = new ScheduleDatabaseManager(context);
        }

        return myDBManager;
    }

    private ScheduleDatabaseManager(Context context)
    {
        myContext = context;

        //DB Open
        db = context.openOrCreateDatabase(DB_SCHEDULE, context.MODE_PRIVATE,null);

        //Table 생성
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_SCHEDULE +
                "(" + "idx INTEGER PRIMARY KEY AUTOINCREMENT," + // idx
                "title TEXT NOT NULL," + // 제목
                "time_start TEXT NOT NULL," + // 시작 시간
                "time_end TEXT NOT NULL," + //  종료 시간
                "place TEXT," + // 장소
                "latitude REAL," + // 위도
                "longitude REAL," + // 경도
                "memo TEXT," + // 메모
                "year INTEGER NOT NULL," + // 년
                "month INTEGER NOT NULL," + // 월
                "date INTEGER NOT NULL);"); // 일

    }

    // insert
    public long insert(ContentValues addRowValue)
    {
        return db.insert(TABLE_SCHEDULE, null, addRowValue);
    }


    // select를 위한 query
    public Cursor query(String[] colums,
                        String selection,
                        String[] selectionArgs,
                        String groupBy,
                        String having,
                        String orderby)
    {
        return db.query(TABLE_SCHEDULE,
                colums,
                selection,
                selectionArgs,
                groupBy,
                having,
                orderby);
    }


    // update
    public int update(ContentValues updateRowValue,
                      String whereClause,
                      String[] whereArgs)
    {
        return db.update(TABLE_SCHEDULE,
                updateRowValue,
                whereClause,
                whereArgs);
    }


    // delete
    public int delete(String whereClause,
                      String[] whereArgs)
    {

        return db.delete(TABLE_SCHEDULE,
                whereClause,
                whereArgs);
    }
}
