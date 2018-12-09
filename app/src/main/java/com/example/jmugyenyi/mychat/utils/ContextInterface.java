package com.example.jmugyenyi.mychat.utils;

import com.google.firebase.auth.FirebaseAuth;

//This interface is implemented by the strategy pattern
public interface ContextInterface {

    public void getAssociatedHouses(FirebaseAuth authenticatedUser);
}
