package com.jatinsinghroha.firebaseintro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText messagesET;
    private ImageView sendMessageBtn;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference messagesDBRef;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private final static int REQUEST_CODE_SIGN_IN = 111;
    private boolean sendEnabled = false;

    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messagesET = findViewById(R.id.messageET);
        sendMessageBtn = findViewById(R.id.sendMessage);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        messagesDBRef = mFirebaseDatabase.getReference().child("messages");

        mFirebaseAuth = FirebaseAuth.getInstance();

        mAuthStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();

            if (user == null) {
                //Re-direct to Login
                Toast.makeText(MainActivity.this, "Please Login", Toast.LENGTH_LONG).show();

                List<String> whiteListedCountries = new ArrayList<>();
                whiteListedCountries.add("IN");
                whiteListedCountries.add("US");

                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setIsSmartLockEnabled(false)
                                .setLogo(R.drawable.ic_baseline_chat_24)
                                .setAvailableProviders(
                                        Arrays.asList(
                                                new AuthUI.IdpConfig.GoogleBuilder().build(),
                                                new AuthUI.IdpConfig.PhoneBuilder().setDefaultCountryIso("IN").setAllowedCountries(whiteListedCountries).build(),
                                                new AuthUI.IdpConfig.EmailBuilder().build())
                                ).build(), REQUEST_CODE_SIGN_IN
                );

            } else {
                //User Logged In
                Toast.makeText(MainActivity.this, "User Phone - "+user.getPhoneNumber(), Toast.LENGTH_LONG).show();

                this.user = user;
            }

        };

        mFirebaseAuth.addAuthStateListener(mAuthStateListener);

        messagesET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty())
                    sendEnabled = false;
                else
                    sendEnabled = true;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sendEnabled) {
                    messagesDBRef.push().setValue(
                            new MessageModel(
                                    messagesET.getText().toString(),
                                    user.getUid(),
                                    "Jatin",
                                    null,
                                    System.currentTimeMillis())
                    );
                    sendEnabled = false;
                    messagesET.setText("");
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a message", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SIGN_IN && resultCode == Activity.RESULT_OK) {
            Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
        }
    }
}