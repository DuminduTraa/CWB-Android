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

    public TextView water_price;
    public TextView jet_price;
    public TextView vaccum_price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_description);

        host = (TabHost)findViewById(R.id.tabHost);
        host.setup();


        setupTab((LinearLayout)findViewById(R.id.tab1), "",1);
        setupTab((LinearLayout)findViewById(R.id.tab2), "",2);
        setupTab((LinearLayout)findViewById(R.id.tab3), "",3);

        water_price = (TextView)findViewById(R.id.water_price);
        jet_price = (TextView)findViewById(R.id.jet_price);
        vaccum_price = (TextView)findViewById(R.id.vaccum_price);

        Intent intent = getIntent();
        ObjectID = intent.getStringExtra("ObjectID");


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("CarWashBay").child(ObjectID);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.e("AAA",dataSnapshot.child("hasWater").getValue()+"");

                if(dataSnapshot.child("hasWater").getValue().toString().equals("true")){
                    String waterCents =  dataSnapshot.child("WaterCents").getValue().toString();

                    String waterAmount =  dataSnapshot.child("waterAmount").getValue().toString();

                    water_price.setText(waterCents+"c"+" for "+waterAmount);

                }
                else{

                }

                if(dataSnapshot.child("hasJet").getValue().toString().equals("true")){

                }
                else{

                }

                if(dataSnapshot.child("hasVacuum").getValue().toString().equals("true")){

                }
                else{

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
        View tabview = createTabView(host.getContext(), tag,index);
        TabHost.TabSpec setContent = host.newTabSpec(tag).setIndicator(tabview).setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return view;
            }
        });
        host.addTab(setContent);
    }
}
