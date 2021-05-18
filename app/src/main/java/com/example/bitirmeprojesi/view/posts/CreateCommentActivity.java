package com.example.bitirmeprojesi.view.posts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bitirmeprojesi.R;
import com.example.bitirmeprojesi.model.Comment;
import com.example.bitirmeprojesi.view.CreatePostActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateCommentActivity extends AppCompatActivity {

    CircleImageView userProfilePhoto;
    EditText commentWriteText;
    TextView fullNameTextView;
    Button createCommentButton;
    private FirebaseAuth auth;
    private  String postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_comment);
        auth=FirebaseAuth.getInstance();
        initViews();
        //saveCommentDatabase();
        createCommentButton();

        postId=getIntent().getStringExtra("postId");

    }

    private void initViews(){
        commentWriteText=findViewById(R.id.commentWriteText);
        userProfilePhoto=findViewById(R.id.userProfilePhoto);
        fullNameTextView=findViewById(R.id.fullNameTextView);
    }

    private void createCommentButton(){
        createCommentButton=findViewById(R.id.createCommentButton);
        createCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCommentDatabase();
            }
        });
    }

    private void saveCommentDatabase(){
        String text=commentWriteText.getText().toString();
        String fullname=auth.getCurrentUser().getDisplayName();
        String profilePhotoUrl=auth.getCurrentUser().getPhotoUrl().toString();
        String userId=auth.getCurrentUser().getUid();
        long currentTimeMilis =System.currentTimeMillis();

        Comment comment=new Comment();
        comment.setText(text);
        comment.setFullName(fullname);
        comment.setUserProfilePhotoUri(profilePhotoUrl);
        comment.setUserId(userId);
        comment.setDateTime(currentTimeMilis);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").document(postId).collection("commentList")
                .add(comment)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        Log.i("ygyfrf", "vjfujvf");
                        onBackPressed();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateCommentActivity.this, "savePostToDatabase failed.", Toast.LENGTH_SHORT).show();
                    }
                });


    }
}