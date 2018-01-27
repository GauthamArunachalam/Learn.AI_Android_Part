package com.example.gauthama.learnai;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import static android.net.wifi.WifiConfiguration.Status.strings;

/**
 * Created by GAUTHAM A on 25-01-2018.
 */

public class Tab1_frag extends Fragment {
    private static final String TAG = "Tab1Fragement";

    ImageView img;
    TextView name, dis;
    Button vr, pdf, normal;
    TransferUtility transferUtility;
    CognitoCachingCredentialsProvider credentialsProvider;
    AmazonS3 s3;
    AWSCredentials creden=new BasicAWSCredentials("AKIAIKAF6ANQ34POVRTQ","iz5cQVwif9bMq4vadXOt6DU4gZApPjPY6Jo+ScoV");
    AmazonS3Client s3Client=new AmazonS3Client(creden);


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1_frag,container,false);

        img = (ImageView)view.findViewById(R.id.Pic);
        name = (TextView)view.findViewById(R.id.name);
        vr = (Button)view.findViewById(R.id.vr);
        pdf = (Button)view.findViewById(R.id.pdf);
        normal = (Button)view.findViewById(R.id.normal);

        credentialsProvider=new CognitoCachingCredentialsProvider(getContext(),
                "us-east-1:064736a0-7e20-4216-bfed-da6127a29a1a",
                Regions.US_EAST_1);
        s3=new AmazonS3Client(credentialsProvider);

        System.out.println("Cred: "+credentialsProvider);
        transferUtility  = new TransferUtility(s3,view.getContext());

        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Download Started", Toast.LENGTH_LONG).show();
                new MyTask_S3_PDF_Down().execute();
            }
        });




        return view;
    }

    public class MyTask_S3_PDF_Down extends AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... strings) {

            System.out.println("Works");

            s3.setRegion(Region.getRegion(Regions.US_EAST_1));
            System.out.println("S3"+s3);

            String str_FilePathInDevice = "/sdcard/" + "/"
                    + "Learn.AI" + "/" + "Learn_AIAbs.pdf";

            File file = new File(str_FilePathInDevice);

            String str_Path = file.getPath().replace(file.getName(), "");
            File filedir = new File(str_Path);

            try {
                filedir.mkdirs();
            } catch (Exception ex1) {
            }
//
//            TransferObserver observer = transferUtility.download( "apjpdf", "Learn_AIAbs.pdf", new File(Environment.getExternalStorageDirectory()+"/Learn.AI/pdf/Learn_AIAbs.pdf"));
//            System.out.println("Observer: "+observer);
//            System.out.println("Utility: "+observer);

            S3Object object = s3Client.getObject(new GetObjectRequest("apjpdf", "Learn_AIAbs.pdf"));

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    object.getObjectContent()));
            Writer writer = null;
            try {
                writer = new OutputStreamWriter(new FileOutputStream(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            while (true) {
                String line = null;

                    line = reader.readLine();

                if (line == null)
                    break;

                    writer.write(line + "\n");

            }

                writer.flush();


                writer.close();


                reader.close();


            //Toast.makeText(getActivity(),"Download Finished ",Toast.LENGTH_LONG).show();


//            observer.setTransferListener(new TransferListener() {
//                @Override
//                public void onStateChanged(int id, TransferState state) {
//                    System.out.println("State Changed");
//                }
//
//                @Override
//                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
//                    Toast.makeText(getActivity(), "Download Successfull",Toast.LENGTH_LONG).show();
//                }
//
//                @Override
//                public void onError(int id, Exception ex) {
//                    System.out.println("EXCEPTION "+ex);
//                }
//            });
          //  Toast.makeText(getActivity(),"The File is in sd card/Learn.AI",Toast.LENGTH_SHORT).show();
            return null;
        }
    }


}
