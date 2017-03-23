package com.dumindudulanga.cwb;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Tile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ListViewActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener
{

    private DatabaseReference myRef;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        buildGoogleApiClient();

        final ArrayList<TileDetail> stationDetails = new ArrayList<TileDetail>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("CarWashBay");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> iterator = dataSnapshot.getChildren();

                for (DataSnapshot r : iterator){

                    double latitude = Double.parseDouble(r.child("locationGlatitude").getValue().toString());
                    double longitude = Double.parseDouble(r.child("locationGlongitude").getValue().toString());

                    Location thisLocation = new Location("A");
                    thisLocation.setLatitude(latitude);
                    thisLocation.setLongitude(longitude);

                    String hasWater = r.child("hasWater").getValue().toString();
                    String hasVacuum = r.child("hasVacuum").getValue().toString();
                    String hasJet = r.child("hasJet").getValue().toString();
                    String stationName = r.child("stName").getValue().toString();
                    String stationAddress = r.child("blkNo").getValue().toString();
                    Float distance = mLastLocation.distanceTo(thisLocation);
                    String noOfLots = r.child("availableBays").getValue().toString();

                    stationDetails.add(new TileDetail(r.getKey(),hasWater, hasVacuum, hasJet, stationName,
                            stationAddress, distance, noOfLots));

                }

                Collections.sort(stationDetails, new Comparator<TileDetail>() {
                    @Override
                    public int compare(TileDetail T1, TileDetail T2) {
                        return Math.round(T1.getDistance()) - Math.round(T2.getDistance()); // Ascending
                    }

                });

                CustomAdapter itemsAdapter = new CustomAdapter(ListViewActivity.this,stationDetails);

                ListView listView = (ListView)findViewById(R.id.list_view);
                listView.setAdapter(itemsAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getBaseContext(), DescriptionActivity.class);
                        intent.putExtra("ObjectID",stationDetails.get(position).getObjectID());
                        intent.putExtra("StationName",stationDetails.get(position).getStationName());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (ContextCompat.checkSelfPermission(ListViewActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        } else {
            Toast.makeText(ListViewActivity.this, "Location Permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i("Location", "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i("Location", "Connection suspended");
        mGoogleApiClient.connect();
    }

}

