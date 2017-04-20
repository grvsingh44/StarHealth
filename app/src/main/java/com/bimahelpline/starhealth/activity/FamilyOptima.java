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

public class FamilyOptima extends AppCompatActivity {

    private String title = "FamilyOptima";
    private String url = "https://retail.starhealth.in/agentpolicies/fhonew/agentnew?intermediaryCode=BA0000195747&officeCode=161111&smCode=SH29866&type=AGENT";
    final Context context = this;
    private DatabaseHandler mDatabaseSource;
    private EditText mSumInsured, mAdult, mChild, mZone;
    static EditText mAge;
    private boolean isFieldEmpty;
    private String s, s1, mAgeYear, age,SumInsured,adult,child ,mBasePremium,zone;
    private int mBasePremiumInt,mAgeInt, mAgeYearInt,mCurrentYearInt,mCurrentDate,mCurrentMonth, month,day;
    private float mTax;
    private float mTaxSum;
    private long mTotalPremium;
    private InterstitialAd mInterstitialAd;
    private NumberFormat format;
    private FirebaseAnalytics firebaseAnalytics;
    private int mSumInsuredInt;
    private Button mPlanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_optima);
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
        mSumInsured = (EditText)findViewById(R.id.etSumInsured);
        mAge = (EditText)findViewById(R.id.etAge);
        mAdult = (EditText)findViewById(R.id.etAdult);
        mChild = (EditText)findViewById(R.id.etChild);
        mZone = (EditText)findViewById(R.id.etZone);
        mSumInsured.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSI();
            }
        });
        mAdult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAdult();
            }
        });
        mChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectChild();
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
        final Button mCalButton = (Button)findViewById(R.id.calButton);
        mCurrentYearInt = Calendar.getInstance().get(Calendar.YEAR);
        mCurrentDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        mCurrentMonth = (Calendar.getInstance().get(Calendar.MONTH)+1);
        mCalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (mSumInsured.getText().toString().contentEquals("")
                            || mAge.getText().toString().contentEquals("")
                            || mAdult.getText().toString().contentEquals("") || mChild.getText().toString().contentEquals("") || mZone.getText().toString().contentEquals("")){
                        isFieldEmpty = true;
                        AlertDialog.Builder builder = new AlertDialog.Builder(FamilyOptima.this);
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
                    SumInsured = mSumInsured.getText().toString().trim();
                    adult = mAdult.getText().toString().trim();
                    child = mChild.getText().toString().trim();
                    zone = mZone.getText().toString().trim();
                    if(mAgeYearInt >= 0 && mAgeYearInt <= 35){
                        mAgeYearInt = 0;
                        age = ""+mAgeYearInt;
                        mBasePremium = mDatabaseSource.getDataORF(SumInsured,age,adult,child,zone);
                    }
                    if (mAgeYearInt >=36 && mAgeYearInt <= 45){
                        mAgeYearInt = 36;
                        age = ""+mAgeYearInt;
                        mBasePremium = mDatabaseSource.getDataORF(SumInsured,age,adult,child,zone);
                    }
                    if (mAgeYearInt >= 46 && mAgeYearInt <= 50){
                        mAgeYearInt = 46;
                        age = ""+mAgeYearInt;
                        mBasePremium = mDatabaseSource.getDataORF(SumInsured,age,adult,child,zone);
                    }
                    if (mAgeYearInt >=51 && mAgeYearInt <= 55){
                        mAgeYearInt = 51;
                        age = ""+mAgeYearInt;
                        mBasePremium = mDatabaseSource.getDataORF(SumInsured,age,adult,child,zone);
                    }
                    if (mAgeYearInt >= 56 && mAgeYearInt <= 60){
                        mAgeYearInt = 56;
                        age = ""+mAgeYearInt;
                        mBasePremium = mDatabaseSource.getDataORF(SumInsured,age,adult,child,zone);
                    }
                    if (mAgeYearInt >=61 && mAgeYearInt <= 65){
                        mAgeYearInt = 61;
                        age = ""+mAgeYearInt;
                        mBasePremium = mDatabaseSource.getDataORF(SumInsured,age,adult,child,zone);
                    }
                    if (mAgeYearInt >= 66 && mAgeYearInt <= 70){
                        mAgeYearInt = 66;
                        age = ""+mAgeYearInt;
                        mBasePremium = mDatabaseSource.getDataORF(SumInsured,age,adult,child,zone);
                    }
                    if (mAgeYearInt >=71 && mAgeYearInt <=75){
                        mAgeYearInt = 71;
                        age = ""+mAgeYearInt;
                        mBasePremium = mDatabaseSource.getDataORF(SumInsured,age,adult,child,zone);
                    }
                    if (mAgeYearInt >=76 && mAgeYearInt <=80){
                        mAgeYearInt = 71;
                        age = ""+mAgeYearInt;
                        mBasePremium = mDatabaseSource.getDataORF(SumInsured,age,adult,child,zone);
                    }
                    if (mAgeYearInt >=81){
                        mAgeYearInt = 81;
                        age = ""+mAgeYearInt;
                        mBasePremium = mDatabaseSource.getDataORF(SumInsured,age,adult,child,zone);
                    }
                    mBasePremiumInt = Integer.parseInt(mBasePremium.trim());
                    mSumInsuredInt = Integer.parseInt(SumInsured);
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
                String text = res.getString(R.string.family_optima);
                CharSequence styledText = Html.fromHtml(text);
                tvPlanDetails.setText(styledText);
                Button shareButton = (Button)dialog.findViewById(R.id.btPlanShare);
                shareButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "Recharge Benefit: \n" +
                                "1. This benefit provides additional indemnity upto specified limits, when the sum insured under the policy is exhausted / exceeded. This can be utilized for any claim payable under the policy.\n" +
                                "2. More than 400 Day Care procedures\n" +
                                "3. Expenses incurred towards cost of Health Check up\n" +
                                "4. Coverage for New born Baby\n" +
                                "5. Coverage for Domiciliary hospitalization treatments\n" +
                                "6. Donor expenses for organ transplantation\n" +
                                "\n" +
                                "Bonus:\n" +
                                "\n" +
                                "1. Bonus in respect of a claim-free year of insurance is allowable upto the limits specified. The bonus so granted will be reduced in the same order in which it was given following a claim. However the Basic sum insured shall not be reduced.\n" +
                                "\n" +
                                "2. Automatic Restoration of Sum Insured:\n" +
                                "\n" +
                                "3. Immediately upon exhaustion of the limit of coverage during the policy period, there shall be an automatic restoration of the basic sum insured by 100% where the sum insured is Rs.3,00,000/- and above.\n" +
                                "\n" +
                                "Policy Benefits:\n" +
                                "\n" +
                                "1. Hospitalisation Cover Protects the insured for in - patient hospitalisation expenses for a minimum of 24 hrs.These expenses include room rent,nursing and boarding charges.\n" +
                                "2. Surgeon, Anesthetist, Medical Practitioner, Consultants, Specialist Fees, Anesthesia, Blood, Oxygen, Operation Theatre charges, Cost of Pacemaker, Cost of Medicine and drugs etc.\n" +
                                "3. Ambulance charges for emergency transportation upto a sum of Rs.750/- per hospitalization and overall limit of Rs.1500/- per policy period.\n" +
                                "4. Pre Hospitalisation expenses upto 60 days prior to admission in the hospital.\n" +
                                "5. Post Hospitalisation expenses upto 90 days after discharge from the hospital.");
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
        tvAge.setText(mAgeYear);
        TextView tvAdult = (TextView)dialog.findViewById(R.id.tvAdult);
        tvAdult.setText(adult);
        TextView tvChild = (TextView)dialog.findViewById(R.id.tvChild);
        tvChild.setText(child);
        TextView tvLabelPlan = (TextView)dialog.findViewById(R.id.tvLabelPlan);
        tvLabelPlan.setVisibility(View.GONE);
        TextView tvTPA = (TextView)dialog.findViewById(R.id.tvPlan);
        tvTPA.setVisibility(View.GONE);
        TextView tvZone = (TextView)dialog.findViewById(R.id.tvZone);
        tvZone.setText(zone);
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
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID,"Share_Button_FamilyOptima");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,"Share_Button_FamilyOptima");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE,"ShareButton");
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Dear Customer"+'\n'+ "Premium Calculation of Star Health Family Optima Policy as below :"+
                                '\n'+"Sum Insured : "+SumInsured+
                                '\n'+"Date Of Birth : "+s+
                                '\n'+"Age : "+mAgeYear+
                                '\n'+"Adult : "+adult+
                                '\n'+"Child : "+child+
                                '\n'+"Zone : "+zone+
                                '\n'+"Base Premium : "+mBasePremium+
                                '\n'+"Tax @15% : "+"+ "+Float.toString(mTaxSum)+
                                '\n'+"Total Premium : "+Float.toString(mTotalPremium)+
                                "\n"+"Product Brochures"+
                                "\n"+"https://goo.gl/ovHGol");
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

    private void selectZone(){
        final CharSequence[] items = {"Zone 1","Zone 2","Zone 3"};
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

    private void selectAdult() {
        final CharSequence[] items = {"1","2"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Adult")
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

    private void selectChild() {
        final CharSequence[] items = {"0","1","2","3"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Child")
                .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mChild.setText(items[which]);
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
        final CharSequence[] items = {"200000","300000","400000","500000","1000000","1500000"};
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
