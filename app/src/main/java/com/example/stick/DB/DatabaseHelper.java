package com.example.stick.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.stick.Model.NoteModel;
import com.example.stick.Model.TaskModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(@Nullable Context context) {
        super(context, DBConstants.DB_NAME, null, DBConstants.DB_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTableOne(db); //Main Table
        createTableTwo(db); //Task Table
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.T1_NAME);
        onCreate(db);
    }

    private void createTableOne(SQLiteDatabase db) {
        Log.d("dbko", "createTableOne: " + DBConstants.T1_QUERY);
        db.execSQL(DBConstants.T1_QUERY);
    }

    private void createTableTwo(SQLiteDatabase db) {
        Log.d("dbko", "createTableOne: " + DBConstants.T2_QUERY);
        db.execSQL(DBConstants.T2_QUERY);
    }

    public long insertNote(String title, String color) {
        long dateMilis = Calendar.getInstance().getTimeInMillis();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBConstants.T1_TITLE, title);
        values.put(DBConstants.T1_COLOR, color);
        values.put(DBConstants.T1_DATE, dateMilis);
        long id = db.insert(DBConstants.T1_NAME, null, values);
        db.close();
        return id;
    }

    public NoteModel getNote(long noteID) {
        String selectQuery = "SELECT * FROM " + DBConstants.T1_NAME + " WHERE " + DBConstants.T1_ID + " = " + noteID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        NoteModel note = new NoteModel(
                cursor.getInt(cursor.getColumnIndex(DBConstants.T1_ID)),
                cursor.getString(cursor.getColumnIndex(DBConstants.T1_TITLE)),
                cursor.getString(cursor.getColumnIndex(DBConstants.T1_COLOR)),
                cursor.getLong(cursor.getColumnIndex(DBConstants.T1_DATE)));
        db.close();
        return note;
    }

    public List<NoteModel> getAllNotes() {
        List<NoteModel> notes = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DBConstants.T1_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToNext()){
            do {
                NoteModel note = new NoteModel(
                        cursor.getInt(cursor.getColumnIndex(DBConstants.T1_ID)),
                        cursor.getString(cursor.getColumnIndex(DBConstants.T1_TITLE)),
                        cursor.getString(cursor.getColumnIndex(DBConstants.T1_COLOR)),
                        cursor.getLong(cursor.getColumnIndex(DBConstants.T1_DATE)));
                notes.add(note);
            } while (cursor.moveToNext());
        }
        db.close();
        return notes;
    }

    public List<TaskModel> getTasks(long noteID){
        List<TaskModel> tasks = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DBConstants.T2_NAME + " WHERE " + DBConstants.T2_PARENTID + " = " + noteID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToNext()){
            do{
                TaskModel task = new TaskModel(
                        cursor.getInt(cursor.getColumnIndex(DBConstants.T2_ID)),
                        cursor.getString(cursor.getColumnIndex(DBConstants.T2_CONTENT)),
                        cursor.getString(cursor.getColumnIndex(DBConstants.T2_STATUS)),
                        cursor.getLong(cursor.getColumnIndex(DBConstants.T2_DATE)),
                        cursor.getLong(cursor.getColumnIndex(DBConstants.T2_PARENTID)));
                tasks.add(task);
            } while (cursor.moveToNext());
        }
        db.close();
        return tasks;
    }

    public long insertTask(String content, String status, long parentID){
        long dateMilis = Calendar.getInstance().getTimeInMillis();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBConstants.T2_CONTENT, content);
        values.put(DBConstants.T2_STATUS, status);
        values.put(DBConstants.T2_DATE, dateMilis);
        values.put(DBConstants.T2_PARENTID, parentID);
        long id = db.insert(DBConstants.T2_NAME, null, values);
        db.close();
        return id;
    }

    public boolean updateTitle(long id, String title){
        String clause = "id="+id;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(DBConstants.T1_TITLE, title);
        int status = db.update(DBConstants.T1_NAME, value, clause, null);
        db.close();
        return status > 0;
    }

}
