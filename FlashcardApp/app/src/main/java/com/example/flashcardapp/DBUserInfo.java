package com.example.flashcardapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DBUserInfo extends SQLiteOpenHelper {

    private String TABLE_NAME="UserInfo";

    private static final String NAME_COL = "name";
    private static final String AGE_COL = "age";
    private static final String GENDER_COL = "gender";
    private static final String USERNAME_COL = "username";
    private static final String PASSWORD_COL = "password";

    public DBUserInfo(Context context, String DATABASE_NAME){
        super(context,DATABASE_NAME,null,1);
    }

    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + "("
                + USERNAME_COL + " TEXT PRIMARY KEY, "
                + NAME_COL + " TEXT,"
                + AGE_COL + " INTEGER,"
                + GENDER_COL + " TEXT, "
                + PASSWORD_COL + " TEXT"
                + ")";
        db.execSQL(query);

    }

    public void addInfo(String username, String name, int age, String gender, String password) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        values.put(USERNAME_COL, username);
        values.put(NAME_COL, name);
        values.put(AGE_COL, age);
        values.put(GENDER_COL, gender);
        values.put(PASSWORD_COL, password);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }


    public String selectQuery(String fieldname) {
        Log.d("==INSIDE QUERY SELECT=======================","");
        String query="SELECT "+fieldname+" FROM "+TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor extract=db.rawQuery(query,null);

        extract.moveToFirst();
        String val = extract.getString(0) + " ";
        Log.d("==Updated FIRST ITEM==",val);
        //while (extract.moveToNext()) {
        //    val += extract.getString(0) + " ";
        //}

        extract.close();
        db.close();

        return val;
    }

    public String selectQuery(String fieldname, String condition) {
        Log.d("==INSIDE QUERY SELECT=======================","");
        String query="SELECT "+fieldname+" FROM "+TABLE_NAME + " WHERE " + condition;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor extract=db.rawQuery(query,null);


        boolean first = extract.moveToFirst();

        if (!first) { // if empty
            extract.close();
            db.close();
            return "";
        }

        String val = extract.getString(0);
//        Log.d("==Updated FIRST ITEM==",val);
//        while (extract.moveToNext()) {
//            val = extract.getString(0);
//        }

        extract.close();
        db.close();

        return val;
    }

    public void updateQuery(String value, String condition){
        String query = "UPDATE "+TABLE_NAME+" SET "+ value  + " WHERE " + condition;
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(query);
        db.close();
    }

    public void deleteQuery(String condition){
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + condition;
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(query);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public static byte[] messageDigest(String s) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(s.getBytes(StandardCharsets.UTF_8));
    }


}
