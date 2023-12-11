package com.practice.e_commerce_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.practice.e_commerce_app.Fragments.CartFragment;
import com.practice.e_commerce_app.Fragments.FavoriteFragment;
import com.practice.e_commerce_app.Fragments.HomeFragment;
import com.practice.e_commerce_app.Fragments.ProfileFragment;
import com.practice.e_commerce_app.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    String id;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_home) {
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.bottom_favorite) {
                replaceFragment(new FavoriteFragment());
            } else if (item.getItemId() == R.id.bottom_cart) {
                replaceFragment(new CartFragment());
            } else if (item.getItemId() == R.id.bottom_profile) {
                replaceFragment(new ProfileFragment());
            }
            return false;
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}