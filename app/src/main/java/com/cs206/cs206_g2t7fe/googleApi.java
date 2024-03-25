package com.cs206.cs206_g2t7fe;

import androidx.annotation.Nullable;

import java.io.IOException;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.NearbySearchRequest;
import com.google.maps.errors.ApiException;
import com.google.maps.PlacesApi;
import com.google.maps.TextSearchRequest;

import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.Photo;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;

public class googleApi {
    private final String apiKey = "AIzaSyAasPMUIG47TMP5Yj6nR19sfiX4g2nXfNw";
    private final GeoApiContext context = new GeoApiContext.Builder()
            .apiKey(apiKey)
            .build();

    private String nextPage;
    // Invoke .shutdown() after your application is done making requests

    //needa add String address as input
    public LatLng getLatLng(String address) throws ApiException, InterruptedException, IOException{
        GeocodingResult[] results =  GeocodingApi.geocode(this.context, address).await();
//        LatLng latlngloc = results[0].geometry.location;
        //below is some gson nonsense from what i can tell, need the latlng above
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        System.out.println(gson.toJson(results[0].geometry.location));
        return results[0].geometry.location;
    }

    public PlacesSearchResult[] searchForPlace(String query, double Lat, double Lng) throws ApiException, IOException, InterruptedException{
        LatLng location = new LatLng(Lat, Lng);
        // Gson gson = new GsonBuilder().setPrettyPrinting().create();
        TextSearchRequest request = PlacesApi.textSearchQuery(this.context, query, location);
        request.radius(500);
        PlacesSearchResponse result = request.await();
        nextPage = result.nextPageToken;
        return result.results;
    }

    public void getDirections(String origin, String destination) throws ApiException, IOException, InterruptedException{
        DirectionsResult result = DirectionsApi.getDirections(this.context, origin, destination).await();
        DirectionsRoute routes[] = result.routes;
    }

    public PlacesSearchResult[] searchNextPage() throws ApiException, IOException, InterruptedException{
        // Gson gson = new GsonBuilder().setPrettyPrinting().create();
        NearbySearchRequest request = PlacesApi.nearbySearchNextPage(this.context, nextPage);
        PlacesSearchResponse result = request.await();
        nextPage = result.nextPageToken;
        return result.results;
    }

    public String buildImageUrl(Photo photo){
        String str = "https://maps.googleapis.com/maps/api/place/photo?"
                + "maxwidth=" + photo.width
                + "&photo_reference=" + photo.photoReference
                + "&key="+apiKey;
        return  str;
    }

    public void shutdown() throws IOException{
        this.context.close();
    }
}
