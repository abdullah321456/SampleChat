package com.grit.chatsample.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.grit.chatsample.Constants.Constants;
import com.grit.chatsample.MainActivity;
import com.grit.chatsample.R;
import com.grit.chatsample.adapters.ContactsAdapter;
import com.grit.chatsample.application;
import com.grit.chatsample.pojos.Users;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {


    RecyclerView userListView;
    application app;
    private ContactsAdapter adapter;
    SharedPreferences mPrefs;
    SpinKitView spin_kit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);
        mPrefs = getSharedPreferences("ChatPrefs", MODE_PRIVATE);
        app=(application) getApplicationContext();
        userListView = findViewById(R.id.userListView);
        spin_kit = findViewById(R.id.spin_kit);
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.main);
        toolbar.setOnMenuItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        spin_kit.setVisibility(View.VISIBLE);
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

            String userName = mPrefs.getString("username", "");

            adapter = new ContactsAdapter(UserActivity.this, user,userName);
            userListView.setAdapter(adapter);

            setClickListeners(adapter);

            spin_kit.setVisibility(View.GONE);
        }
    };

    public void setClickListeners(ContactsAdapter adapter) {
        if (adapter != null) {
            adapter.setOnClickListener(new ContactsAdapter.ClickListener() {
                @Override
                public void onRowClick(int position, ArrayList<Users> list) {
                    if (position != -1) {
                        Users receiver = list.get(position);
                        startActivity(new Intent(UserActivity.this, ChatActivity.class)
                                        .putExtra("receiver_name", receiver.getUsername()));
                    }
                }

            });
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                mPrefs.edit().putString("username", "").apply();
                mPrefs.edit().putString("password", "").apply();
                Intent mIntent = new Intent(UserActivity.this, LoginActivity.class);
                startActivity(mIntent);
                finish();
                break;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(UserActivity.this).unregisterReceiver(addNewUserBroadcastReceiver);
    }
}
