package com.universl.didul.car_maintenance.fragments;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.universl.didul.car_maintenance.MainActivity;
import com.universl.didul.car_maintenance.R;
import com.universl.didul.car_maintenance.database.fuel_records;
import com.universl.didul.car_maintenance.database.odometer_records;
import com.universl.didul.car_maintenance.database.service_records;
import com.universl.didul.car_maintenance.database.trip_records;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class SecondFragment extends Fragment {
    fuel_records myDB1;
    trip_records myDB3;
    service_records myDB2;
    odometer_records myDB4;
    ArrayList<String> activity_veh;
    ArrayList<Integer> activity_odo, activity_fcost, activity_scost, activity_ecost, activity_dis;
    ArrayList<Double> activity_avgs, activity_fqua;
    TextView distance, fillups, fuelqty, fuelc, servicec, oexpence, tcost, tcostkm,
            avgf, dfill, qfill, cfill, avgl, fillm, costkm, costd, costm,
            serck, expck, ttrips, ttripsd, avgs;
    Spinner vehicle;
    int difference = 0;
    String vandi = "";
    public static HashMap<String, String> vehicles = MainActivity.vehicles;
    ArrayList<String> motor = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        distance = view.findViewById(R.id.dis);
        fillups = view.findViewById(R.id.fills);
        fuelqty = view.findViewById(R.id.fquan);
        fuelc = view.findViewById(R.id.fcost);
        servicec = view.findViewById(R.id.scost);
        oexpence = view.findViewById(R.id.exp);
        tcost = view.findViewById(R.id.tcost);
        tcostkm = view.findViewById(R.id.costkm);
        avgf = view.findViewById(R.id.avgf);
        dfill = view.findViewById(R.id.dfill);
        qfill = view.findViewById(R.id.qfill);
        cfill = view.findViewById(R.id.cfill);
        avgl = view.findViewById(R.id.avgfp);
        fillm = view.findViewById(R.id.fillm);
        costkm = view.findViewById(R.id.fillck);
        costd = view.findViewById(R.id.fillcd);
        costm = view.findViewById(R.id.fillcm);
        serck = view.findViewById(R.id.serck);
        expck = view.findViewById(R.id.expck);
        ttrips = view.findViewById(R.id.ttrips);
        ttripsd = view.findViewById(R.id.ttripd);
        avgs = view.findViewById(R.id.avgs);
        vehicle = view.findViewById(R.id.vehicle);

        myDB1 = new fuel_records(getActivity());
        myDB3 = new trip_records(getActivity());
        myDB2 = new service_records(getActivity());
        myDB4 = new odometer_records(getActivity());

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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner, motor);
        vehicle.setAdapter(adapter);

        vehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vandi = (String) vehicle.getSelectedItem();
                //System.out.println(vandi);
                activity_odo = new ArrayList<>();
                activity_fqua = new ArrayList<>();
                activity_fcost = new ArrayList<>();
                activity_scost = new ArrayList<>();
                activity_ecost = new ArrayList<>();
                activity_dis = new ArrayList<>();
                activity_avgs = new ArrayList<>();
                activity_veh = new ArrayList<>();
                storeDataInArrays1();
                storeDataInArrays2();
                storeDataInArrays3();
                storeDataInArrays4();
                DecimalFormat df = new DecimalFormat("0.00");
                int fqua = 0;
                for (double number : activity_fqua) {
                    fqua += number;
                }
                fuelqty.setText(String.valueOf(fqua) + " l");

                int fcost = 0;
                for (int number : activity_fcost) {
                    fcost += number;
                }
                fuelc.setText(String.valueOf(fcost) + " LKR");

                int scost = 0;
                System.out.println(activity_scost);
                for (int number : activity_scost) {
                    scost += number;
                }
                servicec.setText(String.valueOf(scost) + " LKR");

                int ecost = 0;
                System.out.println(activity_ecost);
                for (int number : activity_ecost) {
                    ecost += number;
                }
                oexpence.setText(String.valueOf(ecost) + " LKR");

                try {
                    if (activity_odo.size() == 1) {
                        int value = activity_odo.get(0);
                        difference = value;
                        distance.setText(value + " km");
                    } else if (activity_odo.size() > 1) {
                        int max = Collections.max(activity_odo);
                        int min = Collections.min(activity_odo);
                        difference = max - min;
                        distance.setText(difference + " km");
                    } else {
                        distance.setText("0 km");
                    }
                }catch(Exception e){
                    distance.setText("0 km");
                }

                int Cfillups = activity_fqua.size();
                fillups.setText(String.valueOf(Cfillups));
                double total_cost = fcost + scost + ecost;
                tcost.setText(total_cost + " LKR");

                int ttripd = 0;
                for (int number : activity_dis) {
                    ttripd += number;
                }
                ttripsd.setText(String.valueOf(ttripd));

                int tcount = activity_avgs.size();
                ttrips.setText(String.valueOf(tcount));

                int avg_s = 0;
                for (double number : activity_avgs) {
                    avg_s += number;
                }

                try {
                    avgs.setText(df.format(avg_s / tcount) + "kmh");
                }catch (Exception e){
                    avgs.setText("n/a");
                }
                try {
                    if(String.valueOf(total_cost / difference) == "Infinity"){
                        tcostkm.setText("0.00 LKR");
                    }else{
                        tcostkm.setText(df.format(total_cost / difference) + " LKR");
                    }
                }catch (Exception e){
                    tcostkm.setText("0.00 LKR");
                }
                try {
                    avgf.setText(df.format(difference / fqua) + " km/l");
                }catch (Exception e){
                    avgf.setText("0 km/l");
                }
                try {
                    dfill.setText(df.format(difference / Cfillups) + " km");
                }catch (Exception e){
                    dfill.setText("0 km");
                }
                try {
                    qfill.setText(df.format(fqua / Cfillups) + " l");
                }catch (Exception e){
                    qfill.setText("0 l");
                }
                try {
                    cfill.setText(df.format(total_cost / Cfillups) + " LKR");
                }catch (Exception e){
                    cfill.setText("0.00 LKR");
                }
                try {
                    avgl.setText(df.format(total_cost / fqua) + " LKR");
                }catch (Exception e){
                    avgl.setText("0 LKR");
                }
                try {
                    fillm.setText(df.format(Cfillups / 12));
                }catch (Exception e){
                    fillm.setText("0");
                }
                try {
                    costkm.setText(df.format(fcost / difference) + " LKR");
                }catch (Exception e){
                    costkm.setText("0.00 LKR");
                }
                try {
                    costd.setText(df.format(fcost / 365) + " LKR");
                }catch (Exception e){
                    costd.setText("0.00 LKR");
                }try {
                    costm.setText(df.format(fcost / 12) + " LKR");
                }catch (Exception e){
                    costm.setText("0.00 LKR");
                }try {
                    serck.setText(df.format(scost / difference) + " LKR");
                }catch (Exception e){
                    serck.setText("0.00 LKR");
                }try {
                    expck.setText(df.format(ecost / difference) + " LKR");
                }catch (Exception e){
                    expck.setText("0.00 LKR");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    void storeDataInArrays1(){
        Cursor cursor = myDB1.readAllData1(vandi);
        if(cursor.getCount() != 0){
            while (cursor.moveToNext()){
                try {
                    activity_fqua.add(Double.valueOf(cursor.getString(3)));
                    activity_fcost.add(Integer.valueOf(cursor.getString(5)));
                    activity_veh.add(cursor.getString(8));
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

}