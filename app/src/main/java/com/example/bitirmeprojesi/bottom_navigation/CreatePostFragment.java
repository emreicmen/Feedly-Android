package com.example.bitirmeprojesi.bottom_navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bitirmeprojesi.R;

public class CreatePostFragment extends Fragment {

    private CreatePostFragment(){

    }

    public  static CreatePostFragment newInstance(){
        return new CreatePostFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.create_posts_fragment,container,false);
    }
}
