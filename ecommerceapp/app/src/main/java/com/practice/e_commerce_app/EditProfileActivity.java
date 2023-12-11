package com.practice.e_commerce_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.practice.e_commerce_app.Fragments.ProfileFragment;
import com.practice.e_commerce_app.databinding.ActivityEditProfileBinding;

public class EditProfileActivity extends AppCompatActivity {
    ActivityEditProfileBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.editProfileBackBtn.setOnClickListener(view -> {
            Intent intent = new Intent(EditProfileActivity.this, ProfileFragment.class);
            startActivity(intent);
        });
    }
}