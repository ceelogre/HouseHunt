package com.example.jmugyenyi.mychat;

import android.provider.BaseColumns;

public class UsersCredentialsContract {
    /* Inner class that defines the table contents */
    public static class FeedCredentialsEntry implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String user_email = "user_email";
        public static final String user_password = "user_password";
        public static final String user_status = "user_status";
    }
}
