package com.dumindudulanga.cwb;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
    public TextView vacuumPriceTextView;
    public TextView jetPriceTextView;

    public Button waterRouteButton;
    public Button vacuumRouteButton;
    public Button jetRouteButton;

    public Button waterFeedbackButton;
    public Button vacuumFeedbackButton;
    public Button jetFeedbackButton;

    public Button waterPreBookButton;
    public Button vacuumPreBookButton;
    public Button jetPreBookButton;

    public LinearLayout controlWaterLayout;
    public LinearLayout controlVacuumLayout;
    public LinearLayout controlJetLayout;

    public RatingBar waterRatingBar;
    public RatingBar vacuumRatingBar;
    public RatingBar jetRatingBar;

    public boolean ratingVisibility = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_description);
        getIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        setupTab((LinearLayout)findViewById(R.id.tab1), "",1);
        setupTab((LinearLayout)findViewById(R.id.tab2), "",2);
        setupTab((LinearLayout)findViewById(R.id.tab3), "",3);

        waterPriceTextView = (TextView)findViewById(R.id.water_price);
        vacuumPriceTextView = (TextView)findViewById(R.id.vacuum_price);
        jetPriceTextView = (TextView)findViewById(R.id.jet_price);

        waterFeedbackButton = (Button)findViewById(R.id.feedback_water);
        vacuumFeedbackButton = (Button)findViewById(R.id.feedback_vacuum);
        jetFeedbackButton = (Button)findViewById(R.id.feedback_jet);

        //configureFeedBack();
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
                    waterPriceTextView.setText(waterCents+"c"+" for "+waterAmount);
                 }
                else{
                    waterPriceTextView.setText("N/A");
                }

                if(dataSnapshot.child("hasVacuum").getValue().toString().equals("true")){
                    String vacuumDollar =  dataSnapshot.child("VacuumDollar").getValue().toString();
                    String vacuumAmount =  dataSnapshot.child("vacuumAmount").getValue().toString();
                    vacuumPriceTextView.setText("$"+vacuumDollar+" for "+vacuumAmount);
                }
                else{
                    vacuumPriceTextView.setText("N/A");
                }

                if(dataSnapshot.child("hasJet").getValue().toString().equals("true")){
                    String jetCents =  dataSnapshot.child("JetCents").getValue().toString();
                    String jetAmount =  dataSnapshot.child("JetAmount").getValue().toString();
                    jetPriceTextView.setText(jetCents+"c"+" for "+jetAmount);

                }
                else{
                    jetPriceTextView.setText("N/A");
                }

                configureFeedBack();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //configureFeedBack();
    }

    private static View createTabView(final Context context, final String text, int index) {

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
        //host.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        host.addTab(setContent);
    }

    public  void configureFeedBack(){

        controlWaterLayout = (LinearLayout)findViewById(R.id.control_water);
        controlVacuumLayout = (LinearLayout)findViewById(R.id.control_vacuum);
        controlJetLayout = (LinearLayout)findViewById(R.id.control_jet);

        waterRatingBar =  (RatingBar)findViewById(R.id.rating_water);
        vacuumRatingBar =  (RatingBar)findViewById(R.id.rating_vacuum);
        jetRatingBar =  (RatingBar)findViewById(R.id.rating_jet);

        View.OnClickListener feedbackListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ratingVisibility == false){
                    waterRatingBar.setVisibility(View.VISIBLE);
                    vacuumRatingBar.setVisibility(View.VISIBLE);
                    jetRatingBar.setVisibility(View.VISIBLE);

                }
                else{
                    waterRatingBar.setVisibility(View.INVISIBLE);
                    vacuumRatingBar.setVisibility(View.INVISIBLE);
                    jetRatingBar.setVisibility(View.INVISIBLE);
                }
                 ratingVisibility = !ratingVisibility;

//                vacuumPriceTextView.setText("sd");
//                jetPriceTextView.setText("sd");
//                waterPriceTextView.setText("d");
//
//                ratingBar = new RatingBar(getApplicationContext());
//                ratingBar.setNumStars(5);
//                ratingBar.setStepSize(1);
//                ratingBar.setRating(0);
//
//                ViewGroup parent = (ViewGroup) controlWaterLayout.getParent();
//                parent.removeView(controlWaterLayout);
//                controlWaterLayout.addView(ratingBar,0);
//                parent.addView(controlWaterLayout);
//
//                parent = (ViewGroup) controlVacuumLayout.getParent();
//                parent.removeView(controlVacuumLayout);
//                controlVacuumLayout.addView(ratingBar,0);
//                parent.addView(controlVacuumLayout);
//
//                parent = (ViewGroup) controlJetLayout.getParent();
//                parent.removeView(controlJetLayout);
//                controlJetLayout.addView(ratingBar,0);
//                parent.addView(controlJetLayout);
//
//                controlWaterLayout.addView(ratingBar,0);
//                controlVacuumLayout.addView(ratingBar,0);
//                controlJetLayout.addView(ratingBar,0);
            }
        };
        waterFeedbackButton.setOnClickListener(feedbackListener);
        vacuumFeedbackButton.setOnClickListener(feedbackListener);
        jetFeedbackButton.setOnClickListener(feedbackListener);

    }
}
