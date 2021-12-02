package com.grit.chatsample;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.grit.chatsample.Constants.Constants;
import com.grit.chatsample.Interface.UserVerificationCallback;
import com.grit.chatsample.pojos.Message;
import com.grit.chatsample.pojos.Users;

import java.util.ArrayList;
import java.util.HashMap;

public class application extends Application {



    public static boolean status=false;


    FirebaseDatabase database;

    DatabaseReference userRef;

    DatabaseReference messageRef;


    DatabaseReference senderMessageRef;
    ValueEventListener senderEventListener;



    DatabaseReference lastMessageRef;
    ValueEventListener lastMessageEventListener;



    DatabaseReference checkUserExistDatabaseRef;
    ValueEventListener checkUserExistListener;


    DatabaseReference networkStatusDatabaseRef;


    SharedPreferences mPrefs;

    @Override
    public void onCreate() {
        super.onCreate();
        database = FirebaseDatabase.getInstance();

        mPrefs = getSharedPreferences("ChatPrefs", MODE_PRIVATE);
        userRef = database.getReference("users/");
        lastMessageRef = database.getReference("users/");
        messageRef = database.getReference("messages/");
        senderMessageRef = database.getReference("messages/");
        networkStatusDatabaseRef = database.getReference(".info/connected/");


        getUserContacts();
        checkOnlineOfflineStatus();
    }

    public void checkIfUserExist(String username, UserVerificationCallback userVerificationCallback) {

        if (checkUserExistDatabaseRef != null && checkUserExistListener != null) {
            checkUserExistDatabaseRef.removeEventListener(checkUserExistListener);
        }

        checkUserExistDatabaseRef = database.getReference("users/" + username);
        checkUserExistListener = checkUserExistDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if(snapshot!=null && snapshot.getValue(Users.class)!=null){
                    Users users=snapshot.getValue(Users.class);
                    userVerificationCallback.handleVerification(false, Constants.REGISTRATION_FAILED_MESSAGE, users);
                }else{
                    userVerificationCallback.handleVerification(true, Constants.REGISTRATION_SUCCESS_MESSAGE, null);
                }
                checkUserExistDatabaseRef.removeEventListener(checkUserExistListener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void validateUserNamePassword(String username, UserVerificationCallback userVerificationCallback) {

        if (checkUserExistDatabaseRef != null && checkUserExistListener != null) {
            checkUserExistDatabaseRef.removeEventListener(checkUserExistListener);
        }

        checkUserExistDatabaseRef = database.getReference("users/" + username);
        checkUserExistListener = checkUserExistDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot!=null && snapshot.getValue(Users.class)!=null){
                    Users users=snapshot.getValue(Users.class);
                    userVerificationCallback.handleVerification(true, Constants.VALIDATION_SUCCESS_MESSAGE,
                            users);
                }else{
                    userVerificationCallback.handleVerification(false, Constants.VALIDATION_FAILED_MESSAGE, null);
                }
                checkUserExistDatabaseRef.removeEventListener(checkUserExistListener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    public void registerUser(Users user) {
        userRef.child(user.getUsername()).setValue(user);
    }


    public void pushMessage(Message message, String sender, String receiver) {

        DatabaseReference senderRef = messageRef.child(sender + "_" + receiver).push();
        senderRef.setValue(message);
        messageRef.child(receiver + "_" + sender + "/" + senderRef.getKey()).setValue(message);

    }

    public void updateUserLastMessage(String user,String otherUser, String message) {

        if (lastMessageEventListener != null && lastMessageRef != null) {
            lastMessageRef.removeEventListener(lastMessageEventListener);
        }

        lastMessageRef.child(user).child("lastMessage").child(otherUser).setValue(message);
    }

    public void getUserContacts(){
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<Users> usersArrayList=new ArrayList<>();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    Users users=ds.getValue(Users.class);
                    assert users != null;
                    if(!users.getUsername().equalsIgnoreCase(mPrefs.getString("username", "")))
                            usersArrayList.add(users);
                }

                Intent intent = new Intent(Constants.ADD_NEW_USER);
                intent.putParcelableArrayListExtra(Constants.USER, usersArrayList);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void getUserMessages(String sender,String receiver){
        if(senderMessageRef!=null && senderEventListener!=null){
            senderMessageRef.removeEventListener(senderEventListener);
        }


        /*senderEventListener=senderMessageRef.child(receiver+"_"+sender).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ArrayList<Message> messageArrayList=new ArrayList<>();
                Message message=snapshot.getValue(Message.class);
                messageArrayList.add(message);

                Intent intent = new Intent(Constants.ADD_NEW_MESSAGE);
                intent.putParcelableArrayListExtra(Constants.MESSAGE, messageArrayList);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        senderEventListener=senderMessageRef.child(receiver+"_"+sender).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<Message> messageArrayList=new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Message message=ds.getValue(Message.class);
                    messageArrayList.add(message);
                    System.out.println("message = "+message.getMessage());
                }
                Intent intent = new Intent(Constants.ADD_NEW_MESSAGE);
                intent.putParcelableArrayListExtra(Constants.MESSAGE, messageArrayList);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



    public void logoutUser(String username){
        userRef.child(username).child("loggedIn").setValue(false);
    }


    public void checkOnlineOfflineStatus(){


        networkStatusDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    status=true;

                    Intent intent = new Intent(Constants.USER_STATUS);
                    intent.putExtra(Constants.STATUS, status);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);


                } else {
                    status=false;

                    Intent intent = new Intent(Constants.USER_STATUS);
                    intent.putExtra(Constants.STATUS, status);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                status=false;
            }
        });
    }

}
