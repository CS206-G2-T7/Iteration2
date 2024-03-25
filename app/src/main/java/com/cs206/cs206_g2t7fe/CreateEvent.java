package com.cs206.cs206_g2t7fe;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.Random;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.cs206.cs206_g2t7fe.databinding.ActivityCreateEventBinding;

import java.util.Calendar;
import java.util.Random;

public class CreateEvent extends AppCompatActivity {

    AppCompatButton submitButton;

    private EditText Event_Name;

    private Button pickDateBtn;
    private TextView selectedDateTV, Event_Date;


    private AppBarConfiguration appBarConfiguration;
//    private ActivityCreateEventBinding binding;

    // creating a variable for our
    // Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;

    // creating a variable for
    // our object class
    UserEvents userEvents;

    private Calendar selectedDate = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        String date_out;

        // initializing our edittext and button
        Event_Name = findViewById(R.id.event_name);
        Event_Date = selectedDateTV;
        // below line is used to get the
        // instance of our FIrebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("UserEvents");

        // initializing our object
        // class variable.
        userEvents = new UserEvents();

        // Storing data into SharedPreferences
        SharedPreferences sharedPreferences = this.getSharedPreferences("SharedPref",MODE_PRIVATE);

        String userID= sharedPreferences.getString("userID", null);

        // on below line we are initializing our variables.
        pickDateBtn = findViewById(R.id.idBtnPickDate);
        selectedDateTV = findViewById(R.id.idTVSelectedDate);

        // on below line we are adding click listener for our pick date button
        pickDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting
                // the instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);


                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        CreateEvent.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                selectedDate.set(year, monthOfYear, dayOfMonth);
                                // on below line we are setting date to our text view.
                                selectedDateTV.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }

        });

        final String[] event_name = new String[1];
        Event_Name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                event_name[0] = s.toString();
            }
        });


        submitButton = (AppCompatButton) findViewById(R.id.btn_create_event);
        submitButton.setOnClickListener(new View.OnClickListener(){
            // getting text from our edittext fields.
            String name = Event_Name.getText().toString();
            String date = selectedDateTV.getText().toString();

            @Override
            public void onClick(View v) {
                // below line is for checking whether the
                // edittext fields are empty or not.
                System.out.println("Name: " + name);
                System.out.println(selectedDate);
                long epochMillis = selectedDate.getTimeInMillis();
                if (epochMillis == 0){
                    Toast.makeText(CreateEvent.this, "Please add some date.", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(event_name[0])) {
                    // if the text fields are empty
                    // then show the below message.
                    Toast.makeText(CreateEvent.this, "Please add the event data.", Toast.LENGTH_SHORT).show();
                }else {
                    // else call the method to add
                    // data to our database.
                    System.out.println("This is the date: " + epochMillis);
                    addDataToFirebase("junjieoi", event_name[0], epochMillis, userID);
                    addEventLocation();
                }
            }
        });
    }

    public String generateRandomString(int length) {
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    private void addDataToFirebase(String username, String eventName, Long dateIn, String userID) {

        String randomString = generateRandomString(10); // Generates a random string of 10 characters
        System.out.println(randomString);

        userEvents.setEventID(randomString);
        userEvents.setEventName(eventName);
        userEvents.setEventDate(dateIn);
        userEvents.setUserID(userID);

        System.out.println(userEvents.getEventID());

        System.out.println(userEvents.toString());

        databaseReference.child(randomString).setValue(userEvents)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        Toast.makeText(CreateEvent.this, "Data added", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        Toast.makeText(CreateEvent.this, "Fail to add data " + e, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void addEventLocation(){
        Intent intent = new Intent(this, AddNewActivity.class);
        startActivity(intent);
    }
}package com.cs206.cs206_g2t7fe;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import java.util.Random;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.cs206.cs206_g2t7fe.databinding.ActivityCreateEventBinding;

import java.util.Calendar;
import java.util.Random;

public class CreateEvent extends AppCompatActivity {

    AppCompatButton submitButton;

    private EditText Event_Name;

    private Button pickDateBtn;
    private TextView selectedDateTV, Event_Date;


    private AppBarConfiguration appBarConfiguration;
//    private ActivityCreateEventBinding binding;

    // creating a variable for our
    // Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;

    // creating a variable for
    // our object class
    UserEvents userEvents;

    private Calendar selectedDate = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        String date_out;

        // initializing our edittext and button
        Event_Name = findViewById(R.id.event_name);
        Event_Date = selectedDateTV;
        // below line is used to get the
        // instance of our FIrebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("UserEvents");

        // initializing our object
        // class variable.
        userEvents = new UserEvents();

        // on below line we are initializing our variables.
        pickDateBtn = findViewById(R.id.idBtnPickDate);
        selectedDateTV = findViewById(R.id.idTVSelectedDate);

        // on below line we are adding click listener for our pick date button
        pickDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting
                // the instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);


                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        CreateEvent.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                selectedDate.set(year, monthOfYear, dayOfMonth);
                                // on below line we are setting date to our text view.
                                selectedDateTV.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }

        });

        final String[] event_name = new String[1];
        Event_Name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                event_name[0] = s.toString();
            }
        });


        submitButton = (AppCompatButton) findViewById(R.id.btn_create_event);
        submitButton.setOnClickListener(new View.OnClickListener(){
            // getting text from our edittext fields.
            String name = Event_Name.getText().toString();
            String date = selectedDateTV.getText().toString();

            @Override
            public void onClick(View v) {
                // below line is for checking whether the
                // edittext fields are empty or not.
                System.out.println("Name: " + name);
                System.out.println(selectedDate);
                long epochMillis = selectedDate.getTimeInMillis();
                if (epochMillis == 0){
                    Toast.makeText(CreateEvent.this, "Please add some date.", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(event_name[0])) {
                    // if the text fields are empty
                    // then show the below message.
                    Toast.makeText(CreateEvent.this, "Please add the event data.", Toast.LENGTH_SHORT).show();
                }else {
                    // else call the method to add
                    // data to our database.
                    System.out.println("This is the date: " + epochMillis);
                    addDataToFirebase("junjieoi", event_name[0], epochMillis);
                    addEventLocation();
                }
            }
        });
    }

    public String generateRandomString(int length) {
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    private void addDataToFirebase(String username, String eventName, Long dateIn) {

        String randomString = generateRandomString(10); // Generates a random string of 10 characters
        System.out.println(randomString);

        userEvents.setEventID(randomString);
        userEvents.setEventName(eventName);
        userEvents.setEventDate(dateIn);
        userEvents.setUserID(username);

        System.out.println(userEvents.getEventID());

        System.out.println(userEvents.toString());

        databaseReference.setValue(userEvents)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        Toast.makeText(CreateEvent.this, "Data added", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        Toast.makeText(CreateEvent.this, "Fail to add data " + e, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void addEventLocation(){
        Intent intent = new Intent(this, AddNewActivity.class);
        startActivity(intent);
    }
}