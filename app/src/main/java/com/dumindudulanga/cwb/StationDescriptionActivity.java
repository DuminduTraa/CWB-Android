package com.dumindudulanga.cwb;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

public class StationDescriptionActivity extends AppCompatActivity {

    public TabHost host;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_description);

        host = (TabHost)findViewById(R.id.tabHost);
        host.setup();


        setupTab((LinearLayout)findViewById(R.id.tab1), "",1);
        setupTab((LinearLayout)findViewById(R.id.tab2), "",2);
        setupTab((LinearLayout)findViewById(R.id.tab3), "",3);




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
