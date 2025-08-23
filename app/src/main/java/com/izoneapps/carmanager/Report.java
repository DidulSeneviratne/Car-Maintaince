package com.izoneapps.carmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.izoneapps.carmanager.database.fuel_records;
import com.izoneapps.carmanager.database.odometer_records;
import com.izoneapps.carmanager.database.service_records;
import com.izoneapps.carmanager.database.trip_records;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Report extends AppCompatActivity implements AdapterView.OnItemSelectedListener, NavigationView.OnNavigationItemSelectedListener{
    fuel_records myDB1;
    trip_records myDB3;
    service_records myDB2;
    odometer_records myDB4;
    ArrayList<Integer> activity_odo, activity_fcost, activity_scost, activity_ecost, activity_dis;
    ArrayList<Double> activity_avgs, activity_fqua;
    EditText email;
    Button report;
    String mail, body = "";
    Spinner vehicle;
    int difference = 0;
    double kml, kmf, fqa, cfill, cl, cm, fkm, fy, fm, skm, ekm = 0;
    String vandi = "";
    public static HashMap<String, String> vehicles = MainActivity.vehicles;
    ArrayList<String> motor = new ArrayList<>();
    ImageView home, download;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    String fileName = "downloads";
    File outputFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report);
        drawerLayout = findViewById(R.id.select_lines_root);
        navigationView = findViewById(R.id.nav_view);

        Toolbar toolbar = findViewById(R.id.search_bar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(Html.fromHtml("<font color='#FFFFFF' size='10'>" + "&nbsp;&nbsp;Report Generate" +"</font>"));

        navigationView.bringToFront();
        toolbar.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        vehicle = findViewById(R.id.vehicle);
        home = findViewById(R.id.home);
        download = findViewById(R.id.download);
        myDB1 = new fuel_records(Report.this);
        myDB3 = new trip_records(Report.this);
        myDB2 = new service_records(Report.this);
        myDB4 = new odometer_records(Report.this);
        email = findViewById(R.id.email);
        report = findViewById(R.id.report);

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().equals("")){
                    Toast.makeText(Report.this, "Please fill the email", Toast.LENGTH_SHORT).show();
                }else{
                    generateEmail();
                }
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Report.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    File textFile = TextFileGenerator.generateTextFile(body);
                    if (textFile != null) {
                        Toast.makeText(Report.this, "Text file downloaded (look your download directory)", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Report.this, "Failed to generate text file", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e){
                    Log.d("error",String.valueOf(e));
                }
            }
        });

        try {
            Set<String> keySet = vehicles.keySet();
            Log.d("reminder", keySet.toString());

            // Iterate over the keySet using a for loop
            for (String key : keySet) {
                // Get the corresponding value from the HashMap using the get() method
                String value = vehicles.get(key);
                motor.add(value);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner, motor);
        vehicle.setAdapter(adapter);

        vehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vandi = (String) vehicle.getSelectedItem();
                activity_odo = new ArrayList<>();
                activity_fqua = new ArrayList<>();
                activity_fcost = new ArrayList<>();
                activity_scost = new ArrayList<>();
                activity_ecost = new ArrayList<>();
                activity_dis = new ArrayList<>();
                activity_avgs = new ArrayList<>();
                storeDataInArrays1();
                storeDataInArrays2();
                storeDataInArrays3();
                storeDataInArrays4();
                int fqua = 0;
                int fcost = 0;
                int scost = 0;
                int ecost = 0;

                for (int number : activity_fcost) {
                    fcost += number;
                }for (double number : activity_fqua) {
                    fqua += number;
                }for (int number : activity_scost) {
                    scost += number;
                }for (int number : activity_ecost) {
                    ecost += number;
                }

                try {
                    if (activity_odo.size() == 1) {
                        int value = activity_odo.get(0);
                        difference = value;
                    } else if (activity_odo.size() > 1) {
                        int max = Collections.max(activity_odo);
                        int min = Collections.min(activity_odo);
                        difference = max - min;
                    } else {
                        difference = 0;
                    }
                }catch(Exception e){
                    difference = 0;
                }

                int Cfillups = activity_fqua.size();
                double total_cost = fcost + scost + ecost;
                double ckm = total_cost/difference;

                int ttripd = 0;
                for (int number : activity_dis) {
                    ttripd += number;
                }

                int avg_s = 0;
                for (double number : activity_avgs) {
                    avg_s += number;
                }

                int tcount = activity_avgs.size();

                try {
                    avg_s = avg_s / tcount;
                }catch (Exception e){
                    avg_s = 0;
                }
                try {
                    kml = difference / fqua;
                }catch (Exception e){
                    kml = 0.00;
                }
                try {
                    kmf = difference / Cfillups;
                }catch (Exception e){
                    kmf = 0.00;
                }
                try {
                    fqa = fqua / Cfillups;
                }catch (Exception e){
                    fqa = 0.00;
                }
                try {
                    cfill = total_cost / Cfillups;
                }catch (Exception e){
                    cfill = 0.00;
                }
                try {
                    cl = total_cost / fqua;
                }catch (Exception e){
                    cl = 0.00;
                }
                try {
                    cm = Cfillups / 12;
                }catch (Exception e){
                    cm = 0.00;
                }
                try {
                    fkm = fcost / difference;
                }catch (Exception e){
                    fkm = 0.00;
                }
                try {
                    fy = fcost / 365;
                }catch (Exception e){
                    fy = 0.00;
                }
                try {
                    fm = fcost / 12;
                }catch (Exception e){
                    fm = 0.00;
                }try {
                    skm = scost / difference;
                }catch (Exception e){
                    skm = 0.00;
                }try {
                    ekm = ecost / difference;
                }catch (Exception e){
                    ekm = 0.00;
                }

                body =  vandi + "\n\n" +
                        "Distance: " + difference + " km\n" +
                        "Fill-ups: " + Cfillups + "\n" +
                        "Fuel Qty: " + fqua + " l\n" +
                        "Fuel Cost: " + fcost + " LKR\n" +
                        "Service Cost: " + scost + " LKR\n" +
                        "Other Expenses: " + ecost + " LKR\n" +
                        "Total Cost: " + total_cost + " LKR\n" +
                        "Total Cost/km: " + ckm + " LKR\n\n" +
                        "Avg Fuel Eff: " + kml + " km/l\n" +
                        "Dist Btwn Fill-Ups: " + kmf + " km\n" +
                        "Qty Per Fill-Up: " + fqa + " l\n" +
                        "Cost Per Fill-Up: " + cfill + " LKR\n" +
                        "Avg Price/Ltr: " + cl + " LKR\n" +
                        "Fill-Ups per Mth: " + cm + "\n" +
                        "Fuel Cost/km: " + fkm + " LKR\n" +
                        "Fuel Cost/Day: " + fy + " LKR\n" +
                        "Fuel Cost/Mth: " + fm + " LKR\n\n" +
                        "Service Cost/km: " + skm + " LKR\n\n" +
                        "Other Expenses/km: " + ekm + " LKR\n\n" +
                        "Total Trips: " + tcount + "\n" +
                        "Total Trip Distance: " + ttripd + " km\n" +
                        "Avg Speed: " + avg_s + " kmh\n";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    void storeDataInArrays1(){
        Cursor cursor = myDB1.readAllData1(vandi);
        if(cursor.getCount() != 0){
            while (cursor.moveToNext()){
                try {
                    activity_fqua.add(Double.valueOf(cursor.getString(3)));
                    activity_fcost.add(Integer.valueOf(cursor.getString(5)));
                }catch(Exception e){
                    System.out.println(e);
                }
            }
        }
    }

    void storeDataInArrays2(){
        Cursor cursor = myDB2.readAllData1(vandi);
        if(cursor.getCount() != 0){
            while (cursor.moveToNext()){
                try {
                    activity_scost.add(Integer.valueOf(cursor.getString(4)));
                }catch(Exception e){
                    System.out.println(e);
                }
            }
        }
        Cursor cursor1 = myDB2.readAllData2(vandi);
        if(cursor1.getCount() != 0){
            while (cursor1.moveToNext()){
                try {
                    activity_ecost.add(Integer.valueOf(cursor1.getString(4)));
                }catch(Exception e){
                    System.out.println(e);
                }
            }
        }
    }

    void storeDataInArrays3(){
        Cursor cursor = myDB3.readAllData1(vandi);
        if(cursor.getCount() != 0){
            while (cursor.moveToNext()){
                try {
                    activity_dis.add(Integer.valueOf(cursor.getString(7)));
                    activity_avgs.add(Double.valueOf(cursor.getString(9)));
                }catch(Exception e){
                    System.out.println(e);
                }
            }
        }
    }

    void storeDataInArrays4(){
        Cursor cursor = myDB4.readAllData1(vandi);
        if(cursor.getCount() != 0){
            while (cursor.moveToNext()){
                try {
                    activity_odo.add(Integer.valueOf(cursor.getString(2)));
                }catch(Exception e){
                    System.out.println(e);
                }
            }
        }
    }

    void generateEmail(){
        try {
            mail = String.valueOf(email.getText());
            String stringSenderEmail = "dulsaradidul@gmail.com";
            String stringReceiverEmail = mail;
            String stringPasswordSenderEmail = "njit bsfh arfi ctxo";

            String stringHost = "smtp.gmail.com";

            Properties properties = System.getProperties();

            properties.put("mail.smtp.host", stringHost);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");

            javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);
                }
            });

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));

            mimeMessage.setSubject("Vehicle Report");
            mimeMessage.setText(body);

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(mimeMessage);
                        Log.d("text","Successfully");
                        //Toast.makeText(Report.this, "Successfully sent", Toast.LENGTH_SHORT).show();
                    } catch (MessagingException e) {
                        //Toast.makeText(Report.this, String.valueOf(e), Toast.LENGTH_SHORT).show();
                        Log.d("text",String.valueOf(e));
                    }
                }
            });
            thread.start();
            Toast.makeText(Report.this, "Your successfully send the report", Toast.LENGTH_SHORT).show();
            finish();

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
            Intent intent = new Intent(Report.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.vehicles) {
            Intent intent = new Intent(Report.this, Vehicle.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.reminders) {
            Intent intent = new Intent(Report.this, Reminders.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.report) {
            Intent intent = new Intent(Report.this, Report.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.rate) {
            rate();
        }
        return true;
    }

    public void rate() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View customView = inflater.inflate(R.layout.dialog_rate, null);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(customView)
                .create();

        // Handle custom buttons
        TextView btnYes = customView.findViewById(R.id.btnYes);
        TextView btnNo = customView.findViewById(R.id.btnNo);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.universl.didul.car_maintenance")));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.universl.didul.car_maintenance")));
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
}