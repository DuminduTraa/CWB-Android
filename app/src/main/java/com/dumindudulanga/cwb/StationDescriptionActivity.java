package com.dumindudulanga.cwb;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StationDescriptionActivity extends AppCompatActivity {

    public TabHost host;
    public String ObjectID;
    public TextView waterPriceTextView;
    public TextView jetPriceTextView;
    public TextView vacumPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_description);

        host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        setupTab((LinearLayout)findViewById(R.id.tab1), "",1);
        setupTab((LinearLayout)findViewById(R.id.tab2), "",2);
        setupTab((LinearLayout)findViewById(R.id.tab3), "",3);

        waterPriceTextView = (TextView)findViewById(R.id.water_price);
        jetPriceTextView = (TextView)findViewById(R.id.jet_price);
        vacumPriceTextView = (TextView)findViewById(R.id.vaccum_price);

        Intent intent = getIntent();
        ObjectID = intent.getStringExtra("ObjectID");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("CarWashBay").child(ObjectID);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("hasWater").getValue().toString().equals("true")){
                    String waterCents =  dataSnapshot.child("WaterCents").getValue().toString();
                    String waterAmount =  dataSnapshot.child("waterAmount").getValue().toString();
                    waterPriceTextView.setText(waterCents+"c"+" for "+waterAmount);
                 }
                else{
                    waterPriceTextView.setText("N/A");
                }

                if(dataSnapshot.child("hasJet").getValue().toString().equals("true")){
                    String jetDollar = dataSnapshot.child("jetDollar").getValue().toString();
                    String jetAmount = dataSnapshot.child("jetAmount").getValue().toString();
                    jetPriceTextView.setText("$" + jetDollar + " for " + jetAmount);
                }
                else{
                    jetPriceTextView.setText("N/A");
                }

                if(dataSnapshot.child("hasVacuum").getValue().toString().equals("true")){
                    String vacuumDollar = dataSnapshot.child("VacuumDollar").getValue().toString();
                    String vacuumAmount = dataSnapshot.child("vacuumAmount").getValue().toString();
                    vacumPriceTextView.setText("$" + vacuumDollar + " for " + vacuumAmount);
                }
                else{
                    vacumPriceTextView.setText("N/A");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private static View createTabView(final Context context, final String text,int index) {
        View view = null;
        switch(index){
            case 1: view = LayoutInflater.from(context).inflate(R.layout.tab_water, null); break;
            case 2: view = LayoutInflater.from(context).inflate(R.layout.tab_vaccum, null);break;
            case 3: view = LayoutInflater.from(context).inflate(R.layout.tab_jet, null);break;
        }
        return view;
    }

    private void setupTab(final View view, final String tag,int index) {
        View tabView = createTabView(host.getContext(), tag,index);
        TabHost.TabSpec setContent = host.newTabSpec(tag).setIndicator(tabView).setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return view;
            }
        });
        host.addTab(setContent);
    }
}
