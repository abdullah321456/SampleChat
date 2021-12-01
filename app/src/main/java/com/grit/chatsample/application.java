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
import com.grit.chatsample.pojos.Message;
import com.grit.chatsample.pojos.Users;

import java.util.ArrayList;

public class application extends Application {

    FirebaseDatabase database;

    DatabaseReference userRef;

    DatabaseReference messageRef;


    DatabaseReference senderMessageRef;
    ValueEventListener senderEventListener;



    DatabaseReference lastMessageRef;
    ValueEventListener lastMessageEventListener;



    DatabaseReference checkUserExistDatabaseRef;
    ValueEventListener checkUserExistListener;

    @Override
    public void onCreate() {
        super.onCreate();
        database = FirebaseDatabase.getInstance();


        userRef = database.getReference("users/");
        lastMessageRef = database.getReference("users/");
        messageRef = database.getReference("messages/");
        senderMessageRef = database.getReference("messages/");

        getUserContacts();

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


    void registerUser(Users user) {
        userRef.child(user.getUsername()).setValue(user);
    }


    void pushMessage(Message message, String sender, String receiver) {

        DatabaseReference senderRef = messageRef.child(sender + "_" + receiver).push();
        senderRef.setValue(message);
        messageRef.child(receiver + "_" + sender + "/" + senderRef.getKey()).setValue(message);

    }

    void updateUserLastMessage(String user, String message) {

        if (lastMessageEventListener != null && lastMessageRef != null) {
            lastMessageRef.removeEventListener(lastMessageEventListener);
        }

        lastMessageEventListener = userRef.child(user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users prevUser=snapshot.getValue(Users.class);
                prevUser.setLastMessage(message);
                lastMessageRef.child(user).setValue(prevUser);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getUserContacts(){
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<Users> usersArrayList=new ArrayList<>();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    Users users=ds.getValue(Users.class);
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


        senderMessageRef.child(receiver+"_"+sender).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Message message=ds.getValue(Message.class);
                    System.out.println("message = "+message.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

}
