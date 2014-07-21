package com.mobile.gestureandvoice;

import android.app.Activity;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Quan on 7/17/2014.
 */
public class GestureActivity extends Activity implements OnGesturePerformedListener{

    GestureLibrary gestureLib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GestureOverlayView gestureOverlayView = new GestureOverlayView(this);
        View inflate = getLayoutInflater().inflate(R.layout.gesture_activity, null);
        gestureOverlayView.setGestureColor(Color.rgb(0, 255, 0));
        gestureOverlayView.setGestureVisible(true);
        gestureOverlayView.setUncertainGestureColor(Color.rgb(0, 0, 255));

        gestureOverlayView.addView(inflate);
        gestureOverlayView.addOnGesturePerformedListener(this);

        gestureLib = GestureLibraries.fromRawResource(this, R.raw.gestures);
        /*if (!gestureLib.load()) {
            Log.i("debug", "FAILED");
        } else {
            Log.i("debug", "LOADED");
        }*/
        setContentView(gestureOverlayView);
        //Log.i("debug", "onCreate");

    }

    @Override
    public void onGesturePerformed(GestureOverlayView gestureOverlayView, Gesture gesture) {
        ArrayList<Prediction> predictions = gestureLib.recognize(gesture);
        if (predictions.size() > 0) {
            Prediction prediction = predictions.get(0);
            if (prediction.score > 1) {
                String str = prediction.name;
                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@" + str);
                if (str.equals("E")) {
                    Toast.makeText(getApplicationContext(), "E", Toast.LENGTH_SHORT).show();
                } else if (str.equals("N")) {
                    Toast.makeText(getApplicationContext(), "N", Toast.LENGTH_SHORT).show();

                } else if (str.equals("D")) {
                    Toast.makeText(getApplicationContext(), "D", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }
}
