package com.example.player;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "users";//database adı

    private static final String db_name="Users";
    private static final String name="Name";
    private static final String surname="Surname";
    private static final String mail="Mail";
    private static final String phone="Phone";
    private static final String password="Password";



    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + db_name + "("
                + mail + " TEXT PRIMARY KEY,"
                + name + " TEXT,"
                + surname + " TEXT,"
                + phone+ " TEXT,"
                + password + " TEXT" + ")";

        db.execSQL(CREATE_TABLE);

    }



    public Boolean addUser(String name,String surname,String mail,String phone,String password){
        SQLiteDatabase MyDb= this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("Mail",mail);
        contentValues.put("Name",name);
        contentValues.put("Surname",surname);
        contentValues.put("Phone",phone);
        contentValues.put("Password",password);
        long result=MyDb.insert("Users",null,contentValues);
        if (result==-1)
            return false;
        else
            return true;
    }

    public Boolean checkmail(String mail){
        SQLiteDatabase MYDB=this.getWritableDatabase();
        Cursor cursor=MYDB.rawQuery("Select * from Users where mail=?",new String[]{mail});
        if (cursor.getCount()>0)
            return false;
        else
            return true;
    }

    public Boolean checkmailpass(String mail, String password){
        SQLiteDatabase MyDB= this.getWritableDatabase();
        Cursor cursor=MyDB.rawQuery("Select * from Users where Mail=? and Password=?",new String[]{mail,password});
        if (cursor.getCount()>0)
            return true;
        else
            return false;
    }




    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
