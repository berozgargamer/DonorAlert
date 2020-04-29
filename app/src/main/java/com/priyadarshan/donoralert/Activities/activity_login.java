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

import java.util.HashMap;
import java.util.Map;

public class activity_login extends AppCompatActivity {

    EditText etMobile, etPassword;
    Button btLogin;
    public String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etMobile = findViewById(R.id.et_login_number);
        etPassword = findViewById(R.id.ep_login_password);
        btLogin = findViewById(R.id.bt_login);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etMobile.setError(null);
                etPassword.setError(null);
                mobile = etMobile.getText().toString();
                String password = etPassword.getText().toString();

                if (isValid(mobile, password)){
                    login(mobile, password);
                }
            }
        });
    }

    private void login(final String mobile, final String password){
        StringRequest sr = new StringRequest(Request.Method.POST, EndPoints.loginURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.equals("Invalid Credentials")){
                    Toast.makeText(activity_login.this, response, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(activity_login.this, MainActivity.class);
                    intent.putExtra("mobile", mobile);
                    startActivity(intent);
                    //startActivity(new Intent(activity_login.this, MainActivity.class));
                    //activity_login.this.finish();

                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("number", mobile).apply();
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("city", response).apply();

                    activity_login.this.finish();

                }
                else{
                    Toast.makeText(activity_login.this, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_login.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                Log.d("Error", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("number", mobile);
                params.put("password", password);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(sr);
    }

    private boolean isValid(String mobile, String password){
        if(mobile.isEmpty()) {
            showMessage("Empty Mobile Number");
            etMobile.setError("Empty Mobile Number");
            return false;
        }
        else if (password.isEmpty()) {
            showMessage("Empty Password");
            etPassword.setError("Empty Password");
            return false;
        }
        return true;
    }

    private void showMessage(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
