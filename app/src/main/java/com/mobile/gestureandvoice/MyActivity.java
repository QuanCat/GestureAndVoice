package com.mobile.gestureandvoice;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MyActivity extends Activity{

    private static final int GESTURE = 0, VOICE = 1, DELETE = 2;

    EditText nameTxt, phoneTxt, emailTxt, addressTxt;
    ImageView contactImg;
    List<Contact> contacts = new ArrayList<Contact>();
    ListView contactListView;
    Uri imageUri = Uri.parse("android.resource://com.mobile.gestureandvoice/drawable/on_user_logo.png");
    //db
    DatabaseHandler dbHandler;
    //edit,delete contacts
    int longClickedItemIndex;
    ArrayAdapter<Contact> contactAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        nameTxt = (EditText) findViewById(R.id.txtName);
        phoneTxt = (EditText) findViewById(R.id.txtPhone);
        emailTxt = (EditText) findViewById(R.id.txtEmail);
        addressTxt = (EditText) findViewById(R.id.txtAddress);
        contactListView = (ListView) findViewById(R.id.listView);
        contactImg = (ImageView) findViewById(R.id.imgViewContactImage);
        //db
        dbHandler = new DatabaseHandler(getApplicationContext());

        //edit,delete item from list
        registerForContextMenu(contactListView);
        contactListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                // i -> position
                longClickedItemIndex = i;
                return false;
            }
        });

        //tab
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("creator");
        tabSpec.setContent(R.id.tabCreator);
        tabSpec.setIndicator("Creator");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("list");
        tabSpec.setContent(R.id.tabContactList);
        tabSpec.setIndicator("List");
        tabHost.addTab(tabSpec);
        //populate the list tab
        populateList();
        //Add Contact Button clickListener
        final Button addBtn = (Button) findViewById(R.id.btnAdd);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Contact contact = new Contact(dbHandler.getContactsCount(), String.valueOf(nameTxt.getText()), String.valueOf(phoneTxt.getText()),
                        String.valueOf(emailTxt.getText()), String.valueOf(addressTxt.getText()), imageUri);
                if (!contactExists(contact)) {
                    dbHandler.createContact(contact);
                    contacts.add(contact);
                    //notify database has been changed
                    contactAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), String.valueOf(nameTxt.getText()) + " Has Been Added to Your Contacts",
                            Toast.LENGTH_SHORT).show();
                    return;
                }


                Toast.makeText(getApplicationContext(), String.valueOf(nameTxt.getText()) + "already exists, please use a different name.",
                        Toast.LENGTH_SHORT).show();

                //Toast.makeText(getApplicationContext(), "Your Contact has been Created!", Toast.LENGTH_SHORT).show();
                /*addContact(nameTxt.getText().toString(), phoneTxt.getText().toString(), emailTxt.getText().toString(),
                        addressTxt.getText().toString());*/
              /*  contacts.add(new Contact(0, nameTxt.getText().toString(), phoneTxt.getText().toString(), emailTxt.getText().toString(),
                        addressTxt.getText().toString(), imageUri));*/

            }
        });

        nameTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                addBtn.setEnabled(String.valueOf(nameTxt.getText()).toString().trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //image clickListener
        contactImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Contact Image"), 1);
            }
        });

        if (dbHandler.getContactsCount() != 0) {
            contacts.addAll(dbHandler.getAllContacts());
        }

    }

    //pop-up window
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);

        //create menu
        menu.setHeaderIcon(R.drawable.pencil_icon);
        menu.setHeaderTitle("Contact Options");
        //menu.add(Menu.NONE, EDIT, menu.NONE, "Edit Contact");
        //menu.add(Menu.NONE, DELETE, menu.NONE, "Delete Contact");
        menu.add(Menu.NONE, GESTURE, menu.NONE, "Gesture");
        menu.add(Menu.NONE, VOICE, menu.NONE, "Voice");
        menu.add(Menu.NONE, DELETE, menu.NONE, "Delete");
    }

    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case GESTURE:
                try {
                    Class gestureClass = Class.forName("com.mobile.gestureandvoice.GestureActivity");
                    Intent intentGesture = new Intent(MyActivity.this, gestureClass);
                    startActivity(intentGesture);
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                break;
            case VOICE:
                try {
                    Class voiceClass = Class.forName("com.mobile.gestureandvoice.VoiceActivity");
                    Intent intentVoice = new Intent(MyActivity.this, voiceClass);
                    startActivity(intentVoice);
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case DELETE:
                dbHandler.deleteContact(contacts.get(longClickedItemIndex));
                contacts.remove(longClickedItemIndex);
                contactAdapter.notifyDataSetChanged();
                break;
        }
        return super.onContextItemSelected(item);
    }
    //check is the contact exists
    private boolean contactExists(Contact contact) {
        String name = contact.get_name();
        int contactCount = contacts.size();
        for (int i = 0; i < contactCount; i++) {
            if (name.compareToIgnoreCase(contacts.get(i).get_name()) == 0) {
                return true;
            }
        }
        return false;
    }
    //get image
    public void onActivityResult(int reqCode, int resCode, Intent data) {
        if (resCode == RESULT_OK) {
            if (reqCode == 1) {
                //image
                imageUri = data.getData();
                contactImg.setImageURI(data.getData());
            }
        }
    }

    //populate the contacts
    private void populateList() {
        contactAdapter = new ContactListAdapter();
        contactListView.setAdapter(contactAdapter);
    }
    //Add Contact without image
 /*   private void addContact(String name, String phone, String email, String address) {
        contacts.add(new Contact(name, phone, email, address));
    }*/
    //Contact List
    private class ContactListAdapter extends ArrayAdapter<Contact> {
        public ContactListAdapter() {
            super(MyActivity.this, R.layout.listview_contact, contacts);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.listview_contact, parent, false);
            }
            Contact currentContacts = contacts.get(position);
            System.out.println(currentContacts);
            TextView name = (TextView) view.findViewById(R.id.contactName);
            name.setText(currentContacts.get_name());
            TextView phone = (TextView) view.findViewById(R.id.phoneNumber);
            phone.setText(currentContacts.get_phone());
            TextView email = (TextView) view.findViewById(R.id.emailAddress);
            email.setText(currentContacts.get_email());
            TextView address = (TextView) view.findViewById(R.id.cAddress);
            address.setText(currentContacts.get_address());
            ImageView ivContactImg = (ImageView) view.findViewById(R.id.ivContactImgDisplay);
            System.out.println("**********************"+ivContactImg);
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
