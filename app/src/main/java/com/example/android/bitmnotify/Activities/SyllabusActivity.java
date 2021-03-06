package com.example.android.bitmnotify.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.bitmnotify.Adapters.ExpandableListViewAdapter;
import com.example.android.bitmnotify.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class SyllabusActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    TextView navUsername;
    TextView navEmail;
    CircleImageView navDp;
    FirebaseAuth mAuth;
    StorageReference mStorageReference;
    PDFView pdfView;
    ExpandableListView expandableListView;
    ExpandableListViewAdapter expandableListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabus);

        mAuth = FirebaseAuth.getInstance();
        mStorageReference = FirebaseStorage.getInstance().getReference();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Syllabus");
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

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListViewAdapter = new ExpandableListViewAdapter(this);
        expandableListView.setAdapter(expandableListViewAdapter);
        pdfView = (PDFView) findViewById(R.id.pdfView);

        navUsername.setText(mAuth.getCurrentUser().getDisplayName());
        navEmail.setText(mAuth.getCurrentUser().getEmail());
        Glide.with(this).load(mAuth.getCurrentUser().getPhotoUrl()).crossFade().into(navDp);

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                Intent intent = new Intent(SyllabusActivity.this, ProfileActivity.class);
                intent.putExtra("uId", mAuth.getCurrentUser().getUid());
                drawer.closeDrawer(GravityCompat.START);
                startActivity(intent);
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {


                final ProgressDialog pd = new ProgressDialog(SyllabusActivity.this);
                pd.setMessage("Loading...");
                pd.show();
                StorageReference storageReference = mStorageReference.child("syllabus/MeetThakkar_InternshalaResume.pdf");

                File rootpath = new File(Environment.getExternalStorageDirectory(), "syllabus");
                if(!rootpath.exists()) {
                    rootpath.mkdirs();
                }

                final File localFile = new File(rootpath, "syl.pdf");

                expandableListView.setVisibility(View.GONE);
                pdfView.setVisibility(View.VISIBLE);

                storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        pdfView.fromFile(localFile).load();
                        Toast.makeText(SyllabusActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(SyllabusActivity.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                return false;
            }
        });

        /*btnDownload = (Button) findViewById(R.id.button_download);

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StorageReference storageReference = mStorageReference.child("syllabus/MeetThakkar_InternshalaResume.pdf");

                final File sDirectory = new File(Environment.getExternalStorageDirectory(), "syllabus");
                if(!sDirectory.exists())
                    sDirectory.mkdirs();
                final File localFile = new File(sDirectory, "syllabus.pdf");
                storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(SyllabusActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(localFile), "application/pdf");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SyllabusActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });*/
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
            Intent i = new Intent(getApplicationContext(), CircularActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_syllabus) {
            drawer.closeDrawer(GravityCompat.START);
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
        } else if(expandableListView.getVisibility() == View.GONE) {
            pdfView.setVisibility(View.GONE);
            expandableListView.setVisibility(View.VISIBLE);
        }
        else {
            NavUtils.navigateUpFromSameTask(this);
        }
    }
}
