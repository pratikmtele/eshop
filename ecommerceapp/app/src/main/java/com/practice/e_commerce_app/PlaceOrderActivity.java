package com.practice.e_commerce_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.practice.e_commerce_app.Models.AddressModel;
import com.practice.e_commerce_app.Models.ProductModel;
import com.practice.e_commerce_app.databinding.ActivityPlaceOrderBinding;

import java.util.ArrayList;

public class PlaceOrderActivity extends AppCompatActivity {
    ActivityPlaceOrderBinding binding;
    ArrayList<ProductModel> productList;
    DatabaseReference reference;
    AddressModel addressModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlaceOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        productList = new ArrayList<>();
        addressModel = new AddressModel();

        // Firebase Instance
        reference = FirebaseDatabase.getInstance().getReference();

        // fetch product id from the database
        getCartProductId();

        // display customer address
        displayCustomerAddress();

    }

    private void displayCustomerAddress() {
        reference.child("Addresses").child(FirebaseAuth.getInstance().getCurrentUser()
                .getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                addressModel = snapshot.getValue(AddressModel.class);
                binding.customerName.setText(addressModel.getFullName());
                String address = addressModel.getBuildingName()+", "+addressModel.getArea()+", "+addressModel.getCity()+
                        ", "+addressModel.getState()+"-"+addressModel.getPincode();
                binding.customerAddress.setText(address);
                binding.customerPhoneNumber.setText(addressModel.getPhoneNumber());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void displayPriceDetails() {
        binding.itemsCount.setText("(" + productList.size() + " items)");
        binding.deliveryCharges.setText("₹40");
        Double items_price = 0.0d;
        for (int i = 0; i < productList.size(); i++) {
            items_price += Double.parseDouble(productList.get(i).getProduct_price());
        }
        binding.itemsPrice.setText("₹" + items_price);
        Double totalPrice = items_price + 40;
        binding.totalPrice.setText("₹" + totalPrice);
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
                Toast.makeText(PlaceOrderActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayCartProducts(String productId) {
        productList.clear();
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
                productList.add(model);

                displayPriceDetails();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}