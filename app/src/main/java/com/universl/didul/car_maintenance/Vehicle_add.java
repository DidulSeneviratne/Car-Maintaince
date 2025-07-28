package com.universl.didul.car_maintenance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.os.Build;

import com.google.gson.Gson;
import com.universl.didul.car_maintenance.database.documents;
import com.universl.didul.car_maintenance.database.vehicles;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Objects;

public class Vehicle_add extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    ImageView save, home;
    EditText make, model, year, lic, chassi, insurance;
    String makes, models, years, licence, chassis, insurances, fuel, pic;
    Spinner fuel_type;
    ImageView picture, document;
    Button doc;

    public static HashMap<String, String> vehicle = MainActivity.vehicles;
    Gson gson = new Gson();
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri uri;
    private Bitmap bitmap1, bitmap2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_vehicle);
        save = findViewById(R.id.save);
        make = findViewById(R.id.make);
        model = findViewById(R.id.model);
        year = findViewById(R.id.year);
        lic = findViewById(R.id.lic);
        chassi = findViewById(R.id.chassi);
        insurance = findViewById(R.id.insurance);
        fuel_type = findViewById(R.id.fuel_type);
        picture = findViewById(R.id.picture);
        document = findViewById(R.id.document);
        doc = findViewById(R.id.doc);
        Toolbar toolbar = findViewById(R.id.search_bar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(Html.fromHtml("<font color='#FFFFFF' size='10'>" + "&nbsp;&nbsp;Add Vehicles" +"</font>"));

        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this,
                R.array.item,
                R.layout.spinner1
        );
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        fuel_type.setAdapter(adapter);

        fuel_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fuel = (String) fuel_type.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.photo);
        bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.photo);
        picture.setImageBitmap(bitmap1);
        document.setImageBitmap(bitmap2);

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pic = "main";
                Intent intent = new Intent(Intent.ACTION_PICK, Uri.parse(
                        "content://media/internal/images/media"
                ));
                startActivityForResult(intent,PICK_IMAGE_REQUEST);
            }
        });

        doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pic = "document";
                Intent intent = new Intent(Intent.ACTION_PICK, Uri.parse(
                        "content://media/internal/images/media"
                ));
                startActivityForResult(intent,PICK_IMAGE_REQUEST);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (make.getText().toString().equals("") && model.getText().toString().equals("") && year.getText().toString().equals("")
                            && lic.getText().toString().equals("") && chassi.getText().toString().equals("") && insurance.getText().toString().equals("")) {
                        Toast.makeText(Vehicle_add.this, "Please fill all the data", Toast.LENGTH_SHORT).show();
                    } else {
                        saveEventAction();
                    }
                }catch(Exception e){
                    Toast.makeText(Vehicle_add.this, "Enter valid data", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        try{
            super.onActivityResult(requestCode, resultCode, data);
            if(resultCode == RESULT_OK && (requestCode == PICK_IMAGE_REQUEST)){
                uri = data.getData();
                if(Objects.equals(pic, "main")){
                    bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                    picture.setImageBitmap(bitmap1);
                } else if (Objects.equals(pic, "document")) {
                    bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                    document.setImageBitmap(bitmap2);
                }
                String x = getPath(uri);
                Toast.makeText(getApplicationContext(),x, Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public String getPath(Uri uri){
        if(uri == null) return null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri,projection,null,null,null);
        if(cursor != null){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }

    public void saveEventAction() throws IOException {
        vehicles myDB = new vehicles(Vehicle_add.this);
        makes = make.getText().toString();
        models = model.getText().toString();
        years = year.getText().toString();
        licence = lic.getText().toString();
        chassis = chassi.getText().toString();
        insurances = insurance.getText().toString();
        System.out.println(bitmap1);
        System.out.println(bitmap2);
        long i = myDB.insertData(bitmap1, makes, models, years, licence, chassis, insurances, fuel);
        if(i != -1){
            documents myDB1 = new documents(Vehicle_add.this);
            myDB1.insertData(String.valueOf(i),bitmap2);
            String body = makes + " " + models;
            vehicle.put(String.valueOf(i), body);
            String serializedMap = gson.toJson(vehicle);
            FileOutputStream fos = openFileOutput("myHashMap.json", MODE_PRIVATE);
            fos.write(serializedMap.getBytes());
            fos.close();
        }
        finish();
        Intent intent = new Intent(this, MainActivity.class); // Replace NewActivity with your target activity class name
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed(); // Perform default back action
        finish();
        Intent intent = new Intent(this, Vehicle.class); // Replace NewActivity with your target activity class name
        startActivity(intent);
    }
}