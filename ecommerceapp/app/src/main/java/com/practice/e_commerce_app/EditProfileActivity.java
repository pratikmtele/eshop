package com.practice.e_commerce_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.practice.e_commerce_app.Models.UserModel;
import com.practice.e_commerce_app.databinding.ActivityEditProfileBinding;

public class EditProfileActivity extends AppCompatActivity {
    ActivityEditProfileBinding binding;
    DatabaseReference reference;
    Uri profile_image;
    ProgressDialog progressDialog;
    UserModel user;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // FirebaseInstance
        reference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading Profile");
        progressDialog.setMessage("Please wait while uploading profile...");

        binding.editProfileBackBtn.setOnClickListener(view -> {
            Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
            intent.putExtra("FragmentTag", "ProfileFragment");
            startActivity(intent);
        });

        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });

        // display profile
        reference.child("Users").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = new UserModel();
                user.setName(snapshot.child("name").getValue(String.class));
                user.setEmail(snapshot.child("email").getValue(String.class));
                user.setPhone(snapshot.child("phone").getValue(String.class));
                user.setProfilePic(snapshot.child("profilePic").getValue(String.class));
                if (user.getProfilePic() != null) {
                    Glide.with(EditProfileActivity.this).load(user.getProfilePic()).into(binding.profileImage);
                }
                binding.uptName.setText(user.getName());
                binding.uptEmail.setText(user.getEmail());
                binding.uptPhone.setText(user.getPhone());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // update user details
        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.uptName.getText().toString();
                String email = binding.uptEmail.getText().toString();
                String phone = binding.uptPhone.getText().toString();

                if (name.equals(user.getName()) && email.equals(user.getEmail()) && phone.equals(user.getPhone())) {
                    return;
                } else {
                    progressDialog.setTitle("Updating your details");
                    progressDialog.setMessage("Please wait while updating your details...");
                    progressDialog.show();
                    user.setName(name);
                    user.setEmail(email);
                    user.setPhone(phone);

                    updatingUserDetails();
                }
            }
        });
    }

    private void updatingUserDetails() {
        reference.child("Users").child(auth.getCurrentUser().getUid())
                .setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(EditProfileActivity.this, "Details updated", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadProfile() {
        progressDialog.show();
        StorageReference ProfileFolder = FirebaseStorage.getInstance().getReference().child("Profile");
        StorageReference ProfileName = ProfileFolder.child(auth.getCurrentUser().getUid()).child("profile:0");

        ProfileName.putFile(profile_image).addOnSuccessListener(taskSnapshot -> ProfileName.getDownloadUrl().addOnSuccessListener(uri -> {
            String profile_image_url = String.valueOf(uri);
            storeLinks(profile_image_url, FirebaseAuth.getInstance().getCurrentUser().getUid());

        }).addOnFailureListener(e -> {
            Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }));
    }

    private void storeLinks(String profileImageUrl, String userId) {
        reference.child("Users").child(userId).child("profilePic")
                .setValue(profileImageUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EditProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data.getClipData() != null) {
                profile_image = data.getClipData().getItemAt(0).getUri();
                if (profile_image != null)
                    uploadProfile();
            } else {
                Uri imageUrl = data.getData();
                profile_image = imageUrl;
                if (profile_image != null)
                    uploadProfile();
            }
        }
    }
}