package com.mobile.gestureandvoice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.util.Log;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class VoiceActivity extends Activity{

    private static final String TAG = VoiceActivity.class.getName();

    //speech recognizer for callbacks
    private SpeechRecognizer mSpeechRecognizer;

    //handler to post changes to progress bar
    private Handler mHandler = new Handler();

    TextView responseText;

    //intent for speech recognize
    Intent mSpeechIntent;

    //this bool will record that it's time to kill P.A.L.
    boolean killCommanded = false;

    //legal commands
    private static final String[] VALID_COMMANDS = {
            "Edit",
            "New",
            "Delete"
    };
    private static final int VALID_COMMANDS_SIZE = VALID_COMMANDS.length;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
        responseText = (TextView) findViewById(R.id.textView);
    }

    @Override
    protected void onStart() {
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(VoiceActivity.this);
        SpeechListener mRecognitionListener = new SpeechListener();
        mSpeechRecognizer.setRecognitionListener(mRecognitionListener);
        mSpeechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        mSpeechIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"com.mobile.gestureandvoice");

        // Given an hint to the recognizer about what the user is going to say
        mSpeechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        // Specify how many results you want to receive. The results will be sorted
        // where the first result is the one with higher confidence.
        mSpeechIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 20);


        mSpeechIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);


        mSpeechRecognizer.startListening(mSpeechIntent);
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
    private String getResponse(int command){
        Calendar c = Calendar.getInstance();

        String retString =  "You can speak now!";
        SimpleDateFormat dfDate_day;
        switch (command) {
            case 0:

                break;
            case 1:

                break;
            case 2:
                killCommanded = true;
                break;

            default:
                break;
        }
        return retString;
    }

    @Override
    protected void onPause() {
        //kill the voice recognizer
        if(mSpeechRecognizer != null){
            mSpeechRecognizer.destroy();
            mSpeechRecognizer = null;

        }
        super.onPause();
    }

    private void processCommand(ArrayList<String> matchStrings){
        String response = "I'm sorry.";
        int maxStrings = matchStrings.size();
        boolean resultFound = false;
        for(int i =0; i < VALID_COMMANDS_SIZE && !resultFound;i++){
            for(int j=0; j < maxStrings && !resultFound; j++){
                if(StringUtils.getLevenshteinDistance(matchStrings.get(j), VALID_COMMANDS[i]) <(VALID_COMMANDS[i].length() / 3) ){
                    response = getResponse(i);
                }
            }
        }

        final String finalResponse = response;
        mHandler.post(new Runnable() {
            public void run() {
                responseText.setText(finalResponse);
            }
        });

    }
    class SpeechListener implements RecognitionListener {
        public void onBufferReceived(byte[] buffer) {
            Log.d(TAG, "buffer received ");
        }
        public void onError(int error) {
            //if critical error then exit
            if(error == SpeechRecognizer.ERROR_CLIENT || error == SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS){
                Log.d(TAG, "client error");
            }
            //else ask to repeats
            else{
                Log.d(TAG, "other error");
                mSpeechRecognizer.startListening(mSpeechIntent);
            }
        }
        public void onEvent(int eventType, Bundle params) {
            Log.d(TAG, "onEvent");
        }
        public void onPartialResults(Bundle partialResults) {
            Log.d(TAG, "partial results");
        }
        public void onReadyForSpeech(Bundle params) {
            Log.d(TAG, "on ready for speech");
        }
        public void onResults(Bundle results) {
            Log.d(TAG, "on results");
            ArrayList<String> matches = null;
            if(results != null){
                matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if(matches != null){
                    Log.d(TAG, "results are " + matches.toString());
                    final ArrayList<String> matchesStrings = matches;
                    processCommand(matchesStrings);
                    if(!killCommanded)
                        mSpeechRecognizer.startListening(mSpeechIntent);
                    else
                        finish();

                }
            }

        }
        public void onRmsChanged(float rmsdB) {
        }
        public void onBeginningOfSpeech() {
            Log.d(TAG, "speech beginning");
        }
        public void onEndOfSpeech() {
            Log.d(TAG, "speech done");
        }

    };

}
