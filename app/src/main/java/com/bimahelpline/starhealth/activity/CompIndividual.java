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
import android.util.Log;
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

public class CompIndividual extends AppCompatActivity {

    private final String url = "https://retail.starhealth.in/agentpolicies/comprehensiveind/agentnew?intermediaryCode=BA0000195747&officeCode=161111&smCode=SH29866&type=AGENT";
    private final String title = "Star Health- Comprehensive Indi";
    static EditText mAge;
    final Context context = this;
    private DatabaseHandler mDatabaseSource;
    private int mCurrentYearInt;
    private InterstitialAd mInterstitialAd;
    private EditText mSumInsured;
    private boolean isFieldEmpty;
    private String s, s1, mAgeYear, age,SumInsured,adult,child ,mBasePremium;
    private int mBasePremiumInt,mAgeInt, mAgeYearInt,day,month,mCurrentMonth,mCurrentDate;
    private long mTotalPremium;
    private float mTax;
    private float mTaxSum;
    private NumberFormat format;
    private FirebaseAnalytics firebaseAnalytics;
    private Button mPlanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comp_indi);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(CompIndividual.this);
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
                    adult = "1";
                    child ="0";
                    Log.d("CompIndi :",mAgeYear+" "+SumInsured+" "+adult+" "+child);
                    if(mAgeYearInt >= 0 && mAgeYearInt <= 35){
                        mAgeYearInt = 0;
                        age = ""+mAgeYearInt;
                        adult = "1";
                        child = "0";
                        mBasePremium = mDatabaseSource.getCompre(SumInsured,age,adult,child);
                    }
                    if (mAgeYearInt >=36 && mAgeYearInt <= 45){
                        mAgeYearInt = 36;
                        age = ""+mAgeYearInt;
                        adult = "1";
                        child = "0";
                        mBasePremium = mDatabaseSource.getCompre(SumInsured,age,adult,child);
                    }
                    if (mAgeYearInt >= 46 && mAgeYearInt <= 50){
                        mAgeYearInt = 46;
                        age = ""+mAgeYearInt;
                        adult = "1";
                        child = "0";
                        mBasePremium = mDatabaseSource.getCompre(SumInsured,age,adult,child);
                    }
                    if (mAgeYearInt >=51 && mAgeYearInt <= 55){
                        mAgeYearInt = 51;
                        age = ""+mAgeYearInt;
                        adult = "1";
                        child = "0";
                        mBasePremium = mDatabaseSource.getCompre(SumInsured,age,adult,child);
                    }
                    if (mAgeYearInt >= 56 && mAgeYearInt <=60){
                        mAgeYearInt = 56;
                        age = ""+mAgeYearInt;
                        adult = "1";
                        child = "0";
                        mBasePremium = mDatabaseSource.getCompre(SumInsured,age,adult,child);
                    }
                    if (mAgeYearInt >=61 && mAgeYearInt <= 65){
                        mAgeYearInt = 61;
                        age = ""+mAgeYearInt;
                        adult = "1";
                        child = "0";
                        mBasePremium = mDatabaseSource.getCompre(SumInsured,age,adult,child);
                    }
                    if (mAgeYearInt >= 66 && mAgeYearInt <= 70){
                        mAgeYearInt = 66;
                        age = ""+mAgeYearInt;
                        adult = "1";
                        child = "0";
                        mBasePremium = mDatabaseSource.getCompre(SumInsured,age,adult,child);
                    }
                    if (mAgeYearInt >=71 && mAgeYearInt <=75){
                        mAgeYearInt = 71;
                        age = ""+mAgeYearInt;
                        adult = "1";
                        child = "0";
                        mBasePremium = mDatabaseSource.getCompre(SumInsured,age,adult,child);
                    }
                    if (mAgeYearInt == 76){
                        mAgeYearInt = 76;
                        age = ""+mAgeYearInt;
                        adult = "1";
                        child = "0";
                        mBasePremium = mDatabaseSource.getCompre(SumInsured,age,adult,child);
                    }else if (mAgeYearInt > 76){
                        isFieldEmpty = true;
                        AlertDialog.Builder builder = new AlertDialog.Builder(CompIndividual.this);
                        builder.setMessage("Age Can't be more than 76 Years !")
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
        mPlanButton = (Button)findViewById(R.id.btPlanD);
        mPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AppCompatDialog dialog = new AppCompatDialog(context, R.style.Theme_AppCompat_Light_NoActionBar);
                dialog.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.planidalog);
                dialog.setCanceledOnTouchOutside(true);
                TextView tvPlanDetails = (TextView) dialog.findViewById(R.id.tvPlanDetials);
                Resources res = getResources();
                String text = res.getString(R.string.compfamilydetails);
                CharSequence styledText = Html.fromHtml(text);
                tvPlanDetails.setText(styledText);
                Button shareButton = (Button) dialog.findViewById(R.id.btPlanShare);
                shareButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "Plan Details :\n" + "Section 1:\n" +
                                "Hospitalization cover protects the insured for in patient hospitalization expenses for a minimum period of 24 hrs. These expenses include room rent (Single Standard A/C room), nursing and boarding charges, Surgeon, Anesthetist, Medical Practitioner, Consultants, Specialist fees, Cost of Medicines and Drugs.\n" +
                                "Ambulance charges for emergency transportation to hospital as per specified limits.\n" +
                                "Air Ambulance Benefit: Permitted up to 10% of the Sum Insured opted, during one policy period, applicable for sum insured of Rs.7.5 lakhs and above only.\n" +
                                "Pre hospitalization expenses up to 30 days prior to admission in the hospital\n" +
                                "Post hospitalization expenses up to 60 days after discharge from the hospital\n" +
                                "Out-patient consultation (other than dental and ophthal) expenses up to limits mentioned in the table of benefits\n" +
                                "Coverage for Domiciliary hospitalization for a period exceeding three days.\n" +
                                "Section 2:\n" +
                                "Expenses for Delivery - including Pre-natal and Post natal expenses up-to the limits specified. A waiting period of 36 months of continuous coverage under this policy with Star will apply.\n" +
                                "Automatic coverage for the New Born (including internal and external congenital problems) up to the expiry of the current policy (On renewal this new born baby should be covered specifically)\n" +
                                "Vaccination expenses for the New Born upto Rs.1000/- payable.\n" +
                                "Section 3: Outpatient Dental, Ophthalmic treatment\n" +
                                "Dental / Ophthalmic treatments as an out-patient - once in a block of every three years irrespective of whether a claim is made or not.\n" +
                                "Section 4: Hospital Cash\n" +
                                "Hospital cash benefit for each completed day of hospitalization.\n" +
                                "Section 5: Health Check up\n" +
                                "Cost of Health Check-up once after a block of every three claim-free years\n" +
                                "Section 6: Bariatric Surgery\n" +
                                "Expenses incurred on hospitalization for bariatric surgical procedure and its complications thereof are payable subject to a maximum of Rs.2,50,000/- during the policy period.\n" +
                                "This maximum limit of Rs.2,50,000/- is inclusive of pre-hospitalization and post hospitalization expenses.\n" +
                                "Claim under this section shall be processed only on cashless basis.\n" +
                                "Coverage under this section is subject to a waiting period of 36 months and payable only while the policy is in force\n" +
                                "Section 7: Accidental Death and Permanent Total Disablement\n" +
                                "Cover against Accidental Death\n" +
                                "The person chosen by the Proposer at the time of payment of premium as mentioned in the schedule herein is covered for Personal Accident\n" +
                                "The sum insured for this section is equal to the sum insured opted for the Health insurance.\n" +
                                "Section 8: Option for Second Medical Opinion\n" +
                                "The Insured Person is given the facility of obtaining a Second Medical Opinion in electronic mode from a Doctor in the Company's network of Medical Practitioners\n" +
                                "Medical records can be sent to the mail id e_medicalopinion@starhealth.in\n" +
                                "Automatic Restoration of Sum Insured (Applicable for Section 1 Only)\u200B\n" +
                                "Upon exhaustion of Basic sum insured and the Bonus, sum insured will be automatically restored by 100% once policy period. Such restored Sum Insured can be utilized only for illness / disease unrelated to the illness / diseases for which claim/s was / were made.\n" +
                                "Such restoration will be available for section 1 other than Section 1G.\n" +
                                "Bonus: \n" +
                                "Following a claim free year bonus at 50% of the basic sum insured (max.100%) would be allowed. Where there is a claim the Bonus would be reversed in the same order in which it was given.");
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
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID,"shareButton");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,"Share_Button_CompIndi");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE,"Button");
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Dear Customer"+'\n'+ "Premium Calculation of Star Health Comprehensive Individual Policy as below :"+
                                '\n'+"Sum Insured : "+SumInsured+
                                '\n'+"Date Of Birth : "+s+
                                '\n'+"Age : "+mAgeYear+
                                '\n'+"Base Premium : "+mBasePremium+
                                '\n'+"Tax @15% : "+"+ "+Float.toString(mTaxSum)+
                                '\n'+"Total Premium : "+Float.toString(mTotalPremium)+
                                "\n"+"Product Brochures"+
                                "\n"+"https://goo.gl/oJsPRv");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void selectSI() {
        final CharSequence[] items = {"500000","750000","1000000","1500000","2000000","2500000"};
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
