package com.cs206.cs206_g2t7fe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.content.SharedPreferences;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.cs206.cs206_g2t7fe.databinding.ActivityUserOnboardingPageBinding;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class UserOnboardingPage extends AppCompatActivity {

    Button nextButton;

    private ActivityUserOnboardingPageBinding binding;

    // creating a variable for our
    // Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;

    private EditText firstNameTextView, lastNameTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUserOnboardingPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // below line is used to get the
        // instance of our FIrebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("UserInformation");

        String userID = "";

        firstNameTextView = findViewById(R.id.editTextFirstName);
        lastNameTextView = findViewById(R.id.editTextLastName);

        if (getIntent().hasExtra("userID")) {
            userID = getIntent().getStringExtra("userID");  // return the data associated with the "KEY"
        }

        // Storing data into SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("SharedPref",MODE_PRIVATE);

        // Creating an Editor object to edit(write to the file)
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        myEdit.putString("userID", userID);

        // Once the changes have been made, we need to commit to apply those changes made,
        // otherwise, it will throw an error
        myEdit.commit();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().hide();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_user_onboarding_page);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        FragmentManager fragmentManager = getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.nav_host_fragment_activity_user_onboarding_page);
        if (navHostFragment != null) {
            fragmentManager.beginTransaction().hide(navHostFragment).commit();
            // Use this to show it again
            // fragmentManager.beginTransaction().show(navHostFragment).commit();
        }

        nextButton = (Button) findViewById(R.id.nextQuizButton);
        String finalUserID = userID;
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = firstNameTextView.getText().toString();
                String lastName = lastNameTextView.getText().toString();
                updateUserInformation(finalUserID, firstName, lastName);
                openQuizPage();
            }
        });
    }

    public void updateUserInformation(String userID, String firstName, String lastName){
        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                Map<String, Object> postValues = new HashMap<String,Object>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    postValues.put(snapshot.getKey(),snapshot.getValue());
                }
                postValues.put("firstName", firstName);
                postValues.put("lastName", lastName);
                databaseReference.child(userID).updateChildren(postValues);
                Toast.makeText(UserOnboardingPage.this, "Data Added", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                Toast.makeText(UserOnboardingPage.this, "Fail to add data ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void openQuizPage() {
        Intent intent = new Intent(this, QuizFirstPage.class);
        startActivity(intent);
    }



}