package com.dumindudulanga.cwb;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecyclerViewSimpleFragment extends BaseFragment {

    private View view;
    private RecyclerView recyclerView;
    private final List<RecyclerBean> allDataSource = new ArrayList<>();
    private int lastPosition = 0;
    private int size = 5;


    public static RecyclerViewSimpleFragment newInstance() {
        RecyclerViewSimpleFragment fragment = new RecyclerViewSimpleFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_station_description, null);
        initDataSource();
        initRecyclerView(view);
        return view;
    }

    public void initRecyclerView(View view) {

    }

    private void initDataSource() {

    }

    @Override
    public View getScrollableView() {
        return recyclerView;
    }

    @Override
    public void pullToRefresh() {
        lastPosition = 0;
        getDataTask();
    }

    @Override
    public void refreshComplete() {
        if (getActivity() instanceof DescriptionActivity2) {
            ((DescriptionActivity2) getActivity()).refreshComplete();
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

    public class RecyclerBean {
        public String title;
        public int icon;

        public RecyclerBean(String title, int icon) {
            this.title = title;
            this.icon = icon;
        }
    }


}
