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
import com.practice.e_commerce_app.Models.PopularProductModel;
import com.practice.e_commerce_app.NotificationActivity;
import com.practice.e_commerce_app.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    ImageSlider imageSlider;
    ImageView notifications;
    RecyclerView category_recyclerview, product_recyclerview;
    FirebaseDatabase database;
    DatabaseReference reference;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        product_recyclerview = view.findViewById(R.id.popular_products_recyclerview);
        notifications = view.findViewById(R.id.notifications);

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
                ArrayList<SlideModel>slideModels = new ArrayList<>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
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
                for (DataSnapshot snapshot: datasnapshot.getChildren()) {
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

        //Popular Products Recyclerview
        ArrayList<PopularProductModel> productList = new ArrayList<>();
        productList.add(new PopularProductModel(R.drawable.download, "Men’s Travel Brown checkered spread Collar Casual Shirt", "₹754"));
        productList.add(new PopularProductModel(R.drawable.t_shirt, "Men’s White TShirt", "₹450"));
        productList.add(new PopularProductModel(R.drawable.laptop, "Lenovo IdeaPad Gaming 3 Laptop (8gb Ram, 512gb Hard Disk)", "₹55000"));
        productList.add(new PopularProductModel(R.drawable.mobile, "Realme 7 pro (6 gb Ram and 64 gb Storage)", "₹15343"));
        productList.add(new PopularProductModel(R.drawable.download, "Men’s Travel Brown checkered spread Collar Casual Shirt", "₹1400"));
        productList.add(new PopularProductModel(R.drawable.laptop, "Lenovo IdeaPad Gaming 3 Laptop (8gb Ram, 512gb Hard Disk)", "₹55000"));
        productList.add(new PopularProductModel(R.drawable.mobile, "Realme 7 pro (6 gb Ram and 64 gb Storage)", "₹15343"));
        productList.add(new PopularProductModel(R.drawable.download, "Men’s Travel Brown checkered spread Collar Casual Shirt", "₹1400"));

        ProductAdapter productAdapter = new ProductAdapter(productList, view.getContext());
        product_recyclerview.setAdapter(productAdapter);

        GridLayoutManager productLayout = new GridLayoutManager(view.getContext(), 2);
        product_recyclerview.setLayoutManager(productLayout);

        return view;
    }
}