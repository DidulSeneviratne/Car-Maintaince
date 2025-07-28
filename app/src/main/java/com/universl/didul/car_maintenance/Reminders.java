package com.universl.didul.car_maintenance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class Reminders extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    MyViewPageAdapter1 myViewPageAdapter;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminders);

        Toolbar toolbar = findViewById(R.id.search_bar);
        drawerLayout = findViewById(R.id.select_lines_root);
        navigationView = findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(Html.fromHtml("<font color='#FFFFFF' size='10'>" + "&nbsp;&nbsp;Reminders" +"</font>"));

        navigationView.bringToFront();
        toolbar.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        home = findViewById(R.id.home);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.view_pager);
        myViewPageAdapter = new MyViewPageAdapter1(this);
        viewPager2.setAdapter(myViewPageAdapter);

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

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Reminders.this, MainActivity.class);
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
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            Intent intent = new Intent(Reminders.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.vehicles) {
            Intent intent = new Intent(Reminders.this, Vehicle.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.reminders) {
            Intent intent = new Intent(Reminders.this, Reminders.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.report) {
            Intent intent = new Intent(Reminders.this, Report.class);
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