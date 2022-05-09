package com.example.litteray.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.litteray.R;
import com.example.litteray.databinding.ActivityUserInfoBinding;
import com.example.litteray.models.User;
import com.example.litteray.utilities.Constants;
import com.example.litteray.utilities.Manager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class UserInfoActivity extends BaseActivity {

    private ActivityUserInfoBinding binding;
    private Manager pref;
    private String encodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        binding = ActivityUserInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        pref = new Manager(getApplicationContext());

        setListeners();
        getInfo();
    }

    private void setListeners() {
        binding.submitButton.setOnClickListener(v ->{
            if (isValidChangedInfoDetails()) {
                changeInfo();
            }
        });
        binding.goBack.setOnClickListener(v -> onBackPressed());
        binding.layoutImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            chooseImage.launch(intent);
        });
    }

    private void getInfo() {
        byte[] bytes = Base64.decode(pref.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        FirebaseFirestore firebase = FirebaseFirestore.getInstance();
        DocumentReference user = firebase.collection(Constants.KEY_COLLECTION_USERS).document(pref.getString(Constants.KEY_USER_ID));
        binding.loginInput.setText(pref.getString(Constants.KEY_LOGIN));
        binding.usernameInput.setText(pref.getString(Constants.KEY_USERNAME));
        binding.emailInput.setText(pref.getString(Constants.KEY_EMAIL));
        binding.passwordInput.setText(pref.getString(Constants.KEY_PASSWORD));
        binding.changeProfileImage.setImageBitmap(bitmap);
    }

    private void changeInfo() {
        FirebaseFirestore firebase = FirebaseFirestore.getInstance();
        DocumentReference user = firebase.collection(Constants.KEY_COLLECTION_USERS).document(pref.getString(Constants.KEY_USER_ID));
        user.update(Constants.KEY_LOGIN, binding.loginInput.getText().toString());
        user.update(Constants.KEY_USERNAME, binding.usernameInput.getText().toString());
        user.update(Constants.KEY_EMAIL, binding.emailInput.getText().toString());
        user.update(Constants.KEY_PASSWORD, binding.passwordInput.getText().toString());
        user.update(Constants.KEY_IMAGE, encodedImage);
        pref.putBoolean(Constants.KEY_IS_SIGNED_IN, false);
        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private String encodeImage(Bitmap bitmap) {
        int previewWidth = 600;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private final ActivityResultLauncher<Intent> chooseImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.changeProfileImage.setImageBitmap(bitmap);
                            binding.textChoose.setVisibility(View.GONE);
                            encodedImage = encodeImage(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }



    private Boolean isValidChangedInfoDetails() {
        if(encodedImage == null) {
            showToast("You didn't choose the image");
            return false;
        } else if (binding.loginInput.getText().toString().trim().isEmpty()) {
            showToast("Enter login");
            return false;
        } else if (binding.usernameInput.getText().toString().trim().isEmpty()) {
            showToast("Enter username");
            return false;
        } else if (binding.emailInput.getText().toString().trim().isEmpty()) {
            showToast("Enter e-mail");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailInput.getText().toString()).matches()) {
            showToast("E-mail is invalid");
            return false;
        } else if (binding.passwordInput.getText().toString().trim().isEmpty()) {
            showToast("Enter password");
            return false;
        } else {
            return true;
        }
    }
}