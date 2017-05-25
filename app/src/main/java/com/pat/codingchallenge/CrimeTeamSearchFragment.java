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

public class CrimeTeamSearchFragment extends CrimeFragment implements NFLFetchTask.CrimeListener
{

    private List<NFLCrimeSearchItem> crimeItems;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        crimeItems = new ArrayList<>();
    }


    @Override
    public void onStart()
    {
        super.onStart();
        String query = getArguments().getString(CRIME_SEARCH_BUNDLE);
        NFLFetchTask fetchTask = new NFLFetchTask(this);
        fetchTask.searchTeams(query);
    }

    @Override
    public void jsonResults(String json)
    {
        Type type = new TypeToken<List<NFLCrimeSearchItem>>(){}.getType();
        parseResults(json, type);
    }

    void parseResults(String json, Type type)
    {
        Log.d("json results", json);
        Gson gson = new GsonBuilder().create();
        crimeItems = gson.fromJson(json, type);
        updateAdapter();
    }

    public void updateAdapter()
    {
        if (isAdded())
        {
            recyclerView.setAdapter(new CrimeTeamSearchAdapter(crimeItems));
        }
    }

    private void displayTeamDetails(String teamName)
    {
        CrimeTeamDetailFragment fragment = new CrimeTeamDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CRIME_TEAM_DETAIL_BUNDLE, teamName);
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.content_fragment, fragment)
                .addToBackStack(null)
                .commit();
    }

    class CrimeTeamSearchAdapter extends RecyclerView.Adapter<CrimeTeamSearchItemHolder> {

        public CrimeTeamSearchAdapter(List<NFLCrimeSearchItem> items) {
            crimeItems = items;
        }

        @Override
        public CrimeTeamSearchItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.crime_search_item, parent, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = recyclerView.getChildLayoutPosition(v);
                    NFLCrimeSearchItem item = crimeItems.get(position);
                    displayTeamDetails(item.getTeamCode());

                }
            });
            return new CrimeTeamSearchItemHolder(view);
        }

        @Override
        public void onBindViewHolder(CrimeTeamSearchItemHolder holder, int position) {
            NFLCrimeSearchItem item = crimeItems.get(position);
            holder.bindTextViews(getResources().getString(R.string.team_name_label, item.getTeamName()),
                    getResources().getString(R.string.team_abbrev_label, item.getTeamCode()),
                    getResources().getString(R.string.team_city_label, item.getCity()));
        }

        @Override
        public int getItemCount() {
            return crimeItems.size();
        }
    }



}
