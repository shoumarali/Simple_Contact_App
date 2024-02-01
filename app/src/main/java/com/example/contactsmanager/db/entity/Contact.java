package com.example.contactsmanager.db.entity;

import android.content.Context;

public class Contact {

    public static final String TABLE_NAME = "contacts";
    public static final String COLUMN_ID = "contact_id";
    public static final String COLUM_NAME="contact_name";
    public static final String COLUMN_EMAIL = "contact_email";

    private String name;
    private String email;
    private int id;

    public Contact(){

    }
    public Contact(String name, String email, int id){
        this.name=name;
        this.id=id;
        this.email=email;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static final String CREATE_TABLE=
            "CREATE TABLE "+TABLE_NAME+"("
            +COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
            +COLUM_NAME+" TEXT,"
            +COLUMN_EMAIL+" DATETIME DEFAULT CURRENT_TIMESTAMP"
            +")";
}
