package com.universl.didul.car_maintenance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.universl.didul.car_maintenance.database.fuel_records;
import com.universl.didul.car_maintenance.database.trip_records;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

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
        myDB.insertData(ddates, dodometers, dlocations, adates, aodometers, alocations, distances, times, avg_speeds, parkings, notes, vehicle);
        finish();
        Intent intent = new Intent(this, MainActivity.class); // Replace NewActivity with your target activity class name
        startActivity(intent);
    }

    public void add1(View view) {
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Set the selected date in the text view.
                ddate.setText(String.format(Locale.getDefault(),"%02d/%02d/%04d", dayOfMonth, month + 1, year));
            }
        };
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, onDateSetListener, year, month, day);
        datePickerDialog.show();
    }

    public void add2(View view) {
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Set the selected date in the text view.
                adate.setText(String.format(Locale.getDefault(),"%02d/%02d/%04d", dayOfMonth, month + 1, year));
            }
        };
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, onDateSetListener, year, month, day);
        datePickerDialog.show();
    }

    private void loadLocation() {
        if (Geocoder.isPresent()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(this.getString(R.string.title));
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setMaxLines(1);
            input.setSingleLine(true);
            alert.setView(input);
            alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String result = input.getText().toString();
                    if (!result.isEmpty()) {
                        try {
                            location = result;
                            Geocoder gc = new Geocoder(add_trip.this);
                            List<Address> addresses = gc.getFromLocationName(location, 1); // get the found Address Objects

                            if (!addresses.isEmpty()) {
                                add_trip.this.location = location;
                                Address address = addresses.get(0);
                                lat = address.getLatitude();
                                lng = address.getLongitude();
                                if(Objects.equals(loc, "dloc")){
                                    dlocation.setText(location);
                                }else{
                                    alocation.setText(location);
                                }
                                //Log.i("LOCATION:", location + " Lat" + lat + "Lon: " + lng);
                                //Toast.makeText(HadahanaActivity.this, "LOCATION:"+location + " Lat" + lat + "Lon: " + lng, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(add_trip.this, "Location not found!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            Toast.makeText(add_trip.this, "Location not found!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
            alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Cancelled
                }
            });
            alert.show();
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
