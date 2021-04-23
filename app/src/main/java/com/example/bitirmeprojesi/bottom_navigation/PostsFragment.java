package com.example.bitirmeprojesi.bottom_navigation;

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
import com.example.bitirmeprojesi.model.User;
import com.example.bitirmeprojesi.view.RecyclerItemClickListener;
import com.example.bitirmeprojesi.view.posts.PostRcyclerAdapter;

import java.util.ArrayList;

public class PostsFragment extends Fragment {

    private RecyclerView postRecyclerView;
    private ArrayList<User> userList = new ArrayList<>();
    private PostRcyclerAdapter postRcyclerAdapter;

    private PostsFragment(){

    }

    public  static PostsFragment newInstance(){
        return new PostsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.posts_fragment_layout,container,false);
        postRecyclerView = rootView.findViewById(R.id.postRecyclerView);
        getNames();
        initRecyclerView();
        return rootView;
    }

    private void getNames(){
        for (int i = 1; i < 30; i++){
            User student = new User(
                    "Enes" + i,
                    "İçmen" + i,
                    i*10

            );
            userList.add(student);
        }
    }

    private void initRecyclerView(){

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        postRecyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());
        postRecyclerView.addItemDecoration(dividerItemDecoration);

        postRcyclerAdapter = new PostRcyclerAdapter(getContext(), userList, new RecyclerItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getContext(), userList.get(position).getName() + " Clicked!", Toast.LENGTH_SHORT).show();
            }
        });
        postRecyclerView.setAdapter(postRcyclerAdapter);
        postRcyclerAdapter.notifyDataSetChanged();

    }
}
