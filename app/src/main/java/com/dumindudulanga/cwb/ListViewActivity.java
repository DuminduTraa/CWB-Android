package com.dumindudulanga.cwb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("CarWashBay");
        //DatabaseReference carWashRef = myRef.child("CarWashBay");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> iterator = dataSnapshot.getChildren();

                for (DataSnapshot child : iterator){
                    placeNames.add(child.child("stName").getValue().toString());
                    Log.d("Children",placeNames.get(0)+"");
                }

                ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(ListViewActivity.this,
                        android.R.layout.simple_expandable_list_item_1, placeNames);

                ListView listView = (ListView)findViewById(R.id.list_view);
                listView.setAdapter(itemsAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }



}
