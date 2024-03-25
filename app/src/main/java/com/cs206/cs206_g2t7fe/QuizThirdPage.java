package com.cs206.cs206_g2t7fe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.widget.AppCompatButton;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.cs206.cs206_g2t7fe.databinding.ActivityQuizFirstPageBinding;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuizThirdPage extends AppCompatActivity {

    //    private AppBarConfiguration appBarConfiguration;
    //    private ActivityQuizFirstPageBinding binding;

    ArrayList<String>currentArray = new ArrayList<>();
    ArrayList<String>outputArray = new ArrayList<>();

    // creating a variable for our
    // Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;

    String userID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_third_page);

        // below line is used to get the
        // instance of our FIrebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("UserInformation");

        // Storing data into SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("SharedPref",MODE_PRIVATE);

        userID= sharedPreferences.getString("userID", null);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ArrayList<String> previousArray = extras.getStringArrayList("q2Ans");
            //The key argument here must match that used in the other activity
            currentArray = previousArray;
        }

        // determinate progress
        ProgressBar bar = findViewById(R.id.progressBar);
        AppCompatButton next = findViewById(R.id.quiz_lets_go);

        // set up options buttons and next button
        setUpButtons(next);
    }



    public void handlePreferences(){
        for (int i=0; i<currentArray.size()-1; i++){
            String currentString = currentArray.get(i);
            String[] parts = currentString.split("/");
            String lastPart = parts[parts.length - 1];
            if (lastPart.toLowerCase().contains("qn2")){
                if (lastPart.equals("quiz_qn2_op1")){
                    System.out.println("1");
                    outputArray.add("Casual Dining");
                } else if (lastPart.equals("quiz_qn2_op2")) {
                    System.out.println("2");
                    outputArray.add("Fine Dining");
                } else if (lastPart.equals("quiz_qn2_op3")) {
                    System.out.println("3");
                    outputArray.add("Ethnic Cuisine");
                } else if (lastPart.equals("quiz_qn2_op4")) {
                    System.out.println("4");
                    outputArray.add("Vegetarian Cuisine");
                } else if (lastPart.equals("quiz_qn2_op5")) {
                    System.out.println("5");
                    outputArray.add("Fast Food");
                } else {
                    System.out.println("Error");
                }
            }
        }
        updateUserPreference(userID);
    }

    public void updateUserPreference(String userID){
        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                Map<String, Object> postValues = new HashMap<String,Object>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    postValues.put(snapshot.getKey(),snapshot.getValue());
                }
                postValues.put("initialPreference", outputArray);
                postValues.put("quizDone",Boolean.TRUE);
                databaseReference.child(userID).updateChildren(postValues);
                Toast.makeText(QuizThirdPage.this, "Data Added", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                Toast.makeText(QuizThirdPage.this, "Fail to add data ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // sets "lets go" button to landing page
    public void openLandingPage() {
        Intent intent = new Intent(this, LandingPage.class);
        System.out.println(currentArray);
        handlePreferences();
        startActivity(intent);
    }

    // sets up quiz option buttons
    public void setUpButtons(AppCompatButton next) {
        // retrieve option buttons
        AppCompatButton option1 = findViewById(R.id.quiz_qn3_op1);
        AppCompatButton option2 = findViewById(R.id.quiz_qn3_op2);
        AppCompatButton option3 = findViewById(R.id.quiz_qn3_op3);

        // change colours of options when clicked
        option1.setOnClickListener(colourChangeListener(option1));
        option2.setOnClickListener(colourChangeListener(option2));
        option3.setOnClickListener(colourChangeListener(option3));

        // set up "next" button
        setUpLetsGoButton(next);
    }


    // 2 main functions for the "next" button
    public void setUpLetsGoButton(Button next) {

        // set default state
        next.setEnabled(false);

        // navigates to next quiz page
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open the next quiz page
                openLandingPage();
            }
        });
    }

    // change button colour to green when clicked
    public View.OnClickListener colourChangeListener (Button b) {
        // return this variable
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.setBackgroundResource(R.drawable.button_background);

                currentArray.add(getResources().getResourceName(b.getId()));

                // when any options buttons is clicked, the "next" button will be enabled
                AppCompatButton next = (AppCompatButton) findViewById(R.id.quiz_lets_go);
                next.setEnabled(true);
                next.setBackgroundResource(R.drawable.button_background);

            }
        };
    }

    // shows new quiz progress in progress bar
    public void moveProgressBar(ProgressBar bar) {
        bar.setVisibility(View.VISIBLE);
        bar.incrementProgressBy(33);
    }
//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_quiz_first_page);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
}