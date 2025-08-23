package com.izoneapps.carmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.izoneapps.carmanager.database.odometer_records;

import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

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
        String sharedId = UUID.randomUUID().toString();
        dates = date.getText().toString();
        odometers = String.valueOf(odo);
        notes = note.getText().toString();
        myDB.insertData(sharedId, dates, odometers, notes, vehicle);
        finish();
        Intent intent = new Intent(this, MainActivity.class); // Replace NewActivity with your target activity class name
        startActivity(intent);
    }

    public void add(View view) {
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
            date.setText(selectedDate);  // update your TextView
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed(); // Perform default back action
        finish();
        Intent intent = new Intent(this, MainActivity.class); // Replace NewActivity with your target activity class name
        startActivity(intent);
    }
}