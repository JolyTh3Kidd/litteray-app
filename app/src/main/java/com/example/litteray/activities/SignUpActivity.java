package com.example.litteray.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

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

import com.example.litteray.R;
import com.example.litteray.databinding.ActivitySignInBinding;
import com.example.litteray.databinding.ActivitySignUpBinding;
import com.example.litteray.utilities.Constants;
import com.example.litteray.utilities.Manager;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private Manager pref;
    private String encodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        pref = new Manager(getApplicationContext());
        setListeners();
    }

    private void setListeners() {
        binding.signUpButton.setOnClickListener(v ->{
            if (isValidSignUpDetails()) {
                signUp();
            }
        });
        binding.layoutImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            chooseImage.launch(intent);
        });
    }

    private void signUp() {
        FirebaseFirestore firebase = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_LOGIN, binding.loginInput.getText().toString().toLowerCase(Locale.ROOT));
        user.put(Constants.KEY_EMAIL, binding.emailInput.getText().toString());
        user.put(Constants.KEY_PASSWORD, binding.passwordInput.getText().toString());
        user.put(Constants.KEY_USERNAME, binding.usernameInput.getText().toString());
        user.put(Constants.KEY_IMAGE, encodedImage);
        firebase.collection(Constants.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    pref.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                    pref.putString(Constants.KEY_USER_ID, documentReference.getId());
                    pref.putString(Constants.KEY_LOGIN, binding.loginInput.getText().toString().toLowerCase(Locale.ROOT));
                    pref.putString(Constants.KEY_EMAIL, binding.emailInput.getText().toString());
                    pref.putString(Constants.KEY_USERNAME, binding.usernameInput.getText().toString());
                    pref.putString(Constants.KEY_PASSWORD, binding.passwordInput.getText().toString());
                    pref.putString(Constants.KEY_IMAGE, encodedImage);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .addOnFailureListener(exception -> {
                    showToast(exception.getMessage());
                });
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
                            binding.profilePic.setImageBitmap(bitmap);
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

    private Boolean isValidSignUpDetails() {
        if (binding.loginInput.getText().toString().trim().isEmpty()) {
            showToast("Enter login");
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
        } else if (binding.usernameInput.getText().toString().trim().isEmpty()) {
            showToast("Enter username");
            return false;
        } else if(encodedImage == null) {
            showToast("Select Profile Picture");
            return false;
        } else {
            return true;
        }
    }
}