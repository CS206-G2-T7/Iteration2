package com.cs206.cs206_g2t7fe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.cs206.cs206_g2t7fe.databinding.ActivityLandingPageBinding;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class LandingPage extends AppCompatActivity {
    ImageButton button;
    AppCompatButton newEventButton, surpriseMeButton;

    private ActivityLandingPageBinding binding;

    Spinner locationSelect;

    String userEmail = "";

    // creating a variable for our
    // Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference, databaseReferenceEvent;

    interface MyCallback {
        String onCallback(String value);
    }


    private void getdata(String email, MyCallback myCallback){
        databaseReference.addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                int found = 0;
                String userID = "";
                Map<String, Object> postValues = new HashMap<String,Object>();
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {

                    postValues.put(childSnapshot.getKey(),childSnapshot.getValue());

                    userID = childSnapshot.getKey();

                    for (DataSnapshot grandChildSnapshot: childSnapshot.getChildren()) {
                        if (grandChildSnapshot.getValue().equals(email)) {
                            found = 1;
                            break;
                        }
                    }
                    if (found == 1){
                        break;
                    }
                }
                if (found == 1) {
                    myCallback.onCallback(userID);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                System.out.println("Error");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLandingPageBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        // below line is used to get the
        // instance of our Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("UserInformation");

        databaseReferenceEvent = firebaseDatabase.getReference("UserEvents");

        button = (ImageButton) findViewById(R.id.imageButton1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEventDetails();
            }
        });


        newEventButton = (AppCompatButton) findViewById(R.id.button6);
        newEventButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                createNewEvent();
            }
        });

        surpriseMeButton = (AppCompatButton) findViewById(R.id.surpriseMe);
        surpriseMeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openSupriseMePage();
            }
        });

        locationSelect = findViewById(R.id.locationSelect);
        ArrayAdapter<String> mrtList = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Mrtlocations.getMrtList());
        //set the spinners adapter to the previously created one.
        locationSelect.setAdapter(mrtList);
        locationSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                location = mrtList.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                location = mrtList.getItem(0);
            }
        });

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().hide();

        if (getIntent().hasExtra("Email")) {
            userEmail = getIntent().getStringExtra("Email");  // return the data associated with the "KEY"
        }

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_landing_page);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        FragmentManager fragmentManager = getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.nav_host_fragment_activity_landing_page);
        if (navHostFragment != null) {
            fragmentManager.beginTransaction().hide(navHostFragment).commit();
            // Use this to show it again
            // fragmentManager.beginTransaction().show(navHostFragment).commit();
        }

        // Storing data into SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("SharedPref",MODE_PRIVATE);

        // Creating an Editor object to edit(write to the file)
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        getdata(userEmail, new MyCallback() {
            @Override
            public String onCallback(String value) {
                myEdit.putString("userID", value);
                return "";
            }
        });
        myEdit.commit();

        String userID= sharedPreferences.getString("userID", null);
        System.out.println("This is the userID: " + userID);

    }

    public void openEventDetails(){
        Intent intent = new Intent(this, EventDetails.class);
        startActivity(intent);
    }

    // test method
    public void openQuizPage() {
        Intent intent = new Intent(this, QuizFirstPage.class);
        startActivity(intent);
    }

    public void createNewEvent() {
        Intent intent = new Intent(this, CreateEvent.class);
        startActivity(intent);
    }

    public void openSupriseMePage(){
        Intent intent = new Intent(this, SurpriseMePage.class);
        intent.putExtra("location", location);
        startActivity(intent);
    }
}