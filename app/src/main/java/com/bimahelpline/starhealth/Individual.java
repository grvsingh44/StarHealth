package com.bimahelpline.starhealth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.bimahelpline.starhealth.activity.CardiacCare;
import com.bimahelpline.starhealth.activity.CompIndividual;
import com.bimahelpline.starhealth.activity.DiabetesIndividual;
import com.bimahelpline.starhealth.activity.MediClassic;
import com.bimahelpline.starhealth.activity.SeniorCitizens;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class Individual extends AppCompatActivity {

    private Button button1, button2, button3, button4,button5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual);
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);
        button1 = (Button)findViewById(R.id.button1);
        button2 = (Button)findViewById(R.id.button2);
        button3 = (Button)findViewById(R.id.button3);
        button4 = (Button)findViewById(R.id.button4);
        button5 = (Button)findViewById(R.id.button5);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Individual.this, CardiacCare.class );
                startActivity(i);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Individual.this, SeniorCitizens.class);
                startActivity(i);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Individual.this, CompIndividual.class);
                startActivity(i);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Individual.this, MediClassic.class);
                startActivity(i);
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Individual.this, DiabetesIndividual.class);
                startActivity(i);
            }
        });

    }

}
