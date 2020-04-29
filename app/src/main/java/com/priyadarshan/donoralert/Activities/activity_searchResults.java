package com.priyadarshan.donoralert.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.priyadarshan.donoralert.Adapter.SearchAdapter;
import com.priyadarshan.donoralert.DataModels.Donor;
import com.priyadarshan.donoralert.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class activity_searchResults extends AppCompatActivity {

    List<Donor> donorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        donorList = new ArrayList<>();

        String json;
        String city, blood_group;
        Intent intent = getIntent();
        json = intent.getStringExtra("json");
        city = intent.getStringExtra("city");
        blood_group = intent.getStringExtra("blood_group");

        TextView heading = findViewById(R.id.heading);
        //SpannableString spannableString = new SpannableString("Donors in "+city+ " with blood group "+blood_group);
        String str = "Donors in "+city+ " with blood group "+blood_group;
        heading.setText(str);

        Gson gson = new Gson();
        Type type = new TypeToken<List<Donor>>(){}.getType();
        List<Donor> dataModels = gson.fromJson(json, type);

        if (dataModels != null && dataModels.isEmpty()){
            heading.setText("No Results");
        }
        else if (dataModels != null){
            donorList.addAll(dataModels);
        }

        RecyclerView recyclerView = findViewById(R.id.rv_search);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        SearchAdapter adapter = new SearchAdapter(donorList, activity_searchResults.this);
        recyclerView.setAdapter(adapter);
    }
}
