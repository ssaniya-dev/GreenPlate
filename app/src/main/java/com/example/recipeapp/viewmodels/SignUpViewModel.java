package com.example.recipeapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.recipeapp.model.Ingredient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SignUpViewModel extends ViewModel {
    private final MutableLiveData<Boolean> signUpSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> signUpError = new MutableLiveData<>();

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    public LiveData<Boolean> getSignUpSuccess() {
        return signUpSuccess;
    }

    public LiveData<String> getSignUpError() {
        return signUpError;
    }

    public void signUp(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            signUpError.postValue("Email and password cannot be empty.");
            return;
        }

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser loggedInUser = auth.getCurrentUser();
                        Map<String, Object> userData = new HashMap<>();
                        List<Ingredient> l = new ArrayList<>();
                        List<Ingredient> l2 = new ArrayList<>();
                        userData.put("email", email);
                        userData.put("height", "");
                        userData.put("weight", "");
                        userData.put("gender", "");
                        Ingredient i1 = new Ingredient("Tomato", 13, 200, "10-04-2024", false);
                        Ingredient i2 = new Ingredient("Potato", 10, 300, "10-19-2024", false);
                        l.add(i1);
                        l.add(i2);
                        Ingredient i3 = new Ingredient("Please enter in an ingredient",
                                0, 0, "", false);
                        l2.add(i3);
                        FirebaseDatabase database = FirebaseDatabase.getInstance("https://recipeapp-1fba1-default-rtdb.firebaseio.com/");
                        DatabaseReference db = database.getReference();
                        if (loggedInUser != null) {
                            db.child("users").child(loggedInUser.getUid())
                                    .setValue(userData)
                                    .addOnCompleteListener(userTask -> {
                                        if (userTask.isSuccessful()) {
                                            signUpSuccess.postValue(true);
                                        } else {
                                            signUpError.postValue(Objects.requireNonNull(
                                                    userTask.getException()).getMessage());
                                        }
                                    });
                            db.child("shoppinglist").child(loggedInUser.getUid())
                                    .setValue(l)
                                    .addOnCompleteListener(userTask -> {
                                        if (userTask.isSuccessful()) {
                                            signUpSuccess.postValue(true);
                                        } else {
                                            signUpError.postValue(Objects.requireNonNull(
                                                    userTask.getException()).getMessage());
                                        }
                                    });
                            db.child("pantry").child(loggedInUser.getUid())
                                    .setValue(l2)
                                    .addOnCompleteListener(userTask -> {
                                        if (userTask.isSuccessful()) {
                                            signUpSuccess.postValue(true);
                                        } else {
                                            signUpError.postValue(Objects.requireNonNull(
                                                    userTask.getException()).getMessage());
                                        }
                                    });
                        }
                    } else {
                        signUpError.postValue(Objects.requireNonNull(
                                task.getException()).getMessage());
                    }
                });
    }
}
