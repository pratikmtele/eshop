package com.practice.e_commerce_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.practice.e_commerce_app.Fragments.HomeFragment;
import com.practice.e_commerce_app.Models.ProductModel;
import com.practice.e_commerce_app.databinding.ActivityProductDescBinding;

import java.util.ArrayList;

public class ProductDescActivity extends AppCompatActivity {

    ActivityProductDescBinding binding;
    DatabaseReference reference;
    String product_id, product_title, product_price;
    ArrayList<SlideModel> slideModel;
    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDescBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Firebase Instance
        reference = FirebaseDatabase.getInstance().getReference();

        slideModel = new ArrayList<>();

        Intent productIntent = getIntent();
        product_id = productIntent.getStringExtra("productId");

        // fetching data from the database
        reference.child("Products").child(product_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                product_title = dataSnapshot.child("product_name").getValue(String.class);
                product_price = dataSnapshot.child("product_price").getValue(String.class);
                for (int i=0; i<3; i++){
                    imageUrl = dataSnapshot.child("productUrls").child(i+"").getValue(String.class);
                    if (imageUrl != null){
                        slideModel.add(new SlideModel(imageUrl, ScaleTypes.CENTER_INSIDE));
                    }
                }

                binding.productTitle.setText(product_title);
                binding.productPrice.setText("₹"+product_price);
                binding.productImageSlider.setImageList(slideModel, ScaleTypes.CENTER_INSIDE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProductDescActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //binding.productImageSlider.setImageList(slideModel, ScaleTypes.CENTER_INSIDE);

        binding.backBtn.setOnClickListener(v -> {
            startActivity(new Intent(ProductDescActivity.this, HomeFragment.class));
        });

    }
}