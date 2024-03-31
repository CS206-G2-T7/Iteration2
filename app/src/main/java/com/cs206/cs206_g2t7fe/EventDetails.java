package com.cs206.cs206_g2t7fe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
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
import java.util.*;

public class EventDetails extends AppCompatActivity {

    ImageButton backButton;

    Button myButton, inviteButton;

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

    private ListView mListView;

    venueAdapter venueAdapter;


    int placesLinearLayoutID = 0;

    int inviteesLinearLayoutID = 0;


    interface MyCallback {
        HashMap<String, String> onCallback(HashMap<String, String> value);
    }

    private void getEventDetails (String eventID, MyCallback myCallback ){
        databaseReference.addValueEventListener(new ValueEventListener() {
            int matched = 0;
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                HashMap<String, String> internalHashMap = new HashMap<>();
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                    System.out.println("This is the current event: " + childSnapshot.getValue());
                    System.out.println(childSnapshot.getKey());
                    if (childSnapshot.getKey().equals(eventID)){
                        matched = 1;
                        for (DataSnapshot grandChildSnapShot: childSnapshot.getChildren()){
                            System.out.println(grandChildSnapShot.getValue());
                            System.out.println("Matched");

                            if (grandChildSnapShot.getKey().equals("eventDate")){

                                Date date = new Date(grandChildSnapShot.getValue(Long.class));
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                                String formattedDate = sdf.format(date);

                                internalHashMap.put("eventDate", formattedDate);
                            } else{
                                internalHashMap.put(grandChildSnapShot.getKey(), grandChildSnapShot.getValue().toString());
                            }
                        }
                    }
                    System.out.println(internalHashMap.toString());
                    if (matched == 1){
                        break;
                    }
                }
                if (matched == 1){
                    myCallback.onCallback(internalHashMap);
                }
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

            myButton = (Button) findViewById(R.id.InviteButton);
            myButton.setVisibility(View.INVISIBLE);

            System.out.println("These are the keys:  " + entry.getKey());
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


            } else if (entry.getKey().equals("eventID")) {
                continue;

            } else if (entry.getKey().equals("eventVenue")) {

                System.out.println("Inside Venues: ");
                int existingViewId = placesLinearLayoutID;

                // Instantiate the ListView
                ListView listView = new ListView(this);

                listView.setId(View.generateViewId());

                // Get the parent view and existing view
                ViewGroup parent = (ViewGroup) findViewById(R.id.container);
                View existingView = findViewById(existingViewId);

                // Calculate the position to add the ListView
                int position = parent.indexOfChild(existingView) + 1;

                // Add the ListView to the parent layout
                contentLayout.addView(listView, position);

                String listString = entry.getValue();
                List<String> list = Arrays.asList(listString.substring(1, listString.length() - 1).split(", "));

                ArrayList<String> arrayList = new ArrayList<>(list);

                System.out.println(arrayList);

                for (int i=1; i<arrayList.size(); i++){
                    System.out.println(arrayList.get(i));
                }

                System.out.println("This is the event venues data " + entry.getValue());

                venueInformation venueInformation;

                if (arrayList.size()%2==0){
                    venueInformation = new venueInformation(arrayList.get(0), arrayList.get(1), arrayList.get(2));
                }else{
                    venueInformation = new venueInformation(arrayList.get(0), arrayList.get(1), arrayList.get(3));
                }

                ArrayList<venueInformation> venueInfo = new ArrayList<>();
                venueInfo.add(venueInformation);

                mListView = findViewById(R.id.places_list_view);
                //adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, keyList);
                venueAdapter = new venueAdapter(this, venueInfo);
                mListView.setAdapter(venueAdapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // get the associated Event object
                        venueInformation venue_info = venueInfo.get(position);

                        // get the unique id
                        String uniqueId = venue_info.getLocationID();

                        toVenueInformationPage(eventMap);

                        // You can now use this uniqueId to pull event information from the backend database
                    }
                });

            } else if (entry.getKey().equals("eventInvitees")){
                System.out.println("Invitees Found");
            } else{
                System.out.println("Hello");
                myButton = (Button) findViewById(R.id.InviteButton);
                myButton.setVisibility(View.VISIBLE);
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

        inviteButton = (Button) findViewById(R.id.InviteButton);
        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toInvitePage(eventID);
            }
        });
    }


    public void backToMainPage(String userID){
        Intent intent = new Intent(this, LandingPage.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

    public void toInvitePage(String eventID){
        Intent intent = new Intent(this, AddFriendsEmpty.class);
        intent.putExtra("eventID", eventID);
        startActivity(intent);
    }

    public void toVenueInformationPage (HashMap<String, String> map){
        Intent intent = new Intent(this, DynamicVenueDetail.class);
        intent.putExtra("venueDetails", map);
        startActivity(intent);
    }

}