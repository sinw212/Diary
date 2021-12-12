package com.example.diary.DB;
import android.provider.BaseColumns;

public class DataBases {

    public static final class CreateDB implements BaseColumns {
        public static final String _DATABASE = "TodayDiary.db";

        public static final String NAME = "name";
        public static final String ID = "id";
        public static final String PASSWORD = "password";
        public static final String DATE = "date";
        public static final String DIARY = "diary";
        public static final String TODO = "todo";
        public static final String CHECK = "check01";

        public static final String _USERTABLE = "userTable";
        public static final String _DIARYTABLE = "diaryTable";
        public static final String _TODOTABLE = "todoTable";

        public static final String _CREATE_USERTABLE = "create table if not exists " + _USERTABLE + "("
                + _ID + " integer primary key autoincrement, " //index 순번(순차적 증가)
                + NAME + " text not null, "
                + ID + " text not null, "
                + PASSWORD + " text not null );";

        public static final String _CREATE_DIARYTABLE = "create table if not exists " + _DIARYTABLE + "("
                + DATE + " text not null, "
                + DIARY + " text not null );";

        public static final String _CREATE_TODOTABLE = "create table if not exists " + _TODOTABLE + "("
                + _ID + " integer primary key autoincrement, "
                + DATE + " text not null, "
                + TODO + " text not null, "
                + CHECK + " integer not null );";
    }
}