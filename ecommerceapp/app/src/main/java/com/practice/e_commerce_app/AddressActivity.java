package com.practice.e_commerce_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.practice.e_commerce_app.Models.AddressModel;
import com.practice.e_commerce_app.databinding.ActivityAddressBinding;

public class AddressActivity extends AppCompatActivity {
    ActivityAddressBinding binding;
    String[] indian_states = {"Select State", "Andhra Pradesh",
            "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand",
            "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha",
            "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal",
            "Jammu and Kashmir", "Ladakh", "Andaman and Nicobar Islands", "Chandigarh", "Dadra and Nagar Haveli and Daman and Diu",
            "Lakshadweep", "Delhi", "Puducherry"};
    AddressModel addressModel;
    DatabaseReference reference;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Firebase Instance
        reference = FirebaseDatabase.getInstance().getReference();

        binding.addressBackBtn.setOnClickListener(view -> {
            Intent intent = new Intent(AddressActivity.this, MainActivity.class);
            intent.putExtra("FragmentTag", "ProfileFragment");
            startActivity(intent);
        });

        addressModel = new AddressModel();

        arrayAdapter = new ArrayAdapter<>(this, R.layout.sample_spinner_layout, indian_states);
        binding.edtState.setAdapter(arrayAdapter);

        binding.edtState.setSelection(0);

        binding.saveAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isValid = edtValidation();
                if (isValid) {
                    addressModel.setFullName(binding.edtFullName.getText().toString());
                    addressModel.setPhoneNumber(binding.edtPhoneNumber.getText().toString());
                    addressModel.setBuildingName(binding.edtBuildingName.getText().toString());
                    addressModel.setArea(binding.edtRoadName.getText().toString());
                    addressModel.setPincode(binding.edtPincode.getText().toString());
                    addressModel.setCity(binding.edtCity.getText().toString());
                    saveAddress(addressModel);
                }
            }
        });
        binding.edtState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    addressModel.setState(adapterView.getItemAtPosition(position).toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //display address
        reference.child("Addresses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    displayAddress();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void displayAddress() {
        reference.child("Addresses").child(FirebaseAuth.getInstance().getCurrentUser()
                .getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                addressModel = snapshot.getValue(AddressModel.class);
                binding.edtFullName.setText(addressModel.getFullName());
                binding.edtPhoneNumber.setText(addressModel.getPhoneNumber());
                binding.edtBuildingName.setText(addressModel.getBuildingName());
                binding.edtRoadName.setText(addressModel.getArea());
                binding.edtPincode.setText(addressModel.getPincode());
                binding.edtCity.setText(addressModel.getCity());
                int position = arrayAdapter.getPosition(addressModel.getState());
                binding.edtState.setSelection(position);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void saveAddress(AddressModel addressModel) {
        reference.child("Addresses").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(addressModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddressActivity.this, "Address Saved", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddressActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public Boolean edtValidation() {

        if (binding.edtFullName.getText().toString().equals(addressModel.getFullName()) &&
                binding.edtPhoneNumber.getText().toString().equals(addressModel.getPhoneNumber()) &&
                binding.edtBuildingName.getText().toString().equals(addressModel.getBuildingName()) &&
                binding.edtRoadName.getText().toString().equals(addressModel.getArea()) &&
                binding.edtState.getSelectedItemPosition() == arrayAdapter.getPosition(addressModel.getState()) &&
                binding.edtCity.getText().toString().equals(addressModel.getCity()) &&
                binding.edtPincode.getText().toString().equals(addressModel.getPincode())) {
            return false;
        }

        if (binding.edtFullName.length() == 0) {
            Toast.makeText(this, "Full name is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (binding.edtPhoneNumber.length() == 0) {
            Toast.makeText(AddressActivity.this, "Phone number is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (binding.edtPhoneNumber.length() != 10) {
            Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (binding.edtBuildingName.length() == 0) {
            Toast.makeText(this, "Building name or House no. is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (binding.edtRoadName.length() == 0) {
            Toast.makeText(this, "Area or Road name is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (binding.edtState.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Select state", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (binding.edtCity.length() == 0) {
            Toast.makeText(this, "City is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (binding.edtPincode.length() == 0) {
            Toast.makeText(this, "Pincode is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (binding.edtPincode.length() != 6) {
            Toast.makeText(this, "Invalid Pincode", Toast.LENGTH_SHORT).show();
            return false;
        }

        // if all are valid then return true
        return true;
    }
}