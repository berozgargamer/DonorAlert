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

import java.util.HashMap;
import java.util.Map;

public class activity_make_request extends AppCompatActivity {

    EditText message;
    EditText title;
    Button request;
    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_request);
        activity_login lg = new activity_login();

        Intent intent = getIntent();
        number = intent.getStringExtra("mobile");

        message = findViewById(R.id.request_et);
        title = findViewById(R.id.title_et);
        request = findViewById(R.id.request_btn);

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid()){
                    uploadMessage(message.getText().toString(), title.getText().toString(), number);
                }
            }
        });
    }

    public boolean isValid(){
        if(message.getText().toString().isEmpty() || title.getText().toString().isEmpty()){
            showMessage("Title should not be empty");
            return false;
        }
        else if(title.getText().toString().isEmpty()){
            showMessage("Message should not be empty");
            return false;
        }
        return true;
    }

    public void uploadMessage(final String message, final String title, final String number){
        StringRequest sr = new StringRequest(Request.Method.POST, EndPoints.uploadPostURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("Success")){
                    Toast.makeText(activity_make_request.this, response,Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_make_request.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                Log.d("VOLLEY", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();

                params.put("title", title);
                params.put("message", message);
                params.put("number", number);

                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(sr);

    }

    public void showMessage(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
