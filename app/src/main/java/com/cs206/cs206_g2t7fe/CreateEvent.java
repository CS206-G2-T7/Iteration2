package com.cs206.cs206_g2t7fe;

import android.content.Intent;
import android.os.Bundle;

import android.widget.ImageButton;
import androidx.appcompat.widget.AppCompatButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.cs206.cs206_g2t7fe.databinding.ActivityCreateEventBinding;

public class CreateEvent extends AppCompatActivity {

    AppCompatButton submitButton;

    private AppBarConfiguration appBarConfiguration;
//    private ActivityCreateEventBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    public void addEventLocation(){
        Intent intent = new Intent(this, AddNewActivity.class);
        startActivity(intent);
    }
}