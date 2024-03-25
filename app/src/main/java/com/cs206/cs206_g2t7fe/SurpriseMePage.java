package com.cs206.cs206_g2t7fe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;

import com.cs206.cs206_g2t7fe.ui.CustomLoading;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.cs206.cs206_g2t7fe.databinding.ActivitySurpriseMePageBinding;

import com.google.android.material.navigation.NavigationView;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlacesSearchResult;
import com.google.maps.model.Photo;
import com.google.maps.errors.ApiException;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;


public class SurpriseMePage extends AppCompatActivity {

    googleApi api = new googleApi();
    ImageButton addEventButton;
    ImageButton generateButton;
    TextView locationName;
    TextView locationAddress;
    TextView locationRef;
    ImageView locationImage;
    private ActivitySurpriseMePageBinding binding;
    PlacesSearchResult[] searchResults;

    PlacesSearchResult currentDisplay;

    Photo placePic;
    String placeAddress;
    String placeName;
    String placeRef;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
//            LatLng centralloc = new LatLng();
//            int pricepoint = 0;
//            for (/*number of people in the group*/){
//                LatLng temp = api.getLatLng(/*current person's inserted location*/);
//                centralloc.lat += temp.lat;
//                centralloc.lng += temp.lng;
//                /*pick up user's option*/
//                if(user option == "low"){
//                    pricepoint += 10;
//                }elif(user option == "medium"){
//                    pricepoint += 33;
//                }elif(user option == "high") {
//                    pricepoint += 50;
//                }
//            }
//            centralloc.lat = centralloc.lat/ /*number of people*/;
//            centralloc.lng = centralloc.lng/ /*number of people*/;
//            pricepoint = pricepoint / /*number of people in group*/

            String location = getIntent().getExtras().getString("location");
            Log.v("location", location);
            LatLng centralLoc = api.getLatLng(location);
            //LatLng centralloc = api.getLatLng("simei"/*insert user address here(location)*/);
            //String query = "restaurants around $" + pricepoint;
            //i prefixed the query location for now
            this.searchResults = api.searchForPlace("medium priced restaurant", centralLoc.lat, centralLoc.lng);
            Toast.makeText(getApplicationContext(),
                            "api worked",
                            Toast.LENGTH_LONG)
                    .show();
            this.currentDisplay = searchResults[0];
            if (currentDisplay == null){
                Toast.makeText(getApplicationContext(),
                                "sum ting wong, display is null" ,
                                Toast.LENGTH_LONG)
                        .show();
            }
        }catch (ApiException e){
            Toast.makeText(getApplicationContext(),
                            "apiexception threw a sum ting wong" + e.getMessage(),
                            Toast.LENGTH_LONG)
                    .show();
            Log.v("api error", e.getMessage());
        }catch (IOException e){
            Toast.makeText(getApplicationContext(),
                            "ioexception at api threw a sum ting wong",
                            Toast.LENGTH_LONG)
                    .show();
            Log.v("io error", e.getMessage());
        }catch (InterruptedException e){
            Toast.makeText(getApplicationContext(),
                            "interruptedexception threw a sum ting wong",
                            Toast.LENGTH_LONG)
                    .show();
            Log.v("interrupted error", e.getMessage());
        }
        //idk where is the box where we adding the details of the place so the function is empty need update

        Toast.makeText(getApplicationContext(),
                        "api cleared",
                        Toast.LENGTH_LONG)
                .show();
        Log.v("api work", "api cleared");
        binding = ActivitySurpriseMePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        displayPlace();
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().hide();

        addEventButton = (ImageButton) findViewById(R.id.addEventButton);
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewEvent();
            }
        });

        generateButton = (ImageButton) findViewById(R.id.imageButton4);
        final CustomLoading customLoading = new CustomLoading(SurpriseMePage.this);

        generateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                customLoading.startLoadingDialog();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //so here is where i want to cycle the result array
                        count++;
                        if (count < 20){
                            currentDisplay = searchResults[count];
                            //if place perma closed, find the next one
                            while(currentDisplay.permanentlyClosed == true) {
                                count++;
                                currentDisplay = searchResults[count];
                                if (count == 20){
                                    try {
                                        searchResults = api.searchNextPage();
                                    } catch (Exception e){
                                        Toast.makeText(getApplicationContext(),
                                                        "find new page error " + e.getMessage(),
                                                        Toast.LENGTH_LONG)
                                                .show();
                                    }
                                }
                                //todo fix the out of bounds issue if somehow all 20 are perma closed
                            }

                        }else{
                            count = 0;
                            currentDisplay = searchResults[count];
                        }
                        //idk how to add this in
                        displayPlace();
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
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_surprise_me_page);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        FragmentManager fragmentManager = getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.nav_host_fragment_activity_surprise_me_page);
        if (navHostFragment != null) {
            fragmentManager.beginTransaction().hide(navHostFragment).commit();
            // Use this to show it again
            // fragmentManager.beginTransaction().show(navHostFragment).commit();
        }
    }

    public void addNewEvent(){
        Intent intent = new Intent(this, SurpriseMeConfirmation.class);
        intent.putExtra("placename", placeName);
        intent.putExtra("placeaddress", placeAddress);
        startActivity(intent);
    }

    public void displayPlace() {
        //variables for display
        //need to change the fields to these....IF I CAN EVEN ADD THE FIELDS
        placePic = currentDisplay.photos[0];
        placeAddress = currentDisplay.formattedAddress;
        Log.v("place address", placeAddress);
        placeName = currentDisplay.name;
        Log.v("place name", placeName);
        placeRef = placePic.htmlAttributions[0];

//        String imageurl = api.buildImageUrl(placePic);
        locationImage = findViewById(R.id.placeImage);
//        try {
//            URL url = new URL(imageurl);
//            Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//            locationImage.setImageBitmap(bitmap);
//        } catch (MalformedURLException e) {
//            Toast.makeText(getApplicationContext(),
//                            "MalformedURLException threw a sum ting wong",
//                            Toast.LENGTH_LONG)
//                    .show();
//        } catch (IOException e) {
//            Toast.makeText(getApplicationContext(),
//                            "ioexception at url threw a sum ting wong",
//                            Toast.LENGTH_LONG)
//                    .show();
//        }
        String imageurl = api.buildImageUrl(placePic);


        Picasso.get()
                .load(imageurl)
                .placeholder(android.R.drawable.sym_def_app_icon)
                .error(android.R.drawable.sym_def_app_icon)
                .into(locationImage);
//        InputStream url = null;
//        try {
//            Log.v("image url", imageurl);
//            url = new URL(imageurl).openStream();
//            //Bitmap bitmap = BitmapFactory.decodeStream(url);
//            //locationImage.setImageBitmap(bitmap);
//        } catch (Exception e) {
//            Toast.makeText(getApplicationContext(),
//                            "exception at url threw a sum ting wen wong",
//                            Toast.LENGTH_LONG)
//                    .show();
//
//            //Log.e("error", e.getMessage());
//        }


        locationName = findViewById(R.id.locationNameField);
        locationName.setText(placeName);

        locationAddress = findViewById(R.id.locationAddressField);
        locationAddress.setText(placeAddress);

        locationRef = findViewById(R.id.placeRefField);
        locationRef.setText("");
//        if(placeRef != null) {
//            locationRef.setText(placeRef);
//        }else {
//            locationRef.setText("");
//        }
        float placeRating = currentDisplay.rating;
    }
}