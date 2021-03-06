package com.example.android.bitmnotify.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.bitmnotify.R;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class CircularActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    TextView navUsername;
    TextView navEmail;
    CircleImageView navDp;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circular);

        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Circulars");
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        navUsername = (TextView) header.findViewById(R.id.nav_username);
        navEmail = (TextView) header.findViewById(R.id.nav_email);
        navDp = (CircleImageView) header.findViewById(R.id.nav_dp);

        navUsername.setText(mAuth.getCurrentUser().getDisplayName());
        navEmail.setText(mAuth.getCurrentUser().getEmail());
        Glide.with(this).load(mAuth.getCurrentUser().getPhotoUrl()).crossFade().into(navDp);

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                Intent intent = new Intent(CircularActivity.this, ProfileActivity.class);
                intent.putExtra("uId", mAuth.getCurrentUser().getUid());
                drawer.closeDrawer(GravityCompat.START);
                startActivity(intent);
            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (id == R.id.nav_home) {
            NavUtils.navigateUpFromSameTask(this);
        } else if (id == R.id.nav_circulars) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_syllabus) {
            Intent i = new Intent(getApplicationContext(), SyllabusActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_resources) {
            Intent i = new Intent(getApplicationContext(), ResourcesActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_results) {
            Intent i = new Intent(getApplicationContext(), ResultActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Hey check out my app at: https://play.google.com/store/apps/details?id=com.google.android.apps.plus");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);

        } else if (id == R.id.nav_feedback) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            NavUtils.navigateUpFromSameTask(this);
        }
    }
}
