package com.practice.e_commerce_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.practice.e_commerce_app.Models.AddressModel;
import com.practice.e_commerce_app.Models.OrderModel;
import com.practice.e_commerce_app.Models.ProductModel;
import com.practice.e_commerce_app.databinding.ActivityPlaceOrderBinding;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;

public class PlaceOrderActivity extends AppCompatActivity {
    ActivityPlaceOrderBinding binding;
    ArrayList<ProductModel> productList;
    DatabaseReference reference, orderDetailsReference;
    AddressModel addressModel;
    ArrayList<ProductModel> product_ids;
    Double totalPrice ;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlaceOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        productList = new ArrayList<>();
        addressModel = new AddressModel();
        product_ids = new ArrayList<>();

        // setting up progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Placing Order");
        progressDialog.setMessage("Please wait while placing your order...");

        // Firebase Instance
        reference = FirebaseDatabase.getInstance().getReference();
        orderDetailsReference = FirebaseDatabase.getInstance().getReference();

        // fetch product id from the database
        getCartProductId();

        // display customer address
        displayCustomerAddress();

        binding.placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                LocalDateTime currentDateTime = LocalDateTime.now();
                String date = currentDateTime.getDayOfMonth() +"/"+currentDateTime.getMonthValue()+"/"+currentDateTime.getYear();
                String time = currentDateTime.getHour()+":"+currentDateTime.getMinute()+":"+currentDateTime.getSecond();
                int stock;
                for (int i=0; i<productList.size(); i++){
                    stock = Integer.parseInt(productList.get(i).getStock());
                    stock -= 1;
                    productList.get(i).setStock(stock+"");
                }

                DatabaseReference productReference = FirebaseDatabase.getInstance().getReference().child("Products");
                for (int i=0; i<productList.size(); i++){
                   productReference.child(productList.get(i).getProduct_id()).child("stock")
                           .setValue(productList.get(i).getStock()).addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void unused) {
                               }
                           });
                }

                OrderModel orderModel = new OrderModel();
                orderModel.setDate(date);
                orderModel.setTime(time);
                orderModel.setTotal_price(totalPrice+"");
                orderModel.setAddress_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
                orderModel.setPayment_mode(binding.cashOnDeliveryOption.getText().toString());
                String key = orderDetailsReference.child("OrderDetails").child(FirebaseAuth.getInstance()
                        .getCurrentUser().getUid()).push().getKey();
                orderModel.setOrder_id(key);
                orderModel.setProduct_ids(product_ids);

                orderDetailsReference.child("OrderDetails").child(FirebaseAuth.getInstance()
                        .getCurrentUser().getUid()).child(key).setValue(orderModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        deleteProductsFromCart();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PlaceOrderActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void deleteProductsFromCart() {
        for (int i=0; i<productList.size(); i++){
            FirebaseDatabase.getInstance().getReference().child("CartProducts").child(FirebaseAuth.getInstance().getCurrentUser().getUid())

                    .removeValue().addOnSuccessListener(unused -> {
                        Toast.makeText(PlaceOrderActivity.this, "Order Placed. Thank you!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PlaceOrderActivity.this, MainActivity.class);
                        intent.putExtra("FragmentTag", "HomeFragment");
                        progressDialog.dismiss();
                        startActivity(intent);
                    });
        }
    }

    private void displayCustomerAddress() {
        reference.child("Addresses").child(FirebaseAuth.getInstance().getCurrentUser()
                .getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                addressModel = snapshot.getValue(AddressModel.class);
                binding.customerName.setText(addressModel.getFullName());
                String address = addressModel.getBuildingName() + ", " + addressModel.getArea() + ", " + addressModel.getCity() +
                        ", " + addressModel.getState() + "-" + addressModel.getPincode();
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
        totalPrice = items_price + 40;
        binding.totalPrice.setText("₹" + totalPrice);
    }

    private void getCartProductId() {
        reference.child("CartProducts").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String product_id = snapshot.getKey();
                    ProductModel model = new ProductModel();
                    model.setProduct_id(product_id);
                    product_ids.add(model);
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