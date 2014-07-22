package com.mobile.gestureandvoice;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Quan on 7/15/2014.
 */
public class Contact implements Parcelable{
    private String _name, _phone, _email, _address;
    private Uri _imageUri;
    private int _id;

    public Contact(int id, String name, String phone, String email, String address, Uri imageUri) {
        _id = id;
        _name = name;
        _phone = phone;
        _email = email;
        _address = address;
        _imageUri = imageUri;
    }
    public Contact(int id, String name, String phone, String email, String address) {
        _id = id;
        _name = name;
        _phone = phone;
        _email = email;
        _address = address;
    }

    public int get_id() {
        return _id;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this._id);
        parcel.writeString(this._name);
        parcel.writeString(this._phone);
        parcel.writeString(this._email);
        parcel.writeString(this._address);
    }

    public static final Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel source) {

            return new Contact(source.readInt(), source.readString(), source.readString(),
                    source.readString(), source.readString());
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }

    };


}
