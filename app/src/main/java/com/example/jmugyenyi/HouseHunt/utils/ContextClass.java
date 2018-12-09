package com.example.jmugyenyi.HouseHunt.utils;

import com.google.firebase.auth.FirebaseAuth;

//This is a context of the strategy pattern
public class ContextClass {

    ContextInterface user;

    public ContextClass(ContextInterface user) {
        this.user = user;
    }

    public void getHouses(FirebaseAuth authenticatedUser) {
        user.getAssociatedHouses(authenticatedUser);
    }
}
