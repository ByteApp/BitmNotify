package com.example.android.bitmnotify.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.bitmnotify.ObjectClasses.User;
import com.example.android.bitmnotify.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    CircleImageView dpProfile;
    TextView tvName;
    TextView tvPosts;
    TextView tvFollowers;
    DatabaseReference mDatabaseReference;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.show();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        dpProfile = (CircleImageView) findViewById(R.id.dp_profile);
        tvName = (TextView) findViewById(R.id.textView_name_profile);
        tvPosts = (TextView) findViewById(R.id.textView_posts_profile);
        tvFollowers = (TextView) findViewById(R.id.textView_followers_profile);

        Intent intent = getIntent();
        String uId = intent.getStringExtra("uId");

        DatabaseReference user = mDatabaseReference.child(uId).child(uId);
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                tvName.setText(u.getName());
                tvPosts.setText(u.getName());
                tvFollowers.setText(u.getName());
                Glide.with(ProfileActivity.this).load(u.getDpUrl()).into(dpProfile);
                pd.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
