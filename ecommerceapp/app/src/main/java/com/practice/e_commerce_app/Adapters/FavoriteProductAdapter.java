package com.practice.e_commerce_app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.practice.e_commerce_app.Models.FavoriteProductModel;
import com.practice.e_commerce_app.R;

import java.util.ArrayList;

public class FavoriteProductAdapter extends RecyclerView.Adapter<FavoriteProductAdapter.viewHolder>{
    ArrayList<FavoriteProductModel> list;
    Context context;

    public FavoriteProductAdapter(ArrayList<FavoriteProductModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_favorite_product_design, parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        FavoriteProductModel model = list.get(position);
        holder.favorite_product_image.setImageResource(model.getProductImage());
        holder.title.setText(model.getTitle());
        holder.price.setText(model.getPrice());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView favorite_product_image;
        TextView title, price;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            favorite_product_image = itemView.findViewById(R.id.favorite_product_image);
            title = itemView.findViewById(R.id.favorite_product_title);
            price = itemView.findViewById(R.id.favorite_product_price);
        }
    }
}
