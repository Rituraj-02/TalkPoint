package com.example.talkpoint.Fragment;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.talkpoint.Adapter.PostAdapter;
import com.example.talkpoint.Adapter.StoryAdapter;
import com.example.talkpoint.Model.Post;
import com.example.talkpoint.Model.Story;
import com.example.talkpoint.Model.UserStories;
import com.example.talkpoint.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment {

    RecyclerView storyRv,dashboardRV;
    ArrayList<Story> list;
    ArrayList<Post> postdList;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseStorage storage;
    RoundedImageView addStoryImage;
    ActivityResultLauncher<String> galleryLauncher;


    public HomeFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = new ArrayList<>();
        postdList=new ArrayList<>();


    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        storyRv = view.findViewById(R.id.storyRV);

        // Add data to the list
//        list.add(new Story(R.drawable.mountain, R.drawable.live, R.drawable.users, "Raman"));
//        list.add(new Story(R.drawable.story, R.drawable.live, R.drawable.users, "simran"));
//        list.add(new Story(R.drawable.story, R.drawable.live, R.drawable.users, "simran"));
//        list.add(new Story(R.drawable.mountain, R.drawable.live, R.drawable.users, "kisan"));
//        list.add(new Story(R.drawable.mountain, R.drawable.live, R.drawable.users, "Raman"));


        StoryAdapter adapter = new StoryAdapter(list, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        storyRv.setLayoutManager(linearLayoutManager);
        storyRv.setNestedScrollingEnabled(false);
        storyRv.setAdapter(adapter);

        //Recycler view for the post by user
        dashboardRV=view.findViewById(R.id.dashboardRv);
        postdList = new ArrayList<>();
//        dashboardList.add(new Post(R.drawable.profile,R.drawable.postimg,R.drawable.bookmark,
//                "James Sons","521","Working Hard","10","96"));
//        dashboardList.add(new Post(R.drawable.profile,R.drawable.postimg,R.drawable.bookmark,
//                "James Sons","521","Working Hard","10","96"));
//        dashboardList.add(new Post(R.drawable.profile,R.drawable.story,R.drawable.bookmark,
//                "James hook","52","Working Hard","2","1"));
//        dashboardList.add(new Post(R.drawable.profile,R.drawable.postimg,R.drawable.bookmark,
//                "James Sons","521","Working Hard","10","96"));


        PostAdapter postAdapter =new PostAdapter(postdList,getContext());
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        dashboardRV.setLayoutManager(layoutManager);
        dashboardRV.setNestedScrollingEnabled(true);
        dashboardRV.setAdapter(postAdapter);

        //for showing the data from the firebase in the home Fragment

        database.getReference().child("posts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postdList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    //get all the post from the Firebase
                    //store it in the object of Post that is post
                    Post post = dataSnapshot.getValue(Post.class);
                    post.setPostId(dataSnapshot.getKey());
                    postdList.add(post);
                }
            postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

     addStoryImage = view.findViewById(R.id.addStoryImg);
        if (addStoryImage != null) {
            addStoryImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    galleryLauncher.launch("image/*");
                }
            });
        }
//taking image from the gallery
        galleryLauncher= registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    addStoryImage.setImageURI(result);

                    final StorageReference reference = storage.getReference().child("stories")
                            .child(FirebaseAuth.getInstance().getUid())
                            .child(new Date().getTime()+"");
                    reference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                           reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                               @Override
                               public void onSuccess(Uri uri) {
                                   Story story = new Story();
                                   story.setStoryAt(new Date().getTime());

                                  database.getReference()
                                          .child("stroies")
                                          .child(FirebaseAuth.getInstance().getUid())
                                          .child("postedBy")
                                          .setValue(story.getStoryAt()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                              @Override
                                              public void onSuccess(Void unused) {
                                                  UserStories stories = new UserStories(uri.toString(),story.getStoryAt());

                                                  database.getReference()
                                                          .child("stories")
                                                          .child(FirebaseAuth.getInstance().getUid())
                                                          .child("userStories")
                                                          .push()
                                                          .setValue(stories);

                                              }
                                          });
                               }
                           }) ;
                        }
                    });
                }
            }
        });




    return view;
    }
}
