package com.example.amedentix.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.amedentix.R;
import com.example.amedentix.api.Urls;
import com.example.amedentix.api.VolleySingleton;
import com.example.amedentix.models.Login;
import com.example.amedentix.storage.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ChooseUserType extends AppCompatActivity {

    JSONObject jsonBodyObj = new JSONObject();
    ImageButton doctor,patient;
    String id, token, type, requestBody = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_user_type);

        id = SharedPrefManager.getInstance(getApplicationContext()).getId();
        token = SharedPrefManager.getInstance(getApplicationContext()).getToken();

        doctor = (ImageButton)findViewById(R.id.doctor);
        patient = (ImageButton)findViewById(R.id.patient);

        doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "doctor";
                try {
                    jsonBodyObj.put("user_id", id);
                    jsonBodyObj.put("type", type);
                }catch (JSONException e){
                    e.printStackTrace();
                }
                requestBody = jsonBodyObj.toString();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_SET_TYPE,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    //converting response to json object
                                    JSONObject obj = new JSONObject(response);

                                    //if no error in response
                                    if (!obj.getBoolean("error")) {
                                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(getApplicationContext(),Home.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_right_anim,R.anim.slide_left_anim);
                                    } else {
                                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                    public Map<String, String> getHeaders()throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json");
                        headers.put("Authorization", "Bearer " + token);
                        return headers;
                    }

                };

                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
            }
        });

        patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "patient";
                try {
                    jsonBodyObj.put("user_id", id);
                    jsonBodyObj.put("type", type);
                }catch (JSONException e){
                    e.printStackTrace();
                }
                requestBody = jsonBodyObj.toString();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_SET_TYPE,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    //converting response to json object
                                    JSONObject obj = new JSONObject(response);

                                    //if no error in response
                                    if (!obj.getBoolean("error")) {
                                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(getApplicationContext(),Home.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_right_anim,R.anim.slide_left_anim);
                                    } else {
                                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                    public Map<String, String> getHeaders()throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json");
                        headers.put("Authorization", "Bearer " + token);
                        return headers;
                    }

                };

                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
            }
        });
    }
}
