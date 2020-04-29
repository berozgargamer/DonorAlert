package com.priyadarshan.donoralert.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.priyadarshan.donoralert.R;
import com.priyadarshan.donoralert.Utils.EndPoints;
import com.priyadarshan.donoralert.Utils.VolleySingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class activity_registration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final EditText etName, etCity, etMobile, etBloodgroup, etPasssword;
        Button btRegister;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        etName = (EditText)findViewById(R.id.etname);
        etCity = (EditText)findViewById(R.id.etcity);
        etMobile = (EditText)findViewById(R.id.etmobile);
        etBloodgroup = (EditText)findViewById(R.id.etbloodgroup);
        etPasssword = (EditText)findViewById(R.id.etpassword);

        btRegister = (Button)findViewById(R.id.btRegister);

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("Register", "Started");

                String name = etName.getText().toString();
                String city = etCity.getText().toString();
                String mobile = etMobile.getText().toString();
                String bloodgroup = etBloodgroup.getText().toString();
                String password = etPasssword.getText().toString();

                if(isValid(name, city, mobile, bloodgroup, password)){
                    register(name, city, mobile, bloodgroup, password);
                }

                //showMessage("Sucessfully Registered");
            }
        });
    }

    public void register(final String name, final String city, final String mobile, final String bloodgroup, final String password){
        StringRequest sr = new StringRequest(Request.Method.POST, EndPoints.registerURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("Success")){
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("city", city).apply();
                    Toast.makeText(activity_registration.this, response, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(activity_registration.this, activity_login.class));
                    activity_registration.this.finish();
                }
                else{
                    Toast.makeText(activity_registration.this, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_registration.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                Log.d("Error", error.getMessage());

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("city", city);
                params.put("number", mobile);
                params.put("blood_group", bloodgroup);
                params.put("password", password);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(sr);
    }

    private boolean isValid(String name, String city, String mobile, String bloodgroup, String password){
        List<String> bgList = new ArrayList<>();
        bgList.add("A+");
        bgList.add("A-");
        bgList.add("B+");
        bgList.add("B-");
        bgList.add("AB+");
        bgList.add("AB-");
        bgList.add("O+");
        bgList.add("O-");

        if(name.isEmpty()){
            showMessage("Name is empty");
            return false;
        }
        else if(!bgList.contains(bloodgroup)){
            showMessage("Enter a valid Blood Group");
            return false;
        }
        else if(mobile.length() != 10){
            showMessage("Not a valid mobile number");
            return false;
        }
        return true;
    }

    private  void showMessage(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
