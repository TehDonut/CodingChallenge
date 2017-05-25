package com.pat.codingchallenge;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Pat on 5/24/2017.
 */

public class CrimeDetailItemHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.team_abbrev) TextView teamAbbrev;
    @BindView(R.id.team_name) TextView teamName;
    @BindView(R.id.team_city) TextView teamCity;
    @BindView(R.id.arrest_count) TextView arrestCount;


    public CrimeDetailItemHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindTextViews(String abbrev, String name, String city, String arrest) {
        teamAbbrev.setText(abbrev);
        teamName.setText(name);
        teamCity.setText(city);
        arrestCount.setText(arrest);
    }

}
