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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.Button;

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

public class edit_expense_reminder extends AppCompatActivity {
    ImageView delete, update, home;
    EditText name, km, day;
    String id, names, kms, days, enables, vehicle;
    RadioButton enable, Rkm, Rday, Both;
    public static HashMap<String, String> alarmReminder = MainActivity.hashMap;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_expense_remind);
        Toolbar toolbar = findViewById(R.id.search_bar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(Html.fromHtml("<font color='#FFFFFF' size='10'>" + "&nbsp;&nbsp;Edit Reminders" +"</font>"));

        delete = findViewById(R.id.delete);
        update = findViewById(R.id.update);
        name = findViewById(R.id.name);
        km = findViewById(R.id.km);
        day = findViewById(R.id.days);
        enable = findViewById(R.id.enable);
        Rkm = findViewById(R.id.Rkm);
        Rday = findViewById(R.id.Rdays);
        Both = findViewById(R.id.both);

        getAndSetIntentData();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (name.getText().toString().equals("") && km.getText().toString().equals("") && day.getText().toString().equals("")) {
                        Toast.makeText(edit_expense_reminder.this, "Please fill all the data", Toast.LENGTH_SHORT).show();
                    } else {
                        updateData();
                    }
                }catch(Exception e){
                    Toast.makeText(edit_expense_reminder.this, "Enter valid data", Toast.LENGTH_SHORT).show();
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
        expenses_reminders myDB = new expenses_reminders(edit_expense_reminder.this);
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
        }else if(Rday.isChecked()){
            enables = "days";
            kms = "";
            days = String.valueOf(day.getText());
        }

        myDB.updateData(id, names, kms, days, vehicle, enables);
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
        alarmReminder.put(id, body);
        String serializedMap = gson.toJson(alarmReminder);
        FileOutputStream fos = openFileOutput("myHashMap1.json", MODE_PRIVATE);
        fos.write(serializedMap.getBytes());
        fos.close();
        finish();
        Intent intent = new Intent(this, Reminders.class); // Replace NewActivity with your target activity class name
        startActivity(intent);
    }

    void getAndSetIntentData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("name") &&
                getIntent().hasExtra("km") && getIntent().hasExtra("days") && getIntent().hasExtra("enable")){
            //Getting Data from Intent
            id = getIntent().getStringExtra("id");
            names = getIntent().getStringExtra("name");
            kms = getIntent().getStringExtra("km");
            days = getIntent().getStringExtra("days");
            vehicle = getIntent().getStringExtra("vehicle");
            enables = getIntent().getStringExtra("enable");

            if(enables.equals("no")){
                enable.setChecked(true);
            }else if(enables.equals("both")){
                Both.setChecked(true);
            }else if(enables.equals("km")){
                Rkm.setChecked(true);
            }else if(enables.equals("days")){
                Rday.setChecked(true);
            }

            name.setText(names);
            km.setText(kms);
            day.setText(days);
        }
    }

    void confirmDialog(){
        /* AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + names + " ?");
        builder.setMessage("Are you sure you want to delete " + names + " ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try{
                    expenses_reminders myDB = new expenses_reminders(edit_expense_reminder.this);
                    myDB.deleteOneRow(id);
                    alarmReminder.remove(id);
                    String serializedMap = gson.toJson(alarmReminder);
                    FileOutputStream fos = openFileOutput("myHashMap1.json", MODE_PRIVATE);
                    fos.write(serializedMap.getBytes());
                    fos.close();
                    signOut();
                }catch(Exception e){
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
        title.setText("Delete " + names + " ?");
        message.setText("Are you sure you want to delete " + names + " ?");

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
                    expenses_reminders myDB = new expenses_reminders(edit_expense_reminder.this);
                    myDB.deleteOneRow(id);
                    alarmReminder.remove(id);
                    String serializedMap = gson.toJson(alarmReminder);
                    FileOutputStream fos = openFileOutput("myHashMap1.json", MODE_PRIVATE);
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

    void signOut(){
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