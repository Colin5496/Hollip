package com.getbase.floatingactionbutton.sample;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBManager extends SQLiteOpenHelper {

    public static Cursor cursor_all;


    public DBManager(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블을 생성한다.
        // create table 테이블명 (컬럼명 타입 옵션);
//        db.execSQL("CREATE TABLE FOOD_LIST( _id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price INTEGER);");
        db.execSQL("CREATE TABLE APP_LIST( _id INTEGER PRIMARY KEY AUTOINCREMENT, package TEXT, app_name TEXT, button_id INTEGER,  button_status TEXT);");

        db.execSQL("insert into APP_LIST values(null, 'dial', '전화', 1, 'on');"); // 전화
        db.execSQL("insert into APP_LIST values(null, 'sms', '문자', 2, 'on');"); // 카카오톡
        db.execSQL("insert into APP_LIST values(null, 'com.kakao.talk', '카카오톡', 3, 'on');"); // 카카오톡

        db.execSQL("CREATE TABLE PRANK_LIST( _id INTEGER PRIMARY KEY AUTOINCREMENT, user_name TEXT, user_number TEXT);");
        db.execSQL("insert into PRANK_LIST values(null, '이상헌', '010-3342-5496');"); // 전화

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

    public String selectOnOff(String sql) {
        SQLiteDatabase db = getReadableDatabase();
        String OnOff = "";

        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            OnOff = cursor.getString(cursor.getColumnIndex("button_status"));
        }
        return OnOff;
    }

    public String selectName(String sql) {
        SQLiteDatabase db = getReadableDatabase();
        String str = "";

        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            str = cursor.getString(cursor.getColumnIndex("app_name"));
        }
        return str;
    }

    public String getPackagename(String index) {
        return this.select("select package from APP_LIST where _id = " + index + ";");
    }

    public String getStatus(String index) {
        return this.selectstatus("select button_status from APP_LIST where _id = " + index + ";");
    }

    public String getAppname(String packageName)
    {
        return this.selectappname("select app_name from APP_LIST where package = '" + packageName + "';");
    }

    public boolean isExistData(String packageName) {
        String temp_status = "";
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("select button_status from APP_LIST where package = '" + packageName + "';", null);
        cursor.moveToNext();
        try {
            temp_status = cursor.getString(cursor.getColumnIndex("button_status"));
            if (temp_status.equals("on")) return true;
            else return false;
        } catch (Exception e) {
            return false;
        }
    }

    public int getDbCount() {
        return cursor_all.getCount();
    }

    public int getLastRow()
    {
        SQLiteDatabase db = getReadableDatabase();
        int result = 0;

        Cursor last_cursor = db.rawQuery("select * from APP_LIST", null);
        last_cursor .moveToLast();
        result = last_cursor.getInt(0);

        return result;
    }

    // select package from APP_LIST where package = 'packageName';
    public String select(String sql) {
        SQLiteDatabase db = getReadableDatabase();
        String str = "";

        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            str = cursor.getString(cursor.getColumnIndex("package"));
        }
        return str;
    }

    public String selectstatus(String sql) {
        SQLiteDatabase db = getReadableDatabase();
        String str = "";

        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            str = cursor.getString(cursor.getColumnIndex("button_status"));
        }
        return str;
    }

    public String selectappname(String sql) {
        SQLiteDatabase db = getReadableDatabase();
        String str = "";

        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            str = cursor.getString(cursor.getColumnIndex("app_name"));
        }
        return str;
    }

    public String PrintData() {
        SQLiteDatabase db = getReadableDatabase();
        String str = "";

        Cursor cursor = db.rawQuery("select * from APP_LIST", null);
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

        cursor_all = db.rawQuery("select * from APP_LIST", null);
        while (cursor_all.moveToNext()) {
            str = cursor_all.getString(cursor_all.getColumnIndex("package")) + "/" + cursor_all.getString(cursor_all.getColumnIndex("app_name")) + "/" + cursor_all.getInt(cursor_all.getColumnIndex("button_id")) + "/" + cursor_all.getString(cursor_all.getColumnIndex("button_status"));
            list.add(str);
        }

        return list;
    }


    public ArrayList<String> selectAll_prank() {
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



    public void delete_prank(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }


    public String select_number_prank(String sql) {
        SQLiteDatabase db = getReadableDatabase();
        String str = "";

        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            str = cursor.getString(cursor.getColumnIndex("user_number"));
        }
        return str;
    }

    public void insert_prank(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }
}