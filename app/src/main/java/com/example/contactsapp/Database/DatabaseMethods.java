package com.example.contactsapp.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.contactsapp.Model.Contact;
import java.util.ArrayList;

public class DatabaseMethods {

    public DatabaseMethods() {
    }

    public boolean addContact(DatabaseHelper database, Contact contact){
        SQLiteDatabase db = database.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.CONTACTS_COL1,contact.getName());
        contentValues.put(DatabaseHelper.CONTACTS_COL2,contact.getPhone());
        contentValues.put(DatabaseHelper.CONTACTS_COL3,contact.getEmail());
        contentValues.put(DatabaseHelper.CONTACTS_COL5,contact.getFavourite());

        long result = db.insert(DatabaseHelper.CONTACT_TABLE_NAME,null,contentValues);
        db.close();

        if (result == -1){
            return false;
        }else {
            return true;
        }
    }

    public ArrayList<Contact> getAllContacts(DatabaseHelper databaseHelper){
        ArrayList<Contact> contactList = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.CONTACT_TABLE_NAME,null);

        while (cursor.moveToNext()){
            Contact contact = new Contact();
            contact.setId(cursor.getInt(0));
            contact.setName(cursor.getString(1));
            contact.setPhone(cursor.getString(2));
            contact.setEmail(cursor.getString(3));
            contact.setProfile_photo(cursor.getBlob(4));
            contact.setFavourite(cursor.getString(5));
            contactList.add(contact);
        }
        return contactList;
    }

    public Contact getContact(DatabaseHelper databaseHelper,int id){
        Contact contact = new Contact();

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.CONTACT_TABLE_NAME + " WHERE " + DatabaseHelper.CONTACTS_COL0 + " like" + " '%" + id + "%'",null);
        while (cursor.moveToNext()){
            contact.setId(cursor.getInt(0));
            contact.setName(cursor.getString(1));
            contact.setPhone(cursor.getString(2));
            contact.setEmail(cursor.getString(3));
            contact.setProfile_photo(cursor.getBlob(4));
            contact.setFavourite(cursor.getString(5));
        }
        return contact;
    }

    public Integer deleteContact(DatabaseHelper databaseHelper,int id){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        return db.delete(DatabaseHelper.CONTACT_TABLE_NAME,DatabaseHelper.CONTACTS_COL0+ "=?",new String[]{String.valueOf(id)});
    }

    public boolean updateContact(DatabaseHelper databaseHelper,Contact contact,int id){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.CONTACTS_COL1,contact.getName());
        contentValues.put(DatabaseHelper.CONTACTS_COL2,contact.getPhone());
        contentValues.put(DatabaseHelper.CONTACTS_COL3,contact.getEmail());
        contentValues.put(DatabaseHelper.CONTACTS_COL4,contact.getProfile_photo());
        contentValues.put(DatabaseHelper.CONTACTS_COL5,contact.getFavourite());


        int update = db.update(DatabaseHelper.CONTACT_TABLE_NAME,contentValues,DatabaseHelper.CONTACTS_COL0 + "=? ",new String[]{String.valueOf(id)});
        if (update != 1){
            return false;
        }else {
            return true;
        }
    }

    public ArrayList<Contact> searchContact(DatabaseHelper databaseHelper,String searcHWord){
        ArrayList<Contact> searchList = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.CONTACT_TABLE_NAME + " WHERE " +
                DatabaseHelper.CONTACTS_COL1 + " like '%" + searcHWord + "%'",null);

        while (cursor.moveToNext()){
            Contact contact = new Contact();
            contact.setId(cursor.getInt(0));
            contact.setName(cursor.getString(1));
            contact.setPhone(cursor.getString(2));
            contact.setEmail(cursor.getString(3));
            contact.setProfile_photo(cursor.getBlob(4));
            contact.setFavourite(cursor.getString(5));
            searchList.add(contact);
        }
        return searchList;
    }

    public ArrayList<Contact> getAllFavourites(DatabaseHelper databaseHelper){
        ArrayList<Contact> favouriteList = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.CONTACT_TABLE_NAME + " WHERE " + DatabaseHelper.CONTACTS_COL5 + "='favourite'",null);

        while (cursor.moveToNext()){
            Contact contact = new Contact();
            contact.setId(cursor.getInt(0));
            contact.setName(cursor.getString(1));
            contact.setPhone(cursor.getString(2));
            contact.setEmail(cursor.getString(3));
            contact.setProfile_photo(cursor.getBlob(4));
            contact.setFavourite(cursor.getString(5));
            favouriteList.add(contact);
        }
        return favouriteList;
    }

    public ArrayList<Contact> searchFavourite(DatabaseHelper databaseHelper,String searcHWord){
        ArrayList<Contact> searchList = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.CONTACT_TABLE_NAME + " WHERE " +
                DatabaseHelper.CONTACTS_COL1 + " like '%" + searcHWord + "%'" + " AND " + DatabaseHelper.CONTACTS_COL5 + "='favourite'" ,null);

        while (cursor.moveToNext()){
            Contact contact = new Contact();
            contact.setId(cursor.getInt(0));
            contact.setName(cursor.getString(1));
            contact.setPhone(cursor.getString(2));
            contact.setEmail(cursor.getString(3));
            contact.setProfile_photo(cursor.getBlob(4));
            contact.setFavourite(cursor.getString(5));
            searchList.add(contact);
        }
        return searchList;
    }
}
