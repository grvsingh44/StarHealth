package com.bimahelpline.starhealth.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public class DatabaseHandler {

    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDatabaseHelper;

    public DatabaseHandler(Context context) {
        mDatabaseHelper = new DatabaseHelper(context);
    }

    public void createDatabase(){
        mDatabaseHelper.createDatabase();
    }

    public void open() throws SQLException {
        mDatabase = mDatabaseHelper.getWritableDatabase();
    }
    public void close() {
        mDatabase.close();
    }

    public String getDataORF(String suminsured, String age, String adult, String child, String zone){
        String [] columns = {DatabaseHelper.Premium};
        String [] selectionArgs = {suminsured, age, adult, child, zone};
        Cursor cursor = mDatabase.query(DatabaseHelper.TABLE_HEALTH_OPTIMA,
                columns, DatabaseHelper.SumInsured + "=? AND " +
                        DatabaseHelper.AgeRange + " =? AND " +
                        DatabaseHelper.Adult + " =? AND " +
                        DatabaseHelper.Child + " =? AND " +
                        DatabaseHelper.Zone + " =?",
                selectionArgs, null, null, null );
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()){
            int index0 = cursor.getColumnIndex(DatabaseHelper.Premium);
            int mPremiumAmount = cursor.getInt(index0);
            buffer.append(mPremiumAmount);
        }
        return buffer.toString();
    }

    public String getCompre(String suminsured, String age, String adult, String child){
        String [] columns = {DatabaseHelper.Premium};
        String [] selectionArgs = {suminsured, age, adult, child};
        Cursor cursor = mDatabase.query(DatabaseHelper.TABLE_COMPREHENSIVE,
                columns, DatabaseHelper.SumInsured + "=? AND " +
                        DatabaseHelper.AgeRange + " =? AND " +
                        DatabaseHelper.Adult + " =? AND " +
                        DatabaseHelper.Child + " =?",
                selectionArgs, null, null, null );
        StringBuffer buffer = new StringBuffer();
        while(cursor.moveToNext()){
            int index0 = cursor.getColumnIndex(DatabaseHelper.Premium);
            int mPremiumAmount = cursor.getInt(index0);
            buffer.append(mPremiumAmount);
        }
        return buffer.toString();
    }

    public String getcardiaccare(String suminsured, String age, String plan){
        String [] columns = {DatabaseHelper.Premium};
        String [] selectionArgs = {suminsured, age, plan};
        Cursor cursor = mDatabase.query(DatabaseHelper.TABLE_cardiaccare,
                columns, DatabaseHelper.SumInsured + "=? AND " +
                        DatabaseHelper.AgeRange + " =? AND " +
                        DatabaseHelper.Plan + " =?",
                selectionArgs, null, null, null );
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()){
            int index0 = cursor.getColumnIndex(DatabaseHelper.Premium);
            int mPremiumAmount = cursor.getInt(index0);
            buffer.append(mPremiumAmount);
        }
        return  buffer.toString();
    }

    public String getSenior(String suminsured, String age){
        String [] columns = {DatabaseHelper.Premium};
        String [] selectionArgs = {suminsured, age};
        Cursor cursor = mDatabase.query(DatabaseHelper.TABLE_SENIORCITIZEN,
                columns, DatabaseHelper.SumInsured + "=? AND " +
                        DatabaseHelper.AgeRange + " =?",
                selectionArgs, null, null, null );
        StringBuffer buffer = new StringBuffer();
        while(cursor.moveToNext()){
            int index0 = cursor.getColumnIndex(DatabaseHelper.Premium);
            int mPremiumAmount = cursor.getInt(index0);
            buffer.append(mPremiumAmount);
        }
        return buffer.toString();
    }

    public String getDiabetes(String suminsured, String age, String adult, String plan){
        String [] columns = {DatabaseHelper.Premium};
        String [] selectionArgs = {suminsured, age,adult, plan};
        Cursor cursor = mDatabase.query(DatabaseHelper.TABLE_DIABETES,
                columns, DatabaseHelper.SumInsured + "=? AND " +
                        DatabaseHelper.AgeRange + " =? AND " +
                        DatabaseHelper.Adult + " =? AND " +
                        DatabaseHelper.Plan + " =?",
                selectionArgs, null, null, null );
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()){
            int index0 = cursor.getColumnIndex(DatabaseHelper.Premium);
            int mPremiumAmount = cursor.getInt(index0);
            buffer.append(mPremiumAmount);
        }
        return  buffer.toString();
    }

    public String getSuper(String suminsured, String age, String adult, String child, String plan, String deductible){
        String [] columns = {DatabaseHelper.Premium};
        String [] selectionArgs = {suminsured, age, adult, child, plan, deductible};
        Cursor cursor = mDatabase.query(DatabaseHelper.TABLE_SUPER,
                columns, DatabaseHelper.SumInsured + "=? AND " +
                        DatabaseHelper.AgeRange + " =? AND " +
                        DatabaseHelper.Adult + " =? AND " +
                        DatabaseHelper.Child + " =? AND " +
                        DatabaseHelper.Plan + " =? AND " +
                        DatabaseHelper.Deductible + " =?",
                selectionArgs, null, null, null );
        StringBuffer buffer = new StringBuffer();
        while ( (cursor.moveToNext())){
            int index0 = cursor.getColumnIndex(DatabaseHelper.Premium);
            int mPremiumAmount = cursor.getInt(index0);
            buffer.append(mPremiumAmount);
        }
        return buffer.toString();
    }

    public String getMedi(String suminsured, String age, String zone){
        String [] columns = {DatabaseHelper.Premium};
        String [] selectionArgs = {suminsured, age, zone};
        Cursor cursor = mDatabase.query(DatabaseHelper.TABLE_MEDICLASSIC,
                columns, DatabaseHelper.SumInsured + " =? AND " +
                        DatabaseHelper.AgeRange + " =? AND " +
                        DatabaseHelper.Zone + " =?",
                selectionArgs, null, null, null);
        StringBuffer buffer = new StringBuffer();
        while ((cursor.moveToNext())){
            int index0 = cursor.getColumnIndex(DatabaseHelper.Premium);
            int mPremiumAmount = cursor.getInt(index0);
            buffer.append(mPremiumAmount);
        }
        return buffer.toString();
    }
}
