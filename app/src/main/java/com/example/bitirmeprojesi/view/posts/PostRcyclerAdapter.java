package com.example.bitirmeprojesi.view.posts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bitirmeprojesi.R;
import com.example.bitirmeprojesi.model.Post;
import com.example.bitirmeprojesi.view.RecyclerItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostRcyclerAdapter extends RecyclerView.Adapter<PostRcyclerAdapter.PostViewHolder> {

    private Context context;
    private ArrayList<Post> postList;
    private RecyclerItemClickListener recyclerItemClickListener;


    public static class PostViewHolder extends RecyclerView.ViewHolder {

        ImageView userProfilePhoto;
        TextView fullNameTextView;
        TextView dateTimeTextView;
        TextView postWriteText;
        ImageView postPhotoImageView;




        public PostViewHolder(final View view, RecyclerItemClickListener recyclerItemClickListener) {
            super(view);
            userProfilePhoto = view.findViewById(R.id.userProfilePhoto);
            fullNameTextView = view.findViewById(R.id.fullNameTextView);
            dateTimeTextView = view.findViewById(R.id.dateTimeTextView);
            postWriteText = view.findViewById(R.id.postWriteText);
            postPhotoImageView = view.findViewById(R.id.postPhotoImageView);
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
                               ArrayList<Post> postList,
                               RecyclerItemClickListener recyclerItemClickListener) {
        this.context = context;
        this.postList= postList;
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
        Post post = postList.get(position);
        if (post.getPhotoUrl() != null){
            holder.postPhotoImageView.setVisibility(View.VISIBLE);
            Picasso.get().load(post.getPhotoUrl()).into(holder.postPhotoImageView);
        } else {
            holder.postPhotoImageView.setVisibility(View.GONE);
        }

        holder.fullNameTextView.setText(post.getFullName());
        holder.postWriteText.setText(post.getText());

    }


    @Override
    public int getItemCount() {
        return postList.size();
    }
}
