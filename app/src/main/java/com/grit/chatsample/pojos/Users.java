package com.grit.chatsample.pojos;

import android.os.Parcel;
import android.os.Parcelable;

public class Users implements Parcelable {

    public Users(){

    }
    public Users(String username,String password,String lastMessage){
        this.username=username;
        this.password=password;
        this.lastMessage=lastMessage;
    }

    protected Users(Parcel in) {
        username = in.readString();
        password = in.readString();
        lastMessage = in.readString();
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

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    String lastMessage;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(lastMessage);
    }
}
