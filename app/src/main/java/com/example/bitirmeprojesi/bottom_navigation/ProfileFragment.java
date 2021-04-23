package com.example.bitirmeprojesi.bottom_navigation;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bitirmeprojesi.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    private ImageView profileImageView;
    private TextView profileTextView;

    private ProfileFragment(){}

    public static ProfileFragment newInstance(){
        return new ProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment_layout,container,false);
        profileImageView = view.findViewById(R.id.profileImageView);
        profileTextView = view.findViewById(R.id.profileTextView);
        setProfileInfo();
        showProfilePhoto();
        return view;
    }

    private void setProfileInfo(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String emailAddress = user.getEmail();
        String displayName = user.getDisplayName();
        String profileInfo = "Name: " + displayName + "\n" + "Email: " + emailAddress;
        profileTextView.setText(profileInfo);
    }

    private void showProfilePhoto(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference photoRef = storageRef.child("images/profilePhotos/12345.jpeg");

        photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                // Pass it to Picasso to download, show in ImageView and caching
                Picasso.get().load(uri.toString()).into(profileImageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }
}
