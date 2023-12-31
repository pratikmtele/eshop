package com.practice.e_commerce_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.practice.e_commerce_app.Helper.ProductHelper;
import com.practice.e_commerce_app.Models.AddressModel;
import com.practice.e_commerce_app.Models.ProductModel;
import com.practice.e_commerce_app.databinding.ActivityProductDescBinding;

import java.util.ArrayList;

public class ProductDescActivity extends AppCompatActivity {

    ActivityProductDescBinding binding;
    DatabaseReference reference;
    String product_id;
    ArrayList<SlideModel> slideModel;
    String imageUrl;
    AddressModel addressModel;
    ProductModel productModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDescBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Firebase Instance
        reference = FirebaseDatabase.getInstance().getReference();

        slideModel = new ArrayList<>();

        addressModel = new AddressModel();
        productModel = new ProductModel();

        Intent productIntent = getIntent();
        product_id = productIntent.getStringExtra("productId");

        // fetching data from the database
        reference.child("Products").child(product_id).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productModel = dataSnapshot.getValue(ProductModel.class);
                String product_title = dataSnapshot.child("product_name").getValue(String.class);
                for (int i = 0; i < 3; i++) {
                    imageUrl = dataSnapshot.child("productUrls").child(i + "").getValue(String.class);
                    if (imageUrl != null) {
                        slideModel.add(new SlideModel(imageUrl, ScaleTypes.CENTER_INSIDE));
                    }
                }

                binding.productTitle.setText(product_title);

                if (!productModel.getStock().equals("0"))
                    binding.productPrice.setText("₹" + productModel.getProduct_price());
                else{
                    binding.productPrice.setTextColor(getResources().getColor(R.color.red));
                    binding.productPrice.setText("Out of Stock");
                }

                binding.productImageSlider.setImageList(slideModel, ScaleTypes.CENTER_INSIDE);

                if (Integer.parseInt(productModel.getStock()) <= 0)
                    binding.buyNowBtn.setEnabled(false);
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
                String address = addressModel.getBuildingName() + ", " + addressModel.getArea() + ", " + addressModel.getCity() +
                        ", " + addressModel.getState() + "-" + addressModel.getPincode();
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
                new ProductHelper().addToCartProduct(getApplicationContext(), FirebaseAuth.getInstance().getCurrentUser().getUid(),
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
                Boolean isSuccess = new ProductHelper().addToCartProduct(ProductDescActivity.this, FirebaseAuth.getInstance()
                        .getCurrentUser().getUid(), product_id);
                if (isSuccess) {
                    startActivity(new Intent(ProductDescActivity.this, PlaceOrderActivity.class));
                }
            }
        });
    }

    private void isProductAddedToCart(String user_id, String productId) {
        reference.child("CartProducts").child(user_id).child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean isAdded = snapshot.exists();
                if (isAdded) {
                    binding.addToCartBtn.setText("Added To Cart");
                    binding.addToCartBtn.setTextColor(getResources().getColor(R.color.black));
                    binding.addToCartBtn.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}