package com.practice.e_commerce_app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.practice.e_commerce_app.Models.ProductModel;
import com.practice.e_commerce_app.ProductDescActivity;
import com.practice.e_commerce_app.R;

import java.util.ArrayList;

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.viewHolder> {

    ArrayList<ProductModel> list;
    Context context;
    DatabaseReference reference;

    public CartProductAdapter(ArrayList<ProductModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public CartProductAdapter() {
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_cart_design, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        ProductModel model = list.get(position);
        Glide.with(context).load(model.getProduct_image()).into(holder.product_image);
        holder.product_name.setText(model.getProduct_title());
        holder.price.setText("₹" + model.getProduct_price());

        reference = FirebaseDatabase.getInstance().getReference();

        holder.cart_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleProductClick(list.get(holder.getAdapterPosition()).getProduct_id());
            }
        });

        // remove product from the cart
        holder.remove_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeCartProduct(list.get(holder.getAdapterPosition()).getProduct_id());
            }
        });

        if (list.get(holder.getAdapterPosition()).getStock() == "0" || list.get(holder.getAdapterPosition()).getStock() == null)
            holder.out_of_stock.setText("Out of stock");
        else
            holder.out_of_stock.setText("");
    }

    private void removeCartProduct(String productId) {
        reference.child("CartProducts").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(productId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context, "Product Removed", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleProductClick(String productId) {
        Intent intent = new Intent(context, ProductDescActivity.class);
        intent.putExtra("productId", productId);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView product_image, remove_product;
        TextView product_name, price, out_of_stock;
        ConstraintLayout cart_layout;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            product_image = itemView.findViewById(R.id.cart_product_image);
            remove_product = itemView.findViewById(R.id.remove_product);

            product_name = itemView.findViewById(R.id.cart_product_name);
            price = itemView.findViewById(R.id.cart_product_price);
            cart_layout = itemView.findViewById(R.id.Cart_Layout);
            out_of_stock = itemView.findViewById(R.id.out_of_stock_msg);
        }
    }
}
