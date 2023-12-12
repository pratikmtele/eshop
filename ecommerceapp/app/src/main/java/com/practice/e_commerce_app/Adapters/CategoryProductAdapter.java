package com.practice.e_commerce_app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.practice.e_commerce_app.Models.ProductModel;
import com.practice.e_commerce_app.ProductDescActivity;
import com.practice.e_commerce_app.R;

import java.util.ArrayList;

public class CategoryProductAdapter extends RecyclerView.Adapter<CategoryProductAdapter.ViewHolder>{
    Context context;
    ArrayList<ProductModel> list;

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
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            product_layout = itemView.findViewById(R.id.product_layout);
            product_image = itemView.findViewById(R.id.product_detail_image);
            product_title = itemView.findViewById(R.id.product_detail_title);
            product_price = itemView.findViewById(R.id.product_detail_price);
        }
    }
}
