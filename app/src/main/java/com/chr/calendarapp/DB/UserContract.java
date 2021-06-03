package com.chr.calendarapp.DB;


import android.provider.BaseColumns;

public final class UserContract {
    public static final String DB_NAME="user.db";
    public static final int DATABASE_VERSION = 1;
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private UserContract() {}

    /* Inner class that defines the table contents */
    public static class Users implements BaseColumns {
        public static final String TABLE_NAME="Users";

        public static final String KEY_TITLE = "title";
        public static final String KEY_PLACE = "place";

        public static final String KEY_YEAR = "Year";
        public static final String KEY_MONTH = "Month";
        public static final String KEY_DATE = "Date";
        public static final String KEY_TIME = "Time";

        public static final String KEY_TIME_START = "time_start";
        public static final String KEY_TIME_END = "time_end";

        public static final String KEY_LATITUDE = "latitude";
        public static final String KEY_LONGITUDE = "longitude";

        public static final String KEY_MEMO = "memo";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY" + COMMA_SEP +

                KEY_TITLE + TEXT_TYPE + COMMA_SEP +
                KEY_PLACE + TEXT_TYPE + COMMA_SEP +

                KEY_YEAR + TEXT_TYPE + COMMA_SEP +
                KEY_MONTH + TEXT_TYPE + COMMA_SEP +
                KEY_DATE + TEXT_TYPE + COMMA_SEP +
                KEY_TIME + TEXT_TYPE + COMMA_SEP +

                KEY_TIME_START + TEXT_TYPE + COMMA_SEP +
                KEY_TIME_END + TEXT_TYPE + COMMA_SEP +

                KEY_LATITUDE + TEXT_TYPE + COMMA_SEP +
                KEY_LONGITUDE + TEXT_TYPE + COMMA_SEP +

                KEY_MEMO + TEXT_TYPE + " )";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}