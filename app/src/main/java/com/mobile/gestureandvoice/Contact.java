package com.mobile.gestureandvoice;

import android.net.Uri;

/**
 * Created by Quan on 7/15/2014.
 */
public class Contact {
    private String _name, _phone, _email, _address;
    private Uri _imageUri;

    public Contact(String name, String phone, String email, String address, Uri imageUri) {
        _name = name;
        _phone = phone;
        _email = email;
        _address = address;
        _imageUri = imageUri;
    }

    public String get_name() {
        return _name;
    }

    public String get_phone() {
        return _phone;
    }

    public String get_email() {
        return _email;
    }

    public String get_address() {
        return _address;
    }

    public Uri get_imageUri() {
        return _imageUri;
    }
}
