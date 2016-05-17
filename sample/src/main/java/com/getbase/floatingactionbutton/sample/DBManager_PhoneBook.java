package com.getbase.floatingactionbutton.sample;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBManager_PhoneBook extends SQLiteOpenHelper {

    public static Cursor cursor_all;


    public DBManager_PhoneBook(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블을 생성한다.
        // create table 테이블명 (컬럼명 타입 옵션);
//        db.execSQL("CREATE TABLE FOOD_LIST( _id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price INTEGER);");
        db.execSQL("CREATE TABLE PRANK_LIST( _id INTEGER PRIMARY KEY AUTOINCREMENT, user_name TEXT, user_number TEXT);");
//        db.execSQL("insert into PRANK_LIST values(null, '이상헌', '010-3342-5496');"); // 전화
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insert(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }

    public void update(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }

    public void delete(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }


    public String select_number(String sql) {
        SQLiteDatabase db = getReadableDatabase();
        String str = "";

        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            str = cursor.getString(cursor.getColumnIndex("user_number"));
        }
        return str;
    }

    public String PrintData() {
        SQLiteDatabase db = getReadableDatabase();
        String str = "";

        Cursor cursor = db.rawQuery("select * from PRANK_LIST", null);
        while (cursor.moveToNext()) {
            str += cursor.getInt(0)
                    + ", packageName: "
                    + cursor.getString(1)
                    + ", appName: "
                    + cursor.getString(2)
                    + ", button_id: "
                    + cursor.getInt(3)
                    + ", button_status: "
                    + cursor.getString(4)
                    + "\n";
        }

        return str;
    }


    public ArrayList<String> selectAll() {
        SQLiteDatabase db = getReadableDatabase();
        String str = "";
        ArrayList<String> list = new ArrayList<String>();

        cursor_all = db.rawQuery("select * from PRANK_LIST", null);
        while (cursor_all.moveToNext()) {
            str = cursor_all.getString(cursor_all.getColumnIndex("user_name")) + "/" + cursor_all.getString(cursor_all.getColumnIndex("user_number"));
            list.add(str);
        }
        return list;
    }


}