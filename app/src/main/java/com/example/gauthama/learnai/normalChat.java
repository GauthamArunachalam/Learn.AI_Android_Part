package com.example.gauthama.learnai;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import java.util.Locale;

import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIDataService;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;

public class normalChat extends AppCompatActivity implements AIListener, TextToSpeech.OnInitListener {

    TextView ques, res;
    TextToSpeech tts;
    Button lis,speak;
   // EditText textIn;
    AIService aiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_chat);

        ques = (TextView)findViewById(R.id.question);
        res = (TextView)findViewById(R.id.result);
        speak = (Button)findViewById(R.id.speak);
        tts = new TextToSpeech(this, this);
//        textPro = (Button)findViewById(R.id.sendText);
//        textIn = (EditText)findViewById(R.id.textInput);


        lis = (Button)findViewById(R.id.lis);

        final AIConfiguration config = new AIConfiguration("2c890cf705f14c859fcdb438300d88eb",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        aiService = AIService.getService(this, config);
        aiService.setListener(this);

        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speakOut();
            }
        });

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
        res.setText(error.toString());
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

    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                speak.setEnabled(true);
                speakOut();
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }

    private void speakOut() {

        String text = res.getText().toString();

        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
}
