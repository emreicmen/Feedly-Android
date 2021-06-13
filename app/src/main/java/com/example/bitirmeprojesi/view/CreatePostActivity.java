package com.example.bitirmeprojesi.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bitirmeprojesi.R;
import com.example.bitirmeprojesi.RegisterActivity;
import com.example.bitirmeprojesi.model.Comment;
import com.example.bitirmeprojesi.model.Post;
import com.example.bitirmeprojesi.view.posts.PostsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreatePostActivity extends AppCompatActivity {

    CircleImageView userProfilePhoto;
    TextView fullNameTextView;
    EditText postWriteText;
    Button createButton;
    ImageView postPhotoImageView;
    private FirebaseAuth auth;
    private Uri photoUri;

    private final int REQUEST_CODE_SELECT_PICTURE=6666;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        auth = FirebaseAuth.getInstance();
        iniViews();
        CreateButton();
    }

    private void iniViews() {
        fullNameTextView = findViewById(R.id.fullNameTextView);
        userProfilePhoto = findViewById(R.id.userProfilePhoto);
        postWriteText = findViewById(R.id.postWriteText);
        postPhotoImageView = findViewById(R.id.postPhotoImageView);
        postPhotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePostPhoto();
            }
        });
    }

    private void CreateButton(){
        createButton=findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (photoUri == null){
                    savePostToDatabase(null);
                } else {
                    uploadImageToFirebase();
                }
            }
        });
    }

    private void choosePostPhoto(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), REQUEST_CODE_SELECT_PICTURE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(resultCode == RESULT_OK){
            if (requestCode== REQUEST_CODE_SELECT_PICTURE){
                photoUri=intent.getData();
                if(null!=photoUri){
                    postPhotoImageView.setImageURI(photoUri);
                }
            }
        }
    }

    private void uploadImageToFirebase(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        String fileName = UUID.randomUUID().toString() + ".jpg";
        StorageReference profileImageRef = storageRef.child("images/postImages/" + fileName);
        UploadTask uploadTask = profileImageRef.putFile(photoUri);
        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Got the download URL for 'users/me/profile.png'
                        // Pass it to Picasso to download, show in ImageView and caching
                        savePostToDatabase(uri);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
            }
        });
    }

    private void savePostToDatabase(Uri photoUri) {
        String text = postWriteText.getText().toString();
        String fullName = auth.getCurrentUser().getDisplayName();
        long currentTimeMillis = System.currentTimeMillis();
        String profilePhotoUrl = auth.getCurrentUser().getPhotoUrl().toString();
        String userId = auth.getCurrentUser().getUid();
        int likeCount = 0;
        int commentCount = 0;
        ArrayList<Comment> comments = null;
        double lat = 0.0;
        double lng = 0.0;

        Post post = new Post();
        post.setText(text);
        post.setDateTime(currentTimeMillis);
        post.setFullName(fullName);
        post.setUserProfilePhotoUrl(profilePhotoUrl);
        post.setUserId(userId);
        post.setLikeCount(likeCount);
        post.setLatitude(lat);
        post.setLongitude(lng);
        post.setPhotoUrl(photoUri.toString());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts")
                .add(post)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        onBackPressed();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreatePostActivity.this, "savePostToDatabase failed.", Toast.LENGTH_SHORT).show();
                    }
                });


    }

}