package com.example.eshopadminpanel;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.eshopadminpanel.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    String emailId, password;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ActivityLoginBinding binding;
    Dialog forgotPassDialog;
    AppCompatButton cancel_btn, reset_btn;
    EditText txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        forgotPassDialog = new Dialog(LoginActivity.this);
        forgotPassDialog.setContentView(R.layout.forgot_password_dialogbox);
        forgotPassDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        reset_btn = forgotPassDialog.findViewById(R.id.reset_btn);
        cancel_btn = forgotPassDialog.findViewById(R.id.cancel_btn);
        txtEmail = forgotPassDialog.findViewById(R.id.txt_email);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        binding.loginBtn.setOnClickListener(view -> {
            emailId = binding.edtEmail.getText().toString();
            password = binding.edtPassword.getText().toString();

            if (emailId.equals("") || password.equals("")) {
                Toast.makeText(LoginActivity.this, "Enter Email and Password.", Toast.LENGTH_SHORT).show();
            } else {
                binding.loginBtn.setVisibility(View.INVISIBLE);
                binding.progressBar.setVisibility(View.VISIBLE);
                userLogin(emailId, password);
            }
        });

        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        binding.signUpBtn.setOnClickListener(view -> {
            Intent signupPageIntent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(signupPageIntent);
        });

        //Forgot Password
        binding.forgetPassword.setOnClickListener(view -> {
            forgotPassDialog.show();
        });

        cancel_btn.setOnClickListener(view -> {
            forgotPassDialog.hide();
        });

        reset_btn.setOnClickListener(view -> {
            if (txtEmail.getText().toString().isEmpty()) {
                Toast.makeText(LoginActivity.this, "Enter your email address.", Toast.LENGTH_SHORT).show();
            } else {
                forgetEmail(txtEmail.getText().toString());
            }
        });
    }

    private void forgetEmail(String emailId) {
        auth.sendPasswordResetEmail(emailId).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                forgotPassDialog.hide();
                Toast.makeText(LoginActivity.this, "Check your email", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void userLogin(String emailId, String password) {
        auth.signInWithEmailAndPassword(emailId, password).addOnCompleteListener(task -> {
            binding.progressBar.setVisibility(View.GONE);
            binding.loginBtn.setVisibility(View.VISIBLE);
            if (task.isSuccessful()) {
                Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                binding.edtEmail.setText("");
                binding.edtPassword.setText("");
            }
        });
    }
}