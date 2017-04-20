package com.bimahelpline.starhealth.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;


public class DatabaseHelper extends SQLiteOpenHelper {

    public SQLiteDatabase db;
    private final Context mContext;
    public static final String DATABASE_PATH = "/data/data/com.bimahelpline.starhealth/databases/";
    public static final String DATABASE_NAME = "starhealth";
    public static final int DB_VERSION = 8;
    public static final String TABLE_HEALTH_OPTIMA="familyoptima";
    public static final String TABLE_COMPREHENSIVE="comprehensive";
    public static final String TABLE_cardiaccare="cardiaccare";
    public static final String TABLE_SENIORCITIZEN="seniorcitizen";
    public static final String TABLE_DIABETES="diabetes";
    public static final String TABLE_MEDICLASSIC="mediclassic";
    public static final String TABLE_SUPER="supersurplus";

    public static final String SumInsured = "SumInsured";
    public static final String AgeRange = "Age";
    public static final String Plan = "Plan";
    public static final String Deductible = "Deductible";
    public static final String Premium = "Premium";
    public static final String Adult = "Adult";
    public static final String Child = "Child";
    public static final String Zone = "Zone";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion > oldVersion){
            copyDBFromResource();
        }
    }

    public void open() throws SQLException {
        this.getWritableDatabase();
    }

    public void createDatabase(){
        createDB();
    }

    private void createDB(){
        boolean dbExist = DBExists();
        if(!dbExist){
            this.getReadableDatabase();
            copyDBFromResource();
        }
    }

    private boolean DBExists(){
        SQLiteDatabase db = null;
        try{
            String databasePath = DATABASE_PATH + DATABASE_NAME;
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

    private void copyDBFromResource(){
        InputStream inputStream = null;
        OutputStream outputStream = null;
        String dbFilePath = DATABASE_PATH + DATABASE_NAME;
        try {
            inputStream = mContext.getAssets().open(DATABASE_NAME);
            outputStream = new FileOutputStream(dbFilePath);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0 ){
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        }catch (IOException e){
            throw new Error("Problem copying database.");
        }
    }
}
