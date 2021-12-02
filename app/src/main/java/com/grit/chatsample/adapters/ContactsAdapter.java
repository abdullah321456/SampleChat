package com.grit.chatsample.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.grit.chatsample.R;
import com.grit.chatsample.pojos.Users;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {

    public ArrayList<Users> list;
    AppCompatActivity act;
    ClickListener listener;
    String userName;


    public ContactsAdapter(AppCompatActivity act, ArrayList<Users> list,String userName) {
        this.act = act;
        this.list = list;
        this.userName=userName;
    }

    public interface ClickListener {

        void onRowClick(int position, ArrayList<Users> list);
    }

    public class ContactsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout user_layout;
        TextView tittle;
        TextView tvLastMessage;

        public ContactsViewHolder(View view) {
            super(view);
            user_layout = view.findViewById(R.id.user_layout);
            tittle = view.findViewById(R.id.tv_name);
            tvLastMessage = view.findViewById(R.id.tv_last_message);

            user_layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.user_layout:
                    if (listener != null) {
                        listener.onRowClick(getAdapterPosition(), list);
                    }
                    break;
            }
        }
    }

    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ContactsViewHolder holder, int position) {
        Users user = list.get(position);
        if (user != null) {
            String username = user.getUsername();
            String last_message="";
            HashMap<String,String> lastMessageHash = user.getLastMessage();

            if(lastMessageHash!=null && lastMessageHash.get(userName)!=null && lastMessageHash.get(userName).length()>0){
                last_message=lastMessageHash.get(userName);
            }

            holder.tittle.setText(username);
            holder.tvLastMessage.setText(last_message);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public void setOnClickListener(ClickListener listener) {
        this.listener = listener;
    }


}