package com.practice.e_commerce_app.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.practice.e_commerce_app.Adapters.CartProductAdapter;
import com.practice.e_commerce_app.Models.ProductModel;
import com.practice.e_commerce_app.R;

import java.util.ArrayList;

public class CartFragment extends Fragment {
    RecyclerView cart_product_recyclerview;
    ArrayList<ProductModel> cartProductList;
    DatabaseReference reference;
    CartProductAdapter cartProductAdapter;

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

        // Firebase Instance
        reference = FirebaseDatabase.getInstance().getReference();

        cartProductList = new ArrayList<>();

        cartProductAdapter = new CartProductAdapter(cartProductList, view.getContext());
        cart_product_recyclerview.setAdapter(cartProductAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        cart_product_recyclerview.setLayoutManager(linearLayoutManager);

        getCartProductId();

        return view;
    }

    private void displayCartProducts(String productId) {
        cartProductList.clear();
        reference = FirebaseDatabase.getInstance().getReference().child("Products").child(productId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ProductModel model = new ProductModel();
                model.setProduct_id(snapshot.child("product_id").getValue(String.class));
                model.setProduct_title(snapshot.child("product_name").getValue(String.class));
                model.setProduct_price(snapshot.child("product_price").getValue(String.class));
                model.setProduct_image(snapshot.child("productUrls").child("0").getValue(String.class));
                cartProductList.add(model);

                cartProductAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getCartProductId () {
        reference.child("CartProducts").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String product_id = snapshot.getKey();
                        displayCartProducts(product_id);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}