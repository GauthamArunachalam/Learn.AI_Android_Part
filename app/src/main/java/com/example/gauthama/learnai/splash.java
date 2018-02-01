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

import com.daimajia.androidanimations.library.Techniques;
import com.github.barteksc.pdfviewer.util.Constants;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

public class splash extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 4000;

    TextView t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Intent i = new Intent(splash.this, GSignIn.class);
            startActivity(i);
                finish();

//        t = (TextView)findViewById(R.id.textView);
//        Typeface mycustomFont = Typeface.createFromAsset(getAssets(), "fonts/splashFont.ttf");
//        t.setTypeface(mycustomFont);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        },SPLASH_TIME_OUT);
   }

//    @Override
//    public void initSplash(ConfigSplash configSplash) {
//        configSplash.setBackgroundColor(R.color.colorPrimary); //any color you want form colors.xml
//        configSplash.setAnimCircularRevealDuration(2000); //int ms
//        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
//        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM);
//
//        configSplash.setLogoSplash(R.mipmap.logo); //or any other drawable
//        configSplash.setAnimLogoSplashDuration(2000); //int ms
//        configSplash.setAnimLogoSplashTechnique(Techniques.Bounce);
//
//        //configSplash.setPathSplash(Constants.); //set path String
//        configSplash.setOriginalHeight(400); //in relation to your svg (path) resource
//        configSplash.setOriginalWidth(400); //in relation to your svg (path) resource
//        configSplash.setAnimPathStrokeDrawingDuration(3000);
//        configSplash.setPathSplashStrokeSize(3); //I advise value be <5
//        configSplash.setPathSplashStrokeColor(R.color.colorPrimary); //any color you want form colors.xml
//        configSplash.setAnimPathFillingDuration(3000);
//        configSplash.setPathSplashFillColor(R.color.colorPrimary); //path object filling color
//
//    }
//
//    @Override
//    public void animationsFinished() {
//
//    }
}
