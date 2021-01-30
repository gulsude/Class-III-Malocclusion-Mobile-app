package com.example.classiii;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Info extends AppCompatActivity {

    private TextView txtView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Button btnHomePage = findViewById(R.id.btnGoHomePage);
        Button btnScan = findViewById(R.id.btnScan);
        Button btnGoSaved = findViewById(R.id.btnGoSaved);
        txtView2 = findViewById(R.id.textView2);

        txtView2.setText("Malokluzyon literaturde kotu kapanis olarak tanimlanmaktadir. Bazi cocuklarin cene ve disleri duzgun olarak gelisemezler. Bu da duzgun olarak siralanmamis, karsi cenedeki dislerle iyi uyumda olmayan iliskiye neden olur. Genel olarak malokluzyon sagligi etkilemez, bir hastalik degildir. Malokluzyon sadece dislerin normal pozisyonlarinda olmamasidir. Ancak bu durum, bireyin yuz seklini, dislerin gorunumlerini etkileyerek kiside utangaclik, kendine guvende eksiklik ve hatta depresyona sebep olabilir. Ileri derecedeki malokluzyonlar yeme, konusma gibi fonksiyonlari etkileyebilir ve dislerin temizlenmesini zorlastirabilir.");

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

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getBaseContext(), Scan.class);
                startActivity(myIntent);
            }
        });

    }
}
