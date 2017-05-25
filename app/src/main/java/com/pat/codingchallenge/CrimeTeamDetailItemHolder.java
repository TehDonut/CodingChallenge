package com.pat.codingchallenge;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Pat on 5/24/2017.
 */

public class CrimeTeamDetailItemHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.name) TextView name;
    @BindView(R.id.date) TextView date;
    @BindView(R.id.category) TextView category;
    @BindView(R.id.position) TextView position;
    @BindView(R.id.position_name) TextView positionName;
    @BindView(R.id.position_type) TextView positionType;
    @BindView(R.id.encounter) TextView encounter;
    @BindView(R.id.crime_category) TextView crimeCategory;
    @BindView(R.id.description) TextView description;
    @BindView(R.id.outcome) TextView outcome;


    public CrimeTeamDetailItemHolder(View itemView)
    {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindTextViews(String playerName, String dateArrested, String cat, String pos, String posName, String posType, String enc, String crimeCat, String desc, String out)
    {
        name.setText(playerName);
        date.setText(dateArrested);
        category.setText(cat);
        position.setText(pos);
        positionName.setText(posName);
        positionType.setText(posType);
        encounter.setText(enc);
        crimeCategory.setText(crimeCat);
        description.setText(desc);
        outcome.setText(out);
    }
}
