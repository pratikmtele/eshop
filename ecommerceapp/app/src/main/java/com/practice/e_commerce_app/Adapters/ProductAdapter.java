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
import com.practice.e_commerce_app.Models.ProductModel;
import com.practice.e_commerce_app.ProductDescActivity;
import com.practice.e_commerce_app.R;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{

    Context context;
    ArrayList<ProductModel> arrayList;

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
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            product_layout = itemView.findViewById(R.id.product_layout);
            productImageView = itemView.findViewById(R.id.product_detail_image);
            productTitleTextView = itemView.findViewById(R.id.product_detail_title);
            productPriceTextView = itemView.findViewById(R.id.product_detail_price);
        }
    }
}
