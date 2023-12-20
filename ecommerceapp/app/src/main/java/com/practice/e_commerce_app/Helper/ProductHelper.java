package com.practice.e_commerce_app.Helper;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProductHelper {
    DatabaseReference reference;
    Boolean isSuccess = true;

    public Boolean addToCartProduct(Context context, String user_id, String product_id) {

        reference = FirebaseDatabase.getInstance().getReference().child("CartProducts");

        reference.child(user_id).child(product_id).setValue(product_id).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                isSuccess = true;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                isSuccess = false;
            }
        });
        return isSuccess;
    }
}
