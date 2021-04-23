package com.example.bitirmeprojesi.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.bitirmeprojesi.LoginActivity;
import com.example.bitirmeprojesi.R;
import com.example.bitirmeprojesi.view.posts.PostsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        redirect();
    }

    private void redirect() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        Intent intent;
        if (currentUser != null) {
            intent = new Intent(this, PostsActivity.class);
        } else {
            intent = new Intent(this, WelcomeActivity.class);
        }
        startActivity(intent);
    }
}