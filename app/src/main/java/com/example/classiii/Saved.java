package com.example.classiii;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import java.io.File;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Map;
import java.util.Random;

public class Saved extends AppCompatActivity {

    private Button btnGoHomePage;
    private Button btnNewScan;
    private List<ScannedFace> ScannedFaceList = new ArrayList<>();
    private RecyclerView ScannedFaceRecyclerView;
    private RecyclerViewVerticalListAdapter ScannedFaceAdapter;

    String facescan;
    File fsave;
    String classresult;
    String angles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_saved);

        SharedPreferences sharedPreferences = Saved.this.getSharedPreferences("prefs", MODE_PRIVATE);
        Bundle extras = getIntent().getExtras();

        if ( extras != null) {
            facescan = extras.getString("facescan");

                if (facescan != null ) {
                    Random rand = new Random();
                    int randint = rand.nextInt(1000000000);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(new Integer(randint).toString(), facescan);
                    editor.apply();
                    editor.commit();
                }
        }

        ScannedFaceRecyclerView = findViewById(R.id.idRecyclerViewVerticalList);

        // add a divider after each item for more clarity
        ScannedFaceRecyclerView.addItemDecoration(new DividerItemDecoration(Saved.this, LinearLayoutManager.VERTICAL));
        ScannedFaceAdapter = new RecyclerViewVerticalListAdapter(ScannedFaceList, getApplicationContext());
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(Saved.this, LinearLayoutManager.VERTICAL, false);
        ScannedFaceRecyclerView.setLayoutManager(verticalLayoutManager);
        ScannedFaceRecyclerView.setAdapter(ScannedFaceAdapter);
        populateScannedFaceList();

        btnGoHomePage = findViewById(R.id.btnGoHomePage);
        btnGoHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

        btnNewScan = findViewById(R.id.btnNewScan);
        btnNewScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Scan.class);
                startActivity(i);
            }
        });
    }

    private void populateScannedFaceList(){

            SharedPreferences sharedPreferences = Saved.this.getSharedPreferences("prefs", MODE_PRIVATE);
            //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

            Map<String,?> keys = sharedPreferences.getAll();

            for(Map.Entry<String,?> entry : keys.entrySet()){
                String facescan =  entry.getValue().toString();

                if ( !facescan.equals("")) {
                    String[] tokens = facescan.split("---");
                    String[] key = entry.toString().split("=");
                    fsave = new File(tokens[1]);
                    classresult = tokens[2];
                    angles = tokens[3];
                    ScannedFace load = new ScannedFace(key[0], tokens[0], classresult, fsave, angles);
                    ScannedFaceList.add(load);
                }
            }
            ScannedFaceAdapter.notifyDataSetChanged();
        }
}
