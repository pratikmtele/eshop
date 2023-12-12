package com.practice.e_commerce_app.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.practice.e_commerce_app.Adapters.CategoryAdapter;
import com.practice.e_commerce_app.Adapters.ProductAdapter;
import com.practice.e_commerce_app.Models.CategoryModel;
import com.practice.e_commerce_app.Models.ProductModel;
import com.practice.e_commerce_app.NotificationActivity;
import com.practice.e_commerce_app.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    ImageSlider imageSlider;
    ImageView notifications;
    RecyclerView category_recyclerview, product_recyclerview;
    FirebaseDatabase database;
    DatabaseReference reference;
    public ArrayList<ProductModel> productList;
    ProductAdapter productAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(getContext(), productList);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolBar);

        //Firebase Instances
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        imageSlider = view.findViewById(R.id.product_image_slider);
        category_recyclerview = view.findViewById(R.id.category_recyclerview);
        notifications = view.findViewById(R.id.notifications);
        product_recyclerview = view.findViewById(R.id.show_products_recyclerview);

        // Toolbar settings
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        //Notifications button
        notifications.setOnClickListener(view1 -> {
            Intent notificationIntent = new Intent(getActivity(), NotificationActivity.class);
            startActivity(notificationIntent);
        });

        // create a list for images //imageSlider

        reference.child("Banners").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<SlideModel> slideModels = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    slideModels.add(new SlideModel(snapshot.getValue().toString(), ScaleTypes.FIT));
                }
                imageSlider.setImageList(slideModels, ScaleTypes.FIT);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(view.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Category recyclerview
        ArrayList<CategoryModel> list = new ArrayList<>();

        reference.child("Categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    list.add(new CategoryModel(snapshot.getValue().toString()));
                }
                CategoryAdapter categoryAdapter = new CategoryAdapter(list, view.getContext());
                category_recyclerview.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(view.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        category_recyclerview.setLayoutManager(linearLayoutManager);

        product_recyclerview.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        product_recyclerview.setAdapter(productAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        productList.clear();
        reference.child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String product_id = snapshot.child("product_id").getValue(String.class);
                    String product_title = snapshot.child("product_name").getValue(String.class);
                    String product_price = snapshot.child("product_price").getValue(String.class);
                    String product_imageUrl = snapshot.child("productUrls").child("0").getValue(String.class);

                    ProductModel model = new ProductModel(product_id, product_title, "₹"+product_price, product_imageUrl);
                    productList.add(model);
                }
                productAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}