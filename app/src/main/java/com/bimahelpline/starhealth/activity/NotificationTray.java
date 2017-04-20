package com.bimahelpline.starhealth.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bimahelpline.starhealth.R;
import com.bimahelpline.starhealth.adapter.NotificationAdapter;
import com.bimahelpline.starhealth.database.OneSignalDBHelper;
import com.bimahelpline.starhealth.item.Item;

import java.util.ArrayList;

public class NotificationTray extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private OneSignalDBHelper mDatabase;
    private ArrayList<Item> mArrayList = new ArrayList<Item>();
    private Cursor mCursor;
    private NotificationAdapter mNotificationAdapter;
    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_tray);
        setTitle("Notification");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView = (RecyclerView)findViewById(R.id.CustomRecyclerView);
        loadData();
        mNotificationAdapter = new NotificationAdapter(this,mArrayList);
        if (mNotificationAdapter.getItemCount() > 0) {
            if (mRecyclerView != null) {
                mRecyclerView.setAdapter(mNotificationAdapter);
            }
            if (mRecyclerView != null) {
                mRecyclerView.setHasFixedSize(true);
            }
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        }
    }

    public void loadData(){
        mDatabase = new OneSignalDBHelper(NotificationTray.this);
        mDatabase.openOnsignalDB();
        mDatabase.getWritableDatabase();
        mCursor = mDatabase.queryNotification("select * from notification order by datetime(created_time) DESC LIMIT 10");
        if (mCursor != null) {
            while (mCursor.moveToNext()){
                item = new Item();
                item.setTitle(mCursor.getString(8));
                item.setBody(mCursor.getString(9));
                item.setTime(mCursor.getLong(11));
                mArrayList.add(item);
            }
        }
    }
}
