package com.jatinsinghroha.firebaseintro;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;

import androidx.annotation.NonNull;

public class FCMService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d("FCMToken", s);
    }
}
