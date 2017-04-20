package com.bimahelpline.starhealth.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bimahelpline.starhealth.custom.DatePickerFragment;
import com.bimahelpline.starhealth.R;
import com.bimahelpline.starhealth.database.DatabaseHandler;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.text.NumberFormat;
import java.util.Calendar;

public class SeniorCitizens extends AppCompatActivity {

    private String url = "https://retail.starhealth.in/agentpolicies/redcarpet/agentnew?intermediaryCode=BA0000195747&officeCode=161111&smCode=SH29866&type=AGENT",title = "Star Health - Senior Citizen";
    static EditText mAge;
    final Context context = this;
    private DatabaseHandler mDatabaseSource;
    private int mCurrentYearInt;
    private InterstitialAd mInterstitialAd;
    private EditText mSumInsured;
    private boolean isFieldEmpty;
    private String s, s1, mAgeYear, age,SumInsured,adult,child,mBasePremium;
    private int mBasePremiumInt,mAgeInt, mAgeYearInt,mCurrentDate,mCurrentMonth,day,month;
    private float mTax;
    private float mTaxSum;
    private long mTotalPremium;
    private NumberFormat format;
    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senior_citizens);
        getSupportActionBar().setTitle(title);
//        AdView adView = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder()
//                .build();
//        adView.loadAd(adRequest);
        NativeExpressAdView adView = (NativeExpressAdView)findViewById(R.id.adView);
        AdRequest request = new AdRequest.Builder()
                .addTestDevice("YOUR_DEVICE_ID")
                .build();
        adView.loadAd(request);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        requestNewInterstitial();
        mDatabaseSource = new DatabaseHandler(this);
        mDatabaseSource.open();
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mSumInsured = (EditText)findViewById(R.id.etSumInsured);
        mAge = (EditText)findViewById(R.id.etAge);
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
        Button mCalButton = (Button)findViewById(R.id.calButton);
        mCurrentYearInt = Calendar.getInstance().get(Calendar.YEAR);
        mCurrentDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        mCurrentMonth = (Calendar.getInstance().get(Calendar.MONTH)+1);
        mCalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if (mSumInsured.getText().toString().contentEquals("") || mAge.getText().toString().contentEquals("")){
                        isFieldEmpty = true;
                        AlertDialog.Builder builder = new AlertDialog.Builder(SeniorCitizens.this);
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
                        }
                    }
                    mAgeYear = ""+mAgeYearInt;
                    SumInsured = mSumInsured.getText().toString();
                    if(mAgeYearInt >= 60){
                        mAgeYearInt = 60;
                        age = ""+mAgeYearInt;
                        mBasePremium = mDatabaseSource.getSenior(SumInsured,age);
                    }else if (mAgeYearInt < 60){
                        isFieldEmpty = true;
                        AlertDialog.Builder builder = new AlertDialog.Builder(SeniorCitizens.this);
                        builder.setMessage("Age Can't be less than 60 Years !")
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
            }
        });
    }

    private void result() {
        final AppCompatDialog dialog = new AppCompatDialog(context, R.style.Theme_AppCompat_Dialog_MinWidth);
        dialog.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        TextView tvSISummary = (TextView)dialog.findViewById(R.id.tvSISummary);
        tvSISummary.setText(SumInsured);
        TextView tvSIBasePremium = (TextView)dialog.findViewById(R.id.tvSIBasePremium);
        tvSIBasePremium.setText(mBasePremium);
        TextView tvAge = (TextView)dialog.findViewById(R.id.tvDialogAge);
        tvAge.setText(mAgeYear);
        TextView tvLabelAdult = (TextView)dialog.findViewById(R.id.tvAdultLabel);
        tvLabelAdult.setVisibility(View.GONE);
        TextView tvAdult = (TextView)dialog.findViewById(R.id.tvAdult);
        tvAdult.setVisibility(View.GONE);
        TextView tvLabelChild = (TextView)dialog.findViewById(R.id.tvChildLabel);
        tvLabelChild.setVisibility(View.GONE);
        TextView tvChild = (TextView)dialog.findViewById(R.id.tvChild);
        tvChild.setVisibility(View.GONE);
        TextView tvLabelPlan = (TextView)dialog.findViewById(R.id.tvLabelPlan);
        tvLabelPlan.setVisibility(View.GONE);
        TextView tvTPA = (TextView)dialog.findViewById(R.id.tvPlan);
        tvTPA.setVisibility(View.GONE);
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
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID,"Share_Button_SeniorCitizen");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,"Share_Button_SeniorCitizen");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE,"ShareButton");
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Dear Customer"+'\n'+ "Premium Calculation of Star Health Senior Citizen Policy as below :"+
                                '\n'+"Sum Insured : "+SumInsured+
                                '\n'+"Date Of Birth : "+s+
                                '\n'+"Age : "+mAgeYear+
                                '\n'+"Base Premium : "+mBasePremium+
                                '\n'+"Tax @15% : "+"+ "+Float.toString(mTaxSum)+
                                '\n'+"Total Premium : "+Float.toString(mTotalPremium)+
                                "\n"+"Product Brochures"+
                                "\n"+"https://goo.gl/IrCHOM");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void selectSI() {
        final CharSequence[] items = {"100000","200000","300000","400000","500000","750000","1000000"};
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

    public void selectDOB(View v) {
        new DatePickerFragment((EditText) v).show(getSupportFragmentManager(), "datePicker");
    }

    private void requestNewInterstitial() {
        AdRequest adRequest1 = new AdRequest.Builder()
                .build();
        mInterstitialAd.loadAd(adRequest1);
    }

}
