package com.dumindudulanga.cwb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListViewActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);


        final ArrayList<String> placeNames = new ArrayList<String>();
        final ArrayList<DataSnapshot> objectIDs = new ArrayList<DataSnapshot>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("CarWashBay");
        //DatabaseReference carWashRef = myRef.child("CarWashBay");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> iterator = dataSnapshot.getChildren();

                for (DataSnapshot r : iterator){
                    objectIDs.add(r);
                    //Log.e("AAA",r.getKey());

                    placeNames.add(r.child("stName").getValue().toString());
                }

                ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(ListViewActivity.this,
                        android.R.layout.simple_expandable_list_item_1, placeNames);

                ListView listView = (ListView)findViewById(R.id.list_view);
                listView.setAdapter(itemsAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //Log.e("AAA",objectIDs.get(position).child("stName").getValue().toString());

                        Intent intent = new Intent(getBaseContext(), StationDescriptionActivity.class);
                        intent.putExtra("ObjectID",objectIDs.get(position).getKey());
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
