package com.cs206.cs206_g2t7fe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class LoginPage extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        // Initialize UI elements
        editTextUsername = findViewById(R.id.username);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.loginButton);

        // Set a click listener for the login button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve entered username and password
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();

                // Implement authentication logic here
                if (username.equals("jj") && password.equals("123")) {
                    // Successful login
                    openLandingPage();
                } else {
                    // Failed login
                    Toast.makeText(LoginPage.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void openLandingPage() {
        Intent intent = new Intent(this, LandingPage.class);
        startActivity(intent);
    }
}