package com.practice.e_commerce_app.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.practice.e_commerce_app.AddressActivity;
import com.practice.e_commerce_app.EditProfileActivity;
import com.practice.e_commerce_app.LoginActivity;
import com.practice.e_commerce_app.Models.UserModel;
import com.practice.e_commerce_app.NotificationActivity;
import com.practice.e_commerce_app.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    LinearLayout sign_out, edit_profile, notifications, addAddress;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference ref;
    TextView user_name;
    CircleImageView profile_image;

    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        sign_out = view.findViewById(R.id.sign_out_layout);
        edit_profile = view.findViewById(R.id.edit_profile_layout);
        notifications = view.findViewById(R.id.notifications_layout);
        user_name = view.findViewById(R.id.user_name);
        profile_image = view.findViewById(R.id.profile_image);
        addAddress = view.findViewById(R.id.shopping_address_layout);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        //display profile picture
        ref.child("Users").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel user = new UserModel();
                user.setProfilePic(snapshot.child("profilePic").getValue(String.class));
                Glide.with(view.getContext()).load(user.getProfilePic()).into(profile_image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sign_out.setOnClickListener(view1 -> {
            auth.signOut();
            Intent intent = new Intent(view.getContext(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        });
        edit_profile.setOnClickListener(view12 -> {
            Intent intent = new Intent(view.getContext(), EditProfileActivity.class);
            startActivity(intent);
        });

        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), NotificationActivity.class));
            }
        });

        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), AddressActivity.class));
            }
        });

        return view;
    }
}