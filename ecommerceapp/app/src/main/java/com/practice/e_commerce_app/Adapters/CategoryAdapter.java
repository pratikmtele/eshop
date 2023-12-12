package com.practice.e_commerce_app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.practice.e_commerce_app.CategoryProductActivity;
import com.practice.e_commerce_app.Models.CategoryModel;
import com.practice.e_commerce_app.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.viewHolder> {
    ArrayList<CategoryModel> categoryList;
    Context context;

    public CategoryAdapter(ArrayList<CategoryModel> categoryList, Context context) {
        this.categoryList = categoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_category_design, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        CategoryModel categoryModel = categoryList.get(position);
        holder.categoryName.setText(categoryModel.getCategory());

        holder.categoryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleCategoryClickEvent(categoryList.get(holder.getAdapterPosition()).getCategory_id());
            }
        });
    }

    private void handleCategoryClickEvent(String category_id) {
        Intent intent = new Intent(context, CategoryProductActivity.class);
        intent.putExtra("category_id", category_id);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.category_name);
        }
    }
}
