package com.codepath.simpletodo.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.simpletodo.R;

public class SplashScreen extends Activity {

    private static int SPLASH_TIME_OUT1 = 1000;
    private static int SPLASH_TIME_OUT2 = 2000;
    private static int SPLASH_TIME_OUT3 = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                ImageView logo = (ImageView) findViewById(R.id.imgLogo);
                logo.setImageResource(R.drawable.logo_large);
            }
        }, SPLASH_TIME_OUT1);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                TextView tvSpashTitle = (TextView) findViewById(R.id.spashTitle);
                tvSpashTitle.setTextColor(getResources().getColor(R.color.white_opaque));
            }
        }, SPLASH_TIME_OUT2);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT3);
    }

}

