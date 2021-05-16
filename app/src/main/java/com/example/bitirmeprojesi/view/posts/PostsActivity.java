package com.example.bitirmeprojesi.view.posts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.bitirmeprojesi.R;
import com.example.bitirmeprojesi.bottom_navigation.PostsFragment;
import com.example.bitirmeprojesi.bottom_navigation.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PostsActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        initViews();
        showFragments(PostsFragment.newInstance());
    }

    private void initViews(){
        bottomNavigationView=findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_item_post:
                        showFragments(PostsFragment.newInstance());
                        return true;
                    case R.id.menu_item_profile:
                        showFragments(ProfileFragment.newInstance());
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void showFragments(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.bottomNavigationFragmentContainer,fragment);
        fragmentTransaction.commit();
    }

}