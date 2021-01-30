package com.example.classiii;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Scan extends AppCompatActivity {

    private ImageView imageView;
    private Button btnScan;
    private Bitmap ocrImageBitmap;
    private Button btnGoHomePage;
    private Button btnSave;
    private Button btnGoSaved;
    private TextView txtView1;
    private String finput = "";
    private String fsave = "noface";
    private String classresult = "";
    private String angles = "";
    private int clicked = 0;

    File file1, file2;
    File pictureFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    File imagesFolder = new File(pictureFolder + "/AppSinifIII");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        if (imagesFolder.exists()) {
            System.out.println("Images folder already exists"); //// 2901
        }else{
            if (imagesFolder.mkdirs()) {
                System.out.println(imagesFolder.toString());
                System.out.println("Successfully created");
            } else {
                System.out.println("Failed to create");
            }
        }

        if(!Python.isStarted())
            Python.start(new AndroidPlatform(this));

        //Python py = Python.getInstance();
        //file2 = new File(Environment.getExternalStorageDirectory()+"/"+timeStamp+".jpg");   /// output image name
        //file2 = new File(Environment.getExternalStorageDirectory()+"/SınıfIIIApp/"+timeStamp+".jpg");   /// output image name

        imageView = findViewById(R.id.imageView);
        btnScan = findViewById(R.id.btnScan);
        btnGoHomePage = findViewById(R.id.btnGoHomePage);
        btnSave = findViewById(R.id.btnSave);
        txtView1 = findViewById(R.id.textView1);
        txtView1.setText("Fotoğraf yükleyiniz ve 'Fotoğraf İşle' butonuna tıklayınız." + "\n" + "İşlem birkaç dakika sürebilir.");
        btnGoSaved = findViewById(R.id.btnGoSaved);


        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (clicked != 1) {
                    if (finput.equals("")) {
                        txtView1.setText("Henüz bir fotoğraf yüklemediniz." + "\n" + "\n" + "Fotoğraf yükleyiniz ve 'Fotoğraf İşle' butonuna tıklayınız." + "\n" + "İşlem birkaç dakika sürebilir.");
                        clicked = -1;
                    } else {
                        //Toast.makeText(getBaseContext(), "İşlem birkaç dakika sürebilir." , Toast.LENGTH_SHORT ).show();
                        Python py = Python.getInstance();
                        PyObject pyf = py.getModule("scan");

                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        String imgsavename = "Scanned_" + timeStamp + ".jpg";
                        fsave = imagesFolder.toString() + "/" + imgsavename;

                        PyObject obj = pyf.callAttr("process", finput, fsave); /// image path

                        String[] tokens = obj.toString().split(",");
                        System.out.println(tokens);
                        tokens[0] = tokens[0].replace("[", "");
                        tokens[0] = tokens[0].replace("]", "");
                        tokens[0] = tokens[0].replaceAll("[,']", "");

                        if (tokens[0].equals("noface")) {
                            txtView1.setText("Profil yüz bulunamadı. Lütfen yeni bir fotoğraf yükleyin.");
                            fsave = "noface";
                            angles = "";
                        } else {
                            classresult = tokens[0];
                            tokens[1] = tokens[1].replace("[", "");
                            tokens[1] = tokens[1].replace("'", "");
                            tokens[2] = tokens[2].replace("]", "");
                            tokens[2] = tokens[2].replaceAll("[,']", "");
                            tokens[3] = tokens[3].replace("]", "");
                            tokens[3] = tokens[3].replace("'", "");
                            tokens[4] = tokens[4].replace("]", "");
                            tokens[4] = tokens[4].replace("'", "");

                            String resulttext = "SONUÇ: " + tokens[0] + "\n" + "\n" + "AÇILAR:" + "\n" + "G-Sn-Pg: " + tokens[1] + "\n" + "G-Prn-Pg: " + tokens[2] + "\n" + "G-Pg-Ls: " + tokens[3] + "\n" + "Li-Sm-Pg: " + tokens[4];
                            angles = "AÇILAR:" + "\n" + "G-Sn-Pg: " + tokens[1] + "\n" + "G-Prn-Pg: " + tokens[2] + "\n" + "G-Pg-Ls: " + tokens[3] + "\n" + "Li-Sm-Pg: " + tokens[4];
                            txtView1.setText(resulttext);

                            imageView.setImageBitmap(BitmapFactory.decodeFile(fsave));

                            Bitmap bitmapsave = BitmapFactory.decodeFile(fsave);
                            int width = bitmapsave.getWidth();
                            int height = bitmapsave.getHeight();
                            int newwidth = 390;
                            int newheight = (newwidth * height) / width;
                            bitmapsave = Bitmap.createScaledBitmap(bitmapsave, newwidth, newheight, false);
                            SaveImage(bitmapsave, imgsavename);
                            clicked = 1;
                        }
                    }
                }
            }
        });

        btnGoSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getBaseContext(), Saved.class);
                myIntent.putExtra("noface","");
                startActivity(myIntent);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (finput.equals("")) {
                    txtView1.setText("Kaydedilecek bir sonuç bulunamadı. Lütfen bir fotoğraf işleyin, ardından sonucunuzu kaydedin");
                } else {
                    if (fsave.equals("noface")){
                        txtView1.setText("Kaydedilecek bir sonuç bulunamadı. Lütfen bir fotoğraf işleyin, ardından sonucunuzu kaydedin");
                    }else {
                        Intent myIntent = new Intent(getBaseContext(), Saved.class);

                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        Date date = new Date();
                        String dt = formatter.format(date);

                        myIntent.putExtra("facescan", dt + "---" + fsave + "---" + classresult + "---" + angles);
                        startActivity(myIntent);
                        Toast.makeText(getBaseContext(),"Kaydedildi" , Toast.LENGTH_SHORT ).show();
                    }
                }
            }

        });

        btnGoHomePage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }

        });
    }

    public void captureImage(View view) {
        //CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(this,true);
        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(this);
    }

    private void SaveImage(Bitmap bitMap, String fname) {
        File file = new File (imagesFolder, fname);
        finput = file.toString();
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitMap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            System.out.println("Kaydedildi: " + fname);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                imageView.setImageURI(resultUri);
                clicked = 0;

                // save image to gallery
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String fname = "Raw_" + timeStamp + ".jpg";
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    int newwidth = 390;
                    int newheight = (newwidth * height) / width;
                    bitmap = Bitmap.createScaledBitmap(bitmap, newwidth, newheight, false);
                    SaveImage(bitmap,fname);
                    System.out.println("Dosyayı kaydettim, bak buraya : "+ resultUri.toString()+fname.toString()); //2901
                } catch (IOException e) {
                    System.out.println("Dosyayı kaydedemedim"); //2901
                    e.printStackTrace();
                }

                file1 = new File(resultUri.toString());   /// input image name
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                ocrImageBitmap = BitmapFactory.decodeFile(resultUri.getEncodedPath(), options);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                error.printStackTrace();
                txtView1.setText(getString(R.string.ocr_reading_error));
            }
        }
    }


    public static <E> void SaveArrayListToSD(Context mContext, String filename, ArrayList<E> list){
        try {
            FileOutputStream fos = mContext.openFileOutput(filename + ".dat", mContext.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(list);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}