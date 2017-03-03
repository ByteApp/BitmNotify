package com.example.android.bitmnotify;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.input_email);
        password = (EditText) findViewById(R.id.input_password);

        loginButton = (Button) findViewById(R.id.login_button);
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
                    Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
