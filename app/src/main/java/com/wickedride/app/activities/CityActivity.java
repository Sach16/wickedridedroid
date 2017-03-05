package com.wickedride.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.wickedride.app.R;
import com.wickedride.app.adapters.CityAdapter;
import com.wickedride.app.manager.SessionManager;
import com.wickedride.app.models.CityResult;
import com.wickedride.app.utils.APIMethods;
import com.wickedride.app.views.MyLinearLayoutManager;
import com.wickedride.app.views.WRProgressView;

import butterknife.ButterKnife;
import butterknife.InjectView;
/**
 * Created by Nitish Kulkarni on 2/8/15.
 */
public class CityActivity extends BaseDefaultActionActivity {

    public static final String TAG = "CITY_ACTIVITY";

    @InjectView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @InjectView(R.id.progress)
    WRProgressView mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        ButterKnife.inject(this);
        fetchAllCities();
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setHasFixedSize(false);
    }

    public void openRideActivity() {
        Intent openRideActivity = new Intent(getApplicationContext(), YourRidesActivity.class);
        startActivity(openRideActivity);
        finish();
    }

    private void fetchAllCities() {
        mProgress.setVisibility(View.VISIBLE);
//        String url = APIMethods.BASE_URL + APIMethods.GET_ALL_CITIES;
//        Log.d("CityActivity","GetAllCities::" + url);
//        JsonObjectRequest citiesRequest = new JsonObjectRequest(Request.Method.GET,
//                url,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        mProgressBar.setVisibility(View.GONE);
//                        Log.d(TAG, "Cities::" + response.toString());
//                        CityResult cities = new Gson().fromJson(response.toString(), CityResult.class);
//                        CityAdapter adapter = new CityAdapter(CityActivity.this, cities.getData());
//                        mRecyclerView.setAdapter(adapter);
//                        SessionManager.setAllCities(getApplicationContext(), response.toString());
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                mProgressBar.setVisibility(View.GONE);
//            }
//        });
//        WRRequestQueue.getInstance(getApplicationContext()).getRequestQueue().add(citiesRequest);

        placeRequest(APIMethods.GET_ALL_CITIES, CityResult.class);
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);
            Log.d("response", response + "");
            Log.d("response", "ApiMethod::" + apiMethod);
        if (APIMethods.GET_ALL_CITIES.equals(apiMethod)) {
            CityResult cityResult = (CityResult) response;
            CityAdapter adapter = new CityAdapter(CityActivity.this, cityResult.getResult().getData());
            mRecyclerView.setAdapter(adapter);
            SessionManager.setAllCities(getApplicationContext(), new Gson().toJson(cityResult));
        }
        mProgress.setVisibility(View.GONE);
    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
        super.onErrorResponse(error, apiMethod);
        mProgress.setVisibility(View.GONE);
    }
}
