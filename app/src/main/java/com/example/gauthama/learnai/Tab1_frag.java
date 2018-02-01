package com.example.gauthama.learnai;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import java.io.File;

/**
 * Created by GAUTHAM A on 25-01-2018.
 */

public class Tab1_frag extends Fragment {
    private static final String TAG = "Tab1Fragement";

    ImageView img;
    TextView name, dis;
    Button vr, pdf, normal,forum;
    File fileToDownload = new File("/storage/sdcard0/Trail/MY");
    AmazonS3 s3;
    TransferUtility transferUtility;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1_frag,container,false);

        img = (ImageView)view.findViewById(R.id.Pic);
        name = (TextView)view.findViewById(R.id.name);
        vr = (Button)view.findViewById(R.id.vr);
        pdf = (Button)view.findViewById(R.id.pdf);
        normal = (Button)view.findViewById(R.id.normal);
      //  forum = (Button)view.findViewById(R.id.forum);



        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(),pdfViewer.class);
                startActivity(intent);
            }
        });


        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), normalChat.class);
                startActivity(i);
            }
        });

//        forum.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(getContext(), forum.class);
//                startActivity(i);
//            }
//        });

        return view;
    }

}
