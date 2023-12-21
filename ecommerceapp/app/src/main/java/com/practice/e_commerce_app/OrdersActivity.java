package com.practice.e_commerce_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.practice.e_commerce_app.Adapters.MyOrderAdapter;
import com.practice.e_commerce_app.Models.OrderModel;
import com.practice.e_commerce_app.Models.ProductModel;
import com.practice.e_commerce_app.databinding.ActivityOrdersBinding;

import java.util.ArrayList;

public class OrdersActivity extends AppCompatActivity {
    ActivityOrdersBinding binding;
    ArrayList<OrderModel> orderList;
    DatabaseReference reference;
    ArrayList<ProductModel> productList;
    MyOrderAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        orderList = new ArrayList<>();
        productList = new ArrayList<>();

        // Firebase Instance
        reference = FirebaseDatabase.getInstance().getReference();

        binding.ordersBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrdersActivity.this, MainActivity.class);
                intent.putExtra("FragmentTag", "ProfileFragment");
                startActivity(intent);
            }
        });

        adapter = new MyOrderAdapter(productList, OrdersActivity.this);
        binding.ordersRecyclerview.setAdapter(adapter);
        binding.ordersRecyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Fetch order details from the database
        getOrderDetails();
    }

    private void getOrderDetails() {
        reference.child("OrderDetails").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    OrderModel model = snapshot.getValue(OrderModel.class);
                    orderList.add(model);
                }
                getProductDetails();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getProductDetails() {
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Products");
        for (int i=0; i< orderList.size(); i++){
            for (int j=0; j<orderList.get(i).getProduct_ids().size(); j++){
                String product_id = orderList.get(i).getProduct_ids().get(j).getProduct_id();
                reference1.child(product_id)
                        .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ProductModel model = new ProductModel();
                        model.setProduct_id(dataSnapshot.child("product_id").getValue(String.class));
                        model.setProduct_image(dataSnapshot.child("productUrls").child("0").getValue(String.class));
                        model.setProduct_title(dataSnapshot.child("product_name").getValue(String.class));
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