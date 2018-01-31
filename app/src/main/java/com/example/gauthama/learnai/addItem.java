package com.example.gauthama.learnai;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ResourceInUseException;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.google.android.gms.auth.api.Auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by GAUTHAM A on 31-01-2018.
 */

public class addItem extends Fragment {

    EditText title, content;
    public static int ReqCode = 100;
    Button submit;
    CognitoCachingCredentialsProvider credentialsProvider;
    CognitoSyncManager syncManager;
    AmazonDynamoDBClient ddb;
    String  tit, disc;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_item,container,false);

        title = (EditText)view.findViewById(R.id.title);
        content = (EditText)view.findViewById(R.id.con);
        submit = (Button)view.findViewById(R.id.sumbit);

        credentialsProvider = new CognitoCachingCredentialsProvider(
                getContext(),
                "us-east-1:064736a0-7e20-4216-bfed-da6127a29a1a", // Identity pool ID
                Regions.US_EAST_1 );

        ddb=new AmazonDynamoDBClient(credentialsProvider);
        ddb.setRegion(Region.getRegion(Regions.US_EAST_1));

        syncManager = new CognitoSyncManager( getContext(),Regions.US_EAST_1,credentialsProvider);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tit = title.getText().toString();
                disc = content.getText().toString();

                new Create_Table_Task().execute();

                Toast.makeText(getContext(),"Your data is uploded",Toast.LENGTH_SHORT).show();

                Intent i = new Intent(getContext(),course.class);
                startActivity(i);
            }
        });



        return view;
    }

    public class Create_Table_Task extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {

            List<AttributeDefinition> attributeDefinitions = new ArrayList<>();
            attributeDefinitions.add(new AttributeDefinition().withAttributeName("Title").withAttributeType(ScalarAttributeType.S));
            attributeDefinitions.add(new AttributeDefinition().withAttributeName("Content").withAttributeType(ScalarAttributeType.S));


            List<KeySchemaElement> keySchemaElements = new ArrayList<>();
            keySchemaElements.add(new KeySchemaElement().withAttributeName("Title").withKeyType(KeyType.HASH));
            keySchemaElements.add(new KeySchemaElement().withAttributeName("Content").withKeyType(KeyType.RANGE));


            CreateTableRequest req = new CreateTableRequest().withTableName("group")
                    .withAttributeDefinitions(attributeDefinitions)
                    .withKeySchema(keySchemaElements)
                    .withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(1L).withWriteCapacityUnits(1L));

            try {
                CreateTableResult result = ddb.createTable(req);
                System.out.println("Result Of Table Creation : " + result.getTableDescription());
                new Create_Table_Task.AddItem_Task().execute();

            } catch (final ResourceInUseException e) {
                System.out.print("Error: " + e.getErrorMessage());

                //Toast.makeText(addItem.this, e.getErrorMessage(), Toast.LENGTH_SHORT).show();
                new Create_Table_Task.AddItem_Task().execute();


            }


            return null;
        }

        public class AddItem_Task extends AsyncTask<String, Integer, String>{

            @Override
            protected String doInBackground(String... strings) {
                Map<String, AttributeValue> data = new HashMap<>();


                data.put("Title", new AttributeValue().withS(tit));
                data.put("Content", new AttributeValue().withS(disc));

                PutItemRequest putItemRequest = new PutItemRequest().withTableName("group").withItem(data);
                try{
                    PutItemResult result = ddb.putItem(putItemRequest);
                    System.out.println("boww"+ result.getAttributes());

                }catch (Exception e) {
                    new AddItem_Task().execute();

                }





                return null;
            }
        }
    }
}
