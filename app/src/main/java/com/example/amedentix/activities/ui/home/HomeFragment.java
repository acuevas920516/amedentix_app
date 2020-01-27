package com.example.amedentix.activities.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.amedentix.R;
import com.example.amedentix.activities.Home;
import com.example.amedentix.adapters.RecyclerViewAdapter;
import com.example.amedentix.api.Urls;
import com.example.amedentix.api.VolleySingleton;
import com.example.amedentix.models.Historia;
import com.example.amedentix.storage.SharedPrefManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private JSONObject jsonBodyObj = new JSONObject();
    private int from = 0;
    private String requestBody = "";

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<Historia> rowsArrayList = new ArrayList<>();

    private boolean isLoading = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.new_home_fragment, container, false);

        recyclerView = root.findViewById(R.id.historiesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        populateData();
        initAdapter();
        initScrollListener();

        return root;
    }

    private void populateData() {
        try {
            jsonBodyObj.put("from", from);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestBody = jsonBodyObj.toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_GET_HISTORIES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                //here goes the implementation
                                String data = obj.getJSONObject("data").getString("histories");
                                JSONArray items = new JSONArray(data);
                                for (int i = 0; i < items.length(); i++) {
                                    JSONObject item = (JSONObject) items.get(i);
                                    rowsArrayList.add(new Historia(
                                            item.get("owner").toString(),
                                            item.get("created_at").toString(),
                                            item.get("_id").toString(),
                                            item.get("media").toString(),
                                            item.getInt("likes")+" ❤",
                                            item.get("description").toString()
                                    ));
                                }
                                recyclerViewAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + SharedPrefManager.getInstance(getActivity().getApplicationContext()).getToken());
                return headers;
            }

        };

        VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void initAdapter() {

        recyclerViewAdapter = new RecyclerViewAdapter(rowsArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == rowsArrayList.size() - 1) {
                        //bottom of list!
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });


    }

    private void loadMore() {
        from += 5;
        rowsArrayList.add(null);
        recyclerViewAdapter.notifyItemInserted(rowsArrayList.size() - 1);

        try {
            jsonBodyObj.put("from", from);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestBody = jsonBodyObj.toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_GET_HISTORIES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            rowsArrayList.remove(rowsArrayList.size() - 1);
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                //here goes the implementation
                                String data = obj.getJSONObject("data").getString("histories");
                                JSONArray items = new JSONArray(data);
                                for (int i = 0; i < items.length(); i++) {
                                    JSONObject item = (JSONObject) items.get(i);
                                    rowsArrayList.add(new Historia(
                                            item.get("owner").toString(),
                                            item.get("created_at").toString(),
                                            item.get("_id").toString(),
                                            item.get("media").toString(),
                                            item.getInt("likes")+" ❤",
                                            item.get("description").toString()
                                    ));
                                }
                                recyclerViewAdapter.notifyDataSetChanged();
                                isLoading = false;
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + SharedPrefManager.getInstance(getActivity().getApplicationContext()).getToken());
                return headers;
            }

        };

        VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
    }


}