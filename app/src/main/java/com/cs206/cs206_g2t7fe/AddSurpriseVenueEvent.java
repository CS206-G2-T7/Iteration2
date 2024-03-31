package com.cs206.cs206_g2t7fe;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.cs206.cs206_g2t7fe.databinding.ActivityAddSurpriseVenueEventBinding;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.Random;

public class AddSurpriseVenueEvent extends AppCompatActivity {

    private ActivityAddSurpriseVenueEventBinding binding;

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

    DatabaseReference databaseVenueReference;

    // creating a variable for
    // our object class
    UserEvents userEvents;

    private Calendar selectedDate = Calendar.getInstance();

    ArrayList<String> venueArray;

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
        databaseVenueReference = firebaseDatabase.getReference("venueLocation");

        if (getIntent().hasExtra("venueID")) {
            String venueID = getIntent().getStringExtra("venueID");
            getVenueInformation(venueID, new MyCallback() {
                @Override
                public ArrayList<String> onCallback(ArrayList<String> value) {
                    venueArray = value;
                    return null;
                }
            });
        }

        if (databaseVenueReference == null) {
            System.out.println("databaseVenueReference is null");
        }

        // initializing our object
        // class variable.
        userEvents = new UserEvents();

        // Storing data into SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("SharedPref",MODE_PRIVATE);

        String userID= sharedPreferences.getString("userID", null);

        System.out.println("This is the create event userID " + userID);

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
                        AddSurpriseVenueEvent.this,
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
                    Toast.makeText(AddSurpriseVenueEvent.this, "Please add some date.", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(event_name[0])) {
                    // if the text fields are empty
                    // then show the below message.
                    Toast.makeText(AddSurpriseVenueEvent.this, "Please add the event data.", Toast.LENGTH_SHORT).show();
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
        userEvents.setEventLocation(venueArray);

        System.out.println(userEvents.getEventID());

        System.out.println(userEvents.toString());

        databaseReference.child(randomString).setValue(userEvents)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        Toast.makeText(AddSurpriseVenueEvent.this, "Data added", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        Toast.makeText(AddSurpriseVenueEvent.this, "Fail to add data " + e, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public interface MyCallback {
        ArrayList<String> onCallback(ArrayList<String> value);
    }

    public void getVenueInformation(String venueID, MyCallback myCallback){
        System.out.println("This is the venue id" + venueID);
        databaseVenueReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                ArrayList<String> internal = new ArrayList<>();
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    System.out.println("This is the venuesss id " + dataSnapshot1.getKey());
                    if (dataSnapshot1.getKey().equals(venueID)){
                        for (DataSnapshot dataSnapshot2: dataSnapshot1.getChildren()){
                            internal.add(Objects.requireNonNull(dataSnapshot2.getValue()).toString());
                        }
                    }
                }
                myCallback.onCallback(internal);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });
    }

    public void addEventLocation(){
        Intent intent = new Intent(this, EventCreationConfirmationPage.class);
        startActivity(intent);
    }
}