package com.dumindudulanga.cwb;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;


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
public class VacuumFragment extends BaseFragment implements View.OnClickListener{

    private View view;
    private ScrollView scrollView;
    private RecyclerView recyclerView;
    private final List<RecyclerBean> allDataSource = new ArrayList<>();
    private int lastPosition = 0;
    private int size = 5;

    private RelativeLayout vacuum_activate;


    public static VacuumFragment newInstance() {
        VacuumFragment fragment = new VacuumFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_vacuum, null);
        initDataSource();
        initRecyclerView(view);
        return view;
    }

    public void initRecyclerView(View view) {
        scrollView = (ScrollView) view.findViewById(R.id.vacuum_scroll2);
        scrollView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        vacuum_activate = (RelativeLayout) view.findViewById(R.id.vacuum_activate2);
        vacuum_activate.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.vacuum_activate2:
                new PostClass("vacuum").execute();
                break;
        }
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
                System.out.println("output===============" + br);
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
