package com.practice.e_commerce_app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.practice.e_commerce_app.Models.TrendingProductModel;
import com.practice.e_commerce_app.R;

import java.util.ArrayList;

public class TrendingProductAdapter extends RecyclerView.Adapter<TrendingProductAdapter.viewHolder> {
    ArrayList<TrendingProductModel> list;
    Context context;

    public TrendingProductAdapter(ArrayList<TrendingProductModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_popular_product_design, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        TrendingProductModel model = list.get(position);
        holder.imageView.setImageResource(model.getImage());
        holder.title.setText(model.getTitle());
        holder.price.setText(model.getPrice());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title, price;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.popular_product_image);
            title = itemView.findViewById(R.id.popular_product_title);
            price = itemView.findViewById(R.id.popular_product_price);
        }
    }
}
