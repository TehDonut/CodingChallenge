package com.pat.codingchallenge;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Pat on 5/24/2017.
 */

public class CrimeTeamSearchItemHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.team_full_name) TextView teamName;
    @BindView(R.id.team_code) TextView teamCode;
    @BindView(R.id.team_city) TextView teamCity;


    public CrimeTeamSearchItemHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindTextViews(String name, String code, String city)
    {
        teamName.setText(name);
        teamCode.setText(code);
        teamCity.setText(city);
    }

}
