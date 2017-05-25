package com.pat.codingchallenge;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Pat on 5/24/2017.
 */

public class CrimeListItemHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.title) TextView crimeName;
    @BindView(R.id.subtitle) TextView crimeCount;


    public CrimeListItemHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindTextViews(String name, String count)
    {
        crimeName.setText(name);
        crimeCount.setText(count);
    }




}
