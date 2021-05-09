package com.example.bitirmeprojesi.bottom_navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bitirmeprojesi.R;
import com.example.bitirmeprojesi.RegisterActivity;
import com.example.bitirmeprojesi.model.Post;
import com.example.bitirmeprojesi.view.RecyclerItemClickListener;
import com.example.bitirmeprojesi.view.posts.PostRcyclerAdapter;
import com.example.bitirmeprojesi.view.posts.PostsActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class PostsFragment extends Fragment {

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
        postRecyclerView = rootView.findViewById(R.id.postRecyclerView);
        floatingActionButton = rootView.findViewById(R.id.createPostButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToCreatePostIntent = new Intent(getActivity(), CreatePostFragment.class);
                startActivity(goToCreatePostIntent);
            }
        });

        getNames();
        initRecyclerView();
        createPost();
        return rootView;
    }

    private void getNames() {
        for (int i = 1; i < 30; i++) {
            Post post = new Post();
            post.setFullName("Enes İçmen" + i * 10);
            post.setText("Make humanity a multiplanet species!");
            post.setPhotoUrl("https://fujifilm-x.com/wp-content/uploads/2019/08/x-t3_sample-images02.jpg");
            postList.add(post);
        }
    }


    private void initRecyclerView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        postRecyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());
        postRecyclerView.addItemDecoration(dividerItemDecoration);

        postRcyclerAdapter = new PostRcyclerAdapter(getContext(), postList, new RecyclerItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
            }
        });
        postRecyclerView.setAdapter(postRcyclerAdapter);
        postRcyclerAdapter.notifyDataSetChanged();

    }

    private void createPost(){
        Post post = new Post();
        post.setText("Hello world!");
        post.setFullName("Mehmet Yılmaz");
        post.setUserProfilePhotoUrl("https://fujifilm-x.com/wp-content/uploads/2019/08/x-t3_sample-images02.jpg");
        post.setUserId(1234);
        post.setDateTime(System.currentTimeMillis());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts")
                .add(post)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getActivity(), "Post successfully created", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Create post failed.", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
