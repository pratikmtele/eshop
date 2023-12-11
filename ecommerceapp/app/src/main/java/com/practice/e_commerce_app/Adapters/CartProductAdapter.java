package com.practice.e_commerce_app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.practice.e_commerce_app.Models.CartProductModel;
import com.practice.e_commerce_app.R;

import java.util.ArrayList;

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.viewHolder>{

    ArrayList<CartProductModel> list;
    Context context;

    public CartProductAdapter(ArrayList<CartProductModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_cart_design, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        CartProductModel model = list.get(position);
        holder.product_image.setImageResource(model.getProduct_image());
        holder.product_name.setText(model.getProduct_name());
        holder.price.setText(model.getProduct_price());
        holder.quantity.setText(model.getQuantity());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView product_image, add_quantity, minus_quantity, remove_product;
        TextView product_name, price, quantity;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            product_image = itemView.findViewById(R.id.cart_product_image);
            add_quantity = itemView.findViewById(R.id.add_quantity);
            minus_quantity = itemView.findViewById(R.id.minus_quantity);
            remove_product = itemView.findViewById(R.id.remove_product);

            product_name = itemView.findViewById(R.id.cart_product_name);
            price = itemView.findViewById(R.id.cart_product_price);
            quantity = itemView.findViewById(R.id.cart_product_quantity);
        }
    }
}
