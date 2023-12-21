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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder>{
    ArrayList<ProductModel> list;
    Context context;

    public MyOrderAdapter(ArrayList<ProductModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_orders_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductModel model = list.get(position);
        Glide.with(context).load(model.getProduct_image()).into(holder.order_product_image);
        holder.order_product_title.setText(model.getProduct_title());
        holder.order_product_price.setText("₹"+model.getProduct_price());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView order_product_image;
        TextView order_product_title, order_product_price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            order_product_image = itemView.findViewById(R.id.order_product_image);
            order_product_title = itemView.findViewById(R.id.order_product_title);
            order_product_price = itemView.findViewById(R.id.order_total_price);
        }
    }
}
