package com.grit.chatsample.pojos;

import android.os.Parcel;
import android.os.Parcelable;

public class Message implements Parcelable {
    protected Message(Parcel in) {
        username = in.readString();
        message = in.readString();
        date = in.readString();
    }

    public Message(){

    }
    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String username;
    String message;
    String date;

    public Message(String username,String message,String date){
        this.username=username;
        this.date=date;
        this.message=message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(message);
        dest.writeString(date);
    }
}
