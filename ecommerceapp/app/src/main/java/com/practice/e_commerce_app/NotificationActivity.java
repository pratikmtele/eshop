package com.practice.e_commerce_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.practice.e_commerce_app.Adapters.NotificationAdapter;
import com.practice.e_commerce_app.Fragments.HomeFragment;
import com.practice.e_commerce_app.Models.NotificationModel;
import com.practice.e_commerce_app.databinding.ActivityNotificationBinding;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {
    ActivityNotificationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.notificationsBackBtn.setOnClickListener(view -> {
            Intent backToPreviusIntent = new Intent(NotificationActivity.this, HomeFragment.class);
            startActivity(backToPreviusIntent);
        });

        binding.noNotificationLayout.setVisibility(View.GONE);

        // Notifications Recyclerview
        ArrayList<NotificationModel> notificationList = new ArrayList<>();
        notificationList.add(new NotificationModel("Order Received!", "Your product containing Men's Travel Shirt is Received.", "1 sec ago"));
        notificationList.add(new NotificationModel("Order Shipment is Ready!", "Your product containing IdeaPad Gaming 3 is ready deliver.", "1 sec ago"));
        notificationList.add(new NotificationModel("Out for Delivery!", "Your product containing Men's Travel Shirt is out for delivery", "1 sec ago"));
        notificationList.add(new NotificationModel("Order Delivered!", "Your product containing Men's Travel Shirt has been delivered.", "1 sec ago"));
        notificationList.add(new NotificationModel("Order Received!", "Your product containing Men's Travel Shirt is Received.", "1 sec ago"));
        notificationList.add(new NotificationModel("Order Received!", "Your product containing Men's Travel Shirt is Received.", "1 sec ago"));
        notificationList.add(new NotificationModel("Order Shipment is Ready!", "Your product containing IdeaPad Gaming 3 is ready deliver.", "1 sec ago"));
        notificationList.add(new NotificationModel("Out for Delivery!", "Your product containing Men's Travel Shirt is out for delivery", "1 sec ago"));

        if (notificationList.size() > 0) {
            NotificationAdapter notificationAdapter = new NotificationAdapter(notificationList, this);
            binding.notificationRecyclerview.setAdapter(notificationAdapter);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            binding.notificationRecyclerview.setLayoutManager(linearLayoutManager);

            binding.notificationLayout.setVisibility(View.VISIBLE);
        } else {
            binding.noNotificationLayout.setVisibility(View.VISIBLE);
            binding.notificationLayout.setVisibility(View.GONE);
        }
    }
}