package com.example.litteray.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.litteray.R;
import com.example.litteray.databinding.ActivityLinkBinding;
import com.example.litteray.utilities.Constants;
import com.example.litteray.utilities.Manager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class LinkActivity extends BaseActivity {

    private ActivityLinkBinding binding;
    private Manager pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link);
        binding = ActivityLinkBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        pref = new Manager(getApplicationContext());

        setListeners();
        getLink();
    }

    private void setListeners() {
        binding.submitButton.setOnClickListener(v ->{
                linkUp();
        });
        binding.goBack.setOnClickListener(v -> onBackPressed());
    }

    private void getLink() {
        FirebaseFirestore firebase = FirebaseFirestore.getInstance();
        DocumentReference user = firebase.collection(Constants.KEY_COLLECTION_USERS).document(pref.getString(Constants.KEY_USER_ID));
        binding.twitterInput.setText(pref.getString(Constants.KEY_TWITTER));
        binding.openSeaInput.setText(pref.getString(Constants.KEY_OPENSEA));
        binding.websiteInput.setText(pref.getString(Constants.KEY_WEBSITE));
        binding.instagramInput.setText(pref.getString(Constants.KEY_INSTAGRAM));
        binding.profileType.setText(pref.getString(Constants.KEY_PROFILE_TYPE));
    }

    private void linkUp() {
        FirebaseFirestore firebase = FirebaseFirestore.getInstance();
        DocumentReference user = firebase.collection(Constants.KEY_COLLECTION_USERS).document(pref.getString(Constants.KEY_USER_ID));
        if (binding.profileType.length() > 20) {
            showToast("Too long profile type");
        } else {
            user.update(Constants.KEY_TWITTER, binding.twitterInput.getText().toString());
            user.update(Constants.KEY_OPENSEA, binding.openSeaInput.getText().toString());
            user.update(Constants.KEY_WEBSITE, binding.websiteInput.getText().toString());
            user.update(Constants.KEY_INSTAGRAM, binding.instagramInput.getText().toString());
            user.update(Constants.KEY_PROFILE_TYPE, binding.profileType.getText().toString());
            pref.putBoolean(Constants.KEY_IS_SIGNED_IN, false);
            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void getToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnSuccessListener(this::updateToken);
    }

    private void updateToken(String token) {
        FirebaseFirestore firebase = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firebase.collection(Constants.KEY_COLLECTION_USERS).document(pref.getString(Constants.KEY_USER_ID));
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}