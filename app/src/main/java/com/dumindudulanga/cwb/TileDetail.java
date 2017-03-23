package com.dumindudulanga.cwb;

/**
 * Created by acer on 2017-02-12.
 */

public class TileDetail {

    private String mObjectID;

    private String mHasWater;
    private String mHasVacuum;
    private String mHasJet;

    private String mStationName;
    private String mStationAddress;
    private Float mDistance;
    private String mNoOfLots;

    public TileDetail(String objectID, String hasWater, String hasVacuum, String hasJet, String stationName,
                      String stationAddress, Float distance, String noOfLots){
        mObjectID = objectID;
        mHasWater = hasWater;
        mHasVacuum = hasVacuum;
        mHasJet = hasJet;
        mStationName = stationName;
        mStationAddress = stationAddress;
        mDistance = distance;
        mNoOfLots = noOfLots;
    }
    public String getObjectID(){
        return mObjectID;
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

    public Float getDistance(){
        return mDistance;
    }

    public String getNoOfLots(){
        return mNoOfLots;
    }
}
