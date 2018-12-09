package com.example.jmugyenyi.mychat.utils;

import com.google.firebase.auth.FirebaseAuth;

public class ContextClass {

    ContextInterface user;
    public ContextClass(ContextInterface user){
    this.user = user;
    }

    public void getHouses(FirebaseAuth authenticatedUser){
        user.getAssociatedHouses(authenticatedUser);
    }
}
