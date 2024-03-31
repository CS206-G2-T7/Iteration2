package com.cs206.cs206_g2t7fe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

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

import com.cs206.cs206_g2t7fe.databinding.ActivityAddFriendsEmptyBinding;

public class AddFriendsEmpty extends AppCompatActivity {

    private ActivityAddFriendsEmptyBinding binding;
    ImageButton backButton;
    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddFriendsEmptyBinding.inflate(getLayoutInflater());
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
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_add_friends_empty);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        FragmentManager fragmentManager = getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.nav_host_fragment_activity_add_friends_empty);
        if (navHostFragment != null) {
            fragmentManager.beginTransaction().hide(navHostFragment).commit();
            // Use this to show it again
            // fragmentManager.beginTransaction().show(navHostFragment).commit();
        }

        setUpButtons();

        backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMainPage();
            }
        });
//
//        nextButton= (Button) findViewById(R.id.nextButton);
//        nextButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addFriend();
//            }
//        });
    }

    public void setUpButtons() {
        Button person1 = findViewById(R.id.person1);
        Button person2 = findViewById(R.id.person2);

        person1.setOnClickListener(colourChangeListener(person1));
        person2.setOnClickListener(colourChangeListener(person2));

        setUpNextButton();
    }

    public View.OnClickListener colourChangeListener (Button b) {
        // return this variable
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                b.setBackgroundResource(R.drawable.button_background);

                Button next = (Button) findViewById(R.id.nextButton);
                next.setEnabled(true);
                next.setBackgroundResource(R.drawable.button_background);
            }
        };
    }

    public void setUpNextButton() {
        Button next = findViewById(R.id.nextButton);

        // set default state
        next.setEnabled(false);

        // navigates to next quiz page
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFriend();
            }
        });
    }

    public void backToMainPage(){
        Intent intent = new Intent(this, LandingPage.class);
        startActivity(intent);
    }

    public void addFriend(){
        Intent intent = new Intent(this, AcknowledgeAdd.class);
        startActivity(intent);
    }
}