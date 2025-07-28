package com.universl.didul.car_maintenance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.universl.didul.car_maintenance.database.fuel_records;
import com.universl.didul.car_maintenance.database.service_records;

import java.io.FileInputStream;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class add_service extends AppCompatActivity {
    ImageView save, home;
    EditText odometer, service, total, station, note;
    TextView date;
    String dates, odometers, services, totals, stations, notes, vehicle;
    ImageView receipt;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri uri;
    private Bitmap bitmap1;
    int odo, tot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_service);
        save = findViewById(R.id.save);
        date = findViewById(R.id.date);
        odometer = findViewById(R.id.km);
        service = findViewById(R.id.name);
        total = findViewById(R.id.cost);
        station = findViewById(R.id.station);
        note = findViewById(R.id.note);
        receipt = findViewById(R.id.img);
        vehicle = getIntent().getStringExtra("vehicle");

        Toolbar toolbar = findViewById(R.id.search_bar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(Html.fromHtml("<font color='#FFFFFF' size='10'>" + "&nbsp;&nbsp;Add Services" +"</font>"));

        receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    if (date.getText().toString().equals("") && odometer.getText().toString().equals("") && service.getText().toString().equals("")
                            && total.getText().toString().equals("")) {
                        Toast.makeText(add_service.this, "Please fill all the data", Toast.LENGTH_SHORT).show();
                    } else {
                        odo = Integer.parseInt(odometer.getText().toString());
                        tot = Integer.parseInt(total.getText().toString());
                        saveEventAction();
                    }
                }catch(Exception e){
                    Toast.makeText(add_service.this, "Enter valid data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.photo);
        receipt.setImageBitmap(bitmap1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        try{
            super.onActivityResult(requestCode, resultCode, data);
            if(resultCode == RESULT_OK && (requestCode == PICK_IMAGE_REQUEST)){
                uri = data.getData();
                bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                receipt.setImageBitmap(bitmap1);
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

    public void saveEventAction() {
        service_records myDB = new service_records(add_service.this);
        dates = date.getText().toString();
        odometers = String.valueOf(odo);
        services = service.getText().toString() + " service";
        totals = String.valueOf(tot);
        stations = station.getText().toString();
        notes = note.getText().toString();
        myDB.insertData(dates, odometers, services, totals, stations, notes, vehicle, bitmap1);
        finish();
        Intent intent = new Intent(this, MainActivity.class); // Replace NewActivity with your target activity class name
        startActivity(intent);
    }

    public void add(View view) {
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Set the selected date in the text view.
                date.setText(String.format(Locale.getDefault(),"%02d/%02d/%04d", dayOfMonth, month + 1, year));
            }
        };
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, onDateSetListener, year, month, day);
        datePickerDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed(); // Perform default back action
        finish();
        Intent intent = new Intent(this, MainActivity.class); // Replace NewActivity with your target activity class name
        startActivity(intent);
    }
}