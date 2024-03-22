package com.cs206.cs206_g2t7fe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.cs206.cs206_g2t7fe.databinding.ActivityLandingPageBinding;

public class LandingPage extends AppCompatActivity {
    ImageButton button;
    AppCompatButton newEventButton, surpriseMeButton;

    private ActivityLandingPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLandingPageBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

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


        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().hide();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_landing_page);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    public void openEventDetails(){
        Intent intent = new Intent(this, CreateEvent.class);
        startActivity(intent);
    }

    public void createNewEvent() {
        Intent intent = new Intent(this, CreateEvent.class);
        startActivity(intent);
    }

    public void openSupriseMePage(){
        Intent intent = new Intent(this, SurpriseMePage.class);
        startActivity(intent);
    }
}