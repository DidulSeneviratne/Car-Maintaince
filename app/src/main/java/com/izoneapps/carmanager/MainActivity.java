package com.izoneapps.carmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.izoneapps.carmanager.database.odometer_records;

import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    MyViewPageAdapter myViewPageAdapter;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Thread thread;
    ArrayList<Integer> activity_odo;
    private static final String CHANNEL_ID = "simplified_coding";
    private static final String CHANNEL_NAME = "Simplified Coding";
    private static final String CHANNEL_DESC = "Simplified Coding Notification";
    public static HashMap<String, String> hashMap = new HashMap<>();
    public static HashMap<String, String> vehicles = new HashMap<>();
    public static long dayDiff;
    ImageView power, vehicle;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Gson gson = new Gson();
    odometer_records myDB;
    SharedPreferences prefs;
    String firstLaunchDateKey = "first_launch_date";
    String today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        String firstDate = prefs.getString(firstLaunchDateKey, null);

        today = getTodayDate();
        if (firstDate == null) {
            // First time launch
            prefs.edit().putString(firstLaunchDateKey, today).apply();
            // Log.d("LaunchDate", "First launch, date saved: " + today);
        } else {
            // Not first launch, calculate difference
            dayDiff = getDaysBetween(firstDate, today);
        }

        Toolbar toolbar = findViewById(R.id.search_bar);
        drawerLayout = findViewById(R.id.select_lines_root);
        navigationView = findViewById(R.id.nav_view);
        power = findViewById(R.id.power);
        vehicle = findViewById(R.id.vehicle);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(Html.fromHtml("<font color='#FFFFFF' size='10'>" + "&nbsp;&nbsp;Car Maintenance" +"</font>"));

        navigationView.bringToFront();
        toolbar.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.view_pager);
        myViewPageAdapter = new MyViewPageAdapter(this);
        viewPager2.setAdapter(myViewPageAdapter);
        activity_odo = new ArrayList<>();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });

        vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Vehicle.class);
                startActivity(intent);
                finish();
            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });

        try {
            FileInputStream fis = openFileInput("myHashMap.json");
            byte[] data = new byte[fis.available()];
            fis.read(data);
            fis.close();

            String json = new String(data);
            vehicles = gson.fromJson(json, new TypeToken<HashMap<String, String>>() {
            }.getType());
        }catch (Exception e){
            System.out.println(e);
        }

        try {
            FileInputStream fis = openFileInput("myHashMap1.json");
            byte[] data = new byte[fis.available()];
            fis.read(data);
            fis.close();

            String json = new String(data);
            hashMap = gson.fromJson(json, new TypeToken<HashMap<String, String>>() {
            }.getType());
        }catch (Exception e){
            System.out.println(e);
        }

        thread = null;

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        Calendar calendar = Calendar.getInstance();
                        Date date = calendar.getTime();
                        String formattedDate = dateFormat.format(date);

                        Set<String> keySet = hashMap.keySet();
                        Log.d("reminder", keySet.toString());

                        // Iterate over the keySet using a for loop
                        for (String key : keySet) {
                            // Get the corresponding value from the HashMap using the get() method
                            String value = hashMap.get(key);
                            String[] list = value.split(" ");

                            String vandi = list[4];
                            int odo = storeDataInArrays(vandi);

                            Log.d("reminder", value);
                            if ("both".equals(list[1]) || "km".equals(list[1]) || "days".equals(list[1])) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                                    channel.setDescription(CHANNEL_DESC);
                                    NotificationManager manager = getSystemService(NotificationManager.class);
                                    manager.createNotificationChannel(channel);
                                }
                                if (formattedDate.equals(list[3])){
                                    hashMap.remove(key);
                                }
                                if (String.valueOf(odo).equals(list[2])){
                                    hashMap.remove(key);
                                }
                                displayNotification(key, list[0] + " " + list[4]);
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();

    }

    private String getTodayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    private long getDaysBetween(String oldDateStr, String newDateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date oldDate = sdf.parse(oldDateStr);
            Date newDate = sdf.parse(newDateStr);

            long diffInMillis = newDate.getTime() - oldDate.getTime();
            return TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void displayNotification(String key, String value) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("You got a event!")
                        .setContentText(value + " - " + key)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat mNotificationMgr = NotificationManagerCompat.from(this);
        Log.d("msg", "notify");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mNotificationMgr.notify(1, mBuilder.build());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.vehicles) {
            Intent intent = new Intent(MainActivity.this, Vehicle.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.reminders) {
            Intent intent = new Intent(MainActivity.this, Reminders.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.report) {
            Intent intent = new Intent(MainActivity.this, Report.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.rate) {
            rate();
        }
        return true;
        /* else if (id == R.id.maps) {
            Intent intent = new Intent(MainActivity.this, Maps.class);
            startActivity(intent);
        }*/
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


    int storeDataInArrays(String vandi){
        Cursor cursor = myDB.readAllData1(vandi);
        if(cursor.getCount() != 0){
            while (cursor.moveToNext()){
                try {
                    activity_odo.add(Integer.valueOf(cursor.getString(2)));
                }catch(Exception e){
                    System.out.println(e);
                }
            }
        }
        int max = 0;
        try {
            max = Collections.max(activity_odo);
        }catch(Exception e){
            e.printStackTrace();
        }

        return max;
    }
}