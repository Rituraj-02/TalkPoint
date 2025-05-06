package com.example.talkpoint;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.talkpoint.Fragment.AddPostFragment;
import com.example.talkpoint.Fragment.HomeFragment;
import com.example.talkpoint.Fragment.NotificationFragment;
import com.example.talkpoint.Fragment.ProfileFragment;
import com.example.talkpoint.Fragment.SearchFragment;
import com.example.talkpoint.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

  ActivityMainBinding binding;
  FirebaseAuth auth = FirebaseAuth.getInstance();


    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);  // Set toolbar
        MainActivity.this.setTitle("My profile");

        replaceFragment(new HomeFragment());




        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                binding.toolbar.setVisibility(View.GONE);
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.notification) {
                binding.toolbar.setVisibility(View.GONE);
                replaceFragment(new NotificationFragment());
            } else if (itemId == R.id.add) {
                binding.toolbar.setVisibility(View.GONE);
                replaceFragment(new AddPostFragment());
            } else if (itemId == R.id.search) {
                binding.toolbar.setVisibility(View.GONE);
                replaceFragment(new SearchFragment());
            } else if (itemId == R.id.profile) {
                binding.toolbar.setVisibility(View.VISIBLE);
                replaceFragment(new ProfileFragment());
            }
            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.setting){
            auth.signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager=getSupportFragmentManager();

        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();

    }

}