package com.jatinsinghroha.firebaseintro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class HomeScreen extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference usersDBRef;
    private FirebaseAuth mFirebaseAuth;
    FirebaseUser user;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private final static int REQUEST_CODE_SIGN_IN = 111;
    String userName = "";

    FloatingActionButton contactsFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        contactsFAB = findViewById(R.id.contactsFAB);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        usersDBRef = mFirebaseDatabase.getReference().child("users");

        mAuthStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();

            if (user == null) {
                //Re-direct to Login
                Toast.makeText(HomeScreen.this, "Please Login", Toast.LENGTH_LONG).show();

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
                                                new AuthUI.IdpConfig.PhoneBuilder().setDefaultCountryIso("IN").setAllowedCountries(whiteListedCountries).build())
                                ).build(), REQUEST_CODE_SIGN_IN
                );

            } else {

                this.user = user;

                initViews();

                DatabaseReference userAccountDB = usersDBRef.child(user.getUid());

                userAccountDB.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            User currentUser = snapshot.getValue(User.class);
                            if (currentUser.getUserName() != null || !currentUser.getUserName().isEmpty()) {
                                Toast.makeText(HomeScreen.this, currentUser.getUserName(), Toast.LENGTH_SHORT).show();
                            } else {
                                askForUserName();
                            }
                        } else {
                            askForUserName();
                            Toast.makeText(HomeScreen.this, "New User", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        Toast.makeText(HomeScreen.this, "Database Error occured", Toast.LENGTH_SHORT).show();
                    }
                });

            }

        };

        mFirebaseAuth.addAuthStateListener(mAuthStateListener);

        contactsFAB.setOnClickListener(v -> startActivity(new Intent(HomeScreen.this, ContactsScreen.class)));
    }

    private void askForUserName() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);

        builder.setTitle("Enter your name");
        builder.setCancelable(false);

        final EditText nameET = new EditText(this);
        nameET.setInputType(InputType.TYPE_CLASS_TEXT);

        builder.setPositiveButton("SAVE", (dialog, which) -> {
            String name = nameET.getText().toString();
            name = name.trim();

            if (name.length() < 2) {
                askForUserName();
            } else {
                userName = name;
                saveUserDetails(name);
            }
        });

        builder.setView(nameET);

        builder.show();
    }

    private void saveUserDetails(String name) {
        User object = new User(user.getUid(), name, user.getPhoneNumber());

        usersDBRef.child(user.getUid()).setValue(object);
    }

    private void initViews() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(HomeScreen.this, "Login Successful", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(HomeScreen.this, "Login Failed", Toast.LENGTH_LONG).show();
            }
        }
    }

}