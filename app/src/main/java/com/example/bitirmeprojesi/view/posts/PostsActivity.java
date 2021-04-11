package com.example.bitirmeprojesi.view.posts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.bitirmeprojesi.R;
import com.example.bitirmeprojesi.model.User;
import com.example.bitirmeprojesi.view.RecyclerItemClickListener;

import java.util.ArrayList;

public class PostsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<User> userList = new ArrayList<>();
    private PostRcyclerAdapter postRcyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        initRecyclerView();
        getNames();
    }
    private void initRecyclerView(){
        recyclerView = findViewById(R.id.postRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        postRcyclerAdapter = new PostRcyclerAdapter(this, userList, new RecyclerItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(PostsActivity.this, userList.get(position).getName() + " Clicked!", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(postRcyclerAdapter);
        postRcyclerAdapter.notifyDataSetChanged();

    }
    private void getNames(){
        for (int i = 1; i < 31; i++){
            User student = new User(
                    "Enes" + i,
                    "İçmen" + i,
                    18
            );
            userList.add(student);
        }
    }
}