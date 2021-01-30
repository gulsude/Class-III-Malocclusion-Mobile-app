package com.example.classiii;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


public class MainActivity extends AppCompatActivity {

    private Button btnScan;
    private Button btnPreviousScans;
    private Button btnInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnScan = findViewById(R.id.btnScan);
        btnInfo = findViewById(R.id.btnInfo);
        btnPreviousScans = findViewById(R.id.btnPreviousScans);


        btnScan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Scan.class);
                startActivity(i);
            }
        });


        btnInfo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //String val = "Key";
                Intent myIntent = new Intent(getBaseContext(), Info.class);
                //myIntent.putExtra("val", val);
                startActivity(myIntent);
            }
        });

        btnPreviousScans.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Saved.class);
                startActivity(i);
            }
        });
    }

    public void captureImage(View view) {
        //CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(this);
        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(this);
    }
}
