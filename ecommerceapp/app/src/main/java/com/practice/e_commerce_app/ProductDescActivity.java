package com.practice.e_commerce_app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.practice.e_commerce_app.databinding.ActivityProductDescBinding;

import java.util.ArrayList;

public class ProductDescActivity extends AppCompatActivity {

    ActivityProductDescBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDescBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // create a list for images //imageSlider
        ArrayList<SlideModel> slideModel = new ArrayList<>();
        slideModel.add(new SlideModel(R.drawable.laptop, ScaleTypes.CENTER_INSIDE));
        slideModel.add(new SlideModel(R.drawable.laptop2, ScaleTypes.CENTER_INSIDE));
        slideModel.add(new SlideModel(R.drawable.ideapad_gaming, ScaleTypes.CENTER_INSIDE));

        binding.productImageSlider.setImageList(slideModel, ScaleTypes.CENTER_INSIDE);

    }
}