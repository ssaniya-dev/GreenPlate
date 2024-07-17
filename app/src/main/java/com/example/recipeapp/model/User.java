package com.example.recipeapp.model;

public class User {
    private static User instance; // Singleton instance
    private String uid;
    private String email;
    private double height;
    private double weight;
    private String gender;

    private User(String uid, String email, double height, double weight, String gender) {
        this.uid = uid;
        this.email = email;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
    }

    public static User getInstance(String uid, String email,
                                   double height, double weight, String gender) {
        if (instance == null) {
            instance = new User(uid, email, height, weight, gender);
        }
        return instance;
    }
}
