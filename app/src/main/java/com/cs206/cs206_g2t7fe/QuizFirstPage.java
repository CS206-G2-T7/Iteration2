package com.cs206.cs206_g2t7fe;

import android.content.Intent;
import android.os.Bundle;

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

public class QuizFirstPage extends AppCompatActivity {

//    private AppBarConfiguration appBarConfiguration;
//    private ActivityQuizFirstPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_first_page);

        // determinate progress
        ProgressBar bar = findViewById(R.id.progressBar);
        AppCompatButton next = findViewById(R.id.quiz_next);

        // set up options buttons and next button
        setUpButtons(next);
    }

    // sets "next" button to open next quiz page
    public void openNextQuizPage() {
        Intent intent = new Intent(this, QuizSecondPage.class);
        startActivity(intent);
    }

    // sets up quiz option buttons
    public void setUpButtons(AppCompatButton next) {
        // retrieve option buttons
        AppCompatButton option1 = findViewById(R.id.quiz_qn1_op1);
        AppCompatButton option2 = findViewById(R.id.quiz_qn1_op2);
        AppCompatButton option3 = findViewById(R.id.quiz_qn1_op3);
        AppCompatButton option4 = findViewById(R.id.quiz_qn1_op4);
        AppCompatButton option5 = findViewById(R.id.quiz_qn1_op5);
        AppCompatButton option6 = findViewById(R.id.quiz_qn1_op6);

        // change colours of options when clicked
        option1.setOnClickListener(colourChangeListener(option1));
        option2.setOnClickListener(colourChangeListener(option2));
        option3.setOnClickListener(colourChangeListener(option3));
        option4.setOnClickListener(colourChangeListener(option4));
        option5.setOnClickListener(colourChangeListener(option5));
        option6.setOnClickListener(colourChangeListener(option6));

        // set up "next" button
        setUpNextButton(next);
    }

    // 2 main functions for the "next" button
    public void setUpNextButton(Button next) {

        // set default state
        next.setEnabled(false);

        // navigates to next quiz page
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open the next quiz page
                openNextQuizPage();
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


                // when any options buttons is clicked, the "next" button will be enabled
                AppCompatButton next = (AppCompatButton) findViewById(R.id.quiz_next);
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