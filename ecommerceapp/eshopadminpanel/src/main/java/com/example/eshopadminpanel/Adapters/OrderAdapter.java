package com.example.eshopadminpanel.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eshopadminpanel.Models.ProductModel;
import com.example.eshopadminpanel.R;
import com.google.firestore.v1.StructuredQuery;

import java.util.ArrayList;

public class OrderAdapter extends  RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    Context context;
    ArrayList<ProductModel> list;

    public OrderAdapter(Context context, ArrayList<ProductModel> list) {
        this.context = context;
        this.list = list;
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
        holder.order_product_title.setText(model.getProduct_name());
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
