package com.example.android.bitmnotify;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Circulars");
        setSupportActionBar(toolbar);

        Button logoutButton = (Button) findViewById(R.id.logout_button);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null) {

                            Toast.makeText(ProfileActivity.this, "Successfull", Toast.LENGTH_SHORT).show();
                        }
                        else {

                            Toast.makeText(ProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}
