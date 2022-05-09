package com.example.litteray.activities;

import android.os.Bundle;

import com.example.litteray.R;
import com.example.litteray.databinding.ActivityAboutUsBinding;

public class AboutUsActivity extends BaseActivity {

    private ActivityAboutUsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        binding = ActivityAboutUsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setListeners();
    }

    private void setListeners() {
        binding.goBack.setOnClickListener(v -> onBackPressed());
    }
}