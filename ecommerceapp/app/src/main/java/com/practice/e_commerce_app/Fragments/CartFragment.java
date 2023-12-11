package com.practice.e_commerce_app.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.practice.e_commerce_app.Adapters.CartProductAdapter;
import com.practice.e_commerce_app.Models.CartProductModel;
import com.practice.e_commerce_app.R;

import java.util.ArrayList;

public class CartFragment extends Fragment {
    RecyclerView cart_product_recyclerview;
    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        cart_product_recyclerview = view.findViewById(R.id.cart_recyclerview);

        ArrayList<CartProductModel> cartProductList = new ArrayList<>();
        cartProductList.add(new CartProductModel(R.drawable.laptop, "IdeaPad Gaming 3 Laptop (6 gb Ram and 512gb " +
                "Hard Disk)", "₹55000", "1"));
        cartProductList.add(new CartProductModel(R.drawable.t_shirt, "Men's travel white t-shirt", "₹490", "2"));
        cartProductList.add(new CartProductModel(R.drawable.mobile, "IQOO z7 pro (6gb Ram and 128gb Storage)","₹15745","1"));
        cartProductList.add(new CartProductModel(R.drawable.download, "Men's red checkered shirt","₹2100","3"));
        cartProductList.add(new CartProductModel(R.drawable.laptop, "IdeaPad Gaming 3 Laptop (6 gb Ram and 512gb " +
                "Hard Disk)", "₹55000", "1"));
        cartProductList.add(new CartProductModel(R.drawable.t_shirt, "Men's travel white t-shirt", "₹490", "2"));
        cartProductList.add(new CartProductModel(R.drawable.mobile, "IQOO z7 pro (6gb Ram and 128gb Storage)","₹15745","1"));

        CartProductAdapter cartProductAdapter = new CartProductAdapter(cartProductList, view.getContext());
        cart_product_recyclerview.setAdapter(cartProductAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        cart_product_recyclerview.setLayoutManager(linearLayoutManager);

        return view;
    }
}