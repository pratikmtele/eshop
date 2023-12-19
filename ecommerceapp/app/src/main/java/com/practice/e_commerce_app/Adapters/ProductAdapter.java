package com.practice.e_commerce_app.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.practice.e_commerce_app.Helper.ProductHelper;
import com.practice.e_commerce_app.Models.ProductModel;
import com.practice.e_commerce_app.ProductDescActivity;
import com.practice.e_commerce_app.R;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    Context context;
    ArrayList<ProductModel> arrayList;
    DatabaseReference reference;

    public ProductAdapter(Context context, ArrayList<ProductModel> productModel) {
        this.context = context;
        this.arrayList = productModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_product_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductModel model = arrayList.get(position);
        Glide.with(context).load(model.getProduct_image()).into(holder.productImageView);
        holder.productTitleTextView.setText(model.getProduct_title());
        holder.productPriceTextView.setText(model.getProduct_price());

        holder.product_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleProductClick(arrayList.get(holder.getAdapterPosition()).getProduct_id());
            }
        });

        //Add to cart btn click event
        holder.product_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isSuccess = new ProductHelper().addToCartProduct(context, FirebaseAuth.getInstance().getCurrentUser().getUid(),
                        arrayList.get(holder.getAdapterPosition()).getProduct_id());
                if (isSuccess)
                    Toast.makeText(context, "Product Added to cart", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        isProductAddedToCart(holder, model.getProduct_id());
    }

    private void isProductAddedToCart(ViewHolder holder, String productId) {
        reference = FirebaseDatabase.getInstance().getReference().child("CartProducts");
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(productId)
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Boolean isAdded = snapshot.exists();
                        if (isAdded) {
                            holder.product_add_to_cart.setText("Added To cart");
                            holder.product_add_to_cart.setEnabled(false);
                            holder.product_add_to_cart.setBackgroundResource(R.drawable.button_stroke);
                            holder.product_add_to_cart.setTextColor(R.color.black);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    private void handleProductClick(String product_id) {
        Intent intent = new Intent(context, ProductDescActivity.class);

        // pass data to the ProductDescActivity
        intent.putExtra("productId", product_id);

        // start the activity
        context.startActivity(intent);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageView;
        ConstraintLayout product_layout;
        TextView productTitleTextView, productPriceTextView;
        AppCompatButton product_add_to_cart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            product_layout = itemView.findViewById(R.id.product_layout);
            productImageView = itemView.findViewById(R.id.product_detail_image);
            productTitleTextView = itemView.findViewById(R.id.product_detail_title);
            productPriceTextView = itemView.findViewById(R.id.product_detail_price);
            product_add_to_cart = itemView.findViewById(R.id.product_add_to_cart_btn);
        }
    }
}
