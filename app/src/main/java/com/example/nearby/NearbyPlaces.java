package com.example.nearby;

import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class NearbyPlaces extends AsyncTask<Object, String, String > {
    private String googleplaceData, url;
    private GoogleMap mMap;

    @Override
    protected String doInBackground(Object... objects) {
        mMap  = (GoogleMap)objects[0];
        url = (String) objects[1];
        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            googleplaceData = downloadUrl.ReadTheUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return googleplaceData;
    }

    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String,String>> nearByPLacesList = null;
        Data data = new Data();
        nearByPLacesList = data.parse(s);
        displayNearByPLaces(nearByPLacesList);
    }

    private  void displayNearByPLaces(List<HashMap<String,String>> nearByPLacesList){
        for (int i=0; i<nearByPLacesList.size(); i++){
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String,String> googleNearByPlaces = nearByPLacesList.get(i);
            String nameofPLace = googleNearByPlaces.get("place_name");
            String vicinity = googleNearByPlaces.get("vicinity");
            double lat = Double.parseDouble(googleNearByPlaces.get("lat"));
            double lng = Double.parseDouble(googleNearByPlaces.get("lng"));
            String reference = googleNearByPlaces.get("reference");
            LatLng latLng = new LatLng(lat,lng);
            markerOptions.position(latLng);
            markerOptions.title(nameofPLace + " : " + "vicinity");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        }
    }
}
