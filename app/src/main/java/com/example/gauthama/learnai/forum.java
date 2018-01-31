package com.example.gauthama.learnai;

import android.support.design.widget.FloatingActionButton;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

public class forum extends AppCompatActivity {
    private static final int CONTENT_VIEW_ID = 10101010;

    FloatingActionButton fab;
    Fragment fragment;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

//        mViewPager = (ViewPager)findViewById(R.id.container);
//        setupViewPager(mViewPager);



        fab = (FloatingActionButton)findViewById(R.id.fab);

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                fragment = new addItem();
//                android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.add(R.id.frag, newFragment).commit();
//            }
//        });
    }
}
