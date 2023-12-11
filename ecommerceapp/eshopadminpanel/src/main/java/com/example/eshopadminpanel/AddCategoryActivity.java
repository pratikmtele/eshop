package com.example.eshopadminpanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.eshopadminpanel.databinding.ActivityAddCategoryBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCategoryActivity extends AppCompatActivity {
    ActivityAddCategoryBinding binding;
    FirebaseDatabase database;
    DatabaseReference reference;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Adding Category");
        progressDialog.setMessage("Please wait while adding category...");


        binding.categoryBackBtn.setOnClickListener(v -> {
            startActivity(new Intent(AddCategoryActivity.this, MainActivity.class));
        });

        binding.addCategory.setOnClickListener(v -> {
            String category = binding.edtCategory.getText().toString();
            if (!category.equals("")){
                progressDialog.show();
                uploadCategory(category);
            }else {
                Toast.makeText(getApplicationContext(), "Please enter category", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadCategory(String category) {
        reference.child("Categories").push().setValue(category).addOnSuccessListener(unused -> {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Category Added Successfully", Toast.LENGTH_SHORT).show();
            binding.edtCategory.setText("");
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}