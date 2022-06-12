package com.example.litteray.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.SearchView;

import com.example.litteray.R;
import com.example.litteray.adapters.UsersAdapters;
import com.example.litteray.databinding.ActivityUsersBinding;
import com.example.litteray.listeners.UserListener;
import com.example.litteray.models.User;
import com.example.litteray.utilities.Constants;
import com.example.litteray.utilities.Manager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends BaseActivity implements UserListener {

    private ActivityUsersBinding binding;
    private Manager pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        pref = new Manager(getApplicationContext());
        getUsers();
        setListeners();
    }

    private void setListeners() {
        binding.goBack.setOnClickListener(v -> onBackPressed());
    }

    private void getUsers() {
        load(true);
        FirebaseFirestore firebase = FirebaseFirestore.getInstance();
        firebase.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    load(false);
                    String currentUserId = pref.getString(Constants.KEY_USER_ID);
                    if(task.isSuccessful() && task.getResult() != null) {
                        List<User> users = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            if(currentUserId.equals(queryDocumentSnapshot.getId())) {
                                continue;
                            }
                            User user = new User();
                            user.username = queryDocumentSnapshot.getString(Constants.KEY_USERNAME);
                            user.login = queryDocumentSnapshot.getString(Constants.KEY_LOGIN);
                            user.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                            user.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                            user.id = queryDocumentSnapshot.getId();
                            users.add(user);
                        }
                        if (users.size() > 0) {
                            UsersAdapters usersAdapters = new UsersAdapters(users, this);
                            binding.usersRecycler.setAdapter(usersAdapters);
                            binding.usersRecycler.setVisibility(View.VISIBLE);
                        } else {
                            errorMessage();
                        }
                    } else {
                        errorMessage();
                    }
                });
    }

    private void errorMessage() {
        binding.errorMessage.setText(String.format("Couldn't load users list"));
        binding.errorMessage.setVisibility(View.VISIBLE);
    }

    private void load(Boolean loading) {
        if (loading) {
            binding.contactsLoad.setVisibility(View.VISIBLE);
        } else {
            binding.contactsLoad.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onUserClicked(User user) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
        finish();
    }
}