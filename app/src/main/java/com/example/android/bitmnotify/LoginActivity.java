package com.example.android.bitmnotify;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    Button loginButton;
    EditText email;
    EditText password;
    String _email;
    String _password;
    ProgressDialog progressDialog;
    TextView signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.login_button);
        email = (EditText) findViewById(R.id.input_email);
        password = (EditText) findViewById(R.id.input_password);
        signUp = (TextView) findViewById(R.id.signup_text_view);

        _email = email.getText().toString();
        _password = password.getText().toString();

        progressDialog = new ProgressDialog(LoginActivity.this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setMessage("Please Wait...");
                progressDialog.setTitle("Logging In");
                progressDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            parseLogin();
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    void parseLogin() {

        ParseUser.logInInBackground(_email, _password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(user != null) {

                    progressDialog.dismiss();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
                else {
                    progressDialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
                                .setTitle("Login Failed!")
                                .setMessage(e.getMessage())
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                    AlertDialog ad = builder.create();
                    ad.show();
                }
            }
        });

    }
}
