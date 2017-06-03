package com.example.android.bitmnotify.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.bitmnotify.ObjectClasses.Feed;
import com.example.android.bitmnotify.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class AddPost extends AppCompatActivity {

    private EditText etTitle;
    private EditText etContent;
    private ImageButton imgBtnAddImage;
    private ImageView imgViewAddImage;
    private Button btnCancel;
    private Button btnPost;
    private Bitmap photo;
    private Uri url;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("feeds");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add a New Post");
        setSupportActionBar(toolbar);

        etTitle = (EditText) findViewById(R.id.editText_title);
        etContent = (EditText) findViewById(R.id.editText_content);
        imgBtnAddImage = (ImageButton) findViewById(R.id.button_add_image);
        imgViewAddImage = (ImageView) findViewById(R.id.imageView_add_image);
        btnCancel = (Button) findViewById(R.id.button_cancel);
        btnPost = (Button) findViewById(R.id.button_post);

        imgBtnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(AddPost.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(AddPost.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                } else {
                    addPhoto();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String title = etTitle.getText().toString();
                final String content = etContent.getText().toString();

                if (title.equals("") || content.equals("")) {
                    Toast.makeText(AddPost.this, "Please enter title and content", Toast.LENGTH_SHORT).show();
                } else {
                    if (imgViewAddImage.getDrawable() != null) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] image = baos.toByteArray();
                        String path = "images/" + UUID.randomUUID() + ".JPEG";
                        storageReference = FirebaseStorage.getInstance().getReference(path);

                        UploadTask uploadTask = storageReference.putBytes(image);
                        uploadTask.addOnSuccessListener(AddPost.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                url = taskSnapshot.getDownloadUrl();
                                databaseReference.push().setValue(new Feed(title, content, url.toString(), ServerValue.TIMESTAMP, user.getUid(), user.getDisplayName(), user.getPhotoUrl().toString()));
                                Toast.makeText(AddPost.this, "Success", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AddPost.this, MainActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddPost.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        databaseReference.push().setValue(new Feed(title, content, "", ServerValue.TIMESTAMP, user.getUid(), user.getDisplayName(), user.getPhotoUrl().toString()));
                        startActivity(new Intent(AddPost.this, MainActivity.class));
                        finish();
                    }
                }
            }
        });

    }

    void addPhoto() {
        Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(cameraIntent, 1);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                addPhoto();
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            if (requestCode == 1 && resultCode == RESULT_OK) {

                Uri image = data.getData();
                CropImage.activity(image)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setInitialCropWindowPaddingRatio(0)
                        .setAspectRatio(16, 9)
                        .start(this);

            }

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(),resultUri);
                    imgViewAddImage.setImageURI(resultUri);
                    imgViewAddImage.setVisibility(View.VISIBLE);
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
        super.onBackPressed();
    }
}
