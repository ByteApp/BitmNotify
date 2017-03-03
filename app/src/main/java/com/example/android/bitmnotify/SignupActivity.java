package com.example.android.bitmnotify;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
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

    Button btnSignUp;

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

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser user = new ParseUser();
                user.setUsername(etEmail.getText().toString());
                user.setEmail(etEmail.getText().toString());
                user.setPassword(etPassword.getText().toString());
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null) {

                            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        }
                        else {

                            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}
