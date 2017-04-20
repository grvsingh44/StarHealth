package com.bimahelpline.starhealth.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;


public class OneSignalDBHelper extends SQLiteOpenHelper {

    private final Context mContext;
    public static final String DATABASE_PATH = "/data/data/com.bimahelpline.starhealth/databases/";
    public static final String ONESIGNAL_DB_NAME = "OneSignal.db";
    public static final int DB_VERSION = 2;
    private SQLiteDatabase mOnsignalDB;
    private OneSignalDBHelper mDatabase;

    public OneSignalDBHelper(Context context) {
        super(context, ONESIGNAL_DB_NAME, null, DB_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void openOnsignalDB() throws SQLException {
        String dbPath = DATABASE_PATH + ONESIGNAL_DB_NAME;
            mOnsignalDB  = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
        Toast.makeText(mContext, "One Signal database is open", Toast.LENGTH_SHORT).show();
    }

    public Cursor queryNotification(String query){
        return mOnsignalDB.rawQuery(query,null);
    }

    private boolean DBExists(){
        SQLiteDatabase db = null;
        try{
            String databasePath = DATABASE_PATH + ONESIGNAL_DB_NAME;
            db = SQLiteDatabase.openDatabase(databasePath, null,
                    SQLiteDatabase.OPEN_READWRITE);
            db.setLocale(Locale.getDefault());
        }catch (SQLiteException e){
            e.printStackTrace();
            Log.e("SqlHelpler"," database not found");
        }

        if(db != null){
            db.close();
        }
        return db !=null ? true : false;
    }
}
