package com.cs206.cs206_g2t7fe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.*;
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

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class LandingPage extends AppCompatActivity {
    ImageButton button;
    AppCompatButton newEventButton, surpriseMeButton, LogoutButton;

    private ActivityLandingPageBinding binding;

    private DatabaseReference mDatabase;
    private ListView mListView;
    private ArrayAdapter<String> adapter;

    private ArrayList<String> keyList = new ArrayList<>();

    private ArrayList<EventsDisplay> eventList = new ArrayList<>();

    String userID = null;

    EventAdapter eventAdapter;

    Spinner locationSelect;
    String location;
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

    private void getName(String userIDInput, MyCallback myCallback){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                int found = 0;
                String firstName = "";
                String userID = "";
                Map<String, Object> postValues = new HashMap<String,Object>();
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {

                    postValues.put(childSnapshot.getKey(),childSnapshot.getValue());

                    userID = childSnapshot.getKey();
                    System.out.println("This is the userID: " + userID + " " + userIDInput);

                    if (userID.equals(userIDInput)){
                        System.out.println("Hello");
                        for (DataSnapshot grandChildSnapshot: childSnapshot.getChildren()) {
                            System.out.println(grandChildSnapshot.getKey());
                            if (grandChildSnapshot.getKey().equals("firstName")) {
                                firstName = grandChildSnapshot.getValue().toString();
                                System.out.println("FirstName: " + firstName);
                                found = 1;
                                break;
                            }
                        }
                    }

                    if (found == 1){
                        break;
                    }
                }
                if (found == 1) {
                    System.out.println(firstName);
                    myCallback.onCallback(firstName);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });
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
                    System.out.println("This is the current userID " + userID);
                    System.out.println("This is the email "+ email);

                    for (DataSnapshot grandChildSnapshot: childSnapshot.getChildren()) {
                        System.out.println(grandChildSnapshot.getValue());
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

    private String getTimeOfDay(){
        LocalTime currentTime = LocalTime.now();

        if(currentTime.isBefore(LocalTime.NOON)) {
            System.out.println("Good Morning");
            return "Good Morning ";
        } else if(currentTime.isBefore(LocalTime.of(19, 0))) { // 5 PM comparable to civil afternoon in western culture
            System.out.println("Good Afternoon");
            return "Good Afternoon: ";
        } else {
            System.out.println("Good Evening");
            return "Good Evening: ";
        }
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

        /*
        button = (ImageButton) findViewById(R.id.imageButton1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEventDetails();
            }
        });
        */

        mListView = findViewById(R.id.list_view);
        //adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, keyList);
        eventAdapter = new EventAdapter(this, eventList);
        mListView.setAdapter(eventAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // get the associated Event object
                EventsDisplay clickedEvent = eventList.get(position);

                // get the unique id
                String uniqueId = clickedEvent.getEventID();

                openEventDetails(uniqueId);

                // You can now use this uniqueId to pull event information from the backend database
            }
        });

        newEventButton = (AppCompatButton) findViewById(R.id.button6);
        newEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewEvent();
            }
        });

        surpriseMeButton = (AppCompatButton) findViewById(R.id.surpriseMe);
        surpriseMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSupriseMePage();
            }
        });


        LogoutButton = (AppCompatButton) findViewById(R.id.buttonLogout);
        /*
        LogoutButton.setOnClickListener(new View.OnClickListener() {
            locationSelect = (Spinner)findViewById(R.id.locationSelect);
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
        });
        */

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().hide();

        SharedPreferences sharedPref = this.getSharedPreferences("fileName", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();

        if (getIntent().hasExtra("Email")) {
            userEmail = getIntent().getStringExtra("Email");
            getdata(userEmail, new MyCallback() {
                @Override
                public String onCallback(String value) {

                    editor.putString("userID", userID);
                    editor.commit();
                    return null;
                }
            });// return the data associated with the "KEY"
        }
        if (getIntent().hasExtra("userID")) {
            userID = getIntent().getStringExtra("userID");  // return the data associated with the "KEY"
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
        SharedPreferences sharedPreferences = getSharedPreferences("SharedPref", MODE_PRIVATE);

        // Creating an Editor object to edit(write to the file)
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        if (userID == null){
            SharedPreferences sharedPreferences1a = getSharedPreferences("SharedPref", MODE_PRIVATE);
            userID = sharedPreferences1a.getString("userID", null);
        }

        getName(userID, new MyCallback() {
            @Override
            public String onCallback(String value) {
                System.out.println(value);
                myEdit.putString("firstName", value);

                System.out.println("Outside");

                myEdit.apply();

                SharedPreferences sharedPreferences2 = getSharedPreferences("SharedPref", MODE_PRIVATE);

                String userFirstName = sharedPreferences2.getString("firstName", null);

                // Find the EditText field
                TextView userGreeting = findViewById(R.id.UserGreeting);

                String timeOfDay = getTimeOfDay();

                // Set new text to the EditText field
                userGreeting.setText(timeOfDay + userFirstName + "! These are your events at a glance.");

                ValueEventListener postListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        keyList.clear();
                        Map<String, Object> postValues = new HashMap<>();
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            postValues.put(childSnapshot.getKey(), childSnapshot.getValue());
                            //System.out.println(childSnapshot.getKey());
                            for (DataSnapshot grandChildSnapshot : childSnapshot.getChildren()) {
                                if (grandChildSnapshot.getKey().equals("userID")) {
                                    System.out.println(grandChildSnapshot.getValue() + " " + userID);
                                    if (grandChildSnapshot.getValue().equals(userID)) {
                                        System.out.println("Inside Here");
                                        String eventName = childSnapshot.child("eventName").getValue(String.class);
                                        String eventID = childSnapshot.child("eventID").getValue(String.class);
                                        Long eventDate = childSnapshot.child("eventDate").getValue(Long.class);
                                        String eventHost = childSnapshot.child("userID").getValue(String.class);

                                        EventsDisplay eventsDisplay = new EventsDisplay(eventName, eventID, eventDate, eventHost);
                                        eventList.add(eventsDisplay);
                                        keyList.add(eventName);
                                    }
                                }
                            }
                        }

                        eventAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "loadData:onCancelled", databaseError.toException());
                    }
                };

                databaseReferenceEvent.addValueEventListener(postListener);

                return "";
            }
        });
    }

    public void openEventDetails(String eventID){
        Intent intent = new Intent(this, EventDetails.class);
        intent.putExtra("eventID", eventID);
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

    public void logout(){
        SharedPreferences sharedPreferences = getSharedPreferences("YourSharedPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Clear all data
        editor.clear();

        // Commit the changes
        editor.apply();

        Intent intent = new Intent(this, LoginPage.class);
        startActivity(intent);
    }
}