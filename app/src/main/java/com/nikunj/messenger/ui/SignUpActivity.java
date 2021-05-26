package com.nikunj.messenger.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.nikunj.messenger.databinding.ActivitySignUpBinding;
import com.nikunj.messenger.model.User;
import com.nikunj.messenger.utils.Extn;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We're creating your Account");

        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.signupEmail.getText().toString().isEmpty()){
                    binding.signupEmail.setError("Email Required");
                    return;
                }
                if (binding.signupName.getText().toString().isEmpty()){
                    binding.signupName.setError("Name Required");
                    return;
                }
                if ( binding.signupPassword.getText().toString().isEmpty()){
                    binding.signupPassword.setError("Password Required");
                    return;
                }
                 progressDialog.show();
                mAuth.createUserWithEmailAndPassword(binding.signupEmail.getText().toString(),
                        binding.signupPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull  Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()){
                            User user =new User(binding.signupName.getText().toString(),binding.signupEmail.getText().toString(),
                                    binding.signupPassword.getText().toString());

                            String id = task.getResult().getUser().getUid();
                            database.getReference().child("User").child(id).setValue(user);

                            Extn.makeToast(SignUpActivity.this,"User Created Successful");

                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(intent);
                        }else {
                            Extn.makeToast(SignUpActivity.this, Objects.requireNonNull(task.getException()).toString());

                        }
                    }
                });
            }
        });
        binding.LogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}