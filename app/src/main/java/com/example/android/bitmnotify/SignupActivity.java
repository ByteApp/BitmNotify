package com.example.android.bitmnotify;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {

    TextInputLayout tilFirstName;
    TextInputLayout tilLastName;
    TextInputLayout tilUsn;
    TextInputLayout tilEmail;
    TextInputLayout tilPassword;
    TextInputLayout tilConfirmPassword;

    EditText etFirstName;
    EditText etLastName;
    EditText etUsn;
    EditText etEmail;
    EditText etPassword;
    EditText etConfirmPassword;

    String username;
    String email;
    String password;

    Button btnSignUp;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        tilFirstName = (TextInputLayout) findViewById(R.id.til_first_name);
        tilLastName = (TextInputLayout) findViewById(R.id.til_last_name);
        tilUsn = (TextInputLayout) findViewById(R.id.til_usn);
        tilEmail = (TextInputLayout) findViewById(R.id.til_email);
        tilPassword = (TextInputLayout) findViewById(R.id.til_password);
        tilConfirmPassword = (TextInputLayout) findViewById(R.id.til_confirm_password);

        etFirstName = (EditText) findViewById(R.id.edittext_first_name);
        etLastName = (EditText) findViewById(R.id.edittext_last_name);
        etEmail = (EditText) findViewById(R.id.edittext_email);
        etUsn = (EditText) findViewById(R.id.edittext_Usn);
        etPassword = (EditText) findViewById(R.id.edittext_password);
        etConfirmPassword = (EditText) findViewById(R.id.edittext_confirm_password);

        btnSignUp = (Button) findViewById(R.id.button_sign_up);

        progressDialog = new ProgressDialog(SignupActivity.this);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = etEmail.getText().toString();
                username = before(email, "@");
                password = etPassword.getText().toString();
                progressDialog.setMessage("Please Wait...");
                progressDialog.setTitle("Creating New Account");
                progressDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            parseSignUp();
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });

    }

    void parseSignUp(){

        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {

                    progressDialog.dismiss();
                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
                else {
                    progressDialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this)
                            .setTitle("SignUp Failed")
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

    String before(String value, String a) {

        int posA = value.indexOf(a);
        if (posA == -1) {

            return null;
        }

        return value.substring(0, posA);
    }
}
