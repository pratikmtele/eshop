package com.practice.e_commerce_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.practice.e_commerce_app.Fragments.CartFragment;
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

        String fragmentToLoad =  getIntent().getStringExtra("FragmentTag");
        if (fragmentToLoad != null)
            loadFragment(fragmentToLoad);

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_home) {
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.bottom_cart) {
                replaceFragment(new CartFragment());
            } else if (item.getItemId() == R.id.bottom_profile) {
                replaceFragment(new ProfileFragment());
            }
            return false;
        });

    }

    private void loadFragment(String fragmentToLoad) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if ("CartFragment".equals(fragmentToLoad))
            transaction.replace(R.id.frame_layout, new CartFragment()).commit();
        else if ("HomeFragment".equals(fragmentToLoad))
            transaction.replace(R.id.frame_layout, new HomeFragment()).commit();
        else if ("ProfileFragment".equals(fragmentToLoad))
            transaction.replace(R.id.frame_layout, new ProfileFragment()).commit();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}