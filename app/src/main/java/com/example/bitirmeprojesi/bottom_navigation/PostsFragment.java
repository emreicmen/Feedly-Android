package com.example.bitirmeprojesi.bottom_navigation;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.bitirmeprojesi.R;
import com.example.bitirmeprojesi.RegisterActivity;
import com.example.bitirmeprojesi.model.Comment;
import com.example.bitirmeprojesi.model.Post;
import com.example.bitirmeprojesi.view.CreatePostActivity;
import com.example.bitirmeprojesi.view.RecyclerItemClickListener;
import com.example.bitirmeprojesi.view.posts.PostDetails;
import com.example.bitirmeprojesi.view.posts.PostRcyclerAdapter;
import com.example.bitirmeprojesi.view.posts.PostsActivity;
import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryBounds;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PostsFragment extends Fragment {

    private SwipeRefreshLayout postsSwipeRefreshLayout;
    private RecyclerView postRecyclerView;
    private ArrayList<Post> postList = new ArrayList<>();
    private PostRcyclerAdapter postRcyclerAdapter;
    private FloatingActionButton floatingActionButton;

    private final int REQUEST_CODE_LOCATION = 6653;
    private FusedLocationProviderClient fusedLocationClient;

    private PostsFragment() {

    }

    public static PostsFragment newInstance() {
        return new PostsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.posts_fragment_layout, container, false);
        postsSwipeRefreshLayout = rootView.findViewById(R.id.postsSwipeRefreshLayout);
        postRecyclerView = rootView.findViewById(R.id.postRecyclerView);
        floatingActionButton = rootView.findViewById(R.id.createPostButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createPostIntent = new Intent(getContext(), CreatePostActivity.class);
                startActivity(createPostIntent);

            }
        });

        postsSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLocation();
            }
        });

        initRecyclerView();
        initLocationHelpers();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getLocation();
    }

    private void initRecyclerView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        postRecyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());
        postRecyclerView.addItemDecoration(dividerItemDecoration);

        postRcyclerAdapter = new PostRcyclerAdapter(getContext(), postList, new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()){
                    case R.id.like_button:

                        likePost(postList.get(position));
                        postRcyclerAdapter.notifyItemChanged(position);
                        break;

                    default:
                        Intent ıntent=new Intent(getContext(), PostDetails.class);
                        ıntent.putExtra("postId",postList.get(position).getId());
                        startActivity(ıntent);
                        break;
                }
            }
        });
        postRecyclerView.setAdapter(postRcyclerAdapter);
        postRcyclerAdapter.notifyDataSetChanged();

    }

    private void getPosts(Location location) {

        postsSwipeRefreshLayout.setRefreshing(true);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final GeoLocation center = new GeoLocation(location.getLatitude(), location.getLongitude());
        final double radiusInM = 1000;

        List<GeoQueryBounds> bounds = GeoFireUtils.getGeoHashQueryBounds(center, radiusInM);
        final List<Task<QuerySnapshot>> tasks = new ArrayList<>();
        for (GeoQueryBounds b : bounds) {
            Query q = db.collection("posts")
                    .orderBy("geohash")
                    .startAt(b.startHash)
                    .endAt(b.endHash);

            tasks.add(q.get());
        }

        // Collect all the query results together into a single list
        Tasks.whenAllComplete(tasks)
                .addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                    @Override
                    public void onComplete(@NonNull Task<List<Task<?>>> t) {
                        List<DocumentSnapshot> matchingDocs = new ArrayList<>();

                        for (Task<QuerySnapshot> task : tasks) {
                            QuerySnapshot snap = task.getResult();
                            for (DocumentSnapshot doc : snap.getDocuments()) {
                                double lat = doc.getDouble("lat");
                                double lng = doc.getDouble("lng");

                                // We have to filter out a few false positives due to GeoHash
                                // accuracy, but most will match
                                GeoLocation docLocation = new GeoLocation(lat, lng);
                                double distanceInM = GeoFireUtils.getDistanceBetween(docLocation, center);
                                if (distanceInM <= radiusInM) {
                                    matchingDocs.add(doc);
                                }
                            }
                        }

                        for (DocumentSnapshot document : matchingDocs) {
                            Post post = document.toObject(Post.class);
                            post.setId(document.getId());
                            postList.add(post);
                        }
                        Collections.sort(postList, new Comparator<Post>() {
                            @Override
                            public int compare(Post post1, Post post2) {
                                return (int)(post2.getDateTime() - post1.getDateTime());
                            }
                        });
                        postRcyclerAdapter.notifyDataSetChanged();
                        postsSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    /*
    private void getPosts(Location location) {
        postsSwipeRefreshLayout.setRefreshing(true);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts") // document("postList.getid()").collection("commentList")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        postsSwipeRefreshLayout.setRefreshing(false);
                        postList.clear();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Post post = document.toObject(Post.class);
                                post.setId(document.getId());
                                postList.add(post);
                            }
                            Collections.sort(postList, new Comparator<Post>() {
                                @Override
                                public int compare(Post post1, Post post2) {
                                    return (int)(post2.getDateTime() - post1.getDateTime());
                                }
                            });
                            postRcyclerAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
    */

    private void likePost(Post post){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        post.setLikeCount(post.getLikeCount() + 1);
        db.collection("posts").document(post.getId()).update("likeCount", post.getLikeCount());
        postRcyclerAdapter.notifyDataSetChanged();
    }

    private void commentCountPost(Post post){
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        post.setCommentCount(post.getCommentCount());
        db.collection("posts").document(post.getId()).update("commentCount",post.getCommentCount());
        postRcyclerAdapter.notifyDataSetChanged();
    }



    private void dislikePost(Post post){
        if(post.getLikeCount() == 0){
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        post.setLikeCount(post.getLikeCount() - 1);
        db.collection("posts").document(post.getId()).update("likeCount", post.getLikeCount());
        postRcyclerAdapter.notifyDataSetChanged();
    }

    //region Permission Management
    private boolean hasLocationPermission(){
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission(){
        String[] permissions = { Manifest.permission.ACCESS_FINE_LOCATION };
        ActivityCompat.requestPermissions(requireActivity(), permissions, REQUEST_CODE_LOCATION);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Toast.makeText(requireContext(), "We need location permission to get your location!", Toast.LENGTH_LONG).show();
            }
        }
    }
    //endregion

    //region Location Management
    private void initLocationHelpers(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
    }

    private void getLocation(){
        if (hasLocationPermission()){
            if (isLocationServiceEnabled()){
                getLastLocation();
            } else {
                Toast.makeText(requireContext(), "You need to enable location services to get your location!", Toast.LENGTH_LONG).show();
            }
        } else {
            requestLocationPermission();
        }
    }

    private boolean isLocationServiceEnabled(){
        LocationManager locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        fusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    getPosts(location);
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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        fusedLocationClient.requestLocationUpdates(
                mLocationRequest,
                new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        getPosts(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());
    }
    //endregion
}
