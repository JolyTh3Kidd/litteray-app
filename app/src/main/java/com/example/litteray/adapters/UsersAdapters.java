package com.example.litteray.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.litteray.databinding.UserContainBinding;
import com.example.litteray.listeners.UserListener;
import com.example.litteray.models.User;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class UsersAdapters extends RecyclerView.Adapter<UsersAdapters.userView> {

    private final List<User> users;
    private final UserListener userListener;

    public UsersAdapters(List<User> users, UserListener userListener) {
        this.users = users;
        this.userListener = userListener;
    }

    @NonNull
    @Override
    public userView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UserContainBinding userContainBinding = UserContainBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new userView(userContainBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull userView holder, int position) {
        holder.setUserData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class userView extends RecyclerView.ViewHolder {

        UserContainBinding binding;

        userView(UserContainBinding userContainBinding) {
            super(userContainBinding.getRoot());
            binding = userContainBinding;
        }
        void setUserData(User user) {
            binding.textName.setText(user.username);
            binding.textLogin.setText(user.login);
            binding.profilePic.setImageBitmap(getUserImage(user.image));
            binding.getRoot().setOnClickListener(v -> userListener.onUserClicked(user));

        }
    }

    private Bitmap getUserImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
