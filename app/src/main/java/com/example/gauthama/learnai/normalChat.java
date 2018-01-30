package com.example.gauthama.learnai;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;

public class normalChat extends AppCompatActivity implements AIListener {

    TextView ques, res;
    Button lis;
    AIService aiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_chat);

        ques = (TextView)findViewById(R.id.question);
        res = (TextView)findViewById(R.id.result);
        lis = (Button)findViewById(R.id.lis);

        final AIConfiguration config = new AIConfiguration("2c890cf705f14c859fcdb438300d88eb",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        aiService = AIService.getService(this, config);
        aiService.setListener(this);




    }

    public void listenButtonOnClick(final View view) {
        aiService.startListening();
    }


    @Override
    public void onResult(AIResponse response) {
        Result result = response.getResult();
        String parameterString = "";
        String query = "";

        query = result.getResolvedQuery().toString();
        parameterString = result.getFulfillment().getSpeech().toString();
        res.setText("Response:" +parameterString);
        ques.setText("Query: "+ query);
    }

    @Override
    public void onError(AIError error) {

    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }
}
