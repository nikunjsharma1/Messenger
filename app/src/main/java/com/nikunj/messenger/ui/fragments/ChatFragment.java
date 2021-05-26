package com.nikunj.messenger.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nikunj.messenger.R;
import com.nikunj.messenger.adapter.UserAdapter;
import com.nikunj.messenger.databinding.FragmentChatBinding;
import com.nikunj.messenger.model.User;

import java.util.ArrayList;


public class ChatFragment extends Fragment {

    public ChatFragment() {
        // Required empty public constructor
    }

   FragmentChatBinding binding;
    ArrayList<User> list=new ArrayList<>();
    FirebaseDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      binding=FragmentChatBinding.inflate(inflater, container, false);
        UserAdapter adapter=new UserAdapter(list,getContext());
        binding.chatRecyclerview.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        binding.chatRecyclerview.setLayoutManager(linearLayoutManager);

        database=FirebaseDatabase.getInstance();

        database.getReference().child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    User user=dataSnapshot.getValue(User.class);
                    assert user != null;
                    user.setUserId(dataSnapshot.getKey());
                    if (!user.getUserId().equals((FirebaseAuth.getInstance().getUid())))
                    list.add(user);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

      return binding.getRoot();
    }
}