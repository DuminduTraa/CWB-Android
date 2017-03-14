package com.dumindudulanga.cwb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListViewActivity extends AppCompatActivity {
    private DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        final ArrayList<TileDetail> stationDetails = new ArrayList<TileDetail>();
        final ArrayList<String> objectIDs = new ArrayList<String>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("CarWashBay");
        //DatabaseReference carWashRef = myRef.child("CarWashBay");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> iterator = dataSnapshot.getChildren();

                for (DataSnapshot r : iterator){
                    objectIDs.add(r.getKey());
                    String hasWater = r.child("hasWater").getValue().toString();
                    String hasVacuum = r.child("hasVacuum").getValue().toString();
                    String hasJet = r.child("hasJet").getValue().toString();
                    String stationName = r.child("stName").getValue().toString();
                    String stationAddress = r.child("blkNo").getValue().toString();
                    String distance = "N/A";
                    String noOfLots = r.child("availableBays").getValue().toString();

                    stationDetails.add(new TileDetail(hasWater, hasVacuum, hasJet, stationName,
                            stationAddress, distance, noOfLots));

                }

                CustomAdapter itemsAdapter = new CustomAdapter(ListViewActivity.this,stationDetails);

                ListView listView = (ListView)findViewById(R.id.list_view);
                listView.setAdapter(itemsAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getBaseContext(), DescriptionActivity.class);
                        intent.putExtra("ObjectID",objectIDs.get(position));
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
}
