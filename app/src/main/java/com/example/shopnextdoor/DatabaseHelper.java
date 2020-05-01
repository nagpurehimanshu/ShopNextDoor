package com.example.shopnextdoor;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class DatabaseHelper extends SQLiteOpenHelper {

    static String name = "myDB";
    static int version = 1;

    String createCustomerTable = "CREATE TABLE if not exists \"customer\" (\n" +
            "\t\"id\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
            "\t\"name\"\tTEXT NOT NULL,\n" +
            "\t\"gender\"\tTEXT NOT NULL,\n" +
            "\t\"mobile\"\tNUMERIC NOT NULL UNIQUE,\n" +
            "\t\"address\"\tTEXT NOT NULL,\n" +
            "\t\"username\"\tTEXT NOT NULL UNIQUE,\n" +
            "\t\"password\"\tTEXT NOT NULL\n" +
            ")";

    public DatabaseHelper(Context context) {
        super(context, name, null, version);
        getWritableDatabase().execSQL(createCustomerTable);
    }

    public void insertUser(ContentValues contentValues){
        getWritableDatabase().insert( "customer", "", contentValues);
    }

    public boolean isLoginValid(String username, String password){
        String query = "select count(*) from customer where username='" + username + "' and password='" + password + "'";
        SQLiteStatement statement = getReadableDatabase().compileStatement(query);
        long l = statement.simpleQueryForLong();
        statement.close();

        if(l==1){
            return true;
        }
        else return false;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
