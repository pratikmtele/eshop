package com.example.eshopadminpanel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eshopadminpanel.Models.AdminModel;
import com.example.eshopadminpanel.databinding.ActivitySignupBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    private FirebaseAuth auth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        binding.backBtn.setOnClickListener(view -> {
            Intent backToLoginIntent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(backToLoginIntent);
        });

        binding.signUpBtn.setOnClickListener(view -> {
            if (binding.signupName.getText().toString().equals("") ||
                    binding.signupEmail.getText().toString().equals("") ||
                    binding.signupPhone.getText().toString().equals("") ||
                    binding.signupPassword.getText().toString().equals("")) {

                Toast.makeText(SignupActivity.this, "Fill all required fields.", Toast.LENGTH_SHORT).show();
            } else if (validatePhoneNumber()){
                binding.signupProgressBar.setVisibility(View.VISIBLE);
                binding.signUpBtn.setVisibility(View.INVISIBLE);
                createAccount();
            }
        });

        binding.logInBtn.setOnClickListener(view -> {
            Intent loginIntent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(loginIntent);
        });
    }

    public void createAccount() {
            auth.createUserWithEmailAndPassword(binding.signupEmail.getText().toString(), binding.signupPassword.getText()
                    .toString()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    AdminModel user = new AdminModel(
                            binding.signupName.getText().toString(),
                            binding.signupEmail.getText().toString(),
                            binding.signupPhone.getText().toString(),
                            1);

                    database.getReference().child("Admin").child(task.getResult().getUser().getUid()).setValue(user);
                    binding.signupProgressBar.setVisibility(View.GONE);
                    binding.signUpBtn.setVisibility(View.VISIBLE);
                    Toast.makeText(SignupActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                    binding.signupName.setText("");
                    binding.signupEmail.setText("");
                    binding.signupPassword.setText("");
                    binding.signupPhone.setText("");
                    auth.signOut();
                } else {
                    binding.signupProgressBar.setVisibility(View.GONE);
                    binding.signUpBtn.setVisibility(View.VISIBLE);
                    Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    }

    private Boolean validatePhoneNumber() {
        if (binding.signupPhone.length() == 0){
            Toast.makeText(this, "Phone number is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (binding.signupPhone.length() != 10){
            Toast.makeText(this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}