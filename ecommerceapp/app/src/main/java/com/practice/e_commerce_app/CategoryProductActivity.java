package com.practice.e_commerce_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.practice.e_commerce_app.Adapters.CategoryProductAdapter;
import com.practice.e_commerce_app.Fragments.HomeFragment;
import com.practice.e_commerce_app.Models.ProductModel;
import com.practice.e_commerce_app.databinding.ActivityCategoryProductBinding;

import java.util.ArrayList;

public class CategoryProductActivity extends AppCompatActivity {
    ActivityCategoryProductBinding binding;
    ArrayList<ProductModel> arrayList;
    CategoryProductAdapter adapter;
    DatabaseReference reference;
    String category_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        arrayList = new ArrayList<>();

        //Firebase Instance
        reference = FirebaseDatabase.getInstance().getReference();

        adapter = new CategoryProductAdapter(this, arrayList);
        binding.categoryProductRecyclerview.setAdapter(adapter);

        binding.categoryProductRecyclerview.setLayoutManager(new GridLayoutManager(this, 2));

        Intent intent = getIntent();
        category_id = intent.getStringExtra("category_id");

        binding.categoryProductBackBtn.setOnClickListener(view -> {
            startActivity(new Intent(CategoryProductActivity.this, HomeFragment.class));
            finish();
        });

        // call function to display products by category
        displayProductByCategory();
    }

    private void displayProductByCategory() {
        Query query = reference.child("Products").orderByChild("category_id").equalTo(category_id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String product_id = snapshot.child("product_id").getValue(String.class);
                    String product_name = snapshot.child("product_name").getValue(String.class);
                    String product_price = snapshot.child("product_price").getValue(String.class);
                    String imageUrl = snapshot.child("productUrls").child(0+"").getValue(String.class);

                    ProductModel model = new ProductModel(product_id, product_name, product_price, imageUrl);
                    arrayList.add(model);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}