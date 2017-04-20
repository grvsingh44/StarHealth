package com.bimahelpline.starhealth.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bimahelpline.starhealth.custom.DatePickerFragment;
import com.bimahelpline.starhealth.R;
import com.bimahelpline.starhealth.database.DatabaseHandler;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Locale;

public class DiabetesIndividual extends AppCompatActivity {

    private static final String TAG = DiabetesIndividual.class.getSimpleName();

    static EditText mAge;
    final Context context = this;
    private DatabaseHandler mDatabaseSource;
    private int mCurrentYearInt;
    private InterstitialAd mInterstitialAd;
    private EditText mSumInsured,mAdult,mPlan;
    private boolean isFieldEmpty;
    private String s, s1, mAgeYear, age,sumInsured,adult,plan ,mBasePremium;
    private int mBasePremiumInt,mAgeInt, mAgeYearInt, day, month,mCurrentMonth,mCurrentDate;
    private float mTax;
    private float mTaxSum;
    private long mTotalPremium;
    private NumberFormat format;
    private FirebaseAnalytics firebaseAnalytics;
    private LinearLayout mAdContainer;
    private NativeExpressAdView mAdmobAdView;
    private AdRequest mAdmobAdRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diabetes);
//        AdView adView = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder()
//                .build();
//        adView.loadAd(adRequest);
//        NativeExpressAdView adView = (NativeExpressAdView)findViewById(R.id.adView);
//        AdRequest request = new AdRequest.Builder()
//                .addTestDevice("YOUR_DEVICE_ID")
//                .build();
//        adView.loadAd(request);
        loadAds();
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        requestNewInterstitial();
        mDatabaseSource = new DatabaseHandler(this);
        mDatabaseSource.open();
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        format = NumberFormat.getCurrencyInstance(Locale.getDefault());
        format.setCurrency(Currency.getInstance("INR"));
        mSumInsured = (EditText)findViewById(R.id.etSumInsured);
        mAge = (EditText)findViewById(R.id.etAge);
        mAdult = (EditText)findViewById(R.id.etAdult);
        mPlan = (EditText)findViewById(R.id.etPlan);
        mSumInsured.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSI();
            }
        });
        mAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDOB(v);
            }
        });
        mAdult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAdult();
            }
        });
        mPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPlan();
            }
        });
        Button mCalButton = (Button)findViewById(R.id.calButton);
        mCalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if (mSumInsured.getText().toString().contentEquals("") || mAge.getText().toString().contentEquals("") || mPlan.getText().toString().contentEquals("") || mAdult.getText().toString().contentEquals("")){
                        isFieldEmpty = true;
                        AlertDialog.Builder builder = new AlertDialog.Builder(DiabetesIndividual.this);
                        builder.setMessage("Please Enter Required Fields")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                }).create().show();
                    }else {
                        isFieldEmpty = false;
                    }
                    mCurrentYearInt = Calendar.getInstance().get(Calendar.YEAR);
                    mCurrentDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                    mCurrentMonth = (Calendar.getInstance().get(Calendar.MONTH)+1);
                    s= mAge.getText().toString().trim();
                    String s1 = s.substring(6);
                    String s2 = s.substring(0,2);
                    String s3 = s.substring(3,5);
                    mAgeInt = Integer.parseInt(s1);
                    day = Integer.parseInt(s2);
                    month = Integer.parseInt(s3);
                    mAgeYearInt = mCurrentYearInt - mAgeInt;
                    if(month > mCurrentMonth){
                        --mAgeYearInt;
                    }else if (month == mCurrentMonth){
                        if (day > mCurrentDate){
                            --mAgeYearInt;
                        }else {
                            mAgeYearInt = mCurrentYearInt - mAgeInt;
                        }
                    }
                    if(mAgeYearInt < 18){
                        isFieldEmpty = true;
                        AlertDialog.Builder builder = new AlertDialog.Builder(DiabetesIndividual.this);
                        builder.setMessage("Age Can't Be Less Than 18 Years")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                }).create().show();
                    }else{
                        isFieldEmpty = false;
                    }
                    mAgeYear = ""+mAgeYearInt;
                    sumInsured = mSumInsured.getText().toString();
                    plan = mPlan.getText().toString();
                    adult = mAdult.getText().toString();
                    if(mAgeYearInt >= 18 && mAgeYearInt <= 30){
                        mAgeYearInt = 18;
                        age = ""+mAgeYearInt;
                        mBasePremium = mDatabaseSource.getDiabetes(sumInsured,age,adult,plan);
                    }
                    if(mAgeYearInt >= 31 && mAgeYearInt <= 35){
                        mAgeYearInt = 31;
                        age = ""+mAgeYearInt;
                        mBasePremium = mDatabaseSource.getDiabetes(sumInsured,age,adult,plan);
                    }
                    if(mAgeYearInt >= 36 && mAgeYearInt <= 40){
                        mAgeYearInt = 36;
                        age = ""+mAgeYearInt;
                        mBasePremium = mDatabaseSource.getDiabetes(sumInsured,age,adult,plan);
                    }
                    if(mAgeYearInt >= 41 && mAgeYearInt <= 45){
                        mAgeYearInt = 41;
                        age = ""+mAgeYearInt;
                        mBasePremium = mDatabaseSource.getDiabetes(sumInsured,age,adult,plan);
                    }
                    if(mAgeYearInt >= 46 && mAgeYearInt <= 50){
                        mAgeYearInt = 31;
                        age = ""+mAgeYearInt;
                        mBasePremium = mDatabaseSource.getDiabetes(sumInsured,age,adult,plan);
                    }
                    if(mAgeYearInt >= 51 && mAgeYearInt <= 55){
                        mAgeYearInt = 51;
                        age = ""+mAgeYearInt;
                        mBasePremium = mDatabaseSource.getDiabetes(sumInsured,age,adult,plan);
                    }
                    if(mAgeYearInt >= 56 && mAgeYearInt <= 60){
                        mAgeYearInt = 56;
                        age = ""+mAgeYearInt;
                        mBasePremium = mDatabaseSource.getDiabetes(sumInsured,age,adult,plan);
                    }
                    if(mAgeYearInt >= 61 && mAgeYearInt <= 65){
                        mAgeYearInt = 61;
                        age = ""+mAgeYearInt;
                        mBasePremium = mDatabaseSource.getDiabetes(sumInsured,age,adult,plan);
                    }
                    if(mAgeYearInt >= 66 && mAgeYearInt <= 70){
                        mAgeYearInt = 66;
                        age = ""+mAgeYearInt;
                        mBasePremium = mDatabaseSource.getDiabetes(sumInsured,age,adult,plan);
                    }
                    if(mAgeYearInt >= 71 && mAgeYearInt <= 75){
                        mAgeYearInt = 31;
                        age = ""+mAgeYearInt;
                        mBasePremium = mDatabaseSource.getDiabetes(sumInsured,age,adult,plan);
                    }
                    if(mAgeYearInt >= 76 && mAgeYearInt <= 80){
                        mAgeYearInt = 76;
                        age = ""+mAgeYearInt;
                        mBasePremium = mDatabaseSource.getDiabetes(sumInsured,age,adult,plan);
                    }
                    if(mAgeYearInt == 81){
                        mAgeYearInt = 81;
                        age = ""+mAgeYearInt;
                        mBasePremium = mDatabaseSource.getDiabetes(sumInsured,age,adult,plan);
                    }else if (mAgeYearInt > 81){
                        isFieldEmpty = true;
                        AlertDialog.Builder builder = new AlertDialog.Builder(DiabetesIndividual.this);
                        builder.setMessage("Age Can't be more than 81 Years !")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                }).create().show();
                    }
                    mBasePremiumInt = Integer.parseInt(mBasePremium.trim());
                    mTax = 15.0f;
                    mTaxSum = (mBasePremiumInt * mTax)/100;
                    mTotalPremium = Math.round(mBasePremiumInt + mTaxSum);
                }catch (Exception e){
                    e.printStackTrace();
                }
                if (!isFieldEmpty){
                    if(mInterstitialAd.isLoaded()){
                        mInterstitialAd.show();
                    }else {
                        result();
                    }
                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            requestNewInterstitial();
                            result();
                        }
                    });
                }
                Log.d(TAG,""+mCurrentMonth+" "+month+" "+" -- CurrentDate: "+mCurrentDate+"-- Selected Date:"+day+"-- Year:"+mAgeYear);
            }
        });

    }

    private void result() {
        final AppCompatDialog dialog = new AppCompatDialog(context, R.style.Theme_AppCompat_Dialog_MinWidth);
        dialog.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        TextView tvSISummary = (TextView)dialog.findViewById(R.id.tvSISummary);
        tvSISummary.setText(sumInsured);
        TextView tvSIBasePremium = (TextView)dialog.findViewById(R.id.tvSIBasePremium);
        tvSIBasePremium.setText(mBasePremium);
        TextView tvAge = (TextView)dialog.findViewById(R.id.tvDialogAge);
        tvAge.setText(mAgeYear);
        TextView tvAdult = (TextView)dialog.findViewById(R.id.tvAdult);
        tvAdult.setText(adult);
        TextView tvLabelChild = (TextView)dialog.findViewById(R.id.tvChildLabel);
        tvLabelChild.setVisibility(View.GONE);
        TextView tvChild = (TextView)dialog.findViewById(R.id.tvChild);
        tvChild.setVisibility(View.GONE);
        TextView tvPlan = (TextView)dialog.findViewById(R.id.tvPlan);
        tvPlan.setText(plan);
        TextView tvLabelZone = (TextView)dialog.findViewById(R.id.tvLabelZone);
        tvLabelZone.setVisibility(View.GONE);
        TextView tvZone = (TextView)dialog.findViewById(R.id.tvZone);
        tvZone.setVisibility(View.GONE);
        TextView tvLabelDeductible = (TextView)dialog.findViewById(R.id.tvLabelDeductible);
        tvLabelDeductible.setVisibility(View.GONE);
        TextView tvDeductible = (TextView)dialog.findViewById(R.id.tvDeductible);
        tvDeductible.setVisibility(View.GONE);
        TextView tvTax = (TextView)dialog.findViewById(R.id.tvTax);
        tvTax.setText(Float.toString(mTaxSum));
        TextView tvTotalPremium = (TextView)dialog.findViewById(R.id.tvTotalPremium);
        tvTotalPremium.setText(Float.toString(mTotalPremium));
        Button shareButton = (Button)dialog.findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID,"Share_Button_Diabetes");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,"Share_Button_Diabetes");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE,"ShareButton");
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Dear Customer"+'\n'+ "Premium Calculation of Star Health Comprehensive Individual Policy as below :"+
                                '\n'+"Sum Insured : "+sumInsured+
                                '\n'+"Date Of Birth : "+s+
                                '\n'+"Age : "+mAgeYear+
                                '\n'+"Base Premium : "+mBasePremium+
                                '\n'+"Plan : "+plan+
                                '\n'+"Member : "+adult+
                                '\n'+"Tax @15% : "+"+ "+Float.toString(mTaxSum)+
                                '\n'+"Total Premium : "+Float.toString(mTotalPremium));
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void requestNewInterstitial() {
        AdRequest adRequest1 = new AdRequest.Builder()
                .build();
        mInterstitialAd.loadAd(adRequest1);
    }

    private void selectSI() {
        final CharSequence[] items = {"300000","400000","500000","1000000"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Sum Insured")
                .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSumInsured.setText(items[which]);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create().show();
    }

    private void selectAdult() {
        final CharSequence[] items = {"1","2"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Member")
                .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAdult.setText(items[which]);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create().show();
    }

    private void selectPlan() {
        final CharSequence[] items = {"A","B"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Plan")
                .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPlan.setText(items[which]);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create().show();
    }


    public void selectDOB(View v) {
        new DatePickerFragment((EditText) v).show(getSupportFragmentManager(), "datePicker");
    }

    private void loadAds() {
        // find out the width of the device in dp
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float deviceWidthInDp = displayMetrics.widthPixels / displayMetrics.density;

        int adWidth = (int)(deviceWidthInDp);
        mAdContainer = (LinearLayout)findViewById(R.id.adViewLayout);
        mAdContainer.removeAllViews();
        mAdmobAdView = new NativeExpressAdView(this);
        mAdmobAdView.setAdSize(new AdSize(adWidth, 250)); // AdSize(width, height), height ranges from 80 to 1200
        mAdContainer.addView(mAdmobAdView);
        mAdmobAdView.setAdUnitId("ca-app-pub-9999828043873612/4956357687"); // set your admob id
        mAdmobAdRequest = new AdRequest.Builder()
                .build();
        mAdmobAdView.loadAd(mAdmobAdRequest);
    }

}
