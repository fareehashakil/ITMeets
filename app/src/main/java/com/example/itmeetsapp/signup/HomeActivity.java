package com.example.itmeetsapp.signup;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.awt.font.TextAttribute;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonLogout;
    private TextView tv_profile, tv_home, tv_ELearning, tv_jobAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (!SharedPrefManager.getInstance(this).isLoggeddIn()){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        tv_home = (TextView) findViewById(R.id.tv_home);
        tv_profile = (TextView) findViewById(R.id.tv_profile);
        tv_ELearning = (TextView) findViewById(R.id.tv_ELibrary);
        tv_jobAnalytics = (TextView) findViewById(R.id.tv_jobAnalytics);
        buttonLogout = (Button) findViewById(R.id.buttonLogout);

        tv_home.setOnClickListener(this);
        tv_profile.setOnClickListener(this);
        tv_ELearning.setOnClickListener(this);
        tv_jobAnalytics.setOnClickListener(this);
        buttonLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==buttonLogout){
            SharedPrefManager.getInstance(this).logout();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        if (v==tv_profile){
            startActivity(new Intent(this,ProfileActivity.class));
        }
        if (v==tv_ELearning){
            startActivity(new Intent(this,ELibrary.class));
        }

    }
}
