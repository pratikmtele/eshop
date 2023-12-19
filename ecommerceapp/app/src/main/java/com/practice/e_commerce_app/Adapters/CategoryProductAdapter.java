package com.practice.e_commerce_app.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

public class CategoryProductAdapter extends RecyclerView.Adapter<CategoryProductAdapter.ViewHolder> {
    Context context;
    ArrayList<ProductModel> list;
    DatabaseReference reference;

    public CategoryProductAdapter(Context context, ArrayList<ProductModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_product_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductModel model = list.get(position);
        Glide.with(context).load(model.getProduct_image()).into(holder.product_image);
        holder.product_title.setText(model.getProduct_title());
        holder.product_price.setText(model.getProduct_price());

        holder.product_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleProductClickEvent(list.get(holder.getAdapterPosition()).getProduct_id());
            }
        });

        holder.product_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ProductHelper().addToCartProduct(context, FirebaseAuth.getInstance().getCurrentUser().getUid(),
                        list.get(holder.getAdapterPosition()).getProduct_id());
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
                            holder.product_add_to_cart.setText("Added to cart");
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

    private void handleProductClickEvent(String productId) {
        Intent intent = new Intent(context, ProductDescActivity.class);
        intent.putExtra("productId", productId);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView product_image;
        TextView product_title, product_price;
        ConstraintLayout product_layout;
        AppCompatButton product_add_to_cart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            product_layout = itemView.findViewById(R.id.product_layout);
            product_image = itemView.findViewById(R.id.product_detail_image);
            product_title = itemView.findViewById(R.id.product_detail_title);
            product_price = itemView.findViewById(R.id.product_detail_price);
            product_add_to_cart = itemView.findViewById(R.id.product_add_to_cart_btn);
        }
    }
}
