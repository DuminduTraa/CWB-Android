package com.dumindudulanga.cwb;

import android.content.Intent;
import android.net.Uri;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FunctionFragment extends BaseFragment{

    private View view;
    private ScrollView scrollView;
    private RecyclerView recyclerView;
    private final List<RecyclerBean> allDataSource = new ArrayList<>();
    private int lastPosition = 0;
    private int size = 5;

    private String latitude;
    private String longitude;

    private String mObjectID;
    private String mFunction;

    private PopupWindow mPopupWindow;
    private Button feedback_button;

    private Context mContext;

    public static FunctionFragment newInstance(String objectID, String function,Context context) {
        FunctionFragment fragment = new FunctionFragment();
        fragment.setObjectID(objectID);
        fragment.setFunction(function);
        fragment.mContext = context;
        return fragment;
    }

    void setObjectID(String objectID){
        mObjectID = objectID;
    }
    void setFunction(String function){
        mFunction = function;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_station, null);
        initDataSource();
        initRecyclerView(view);
        return view;
    }

    public void initRecyclerView(View view) {
        mContext = this.getContext();

        scrollView = (ScrollView) view.findViewById(R.id.vacuum_scroll2);
        scrollView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        final TextView priceTextView = (TextView) view.findViewById(R.id.price_text_view);
        final TextView noOfLotsTextView = (TextView)view.findViewById(R.id.no_of_lots);
        Button routeButton = (Button)view.findViewById(R.id.route_button);
        Button activateButton = (Button) view.findViewById(R.id.activate_button);

        feedback_button = (Button)view.findViewById(R.id.feedback_button);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("CarWashBay").child(mObjectID);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(mFunction.equals("water")){
                    if(dataSnapshot.child("hasWater").getValue().toString().equals("true")){
                        String waterCents =  dataSnapshot.child("WaterCents").getValue().toString();
                        String waterAmount =  dataSnapshot.child("waterAmount").getValue().toString();
                        priceTextView.setText(waterCents+"c"+" for "+waterAmount);
                    }
                    else{
                        priceTextView.setText("N/A");
                    }
                }
                else if(mFunction.equals("vacuum")){
                    if(dataSnapshot.child("hasVacuum").getValue().toString().equals("true")){
                        String vacuumDollar =  dataSnapshot.child("VacuumDollar").getValue().toString();
                        String vacuumAmount =  dataSnapshot.child("vacuumAmount").getValue().toString();
                        priceTextView.setText("$"+vacuumDollar+" for "+vacuumAmount);
                    }
                    else{
                        priceTextView.setText("N/A");
                    }
                }
                else{
                    if(dataSnapshot.child("hasJet").getValue().toString().equals("true")){
                        String jetCents =  dataSnapshot.child("JetCents").getValue().toString();
                        String jetAmount =  dataSnapshot.child("JetAmount").getValue().toString();
                        priceTextView.setText(jetCents+"c"+" for "+jetAmount);
                    }
                    else{
                        priceTextView.setText("N/A");
                    }
                }

                noOfLotsTextView.setText(dataSnapshot.child("availableBays").getValue().toString());

                latitude = dataSnapshot.child("locationGlatitude").getValue().toString();
                longitude = dataSnapshot.child("locationGlongitude").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        activateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PostClass("vacuum").execute();
            }
        });
        routeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" +latitude + "," + longitude + "&mode=d" );
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
        feedback_button.setOnClickListener(new View.OnClickListener({
             @Override
                public void onClick(View view){
            showFeedbackPopUp();
        }
        }));

    }

    private void initDataSource() {

    }

    @Override
    public View getScrollableView() {
        return scrollView;
    }

    @Override
    public void pullToRefresh() {
        lastPosition = 0;
        getDataTask();
    }

    @Override
    public void refreshComplete() {
        if (getActivity() instanceof DescriptionActivity) {
            ((DescriptionActivity) getActivity()).refreshComplete();
        }
    }

    public void getDataTask() {
        getDataTask(true);
    }

    public void getDataTask(final boolean isClear) {

    }

    public List<RecyclerBean> getData() {
        try {
            int end = (lastPosition + size) > allDataSource.size() ? allDataSource.size() : (lastPosition + size);
            return allDataSource.subList(lastPosition, end);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void showFeedbackPopUp(){
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View customView = inflater.inflate(R.layout.feedback_layout,null);

        mPopupWindow = new PopupWindow(
                customView,
                RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
        );

        if(Build.VERSION.SDK_INT>=21){
            mPopupWindow.setElevation(5.0f);
        }

        mPopupWindow.showAtLocation(scrollView, Gravity.CENTER,0,0);
    }

    public class RecyclerBean {
        public String title;
        public int icon;

        public RecyclerBean(String title, int icon) {
            this.title = title;
            this.icon = icon;
        }
    }

    private class PostClass extends AsyncTask<String, Void, Void> {
        private String section;
        private PostClass(String section){
            this.section = section;
        }

        @Override
        protected Void doInBackground(String... params) {
            activateGate(this.section,"true");
            activateGate("gate","true");
            return null;
        }

        private void activateGate(String activate_part,String state){
            Log.e("YEs","YesYes");
            HttpURLConnection client = null;
            try {
                URL url = new URL("http://192.168.1.6:1337/"+activate_part);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("state", "true");

                //String urlParameters = "sbody=buzz";
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type","application/json");

                connection.setDoOutput(true);
                connection.setDoInput(true);
                OutputStreamWriter dStream = new OutputStreamWriter(connection.getOutputStream());
                dStream.write(postDataParams.toString());
                dStream.flush();
                dStream.close();
                int responseCode = connection.getResponseCode();
                Log.e("YEs",responseCode+"");
                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + postDataParams.toString());
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + postDataParams.toString());
                output.append(System.getProperty("line.separator")  + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator")  + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output" + br);
                while((line = br.readLine()) != null ) {
                    responseOutput.append(line);
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (SocketTimeoutException e){
                e.printStackTrace();
            }
            catch (IOException error) {
                //Handles input and output errors
                error.printStackTrace();
            }
            catch (JSONException e){
                e.printStackTrace();
            }
            catch (java.lang.Exception e){
                e.printStackTrace();;
            }
            finally {
                if(client != null) // Make sure the connection is not null.
                    client.disconnect();
            }

        }

        public String getPostDataString(JSONObject params) throws Exception {

            StringBuilder result = new StringBuilder();
            boolean first = true;

            Iterator<String> itr = params.keys();

            while(itr.hasNext()){

                String key= itr.next();
                Object value = params.get(key);

                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));
            }
            return result.toString();
        }
    }




}
