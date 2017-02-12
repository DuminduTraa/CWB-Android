package com.dumindudulanga.cwb;

/**
 * Created by acer on 2017-02-12.
 */

public class TileDetail {

    private String mHasWater;
    private String mHasVacuum;
    private String mHasJet;

    private String mStationName;
    private String mStationAddress;
    private String mDistance;
    private String mNoOfLots;

    public TileDetail(String hasWater, String hasVacuum, String hasJet, String stationName,
                      String stationAddress, String distance, String noOfLots){
        mHasWater = hasWater;
        mHasVacuum = hasVacuum;
        mHasJet = hasJet;
        mStationName = stationName;
        mStationAddress = stationAddress;
        mDistance = distance;
        mNoOfLots = noOfLots;
    }

    public String getStatusHasWater(){
        return mHasWater;
    }

    public String getStatusHasVacuum(){
        return mHasVacuum;
    }

    public String getStatusHasJet(){
        return mHasJet;
    }

    public String getStationName(){
        return mStationName;
    }

    public String getStationAddress(){
        return mStationAddress;
    }

    public String getDistance(){
        return mDistance;
    }

    public String getNoOfLots(){
        return mNoOfLots;
    }
}
