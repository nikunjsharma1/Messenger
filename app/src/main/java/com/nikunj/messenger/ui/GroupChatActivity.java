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
import com.nikunj.messenger.databinding.ActivityGroupChatBinding;
import com.nikunj.messenger.model.MessageModal;

import java.util.ArrayList;
import java.util.Date;

public class GroupChatActivity extends AppCompatActivity {

    ActivityGroupChatBinding binding;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        binding.backImageViewGroupChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupChatActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        final String senderId = auth.getUid();
        binding.userNameDetailGroupChat.setText("Friends Group");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();


        final ArrayList<MessageModal> messageModals = new ArrayList<>();
        final ChatAdapter adapter = new ChatAdapter(messageModals, this);
        binding.GroupChatDetailRecyclerview.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.GroupChatDetailRecyclerview.setLayoutManager(linearLayoutManager);


        database.getReference().child("Group chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageModals.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    MessageModal modal = snapshot1.getValue(MessageModal.class);
            messageModals.add(modal);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( binding.typeAMessageGroupChat.getText().toString().isEmpty()){
                    binding.typeAMessageGroupChat.setError("type something");
                    return;
                }
                String message = binding.typeAMessageGroupChat.getText().toString();
                final MessageModal modal = new MessageModal(senderId, message);
                modal.setTimeStamp(new Date().getTime());
                binding.typeAMessageGroupChat.setText("");
                database.getReference().child("Group chats").push().setValue(modal).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });
            }
        });
    }
}