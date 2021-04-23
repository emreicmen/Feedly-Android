package com.example.bitirmeprojesi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bitirmeprojesi.view.posts.PostsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText email;
    private EditText password;
    private Button registerButtonLogin;
    private Button signInButtonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth=FirebaseAuth.getInstance();
        initViews();


        Button registerButtonLogin = findViewById(R.id.registerButtonLogin);
        registerButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRegisterActivity();
            }
        });
    }

    private void goToRegisterActivity(){
        Intent ıntent = new Intent(this,RegisterActivity.class);
        startActivity(ıntent);
    }

    private void initViews(){
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        signInButtonLogin=findViewById(R.id.signInButtonLogin);

        signInButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignInExistingUsers(email.getText().toString(),password.getText().toString());
            }
        });

    }

    private  void SignInExistingUsers(String email,String password){
        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user =auth.getCurrentUser();
                            startActivity(new Intent(LoginActivity.this,PostsActivity.class));
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Username or password is incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
