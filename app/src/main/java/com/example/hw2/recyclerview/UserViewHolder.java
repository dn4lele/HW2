package com.example.hw2.recyclerview;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.hw2.R;
import com.example.hw2.databinding.UserItemBinding;
import com.example.hw2.objects.User;

public class UserViewHolder extends RecyclerView.ViewHolder{
    public UserItemBinding binding;
    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
    }
    public UserViewHolder(@NonNull UserItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
    public void bind(User user) {
        Glide.with(binding.getRoot())
                .load(user.picture)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.placeholder_ico)
                        .error(R.drawable.error_ico))
                .into(binding.UserImg);


        binding.UserFirstname.setText(user.firstname);
        binding.userLastname.setText(user.lastname);
        binding.userCountry.setText(user.country);
        binding.userCity.setText(user.city);
    }
}
