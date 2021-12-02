package com.grit.chatsample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        application app=(application) getApplicationContext();
        //app.pushMessage(new Message("hamza","hello","12/1/2021"),"hamza","abdullah");
        //app.updateUserLastMessage("hamza","hello321");


        app.getUserMessages("abdullah","hamza");


        /*app.checkIfUserExist("hamza321", new UserVerificationCallback() {
            @Override
            public void handleVerification(boolean success, String message, Users user) {
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();

                if(success){
                   app.registerUser(new Users("hamza","4321",""));
                }
            }
        });*/

        /*app.validateUserNamePassword("abdullah111", new UserVerificationCallback() {
            @Override
            public void handleVerification(boolean success, String message, Users user) {
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(addNewUserBroadcastReceiver,
                new IntentFilter(Constants.ADD_NEW_USER));


        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(addNewMessageBroadcastReceiver,
                new IntentFilter(Constants.ADD_NEW_MESSAGE));


        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(userStatusBroadcastReceiver,
                new IntentFilter(Constants.USER_STATUS));


    }


    private BroadcastReceiver addNewUserBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Users> user = intent.getParcelableArrayListExtra(Constants.USER);
            Toast.makeText(getApplicationContext(),"Add New User = "+user.get(0).getUsername(),Toast.LENGTH_SHORT).show();
        }
    };



    private BroadcastReceiver addNewMessageBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Message> messageArrayList = intent.getParcelableArrayListExtra(Constants.MESSAGE);
            Toast.makeText(getApplicationContext(),"Add New Message = "+messageArrayList.size(),Toast.LENGTH_SHORT).show();
        }
    };

    private BroadcastReceiver userStatusBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean status = intent.getBooleanExtra(Constants.STATUS,false);
            Toast.makeText(getApplicationContext(),"User status = "+status,Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(addNewUserBroadcastReceiver);
        LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(addNewMessageBroadcastReceiver);
        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(userStatusBroadcastReceiver,
                new IntentFilter(Constants.USER_STATUS));

    }

}