package com.dumindudulanga.cwb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ListViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        ListView listView = (ListView)findViewById(R.id.list_view);

        ArrayList<String> placeNames = new ArrayList<String>();
        placeNames.add("Place A");
        placeNames.add("Place B");
        placeNames.add("place C");

        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1, placeNames);
        listView.setAdapter(itemsAdapter);

    }
}
