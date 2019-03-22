package com.zekihan.utilities.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.zekihan.datatype.LastReadInfo;

import java.util.ArrayList;
import java.util.List;

public class LastReadDatabaseHelper extends SQLiteOpenHelper implements DatabaseHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "last_read_db";


    public LastReadDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(LastReadInfo.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + LastReadInfo.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertLastReadInfo(LastReadInfo lastReadInfo) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(LastReadInfo.COLUMN_NOVELID, lastReadInfo.getNovelId());
        values.put(LastReadInfo.COLUMN_CHAPTERNUM, lastReadInfo.getChapterNum());
        values.put(LastReadInfo.COLUMN_SCROLLPOSITION, lastReadInfo.getScrollPosition());

        // insert row
        long id = db.insert(LastReadInfo.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public LastReadInfo getLastReadInfo(String novelId) {

        try {
            // get readable database as we are not inserting anything
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.query(LastReadInfo.TABLE_NAME,
                    new String[]{LastReadInfo.COLUMN_ID, LastReadInfo.COLUMN_NOVELID, LastReadInfo.COLUMN_CHAPTERNUM,LastReadInfo.COLUMN_SCROLLPOSITION},
                    LastReadInfo.COLUMN_NOVELID + "=?",
                    new String[]{novelId}, null, null, null, null);

            if (cursor != null)
                cursor.moveToFirst();

            // prepare note object
            LastReadInfo lastReadInfo = new LastReadInfo(
                    cursor.getString(cursor.getColumnIndex(LastReadInfo.COLUMN_NOVELID)),
                    cursor.getInt(cursor.getColumnIndex(LastReadInfo.COLUMN_CHAPTERNUM)),
                    cursor.getInt(cursor.getColumnIndex(LastReadInfo.COLUMN_SCROLLPOSITION)));

            // close the db connection
            cursor.close();
            return lastReadInfo;
        }catch (CursorIndexOutOfBoundsException e){
            Log.e("LastReadDatabaseHelper",e.toString());
            return null;
        }


    }
    public List<LastReadInfo> getAllLastReadInfos() {
        List<LastReadInfo> lastReadInfos = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + LastReadInfo.TABLE_NAME + " ORDER BY " +
                LastReadInfo.COLUMN_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                LastReadInfo lastReadInfo = new LastReadInfo(
                        cursor.getString(cursor.getColumnIndex(LastReadInfo.COLUMN_NOVELID)),
                        cursor.getInt(cursor.getColumnIndex(LastReadInfo.COLUMN_CHAPTERNUM)),
                        cursor.getInt(cursor.getColumnIndex(LastReadInfo.COLUMN_SCROLLPOSITION)));
                lastReadInfos.add(lastReadInfo);
            } while (cursor.moveToNext());
        }

        // close db connection
        cursor.close();
        db.close();

        // return notes list
        return lastReadInfos;
    }

    public int updateLastReadInfo(LastReadInfo lastReadInfo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LastReadInfo.COLUMN_NOVELID, lastReadInfo.getNovelId());
        values.put(LastReadInfo.COLUMN_CHAPTERNUM, lastReadInfo.getChapterNum());
        values.put(LastReadInfo.COLUMN_SCROLLPOSITION, lastReadInfo.getScrollPosition());

        // updating row
        return db.update(LastReadInfo.TABLE_NAME, values, LastReadInfo.COLUMN_NOVELID + " = ?",
                new String[]{String.valueOf(lastReadInfo.getNovelId())});
    }
    public void deleteLastReadInfo(LastReadInfo lastReadInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(LastReadInfo.TABLE_NAME, LastReadInfo.COLUMN_NOVELID + " = ?",
                new String[]{String.valueOf(lastReadInfo.getNovelId())});
        db.close();
    }
}
