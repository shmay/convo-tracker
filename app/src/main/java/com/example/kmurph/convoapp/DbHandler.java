package com.example.kmurph.convoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;

public class DbHandler extends SQLiteOpenHelper {
    public static final String TAG = "DbHandler";

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "convo.db";
    public static final String TABLE_PEOPLE = "people";
    public static final String TABLE_NOTES= "notes";


    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";

    public static final String NOTE_COLUMN = "note";
    public static final String PERSONID_COLUMN = "person_id";

    public DbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("on create", "asdf");
        String CREATE_PEOPLE_TABLE = "CREATE TABLE " +
                TABLE_PEOPLE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_NAME
                + " TEXT" + ")";

        db.execSQL(CREATE_PEOPLE_TABLE);

        String CREATE_NOTES_TABLE = "CREATE TABLE notes" +
                "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," + "note"
                + " TEXT" + ", person_id INTEGER)";

        db.execSQL(CREATE_NOTES_TABLE);
    }

    public void addPerson(String name) {
        Log.d(TAG, "add person: " + name);
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_PEOPLE, null, values);
        db.close();
    }

    public Note getNoteFromPos(int pos, Person person) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select _id, note from (" +
                "SELECT _id, note, (SELECT COUNT(*) " +
                                 "FROM notes AS t2 " +
                                 "WHERE t2.person_id = " + person.getID() + " AND t2._id <= t1._id) AS row_num " +
                "FROM notes AS t1 WHERE t1.person_id = " + person.getID() + " ) as notes_w_rows " +
                "where notes_w_rows.row_num = " + (pos + 1);

        Cursor cursor = db.rawQuery(query, null);

        Note note = new Note();

        if (cursor.moveToFirst()) {
            note.setID(Integer.parseInt(cursor.getString(0)));
            note.setNote(cursor.getString(1));

            cursor.close();
        } else {
            note = null;
        }

        Log.d(TAG, "note: " + note.getNote());
        return note;
    }

    public void addNote(String name, Person person) {
        Log.d(TAG, "add note: " + name);
        ContentValues values = new ContentValues();
        values.put(NOTE_COLUMN, name);
        values.put(PERSONID_COLUMN, person.getID());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_NOTES, null, values);
        db.close();
    }

    public Person findPerson(String name) {
        Log.d(TAG, "find person: " + name);

        String query = "Select * FROM " + TABLE_PEOPLE + " WHERE " + COLUMN_NAME + " =  \"" + name + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Person person = new Person();

        Log.d("DbHandler","yup");

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            person.setID(Integer.parseInt(cursor.getString(0)));
            person.setName(cursor.getString(1));
            Log.d("setName", cursor.getString(1));
            cursor.close();
        } else {
            Log.d(TAG, "nope");

            person = null;
        }
        db.close();
        return person;
    }

    public ArrayList getAll()
    {
        ArrayList array_list = new ArrayList();
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from people", null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }

    public long noteCountForPerson(Person person) {
        SQLiteDatabase db = this.getReadableDatabase();
        SQLiteStatement s = db.compileStatement( "select count(*) from notes where notes.person_id = " + person.getID() );

        long count = s.simpleQueryForLong();

        return count;
    }

    public void updateName(String newName, Person person) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME,newName);

        db.update(TABLE_PEOPLE, cv, "_id "+"="+person.getID(), null);
    }

    public void updateNote(String newNote, Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NOTE_COLUMN,newNote);

        db.update(TABLE_NOTES, cv, "_id "+"="+note.getID(), null);
    }

    public ArrayList notesForPerson(Person person)
    {
        ArrayList array_list = new ArrayList();
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from notes where notes.person_id = " + person.getID(), null );
        Log.d(TAG, "res: " + res);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(NOTE_COLUMN)));
            res.moveToNext();
        }
        return array_list;
    }

    public void deleteNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NOTES, COLUMN_ID + " = ?", new String[] { String.valueOf(note.getID()) });

        db.close();
    }

    public boolean deletePerson(String name) {

        boolean result = false;

        String query = "Select * FROM " + TABLE_PEOPLE + " WHERE " + COLUMN_NAME + " =  \"" + name + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Person person = new Person();

        if (cursor.moveToFirst()) {
            person.setID(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_PEOPLE, COLUMN_ID + " = ?", new String[] { String.valueOf(person.getID()) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

        Log.d(TAG, "onUpgrade: " + oldVersion + " : " + newVersion);



    }

}