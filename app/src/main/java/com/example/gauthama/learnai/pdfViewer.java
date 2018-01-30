package com.example.gauthama.learnai;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class pdfViewer extends AppCompatActivity {

    File fileToDownload = new File("/storage/sdcard0/Learn.AI/learn.pdf");
    AmazonS3 s3;
    TransferUtility transferUtility;
    PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);
        pdfView = (PDFView)findViewById(R.id.abc);




        credentialsProvider();
      //  setTransferUtility();
        setFileToDownload();

//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


        // File f = new File("/storage/sdcard0/Learn.AI/Learn_AIAbs.pdf");
        pdfView.fromFile(fileToDownload).load();
//        pdfView.fromUri(Uri.fromFile(new File("/storage/sdcard0/Learn.AI/Learn_AIAbs.pdf"))).load();
    }

    public void credentialsProvider(){
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-east-1:892dea48-9d18-407d-a8f2-5cb047882a99", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );

        setAmazonS3Client(credentialsProvider);
    }

    public void setAmazonS3Client(CognitoCachingCredentialsProvider credentialsProvider){
        s3 = new AmazonS3Client(credentialsProvider);
        s3.setRegion(Region.getRegion(Regions.US_EAST_1));
    }

//    public void setTransferUtility(){
//        transferUtility = new TransferUtility(s3, getApplicationContext());
//    }

    public void setFileToDownload(){

        new S3_download().execute();

//        TransferObserver transferObserver = transferUtility.download(
//                "apjpdf",
//                "Learn_AIAbs.pdf",
//                fileToDownload
//        );
//
//        transferObserverListener(transferObserver);
    }

//    public void transferObserverListener(TransferObserver transferObserver){
//        transferObserver.setTransferListener(new TransferListener(){
//            @Override
//            public void onStateChanged(int id, TransferState state) {
//                Log.e("statechange", state+"");
//            }
//            @Override
//            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
//                //try{
////                int percentage = (int) (bytesCurrent/bytesTotal * 100);
////                    Log.e("percentage",percentage +"");
////                }catch(ArithmeticException e){
////                    System.out.print("Error");
////                }
//                Toast.makeText(pdfViewer.this, "Download Success", Toast.LENGTH_SHORT).show();
//            }
//            @Override
//            public void onError(int id, Exception ex) {
//                System.out.println("Error: "+ex);
//            }
//        });
//    }
    public class S3_download extends AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... strings) {
            TransferUtility transferUtility = new TransferUtility(s3,getApplicationContext());


            TransferListener listener = new TransferListener() {
                @Override
                public void onStateChanged(int id, TransferState state) {
                    System.out.println("State "+state);
                }

                @Override
                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                   System.out.println("Total Bytes:"+ bytesTotal);
                }

                @Override
                public void onError(int id, Exception ex) {
                  System.out.println("bow "+ex);
                }
            };

            transferUtility.download("apjpdf","apj.pdf",fileToDownload).setTransferListener(listener);
            return null;
        }


    }
}
