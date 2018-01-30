package com.example.gauthama.learnai;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.w3c.dom.Text;

import java.io.File;

public class Home extends AppCompatActivity {

    ImageView profilePic;
    ImageButton addCourse;
    ImageView pic;
    TextView name;
    TextView profileName;
    AmazonDynamoDBClient ddb;
    ListView listView;
    CognitoCachingCredentialsProvider credentialsProvider;
    Button signOut;


    String username, email, photourl;

    int[] Images ={R.drawable.kalam,R.drawable.ganshi, R.drawable.e, R.drawable.kalpana};
    String[] Names = {"APJ Abdul Kalam", "Gandhi", "Einstein", "Kalpana"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences sharedPreferences = getSharedPreferences("User",MODE_PRIVATE);

        String name = sharedPreferences.getString("name","");
        String photourl = sharedPreferences.getString("photourl","");



        credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-east-1:064736a0-7e20-4216-bfed-da6127a29a1a", // Identity pool ID
                Regions.US_EAST_1 );
        ddb=new AmazonDynamoDBClient(credentialsProvider);

       // new RetrieveItem_Task().execute();

        profilePic =(ImageView)findViewById(R.id.profile_pic);
        profileName=(TextView)findViewById(R.id.ProfileName);
        signOut = (Button)findViewById(R.id.signout);
        listView = (ListView)findViewById(R.id.listview);




        profileName.setText(name);

        Glide.with(this).load(photourl).into(profilePic);

        System.out.println("PIC URl "+ photourl);

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences( "User", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                System.exit(1);
            }
        });

        CustomAdapter customAdapter = new CustomAdapter(Home.this, Names, Images);
        listView.setAdapter(customAdapter);

       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

               String select = Names[i];
               if(select.equals("APJ Abdul Kalam")){
                   Intent intent = new Intent(Home.this, course.class);
                   startActivity(intent);
               }
               else{
                   Toast.makeText(Home.this, "Not Available", Toast.LENGTH_LONG).show();
               }
           }
       });



    }
//
//    public class RetrieveItem_Task extends AsyncTask<String, Integer, String>{
//
//        @Override
//        protected String doInBackground(String... strings) {
//            List<String> gi = new ArrayList<>();
//            gi.add("Email");
//            gi.add("UserName");
//            gi.add("PhotoUrl");
//
//            SharedPreferences sharedPreferences = getSharedPreferences("User",MODE_PRIVATE);
//            email = sharedPreferences.getString("email","");
//            username = sharedPreferences.getString("name","");
//
//            System.out.println("boww "+ username);
//
//            Map<String, AttributeValue> data = new HashMap<>();
//            data.put("Email",new AttributeValue().withS(email));
//            data.put("Username",new AttributeValue().withS(username));
//
//            GetItemRequest getItemRequest = new GetItemRequest().withTableName(username).withAttributesToGet(gi).withConsistentRead(Boolean.TRUE);
//            getItemRequest.setKey(data);
//            GetItemResult result = ddb.getItem(getItemRequest);
//            System.out.print("The retrived data : "+ result.getItem());
//
//            return null;
//        }
//    }


    class CustomAdapter extends ArrayAdapter<String>{
        String[] names ;
        int[] pics;
        Context mContext;
        public CustomAdapter(@NonNull Context context, String names[], int pics[]) {
            super(context, R.layout.list_adapter);
            this.names = names;
            this.pics = pics;
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return names.length;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder mviewHolder = new ViewHolder();
            if(convertView==null) {
                LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.list_adapter, parent, false);
                mviewHolder.prof = (ImageView) convertView.findViewById(R.id.profile);
                mviewHolder.name = (TextView) convertView.findViewById(R.id.name);
                //mviewHolder.bt = (ImageButton) convertView.findViewById(R.id.addCourse);
                convertView.setTag(mviewHolder);
            }else{
                mviewHolder = (ViewHolder)convertView.getTag();
            }

                mviewHolder.prof.setImageResource(pics[position]);
                mviewHolder.name.setText(names[position]);

            return convertView;
        }
    }

    static class ViewHolder{
        ImageView prof;
        TextView name;
        //ImageButton bt;
    }
}

