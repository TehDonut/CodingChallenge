package com.pat.codingchallenge;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
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

public class CrimeTeamDetailFragment extends CrimeFragment implements NFLFetchTask.CrimeListener{
    List<NFLCrimeTeamDetailItem> crimeItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        crimeItems = new ArrayList<>();
    }

    @Override
    public void onStart() {
        super.onStart();
        String team = getArguments().getString(CRIME_TEAM_DETAIL_BUNDLE);
        NFLFetchTask fetchTask = new NFLFetchTask(this);
        fetchTask.teamDetail(team);
    }

    @Override
    public void jsonResults(String json)
    {
        Type type = new TypeToken<List<NFLCrimeTeamDetailItem>>() {
        }.getType();
        parseResults(json, type);
    }

    void parseResults(String json, Type type)
    {
        Gson gson = new GsonBuilder().create();
        crimeItems = gson.fromJson(json, type);
        updateAdapter();
    }

    @Override
    void updateAdapter() {
        if (isAdded())
        {
            recyclerView.setAdapter(new CrimeTeamDetailAdapter(crimeItems));
        }
    }

    class CrimeTeamDetailAdapter extends RecyclerView.Adapter<CrimeTeamDetailItemHolder> {

        public CrimeTeamDetailAdapter(List<NFLCrimeTeamDetailItem> items) {
            crimeItems = items;
        }

        @Override
        public CrimeTeamDetailItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.crime_team_detail_item, parent, false);
            return new CrimeTeamDetailItemHolder(view);
        }

        @Override
        public void onBindViewHolder(CrimeTeamDetailItemHolder holder, int position) {
            NFLCrimeTeamDetailItem item = crimeItems.get(position);
            holder.bindTextViews(item.getName(), item.getDate(), item.getCategory(),
                    item.getPosition(), item.getPositionName(), item.getPositionType(),
                    item.getEncounter(), item.getCrimeCategory(), item.getDescription(), item.getOutcome());
        }

        @Override
        public int getItemCount() {
            return crimeItems.size();
        }
    }
}
