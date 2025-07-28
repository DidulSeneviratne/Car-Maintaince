package com.universl.didul.car_maintenance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.universl.didul.car_maintenance.database.fuel_records;
import com.universl.didul.car_maintenance.database.odometer_records;

import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class add_odometer extends AppCompatActivity {
    ImageView save, home;
    EditText odometer, note;
    TextView date;
    String dates, odometers, notes, vehicle;
    int odo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_odometer);
        save = findViewById(R.id.save);
        date = findViewById(R.id.date);
        odometer = findViewById(R.id.odometer);
        note = findViewById(R.id.note);
        vehicle = getIntent().getStringExtra("vehicle");

        Toolbar toolbar = findViewById(R.id.search_bar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(Html.fromHtml("<font color='#FFFFFF' size='10'>" + "&nbsp;&nbsp;Add Odometer" +"</font>"));

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (date.getText().toString().equals("") && odometer.getText().toString().equals("")) {
                        Toast.makeText(add_odometer.this, "Please fill all the data", Toast.LENGTH_SHORT).show();
                    } else {
                        odo = Integer.parseInt(odometer.getText().toString());
                        saveEventAction();
                    }
                }catch(Exception e){
                    Toast.makeText(add_odometer.this, "Enter valid data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void saveEventAction() {
        odometer_records myDB = new odometer_records(add_odometer.this);
        dates = date.getText().toString();
        odometers = String.valueOf(odo);
        notes = note.getText().toString();
        myDB.insertData(dates, odometers, notes, vehicle);
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