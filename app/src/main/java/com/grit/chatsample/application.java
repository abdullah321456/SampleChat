package com.grit.chatsample;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.grit.chatsample.Constants.Constants;
import com.grit.chatsample.Interface.UserVerificationCallback;
import com.grit.chatsample.pojos.Users;

public class application extends Application {

    FirebaseDatabase database;

    DatabaseReference userRef;

    DatabaseReference messageRef;

    DatabaseReference checkUserExistDatabaseRef;
    ValueEventListener checkUserExistListener;

    @Override
    public void onCreate() {
        super.onCreate();
        database = FirebaseDatabase.getInstance();


        userRef = database.getReference("users/");
        messageRef = database.getReference("messages/");

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {
                    String key = ds.getKey();
                    Intent intent = new Intent(Constants.ADD_NEW_USER);
                    intent.putExtra(Constants.USERNAME, key);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void checkIfUserExist(String username, UserVerificationCallback userVerificationCallback) {

        if (checkUserExistDatabaseRef != null && checkUserExistListener != null) {
            checkUserExistDatabaseRef.removeEventListener(checkUserExistListener);
        }

        checkUserExistDatabaseRef = database.getReference("users/"+username);
        checkUserExistListener = checkUserExistDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if(snapshot.hasChildren()){
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String key = ds.getKey();

                        String username = ds.child("username").getValue(String.class);
                        String password = ds.child("password").getValue(String.class);
                        userVerificationCallback.handleVerification(false, Constants.REGISTRATION_FAILED_MESSAGE,
                                new Users(username,password));
                    }
                }else{
                    userVerificationCallback.handleVerification(true, Constants.REGISTRATION_SUCCESS_MESSAGE, null);

                }
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

        checkUserExistDatabaseRef = database.getReference("users/"+username);
        checkUserExistListener = checkUserExistDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.hasChildren()){
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String key = ds.getKey();

                        String username = ds.child("username").getValue(String.class);
                        String password = ds.child("password").getValue(String.class);
                        userVerificationCallback.handleVerification(true, Constants.VALIDATION_SUCCESS_MESSAGE,
                                new Users(username,password));
                    }
                }else{
                    userVerificationCallback.handleVerification(false, Constants.VALIDATION_FAILED_MESSAGE, null);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    void registerUser(Users user) {
        userRef.child(user.getUsername()).setValue(user);
    }


    void pushMessage(String message,String username){
        messageRef.child(username).push().setValue(message);
    }
}
