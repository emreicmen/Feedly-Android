package com.example.bitirmeprojesi.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreatePostActivity extends AppCompatActivity {

    CircleImageView userProfilePhoto;
    TextView fullNameTextView;
    EditText postWriteText;
    Button createButton;
    private FirebaseAuth auth;

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
    }

    private void CreateButton(){
        createButton=findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePostToDatabase();
            }
        });
    }

    private void savePostToDatabase() {
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