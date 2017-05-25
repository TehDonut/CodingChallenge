package com.pat.codingchallenge;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pat on 5/24/2017.
 */

public class CrimeDetailFragment extends CrimeFragment implements NFLFetchTask.CrimeListener {


    List<NFLCrimeDetailItem> crimeItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        crimeItems = new ArrayList<>();
    }

    @Override
    public void onStart() {
        super.onStart();
        String crime = getArguments().getString(CRIME_NAME_BUNDLE);
        NFLFetchTask fetchTask = new NFLFetchTask(this);
        fetchTask.getTopTeams(crime);
    }

    @Override
    public void jsonResults(String json)
    {
        Type type = new TypeToken<List<NFLCrimeDetailItem>>() {
        }.getType();
        parseResults(json, type);
    }

    void parseResults(String json, Type type)
    {
        Log.d("json results", json);
        Gson gson = new GsonBuilder().create();
        crimeItems = gson.fromJson(json, type);
        updateAdapter();
    }

    @Override
    void updateAdapter() {
        if (isAdded())
        {
            recyclerView.setAdapter(new CrimeDetailAdapter(crimeItems));
        }
    }

    class CrimeDetailAdapter extends RecyclerView.Adapter<CrimeDetailItemHolder> {

        public CrimeDetailAdapter(List<NFLCrimeDetailItem> items) {
            crimeItems = items;
        }

        @Override
        public CrimeDetailItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.crime_detail_list_item, parent, false);
            return new CrimeDetailItemHolder(view);
        }

        @Override
        public void onBindViewHolder(CrimeDetailItemHolder holder, int position) {
            NFLCrimeDetailItem item = crimeItems.get(position);
            holder.bindTextViews(getResources().getString(R.string.team_abbrev_label, item.getTeam()),
                    getResources().getString(R.string.team_name_label, item.getTeam_name()),
                    getResources().getString(R.string.team_city_label, item.getTeam_city()),
                    getResources().getString(R.string.arrest_count_label, item.getArrest_count()));
        }

        @Override
        public int getItemCount() {
            return crimeItems.size();
        }
    }

}
