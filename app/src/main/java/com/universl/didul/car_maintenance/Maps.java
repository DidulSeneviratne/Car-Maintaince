package com.universl.didul.car_maintenance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;

/*import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;*/

import com.universl.didul.car_maintenance.database.expenses_reminders;
import com.universl.didul.car_maintenance.database.service_reminders;

import java.util.Objects;

public class Maps extends AppCompatActivity {

    ImageView home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps);

        Toolbar toolbar = findViewById(R.id.search_bar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(Html.fromHtml("<font color='#FFFFFF' size='10'>" + "&nbsp;&nbsp;Maps" +"</font>"));

        home = findViewById(R.id.home);
        /*MapView mapView = findViewById(R.id.mapView);
        GeoPoint startPoint = new GeoPoint(7.8731, 80.7718); // Replace with your specific location
        mapView.getController().setCenter(startPoint);
        mapView.getController().setZoom(4.0); // Adjust the zoom level as needed

        // Add a marker at the specified location (optional)
        // You can customize the marker using different overlays or markers

        // Enable the my location overlay (optional)
        MyLocationNewOverlay myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), mapView);
        mapView.getOverlays().add(myLocationOverlay);
        myLocationOverlay.enableMyLocation();*/

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Maps.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


}
