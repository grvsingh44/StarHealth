package com.bimahelpline.starhealth.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.text.Editable;
import android.text.TextWatcher;
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

public class SuperSurplus extends AppCompatActivity {

    final Context context = this;
    private DatabaseHandler mDatabaseSource;
    private EditText mSumInsured, mAdult, mChild, mPlan, mDeductible;
    static EditText mAge;
    private boolean isFieldEmpty;
    private String s,mAgeYear, age,SumInsured,adult,child ,mBasePremium,plan,deductible;
    private int mBasePremiumInt,mAgeInt, mAgeYearInt,mCurrentYearInt,mCurrentDate,mCurrentMonth,day,month;
    private float mTax;
    private float mTaxSum;
    private long mTotalPremium;
    private InterstitialAd mInterstitialAd;
    private NumberFormat format;
    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_surplus);
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
        mPlan = (EditText)findViewById(R.id.etPlan);
        mDeductible = (EditText)findViewById(R.id.etDeductible);
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
        mPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPlan();
            }
        });
        mDeductible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDeduc();
            }
        });
        mAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDOB(v);
            }
        });
        final Button mCalButton = (Button)findViewById(R.id.calButton);
        mCurrentYearInt = Calendar.getInstance().get(Calendar.YEAR);
        mCurrentDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        mCurrentMonth = (Calendar.getInstance().get(Calendar.MONTH)+1);
        mPlan.addTextChangedListener(textWatcher);
        mCalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if (mSumInsured.getText().toString().contentEquals("")
                            || mAge.getText().toString().contentEquals("")
                            || mAdult.getText().toString().contentEquals("") || mChild.getText().toString().contentEquals("")
                            || mPlan.getText().toString().contentEquals("") || mDeductible.getText().toString().contentEquals("")){
                        isFieldEmpty = true;
                        AlertDialog.Builder builder = new AlertDialog.Builder(SuperSurplus.this);
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
                    if (mPlan.getText().toString().contentEquals("Silver")){
                        mSumInsured.setText("1000000");
                    }
                    if(mAdult.getText().toString().contentEquals("1") && mChild.getText().toString().contentEquals("0")){
                        isFieldEmpty = true;
                        AlertDialog.Builder builder = new AlertDialog.Builder(SuperSurplus.this);
                        builder.setMessage("Please select one more family member !")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).create().show();
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
                    plan = mPlan.getText().toString().trim();
                    deductible = mDeductible.getText().toString().trim();
                    if(mAgeYearInt >= 0 && mAgeYearInt <= 35){
                        mAgeYearInt = 0;
                        age = ""+mAgeYearInt;
                        mBasePremium = mDatabaseSource.getSuper(SumInsured,age,adult, child, plan, deductible);
                    }
                    if (mAgeYearInt >=36 && mAgeYearInt <= 45){
                        mAgeYearInt = 36;
                        age = ""+mAgeYearInt;
                        mBasePremium = mDatabaseSource.getSuper(SumInsured,age,adult, child, plan, deductible);
                    }
                    if (mAgeYearInt >= 46 && mAgeYearInt <= 50){
                        mAgeYearInt = 46;
                        age = ""+mAgeYearInt;
                        mBasePremium = mDatabaseSource.getSuper(SumInsured,age,adult, child, plan, deductible);
                    }
                    if (mAgeYearInt >=51 && mAgeYearInt <= 55){
                        mAgeYearInt = 51;
                        age = ""+mAgeYearInt;
                        mBasePremium = mDatabaseSource.getSuper(SumInsured,age,adult, child, plan, deductible);
                    }
                    if (mAgeYearInt >= 56 && mAgeYearInt <= 60) {
                        mAgeYearInt = 56;
                        age = "" + mAgeYearInt;
                        mBasePremium = mDatabaseSource.getSuper(SumInsured, age, adult, child, plan, deductible);
                    }
                    if (mAgeYearInt >=61 && mAgeYearInt <= 65){
                        mAgeYearInt = 61;
                        age = ""+mAgeYearInt;
                        mBasePremium = mDatabaseSource.getSuper(SumInsured,age,adult, child, plan, deductible);
                    }
                    if (mAgeYearInt >= 66 && mAgeYearInt <= 70){
                        mAgeYearInt = 66;
                        age = ""+mAgeYearInt;
                        mBasePremium = mDatabaseSource.getSuper(SumInsured,age,adult, child, plan, deductible);
                    }
                    if (mAgeYearInt >=71 && mAgeYearInt <=75){
                        mAgeYearInt = 71;
                        age = ""+mAgeYearInt;
                        mBasePremium = mDatabaseSource.getSuper(SumInsured,age,adult, child, plan, deductible);
                    }
                    if (mAgeYearInt >=76 && mAgeYearInt <=80){
                        mAgeYearInt = 76;
                        age = ""+mAgeYearInt;
                        mBasePremium = mDatabaseSource.getSuper(SumInsured,age,adult, child, plan, deductible);
                    }
                    if (mAgeYearInt == 81){
                        mAgeYearInt = 81;
                        age = ""+mAgeYearInt;
                        mBasePremium = mDatabaseSource.getSuper(SumInsured,age,adult, child, plan, deductible);
                    }else if(mAgeYearInt > 81){
                        isFieldEmpty = true;
                        AlertDialog.Builder builder = new AlertDialog.Builder(SuperSurplus.this);
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
            }
        });
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mPlan.getText().toString().contentEquals("Silver")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SuperSurplus.this);
                builder.setMessage("Only Rs.10,00,000 SumInsured covered in Plan Silver !")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mSumInsured.setText("1000000");
                            }
                        }).create().show();
                mDeductible.setText("");
            }else if(mPlan.getText().toString().contentEquals("Gold")){
                mDeductible.setText("");

            }
        }
    };

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
        TextView tvTPA = (TextView)dialog.findViewById(R.id.tvPlan);
        tvTPA.setText(plan);
        TextView tvLabelZone = (TextView)dialog.findViewById(R.id.tvLabelZone);
        tvLabelZone.setVisibility(View.GONE);
        TextView tvZone = (TextView)dialog.findViewById(R.id.tvZone);
        tvZone.setVisibility(View.GONE);
        TextView tvDeductible = (TextView)dialog.findViewById(R.id.tvDeductible);
        tvDeductible.setText(deductible);
        TextView tvTax = (TextView)dialog.findViewById(R.id.tvTax);
        tvTax.setText(Float.toString(mTaxSum));
        TextView tvTotalPremium = (TextView)dialog.findViewById(R.id.tvTotalPremium);
        tvTotalPremium.setText(Float.toString(mTotalPremium));
        Button shareButton = (Button)dialog.findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID,"Share_Button_SuperSurplus");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,"Share_Button_SuperSurplus");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE,"ShareButton");
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Dear Customer"+'\n'+ "Premium Calculation of Star Health Super Surplus Policy as below :"+
                                '\n'+"Sum Insured : "+SumInsured+
                                '\n'+"Date Of Birth : "+s+
                                '\n'+"Age : "+mAgeYear+
                                '\n'+"Adult : "+adult+
                                '\n'+"Child : "+child+
                                '\n'+"Plan : "+plan+
                                '\n'+"Deductible : "+deductible+
                                '\n'+"Base Premium : "+mBasePremium+
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

    private void selectPlan(){
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
        final CharSequence[] items = {"0","1","2"};
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

    private void selectDeduc() {
        if (mPlan.getText().toString().contentEquals("Silver")){
            final CharSequence[] items = {"300000","500000"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select Deductible")
                    .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mDeductible.setText(items[which]);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
        }else {
            final CharSequence[] items = {"500000", "1000000"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select Deductible")
                    .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mDeductible.setText(items[which]);
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
    }

    private void selectSI() {
        if (mPlan.getText().toString().contentEquals("Silver")){
            final CharSequence [] items = {"1000000"};
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
        }else{
            final CharSequence [] items = {"500000", "1000000", "1500000", "2000000", "2500000"};
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
    }

    public void selectDOB(View v) {
        new DatePickerFragment((EditText) v).show(getSupportFragmentManager(), "datePicker");
    }
}
