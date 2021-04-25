package com.example.bitirmeprojesi.view.posts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bitirmeprojesi.R;
import com.example.bitirmeprojesi.model.User;
import com.example.bitirmeprojesi.view.RecyclerItemClickListener;

import java.util.ArrayList;

public class PostRcyclerAdapter extends RecyclerView.Adapter<PostRcyclerAdapter.PostViewHolder> {

    private Context context;
    private ArrayList<User> userList;
    private RecyclerItemClickListener recyclerItemClickListener;


    public static class PostViewHolder extends RecyclerView.ViewHolder {

        TextView numberTextView;
        TextView fullNameTextView;

        public PostViewHolder(final View view, RecyclerItemClickListener recyclerItemClickListener) {
            super(view);
            numberTextView = view.findViewById(R.id.numberTextView);
            fullNameTextView = view.findViewById(R.id.fullNameTextView);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerItemClickListener != null) {
                        recyclerItemClickListener.onItemClick(v, getAdapterPosition());
                    }
                }
            });
        }
    }


    public PostRcyclerAdapter(Context context,
                               ArrayList<User> userList,
                               RecyclerItemClickListener recyclerItemClickListener) {
        this.context = context;
        this.userList = userList;
        this.recyclerItemClickListener = recyclerItemClickListener;
    }


    @Override
    public PostRcyclerAdapter.PostViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.row_post_list, viewGroup, false);
        PostViewHolder viewHolder = new PostRcyclerAdapter.PostViewHolder(view,recyclerItemClickListener);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(PostRcyclerAdapter.PostViewHolder holder, int position) {
        User user = userList.get(position);
        holder.numberTextView.setText(String.valueOf(user.getId()));
        holder.fullNameTextView.setText(user.getFullName());

    }


    @Override
    public int getItemCount() {
        return userList.size();
    }
}
