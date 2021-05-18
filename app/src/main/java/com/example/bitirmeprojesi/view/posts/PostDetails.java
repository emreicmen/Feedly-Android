package com.example.bitirmeprojesi.view.posts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.bitirmeprojesi.R;
import com.example.bitirmeprojesi.model.Comment;
import com.example.bitirmeprojesi.model.Post;
import com.example.bitirmeprojesi.view.RecyclerItemClickListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PostDetails extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CommentsRecyclerAdapter commentsRecyclerAdapter;
    private ArrayList<Comment> commentList=new ArrayList<>();
    private FloatingActionButton floatingActionButton;
    private String postId;
    private SwipeRefreshLayout commentsSwipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        commentsSwipeRefreshLayout=findViewById(R.id.commentsSwipeRefreshLayout);
        floatingActionButton=findViewById(R.id.createCommentButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PostDetails.this,CreateCommentActivity.class);
                intent.putExtra("postId", postId);
                startActivity(intent);
            }
        });

        commentsSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getComments();
            }
        });


        postId = getIntent().getStringExtra("postId");
        initRecyclerView();
        //getNames();
    }



    @Override
    public void onResume() {
        super.onResume();
        getComments();
    }

    private void initRecyclerView(){
        recyclerView = findViewById(R.id.commentRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        commentsRecyclerAdapter = new CommentsRecyclerAdapter(this, commentList, new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(PostDetails.this, commentList.get(position).getFullName() + " Clicked!" , Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(commentsRecyclerAdapter);
        commentsRecyclerAdapter.notifyDataSetChanged();
    }


    private void getComments() {
        commentsSwipeRefreshLayout.setRefreshing(true);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").document(postId).collection("commentList")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        commentsSwipeRefreshLayout.setRefreshing(false);
                        commentList.clear();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Comment comment = document.toObject(Comment.class);
                                commentList.add(comment);
                            }
                            Collections.sort(commentList, new Comparator<Comment>() {
                                @Override
                                public int compare(Comment comment1, Comment comment2) {
                                    return (int)(comment2.getDateTime() - comment1.getDateTime());
                                }
                            });
                            commentsRecyclerAdapter.notifyDataSetChanged();
                        }
                        else{
                            Toast.makeText(PostDetails.this, "getComments failed.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }




    private void getNames(){
        for (int i = 1; i < 31; i++){
            Comment comment = new Comment(
                    "Yorumlar Başarılı bir şekilde geldi",
                    "Name " + i,
                    null,
                    "18"+i,
                    10L

            );
            commentList.add(comment);
        }
    }



}