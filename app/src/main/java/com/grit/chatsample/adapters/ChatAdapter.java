package com.grit.chatsample.adapters;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.grit.chatsample.R;
import com.grit.chatsample.application;
import com.grit.chatsample.pojos.Message;
import com.grit.chatsample.utils.DateUtils;
import java.util.ArrayList;

/**
 * The type Chat adapter.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private final String TAG = ChatAdapter.this.getClass().getSimpleName();
    private final RecyclerView recyclerView;
    application app;
    public ArrayList<Message> arrayList;
    AppCompatActivity activity;
    SharedPreferences mPrefs;

    public ChatAdapter(AppCompatActivity activity, RecyclerView recyclerView, ArrayList<Message> arrayList,
                       SharedPreferences mPrefs) {

        this.arrayList = arrayList;
        this.recyclerView = recyclerView;
        this.activity = activity;
        this.mPrefs = mPrefs;

        app = (application) activity.getApplicationContext();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder{

        TextView tvDateUp, tvDateDown;
        TextView tv_msg_ref_username;
        TextView tv_msg_ref_msg;


        public ChatViewHolder(View v, Message msg) {
            super(v);

            tv_msg_ref_username = v.findViewById(R.id.tv_msg_ref_name);
            tv_msg_ref_msg = v.findViewById(R.id.tv_msg_ref_msg);

            tvDateUp = v.findViewById(R.id.tv_date_up);
            tvDateDown = v.findViewById(R.id.tv_date_down);

        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        Message msg = arrayList.get(viewType);

        if (msg != null && msg.getUsername().equalsIgnoreCase(mPrefs.getString("username", ""))) {
            return new ChatViewHolder(layoutInflater.inflate(R.layout.item_message_sender, parent, false), msg);
        }else{
            return new ChatViewHolder(layoutInflater.inflate(R.layout.item_message_receiver, parent, false), msg);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }



    @Override
    public void onBindViewHolder(final ChatViewHolder holder, int position) {
        if (holder != null) {

            setReply(position, holder);
            Message messageCurrent = arrayList.get(position);
            setTopBottomDate(position, messageCurrent, holder);

        }
    }


    private void setReply(int position, ChatViewHolder holder) {

        if (arrayList.get(position).getUsername().equalsIgnoreCase(mPrefs.getString("username", ""))) {
            holder.tv_msg_ref_username.setText("You");
        } else {
            holder.tv_msg_ref_username.setText(arrayList.get(position).getUsername());
        }

        holder.tv_msg_ref_msg.setText(arrayList.get(position).getMessage());
    }

    private void setTopBottomDate(int position, Message messageCurrent, ChatViewHolder holder) {
        try {
            Message messageNext = null;
            Message messagePrevious = null;

            if (position == 0) {
                holder.tvDateUp.setVisibility(View.VISIBLE);
                holder.tvDateDown.setVisibility(View.GONE);

                holder.tvDateUp.setText(messageCurrent.getDate());

            } else {
                try {
                    messageNext = arrayList.get(position + 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    messagePrevious = arrayList.get(position - 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (messageCurrent != null &&
                        (messageNext != null && !messageCurrent.getDate().equalsIgnoreCase(messageNext.getDate()))
                        || (messagePrevious != null && !messageCurrent.getDate().equalsIgnoreCase(messagePrevious.getDate()))) {
                    /*current and next date are not same.*/
                    if (position == arrayList.size() - 1) {
                        /*last position.*/
                        holder.tvDateDown.setVisibility(View.GONE);
                        holder.tvDateUp.setVisibility(View.VISIBLE);
                        holder.tvDateUp.setText(messageCurrent.getDate());
                    } else {
                        /*previous item date changed.*/
                        holder.tvDateDown.setVisibility(View.GONE);

                        if (messagePrevious != null && !messageCurrent.getDate().equalsIgnoreCase(messagePrevious.getDate())) {
                            holder.tvDateUp.setVisibility(View.VISIBLE);
                            holder.tvDateUp.setText(messageCurrent.getDate());
                        } else {
                            holder.tvDateUp.setVisibility(View.GONE);
                        }
                    }

                } else {
                    holder.tvDateUp.setVisibility(View.GONE);
                    holder.tvDateDown.setVisibility(View.GONE);
                }
            }

            if (position == arrayList.size() - 1) {
                /*last position*/
                holder.tvDateDown.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}