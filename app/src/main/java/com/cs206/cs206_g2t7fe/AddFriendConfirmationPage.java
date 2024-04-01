package com.cs206.cs206_g2t7fe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.cs206.cs206_g2t7fe.databinding.ActivityAddFriendConfirmationPageBinding;

public class AddFriendConfirmationPage extends AppCompatActivity {

private ActivityAddFriendConfirmationPageBinding binding;
Button doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddFriendConfirmationPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().hide();

        SharedPreferences sharedPreferences2 = getSharedPreferences("SharedPref",MODE_PRIVATE);
        String userID= sharedPreferences2.getString("userID", null);
        System.out.println("User ID In Event: " + userID);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_add_friend_confirmation_page);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        FragmentManager fragmentManager = getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.nav_host_fragment_activity_add_friends_empty);
        if (navHostFragment != null) {
            fragmentManager.beginTransaction().hide(navHostFragment).commit();
            // Use this to show it again
            // fragmentManager.beginTransaction().show(navHostFragment).commit();
        }

        Button doneButton = (Button) findViewById(R.id.done);

        doneButton.setOnClickListener(new View.OnClickListener() {
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