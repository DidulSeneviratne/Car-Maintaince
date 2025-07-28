package com.universl.didul.car_maintenance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.universl.didul.car_maintenance.database.documents;
import com.universl.didul.car_maintenance.database.trip_records;
import com.universl.didul.car_maintenance.database.vehicles;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Edit_vehicle extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    ImageView update, delete;
    EditText make, model, year, lic, chassi, insurance;
    String id, makes, models, years, licence, chassis, insurances, fuel, pic, f_id;
    ArrayList<String> activity_fid;
    ArrayList<byte[]> activity_img;
    Spinner fuel_type;
    ImageView picture, document;
    Button doc;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri uri;
    private Bitmap bitmap1, bitmap2;
    byte[] imgbyte, imgbyte1;

    public static HashMap<String, String> vehicle = MainActivity.vehicles;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_vehicle);
        update = findViewById(R.id.update);
        delete = findViewById(R.id.delete);
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
        Objects.requireNonNull(getSupportActionBar()).setTitle(Html.fromHtml("<font color='#FFFFFF' size='10'>" + "&nbsp;&nbsp;Edit Vehicles" +"</font>"));

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

        try {
            getAndSetIntentData();
        } catch (Exception e) {
            e.printStackTrace();
        }

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

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (make.getText().toString().equals("") && model.getText().toString().equals("") && year.getText().toString().equals("")
                            && lic.getText().toString().equals("") && chassi.getText().toString().equals("") && insurance.getText().toString().equals("")) {
                        Toast.makeText(Edit_vehicle.this, "Please fill all the data", Toast.LENGTH_SHORT).show();
                    } else {
                        updateData();
                    }
                }catch(Exception e){
                    Toast.makeText(Edit_vehicle.this, "Enter valid data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog();
            }
        });

    }

    void updateData() throws IOException {
        vehicles myDB = new vehicles(Edit_vehicle.this);
        makes = make.getText().toString();
        models = model.getText().toString();
        years = year.getText().toString();
        licence = lic.getText().toString();
        chassis = chassi.getText().toString();
        insurances = insurance.getText().toString();
        long i = myDB.updateData(id, bitmap1, makes, models, years, licence, chassis, insurances, fuel);
        if(i != -1){
            documents myDB1 = new documents(Edit_vehicle.this);
            myDB1.updateData(f_id, id, bitmap2);
            String body = makes + " " + models;
            vehicle.put(id, body);
            String serializedMap = gson.toJson(vehicle);
            FileOutputStream fos = openFileOutput("myHashMap.json", MODE_PRIVATE);
            fos.write(serializedMap.getBytes());
            fos.close();
        }
        finish();
        Intent intent = new Intent(this, MainActivity.class); // Replace NewActivity with your target activity class name
        startActivity(intent);
    }

    void getAndSetIntentData(){
        if (getIntent().hasExtra("id") && getIntent().hasExtra("img") &&
                getIntent().hasExtra("make") && getIntent().hasExtra("model") && getIntent().hasExtra("year") && getIntent().hasExtra("lic") &&
                getIntent().hasExtra("chassi") && getIntent().hasExtra("insurance") && getIntent().hasExtra("fuel")) {
            //Getting Data from Intent
            id = getIntent().getStringExtra("id");
            imgbyte = getIntent().getByteArrayExtra("img");
            makes = getIntent().getStringExtra("make");
            models = getIntent().getStringExtra("model");
            years = getIntent().getStringExtra("year");
            licence = getIntent().getStringExtra("lic");
            chassis = getIntent().getStringExtra("chassi");
            insurances = getIntent().getStringExtra("insurance");
            fuel = getIntent().getStringExtra("fuel");
            activity_fid = new ArrayList<>();
            activity_img = new ArrayList<byte[]>();
            storeDataInArrays();
            make.setText(makes);
            model.setText(models);
            year.setText(years);
            lic.setText(licence);
            chassi.setText(chassis);
            insurance.setText(insurances);
            if (Objects.equals(fuel, "Diesel")) {
                fuel_type.setSelection(0);
            } else {
                fuel_type.setSelection(1);
            }
            bitmap1 = BitmapFactory.decodeByteArray(imgbyte, 0, imgbyte.length);
            picture.setImageBitmap(bitmap1);
        }
    }

    void storeDataInArrays(){
        try {
            documents myDB1 = new documents(Edit_vehicle.this);
            Cursor cursor = myDB1.readAllData(id);
            if (cursor.getCount() != 0) {
                while (cursor.moveToNext()) {
                    try {
                        activity_fid.add(cursor.getString(0));
                        activity_img.add(cursor.getBlob(2));
                    } catch (Exception e) {
                        System.out.println(String.valueOf(e));

                        System.out.println("error");
                    }
                }
                f_id = activity_fid.get(0);
                imgbyte1 = activity_img.get(0);
                bitmap2 = BitmapFactory.decodeByteArray(imgbyte1, 0, imgbyte1.length);
                document.setImageBitmap(bitmap2);
            }
        }catch (Exception e){
            Toast.makeText(this, String.valueOf(e), Toast.LENGTH_SHORT).show();
        }
    }

    void confirmDialog() {
        /* AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + makes + " ?");
        builder.setMessage("Are you sure you want to delete " + makes + " ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                try{
                    vehicles myDB = new vehicles(Edit_vehicle.this);
                    documents myDB1 = new documents(Edit_vehicle.this);
                    myDB.deleteOneRow(id);
                    myDB1.deleteOneRow(f_id);
                    vehicle.remove(id);
                    String serializedMap = gson.toJson(vehicle);
                    FileOutputStream fos = openFileOutput("myHashMap.json", MODE_PRIVATE);
                    fos.write(serializedMap.getBytes());
                    fos.close();
                    signOut();
                }catch (Exception e){
                    System.out.println(e);
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show(); */
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.delete_confirm, null);

        // Set dynamic title and message
        TextView title = view.findViewById(R.id.delete_title);
        TextView message = view.findViewById(R.id.delete_message);
        title.setText("Delete " + makes + " ?");
        message.setText("Are you sure you want to delete " + makes + " ?");

        Button btnYes = view.findViewById(R.id.btn_yes);
        Button btnNo = view.findViewById(R.id.btn_no);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(false)
                .create();

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    vehicles myDB = new vehicles(Edit_vehicle.this);
                    documents myDB1 = new documents(Edit_vehicle.this);
                    myDB.deleteOneRow(id);
                    myDB1.deleteOneRow(f_id);
                    vehicle.remove(id);
                    String serializedMap = gson.toJson(vehicle);
                    FileOutputStream fos = openFileOutput("myHashMap.json", MODE_PRIVATE);
                    fos.write(serializedMap.getBytes());
                    fos.close();
                    signOut();
                }catch(Exception e){
                    System.out.println(e);
                }
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    void signOut(){
        finish();
        Intent intent = new Intent(this, MainActivity.class); // Replace NewActivity with your target activity class name
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed(); // Perform default back action
        finish();
        Intent intent = new Intent(this, Vehicle.class); // Replace NewActivity with your target activity class name
        startActivity(intent);
    }

}