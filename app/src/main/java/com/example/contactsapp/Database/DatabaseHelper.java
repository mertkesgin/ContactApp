package com.example.contactsapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "contacts.db";

    public static final String CONTACT_TABLE_NAME = "contacts_table";
    public static final String CONTACTS_COL0 = "ID";
    public static final String CONTACTS_COL1 = "NAME";
    public static final String CONTACTS_COL2 = "PHONE_NUMBER";
    public static final String CONTACTS_COL3 = "EMAIL";
    public static final String CONTACTS_COL4 = "PROFILE_PHOTO";
    public static final String CONTACTS_COL5 = "FAVOURITE";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " +
                CONTACT_TABLE_NAME + " ( " +
                CONTACTS_COL0 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CONTACTS_COL1 + " TEXT, " +
                CONTACTS_COL2 + " TEXT, " +
                CONTACTS_COL3 + " TEXT, " +
                CONTACTS_COL4 + " BLOB, " +
                CONTACTS_COL5 + " TEXT )";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CONTACT_TABLE_NAME);
        onCreate(db);
    }
}
