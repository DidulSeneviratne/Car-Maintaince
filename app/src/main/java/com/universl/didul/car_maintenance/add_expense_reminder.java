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
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.universl.didul.car_maintenance.database.expenses_reminders;
import com.universl.didul.car_maintenance.database.fuel_records;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Objects;

public class add_expense_reminder extends AppCompatActivity {
    ImageView save, home;
    EditText name, km, day;
    RadioButton enable, Rkm, Rdays, Both;
    String names, kms, days, enables, vehicle;
    public static HashMap<String, String> alarmReminder = MainActivity.hashMap;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_expense_remind);
        save = findViewById(R.id.save);
        name = findViewById(R.id.name);
        km = findViewById(R.id.km);
        day = findViewById(R.id.days);
        enable = findViewById(R.id.enable);
        Rkm = findViewById(R.id.Rkm);
        Rdays = findViewById(R.id.Rdays);
        Both = findViewById(R.id.both);
        vehicle = getIntent().getStringExtra("vehicle");

        Toolbar toolbar = findViewById(R.id.search_bar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(Html.fromHtml("<font color='#FFFFFF' size='10'>" + "&nbsp;&nbsp;Add Reminders" +"</font>"));


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (name.getText().toString().equals("") && km.getText().toString().equals("") && day.getText().toString().equals("")) {
                        Toast.makeText(add_expense_reminder.this, "Please fill all the data", Toast.LENGTH_SHORT).show();
                    } else {
                        saveEventAction();
                    }
                }catch(Exception e){
                    Toast.makeText(add_expense_reminder.this, "Enter valid data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void saveEventAction() throws IOException {
        expenses_reminders myDB = new expenses_reminders(add_expense_reminder.this);
        names = String.valueOf(name.getText());
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
        long result = myDB.insertData(names, kms, days, vehicle, enables);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed(); // Perform default back action
        finish();
        Intent intent = new Intent(this, Reminders.class); // Replace NewActivity with your target activity class name
        startActivity(intent);
    }

}