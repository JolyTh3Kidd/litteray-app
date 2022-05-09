package com.example.litteray.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.litteray.databinding.ActivitySignInBinding;
import com.example.litteray.utilities.Constants;
import com.example.litteray.utilities.Manager;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding binding;
    private Manager pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        pref = new Manager(getApplicationContext());
        if (pref.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        setListeners();
    }

    private void setListeners() {
        binding.createAccount.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class)));
        binding.signInButton.setOnClickListener(v ->{
            if (signInInfo()) {
                signIn();
            }
        });
        binding.forgotPassword.setOnClickListener(v -> {
            showToast("Just relax and try to remember it :)");
        });
    }

    private void signIn() {
        FirebaseFirestore firebase = FirebaseFirestore.getInstance();
        firebase.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_LOGIN, binding.loginInput.getText().toString().toLowerCase(Locale.ROOT))
                .whereEqualTo(Constants.KEY_PASSWORD, binding.passwordInput.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null
                            && task.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        pref.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                        pref.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                        pref.putString(Constants.KEY_EMAIL, documentSnapshot.getString(Constants.KEY_EMAIL));
                        pref.putString(Constants.KEY_PROFILE_TYPE, documentSnapshot.getString(Constants.KEY_PROFILE_TYPE));
                        pref.putString(Constants.KEY_LOGIN, documentSnapshot.getString(Constants.KEY_LOGIN));
                        pref.putString(Constants.KEY_PASSWORD, documentSnapshot.getString(Constants.KEY_PASSWORD));
                        pref.putString(Constants.KEY_USERNAME, documentSnapshot.getString(Constants.KEY_USERNAME));
                        pref.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));
                        pref.putString(Constants.KEY_TWITTER, documentSnapshot.getString(Constants.KEY_TWITTER));
                        pref.putString(Constants.KEY_OPENSEA, documentSnapshot.getString(Constants.KEY_OPENSEA));
                        pref.putString(Constants.KEY_WEBSITE, documentSnapshot.getString(Constants.KEY_WEBSITE));
                        pref.putString(Constants.KEY_INSTAGRAM, documentSnapshot.getString(Constants.KEY_INSTAGRAM));
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        showToast("Wrong login or password");
                    }
                });
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private Boolean signInInfo() {
        if(binding.loginInput.getText().toString().trim().isEmpty()) {
            showToast("Enter login");
            return false;
        } else if (binding.passwordInput.getText().toString().trim().isEmpty()) {
            showToast("Enter password");
            return false;
        } else {
            return true;
        }
    }
}