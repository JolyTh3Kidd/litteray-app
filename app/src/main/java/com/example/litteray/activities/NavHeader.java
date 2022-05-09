package com.example.litteray.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;

import com.example.litteray.R;
import com.example.litteray.databinding.ActivityNavHeaderBinding;
import com.example.litteray.models.User;
import com.example.litteray.utilities.Constants;
import com.example.litteray.utilities.Manager;

public class NavHeader extends AppCompatActivity {

    private Manager pref;
    private User receiverUser;
    private ActivityNavHeaderBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNavHeaderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        pref = new Manager(getApplicationContext());
        userHeaderInfo();
    }

    private Bitmap getBitmapEncoded(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    private void userHeaderInfo() {
        receiverUser = (User) getIntent().getSerializableExtra(Constants.KEY_USER);
        binding.usersName.setText(receiverUser.username);
        binding.textLogin.setText(receiverUser.login);
        binding.profilePic.setImageBitmap(getBitmapEncoded(receiverUser.image));
    }
}