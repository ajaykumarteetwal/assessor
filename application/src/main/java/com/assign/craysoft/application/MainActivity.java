package com.assign.craysoft.application;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import assign.craysoft.com.assignindia.activity.Splash;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, Splash.class);
        startActivity(intent);
        finish();
    }
}
