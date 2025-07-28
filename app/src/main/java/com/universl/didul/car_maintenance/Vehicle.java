package com.universl.didul.car_maintenance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.universl.didul.car_maintenance.Adapter.CustomAdapter1;
import com.universl.didul.car_maintenance.database.vehicles;

import java.util.ArrayList;
import java.util.Objects;

public class Vehicle extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    FloatingActionButton mAddReminderButton;
    RecyclerView recyclerView;
    vehicles myDB;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ArrayList<String> activity_id, activity_make, activity_model, activity_year, activity_lic, activity_chassi, activity_insurance, activity_fuel;
    ArrayList<byte[]> activity_img;
    CustomAdapter1 customAdapter;
    ImageView home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vehicles);
        Toolbar toolbar = findViewById(R.id.search_bar);
        drawerLayout = findViewById(R.id.select_lines_root);
        navigationView = findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(Html.fromHtml("<font color='#FFFFFF' size='10'>" + "&nbsp;&nbsp;Vehicles" +"</font>"));

        navigationView.bringToFront();
        toolbar.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        home = findViewById(R.id.home);
        recyclerView = findViewById(R.id.recyclerView);
        mAddReminderButton = findViewById(R.id.add);
        mAddReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Vehicle.this, Vehicle_add.class);
                startActivity(intent);
                finish();
            }
        });

        myDB = new vehicles(Vehicle.this);
        activity_id = new ArrayList<>();
        activity_img = new ArrayList<>();
        activity_make = new ArrayList<>();
        activity_model = new ArrayList<>();
        activity_year = new ArrayList<>();
        activity_lic = new ArrayList<>();
        activity_chassi = new ArrayList<>();
        activity_insurance = new ArrayList<>();
        activity_fuel = new ArrayList<>();
        storeDataInArrays();

        customAdapter = new CustomAdapter1(Vehicle.this,this, activity_id, activity_img, activity_make, activity_model, activity_year, activity_lic, activity_chassi, activity_insurance, activity_fuel);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Vehicle.this));

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Vehicle.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    void storeDataInArrays(){
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount() != 0){
            while (cursor.moveToNext()){
                try {
                    activity_id.add(cursor.getString(0));
                    activity_img.add(cursor.getBlob(1));
                    activity_make.add(cursor.getString(2));
                    activity_model.add(cursor.getString(3));
                    activity_year.add(cursor.getString(4));
                    activity_lic.add(cursor.getString(5));
                    activity_chassi.add(cursor.getString(6));
                    activity_insurance.add(cursor.getString(7));
                    activity_fuel.add(cursor.getString(8));
                }catch(Exception e){
                    System.out.println(e);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed(); // Perform default back action
        finish();
        Intent intent = new Intent(this, MainActivity.class); // Replace NewActivity with your target activity class name
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            Intent intent = new Intent(Vehicle.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.vehicles) {
            Intent intent = new Intent(Vehicle.this, Vehicle.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.reminders) {
            Intent intent = new Intent(Vehicle.this, Reminders.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.report) {
            Intent intent = new Intent(Vehicle.this, Report.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.rate) {
            rate();
        }
        return true;
    }

    public void rate() {
        androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(this);
        alert.setIcon(R.mipmap.ic_launcher);
        alert.setTitle(R.string.app_name);
        alert.setMessage("Do you like this app?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.universl.didul.car_maintenance")));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.universl.didul.car_maintenance")));
                }
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.create().show();
    }
}