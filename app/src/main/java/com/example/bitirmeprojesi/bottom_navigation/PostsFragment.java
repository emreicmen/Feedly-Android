package com.example.bitirmeprojesi.bottom_navigation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.bitirmeprojesi.R;
import com.example.bitirmeprojesi.RegisterActivity;
import com.example.bitirmeprojesi.model.Post;
import com.example.bitirmeprojesi.view.CreatePostActivity;
import com.example.bitirmeprojesi.view.RecyclerItemClickListener;
import com.example.bitirmeprojesi.view.posts.PostRcyclerAdapter;
import com.example.bitirmeprojesi.view.posts.PostsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PostsFragment extends Fragment {

    private SwipeRefreshLayout postsSwipeRefreshLayout;
    private RecyclerView postRecyclerView;
    private ArrayList<Post> postList = new ArrayList<>();
    private PostRcyclerAdapter postRcyclerAdapter;
    private FloatingActionButton floatingActionButton;

    private PostsFragment() {

    }

    public static PostsFragment newInstance() {
        return new PostsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.posts_fragment_layout, container, false);
        postsSwipeRefreshLayout = rootView.findViewById(R.id.postsSwipeRefreshLayout);
        postRecyclerView = rootView.findViewById(R.id.postRecyclerView);
        floatingActionButton = rootView.findViewById(R.id.createPostButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createPostIntent = new Intent(getContext(), CreatePostActivity.class);
                startActivity(createPostIntent);
            }
        });

        postsSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPosts();
            }
        });

        initRecyclerView();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getPosts();
    }

    private void initRecyclerView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        postRecyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());
        postRecyclerView.addItemDecoration(dividerItemDecoration);

        postRcyclerAdapter = new PostRcyclerAdapter(getContext(), postList, new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()){
                    case R.id.like_button:
                        likePost(postList.get(position));
                        break;
                    default:
                        break;
                }
            }
        });
        postRecyclerView.setAdapter(postRcyclerAdapter);
        postRcyclerAdapter.notifyDataSetChanged();

    }

    private void getPosts() {
        postsSwipeRefreshLayout.setRefreshing(true);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts") // document("postList.getid()").collection("commentList")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        postsSwipeRefreshLayout.setRefreshing(false);
                        postList.clear();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Post post = document.toObject(Post.class);
                                post.setId(document.getId());
                                postList.add(post);
                            }
                            Collections.sort(postList, new Comparator<Post>() {
                                @Override
                                public int compare(Post post1, Post post2) {
                                    return (int)(post2.getDateTime() - post1.getDateTime());
                                }
                            });
                            postRcyclerAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void likePost(Post post){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        post.setLikeCount(post.getLikeCount() + 1);
        db.collection("posts").document(post.getId()).update("likeCount", post.getLikeCount());
        postRcyclerAdapter.notifyDataSetChanged();
    }

    private void dislikePost(Post post){
        if(post.getLikeCount() == 0){
            return;
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        post.setLikeCount(post.getLikeCount() - 1);
        db.collection("posts").document(post.getId()).update("likeCount", post.getLikeCount());
        postRcyclerAdapter.notifyDataSetChanged();
    }
}
