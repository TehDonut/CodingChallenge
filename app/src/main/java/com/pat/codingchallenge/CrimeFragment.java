package com.pat.codingchallenge;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by Pat on 5/24/2017.
 */

public abstract class CrimeFragment extends Fragment {


    RecyclerView recyclerView;
    static final String CRIME_NAME_BUNDLE = "crime_name";
    static final String CRIME_SEARCH_BUNDLE = "crime_search";
    static final String CRIME_TEAM_DETAIL_BUNDLE = "crime_team";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.crime_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateAdapter();
        return view;
    }


    abstract void updateAdapter();


}


