package com.practice.e_commerce_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.practice.e_commerce_app.Helper.AddToCartProduct;
import com.practice.e_commerce_app.Models.AddressModel;
import com.practice.e_commerce_app.databinding.ActivityProductDescBinding;

import java.util.ArrayList;

public class ProductDescActivity extends AppCompatActivity {

    ActivityProductDescBinding binding;
    DatabaseReference reference;
    String product_id, product_title, product_price;
    ArrayList<SlideModel> slideModel;
    String imageUrl;
    AddressModel addressModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDescBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Firebase Instance
        reference = FirebaseDatabase.getInstance().getReference();

        slideModel = new ArrayList<>();

        addressModel = new AddressModel();

        Intent productIntent = getIntent();
        product_id = productIntent.getStringExtra("productId");

        // fetching data from the database
        reference.child("Products").child(product_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                product_title = dataSnapshot.child("product_name").getValue(String.class);
                product_price = dataSnapshot.child("product_price").getValue(String.class);
                for (int i = 0; i < 3; i++) {
                    imageUrl = dataSnapshot.child("productUrls").child(i + "").getValue(String.class);
                    if (imageUrl != null) {
                        slideModel.add(new SlideModel(imageUrl, ScaleTypes.CENTER_INSIDE));
                    }
                }

                binding.productTitle.setText(product_title);
                binding.productPrice.setText("₹" + product_price);
                binding.productImageSlider.setImageList(slideModel, ScaleTypes.CENTER_INSIDE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProductDescActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // fetching address from the database
        reference.child("Addresses").child(FirebaseAuth.getInstance().getCurrentUser()
                .getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                addressModel = snapshot.getValue(AddressModel.class);
                String address = addressModel.getBuildingName()+", "+addressModel.getArea()+", "+addressModel.getCity()+
                        ", "+addressModel.getState()+"-"+addressModel.getPincode();
                binding.userAddress.setText(address);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ProductDescActivity.this, MainActivity.class);
            intent.putExtra("FragmentTag", "HomeFragment");
            startActivity(intent);
        });

        binding.addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddToCartProduct().addToCartProduct(getApplicationContext(), FirebaseAuth.getInstance().getCurrentUser().getUid(),
                        product_id);
            }
        });

        binding.cartActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductDescActivity.this, MainActivity.class);
                intent.putExtra("FragmentTag", "CartFragment");
                startActivity(intent);
            }
        });

        isProductAddedToCart(FirebaseAuth.getInstance().getCurrentUser().getUid(), product_id);

        binding.buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isSuccess = new AddToCartProduct().addToCartProduct(ProductDescActivity.this, FirebaseAuth.getInstance()
                        .getCurrentUser().getUid(), product_id);
                if (isSuccess)
                    startActivity(new Intent(ProductDescActivity.this, PlaceOrderActivity.class));
            }
        });
    }

    private void isProductAddedToCart(String user_id, String productId) {
        reference.child("CartProducts").child(user_id).child(productId).addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean isAdded = snapshot.exists();
                if (isAdded) {
                    binding.addToCartBtn.setText("Added To Cart");
                    binding.addToCartBtn.setTextColor(R.color.black);
                    binding.addToCartBtn.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}