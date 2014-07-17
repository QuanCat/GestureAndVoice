package com.mobile.gestureandvoice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Quan on 7/15/2014.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "contactData",
    TABLE_CONTACTS = "contacts",
    KEY_ID = "id",
    KEY_NAME = "name",
    KEY_PHONE = "phone",
    KEY_EMAIL = "email",
    KEY_ADDRESS = "address",
    KEY_IMAGEUri = "imageUri";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_CONTACTS + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT," +
                KEY_PHONE + " TEXT,"+ KEY_EMAIL + " TEXT,"+ KEY_ADDRESS + " TEXT,"+ KEY_IMAGEUri + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    public void createContact(Contact contact) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.get_name());
        values.put(KEY_NAME, contact.get_phone());
        values.put(KEY_EMAIL, contact.get_email());
        values.put(KEY_ADDRESS, contact.get_address());
        values.put(KEY_IMAGEUri, contact.get_imageUri().toString());

        db.insert(TABLE_CONTACTS, null, values);
        db.close();

    }

    //get contact
    public Contact getContact(int id) {
        SQLiteDatabase db = getReadableDatabase();
        //get each row of the database
        Cursor cursor = db.query(TABLE_CONTACTS, new String[] {KEY_ID, KEY_NAME, KEY_PHONE, KEY_EMAIL, KEY_ADDRESS, KEY_IMAGEUri},
                KEY_ID + "=?", new String[] {String.valueOf(id)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), Uri.parse(cursor.getString(5)));

        db.close();
        cursor.close();

        return contact;
    }
    //delete contact
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + "=?", new String[] {String.valueOf(contact.get_id())});
        db.close();
    }
    //get count
    public int getContactsCount() {
        //SELECT * FROM CONTACTS;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CONTACTS, null);
        int count = cursor.getCount();
        db.close();
        cursor.close();
        return count;
    }
    //update contact
    public int updateContact(Contact contact) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.get_name());
        values.put(KEY_NAME, contact.get_phone());
        values.put(KEY_EMAIL, contact.get_email());
        values.put(KEY_ADDRESS, contact.get_address());
        values.put(KEY_IMAGEUri, contact.get_imageUri().toString());

        int rowsAffected = db.update(TABLE_CONTACTS, values, KEY_ID + "=?", new String[] {String.valueOf(contact.get_id())});
        db.close();
        return rowsAffected;
    }

    //get Contacts List
    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<Contact>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CONTACTS, null);
        if (cursor.moveToFirst()) {
            do {
                contacts.add(new Contact(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                        cursor.getString(4), Uri.parse(cursor.getString(5))));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return contacts;
    }

}
