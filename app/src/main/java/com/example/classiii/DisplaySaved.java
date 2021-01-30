package com.example.classiii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.Random;

public class DisplaySaved extends AppCompatActivity {

    public String facescan;
    public File fsave;
    public String classresult;
    public String angles;
    private ImageView imageView;
    private TextView txtView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_saved);

        imageView = findViewById(R.id.imageView);
        txtView1 = findViewById(R.id.textView1);

        Bundle extras = getIntent().getExtras();

        if ( extras != null) {
            facescan = extras.getString("facescan");
            String[] tokens = facescan.split("---");
            fsave = new File(tokens[0]);
            classresult = tokens[1];
            angles = tokens[2];
        }

        String resulttext = "SONUÇ: " + classresult + "\n" + "\n" + angles;
        txtView1.setText(resulttext);
        imageView.setImageBitmap(BitmapFactory.decodeFile(fsave.toString()));


        Button btnHomePage = findViewById(R.id.btnGoHomePage);
        Button btnGoSaved = findViewById(R.id.btnGoSaved);

        btnHomePage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(myIntent);
            }

        });

        btnGoSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getBaseContext(), Saved.class);
                startActivity(myIntent);
            }




        });

    }
}
