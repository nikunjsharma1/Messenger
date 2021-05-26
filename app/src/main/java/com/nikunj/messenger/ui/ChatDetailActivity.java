package com.nikunj.messenger.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nikunj.messenger.R;
import com.nikunj.messenger.adapter.ChatAdapter;
import com.nikunj.messenger.databinding.ActivityChatDetailBinding;
import com.nikunj.messenger.model.MessageModal;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatDetailActivity extends AppCompatActivity {
    ActivityChatDetailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
         getSupportActionBar().hide();

        final String senderId = auth.getUid();
        String recieverId = getIntent().getStringExtra("userId");
        String userName = getIntent().getStringExtra("name");
        String profilePic = getIntent().getStringExtra("profile");
        binding.userNameDetail.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.profile).into(binding.profileImage);

        final String senderRoom = senderId + recieverId;
        final String receiverRoom = recieverId + senderId;

        binding.backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatDetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        final ArrayList<MessageModal> messageModals = new ArrayList<>();
        final ChatAdapter chatAdapter = new ChatAdapter(messageModals, this,recieverId);
        binding.chatDetailRecyclerview.setAdapter(chatAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatDetailRecyclerview.setLayoutManager(layoutManager);
        database.getReference().child("chats").child(senderRoom).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageModals.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                   MessageModal modal=snapshot1.getValue(MessageModal.class);
                   modal.setMessageId(snapshot1.getKey());
                   messageModals.add(modal);
                }
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( binding.typeAMessage.getText().toString().isEmpty()){
                    binding.typeAMessage.setError("type something");
                    return;
                }
                String message = binding.typeAMessage.getText().toString();
                final MessageModal modal = new MessageModal(senderId, message);
                modal.setTimeStamp(new Date().getTime());
                binding.typeAMessage.setText("");
                database.getReference().child("chats").child(senderRoom).push().setValue(modal).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        database.getReference().child("chats").child(receiverRoom).push().setValue(modal).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
                    }
                });
            }
        });
    }
}