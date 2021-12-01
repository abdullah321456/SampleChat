package com.grit.chatsample.activities;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.appbar.AppBarLayout;
import com.grit.chatsample.Constants.Constants;
import com.grit.chatsample.R;
import com.grit.chatsample.adapters.ChatAdapter;
import com.grit.chatsample.application;
import com.grit.chatsample.pojos.Message;
import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity{

    application app;
    AppCompatActivity activity;
    SpinKitView spin_kit;
    SharedPreferences mPrefs;

    String receiver_name;
    private String sender_name;
    Date d1;
    Bundle extras;
    AppBarLayout app_bar;
    Toolbar toolbar;
    LinearLayout userInput;
    com.google.android.material.floatingactionbutton.FloatingActionButton mSendButton;
    EditText message_edt;
    TextView nameTV;
    TextView lastSeenTV;
    LinearLayout ivBack;


    private static final String SEND_IMAGE = "send_image";
    public ChatAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    public ArrayList<Message> chatMessages = new ArrayList<>();

    private LinearLayout ivBack2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        init();

        initViews();

        getBundleExtras();

        setClickListeners();

        initRecyclerView();

    }

    @Override
    protected void onResume() {
        super.onResume();

        spin_kit.setVisibility(View.VISIBLE);
        app.getUserMessages(sender_name, receiver_name);

        LocalBroadcastManager.getInstance(ChatActivity.this).registerReceiver(addNewMessageBroadcastReceiver,
                new IntentFilter(Constants.ADD_NEW_MESSAGE));
    }

    private BroadcastReceiver addNewMessageBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            chatMessages = intent.getParcelableArrayListExtra(Constants.MESSAGE);

            adapter = new ChatAdapter(ChatActivity.this
                    , recyclerView
                    , chatMessages
                    , mPrefs);

            DefaultItemAnimator animator = new DefaultItemAnimator() {
                @Override
                public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder viewHolder) {
                    return true;
                }
            };

            recyclerView.setItemAnimator(animator);
            recyclerView.setAdapter(adapter);
            spin_kit.setVisibility(View.GONE);
        }
    };

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(ChatActivity.this).unregisterReceiver(addNewMessageBroadcastReceiver);
        super.onDestroy();
    }

    private void initRecyclerView() {
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void setClickListeners() {

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = (String) mSendButton.getTag();
                String message = message_edt.getText().toString().trim();
                if (message.isEmpty()) {
                    Toast.makeText(activity, "Can't send empty message!", Toast.LENGTH_SHORT).show();
                } else {

                    spin_kit.setVisibility(View.VISIBLE);
                    String date = String.valueOf(android.text.format.DateFormat.format("dd-MM-yyyy", new java.util.Date()));

                    app.pushMessage(new Message(sender_name, message, date), sender_name, receiver_name);
                    app.updateUserLastMessage(sender_name, message);
                    app.updateUserLastMessage(receiver_name, message);

                    app.getUserMessages(sender_name, receiver_name);

                    message_edt.setText("");
                }

            }
        });
    }

    private void getBundleExtras() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            getExtras(getIntent());
        }
    }

    private void getExtras(Intent intent) {

        extras = intent.getExtras();

        sender_name = mPrefs.getString("username", "");
        receiver_name = extras.getString("receiver_name");

        nameTV.setText(receiver_name);
        lastSeenTV.setVisibility(View.GONE);
    }

    private void init() {
        app = (application) getApplicationContext();
        activity = ChatActivity.this;
        mPrefs = getSharedPreferences("ChatPrefs", MODE_PRIVATE);
    }

    private void initViews() {
        spin_kit = findViewById(R.id.spin_kit);
        toolbar = findViewById(R.id.toolbar);
        app_bar = (AppBarLayout) findViewById(R.id.app_bar);
        nameTV = (TextView) findViewById(R.id.nameTV);
        lastSeenTV = (TextView) findViewById(R.id.lastSeenTV);
        ivBack = (LinearLayout) findViewById(R.id.ivBack);
        recyclerView = findViewById(R.id.chat_item);
        userInput = findViewById(R.id.user_input);
        mSendButton = (com.google.android.material.floatingactionbutton.FloatingActionButton) findViewById(R.id.sendButton);
        message_edt = (EditText) findViewById(R.id.message);
    }
}
