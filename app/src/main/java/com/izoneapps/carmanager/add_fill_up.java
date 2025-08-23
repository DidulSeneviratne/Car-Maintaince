package com.izoneapps.carmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.izoneapps.carmanager.database.fuel_records;
import com.izoneapps.carmanager.database.odometer_records;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class add_fill_up extends AppCompatActivity {
    ImageView save, home;
    EditText odometer, quantity, price_per_liter, total, station, note;
    String dates, odometers, quantities, price_per_liters, totals, stations, notes, vehicle;
    ImageView receipt;
    TextView date;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri uri;
    private Bitmap bitmap1;
    int odo, tot;
    double pl, lit;
    DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_fill_up);
        save = findViewById(R.id.save);
        date = findViewById(R.id.date);
        odometer = findViewById(R.id.km);
        quantity = findViewById(R.id.ltr);
        price_per_liter = findViewById(R.id.price_ltr);
        total = findViewById(R.id.tot);
        station = findViewById(R.id.station);
        note = findViewById(R.id.note);
        receipt = findViewById(R.id.img);
        vehicle = getIntent().getStringExtra("vehicle");

        Toolbar toolbar = findViewById(R.id.search_bar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(Html.fromHtml("<font color='#FFFFFF' size='10'>" + "&nbsp;&nbsp;Add Fill-ups" +"</font>"));

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

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (date.getText().toString().equals("") && odometer.getText().toString().equals("") && total.getText().toString().equals("")
                            && price_per_liter.getText().toString().equals("")) {
                        Toast.makeText(add_fill_up.this, "Please fill all the data", Toast.LENGTH_SHORT).show();
                    } else {
                        odo = Integer.parseInt(odometer.getText().toString());
                        tot = Integer.parseInt(total.getText().toString());
                        pl = Double.parseDouble(price_per_liter.getText().toString());
                        saveEventAction();
                    }
                }catch(Exception e){
                    Toast.makeText(add_fill_up.this, "Enter valid data", Toast.LENGTH_SHORT).show();
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
        fuel_records myDB = new fuel_records(add_fill_up.this);
        odometer_records myDB1 = new odometer_records(add_fill_up.this);
        String sharedId = UUID.randomUUID().toString();
        dates = date.getText().toString();
        odometers = String.valueOf(odo);
        quantities = df.format(lit);
        price_per_liters = String.valueOf(pl);
        totals = String.valueOf(tot);
        stations = station.getText().toString();
        notes = note.getText().toString();
        myDB.insertData(sharedId, dates, odometers, quantities, price_per_liters, totals, stations, notes, vehicle, bitmap1);
        myDB1.insertData(sharedId, dates, odometers, notes, vehicle);
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
