package com.bimahelpline.starhealth.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
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
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Locale;

public class MediClassic extends AppCompatActivity {

    private String title = "MediClassic-Individual";
    private String url = "https://retail.starhealth.in/agentpolicies/mcinew/agentnew?intermediaryCode=BA0000195747&officeCode=161111&smCode=SH29866&type=AGENT";
    private DatabaseHandler mDatabaseSource;
    private int mCurrentYearInt;
    private EditText mSumInsured, mZone;
    static EditText mAge;
    private String s, s1, mAgeYear, Age,SumInsured,Zone ,mBasePremium;
    private int mBasePremiumInt,mAgeInt, mAgeYearInt,day,month,mCurrentDate,mCurrentMonth;
    private boolean isFieldEmpty;
    private float mTax;
    private float mTaxSum;
    private long mTotalPremium;
    private InterstitialAd mInterstitialAd;
    final Context context = this;
    private NumberFormat format;
    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medi_classic);
        getSupportActionBar().setTitle(title);
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);
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
        mZone = (EditText)findViewById(R.id.etZone);
        final Button mButton = (Button)findViewById(R.id.calButton);
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
        mZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectZone();
            }
        });
        mCurrentYearInt = Calendar.getInstance().get(Calendar.YEAR);
        mCurrentDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        mCurrentMonth = (Calendar.getInstance().get(Calendar.MONTH)+1);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if (mSumInsured.getText().toString().contentEquals("") || mAge.getText().toString().contentEquals("") || mZone.getText().toString().contentEquals("")){
                        isFieldEmpty = true;
                        AlertDialog.Builder builder = new AlertDialog.Builder(MediClassic.this);
                        builder.setMessage("Please Enter All Required Field")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                }).create().show();
                    }else {
                        isFieldEmpty = false;
                    }
                    s = mAge.getText().toString().trim();
                    s1 = s.substring(6);
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
                    Zone = mZone.getText().toString();
                    if(mAgeYearInt >= 0 && mAgeYearInt <= 35){
                        mAgeYearInt = 0;
                        Age = ""+mAgeYearInt;
                        mBasePremium = mDatabaseSource.getMedi(SumInsured,Age,Zone);
                    }
                    if (mAgeYearInt >=36 && mAgeYearInt <= 45){
                        mAgeYearInt = 36;
                        Age = ""+mAgeYearInt;
                        mBasePremium = mDatabaseSource.getMedi(SumInsured,Age,Zone);
                    }
                    if (mAgeYearInt >= 46 && mAgeYearInt <= 50){
                        mAgeYearInt = 46;
                        Age = ""+mAgeYearInt;
                        mBasePremium = mDatabaseSource.getMedi(SumInsured,Age,Zone);
                    }
                    if (mAgeYearInt >=51 && mAgeYearInt <= 55){
                        mAgeYearInt = 51;
                        Age = ""+mAgeYearInt;
                        mBasePremium = mDatabaseSource.getMedi(SumInsured,Age,Zone);
                    }
                    if (mAgeYearInt >= 56 && mAgeYearInt <= 60){
                        mAgeYearInt = 56;
                        Age = ""+mAgeYearInt;
                        mBasePremium = mDatabaseSource.getMedi(SumInsured,Age,Zone);
                    }
                    if (mAgeYearInt >=61 && mAgeYearInt <= 65){
                        mAgeYearInt = 61;
                        Age = ""+mAgeYearInt;
                        mBasePremium = mDatabaseSource.getMedi(SumInsured,Age,Zone);
                    }
                    if (mAgeYearInt >= 66 && mAgeYearInt <= 70){
                        mAgeYearInt = 66;
                        Age = ""+mAgeYearInt;
                        mBasePremium = mDatabaseSource.getMedi(SumInsured,Age,Zone);
                    }
                    if (mAgeYearInt >=71 && mAgeYearInt <=75){
                        mAgeYearInt = 71;
                        Age = ""+mAgeYearInt;
                        mBasePremium = mDatabaseSource.getMedi(SumInsured,Age,Zone);
                    }
                    if (mAgeYearInt >=76 && mAgeYearInt <=80){
                        mAgeYearInt = 76;
                        Age = ""+mAgeYearInt;
                        mBasePremium = mDatabaseSource.getMedi(SumInsured,Age,Zone);
                    }
                    if (mAgeYearInt == 81){
                        mAgeYearInt = 81;
                        Age = ""+mAgeYearInt;
                        mBasePremium = mDatabaseSource.getMedi(SumInsured,Age,Zone);
                    }
                    mBasePremiumInt = Integer.parseInt(mBasePremium.toString().trim());
                    mTax = 15.0f;
                    mTaxSum = (mBasePremiumInt * mTax)/100;
                    mTotalPremium = Math.round(mBasePremiumInt + mTaxSum);
                    Log.d("MediClassic : ",mBasePremiumInt+ " "+mAgeYearInt+" "+mTaxSum);
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
        tvSISummary.setText(format.format(Double.parseDouble(SumInsured)));
        TextView tvSIBasePremium = (TextView)dialog.findViewById(R.id.tvSIBasePremium);
        tvSIBasePremium.setText(format.format(Double.parseDouble(mBasePremium)));
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
        TextView tvZone = (TextView)dialog.findViewById(R.id.tvZone);
        tvZone.setText(Zone);
        TextView tvLabelDeductible = (TextView)dialog.findViewById(R.id.tvLabelDeductible);
        tvLabelDeductible.setVisibility(View.GONE);
        TextView tvDeductible = (TextView)dialog.findViewById(R.id.tvDeductible);
        tvDeductible.setVisibility(View.GONE);
        TextView tvTax = (TextView)dialog.findViewById(R.id.tvTax);
        tvTax.setText(format.format(mTaxSum));
        TextView tvTotalPremium = (TextView)dialog.findViewById(R.id.tvTotalPremium);
        tvTotalPremium.setText(format.format(mTotalPremium));
        Button shareButton = (Button)dialog.findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID,"Share_Button_MediClassic");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,"Share_Button_MediClassic");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE,"ShareButton");
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Dear Customer"+'\n'+ "Premium Calculation of Star Health Medi Classic Policy as below :"+
                                '\n'+"Sum Insured : "+SumInsured+
                                '\n'+"Date Of Birth : "+s+
                                '\n'+"Age : "+mAgeYear+
                                '\n'+"Base Premium : "+mBasePremium+
                                '\n'+"Zone : "+Zone+
                                '\n'+"Tax @15% : "+"+ "+Float.toString(mTaxSum)+
                                '\n'+"Total Premium : "+Float.toString(mTotalPremium)+
                                "\n"+"Product Brochures"+
                                "\n"+"https://goo.gl/rREiSA");
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

    private void selectZone() {
        final CharSequence[] items = {"Zone 1", "Zone 2"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Zone")
                .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mZone.setText(items[which]);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }

    private void selectSI() {
        final CharSequence[] items = {"150000", "200000", "300000", "400000","500000","1000000","1500000"};
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
}
