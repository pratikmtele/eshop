package com.practice.e_commerce_app.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.practice.e_commerce_app.Adapters.FavoriteProductAdapter;
import com.practice.e_commerce_app.Models.FavoriteProductModel;
import com.practice.e_commerce_app.R;

import java.util.ArrayList;

public class FavoriteFragment extends Fragment {
    RecyclerView favorite_products_recyclerview;
    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_favorite, container, false);

        favorite_products_recyclerview = view.findViewById(R.id.favorite_products_recyclerview);

        ArrayList<FavoriteProductModel> favoriteProductList = new ArrayList<>();
        favoriteProductList.add(new FavoriteProductModel(R.drawable.laptop, "Lenovo IdeaPad Gaming 3 (8gb ram and 512gb " +
                "storage)", "₹55034"));
        favoriteProductList.add(new FavoriteProductModel(R.drawable.mobile, "Realme 7 (6gb ram, 64gb Storage)","₹15984"));
        favoriteProductList.add(new FavoriteProductModel(R.drawable.t_shirt, "Men's Travel white TShirt","₹250"));
        favoriteProductList.add(new FavoriteProductModel(R.drawable.laptop, "Lenovo IdeaPad Gaming 3 (8gb ram and 512gb " +
                "storage)", "₹55034"));
        favoriteProductList.add(new FavoriteProductModel(R.drawable.mobile, "Realme 7 (6gb ram, 64gb Storage)","₹15984"));
        favoriteProductList.add(new FavoriteProductModel(R.drawable.t_shirt, "Men's Travel white TShirt","₹250"));

        FavoriteProductAdapter favoriteProductAdapter = new FavoriteProductAdapter(favoriteProductList, view.getContext());
        favorite_products_recyclerview.setAdapter(favoriteProductAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 2);
        favorite_products_recyclerview.setLayoutManager(gridLayoutManager);

        return view;
    }
}