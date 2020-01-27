package com.example.amedentix.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.amedentix.R;
import com.example.amedentix.api.Urls;
import com.example.amedentix.api.VolleySingleton;
import com.example.amedentix.models.Login;
import com.example.amedentix.storage.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private EditText email;
    private EditText name;
    private EditText passw;
    private EditText passw_conf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name= findViewById(R.id.name);
        email = findViewById(R.id.email);
        passw = findViewById(R.id.passw);
        passw_conf = findViewById(R.id.passw_confirm);
    }

    public void ExecuteRegister(View view)
    {
        final String nameVal = name.getText().toString().trim();
        final String emailVal = email.getText().toString().trim();
        final String passwVal = passw.getText().toString().trim();
        final String passw_confVal = passw_conf.getText().toString().trim();

        if (nameVal.isEmpty()) {
            name.setError(getString(R.string.name_null_txt));
        }

        if (emailVal.isEmpty()) {
            email.setError(getString(R.string.email_null_txt));
            email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailVal).matches()) {
            email.setError(getString(R.string.email_error_txt));
            email.requestFocus();
            return;
        }

        if (passwVal.isEmpty()) {
            passw.setError(getString(R.string.passw_null_txt));
            passw.requestFocus();
            return;
        }

        if (passw_confVal.isEmpty()) {
            passw.setError(getString(R.string.passw_conf_null_txt));
            passw.requestFocus();
            return;
        }

        if (!passwVal.equals(passw_confVal)) {
            passw.setError(getString(R.string.passw_conf_null_txt));
            passw_conf.setError(getString(R.string.passw_conf_null_txt));
            passw.requestFocus();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("user");

                                //creating a new user object
                                Login user = new Login(
                                        userJson.getString("api_token"),
                                        userJson.getString("id")
                                );

                                //storing the user in shared preferences
                                SharedPrefManager.getInstance(getApplicationContext()).saveUser(emailVal,passwVal,user.getApi_token(),user.getId());
                                Log.i("volley",user.getApi_token());

                                Intent intent = new Intent(getApplicationContext(),ChooseUserType.class);
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", nameVal);
                params.put("email", emailVal);
                params.put("password", passwVal);
                params.put("password_confirmation", passw_confVal);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
