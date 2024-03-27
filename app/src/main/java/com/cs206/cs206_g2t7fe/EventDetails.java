package com.cs206.cs206_g2t7fe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.cs206.cs206_g2t7fe.databinding.ActivityEventDetailsBinding;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EventDetails extends AppCompatActivity {

    ImageButton backButton;

    private ActivityEventDetailsBinding binding;

    // creating a variable for our
    // Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;

    LinearLayout contentLayout;

    String eventID;

    HashMap<String, String> eventMap;

    interface MyCallback {
        HashMap<String, String> onCallback(HashMap<String, String> value);
    }

    private void getEventDetails (String eventID, MyCallback myCallback ){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                HashMap<String, String> internalHashMap = new HashMap<>();
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                    for (DataSnapshot grandChildSnapShot: childSnapshot.getChildren()){
                        if (grandChildSnapShot.getKey().equals("eventDate")){

                            Date date = new Date(grandChildSnapShot.getValue(Long.class));
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                            String formattedDate = sdf.format(date);

                            internalHashMap.put("eventDate", formattedDate);
                        }else{
                            internalHashMap.put(grandChildSnapShot.getKey(), grandChildSnapShot.getValue().toString());
                        }
                    }
                }
                myCallback.onCallback(internalHashMap);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });
    }

    public void setContentLayout(HashMap<String, String> eventMap){
        for (Map.Entry<String, String> entry : eventMap.entrySet()) {

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, // This makes it take up full width
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );



            if (entry.getKey().equals("eventName")){
                TextView eventNameTextView = new TextView(this);
                eventNameTextView.setText(entry.getValue());
                eventNameTextView.setGravity(Gravity.CENTER);
                eventNameTextView.setLayoutParams(params);
                // Set text size in SP (scaled pixels)
                eventNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                eventNameTextView.setTypeface(eventNameTextView.getTypeface(), Typeface.BOLD);
                eventNameTextView.setTextColor(Color.parseColor("#006400"));
                contentLayout.addView(eventNameTextView);

                TextView staticTextView1 = new TextView(this);
                staticTextView1.setText("Places You Are Visiting");
                staticTextView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
                staticTextView1.setTypeface(eventNameTextView.getTypeface(), Typeface.BOLD);
                staticTextView1.setTextColor(Color.BLACK);

                LinearLayout.LayoutParams layoutParamsForStatic = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                // Set rules to LayoutParams
                layoutParamsForStatic.gravity = Gravity.CENTER;

                // Add LayoutParams to TextView
                staticTextView1.setLayoutParams(layoutParamsForStatic);

                // Add TextView to your LinearLayout
                contentLayout.addView(staticTextView1);

                TextView staticTextView2 = new TextView(this);
                staticTextView2.setText("Who Is Coming Along");
                staticTextView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
                staticTextView2.setTypeface(eventNameTextView.getTypeface(), Typeface.BOLD);
                staticTextView2.setTextColor(Color.BLACK);

                // Add LayoutParams to TextView
                staticTextView2.setLayoutParams(layoutParamsForStatic);

                // Add TextView to your LinearLayout
                contentLayout.addView(staticTextView2);

                //Get The Base Root Group
                ConstraintLayout root = findViewById(R.id.container);

                // Create a new LinearLayout
                LinearLayout linearLayout = new LinearLayout(this);

                // Set some layout parameters
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                );

                linearLayout.setLayoutParams(layoutParams);

                // Set orientation of LinearLayout
                linearLayout.setOrientation(LinearLayout.VERTICAL);

                // Set an id to LinearLayout
                linearLayout.setId(View.generateViewId());

                // Add LinearLayout to the root view
                // Please replace root with your root ViewGroup instance
                root.addView(linearLayout);

            } else if (entry.getKey().equals("eventID")) {
                continue;

            } else{
                System.out.println("Hello");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEventDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().hide();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_event_details);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        FragmentManager fragmentManager = getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.nav_host_fragment_activity_event_details);
        if (navHostFragment != null) {
            fragmentManager.beginTransaction().hide(navHostFragment).commit();
            // Use this to show it again
            // fragmentManager.beginTransaction().show(navHostFragment).commit();
        }

        contentLayout = findViewById(R.id.linearLayout);

        // below line is used to get the
        // instance of our Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference("UserEvents");

        SharedPreferences sharedPreferences2 = getSharedPreferences("SharedPref",MODE_PRIVATE);

        String userID= sharedPreferences2.getString("userID", null);

        System.out.println("User ID In Event: " + userID);

        if (getIntent().hasExtra("eventID")) {
            eventID = getIntent().getStringExtra("eventID");  // return the data associated with the "KEY"
        }

        getEventDetails(eventID, new MyCallback() {
            @Override
            public HashMap<String, String> onCallback(HashMap<String, String> value) {
                System.out.println("Inside Event Function");
                eventMap = value;
                setContentLayout(eventMap);
                return null;
            }
        });

        backButton = (ImageButton) findViewById(R.id.backButtonEventDetails);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMainPage(userID);
            }
        });
    }


    public void backToMainPage(String userID){
        Intent intent = new Intent(this, LandingPage.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

}