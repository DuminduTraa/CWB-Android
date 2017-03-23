package com.dumindudulanga.cwb;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by acer on 2017-02-12.
 */

public class CustomAdapter extends ArrayAdapter<TileDetail> {

    public CustomAdapter(Activity context, ArrayList<TileDetail> stationDetails){
        super(context, 0, stationDetails);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_view_tile, parent, false);
        }

        TileDetail currentTileDetail = getItem(position);

        View waterIndicator = listItemView.findViewById(R.id.water_indicator);
        View vacuumIndicator = listItemView.findViewById(R.id.vacuum_indicator);
        View jetIndicator = listItemView.findViewById(R.id.jet_indicator);

        TextView stationNameTextView = (TextView) listItemView.findViewById(R.id.station_name_text_view);
        TextView stationAddressTextView = (TextView) listItemView.findViewById(R.id.station_address_text_view);
        TextView distanceTextView = (TextView) listItemView.findViewById(R.id.distance_text_view);
        TextView noOfLotsTextView = (TextView) listItemView.findViewById(R.id.lots_text_view);

        if(currentTileDetail.getStatusHasWater().equals("true")){
            waterIndicator.setBackgroundColor(Color.GREEN);
        }
        else{
            waterIndicator.setBackgroundColor(Color.RED);
        }

        if(currentTileDetail.getStatusHasVacuum().equals("true")){
            vacuumIndicator.setBackgroundColor(Color.GREEN);
        }
        else{
            vacuumIndicator.setBackgroundColor(Color.RED);
        }

        if(currentTileDetail.getStatusHasJet().equals("true")){
            jetIndicator.setBackgroundColor(Color.GREEN);
        }
        else{
            jetIndicator.setBackgroundColor(Color.RED);
        }

        stationNameTextView.setText(currentTileDetail.getStationName());
        stationAddressTextView.setText(currentTileDetail.getStationAddress());
        distanceTextView.setText(""+ currentTileDetail.getDistance() + " km" );
        noOfLotsTextView.setText(currentTileDetail.getNoOfLots());

        return listItemView;

    }

}
