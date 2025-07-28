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
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.universl.didul.car_maintenance.database.expenses_reminders;
import com.universl.didul.car_maintenance.database.service_reminders;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class add_service_reminder extends AppCompatActivity {
    ImageView save, home;
    EditText name, km, day, odometer, date;
    RadioButton enable, Rkm, Rdays, Both;
    String names, kms, days, enables, odometers, dates, vehicle;
    public static HashMap<String, String> alarmReminder = MainActivity.hashMap;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_service_remind);
        save = findViewById(R.id.save);
        // home = findViewById(R.id.home);
        name = findViewById(R.id.name);
        km = findViewById(R.id.km);
        day = findViewById(R.id.days);
        enable = findViewById(R.id.enable);
        Rkm = findViewById(R.id.Rkm);
        Rdays = findViewById(R.id.Rdays);
        Both = findViewById(R.id.both);
        odometer = findViewById(R.id.odometer);
        date = findViewById(R.id.date);
        vehicle = getIntent().getStringExtra("vehicle");

        Toolbar toolbar = findViewById(R.id.search_bar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(Html.fromHtml("<font color='#FFFFFF' size='10'>" + "&nbsp;&nbsp;Add Reminders" +"</font>"));


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (name.getText().toString().equals("") && km.getText().toString().equals("") && day.getText().toString().equals("")
                        && odometer.getText().toString().equals("") && date.getText().toString().equals("")) {
                        Toast.makeText(add_service_reminder.this, "Please fill all the data", Toast.LENGTH_SHORT).show();
                    } else {
                        saveEventAction();
                    }
                }catch(Exception e){
                    Toast.makeText(add_service_reminder.this, "Enter valid data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void saveEventAction() throws IOException {
        service_reminders myDB = new service_reminders(add_service_reminder.this);
        names = name.getText().toString();
        if(enable.isChecked()){
            enables = "no";
            kms = "";
            days = "";
        }else if(Both.isChecked()){
            enables = "both";
            kms = String.valueOf(km.getText());
            days = String.valueOf(day.getText());
        }else if(Rkm.isChecked()){
            enables = "km";
            kms = String.valueOf(km.getText());
            days = "";
        }else if(Rdays.isChecked()){
            enables = "days";
            kms = "";
            days = String.valueOf(day.getText());
        }
        odometers = odometer.getText().toString();
        dates = date.getText().toString();
        long result = myDB.insertData(names, kms, days, enables, odometers, dates, vehicle);
        if(result != -1){
            LocalDate currentDate = LocalDate.now();
            // Add 500 days to the current date
            LocalDate futureDate;
            if(!days.isEmpty()){
                futureDate = currentDate.plusDays(Long.parseLong(days));
            }else{
                futureDate = currentDate.plusDays(0);
            }
            // Create a formatter for the desired output format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            // Format the future date with the specified format
            String fDate = futureDate.format(formatter);
            String body = names + " " + enables + " " + kms + " " + fDate + vehicle;
            alarmReminder.put(String.valueOf(result), body);
            String serializedMap = gson.toJson(alarmReminder);
            FileOutputStream fos = openFileOutput("myHashMap1.json", MODE_PRIVATE);
            fos.write(serializedMap.getBytes());
            fos.close();
        }
        finish();
        Intent intent = new Intent(this, Reminders.class); // Replace NewActivity with your target activity class name
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
        Intent intent = new Intent(this, Reminders.class); // Replace NewActivity with your target activity class name
        startActivity(intent);
    }

}