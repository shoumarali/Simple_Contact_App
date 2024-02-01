package com.example.contactsmanager.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.contactsmanager.db.entity.Contact;

import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "contact_db";
    public DataBaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Contact.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+Contact.TABLE_NAME);
        onCreate(db);
    }

    public long insertContact(String name, String email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values =new ContentValues();

        values.put(Contact.COLUM_NAME, name);

        values.put(Contact.COLUMN_EMAIL, email);
        long id = db.insert(Contact.TABLE_NAME, null, values);

        db.close();
        return id;
    }

    public Contact getContact(long id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =db.query(
                Contact.TABLE_NAME,
                new String[]{
                        Contact.COLUMN_ID,
                        Contact.COLUM_NAME,
                        Contact.COLUMN_EMAIL
                },Contact.COLUMN_ID+ " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null,
                null
                );
        if(cursor != null ){
            cursor.moveToFirst();
        }
        Contact contact=new Contact(
                cursor.getString(cursor.getColumnIndexOrThrow(Contact.COLUM_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(Contact.COLUMN_EMAIL)),
                cursor.getInt(cursor.getColumnIndexOrThrow(Contact.COLUMN_ID))
                );
        cursor.close();
        db.close();
        return contact;
    }

    public ArrayList<Contact> getAllContacts() {
       ArrayList<Contact> output = new ArrayList<>();
       String query = "SELECT * FROM "+Contact.TABLE_NAME+" ORDER BY "+Contact.COLUMN_ID+" DESC";

        SQLiteDatabase db =this.getWritableDatabase();

        Cursor cursor=db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do {
                Contact contact=new Contact();
                contact.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Contact.COLUMN_ID)));
                contact.setName(cursor.getString(cursor.getColumnIndexOrThrow(Contact.COLUM_NAME)));
                contact.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(Contact.COLUMN_EMAIL)));
                output.add(contact);
            }while(cursor.moveToNext());
        }
        db.close();
        return output;
    }

    public int UpdateContact(Contact contact){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Contact.COLUMN_EMAIL,contact.getEmail());
        contentValues.put(Contact.COLUM_NAME,contact.getName());
        return db.update(
                Contact.TABLE_NAME,
                contentValues,
                Contact.COLUMN_ID+" = ? ",
                new String[]{String.valueOf(contact.getId())}
                );
    }

    public void deleteContact(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(
                Contact.TABLE_NAME,
                Contact.COLUMN_ID+" = ? ",
                new String[]{String.valueOf(contact.getId())}
                );
        db.close();
    }
}
