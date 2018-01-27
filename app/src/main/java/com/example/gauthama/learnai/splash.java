package com.example.gauthama.learnai;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.constraint.solver.SolverVariable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class splash extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 4000;

    TextView t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        t = (TextView)findViewById(R.id.textView);
        Typeface mycustomFont = Typeface.createFromAsset(getAssets(), "fonts/splashFont.ttf");
        t.setTypeface(mycustomFont);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(splash.this, GSignIn.class);
                startActivity(i);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}
