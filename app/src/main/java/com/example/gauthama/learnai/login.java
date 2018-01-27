package com.example.gauthama.learnai;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class login extends AppCompatActivity {
    ImageView img;
    EditText mail, pass;
    Button login;
    TextView sign;

    String email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        img = (ImageView)findViewById(R.id.imageView);
        mail = (EditText)findViewById(R.id.mail);
        pass = (EditText)findViewById(R.id.pass);
        login = (Button)findViewById(R.id.login);
        sign = (TextView)findViewById(R.id.sign);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = mail.getText().toString();
                password = pass.getText().toString();

                Intent i = new Intent(login.this, Home.class);
                startActivity(i);
            }
        });


        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(login.this, SignUp.class);
                startActivity(i);
            }
        });
    }
}
