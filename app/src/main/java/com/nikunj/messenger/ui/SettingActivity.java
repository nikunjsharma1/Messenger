package com.nikunj.messenger.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nikunj.messenger.R;
import com.nikunj.messenger.databinding.ActivitySettingBinding;
import com.nikunj.messenger.databinding.ActivitySignUpBinding;
import com.nikunj.messenger.model.User;
import com.nikunj.messenger.utils.Extn;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.HashMap;

public class SettingActivity extends AppCompatActivity {
    ActivitySettingBinding binding;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    int REQUEST_CODE_=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        database.getReference().child("User").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user =snapshot.getValue(User.class);
                Picasso.get().load(user.getProfilePic()).placeholder(R.drawable.profile).into(binding.AddProfile);
                if (user.getProfilePic()==null){
                }
                binding.aboutEdit.setText(user.getAbout());
                binding.userNameEdit.setText(user.getUserName());

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });

        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=binding.userNameEdit.getText().toString();
                String about=binding.aboutEdit.getText().toString();

                HashMap<String,Object> obj=new HashMap<>();
                obj.put("userName",username);
                obj.put("about",about);
                database.getReference().child("User").child(mAuth.getUid()).updateChildren(obj);
                Extn.makeToast(SettingActivity.this,"Profile Updated");

            }
        });
        binding.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choseImage();

            }
        });
    }

    private void choseImage() {
        Intent intent=new Intent();
        intent.setAction(intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,REQUEST_CODE_);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data.getData()!=null){
            Uri uri=data.getData();
            binding.AddProfile.setImageURI(uri);
            final StorageReference storageReference=storage.getReference().child("profilePic").child(mAuth.getUid());
            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                 storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                     @Override
                     public void onSuccess(Uri uri) {
                    database.getReference().child("User").child(mAuth.getUid()).child("profilePic").setValue(uri.toString());
                         Extn.makeToast(SettingActivity.this,"Profile Uploaded");
                     }
                 });
                }
            });
        }
    }
}