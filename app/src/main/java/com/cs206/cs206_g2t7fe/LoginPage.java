package com.cs206.cs206_g2t7fe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;


public class LoginPage extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin, buttonRegister;
    private FirebaseAuth mAuth;

    // creating a variable for our
    // Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        // below line is used to get the
        // instance of our Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("UserInformation");

        mAuth = FirebaseAuth.getInstance();

        // Initialize UI elements
        editTextUsername = findViewById(R.id.username);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.loginButton);
        buttonRegister = findViewById(R.id.registerButton);

        // Set a click listener for the login button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Implement authentication logic here
                loginUserAccount();
            }
        });

        //Set a click listener for the registration button

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSignupPage();
            }
        });

    }

    interface MyCallback {
        void onCallback(Integer value);
    }

    private void getdata(String email, MyCallback myCallback) {

        Query query = databaseReference.orderByChild("userName").equalTo(email);
        query.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    System.out.println("Data Not Found");
                    myCallback.onCallback(0);
                } else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    System.out.println("Data Found");
                    myCallback.onCallback(0);
                }
            }
        });
    }


    private void loginUserAccount()
    {

        // Take the value of two edit texts in Strings
        String email = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        System.out.println(email);
        System.out.println(password);

        // validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter email!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter password!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // signin existing user
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(
                                    @NonNull Task<AuthResult> task)
                            {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),
                                                    "Login successful!!",
                                                    Toast.LENGTH_LONG)
                                            .show();

                                    getdata(email, new MyCallback() {
                                        @Override
                                        public void onCallback(Integer value) {
                                            if (value == 0){
                                                // if sign-in is successful
                                                // intent to home activity
                                                Intent intent
                                                        = new Intent(LoginPage.this,
                                                        LandingPage.class);
                                                startActivity(intent);
                                            }else{
                                                Toast.makeText(getApplicationContext(),
                                                                "Login failed!!",
                                                                Toast.LENGTH_LONG)
                                                        .show();
                                            }
                                        }
                                    });

                                }
                            }
                        });
    }

    public void openSignupPage(){
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

}