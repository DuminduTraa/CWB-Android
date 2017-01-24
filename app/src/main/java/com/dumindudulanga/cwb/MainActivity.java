package com.dumindudulanga.cwb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button listViewButton = (Button)findViewById(R.id.list_view_button);
        Button mapViewButton = (Button)findViewById(R.id.map_view_button);
        listViewButton.setOnClickListener(listViewButtonListener);
        mapViewButton.setOnClickListener(mapViewbuttonListener);
    }

    private View.OnClickListener listViewButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, StationDescriptionActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener mapViewbuttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, MapViewActivity.class);
            startActivity(intent);
        }
    };


}
