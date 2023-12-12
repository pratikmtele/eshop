package com.practice.e_commerce_app;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    AppCompatButton signup_btn, login_btn, reset_btn, cancel_btn;
    EditText edtEmailId, edtPassword, txtEmail;
    String emailId, password;
    ProgressBar progressBar;
    FirebaseAuth auth;
    FirebaseDatabase database;
    Dialog forgotPassDialog;
    TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_btn = findViewById(R.id.login_btn);
        signup_btn = findViewById(R.id.signup_btn);
        edtEmailId = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        progressBar = findViewById(R.id.login_progress_bar);
        forgotPassword = findViewById(R.id.forget_password);
        forgotPassDialog = new Dialog(LoginActivity.this);
        forgotPassDialog.setContentView(R.layout.forgot_password_dialogbox);
        forgotPassDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        reset_btn = forgotPassDialog.findViewById(R.id.reset_btn);
        cancel_btn = forgotPassDialog.findViewById(R.id.cancel_btn);
        txtEmail = forgotPassDialog.findViewById(R.id.txt_email);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        login_btn.setOnClickListener(view -> {
            emailId = edtEmailId.getText().toString();
            password = edtPassword.getText().toString();

            if (emailId.equals("") || password.equals("")) {
                Toast.makeText(LoginActivity.this, "Enter Email and Password.", Toast.LENGTH_SHORT).show();
            } else {
                login_btn.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                userLogin(emailId, password);
            }
        });

        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        signup_btn.setOnClickListener(view -> {
            Intent signupPageIntent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(signupPageIntent);
        });

        //Forgot Password
        forgotPassword.setOnClickListener(view -> {
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
            login_btn.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            if (task.isSuccessful()) {
                Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                edtEmailId.setText("");
                edtPassword.setText("");
            }
        });
    }
}