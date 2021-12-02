package com.grit.chatsample.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;

public class Users implements Parcelable  {

    public Users(){

    }
    public Users(String username,String password,HashMap<String,String> lastMessage,boolean isLoggedIn){
        this.username=username;
        this.password=password;
        this.lastMessage=lastMessage;
        this.isLoggedIn=isLoggedIn;
    }


    protected Users(Parcel in) {
        username = in.readString();
        password = in.readString();
        isLoggedIn = in.readByte() != 0;
    }

    public static final Creator<Users> CREATOR = new Creator<Users>() {
        @Override
        public Users createFromParcel(Parcel in) {
            return new Users(in);
        }

        @Override
        public Users[] newArray(int size) {
            return new Users[size];
        }
    };

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    String username;
    String password;

    public HashMap<String, String> getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(HashMap<String, String> lastMessage) {
        this.lastMessage = lastMessage;
    }

    HashMap<String,String> lastMessage;

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    boolean isLoggedIn;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeString(password);
        parcel.writeByte((byte) (isLoggedIn ? 1 : 0));
    }
}
