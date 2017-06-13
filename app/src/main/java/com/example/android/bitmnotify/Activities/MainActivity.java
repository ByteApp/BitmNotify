package com.example.android.bitmnotify.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.bitmnotify.Adapters.FeedAdapter;
import com.example.android.bitmnotify.ObjectClasses.Feed;
import com.example.android.bitmnotify.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FeedAdapter.ListItemClickListener {

    private DatabaseReference mDatabaseRef;
    private RecyclerView rv;
    List<Feed> list = new ArrayList<>();
    FeedAdapter adapter;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    TextView navUsername;
    TextView navEmail;
    CircleImageView navDp;
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user == null) {
                    startActivity(new Intent(MainActivity.this, SignInActivity.class));
                    finish();
                }
            }
        };

        MobileAds.initialize(this, "ca-app-pub-9064309060222668~4652247035");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("News Feed");
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

        if(mAuth.getCurrentUser() != null) {
            navUsername.setText(mAuth.getCurrentUser().getDisplayName());
            navEmail.setText(mAuth.getCurrentUser().getEmail());
            Glide.with(this).load(mAuth.getCurrentUser().getPhotoUrl()).crossFade().into(navDp);
        }

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        DatabaseReference feedRef = mDatabaseRef.child("feeds");
        feedRef.limitToLast(30).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Feed feed = dataSnapshot.getValue(Feed.class);
                list.add(new Feed(feed.getTitle(), feed.getContent(), feed.getImageUrl(), feed.getTimeStamp(), feed.getUserId(), feed.getUsername(), feed.getDpUrl()));
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);
                scrollToBottom();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        adapter = new FeedAdapter(list, this, this);
        rv = (RecyclerView) findViewById(R.id.rootView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);

        rv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Item Clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }

    void scrollToBottom() {
        rv.post(new Runnable() {
            @Override
            public void run() {
                rv.smoothScrollToPosition(adapter.getItemCount() - 1);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you really want to Exit?")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            System.exit(0);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
            //super.onBackPressed();
        }
    }

    public void signOut() {
        mDatabaseRef.child("users").child(mAuth.getCurrentUser().getUid()).removeValue();
        AuthUI.getInstance().signOut(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {

            case R.id.action_logout:
                signOut();
                return true;

            case R.id.action_app_post:
                addpost();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (id == R.id.nav_home) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_circulars) {
            Intent i = new Intent(getApplicationContext(), CircularActivity.class);
            startActivity(i);
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

        } else if (id == R.id.nav_feedback) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void addpost() {
        startActivity(new Intent(MainActivity.this, AddPost.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    public void onListItemClicked(int clikedItemIndex) {
        Intent intent = new Intent(MainActivity.this, FeedDetail.class);
        Bundle b = new Bundle();
        b.putString("title", adapter.feedList.get(clikedItemIndex).getTitle());
        b.putString("content", adapter.feedList.get(clikedItemIndex).getContent());
        b.putString("image", adapter.feedList.get(clikedItemIndex).getImageUrl());
        intent.putExtras(b);
        startActivity(intent);
    }
}
