package com.example.android.bitmnotify.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.bitmnotify.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText etFullName;
    EditText etEmail;
    EditText etPassword;
    EditText etConfirmPassword;
    Button btnRegister;
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child("users");

        etFullName = (EditText) findViewById(R.id.editText_fullname);
        etEmail = (EditText) findViewById(R.id.editText_email_register);
        etPassword = (EditText) findViewById(R.id.editText_password_register);
        etConfirmPassword = (EditText) findViewById(R.id.editText_confirm_password);
        btnRegister = (Button) findViewById(R.id.button_register_register);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etFullName.getText().toString().equals("") || etEmail.getText().toString().equals("") ||
                        etPassword.getText().toString().equals("") || etConfirmPassword.getText().toString().equals("")) {
                    Toast.makeText(RegisterActivity.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (!(etPassword.getText().toString().equals(etConfirmPassword.getText().toString()))) {
                        Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                        etPassword.setText("");
                        etConfirmPassword.setText("");
                    } else {
                        mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(!task.isSuccessful()) {
                                            Toast.makeText(RegisterActivity.this, "Registeration Failed", Toast.LENGTH_SHORT).show();
                                        } else {
                                            mAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                                                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                                            if(!task.isSuccessful()) {
                                                                Toast.makeText(RegisterActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                                            }
                                                            else {
                                                                FirebaseUser user = mAuth.getCurrentUser();
                                                                mRef.push().child("uid").setValue(user.getUid());
                                                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                startActivity(intent);
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });
                    }
                }
            }
        });
    }
}
