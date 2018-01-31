package com.example.gauthama.learnai;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by GAUTHAM A on 25-01-2018.
 */

public class Tab3_frag extends Fragment {
    private static final String TAG = "Tab1Fragement";
    FloatingActionButton fab;
    RecyclerView recyclerView;
    CognitoCachingCredentialsProvider credentialsProvider;
    AmazonDynamoDBClient ddb;
    List<Map<String, AttributeValue>> bow;
    View exview;

//    String[] title = new String[50];
//    String[] content = new String[50];

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab3,container,false);

        fab = (FloatingActionButton)view.findViewById(R.id.floatingActionButton);
        recyclerView = (RecyclerView)view.findViewById(R.id.re);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        credentialsProvider = new CognitoCachingCredentialsProvider(
                getActivity(),
                "us-east-1:064736a0-7e20-4216-bfed-da6127a29a1a", // Identity pool ID
                Regions.US_EAST_1 );

        ddb=new AmazonDynamoDBClient(credentialsProvider);
        ddb.setRegion(Region.getRegion(Regions.US_EAST_1));

        new RetrieveItem_Task().execute();

        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        int i =0;

//        for(i=0;i<=bow.size();i++)
//            set(i);

        Tab3_frag.MyRecyclerAdapter customAdapter = new Tab3_frag.MyRecyclerAdapter(getContext(),bow);
        recyclerView.setAdapter(customAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment child = new addItem();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.fraglay, child).commit();
                fab.setVisibility(view.INVISIBLE);
                recyclerView.setVisibility(view.INVISIBLE);

            }
        });


        return view;
    }

//    public void set(int i){
//        Map<String, AttributeValue> item = bow.get(i);
//        title[i] = item.get("Title").getS();
//        content[i] = item.get("Content").getS();



    public class RetrieveItem_Task extends AsyncTask<String, Integer, String > {

        @Override
        protected String doInBackground(String... strings) {
            List<String> gi= new ArrayList<>();
            gi.add("Title");
            gi.add("Content");

            Map<String, AttributeValue> data=new HashMap<>();

            ScanRequest request = new ScanRequest().withTableName("group");

            ScanResult result = ddb.scan(request);

            bow = result.getItems();

            System.out.println("Bow Length: "+bow.size());
//            for(Map<String, AttributeValue> itemlist: bow){
//                System.out.println("Forum Title : "+itemlist);
//                System.out.println("Forum Content : "+itemlist.get("Title"));
//
//            }


            return null;
        }
    }

    public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {

        Context mctx;
        List<Map<String, AttributeValue>> lst;

        public MyRecyclerAdapter(Context context, List<Map<String, AttributeValue>> list) {
            this.mctx = context;
            this.lst = list;

        }

        @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater= LayoutInflater.from(mctx);
            View v = inflater.inflate(R.layout.recycler, null);
            ViewHolder holder = new ViewHolder(v);
            return holder;
        }

        @Override public void onBindViewHolder(ViewHolder holder, int position) {
            Map<String, AttributeValue> val = lst.get(position);
            //ViewModel item = items.get(position);

            String ti = val.get("Title").getS().toString();
            String cons = val.get("Content").getS().toString();

            System.out.println("Check da : "+ti+" "+cons);
            holder.tit.setText(ti);
            holder.con.setText(cons);
//            Picasso.with(holder.image.getContext()).cancelRequest(holder.image);
//            Picasso.with(holder.image.getContext()).load(item.getImage()).into(holder.image);
           // holder.itemView.setTag(item);
        }

        @Override public int getItemCount() {
            return lst.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView con,tit;

            public ViewHolder(View itemView) {
                super(itemView);
                tit = (TextView) itemView.findViewById(R.id.title);
                con = (TextView) itemView.findViewById(R.id.content);
            }
        }
    }

}
