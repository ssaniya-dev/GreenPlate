package com.example.recipeapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

public class LoginViewModel extends ViewModel {
    private final MutableLiveData<Boolean> loginSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> loginError = new MutableLiveData<>();

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    public LiveData<Boolean> getLoginSuccess() {
        return loginSuccess;
    }

    public LiveData<String> getLoginError() {
        return loginError;
    }

    public void login(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            loginError.postValue("Please enter your email and password.");
            return;
        }

        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> loginSuccess.postValue(true))
                .addOnFailureListener(e -> loginError.postValue(e.getMessage()));
    }

    public boolean notEmptyEmail(String email) {
        return !email.isEmpty();
    }

    public boolean notEmptyPassword(String pass) {
        return !pass.isEmpty();
    }

    public boolean validPassLength(String pass) {
        return !(pass.length() < 6);
    }
}