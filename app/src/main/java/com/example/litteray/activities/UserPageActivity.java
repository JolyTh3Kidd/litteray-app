package com.example.litteray.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;

import com.example.litteray.databinding.ActivityUserPageBinding;
import com.example.litteray.utilities.Constants;
import com.example.litteray.utilities.Manager;

public class UserPageActivity extends BaseActivity {

    private ActivityUserPageBinding binding;
    private Manager pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        pref = new Manager(getApplicationContext());

        userInfo();
        setListeners();
    }

    private void setListeners() {
        binding.footerMenuChat.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            overridePendingTransition(0, 0);
        });
        binding.settingMenu.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            overridePendingTransition(0, 0);
        });
        binding.profileImage.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), UserInfoActivity.class));
        });
    }

    private void userInfo() {
        byte[] bytes = Base64.decode(pref.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        binding.profileImage.setImageBitmap(bitmap);
        binding.usersName.setText(pref.getString(Constants.KEY_USERNAME));
        binding.textLogin.setText(pref.getString(Constants.KEY_LOGIN));
        binding.profileType.setText(pref.getString(Constants.KEY_PROFILE_TYPE));
    }


}