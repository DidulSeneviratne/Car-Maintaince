package com.izoneapps.carmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.izoneapps.carmanager.database.odometer_records;
import com.izoneapps.carmanager.database.trip_records;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class add_trip extends AppCompatActivity {
    ImageView save, home;
    EditText dodometer, aodometer, distance, time, avg_speed, parking, note;
    TextView ddate, adate, dlocation, alocation;
    String ddates, dodometers, dlocations, adates, aodometers, alocations, distances, times, avg_speeds, parkings, notes, vehicle;
    int dodo, aodo, km;
    double hour, avgs;
    String location, loc;
    double lat = 28.7041;
    double lng = 77.1025;
    DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_trip);
        save = findViewById(R.id.save);
        ddate = findViewById(R.id.ddate);
        dodometer = findViewById(R.id.dodo);
        dlocation = findViewById(R.id.dloc);
        adate = findViewById(R.id.adate);
        aodometer = findViewById(R.id.aodo);
        alocation = findViewById(R.id.aloc);
        distance = findViewById(R.id.distance);
        time = findViewById(R.id.time);
        avg_speed = findViewById(R.id.avg);
        parking = findViewById(R.id.park);
        note = findViewById(R.id.notes);
        vehicle = getIntent().getStringExtra("vehicle");

        Toolbar toolbar = findViewById(R.id.search_bar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(Html.fromHtml("<font color='#FFFFFF' size='10'>" + "&nbsp;&nbsp;Add Trips" +"</font>"));

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(ddate.getText().toString().equals("") && dodometer.getText().toString().equals("") && dlocation.getText().toString().equals("") &&
                            adate.getText().toString().equals("") && aodometer.getText().toString().equals("") && alocation.getText().toString().equals("") &&
                            distance.getText().toString().equals("") && time.getText().toString().equals("")){
                        Toast.makeText(add_trip.this, "Please fill all the data", Toast.LENGTH_SHORT).show();
                    }else{
                        dodo = Integer.parseInt(dodometer.getText().toString());
                        aodo = Integer.parseInt(aodometer.getText().toString());
                        km = Integer.parseInt(distance.getText().toString());
                        hour = Double.parseDouble(time.getText().toString());
                        saveEventAction();
                    }
                }catch (Exception e){
                    Toast.makeText(add_trip.this, "Enter valid data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loc = "dloc";
                loadLocation();
            }
        });

        alocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loc = "aloc";
                loadLocation();
            }
        });

        distance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    km = Integer.parseInt(distance.getText().toString());
                    hour = Double.parseDouble(time.getText().toString());
                    avgs = km / hour;
                    avg_speed.setText(df.format(avgs));
                }catch(Exception e){
                    Log.d("Pass","");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        time.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    km = Integer.parseInt(distance.getText().toString());
                    hour = Double.parseDouble(time.getText().toString());
                    avgs = km / hour;
                    avg_speed.setText(df.format(avgs));
                }catch(Exception e){
                    Log.d("Pass","");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void saveEventAction() {
        trip_records myDB = new trip_records(add_trip.this);
        odometer_records myDB1 = new odometer_records(add_trip.this);
        String sharedId = UUID.randomUUID().toString();
        ddates = ddate.getText().toString();
        dodometers = String.valueOf(dodo);
        dlocations = dlocation.getText().toString();
        adates = adate.getText().toString();
        aodometers = String.valueOf(aodo);
        alocations = alocation.getText().toString();
        distances = String.valueOf(km);
        times = String.valueOf(hour);
        avg_speeds = df.format(avgs);
        parkings = parking.getText().toString();
        notes = note.getText().toString();
        myDB.insertData(sharedId, ddates, dodometers, dlocations, adates, aodometers, alocations, distances, times, avg_speeds, parkings, notes, vehicle);
        myDB1.insertData(sharedId, adates, aodometers, notes, vehicle);
        finish();
        Intent intent = new Intent(this, MainActivity.class); // Replace NewActivity with your target activity class name
        startActivity(intent);
    }

    public void add1(View view) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_date_picker, null);

        DatePicker datePicker = dialogView.findViewById(R.id.custom_date_picker);
        TextView btnOk = dialogView.findViewById(R.id.btn_ok);
        TextView btnCancel = dialogView.findViewById(R.id.btn_cancel);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        btnOk.setOnClickListener(v -> {
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth();
            int year = datePicker.getYear();

            String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month + 1, year);
            ddate.setText(selectedDate);  // update your TextView
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    public void add2(View view) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_date_picker, null);

        DatePicker datePicker = dialogView.findViewById(R.id.custom_date_picker);
        TextView btnOk = dialogView.findViewById(R.id.btn_ok);
        TextView btnCancel = dialogView.findViewById(R.id.btn_cancel);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        btnOk.setOnClickListener(v -> {
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth();
            int year = datePicker.getYear();

            String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month + 1, year);
            adate.setText(selectedDate);  // update your TextView
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void loadLocation() {
        if (Geocoder.isPresent()) {
            LayoutInflater inflater = LayoutInflater.from(add_trip.this);
            View dialogView = inflater.inflate(R.layout.dialog_location, null);

            EditText input = dialogView.findViewById(R.id.input_location);
            TextView btnYes = dialogView.findViewById(R.id.btn_yes);
            TextView btnNo = dialogView.findViewById(R.id.btn_no);

            AlertDialog dialog = new AlertDialog.Builder(add_trip.this)
                    .setView(dialogView)
                    .setCancelable(false) // prevent closing on outside touch
                    .create();

            btnYes.setOnClickListener(v -> {
                String result = input.getText().toString();
                if (!result.isEmpty()) {
                    try {
                        location = result;
                        Geocoder gc = new Geocoder(add_trip.this);
                        List<Address> addresses = gc.getFromLocationName(location, 1);

                        if (!addresses.isEmpty()) {
                            add_trip.this.location = location;
                            Address address = addresses.get(0);
                            lat = address.getLatitude();
                            lng = address.getLongitude();
                            if (Objects.equals(loc, "dloc")) {
                                dlocation.setText(location);
                            } else {
                                alocation.setText(location);
                            }
                        } else {
                            Toast.makeText(add_trip.this, "Location not found!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        Toast.makeText(add_trip.this, "Location not found!", Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                } else {
                    input.setError("Enter location");
                }
            });

            btnNo.setOnClickListener(v -> dialog.dismiss());

            dialog.show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed(); // Perform default back action
        finish();
        Intent intent = new Intent(this, MainActivity.class); // Replace NewActivity with your target activity class name
        startActivity(intent);
    }
}
