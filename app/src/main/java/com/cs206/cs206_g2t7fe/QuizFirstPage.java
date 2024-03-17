package com.cs206.cs206_g2t7fe;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.cs206.cs206_g2t7fe.databinding.ActivityQuizFirstPageBinding;

public class QuizFirstPage extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityQuizFirstPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_first_page);

        // determinate progress
        Button nextButton = findViewById(R.id.quiz_next);
        ProgressBar bar = findViewById(R.id.progressBar);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNextQuizPage();
                moveProgressBar(bar);
            }
        });
    }

    public void openNextQuizPage() {
        Intent intent = new Intent(this, QuizSecondPage.class);
        startActivity(intent);
    }

    // shows new quiz progress in progress bar
    public void moveProgressBar(ProgressBar bar) {
        bar.setVisibility(View.VISIBLE);
        bar.incrementProgressBy(33);
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_quiz_first_page);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}