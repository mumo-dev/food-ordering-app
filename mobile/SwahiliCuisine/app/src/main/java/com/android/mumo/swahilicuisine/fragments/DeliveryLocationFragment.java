package com.android.mumo.swahilicuisine.fragments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mumo.swahilicuisine.MainActivity;
import com.android.mumo.swahilicuisine.R;
import com.android.mumo.swahilicuisine.interfaces.OnLocationSetListener;
import com.android.mumo.swahilicuisine.model.Area;
import com.android.mumo.swahilicuisine.model.Town;
import com.android.mumo.swahilicuisine.services.ApiService;
import com.android.mumo.swahilicuisine.services.RetrofitClient;
import com.android.mumo.swahilicuisine.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryLocationFragment extends Fragment {

    private AutoCompleteTextView townAutoComplete;
    private Spinner areaDropDown;
    private ProgressBar mLoadingIndicator;
    private TextView errorTv;
    private LinearLayout mainContent;
    private Button button;


    private List<Town> towns = new ArrayList<>();
    private List<Area> areas = new ArrayList<>();

    public static final String TAG = "DeliveryLocation";

    private Town mUserTown;
    private Area mUserArea;

    OnLocationSetListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnLocationSetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnArticleSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public DeliveryLocationFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_delivery_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //


        townAutoComplete = view.findViewById(R.id.tv_town);
        areaDropDown = view.findViewById(R.id.area_spinner);
        mainContent = view.findViewById(R.id.main_content);
        errorTv = view.findViewById(R.id.tv_error);
        button = view.findViewById(R.id.p_btn);
        mLoadingIndicator = view.findViewById(R.id.pg_loading_indicator);

        errorTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchTowns();
            }
        });

        areaDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String areaName = (String) parent.getItemAtPosition(position);
//                Toast.makeText(DeliveryLocationFragment.this, areaName, Toast.LENGTH_LONG).show();
                for (Area area : areas) {
                    if (area.getName().equals(areaName)) {
                        mUserArea = area;
                        //set button enabled;;
                        if (mUserArea != null && mUserTown != null) {
                            button.setEnabled(true);
                        }
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mUserArea = null;
                Toast.makeText(getActivity(), "Please choose an area", Toast.LENGTH_LONG).show();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceUtils.storeLocationName(getActivity(), mUserTown.getName());
                PreferenceUtils.storeLocationId(getActivity(), mUserTown.getId());
                PreferenceUtils.storeLocationAreaId(getActivity(), mUserArea.getId());
                PreferenceUtils.storeLocationAreaName(getActivity(), mUserArea.getName());
                mListener.onLocationSet();
            }
        });

        townAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
//                Toast.makeText(DeliveryLocationFragment.this, "Selection: "+ selection, Toast.LENGTH_LONG).show();
                for (Town town : towns) {
                    if (town.getName().equals(selection)) {
                        mUserTown = town;
                        mUserArea = null;
                        button.setEnabled(false);
//                        Toast.makeText(DeliveryLocationFragment.this, "Selection Id: "+ mUserTown.getId(), Toast.LENGTH_LONG).show();

                        fetchArea(mUserTown.getId());
                    }
                }
            }
        });

        String town = PreferenceUtils.getLocationName(getActivity());
        int townId = PreferenceUtils.getLocationId(getActivity());
        String area = PreferenceUtils.getLocationAreaName(getActivity());
        int areaId = PreferenceUtils.getLocationAreaId(getActivity());
        if (town != null && townId != 0) {
            townAutoComplete.setText(town);
            Town town1 = new Town(townId, town);
            mUserTown = town1;

            mUserArea = new Area(areaId, area);
            fetchArea(townId);
            button.setEnabled(true);
        }

        fetchTowns();

    }


    private void uiLoading() {
//        mainContent.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
        errorTv.setVisibility(View.INVISIBLE);

    }

    private void uiLoaded() {
        mainContent.setVisibility(View.VISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        errorTv.setVisibility(View.INVISIBLE);
    }

    private void uiError() {
        mainContent.setVisibility(View.VISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        errorTv.setVisibility(View.VISIBLE);
    }


    private void fetchTowns() {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);

        uiLoading();
        api.fetchTowns().enqueue(new Callback<List<Town>>() {
            @Override
            public void onResponse(Call<List<Town>> call, Response<List<Town>> response) {
                uiLoaded();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        towns = new ArrayList<>();
                        towns.addAll(response.body());
                        Log.i(TAG, "data fetched: " + response.body().size());

                        setTownAdapter();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Town>> call, Throwable t) {
                Log.e(TAG, "Api call failed: " + t.getMessage());
                uiError();
            }
        });
    }

    private void fetchArea(int townId) {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);

        uiLoading();
        api.fetchArea(townId).enqueue(new Callback<List<Area>>() {
            @Override
            public void onResponse(Call<List<Area>> call, Response<List<Area>> response) {
                uiLoaded();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i(TAG, "data fetched: " + response.body().size());
                        areas = new ArrayList<>();
                        areas.addAll(response.body());
                        //set
                        setAreaAdapter();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Area>> call, Throwable t) {
                Log.e(TAG, "Error " + t.getMessage());
                uiError();
            }
        });
    }

    private void setTownAdapter() {
        String[] townArray = new String[towns.size()];
        for (int i = 0; i < towns.size(); i++) {
            townArray[i] = towns.get(i).getName();
        }

        ArrayAdapter townAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, townArray);
        townAutoComplete.setAdapter(townAdapter);
        townAutoComplete.setThreshold(1);

        townAutoComplete.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    // on focus off
                    String str = townAutoComplete.getText().toString();

                    ListAdapter listAdapter = townAutoComplete.getAdapter();
                    for (int i = 0; i < listAdapter.getCount(); i++) {
                        String temp = listAdapter.getItem(i).toString();
                        if (str.compareTo(temp) == 0) {
                            return;
                        }
                    }

                    townAutoComplete.setText("");
                    townAutoComplete.setError("Your town is currenty unavailable");
                }
            }
        });
    }

    private void setAreaAdapter() {

        String[] areaArray = new String[areas.size()];
        for (int i = 0; i < areas.size(); i++) {
            areaArray[i] = areas.get(i).getName();
        }
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, areaArray);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        areaDropDown.setAdapter(adapter);

        String area = PreferenceUtils.getLocationAreaName(getActivity());
        int areaId = PreferenceUtils.getLocationAreaId(getActivity());
        if (area != null) {
            for(int i=0; i< areaArray.length; i++){
                if(areaArray[i].equals(area)){
                    areaDropDown.setSelection(i);
                    break;
                }
            }
        }
    }
}
