package com.example.litteray.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.example.litteray.R;
import com.example.litteray.databinding.ActivitySettingsBinding;
import com.example.litteray.models.User;
import com.example.litteray.utilities.Constants;
import com.example.litteray.utilities.Manager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Objects;

public class SettingsActivity extends BaseActivity {

    private ActivitySettingsBinding binding;
    private Manager pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        pref = new Manager(getApplicationContext());

        setListeners();
        userInfo();
    }

    private void setListeners() {
        binding.aboutUs.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), AboutUsActivity.class));
        });
        binding.linkMenu.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), LinkActivity.class));
        });
        binding.logOut.setOnClickListener(v -> logOut());

        binding.footerMenuChat.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            overridePendingTransition(0, 0);
        });
        binding.footerMenuProfile.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), UserPageActivity.class));
            overridePendingTransition(0, 0);
        });
    }

    private void userInfo() {
        byte[] bytes = Base64.decode(pref.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void getToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnSuccessListener(this::updateToken);
    }

    private void updateToken(String token) {
        FirebaseFirestore firebase = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firebase.collection(Constants.KEY_COLLECTION_USERS).document(pref.getString(Constants.KEY_USER_ID));
    }

    private void logOut() {
        showToast("Logged Out");
        FirebaseFirestore firebase = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firebase.collection(Constants.KEY_COLLECTION_USERS).document(pref.getString(Constants.KEY_USER_ID));
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(unused -> {
                    pref.clear();
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> showToast("Unable to log out"));
    }
}