package com.example.diary.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        //테이블 생성
        db.execSQL(DataBases.CreateDB._CREATE_USERTABLE);
        db.execSQL(DataBases.CreateDB._CREATE_DIARYTABLE);
        db.execSQL(DataBases.CreateDB._CREATE_TODOTABLE);
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    public void createTable(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(DataBases.CreateDB._CREATE_USERTABLE);
        db.execSQL(DataBases.CreateDB._CREATE_DIARYTABLE);
        db.execSQL(DataBases.CreateDB._CREATE_TODOTABLE);
        db.close();
    }

    //회원가입_ 회원 정보 등록
    public void insert_userSignup(String name, String ID, String PW) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL(
                "INSERT INTO " + DataBases.CreateDB._USERTABLE +
                        " VALUES(NULL, '" + name + "', '" + ID + "', '" + PW + "');");
        db.close();
    }

    //회원 정보 조회
    public String getResult_userInfo(int count, String ID) {
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        Cursor cursor = db.rawQuery("SELECT * FROM " + DataBases.CreateDB._USERTABLE + " WHERE ID = '" + ID + "'", null);

        if(cursor.getCount() > 0) {
            switch(count) {
                case 0: //회원가입_ 중복 ID 존재 여부 조회
                    result = "1";
                    break;
                case 1: //로그인_ PW 일치 여부 확인
                    while(cursor.moveToNext()) {
                        result += String.format("%s/%s\n",
                                cursor.getString(1), //Name
                                cursor.getString(3)  //PW
                        );
                    }
                    break;
            }
        } else {
            result = "";
        }
        cursor.close();
        db.close();
        return result;
    }

    //한줄 일기장 등록
    public void insert_today_diary(int count, String selectDate, String diary) {
        SQLiteDatabase db = getWritableDatabase();

        String date = selectDate.substring(0,4) + selectDate.substring(5,7) + selectDate.substring(8,10);
        Log.d("진입dateDiary", date);

        switch(count) {
            case 0: //처음 등록하는 경우
                db.execSQL(
                        "INSERT INTO " + DataBases.CreateDB._DIARYTABLE +
                                " VALUES(" + date + ", '" + diary + "');");
                break;
            case 1: //이미 등록된 일기를 수정하는 경우
                db.execSQL(
                        "UPDATE " + DataBases.CreateDB._DIARYTABLE +
                                " SET DIARY = '" + diary + "' WHERE DATE = '" + date + "';");
                break;
        }
        db.close();
    }

    //한줄 일기장 조회
    public String getResult_diary(String selectDate) {
        SQLiteDatabase db = getReadableDatabase();

        String date = selectDate.substring(0,4) + selectDate.substring(5,7) + selectDate.substring(8,10);
        Log.d("진입dateDiary", date);

        String result = "";

        Cursor cursor = db.rawQuery("SELECT * FROM " + DataBases.CreateDB._DIARYTABLE + " WHERE DATE = '" + date + "'", null);

        if(cursor.getCount() > 0) {
            while(cursor.moveToNext()) {
                result = cursor.getString(1); //Diary
            }
        } else {
            result = "";
        }
        cursor.close();
        db.close();
        return result;
    }


    //오늘의 할일 등록
    public void insert_today_todo(String selectDate, String todo) {
        SQLiteDatabase db = getWritableDatabase();

        String date = selectDate.substring(0,4) + selectDate.substring(5,7) + selectDate.substring(8,10);
        Log.d("진입dateTodo", date);
        int checkBoolean = 0;

        db.execSQL(
                "INSERT INTO " + DataBases.CreateDB._TODOTABLE +
                        " VALUES(NULL, " + date + ", '" + todo + "', " + checkBoolean + ");");
        db.close();
    }

    //오늘의 할일 조회
    public String getResult_todo(String selectDate) {
        SQLiteDatabase db = getReadableDatabase();

        String date = selectDate.substring(0,4) + selectDate.substring(5,7) + selectDate.substring(8,10);
        Log.d("진입dateTodo", date);

        String result = "";

        Cursor cursor = db.rawQuery("SELECT * FROM " + DataBases.CreateDB._TODOTABLE + " WHERE DATE = '" + date + "'", null);

        if(cursor.getCount() > 0) {
            while(cursor.moveToNext()) {
                result += String.format("%s/%s\n",
                        cursor.getString(3), //CheckBoolean
                        cursor.getString(2)  //Todo
                );
            }
        } else {
            result = "";
        }
        cursor.close();
        db.close();
        return result;
    }
}