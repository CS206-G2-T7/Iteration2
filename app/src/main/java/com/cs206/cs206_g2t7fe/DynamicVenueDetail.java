package com.cs206.cs206_g2t7fe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.cs206.cs206_g2t7fe.databinding.ActivityDynamicVenueDetailBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class DynamicVenueDetail extends AppCompatActivity {

    private ActivityDynamicVenueDetailBinding binding;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDynamicVenueDetailBinding.inflate(getLayoutInflater());
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
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_dynamic_venue_detail);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        FragmentManager fragmentManager = getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.nav_host_fragment_activity_dynamic_venue_detail);
        if (navHostFragment != null) {
            fragmentManager.beginTransaction().hide(navHostFragment).commit();
            // Use this to show it again
            // fragmentManager.beginTransaction().show(navHostFragment).commit();
        }

        SharedPreferences sharedPreferences2 = getSharedPreferences("SharedPref",MODE_PRIVATE);

        String userID= sharedPreferences2.getString("userID", null);

        String venueName = "";
        String venueAddress = "";

        if (getIntent().hasExtra("venueDetails")) {
            System.out.println("This is the hashmap");
            HashMap<String, String> internal = (HashMap<String, String>) getIntent().getSerializableExtra("venueDetails");
            System.out.println(internal.toString());
            String input = internal.get("eventVenue");
            String[] array = input.substring(1, input.length() - 1).split(", ");
            ArrayList<String> list = new ArrayList<>(Arrays.asList(array));
            System.out.println(list);
            venueName = list.get(1);
            venueAddress = list.get(2)+list.get(3);
        }

        TextView venueTextView = (TextView) findViewById(R.id.venue_name);
        venueTextView.setText(venueName);

        TextView addressTextView = (TextView) findViewById(R.id.venue_address_information);
        addressTextView.setText(venueAddress);

        backButton = (ImageButton) findViewById(R.id.backButtonDynamicVenueDisplay);
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