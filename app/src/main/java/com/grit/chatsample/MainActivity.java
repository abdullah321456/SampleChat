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
import com.grit.chatsample.pojos.Users;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Users user=new Users();




        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("users/"+"abdullah321");

        /*user.setUsername("abdullah");
        user.setPassword("4321");
        userRef.setValue(user);*/


        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users value = snapshot.getValue(Users.class);
                if(value!=null){
                    System.out.println("snapshot = "+value.getUsername()+" password = "+value.getPassword());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("error = "+error.getDetails());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}