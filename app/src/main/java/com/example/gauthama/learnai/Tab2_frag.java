package com.example.gauthama.learnai;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by GAUTHAM A on 25-01-2018.
 */

public class Tab2_frag extends Fragment {
    private static final String TAG = "Tab1Fragement";

    Button quiz, check, next,back;
    View exview;
    TextView questionTitle, question, ans;
    RadioGroup rg;
    RadioButton rb1, rb2, rb3, rb4;
    CognitoCachingCredentialsProvider credentialsProvider;
    AmazonDynamoDBClient ddb;
    List<Map<String, AttributeValue>> bow;
    String ques, opt1, opt2, opt3, opt4, answer;
    int pos =0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2, container, false);
        exview = view;
        quiz = (Button) view.findViewById(R.id.quiz);
        check = (Button)view.findViewById(R.id.check);
        next = (Button)view.findViewById(R.id.next);
        questionTitle = (TextView)view.findViewById(R.id.questionTitle);
        question = (TextView)view.findViewById(R.id.question);
        rb1 = (RadioButton)view.findViewById(R.id.rbutton1);
        rb2 = (RadioButton)view.findViewById(R.id.rbutton2);
        rb3 = (RadioButton)view.findViewById(R.id.rbutton3);
        rb4 = (RadioButton)view.findViewById(R.id.rbutton4);
        ans = (TextView)view.findViewById(R.id.ans);
        rg = (RadioGroup)view.findViewById(R.id.rbgroup);
        back = (Button)view.findViewById(R.id.back);

        check.setVisibility(view.INVISIBLE);
        next.setVisibility(view.INVISIBLE);
        questionTitle.setVisibility(view.INVISIBLE);
        question.setVisibility(view.INVISIBLE);
        rg.setVisibility(view.INVISIBLE);
        back.setVisibility(view.INVISIBLE);

        credentialsProvider = new CognitoCachingCredentialsProvider(
                getActivity(),
                "us-east-1:064736a0-7e20-4216-bfed-da6127a29a1a", // Identity pool ID
                Regions.US_EAST_1 );

        ddb=new AmazonDynamoDBClient(credentialsProvider);
        ddb.setRegion(Region.getRegion(Regions.US_EAST_1));

        new RetrieveItem_Task().execute();

        quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                check.setVisibility(view.VISIBLE);
               // next.setVisibility(view.VISIBLE);
                questionTitle.setVisibility(view.VISIBLE);
                question.setVisibility(view.VISIBLE);
                rg.setVisibility(view.VISIBLE);

                quiz.setVisibility(view.INVISIBLE);


                select(pos);


            }
        });

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sel = null;
              if(rb1.isChecked()){
                  sel = rb1.getText().toString();
              }
              else if(rb2.isChecked()){
                  sel = rb2.getText().toString();
              }
              else if(rb3.isChecked()){
                  sel = rb3.getText().toString();
              }
              else if(rb4.isChecked()){
                  sel = rb4.getText().toString();
              }

              if(sel==null){
                  Toast.makeText(getContext(),"Please Select some option",Toast.LENGTH_SHORT).show();
              }
              else {

                  if (sel.equals(answer)) {
                      ans.setText("Correct");
                      ans.setBackgroundColor(Color.GREEN);
                  } else {
                      ans.setText("Wrong");
                      ans.setBackgroundColor(Color.RED);
                  }

                  if (ans.getText().equals("Correct")) {
                      check.setVisibility(view.INVISIBLE);
                      next.setVisibility(view.VISIBLE);

                  }
              }


            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),Home.class);
                startActivity(i);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pos<19) {
                    pos = pos + 1;
                    select(pos);
                    check.setVisibility(view.VISIBLE);
                    next.setVisibility(view.INVISIBLE);
                    ans.setBackgroundColor(Color.WHITE);
                    ans.setText("");
                    rb1.setChecked(false);
                    rb2.setChecked(false);
                    rb3.setChecked(false);
                    rb4.setChecked(false);
                }
                else{

                    rg.setVisibility(view.INVISIBLE);
                    question.setText("Congratulations you Completed the Quiz!!!!");
                    next.setVisibility(view.INVISIBLE);
                    check.setVisibility(view.INVISIBLE);
                    back.setVisibility(view.VISIBLE);
                    ans.setVisibility(view.INVISIBLE);
                }
            }
        });





        return view;
    }

    public void select(int i){
        Map<String, AttributeValue> dt = bow.get(i);
         ques = dt.get("ques").getS();
         opt1 = dt.get("op1").getS();
         opt2 = dt.get("op2").getS();
         opt3 = dt.get("op3").getS();
         opt4 = dt.get("op4").getS();
         answer = dt.get("answer").getS();

        question.setText(ques);
        rb1.setText(opt1);
        rb2.setText(opt2);
        rb3.setText(opt3);
        rb4.setText(opt4);


    }




    public class RetrieveItem_Task extends AsyncTask<String, Integer, String > {

        @Override
        protected String doInBackground(String... strings) {

            List<String> gi= new ArrayList<>();
            gi.add("ques");
            gi.add("answer");
            gi.add("op1");
            gi.add("op2");
            gi.add("op3");
            gi.add("op4");

            String dataaa= null;


            Map<String, AttributeValue> data=new HashMap<>();


            ScanRequest request = new ScanRequest().withTableName("quiz");

            ScanResult result = ddb.scan(request);

            bow = result.getItems();
            for(Map<String, AttributeValue> itemlist: bow){
                System.out.println("Item lIst is : "+itemlist);
                System.out.println("Item question : "+itemlist.get("ques"));

            }


            return null;
        }
    }

}
