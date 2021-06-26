package com.example.bitirmeprojesi.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bitirmeprojesi.R;
import com.example.bitirmeprojesi.model.Comment;
import com.example.bitirmeprojesi.model.Post;
import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreatePostActivity extends AppCompatActivity {

    CircleImageView userProfilePhoto;
    TextView fullNameTextView;
    EditText postWriteText;
    Button createButton;
    ImageView postPhotoImageView;
    Button choseImageButton;
    private FirebaseAuth auth;
    private Uri photoUri;
    private Location location;

    private final int REQUEST_CODE_SELECT_PICTURE = 6666;
    private final int REQUEST_CODE_LOCATION = 6653;

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        auth = FirebaseAuth.getInstance();
        iniViews();
        CreateButton();
        initLocationHelpers();
        getLocation();
    }

    private void iniViews() {
        fullNameTextView = findViewById(R.id.fullNameTextView);
        userProfilePhoto = findViewById(R.id.userProfilePhoto);
        postWriteText = findViewById(R.id.postWriteText);
        postPhotoImageView = findViewById(R.id.postPhotoImageView);
        choseImageButton=findViewById(R.id.choseImageButton);
        choseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePostPhoto();
            }
        });
    }

    private void CreateButton() {
        createButton = findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (location == null){
                    Toast.makeText(CreatePostActivity.this, "We need your location to create a post!", Toast.LENGTH_LONG).show();
                    onBackPressed();
                    return;
                }
                if (photoUri == null) {
                    savePostToDatabase(null);
                } else {
                    uploadImageToFirebase();
                }
            }
        });
    }

    private void choosePostPhoto() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), REQUEST_CODE_SELECT_PICTURE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_SELECT_PICTURE) {
                photoUri = intent.getData();
                if (null != photoUri) {
                    postPhotoImageView.setImageURI(photoUri);
                    postPhotoImageView.setVisibility(postPhotoImageView.VISIBLE);
                }
            }
        }
    }

    private void uploadImageToFirebase() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        String fileName = UUID.randomUUID().toString() + ".jpg";
        StorageReference profileImageRef = storageRef.child("images/postImages/" + fileName);
        UploadTask uploadTask = profileImageRef.putFile(photoUri);
        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Got the download URL for 'users/me/profile.png'
                        // Pass it to Picasso to download, show in ImageView and caching
                        savePostToDatabase(uri);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
            }
        });
    }

    private void savePostToDatabase(Uri photoUri) {

        double lat = location.getLatitude();
        double lng = location.getLongitude();
        String geoHash = GeoFireUtils.getGeoHashForLocation(new GeoLocation(lat, lng));

        String text = postWriteText.getText().toString();
        String fullName = auth.getCurrentUser().getDisplayName();
        long currentTimeMillis = System.currentTimeMillis();
        String profilePhotoUrl = auth.getCurrentUser().getPhotoUrl().toString();
        String userId = auth.getCurrentUser().getUid();
        int likeCount = 0;
        int commentCount = 0;
        ArrayList<Comment> comments = null;

        Post post = new Post();

        post.setLat(lat);
        post.setLng(lng);
        post.setGeohash(geoHash);

        post.setText(text);
        post.setDateTime(currentTimeMillis);
        post.setFullName(fullName);
        post.setUserProfilePhotoUrl(profilePhotoUrl);
        post.setUserId(userId);
        post.setLikeCount(likeCount);
        post.setPhotoUrl(photoUri.toString());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts")
                .add(post)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        onBackPressed();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreatePostActivity.this, "savePostToDatabase failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //region Permission Management
    private boolean hasLocationPermission(){
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission(){
        String[] permissions = { Manifest.permission.ACCESS_FINE_LOCATION };
        ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_LOCATION);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Toast.makeText(this, "We need location permission to get your location!", Toast.LENGTH_LONG).show();
                onBackPressed();
            }
        }
    }
    //endregion

    //region Location Management
    private void initLocationHelpers(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void getLocation(){
        if (hasLocationPermission()){
            if (isLocationServiceEnabled()){
                getLastLocation();
            } else {
                Toast.makeText(this, "You need to enable location services to get your location!", Toast.LENGTH_LONG).show();
                onBackPressed();
            }
        } else {
            requestLocationPermission();
        }
    }

    private boolean isLocationServiceEnabled(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        fusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    CreatePostActivity.this.location = location;
                } else {
                    requestNewLocationData();
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.requestLocationUpdates(
                mLocationRequest,
                new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        location = locationResult.getLastLocation();
                    }
                },
                Looper.myLooper());
    }
    //endregion

}