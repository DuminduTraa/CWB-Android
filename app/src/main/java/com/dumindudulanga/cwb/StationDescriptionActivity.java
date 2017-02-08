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

    public TextView water_price;
    public TextView jet_price;
    public TextView vaccum_price;

    public Button water_route;
    public Button jet_route;
    public Button vaccum_route;

    public Button water_feedback;
    public Button jet_feedback;
    public Button vaccum_feedback;

    public Button water_prebook;
    public Button jet_prebook;
    public Button vaccum_prebook;

    public LinearLayout control_water;
    public LinearLayout control_jet;
    public LinearLayout control_vaccum;

    public RatingBar rating_water;
    public RatingBar rating_jet;
    public RatingBar rating_vaccum;

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

        water_price = (TextView)findViewById(R.id.water_price);
        jet_price = (TextView)findViewById(R.id.jet_price);
        vaccum_price = (TextView)findViewById(R.id.vaccum_price);

        water_feedback = (Button)findViewById(R.id.feedback_water);
        jet_feedback = (Button)findViewById(R.id.feedback_jet);
        vaccum_feedback = (Button)findViewById(R.id.feedback_vaccum);

        //configureFeedBack();



        Intent intent = getIntent();
        ObjectID = intent.getStringExtra("ObjectID");


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("CarWashBay").child(ObjectID);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.e("AAA",dataSnapshot.child("hasWater").getValue()+"");
                //sdsd

                if(dataSnapshot.child("hasWater").getValue().toString().equals("true")){
                    String waterCents =  dataSnapshot.child("WaterCents").getValue().toString();

                    String waterAmount =  dataSnapshot.child("waterAmount").getValue().toString();

                    water_price.setText(waterCents+"c"+" for "+waterAmount);

                }
                else{
                    //String waterAmount = "Not Available";
                    water_price.setText("Not Available");
                }

                if(dataSnapshot.child("hasJet").getValue().toString().equals("true")){

                    String jetCents =  dataSnapshot.child("JetCents").getValue().toString();

                    String jetAmount =  dataSnapshot.child("JetAmount").getValue().toString();

                    jet_price.setText(jetCents+"c"+" for "+jetAmount);

                }
                else{
                    jet_price.setText("Not Available");
                }

                if(dataSnapshot.child("hasVacuum").getValue().toString().equals("true")){
                    String vaccumDollar =  dataSnapshot.child("VacuumDollar").getValue().toString();

                    String vaccumAmount =  dataSnapshot.child("vacuumAmount").getValue().toString();

                    vaccum_price.setText("$"+vaccumDollar+" for "+vaccumAmount);
                }
                else{

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
        View tabview = createTabView(host.getContext(), tag,index);
        TabHost.TabSpec setContent = host.newTabSpec(tag).setIndicator(tabview).setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return view;
            }
        });
        //host.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        host.addTab(setContent);
    }

    public  void configureFeedBack(){

        control_water = (LinearLayout)findViewById(R.id.control_water);
        control_jet = (LinearLayout)findViewById(R.id.control_jet);
        control_vaccum = (LinearLayout)findViewById(R.id.control_vaccum);

        rating_water =  (RatingBar)findViewById(R.id.rating_water);
        rating_jet =  (RatingBar)findViewById(R.id.rating_jet);
        rating_vaccum =  (RatingBar)findViewById(R.id.rating_vaccum);

        View.OnClickListener feedbackListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("YOYO","sdsd00jknkjxx");

                if (ratingVisibility == false){
                    rating_water.setVisibility(View.VISIBLE);
                    rating_jet.setVisibility(View.VISIBLE);
                    rating_vaccum.setVisibility(View.VISIBLE);

                }
                else{
                    rating_water.setVisibility(View.INVISIBLE);
                    rating_jet.setVisibility(View.INVISIBLE);
                    rating_vaccum.setVisibility(View.INVISIBLE);
                }
                 ratingVisibility = !ratingVisibility;

//                vaccum_price.setText("sd");
//                jet_price.setText("sd");
//                water_price.setText("d");

//                ratingBar = new RatingBar(getApplicationContext());
//                ratingBar.setNumStars(5);
//                ratingBar.setStepSize(1);
//                ratingBar.setRating(0);

//                ViewGroup parent = (ViewGroup) control_water.getParent();
//                parent.removeView(control_water);
//                control_water.addView(ratingBar,0);
//                parent.addView(control_water);
//
//                parent = (ViewGroup) control_vaccum.getParent();
//                parent.removeView(control_vaccum);
//                control_vaccum.addView(ratingBar,0);
//                parent.addView(control_vaccum);
//
//                parent = (ViewGroup) control_jet.getParent();
//                parent.removeView(control_jet);
//                control_jet.addView(ratingBar,0);
//                parent.addView(control_jet);



//                control_water.addView(ratingBar,0);
//                control_vaccum.addView(ratingBar,0);
//                control_jet.addView(ratingBar,0);



            }
        };
        water_feedback.setOnClickListener(feedbackListener);

        jet_feedback.setOnClickListener(feedbackListener);

        vaccum_feedback.setOnClickListener(feedbackListener);


    }
}
