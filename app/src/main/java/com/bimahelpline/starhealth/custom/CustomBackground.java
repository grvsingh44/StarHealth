package com.bimahelpline.starhealth.custom;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.bimahelpline.starhealth.MainActivity;
import com.bimahelpline.starhealth.database.OneSignalDBHelper;
import com.bimahelpline.starhealth.item.Item;
import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationReceivedResult;

import java.math.BigInteger;


public class CustomBackground extends NotificationExtenderService {

    private OneSignalDBHelper mDatabase;
    private Item item;
    private int count;

    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult notification) {
        boolean inApp = notification.isAppInFocus;
        OverrideSettings overrideSettings = new OverrideSettings();
        overrideSettings.extender = new NotificationCompat.Extender() {
            @Override
            public NotificationCompat.Builder extend(NotificationCompat.Builder builder) {
                return builder.setColor(new BigInteger("FF00FF00", 16).intValue());
            }
        };
        displayNotification(overrideSettings);
        return true;
    }

    public void onStart(@Nullable Intent intent, int startId) {
        super.onStart(intent, startId);
        makeShake();
    }


    public void makeShake(){
        count = getCount();
        if (count > 0){
            MainActivity.shakingBell.shake(count);
        }
        mDatabase.close();
    }

    public int getCount(){
        mDatabase = new OneSignalDBHelper(this);
        mDatabase.openOnsignalDB();
        mDatabase.getReadableDatabase();
        String countQuery = "select opened from notification where opened = 0";
        Cursor cursor = mDatabase.queryNotification(countQuery);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }
//
//    public void loadData() {
//        mDatabase = new OneSignalDBHelper(getApplicationContext());
//        mDatabase.openOnsignalDB();
//        mDatabase.getWritableDatabase();
//        mCursor = mDatabase.queryNotification("select * from notification order by datetime(created_time) DESC LIMIT 10");
//        if (mCursor != null) {
//            while (mCursor.moveToNext()) {
//                item = new Item();
//                item.setTitle(mCursor.getString(8));
//                item.setBody(mCursor.getString(9));
//                item.setTime(mCursor.getLong(11));
//                mArrayList.add(item);
//            }
//        }
//    }

}

