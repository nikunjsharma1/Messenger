package com.nikunj.messenger.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.nikunj.messenger.R;
import com.nikunj.messenger.model.MessageModal;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter {

    ArrayList<MessageModal> messageModals;
    Context context;
    String recId;

    public ChatAdapter(ArrayList<MessageModal> list, Context context) {
        this.messageModals = list;
        this.context = context;
    }

    public ChatAdapter(ArrayList<MessageModal> messageModals, Context context, String recId) {
        this.messageModals = messageModals;
        this.context = context;
        this.recId = recId;
    }

    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == SENDER_VIEW_TYPE) {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender, parent, false);
            return new SenderViewHOlder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_reciever, parent, false);
            return new ReceiverViewHOlder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModal messageModal = messageModals.get(position);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(context).setTitle("Delete").setMessage("Are you sure you want to delete this message")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                String sender = FirebaseAuth.getInstance().getUid() + recId;
                                database.getReference().child("chats").child(sender).child(messageModal.getMessageId()).setValue(null);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ;
                    }
                }).show();
                return false;
            }
        });

        if (holder.getClass() == SenderViewHOlder.class) {
            ((SenderViewHOlder) holder).senderMessage.setText(messageModal.getMessage());

        } else {
            ((ReceiverViewHOlder) holder).receiverMessage.setText(messageModal.getMessage());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (messageModals.get(position).getuId().equals(FirebaseAuth.getInstance().getUid())) {
            return SENDER_VIEW_TYPE;
        } else {
            return RECEIVER_VIEW_TYPE;
        }

    }

    @Override
    public int getItemCount() {
        return messageModals.size();
    }

    public class ReceiverViewHOlder extends RecyclerView.ViewHolder {
        TextView receiverMessage, receiverTime;

        public ReceiverViewHOlder(@NonNull View itemView) {
            super(itemView);
            receiverMessage = itemView.findViewById(R.id.RecieverTextView);
            receiverTime = itemView.findViewById(R.id.RecieverTime);


        }
    }

    public class SenderViewHOlder extends RecyclerView.ViewHolder {
        TextView senderMessage, senderTime;

        public SenderViewHOlder(@NonNull View itemView) {
            super(itemView);
            senderMessage = itemView.findViewById(R.id.sender_Message);
            senderTime = itemView.findViewById(R.id.sender_Time);


        }
    }
}
