package com.bimahelpline.starhealth;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bimahelpline.starhealth.activity.NotificationTray;
import com.bimahelpline.starhealth.custom.CustomBackground;
import com.bimahelpline.starhealth.custom.ShakingBell;
import com.bimahelpline.starhealth.database.DatabaseHelper;
import com.bimahelpline.starhealth.database.OneSignalDBHelper;
import com.bimahelpline.starhealth.item.Item;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private static final int PERMS_REQUEST_CODE = 123;
    private CardView mIndividual, mFamily,mHospital,mCard4, mCard5, mCard6;
    private DatabaseHelper mDatabaseHelper;
    String SHARED_PREF = "star_pref";
    final ArrayList<Item> itemArrayList = new ArrayList<Item>();
    int _id;
    private OneSignalDBHelper mDatabase;
    public static ShakingBell shakingBell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Star Health");
        getSupportActionBar().openOptionsMenu();
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3940256099942544~3347511713");
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("AA94D1D52E269355E240E780B711F71A")
                .build();
        adView.loadAd(adRequest);
        mDatabaseHelper = new DatabaseHelper(this);
        mDatabaseHelper.createDatabase();

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                Manifest.permission.CALL_PHONE},1);

        shakingBell = (ShakingBell)findViewById(R.id.shakeBell);
        shakingBell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NotificationTray.class));
            }
        });

//        ArrayList<Item> items = new ArrayList<Item>();
//        int i = items.size();
//        if (i > 0){
//            shakingBell.shake(i);
//

        mIndividual = (CardView) findViewById(R.id.card1);
        mFamily = (CardView) findViewById(R.id.card2);
        mIndividual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Individual.class);
                startActivity(i);
            }
        });
        mFamily = (CardView) findViewById(R.id.card2);
        mFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Family.class);
                startActivity(i);
            }
        });
        mHospital = (CardView)findViewById(R.id.card3);
        mHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Hospital.class);
                startActivity(i);
            }
        });
        mCard4 = (CardView)findViewById(R.id.card4);
        mCard4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Motor.class);
                startActivity(i);
            }
        });
        mCard5 = (CardView)findViewById(R.id.card5);
        mCard5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, JoinUs.class);
                startActivity(i);
            }
        });
        mCard6 = (CardView)findViewById(R.id.card6);
        mCard6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, News.class);
                startActivity(i);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.miCompose:
                onComposeAction();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void onComposeAction() {
        if (hasPermission()){
            callSupport();
        }else {
            requestParms();
        }
    }

    private void callSupport() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Need Support ?");
        builder.setMessage("Do you need call support.\nPress Call button to dial.");
        builder.setIcon(R.drawable.telemarketer);
        builder.setPositiveButton("Call", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:+919213444222"));
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).create().show();
    }

    private boolean hasPermission(){
        int res = 0;
        String [] permissions = new String[]{Manifest.permission.CALL_PHONE};
        for (String parms : permissions){
            res = checkCallingOrSelfPermission(parms);
            if(!(res == PackageManager.PERMISSION_GRANTED)){
                return false;
            }
        }
        return true;
    }

    private void requestParms(){
        String [] permissions = new String[]{Manifest.permission.CALL_PHONE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(permissions,PERMS_REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean allowed = true;

        switch (requestCode){
            case PERMS_REQUEST_CODE:
                for(int res : grantResults){
                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
                }
                break;
            default:
                allowed = false;
                break;
        }
        if (!allowed) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog_MinWidth);
                    builder
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Request Permission !")
                            .setMessage("In order to provide better support. Bimahelpline needs phone permission")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestParms();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).create().show();
                }
            }
        }
    }

    // Add to each long-lived activity
    @Override
    protected void onResume() {
        super.onResume();

    }

    // for Android, you should also log app deactivation
    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

}

