package com.bimahelpline.starhealth;

import android.app.Application;
import android.database.Cursor;
import android.util.Log;

import com.bimahelpline.starhealth.custom.AppNotifyHandler;
import com.bimahelpline.starhealth.custom.ConnectionDetector;
import com.bimahelpline.starhealth.database.OneSignalDBHelper;
import com.bimahelpline.starhealth.item.Item;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.onesignal.OneSignal;

import java.util.ArrayList;


public class MyApp extends Application {

    private FirebaseAnalytics mFirebaseAnalytics;
    private static MyApp mInstance;
    private OneSignalDBHelper mDatabase;
    private Cursor mCursor;
    private ArrayList<Item> mArrayList = new ArrayList<Item>();

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
//        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
//        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.None)
//                .setNotificationOpenedHandler(new AppNotificationOpenHandler())
                .setNotificationReceivedHandler(new AppNotifyHandler())
                .init();
        OneSignal.enableSound(true);
        OneSignal.enableVibrate(true);
        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                Log.d("debug", "User:" + userId);
                if (registrationId != null)
                    Log.d("debug", "registrationId:" + registrationId);
            }
        });
    }

    public static synchronized MyApp getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectionDetector.ConnectivityReceiverListener listener) {
        ConnectionDetector.connectivityReceiverListener = listener;
    }


//    public class AppNotificationOpenHandler implements OneSignal.NotificationOpenedHandler {
//        @Override
//        public void notificationOpened(OSNotificationOpenResult result) {
//            OSNotification notification = result.notification;
//            OSNotificationAction.ActionType actionType = result.action.type;
////            String title = result.notification.payload.title;
////            String body = result.notification.payload.body;
////
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//
//        }
//
//    }


}
