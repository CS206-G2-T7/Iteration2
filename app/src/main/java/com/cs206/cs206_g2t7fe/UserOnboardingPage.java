package com.cs206.cs206_g2t7fe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;

import com.cs206.cs206_g2t7fe.databinding.ActivityUserOnboardingPageBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class UserOnboardingPage extends AppCompatActivity {

    Button nextButton;

    private ActivityUserOnboardingPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUserOnboardingPageBinding.inflate(getLayoutInflater());
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
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_user_onboarding_page);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        FragmentManager fragmentManager = getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.nav_host_fragment_activity_landing_page);
        if (navHostFragment != null) {
            fragmentManager.beginTransaction().hide(navHostFragment).commit();
            // Use this to show it again
            // fragmentManager.beginTransaction().show(navHostFragment).commit();
        }

        nextButton = (Button) findViewById(R.id.nextQuizButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQuizPage();
            }
        });
    }

    public void openQuizPage() {
        Intent intent = new Intent(this, QuizFirstPage.class);
        startActivity(intent);
    }



}