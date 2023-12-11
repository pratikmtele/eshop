package com.example.eshopadminpanel;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.eshopadminpanel.Adapters.BannerAdapter;
import com.example.eshopadminpanel.Models.BannerModel;
import com.example.eshopadminpanel.databinding.ActivityAddBannerBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AddBannerActivity extends AppCompatActivity {
    private static final int Read_Permission = 101;
    ActivityAddBannerBinding binding;
    ArrayList<Uri> bannerList;
    ArrayList<String> UrlList;
    BannerAdapter bannerAdapter;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseStorage mStorage;
    StorageReference storageReference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBannerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bannerList = new ArrayList<>();
        UrlList = new ArrayList<>();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading Banners");
        progressDialog.setMessage("Please wait while uploading banners...");
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        // Firebase Instances
        mStorage = FirebaseStorage.getInstance();
        storageReference = mStorage.getReference();

        if (ContextCompat.checkSelfPermission(AddBannerActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddBannerActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Read_Permission);
        }

        binding.addBanner.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
        });

        bannerAdapter = new BannerAdapter(bannerList, getApplicationContext());
        binding.BannerRecyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.BannerRecyclerview.setAdapter(bannerAdapter);

        binding.bannerUpload.setOnClickListener(v -> {
            if (bannerList.size() > 0) {
                uploadBanners();
            }
        });

        binding.bannerBackBtn.setOnClickListener(v -> {
            startActivity(new Intent(AddBannerActivity.this, MainActivity.class));
            finish();
        });
    }

    private void uploadBanners() {
        for (int i = 0; i < bannerList.size(); i++) {
            Uri individualBanner = bannerList.get(i);
            if (individualBanner != null) {
                progressDialog.show();
                deleteBanner(i);
                StorageReference imageFolder = FirebaseStorage.getInstance().getReference().child("Banners");
                StorageReference imageName = imageFolder.child("image" + i);
                imageName.putFile(individualBanner).addOnSuccessListener(taskSnapshot -> imageName.getDownloadUrl().addOnSuccessListener(uri -> {
                    UrlList.add(String.valueOf(uri));
                    if (UrlList.size() == bannerList.size()) {
                        storeLinks(UrlList);
                    }
                }));
            } else {
                Toast.makeText(getApplicationContext(), "List is empty", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void deleteBanner(int i) {
        storageReference.child("Banners").delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                reference.child("Banners").removeValue();
            }
        });
    }

    private void storeLinks(ArrayList<String> urlList) {
        BannerModel bannerModel = new BannerModel("", urlList);

        reference.child("Banners").setValue(urlList).addOnSuccessListener(unused -> {
            Toast.makeText(getApplicationContext(), "Banners Uploaded.", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        });

        bannerList.clear();
        bannerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data.getClipData() != null) {
                int x = data.getClipData().getItemCount();

                for (int i = 0; i < x; i++) {
                    bannerList.add(data.getClipData().getItemAt(i).getUri());
                }
                bannerAdapter.notifyDataSetChanged();
            } else {
                Uri imageUrl = data.getData();
                bannerList.add(imageUrl);
            }
            bannerAdapter.notifyDataSetChanged();
        }
    }
}