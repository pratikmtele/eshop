package com.practice.e_commerce_app.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
    TextView item_count, item_price, total_price, discount, delivery_charges;

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
        item_count = view.findViewById(R.id.items_count);
        item_price = view.findViewById(R.id.items_price);
        total_price = view.findViewById(R.id.total_price);
        delivery_charges = view.findViewById(R.id.delivery_charges);
        discount = view.findViewById(R.id.discount);

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
                model.setStock(snapshot.child("stock").getValue(String.class));
                cartProductList.add(model);

                cartProductAdapter.notifyDataSetChanged();
                displayOrderDetails();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void displayOrderDetails() {
        item_count.setText("(" + cartProductList.size() + " items)");
        delivery_charges.setText("₹40");
        discount.setText("₹0.0");
        Double items_price = 0.0d;
        for (int i = 0; i < cartProductList.size(); i++) {
            items_price += Double.parseDouble(cartProductList.get(i).getProduct_price());
        }
        item_price.setText("₹" + items_price);
        Double totalPrice = items_price + 40;
        total_price.setText("₹" + totalPrice);
    }

    private void getCartProductId() {
        reference.child("CartProducts").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
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