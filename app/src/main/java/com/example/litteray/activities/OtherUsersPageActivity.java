package com.example.litteray.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;

import com.example.litteray.adapters.ChatAdapters;
import com.example.litteray.adapters.RecentConversationsAdapter;
import com.example.litteray.databinding.ActivityChatBinding;
import com.example.litteray.databinding.ActivityOtherUsersPageBinding;
import com.example.litteray.models.ChatMessage;
import com.example.litteray.models.User;
import com.example.litteray.utilities.Constants;
import com.example.litteray.utilities.Manager;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OtherUsersPageActivity extends BaseActivity {

    private ActivityOtherUsersPageBinding binding;
    private User receiverUser;
    private List<ChatMessage> chatMessages;
    private ChatAdapters chatAdapters;
    private Manager pref;
    private FirebaseFirestore firebase;
    private String encodedImage;
    private List<ChatMessage> conversations;
    private RecentConversationsAdapter conversationsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtherUsersPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        pref = new Manager(getApplicationContext());

        userInfo();
        setListeners();
        init();
    }

    private void init() {
        pref = new Manager(getApplicationContext());
        chatMessages = new ArrayList<>();
        chatAdapters = new ChatAdapters(
                chatMessages,
                pref.getString(Constants.KEY_USER_ID)
        );
        firebase = FirebaseFirestore.getInstance();
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
        binding.footerMenuProfile.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), UserPageActivity.class));
        });
    }

    private void userInfo() {
        byte[] bytes = Base64.decode(pref.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        binding.profileImage.setImageBitmap(bitmap);
    }
}