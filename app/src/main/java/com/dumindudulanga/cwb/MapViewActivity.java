package com.dumindudulanga.cwb;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.nfc.Tag;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapViewActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Bitmap locationIconBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.location);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(locationIconBitmap,100,100,false);
        final BitmapDescriptor locationIcon = BitmapDescriptorFactory.fromBitmap(resizedBitmap);

        LatLng southWest = new LatLng(1.27, 103.67);
        LatLng northEast = new LatLng(1.41, 103.99);
        LatLngBounds SINGAPORE = new LatLngBounds(southWest,northEast);
        // Set the camera to the greatest possible zoom level that includes the bounds
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(SINGAPORE, 0));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("CarWashBay");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> iterator = dataSnapshot.getChildren();

                for (DataSnapshot r : iterator){
                    try {
                        double latitude = Double.parseDouble(r.child("locationGlatitude").getValue().toString());
                        double longitude = Double.parseDouble(r.child("locationGlongitude").getValue().toString());
                        LatLng location = new LatLng(latitude,longitude);

                        TagDetail tagDetail = new TagDetail();

                        tagDetail.setObjectID(r.getKey());
                        tagDetail.setHasWater(r.child("hasWater").getValue().toString());
                        tagDetail.setHasVacuum(r.child("hasVacuum").getValue().toString());
                        tagDetail.setHasJet(r.child("hasJet").getValue().toString());
                        tagDetail.setStationName(r.child("stName").getValue().toString());
                        tagDetail.setStationAddress(r.child("blkNo").getValue().toString());
                        tagDetail.setDistance("N/A");
                        tagDetail.setNoOfLots(r.child("availableBays").getValue().toString());

                        Marker marker =  mMap.addMarker(new MarkerOptions().position(location)
                                .icon(locationIcon));
                        marker.setTag(tagDetail);
                    }
                    catch (NullPointerException e){
                        Log.d("LatLng","Latitude and Longitude not available");
                    }
                }

                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        Intent intent = new Intent(getBaseContext(), DescriptionActivity.class);
                        TagDetail tagDetail =  (TagDetail) marker.getTag();
                        String objectID = tagDetail.getObjectID();
                        intent.putExtra("ObjectID", objectID);
                        startActivity(intent);
                    }
                });

                if (ContextCompat.checkSelfPermission(MapViewActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                } else {
                    Toast.makeText(MapViewActivity.this, "Location Permission not granted", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{

        private final View mWindow;
        private final View mContents;

        CustomInfoWindowAdapter() {
            mWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
            mContents = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            render(marker, mWindow);
            return mWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
            render(marker, mContents);
            return mContents;
        }

        private void render(Marker marker, View view) {
            TagDetail tagDetail = (TagDetail) marker.getTag();

            View waterIndicator = view.findViewById(R.id.water_indicator);
            if(tagDetail.getHasWater().equals("true")){
                waterIndicator.setBackgroundColor(Color.GREEN);
            }
            else{
                waterIndicator.setBackgroundColor(Color.RED);
            }

            View vacuumIndicator = view.findViewById(R.id.vacuum_indicator);
            if(tagDetail.getHasVacuum().equals("true")){
                vacuumIndicator.setBackgroundColor(Color.GREEN);
            }
            else{
                vacuumIndicator.setBackgroundColor(Color.RED);
            }

            View jetIndicator = view.findViewById(R.id.jet_indicator);
            if(tagDetail.getHasJet().equals("true")){
                jetIndicator.setBackgroundColor(Color.GREEN);
            }
            else{
                jetIndicator.setBackgroundColor(Color.RED);
            }

            TextView stationNameTextView = (TextView) view.findViewById(R.id.station_name_text_view);
            TextView stationAddressTextView = (TextView) view.findViewById(R.id.station_address_text_view);
            TextView distanceTextView = (TextView) view.findViewById(R.id.distance_text_view);
            TextView noOfLotsTextView = (TextView) view.findViewById(R.id.lots_text_view);

            stationNameTextView.setText(tagDetail.getStationName());
            stationAddressTextView.setText(tagDetail.getStationAddress());
            distanceTextView.setText(tagDetail.getDistance());
            noOfLotsTextView.setText(tagDetail.getNoOfLots());
        }
    }

    class TagDetail{
        String objectID;
        String hasWater;
        String hasVacuum;
        String hasJet;
        String stationName;
        String stationAddress;
        String distance;
        String noOfLots;

        void setObjectID(String id){
            objectID = id;
        }
        void setHasWater(String mHasWater){
            hasWater = mHasWater;
        }
        void setHasVacuum(String mHasVacuum){
            hasVacuum = mHasVacuum;
        }
        void setHasJet(String mHasJet){
            hasJet = mHasJet;
        }
        void setStationName(String mStationName){
            stationName = mStationName;
        }
        void setStationAddress(String mStationAddress){
            stationAddress = mStationAddress;
        }
         void setDistance(String mDistance){
            distance = mDistance;
        }
        void setNoOfLots(String mNoOfLots){
            noOfLots = mNoOfLots;
        }

        String getObjectID(){
            return objectID;
        }
        String getHasWater(){
            return hasWater;
        }
        String getHasVacuum(){
            return hasVacuum;
        }
        String getHasJet(){
            return hasJet;
        }
        String getStationName(){
            return stationName;
        }
        String getStationAddress(){
            return stationAddress;
        }
        String getDistance(){
            return distance;
        }
        String getNoOfLots(){
            return noOfLots;
        }
    }
}