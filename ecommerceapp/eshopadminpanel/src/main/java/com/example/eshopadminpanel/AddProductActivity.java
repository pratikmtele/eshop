package com.example.eshopadminpanel;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.eshopadminpanel.Adapters.ProductImagesAdapter;
import com.example.eshopadminpanel.Models.CategoryModel;
import com.example.eshopadminpanel.Models.ProductModel;
import com.example.eshopadminpanel.databinding.ActivityAddProductBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AddProductActivity extends AppCompatActivity {

    private final static int Read_Permission = 101;
    ActivityAddProductBinding binding;
    DatabaseReference reference;
    StorageReference storageReference;
    ArrayList<String> product_images_urls;
    ArrayList<Uri> product_images;
    ArrayList<CategoryModel> list;
    ProductImagesAdapter productImagesAdapter;
    CategoryModel categoryModel;
    String category_id, product_name, price;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Adding Product");
        progressDialog.setMessage("Please wait while adding product...");

        binding.productBackBtn.setOnClickListener(v -> {
            startActivity(new Intent(AddProductActivity.this, MainActivity.class));
            finish();
        });

        // Firebase Instances
        reference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        product_images = new ArrayList<>();
        product_images_urls = new ArrayList<>();
        list = new ArrayList<>();

        // call function to retrieve categories from the database
        retrieveCategories();

        // Permission Check
        if (ContextCompat.checkSelfPermission(AddProductActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddProductActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Read_Permission);
        }

        binding.addProductImages.setOnClickListener(v -> {
            product_images.clear();
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
        });

        productImagesAdapter = new ProductImagesAdapter(this, product_images);
        binding.productImageRecyclerview.setLayoutManager(new GridLayoutManager(this, 2));
        binding.productImageRecyclerview.setAdapter(productImagesAdapter);

        binding.autoCompleteText.setOnItemClickListener((parent, view, position, id) -> {
            category_id = list.get((int) id).getCategory_id();
        });

        // insert product details in to the database
        binding.addProductBtn.setOnClickListener(v -> {
            product_name = binding.edtProductName.getText().toString();
            price = binding.edtPrice.getText().toString();
            if (product_name == null || price == null || category_id == null) {
                Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.show();
                if (product_images.size() > 0) {
                    uploadImages();
                }
            }
        });
    }

    private void uploadImages() {
        for (int i = 0; i < product_images.size(); i++) {
            Uri individualImages = product_images.get(i);
            if (individualImages != null) {
                StorageReference imageFolder = FirebaseStorage.getInstance().getReference().child("Images");
                String key = reference.child("Products").push().getKey();
                StorageReference imageName = imageFolder.child(key).child("image" + i);
                imageName.putFile(individualImages).addOnSuccessListener(taskSnapshot -> imageName.getDownloadUrl().addOnSuccessListener(uri -> {
                    product_images_urls.add(String.valueOf(uri));
                    if (product_images_urls.size() == product_images.size()) {
                        storeLinks(product_images_urls, key);
                    }
                })).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(AddProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "List is empty", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void storeLinks(ArrayList<String> productImagesUrls, String key) {
        ProductModel productModel = new ProductModel(product_name, key, category_id, productImagesUrls, price);

        reference.child("Products").child(key).setValue(productModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Toast.makeText(AddProductActivity.this, "Product Added Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(AddProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void retrieveCategories() {
        ArrayList<String> categoryList = new ArrayList<>();

        reference.child("Categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String category_id = snapshot.getKey();
                    String category_name = snapshot.getValue().toString();
                    categoryModel = new CategoryModel(category_name, category_id);
                    list.add(categoryModel);
                    categoryList.add(categoryModel.getCategory());
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.category_items, categoryList);
                binding.autoCompleteText.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddProductActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data.getClipData() != null) {
                for (int i = 0; i < 3; i++) {
                    product_images.add(data.getClipData().getItemAt(i).getUri());
                }
                productImagesAdapter.notifyDataSetChanged();
            } else {
                Uri imageUrl = data.getData();
                product_images.add(imageUrl);
            }
            productImagesAdapter.notifyDataSetChanged();
        }
    }
}