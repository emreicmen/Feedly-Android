package com.example.bitirmeprojesi.view.posts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bitirmeprojesi.R;
import com.example.bitirmeprojesi.model.Comment;
import com.example.bitirmeprojesi.view.RecyclerItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommentsRecyclerAdapter extends RecyclerView.Adapter<CommentsRecyclerAdapter.CommentViewHolder> {

    private Context context;
    private ArrayList<Comment> commentList;
    private RecyclerItemClickListener recyclerItemClickListener;

    public static class CommentViewHolder extends RecyclerView.ViewHolder{

        ImageView userProfilePhoto;
        TextView postWriteText;
        TextView fullNameTextView;

        public CommentViewHolder(final View view, RecyclerItemClickListener recyclerItemClickListener) {
            super(view);
            userProfilePhoto = view.findViewById(R.id.userProfilePhoto);
            fullNameTextView = view.findViewById(R.id.fullNameTextView);
            postWriteText = view.findViewById(R.id.postWriteText);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerItemClickListener != null){
                        recyclerItemClickListener.onItemClick(v, getAdapterPosition());
                    }
                }
            });
        }
    }

    public CommentsRecyclerAdapter(Context context,
                                ArrayList<Comment> commentList,
                                RecyclerItemClickListener recyclerItemClickListener){
        this.context = context;
        this.commentList = commentList;
        this.recyclerItemClickListener = recyclerItemClickListener;
    }

    @Override
    public CommentsRecyclerAdapter.CommentViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.row_comments_list, viewGroup, false);
        CommentViewHolder viewHolder = new CommentsRecyclerAdapter.CommentViewHolder(view, recyclerItemClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CommentsRecyclerAdapter.CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        Picasso.get().load(comment.getUserProfilePhotoUri()).into(holder.userProfilePhoto);
        holder.fullNameTextView.setText(comment.getFullName());
        holder.postWriteText.setText(comment.getText());

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

}

