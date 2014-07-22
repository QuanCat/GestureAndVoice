package com.mobile.gestureandvoice;

import android.app.Activity;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Quan on 7/17/2014.
 */
public class GestureActivity extends Activity implements OnGesturePerformedListener{

    GestureLibrary gestureLib;
    //db
    DatabaseHandler dbHandler;
    //pass values
    ListView contactListView;
    int longClickedItemIndex;
    ArrayList<Contact> contacts;
    ArrayAdapter<Contact> contactAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gesture_activity);
        dbHandler = new DatabaseHandler(getApplicationContext());
        //System.out.println("hahahahahahahaah==== " + contactListView);
        contactListView = (ListView) findViewById(R.id.listView);
        //System.out.println("contactListView==== ");

        contactAdapter = new ContactListAdapter();
        //System.out.println("herererererererere?==== " + contactListView);
        //contactListView.setAdapter(contactAdapter);
        longClickedItemIndex = getIntent().getIntExtra("contactIndex", -1);
        contacts = getIntent().getParcelableArrayListExtra("contacts");

        GestureOverlayView gestures = (GestureOverlayView) findViewById(R.id.gestures);
        gestures.addOnGesturePerformedListener(this);
        gestureLib = GestureLibraries.fromRawResource(this, R.raw.gestures);
        if (!gestureLib.load()) {
            finish();
        }
    }


    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        ArrayList predictions = gestureLib.recognize(gesture);
        //at least one prediction
        if (predictions.size() > 0) {
            Prediction prediction = (Prediction) predictions.get(0);
            // at least some confidence in the result
            if (prediction.score > 1.0) {
                String str = prediction.name;
                // Show the spell
                if (str.equals("new")) {
                    Toast.makeText(this, prediction.name, Toast.LENGTH_SHORT).show();
                    Class newContact = null;
                    try {
                        newContact = Class.forName("com.mobile.gestureandvoice.MyActivity");
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    Intent newIntent = new Intent(GestureActivity.this, newContact);
                    startActivity(newIntent);

                } else if (str.equals("edit")) {
                    Toast.makeText(this, prediction.name, Toast.LENGTH_SHORT).show();
                    int num = dbHandler.updateContact(contacts.get(longClickedItemIndex));

                } else if (str.equals("delete")) {
                    Toast.makeText(this, prediction.name, Toast.LENGTH_SHORT).show();
                    dbHandler.deleteContact(contacts.get(longClickedItemIndex));
                    contacts.remove(longClickedItemIndex);
                    //something wrong here
                    contactAdapter.notifyDataSetChanged();
                    //direct to Contact List

                }

            }
        }

    }

    //Contact List
    private class ContactListAdapter extends ArrayAdapter<Contact> {
        public ContactListAdapter() {
            super(GestureActivity.this, R.layout.listview_contact, contacts);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            for (Contact c: contacts) {
                System.out.println("namennnnnnnnnnnnnnnnnnnnn!!!!!!!!="+c.get_name().toString());
            }
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.listview_contact, parent, false);
            }
            Contact currentContacts = contacts.get(position);
            TextView name = (TextView) view.findViewById(R.id.contactName);
            name.setText(currentContacts.get_name());
            TextView phone = (TextView) view.findViewById(R.id.phoneNumber);
            phone.setText(currentContacts.get_phone());
            TextView email = (TextView) view.findViewById(R.id.emailAddress);
            email.setText(currentContacts.get_email());
            TextView address = (TextView) view.findViewById(R.id.cAddress);
            address.setText(currentContacts.get_address());
            ImageView ivContactImg = (ImageView) view.findViewById(R.id.ivContactImgDisplay);
            //System.out.println("**********************"+ivContactImg);
            /*if (!ivContactImg.equals(null)) {
                ivContactImg.setImageURI(currentContacts.get_imageUri());
            }*/

            return view;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
