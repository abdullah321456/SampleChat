package com.grit.chatsample.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.grit.chatsample.Constants.Constants;
import com.grit.chatsample.R;
import com.grit.chatsample.adapters.ContactsAdapter;
import com.grit.chatsample.application;
import com.grit.chatsample.pojos.Users;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {


    RecyclerView userListView;
    application app;
    private ContactsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);
        app=(application) getApplicationContext();
        userListView = findViewById(R.id.userListView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        app.getUserContacts();
        LocalBroadcastManager.getInstance(UserActivity.this).registerReceiver(addNewUserBroadcastReceiver,
                new IntentFilter(Constants.ADD_NEW_USER));

    }


    private BroadcastReceiver addNewUserBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Users> user = intent.getParcelableArrayListExtra(Constants.USER);

            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            userListView.setLayoutManager(layoutManager);
            adapter = new ContactsAdapter(UserActivity.this, user);
            userListView.setAdapter(adapter);

            setClickListeners(adapter);
        }
    };

    public void setClickListeners(ContactsAdapter adapter) {
        if (adapter != null) {
            adapter.setOnClickListener(new ContactsAdapter.ClickListener() {
                @Override
                public void onRowClick(int position, ArrayList<Users> list) {
                    if (position != -1) {
                        Users receiver = list.get(position);
                        Toast.makeText(getApplicationContext(), "You click "+ receiver.getUsername(), Toast.LENGTH_SHORT).show();
                    }
                }

            });
        }
    }
}
