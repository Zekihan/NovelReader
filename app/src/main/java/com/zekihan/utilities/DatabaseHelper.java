package com.zekihan.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import com.zekihan.datatype.Genre;
import com.zekihan.datatype.Novel;
import com.zekihan.datatype.Status;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "novels_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(Novel.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Novel.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }
    public long insertNovel(Novel novel) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Novel.COLUMN_ID, novel.getId());
        values.put(Novel.COLUMN_NAME, novel.getName());
        values.put(Novel.COLUMN_DESCRIPTION, novel.getDescription());
        values.put(Novel.COLUMN_CHAPTERCOUNT, novel.getChapterCount());
        values.put(Novel.COLUMN_AUTHOR, novel.getAuthor());
        values.put(Novel.COLUMN_LANGUAGE, novel.getLanguage());
        values.put(Novel.COLUMN_STATUS, "status");
        values.put(Novel.COLUMN_GENRES, "genres");
        values.put(Novel.COLUMN_TAGS, "tags");


        // insert row
        long id = db.insert(Novel.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }
    public Novel getNovel(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Novel.TABLE_NAME,
                new String[]{Novel.COLUMN_IDX, Novel.COLUMN_ID, Novel.COLUMN_NAME, Novel.COLUMN_DESCRIPTION, Novel.COLUMN_AUTHOR, Novel.COLUMN_CHAPTERCOUNT, Novel.COLUMN_LANGUAGE, Novel.COLUMN_STATUS, Novel.COLUMN_GENRES, Novel.COLUMN_TAGS},
                Novel.COLUMN_IDX + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        Novel novel = new Novel(
                cursor.getString(cursor.getColumnIndex(Novel.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Novel.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(Novel.COLUMN_AUTHOR)),
                cursor.getInt(cursor.getColumnIndex(Novel.COLUMN_CHAPTERCOUNT)),
                cursor.getString(cursor.getColumnIndex(Novel.COLUMN_LANGUAGE)),
                cursor.getString(cursor.getColumnIndex(Novel.COLUMN_DESCRIPTION)),
                Status.Completed,
                new ArrayList<Genre>(),
                new ArrayList<String>());

        // close the db connection
        cursor.close();

        return novel;
    }
    public List<Novel> getAllNotes() {
        List<Novel> novels = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Novel.TABLE_NAME + " ORDER BY " +
                Novel.COLUMN_IDX + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Novel novel = new Novel(
                        cursor.getString(cursor.getColumnIndex(Novel.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(Novel.COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndex(Novel.COLUMN_AUTHOR)),
                        cursor.getInt(cursor.getColumnIndex(Novel.COLUMN_CHAPTERCOUNT)),
                        cursor.getString(cursor.getColumnIndex(Novel.COLUMN_LANGUAGE)),
                        cursor.getString(cursor.getColumnIndex(Novel.COLUMN_DESCRIPTION)),
                        Status.Completed,
                        new ArrayList<Genre>(),
                        new ArrayList<String>());
                novels.add(novel);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return novels;
    }
    public int getNovelsCount() {
        String countQuery = "SELECT  * FROM " + Novel.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }
    public int updateNote(Novel novel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Novel.COLUMN_ID, novel.getId());
        values.put(Novel.COLUMN_NAME, novel.getName());
        values.put(Novel.COLUMN_DESCRIPTION, novel.getDescription());
        values.put(Novel.COLUMN_CHAPTERCOUNT, novel.getChapterCount());
        values.put(Novel.COLUMN_AUTHOR, novel.getAuthor());
        values.put(Novel.COLUMN_LANGUAGE, novel.getLanguage());
        values.put(Novel.COLUMN_STATUS, "status");
        values.put(Novel.COLUMN_GENRES, "genres");
        values.put(Novel.COLUMN_TAGS, "tags");

        // updating row
        return db.update(Novel.TABLE_NAME, values, Novel.COLUMN_IDX + " = ?",
                new String[]{String.valueOf(novel.getIdx())});
    }
    public void deleteNote(Novel novel) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Novel.TABLE_NAME, Novel.COLUMN_IDX + " = ?",
                new String[]{String.valueOf(novel.getIdx())});
        db.close();
    }
}