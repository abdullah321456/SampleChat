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
import com.grit.chatsample.pojos.Users;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        application app=(application) getApplicationContext();


        for(int i=0;i<10;i++){
            app.pushMessage("hello"+i,"hamza");
        }

        /*app.checkIfUserExist("hamza", new UserVerificationCallback() {
            @Override
            public void handleVerification(boolean success, String message, Users user) {
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();

                if(success){
                   app.registerUser(new Users("hamza","4321"));
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

    }


    private BroadcastReceiver addNewUserBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String userName = intent.getStringExtra(Constants.USERNAME);
            Toast.makeText(getApplicationContext(),"Add New User = "+userName,Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(addNewUserBroadcastReceiver);

    }

}