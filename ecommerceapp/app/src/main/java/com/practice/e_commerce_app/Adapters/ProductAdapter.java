package com.practice.e_commerce_app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.practice.e_commerce_app.Models.ProductModel;
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
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageView;
        TextView productTitleTextView, productPriceTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productImageView = itemView.findViewById(R.id.product_detail_image);
            productTitleTextView = itemView.findViewById(R.id.product_detail_title);
            productPriceTextView = itemView.findViewById(R.id.product_detail_price);
        }
    }
}
