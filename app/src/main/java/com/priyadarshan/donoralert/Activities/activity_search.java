package com.priyadarshan.donoralert.Activities;

import android.content.Intent;
import android.os.Bundle;
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

public class activity_search extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final EditText bloodgroup, city;

        bloodgroup = findViewById(R.id.sr_bloodgroup);
        city = findViewById(R.id.sr_city);

        Button submit = findViewById(R.id.sr_search);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String blood_group = bloodgroup.getText().toString();
                String _city = city.getText().toString();
                if(isValid(blood_group, _city)){
                    get_search_results(blood_group, _city);
                }
            }
        });
    }

    private boolean isValid(String blood_group, String _city){
        List<String> validBloodGroups = new ArrayList<>();
        validBloodGroups.add("A+");
        validBloodGroups.add("A-");
        validBloodGroups.add("B+");
        validBloodGroups.add("B-");
        validBloodGroups.add("AB+");
        validBloodGroups.add("AB-");
        validBloodGroups.add("O+");
        validBloodGroups.add("O-");

        if(!validBloodGroups.contains(blood_group)){
            showMessage("Invalid Blood Group "+ validBloodGroups);
            return false;
        }
        else if(_city.isEmpty()){
            showMessage("Enter City");
            return false;
        }
        return true;
    }

    private void get_search_results(final String blood_group, final String city){

        StringRequest sr = new StringRequest(Request.Method.POST, EndPoints.searchDonors, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //json
                Intent intent = new Intent(activity_search.this, activity_searchResults.class);
                intent.putExtra("city", city);
                intent.putExtra("blood_group", blood_group);
                intent.putExtra("json", response);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_search.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                Log.d("Error", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("city", city);
                params.put("blood_group", blood_group);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(sr);
    }

    public void showMessage(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
