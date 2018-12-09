package com.example.jmugyenyi.mychat.PaymentProcess;

//This is an interface used to return the feedback about payments from the server to the payment activity
public interface Response {
    void processFinish(String output);
}
