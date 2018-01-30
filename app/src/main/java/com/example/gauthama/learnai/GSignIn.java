package com.example.gauthama.learnai;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.DefaultSyncCallback;
import com.amazonaws.mobileconnectors.cognito.Record;
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
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvingResultCallbacks;
import com.google.android.gms.common.api.Status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GSignIn extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener
{

    SignInButton signin;
    GoogleApiClient googleApiClient;
    public static int ReqCode = 100;
    CognitoCachingCredentialsProvider credentialsProvider;
    CognitoSyncManager syncManager;
    AmazonDynamoDBClient ddb;
    String username,email,photourl;
    private static final int sign_out = 8788;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ReqCode){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            GoogleSignInAccount account = result.getSignInAccount();

            String name = account.getDisplayName().replace(" ","");
            String mail = account.getEmail();
            String url = account.getPhotoUrl().toString();
            System.out.println("google pic url"+ url);
            SharedPreferences sharedPreferences = getSharedPreferences( "User", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name",name);
            editor.putString("email",mail);
            editor.putString("photourl",url);
            editor.putBoolean("isLogged",true);
            editor.commit();

            Dataset dataset = syncManager.openOrCreateDataset(name);
            dataset.put("name",name);
            dataset.put("email",mail);
            dataset.synchronize(new DefaultSyncCallback(){
                @Override
                public void onSuccess(Dataset dataset, List<Record> updatedRecords) {
                    super.onSuccess(dataset, updatedRecords);
                   // Toast.makeText(GSignIn.this, "Success", Toast.LENGTH_SHORT).show();
                }
            });


            new Create_Table_Task().execute();

            startActivity(new Intent(GSignIn.this,Home.class));

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gsign_in);

        signin = (SignInButton)findViewById(R.id.sign);


        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();

        SharedPreferences sharedPreferences = getSharedPreferences( "User", MODE_PRIVATE);
        Boolean islog = sharedPreferences.getBoolean("isLogged",false);
        if(islog){
            startActivity(new Intent(GSignIn.this,Home.class));
            finish();
        }


        credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-east-1:064736a0-7e20-4216-bfed-da6127a29a1a", // Identity pool ID
                Regions.US_EAST_1 );

        ddb=new AmazonDynamoDBClient(credentialsProvider);
        ddb.setRegion(Region.getRegion(Regions.US_EAST_1));

        syncManager = new CognitoSyncManager( getApplicationContext(),Regions.US_EAST_1,credentialsProvider);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);

                startActivityForResult(intent,ReqCode);
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    public class Create_Table_Task extends AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... strings) {

            SharedPreferences sharedPreferences = getSharedPreferences( "User", MODE_PRIVATE);
             username = sharedPreferences.getString("name","");
             email = sharedPreferences.getString("email","");
             photourl = sharedPreferences.getString("photourl","");


            List<AttributeDefinition> attributeDefinitions = new ArrayList<>();
            attributeDefinitions.add(new AttributeDefinition().withAttributeName("Email").withAttributeType(ScalarAttributeType.S));
            attributeDefinitions.add(new AttributeDefinition().withAttributeName("Username").withAttributeType(ScalarAttributeType.S));


            List<KeySchemaElement> keySchemaElements = new ArrayList<>();
            keySchemaElements.add(new KeySchemaElement().withAttributeName("Email").withKeyType(KeyType.HASH));
            keySchemaElements.add(new KeySchemaElement().withAttributeName("Username").withKeyType(KeyType.RANGE));

            CreateTableRequest req = new CreateTableRequest().withTableName(username)
                    .withAttributeDefinitions(attributeDefinitions)
                    .withKeySchema(keySchemaElements)
                    .withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(1L).withWriteCapacityUnits(1L));

            try{
                CreateTableResult result = ddb.createTable(req);
                System.out.println("Result Of Table Creation : "+ result.getTableDescription());
                new AddItem_Task().execute();

            }catch (final ResourceInUseException e){
                System.out.print("Error: "+ e.getErrorMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(GSignIn.this, e.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        new AddItem_Task().execute();
                    }
                });
            }


            return  null;
        }

        public class AddItem_Task extends AsyncTask<String, Integer, String>{

            @Override
            protected String doInBackground(String... strings) {
                Map<String, AttributeValue> data = new HashMap<>();


                data.put("Email", new AttributeValue().withS(email));
                data.put("Username", new AttributeValue().withS(username));
                data.put("PhotoUrl", new AttributeValue().withS(photourl));

                PutItemRequest putItemRequest = new PutItemRequest().withTableName(username).withItem(data);
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
