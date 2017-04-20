package com.bimahelpline.starhealth.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bimahelpline.starhealth.R;
import com.bimahelpline.starhealth.custom.DatePickerFragment;
import com.bimahelpline.starhealth.database.DatabaseHandler;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.text.NumberFormat;
import java.util.Calendar;

public class CardiacCare extends AppCompatActivity {

    static EditText mAge;
    final Context context = this;
    private DatabaseHandler mDatabaseSource;
    private int mCurrentYearInt;
    private InterstitialAd mInterstitialAd;
    private EditText mSumInsured, mPlan;
    private boolean isFieldEmpty;
    private String s, s1, mAgeString, age,SumInsured ,mBasePremium, plan;
    private int mBasePremiumInt,mAgeYearInt, mAgeInt,day,month,mCurrentDate,mCurrentMonth;
    private float mTax;
    private float mTaxSum;
    private long mTotalPremium;
    private NumberFormat format;
    private ImageView im;
    private LinearLayout linearLayout;
    private FirebaseAnalytics firebaseAnalytics;
    private TextView TvSumInsured;
    private Button mCalButton, mPlanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardiac_care);
        getSupportActionBar().setTitle("Cardiac Care");
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
        linearLayout = (LinearLayout)findViewById(R.id.shareLayout);
        TvSumInsured = (TextView)findViewById(R.id.tvSumInsured);
        TvSumInsured.setTextSize(8 * getResources().getDisplayMetrics().density);
        mSumInsured = (EditText)findViewById(R.id.etSumInsured);
        im = (ImageView)findViewById(R.id.testImageView);
        mAge = (EditText)findViewById(R.id.etAge);
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
        mPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPlan();
            }
        });
        mCalButton = (Button)findViewById(R.id.calButton);
        mCurrentYearInt = Calendar.getInstance().get(Calendar.YEAR);
        mCurrentDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        mCurrentMonth = (Calendar.getInstance().get(Calendar.MONTH)+1);
        mCalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (mSumInsured.getText().toString().contentEquals("") || mAge.getText().toString().contentEquals("") || mPlan.getText().toString().contentEquals("")){
                        isFieldEmpty = true;
                        AlertDialog.Builder builder = new AlertDialog.Builder(CardiacCare.this);
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
                    mAgeYearInt = Integer.parseInt(s1);
                    day = Integer.parseInt(s2);
                    month = Integer.parseInt(s3);
                    mAgeInt = mCurrentYearInt - mAgeYearInt;
                    if(month > mCurrentMonth){
                        --mAgeInt;
                    }else if (month == mCurrentMonth){
                        if (day > mCurrentDate){
                            --mAgeInt;
                        }
                    }
                    if(mAgeInt < 10){
                        isFieldEmpty = true;
                        AlertDialog.Builder builder = new AlertDialog.Builder(CardiacCare.this);
                        builder.setMessage("Age Can't Be Less Than 10 Years")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                }).create().show();
                    }else{
                        isFieldEmpty = false;
                    }
                    mAgeString = ""+mAgeInt;
                    SumInsured = mSumInsured.getText().toString();
                    plan = mPlan.getText().toString();
                    if(mAgeInt >= 10 && mAgeInt <= 60){
                        mAgeInt = 10;
                        age = ""+mAgeInt;
                        mBasePremium = mDatabaseSource.getcardiaccare(SumInsured,age,plan);
                    }
                    if (mAgeInt >=61 && mAgeInt <= 65){
                        mAgeInt = 61;
                        age = ""+ mAgeInt;
                        mBasePremium = mDatabaseSource.getcardiaccare(SumInsured,age,plan);
                    }
                    if (mAgeInt >= 66 && mAgeInt <= 70){
                        mAgeInt = 66;
                        age = ""+ mAgeInt;
                        mBasePremium = mDatabaseSource.getcardiaccare(SumInsured,age,plan);
                    }
                    if (mAgeInt >=71 && mAgeInt <= 80){
                        mAgeInt = 71;
                        age = ""+ mAgeInt;
                        mBasePremium = mDatabaseSource.getcardiaccare(SumInsured,age,plan);
                    }
                    if (mAgeInt == 81){
                        mAgeInt = 81;
                        age = ""+ mAgeInt;
                        mBasePremium = mDatabaseSource.getcardiaccare(SumInsured,age,plan);
                    }
                    mBasePremiumInt = Integer.parseInt(mBasePremium.toString().trim());
                    mTax = 15.0f;
                    mTaxSum = (mBasePremiumInt * mTax)/100;
                    mTotalPremium = Math.round(mBasePremiumInt + mTaxSum);
                } catch (Exception e) {
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
        mPlanButton = (Button)findViewById(R.id.btPlanD);
        mPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AppCompatDialog dialog = new AppCompatDialog(context, R.style.Theme_AppCompat_Light_NoActionBar);
                dialog.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.planidalog);
                dialog.setCanceledOnTouchOutside(true);
                TextView tvPlanDetails = (TextView)dialog.findViewById(R.id.tvPlanDetials);
                Resources res = getResources();
                String text = res.getString(R.string.cardia_care_plan);
                CharSequence styledText = Html.fromHtml(text);
                tvPlanDetails.setText(styledText);
                Button shareButton = (Button)dialog.findViewById(R.id.btPlanShare);
                shareButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT,
                                "*Recharge Benefit*:\n" +
                                "This benefit provides additional indemnity upto specified limits, when the sum insured under the policy is exhausted / exceeded. This can be utilized for any claim payable under the policy.\n" +
                                "1.More than 400 Day Care procedures.\n" +
                                "2.Expenses incurred towards cost of Health Check up.\n" +
                                "3.Coverage for New born Baby.\n" +
                                "4.Coverage for Domiciliary hospitalization treatments.\n" +
                                "5.Donor expenses for organ transplantation.\n" +
                                "\n" +
                                "Bonus:\n" +
                                "1.Bonus in respect of a claim-free year of insurance is allowable upto the limits specified. The bonus so granted will be reduced in the same order in which it was given following a claim. However the Basic sum insured shall not be reduced.\n" +
                                "\n" +
                                "2.Automatic Restoration of Sum Insured:\n" +
                                "3.Immediately upon exhaustion of the limit of coverage during the policy period, there shall be an automatic restoration of the basic sum insured by 100% where the sum insured is Rs.3,00,000/- and above.\n" +
                                "\n" +
                                "Policy Benefits:\n" +
                                "1.Hospitalisation Cover Protects the insured for in - patient hospitalisation expenses for a minimum of 24 hrs.These expenses include room rent,nursing and boarding charges.\n" +
                                "2.Surgeon, Anesthetist, Medical Practitioner, Consultants, Specialist Fees, Anesthesia, Blood, Oxygen, Operation Theatre charges, Cost of Pacemaker, Cost of Medicine and drugs etc.\n" +
                                "3.Ambulance charges for emergency transportation upto a sum of Rs.750/- per hospitalization and overall limit of Rs.1500/- per policy period.\n" +
                                "4.Pre Hospitalisation expenses upto 60 days prior to admission in the hospital.\n" +
                                "5.Post Hospitalisation expenses upto 90 days after discharge from the hospital.");
                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);
                    }
                });
                dialog.show();
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
        tvAge.setText(mAgeString);
        TextView tvLabelAdult = (TextView)dialog.findViewById(R.id.tvAdultLabel);
        tvLabelAdult.setVisibility(View.GONE);
        TextView tvAdult = (TextView)dialog.findViewById(R.id.tvAdult);
        tvAdult.setVisibility(View.GONE);
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
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID,"Share_Button_CardiacCare");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,"Share_Button_CardiacCare");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE,"ShareButton");
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Dear Customer"+'\n'+ "Premium Calculation of Star Health Cardiac Care Policy as below :"+
                                '\n'+"Sum Insured : "+SumInsured+
                                '\n'+"Date Of Birth : "+s+
                                '\n'+"Age : "+mAgeString+
                                '\n'+"Base Premium : "+mBasePremium+
                                '\n'+"Plan : "+plan+
                                '\n'+"Tax @15% : "+"+ "+Float.toString(mTaxSum)+
                                '\n'+"Total Premium : "+Float.toString(mTotalPremium));
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void selectPlan() {
        final CharSequence[] items = {"Silver","Gold"};
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
    private void selectSI() {
        final CharSequence[] items = {"300000","400000"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Sum Insured")
                .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSumInsured.setText(items[which]);
                        mSumInsured.setTextSize(8 * getResources().getDisplayMetrics().density);
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