package com.pat.codingchallenge;

import android.os.AsyncTask;
import android.text.format.DateFormat;
import android.util.Log;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Pat on 5/24/2017.
 */

public class NFLFetchTask {
    private static final String NFL_ARREST_ENDPOINT = "http://nflarrest.com/api/v1/";
    private static final String CRIME = "crime";
    private static final String TOP_TEAMS = "topTeams";
    private static final String TEAM = "team";
    private static final String SEARCH = "search";
    private static final String ARRESTS = "arrests";

    private CrimeListener crimeListener;

    public NFLFetchTask(CrimeListener crimeListener)
    {
        this.crimeListener = crimeListener;
    }

    public String run(String url, HashMap<String, String> params)
    {
        OkHttpClient client = new OkHttpClient();
        String finalUrl = url;
        boolean first = true;
        for (HashMap.Entry<String, String> map: params.entrySet())
        {
            finalUrl += (first ? "?" : "&");
            finalUrl += map.getKey() + "=" + map.getValue();
            first = false;
        }
        Request request = new Request.Builder().url(finalUrl).build();
        try
        {
            Response response = client.newCall(request).execute();
            return response.body().string();
        }
        catch(IOException e)
        {
            Log.e("NFLFetchTask", e.getMessage());
        }
        return null;
    }

    public void getTopCrimes(String startDate, String endDate)
    {
        NFLFetchAsyncTask fetchAsyncTask =  new NFLFetchAsyncTask(NFL_ARREST_ENDPOINT + CRIME);
        if (startDate != null)
        {
            fetchAsyncTask.addParam("start_date", startDate);
        }
        if (endDate != null)
        {
            fetchAsyncTask.addParam("end_date", endDate);
        }
        fetchAsyncTask.execute();
    }

    public void getTopTeams(String crime)
    {
        NFLFetchAsyncTask fetchAsyncTask =  new NFLFetchAsyncTask(NFL_ARREST_ENDPOINT + CRIME + "/" + TOP_TEAMS + "/" + crime);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2007);
        String dateString = DateFormat.format("yyyy-MM-dd" ,calendar.getTime()).toString();
        fetchAsyncTask.addParam("start_date", dateString);
        fetchAsyncTask.execute();
    }

    public void searchTeams(String query)
    {
        NFLFetchAsyncTask fetchAsyncTask =  new NFLFetchAsyncTask(NFL_ARREST_ENDPOINT + TEAM + "/" + SEARCH);
        fetchAsyncTask.addParam("term", query);
        fetchAsyncTask.execute();
    }

    public void teamDetail(String team)
    {
        NFLFetchAsyncTask fetchAsyncTask =  new NFLFetchAsyncTask(NFL_ARREST_ENDPOINT + TEAM + "/" + ARRESTS + "/" + team);
        fetchAsyncTask.execute();
    }

    private class NFLFetchAsyncTask extends AsyncTask<Void,Void,String>
    {
        private String endpoint;
        private HashMap<String, String> extraParams;

        public NFLFetchAsyncTask(String endpoint)
        {
            this.endpoint = endpoint;
            extraParams = new HashMap<>();
        }

        public void addParam(String key, String value)
        {
            extraParams.put(key, value);
        }

        @Override
        protected String doInBackground(Void... params)
        {
            return run(endpoint, extraParams);
        }

        @Override
        public void onPostExecute(String json)
        {
            crimeListener.jsonResults(json);
        }
    }

    public interface CrimeListener
    {
        void jsonResults(String json);
    }

}
