package com.cs206.cs206_g2t7fe;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.widget.Toolbar;

import com.cs206.cs206_g2t7fe.ui.CustomLoading;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.cs206.cs206_g2t7fe.databinding.ActivityVenueRecommendationsBinding;

public class VenueRecommendations extends AppCompatActivity {

    Button confirmButton;
    ImageButton generateButton;

    private ActivityVenueRecommendationsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityVenueRecommendationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().hide();

        confirmButton = (Button) findViewById(R.id.completeButton);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmation();
            }
        });

        generateButton = (ImageButton) findViewById(R.id.generateAgain);
        final CustomLoading customLoading = new CustomLoading(VenueRecommendations.this);

        generateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                customLoading.startLoadingDialog();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    // Replace with backend code
                    @Override
                    public void run() {
                        customLoading.dismissDialog();
                    }
                }, 5000);
            }
        });

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_venue_recommendations);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    public void confirmation(){
        Intent intent = new Intent(this, EventCreationConfirmationPage.class);
        startActivity(intent);
    }


}