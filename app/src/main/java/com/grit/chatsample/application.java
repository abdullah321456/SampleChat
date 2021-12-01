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

    DatabaseReference checkUserExistDatabaseRef;
    ValueEventListener checkUserExistListener;

    @Override
    public void onCreate() {
        super.onCreate();
        database = FirebaseDatabase.getInstance();


        userRef= database.getReference("users");


        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user=snapshot.getValue(Users.class);
                System.out.println("new user added = "+user.getPassword()+"  "+user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void checkIfUserExist(String username, UserVerificationCallback userVerificationCallback){

        if(checkUserExistDatabaseRef!=null && checkUserExistListener!=null){
            checkUserExistDatabaseRef.removeEventListener(checkUserExistListener);
        }

        checkUserExistDatabaseRef=database.getReference(username);
        checkUserExistListener=checkUserExistDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users value = snapshot.getValue(Users.class);

                if(value!=null){
                    userVerificationCallback.handleVerification(false,Constants.REGISTRATION_FAILED_MESSAGE,value);
                }else{
                    userVerificationCallback.handleVerification(true,Constants.REGISTRATION_SUCCESS_MESSAGE,null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }







    public void validateUserNamePassword(String username,UserVerificationCallback userVerificationCallback){

        if(checkUserExistDatabaseRef!=null && checkUserExistListener!=null){
            checkUserExistDatabaseRef.removeEventListener(checkUserExistListener);
        }

        checkUserExistDatabaseRef=database.getReference(username);
        checkUserExistListener=checkUserExistDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users value = snapshot.getValue(Users.class);

                if(value==null){
                   userVerificationCallback.handleVerification(false,Constants.VALIDATION_FAILED_MESSAGE,null);
                }else{
                    userVerificationCallback.handleVerification(true,Constants.VALIDATION_SUCCESS_MESSAGE,value);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




    void registerUser(Users user){
        userRef.setValue(user);
    }
}
