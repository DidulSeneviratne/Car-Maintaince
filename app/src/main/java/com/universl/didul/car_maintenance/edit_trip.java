package com.universl.didul.car_maintenance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.universl.didul.car_maintenance.database.fuel_records;
import com.universl.didul.car_maintenance.database.service_reminders;
import com.universl.didul.car_maintenance.database.trip_records;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class edit_trip extends AppCompatActivity {
    ImageView delete, update, home;
    EditText dodometer, aodometer, distance, time, speed, park, note;
    TextView ddate, adate, dlocation, alocation;
    String id, ddates, dodometers, dlocations, adates, aodometers, alocations, distances, times, speeds, parks, notes, vehicle;
    int dodo, aodo, km;
    double hour, avgs;
    String location, loc;
    double lat = 28.7041;
    double lng = 77.1025;
    DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_trip);
        Toolbar toolbar = findViewById(R.id.search_bar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(Html.fromHtml("<font color='#FFFFFF' size='10'>" + "&nbsp;&nbsp;Edit Trips" +"</font>"));

        delete = findViewById(R.id.delete);
        update = findViewById(R.id.update);
        ddate = findViewById(R.id.ddate);
        dodometer = findViewById(R.id.dodometer);
        dlocation = findViewById(R.id.dloc);
        adate = findViewById(R.id.adate);
        aodometer = findViewById(R.id.aodometer);
        alocation = findViewById(R.id.aloc);
        distance = findViewById(R.id.km);
        time = findViewById(R.id.time);
        speed = findViewById(R.id.avg);
        park = findViewById(R.id.lkr);
        note = findViewById(R.id.notes);

        getAndSetIntentData();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(ddate.getText().toString().equals("") && dodometer.getText().toString().equals("") && dlocation.getText().toString().equals("") &&
                            adate.getText().toString().equals("") && aodometer.getText().toString().equals("") && alocation.getText().toString().equals("") &&
                            distance.getText().toString().equals("") && time.getText().toString().equals("")){
                        Toast.makeText(edit_trip.this, "Please fill all the data", Toast.LENGTH_SHORT).show();
                    }else{
                        updateData();
                    }
                }catch (Exception e){
                    Toast.makeText(edit_trip.this, "Enter valid data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog();
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
                    speed.setText(df.format(avgs));
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
                    speed.setText(df.format(avgs));
                }catch(Exception e){
                    Log.d("Pass","");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    void updateData(){
        dodo = Integer.parseInt(dodometer.getText().toString());
        aodo = Integer.parseInt(aodometer.getText().toString());
        km = Integer.parseInt(distance.getText().toString());
        hour = Double.parseDouble(time.getText().toString());
        trip_records myDB = new trip_records(edit_trip.this);
        ddates = ddate.getText().toString();
        dodometers = String.valueOf(dodo);
        dlocations = dlocation.getText().toString();
        adates = adate.getText().toString();
        aodometers = String.valueOf(aodo);
        alocations = alocation.getText().toString();
        distances = String.valueOf(km);
        times = String.valueOf(hour);
        speeds = df.format(avgs);
        parks = park.getText().toString();
        notes = note.getText().toString();
        myDB.updateData(id, ddates, dodometers, dlocations, adates, aodometers, alocations, distances, times, speeds, parks, notes, vehicle);
        finish();
        Intent intent = new Intent(this, MainActivity.class); // Replace NewActivity with your target activity class name
        startActivity(intent);
    }

    void getAndSetIntentData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("d_date") &&
                getIntent().hasExtra("d_odometer") && getIntent().hasExtra("d_location") && getIntent().hasExtra("a_date") && getIntent().hasExtra("a_odometer") &&
                getIntent().hasExtra("a_location") && getIntent().hasExtra("distance") && getIntent().hasExtra("time") && getIntent().hasExtra("avg_speed") &&
                getIntent().hasExtra("parking") && getIntent().hasExtra("notes")){
            //Getting Data from Intent
            id = getIntent().getStringExtra("id");
            ddates = getIntent().getStringExtra("d_date");
            dodometers = getIntent().getStringExtra("d_odometer");
            dlocations = getIntent().getStringExtra("d_location");
            adates = getIntent().getStringExtra("a_date");
            aodometers = getIntent().getStringExtra("a_odometer");
            alocations = getIntent().getStringExtra("a_location");
            distances = getIntent().getStringExtra("distance");
            times = getIntent().getStringExtra("time");
            speeds = getIntent().getStringExtra("avg_speed");
            parks = getIntent().getStringExtra("parking");
            notes = getIntent().getStringExtra("notes");
            vehicle = getIntent().getStringExtra("vehicle");

            ddate.setText(ddates);
            dodometer.setText(dodometers);
            dlocation.setText(dlocations);
            adate.setText(adates);
            aodometer.setText(aodometers);
            alocation.setText(alocations);
            distance.setText(distances);
            time.setText(times);
            speed.setText(speeds);
            park.setText(parks);
            note.setText(notes);
        }
    }

    void confirmDialog(){
        /* AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + alocations + " ?");
        builder.setMessage("Are you sure you want to delete " + alocations + " ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                trip_records myDB = new trip_records(edit_trip.this);
                myDB.deleteOneRow(id);
                signOut();
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
        title.setText("Delete " + alocations + " ?");
        message.setText("Are you sure you want to delete " + alocations + " ?");

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
                    trip_records myDB = new trip_records(edit_trip.this);
                    myDB.deleteOneRow(id);
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
                            Geocoder gc = new Geocoder(edit_trip.this);
                            List<Address> addresses = gc.getFromLocationName(location, 1); // get the found Address Objects

                            if (!addresses.isEmpty()) {
                                edit_trip.this.location = location;
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
                                Toast.makeText(edit_trip.this, "Location not found!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            Toast.makeText(edit_trip.this, "Location not found!", Toast.LENGTH_SHORT).show();
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

    void signOut(){
        finish();
        Intent intent = new Intent(this, MainActivity.class); // Replace NewActivity with your target activity class name
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed(); // Perform default back action
        finish();
        Intent intent = new Intent(this, MainActivity.class); // Replace NewActivity with your target activity class name
        startActivity(intent);
    }

}