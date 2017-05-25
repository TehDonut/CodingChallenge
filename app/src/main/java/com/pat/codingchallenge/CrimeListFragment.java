package com.pat.codingchallenge;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class CrimeListFragment extends CrimeFragment implements NFLFetchTask.CrimeListener
{

    private List<NFLCrimeItem> crimeItems;
    private Calendar startDate;
    private Calendar endDate;
    @BindView(R.id.start_date) TextView startDateButton;
    @BindView(R.id.end_date) TextView endDateButton;
    @BindView(R.id.date_layout) LinearLayout dateLayout;
    String dateFormat = "MMM d yyyy";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        crimeItems = new ArrayList<>();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        ButterKnife.bind(this, view);
        recyclerView = (RecyclerView) view.findViewById(R.id.crime_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateAdapter();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater)
    {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.menu_crime, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchForTeam(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        MenuItem clearItem = menu.findItem(R.id.clear_date);
        clearItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startDateButton.setText(getResources().getString(R.string.set_date));
                endDateButton.setText(getResources().getString(R.string.set_date));
                startDate = null;
                endDate = null;
                refreshList();
                return true;
            }
        });

    }

    @OnClick (R.id.start_date)
    void pickStartDate()
    {
        if (startDate == null)
        {
            startDate = Calendar.getInstance();
        }
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                startDate.set(Calendar.YEAR, year);
                startDate.set(Calendar.MONTH, month);
                startDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                listener,
                startDate.get(Calendar.YEAR),
                startDate.get(Calendar.MONTH),
                startDate.get(Calendar.DAY_OF_MONTH));
             startDateButton.setText(DateFormat.format(dateFormat, startDate.getTime()));
        datePickerDialog.create();
        datePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                startDateButton.setText(DateFormat.format(dateFormat, startDate.getTime()));
                refreshList();
            }
        });
        datePickerDialog.show();
    }

    @OnClick (R.id.end_date)
    void pickEndDate()
    {
        if (endDate == null)
        {
            endDate = Calendar.getInstance();
        }
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                endDate.set(Calendar.YEAR, year);
                endDate.set(Calendar.MONTH, month);
                endDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                listener,
                endDate.get(Calendar.YEAR),
                endDate.get(Calendar.MONTH),
                endDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.create();
        datePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                endDateButton.setText(DateFormat.format(dateFormat, endDate.getTime()));
                refreshList();
            }
        });
        datePickerDialog.show();
    }

    private void refreshList()
    {
        String startDateString = null;
        String endDateString = null;
        if (startDate != null)
        {
            startDateString = DateFormat.format("yyyy-MM-dd" , startDate.getTime()).toString();
        }
        if (endDate != null)
        {
            endDateString = DateFormat.format("yyyy-MM-dd" , endDate.getTime()).toString();
        }
        NFLFetchTask fetchTask = new NFLFetchTask(this);
        fetchTask.getTopCrimes(startDateString, endDateString);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        NFLFetchTask fetchTask = new NFLFetchTask(this);
        fetchTask.getTopCrimes(null, null);
    }


    @Override
    public void jsonResults(String json)
    {
        Type type = new TypeToken<List<NFLCrimeItem>>(){}.getType();
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
            recyclerView.setAdapter(new CrimeListAdapter(crimeItems));
        }
    }

    private void searchForTeam(String teamName)
    {
        CrimeTeamSearchFragment fragment = new CrimeTeamSearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CRIME_SEARCH_BUNDLE, teamName);
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.content_fragment, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void displayCrimeDetails(String crimeName)
    {
        CrimeDetailFragment fragment = new CrimeDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CRIME_NAME_BUNDLE, crimeName);
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.content_fragment, fragment)
                .addToBackStack(null)
                .commit();
    }


    class CrimeListAdapter extends RecyclerView.Adapter<CrimeListItemHolder> {

        public CrimeListAdapter(List<NFLCrimeItem> items) {
            crimeItems = items;
        }

        @Override
        public CrimeListItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.crime_list_item, parent, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = recyclerView.getChildLayoutPosition(v);
                    NFLCrimeItem item = crimeItems.get(position);
                    displayCrimeDetails(item.getCrimeName());
                }
            });
            return new CrimeListItemHolder(view);
        }

        @Override
        public void onBindViewHolder(CrimeListItemHolder holder, int position) {
            NFLCrimeItem item = crimeItems.get(position);
            holder.bindTextViews(getResources().getString(R.string.crime_name_label, item.getCrimeName()),
                    getResources().getString(R.string.crime_count_label, item.getCrimeCount()));
        }

        @Override
        public int getItemCount() {
            return crimeItems.size();
        }
    }




}
