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
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
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

import com.universl.didul.car_maintenance.database.documents;
import com.universl.didul.car_maintenance.database.expenses_reminders;
import com.universl.didul.car_maintenance.database.fuel_records;
import com.universl.didul.car_maintenance.database.vehicles;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class edit_fill_up extends AppCompatActivity {
    ImageView delete, update, home;
    EditText odometer, quantity, price_per_liter, total, station, note;
    TextView date;
    String id, dates, odometers, quantities, price_per_liters, totals, stations, notes, vehicle;
    ImageView receipt;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri uri;
    private Bitmap bitmap1;
    byte[] imgbyte;
    int odo, tot;
    double pl, lit;
    DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_fill_up);
        Toolbar toolbar = findViewById(R.id.search_bar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(Html.fromHtml("<font color='#FFFFFF' size='10'>" + "&nbsp;&nbsp;Edit Fill-ups" +"</font>"));

        delete = findViewById(R.id.delete);
        update = findViewById(R.id.update);
        date = findViewById(R.id.date);
        odometer = findViewById(R.id.km);
        quantity = findViewById(R.id.ltr);
        price_per_liter = findViewById(R.id.price_ltr);
        total = findViewById(R.id.cost);
        station = findViewById(R.id.station);
        note = findViewById(R.id.notes);
        receipt = findViewById(R.id.img);

        getAndSetIntentData();

        receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, Uri.parse(
                        "content://media/internal/images/media"
                ));
                startActivityForResult(intent,PICK_IMAGE_REQUEST);
            }
        });

        total.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    tot = Integer.parseInt(total.getText().toString());
                    pl = Double.parseDouble(price_per_liter.getText().toString());
                    lit = tot / pl;
                    quantity.setText(df.format(lit));
                }catch(Exception e){
                    Log.d("Pass","");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        price_per_liter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    tot = Integer.parseInt(total.getText().toString());
                    pl = Double.parseDouble(price_per_liter.getText().toString());
                    lit = tot / pl;
                    quantity.setText(df.format(lit));
                }catch(Exception e){
                    Log.d("Pass","");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (date.getText().toString().equals("") && odometer.getText().toString().equals("") && total.getText().toString().equals("")
                            && price_per_liter.getText().toString().equals("")) {
                        Toast.makeText(edit_fill_up.this, "Please fill all the data", Toast.LENGTH_SHORT).show();
                    } else {
                        updateData();
                    }
                }catch(Exception e){
                    Toast.makeText(edit_fill_up.this, "Enter valid data", Toast.LENGTH_SHORT).show();
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

    void updateData(){
        odo = Integer.parseInt(odometer.getText().toString());
        tot = Integer.parseInt(total.getText().toString());
        pl = Double.parseDouble(price_per_liter.getText().toString());
        fuel_records myDB = new fuel_records(edit_fill_up.this);
        dates = date.getText().toString();
        odometers = String.valueOf(odo);
        quantities = String.valueOf(quantity.getText());
        price_per_liters = String.valueOf(pl);
        totals = String.valueOf(tot);
        stations = station.getText().toString();
        notes = note.getText().toString();
        myDB.updateData(id, dates, odometers, quantities, price_per_liters, totals, stations, notes, vehicle, bitmap1);
        finish();
        Intent intent = new Intent(this, MainActivity.class); // Replace NewActivity with your target activity class name
        startActivity(intent);
    }

    void getAndSetIntentData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("date") &&
                getIntent().hasExtra("odometer") && getIntent().hasExtra("quantity") && getIntent().hasExtra("price") && getIntent().hasExtra("total") &&
                getIntent().hasExtra("station") && getIntent().hasExtra("notes") && getIntent().hasExtra("receipt")){
            //Getting Data from Intent
            id = getIntent().getStringExtra("id");
            dates = getIntent().getStringExtra("date");
            odometers = getIntent().getStringExtra("odometer");
            quantities = getIntent().getStringExtra("quantity");
            price_per_liters = getIntent().getStringExtra("price");
            totals = getIntent().getStringExtra("total");
            stations = getIntent().getStringExtra("station");
            notes = getIntent().getStringExtra("notes");
            vehicle = getIntent().getStringExtra("vehicle");
            imgbyte = getIntent().getByteArrayExtra("receipt");
            date.setText(dates);
            odometer.setText(odometers);
            quantity.setText(quantities);
            price_per_liter.setText(price_per_liters);
            total.setText(totals);
            station.setText(stations);
            note.setText(notes);
            //System.out.println(Arrays.toString(imgbyte));
            bitmap1 = BitmapFactory.decodeByteArray(imgbyte, 0, imgbyte.length);
            receipt.setImageBitmap(bitmap1);
        }
    }

    void confirmDialog(){
        /* AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + quantities + " ?");
        builder.setMessage("Are you sure you want to delete " + quantities + " ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                fuel_records myDB = new fuel_records(edit_fill_up.this);
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
        title.setText("Delete " + quantities + " ?");
        message.setText("Are you sure you want to delete " + quantities + " ?");

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
                    fuel_records myDB = new fuel_records(edit_fill_up.this);
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