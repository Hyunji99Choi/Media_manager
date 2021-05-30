package com.example.mediamanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.mediamanager.SearchPage.ListViewAdapterData;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MyMedia.db";
    public static final String MOVIES_TABLE_NAME = "media";
    public static final String MOVIES_COLUMN_ID = "id";
    public static final String MOVIES_COLUMN_TITLE = "title";
    public static final String MOVIES_COLUMN_DATE = "date";
    public static final String MOVIES_COLUMN_PATH = "path";
    public static final String MOVIES_COLUMN_MEMO = "memo";
    public static final String MOVIES_COLUMN_KINDS = "kinds";


    public DBHelper(Context context){
        super(context, DATABASE_NAME, null,2);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table media "+"(id integer primary key, title text, date text, path text, memo text, kinds text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS media");
        onCreate(db);
    }

    public boolean insertMovie(String title, String date, String path, String memo, String kinds){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("title",title);
        contentValues.put("date",date);
        contentValues.put("path",path);
        contentValues.put("memo",memo);
        contentValues.put("kinds",kinds);

        db.insert("media",null,contentValues);
        return true;
    }

    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from media where id="+id+"",null);
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, MOVIES_TABLE_NAME);
        return numRows;
    }

    public boolean updateMovie(Integer id, String title, String date, String path, String memo, String kinds){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title",title);
        contentValues.put("date",date);
        contentValues.put("path",path);
        contentValues.put("memo",memo);
        contentValues.put("kinds",kinds);

        db.update("media",contentValues, "id=?",new String[]{Integer.toString(id)});
        return true;
    }

    public Integer deleteMovie(Integer id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("media","id=?",new String[]{Integer.toString(id)});
    }

    public ArrayList getAllMovies() {
        ArrayList array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("Select * from media",null);
        res.moveToFirst();

        while (res.isAfterLast()==false){
            ListViewAdapterData list = new ListViewAdapterData();
            list.setId(res.getInt(res.getColumnIndex(MOVIES_COLUMN_ID)));
            list.setTitle(res.getString(res.getColumnIndex(MOVIES_COLUMN_TITLE)));
            list.setDate(res.getString(res.getColumnIndex(MOVIES_COLUMN_DATE)));

            array_list.add(list);
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList getSearchMovies(String title, String date){
        ArrayList array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("Select * from media where title LIKE'%"+title+"%' OR date LIKE '%"+date+"%'",null);
        res.moveToFirst();

        while (res.isAfterLast()==false){
            ListViewAdapterData list = new ListViewAdapterData();
            list.setId(res.getInt(res.getColumnIndex(MOVIES_COLUMN_ID)));
            list.setTitle(res.getString(res.getColumnIndex(MOVIES_COLUMN_TITLE)));
            list.setDate(res.getString(res.getColumnIndex(MOVIES_COLUMN_DATE)));

            array_list.add(list);
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList getGridView(){
        ArrayList array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("Select * from media ORDER BY id DESC limit 16",null);
        res.moveToFirst();

        while (res.isAfterLast()==false){
            gridViewAdapterData data = new gridViewAdapterData();
            data.setUrl(res.getString(res.getColumnIndex(MOVIES_COLUMN_PATH)));
            data.setKind(res.getString(res.getColumnIndex(MOVIES_COLUMN_KINDS)));

            array_list.add(data);
            res.moveToNext();
        }
        return array_list;
    }

}
