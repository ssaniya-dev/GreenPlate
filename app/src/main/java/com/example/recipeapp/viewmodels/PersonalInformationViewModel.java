package com.example.recipeapp.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class PersonalInformationViewModel extends ViewModel {
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private MutableLiveData<Boolean> isSaveSuccessful = new MutableLiveData<>();
    private MutableLiveData<String> heightData = new MutableLiveData<>();
    private MutableLiveData<String> weightData = new MutableLiveData<>();
    private MutableLiveData<String> genderData = new MutableLiveData<>();
    public boolean validateHeight(String height) {
        return !height.isEmpty();
    }
    public boolean validateWeight(String weight) {
        return !weight.isEmpty();
    }
    public void savePersonalInformation(String email, String height, String weight, String gender) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("height", height);
        userData.put("weight", weight);
        userData.put("gender", gender);

        DatabaseReference db = FirebaseDatabase.getInstance("https://recipeapp-1fba1-default-rtdb.firebaseio.com/")
                .getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            db.child("users").child(user.getUid()).setValue(userData)
                    .addOnSuccessListener(aVoid -> isSaveSuccessful.postValue(true))
                    .addOnFailureListener(e -> isSaveSuccessful.postValue(false));
        }
    }

    public LiveData<Boolean> getIsSaveSuccessful() {
        return isSaveSuccessful;
    }
    public LiveData<String> getHeight() {
        return heightData;
    }
    public LiveData<String> getWeight() {
        return weightData;
    }
    public LiveData<String> getGender() {
        return genderData;
    }

    public void readUserInfo() {
        FirebaseDatabase database = FirebaseDatabase
                .getInstance("https://recipeapp-1fba1-default-rtdb.firebaseio.com/");
        DatabaseReference userRef = database.getReference().child("users/"
                + user.getUid());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                heightData.setValue(snapshot.child("height").getValue(String.class));
                weightData.setValue(snapshot.child("weight").getValue(String.class));
                genderData.setValue(snapshot.child("gender").getValue(String.class));

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error);
            }
        });
    }


}