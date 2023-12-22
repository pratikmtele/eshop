package com.example.eshopadminpanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.eshopadminpanel.Adapters.OrderAdapter;
import com.example.eshopadminpanel.Models.OrderModel;
import com.example.eshopadminpanel.Models.ProductModel;
import com.example.eshopadminpanel.databinding.ActivityOrdersBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrdersActivity extends AppCompatActivity {
    ActivityOrdersBinding binding;
    ArrayList<ProductModel> productList;
    ArrayList<OrderModel> orderList;
    OrderAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        orderList = new ArrayList<>();
        productList = new ArrayList<>();

        binding.ordersBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrdersActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        adapter = new OrderAdapter(OrdersActivity.this, productList);
        binding.ordersRecyclerview.setAdapter(adapter);
        binding.ordersRecyclerview.setLayoutManager(new LinearLayoutManager(OrdersActivity.this, LinearLayoutManager.VERTICAL, false));

        // fetch order details
        getOrderDetails();

    }

    private void getOrderDetails() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("OrderDetails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                        OrderModel model = snapshot1.getValue(OrderModel.class);
                        orderList.add(model);
                    }
                }
                getProductDetails();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getProductDetails() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");
        for (int i=0; i< orderList.size(); i++){
            for (int j=0; j<orderList.get(i).getProduct_ids().size(); j++){
                String product_id = orderList.get(i).getProduct_ids().get(j).getProduct_id();
                reference.child(product_id)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                ProductModel model = new ProductModel();
                                model.setProduct_id(dataSnapshot.child("product_id").getValue(String.class));
                                model.setProduct_image(dataSnapshot.child("productUrls").child("0").getValue(String.class));
                                model.setProduct_name(dataSnapshot.child("product_name").getValue(String.class));
                                model.setProduct_price(dataSnapshot.child("product_price").getValue(String.class));
                                productList.add(model);
                                adapter.notifyDataSetChanged();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
            }
        }
    }
}