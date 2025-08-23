package com.izoneapps.carmanager.fragments;

import android.database.Cursor;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.izoneapps.carmanager.Adapter.CustomAdapter2;
import com.izoneapps.carmanager.Adapter.CustomAdapter3;
import com.izoneapps.carmanager.Adapter.CustomAdapter4;
import com.izoneapps.carmanager.Adapter.CustomAdapter5;
import com.izoneapps.carmanager.DialogFragments;
import com.izoneapps.carmanager.R;
import com.izoneapps.carmanager.database.fuel_records;
import com.izoneapps.carmanager.database.odometer_records;
import com.izoneapps.carmanager.database.service_records;
import com.izoneapps.carmanager.database.trip_records;

import java.nio.charset.StandardCharsets;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class FirstFragment extends Fragment implements AdapterView.OnItemSelectedListener{
    FloatingActionButton mAddReminderButton;
    RecyclerView recyclerView1, recyclerView2, recyclerView3, recyclerView4;
    ArrayList<String> activity_fid, activity_fdate, activity_fodo, activity_fqua, activity_fprice, activity_ftot, activity_fsta, activity_fnotes, activity_fveh,
            activity_sid, activity_sdate, activity_sodo, activity_ss, activity_stot, activity_sta, activity_snot, activity_sveh,
            activity_tid, activity_ddate, activity_dodo, activity_dloc, activity_adate, activity_aodo, activity_aloc, activity_tdis, activity_tt, activity_tavg, activity_tpark, activity_tnot, activity_tveh,
            activity_oid, activity_odate, activity_oo, activity_onotes, activity_oveh, activity_fuid, activity_suid, activity_tuid, activity_ouid;
    ArrayList<byte[]> activity_frep, activity_srep;
    fuel_records myDB1;
    trip_records myDB3;
    service_records myDB2;
    odometer_records myDB4;
    CustomAdapter2 customAdapter1;
    CustomAdapter3 customAdapter2;
    CustomAdapter4 customAdapter3;
    CustomAdapter5 customAdapter4;
    TextView fillups, expenses, odometer, trips;
    Spinner vehicle;
    String vandi = "";
    public static HashMap<String, String> vehicles = new HashMap<>();
    ArrayList<String> motor = new ArrayList<>();
    Gson gson = new Gson();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        recyclerView1 = view.findViewById(R.id.recyclerView1);
        recyclerView2 = view.findViewById(R.id.recyclerView2);
        recyclerView3 = view.findViewById(R.id.recyclerView3);
        recyclerView4 = view.findViewById(R.id.recyclerView4);
        fillups = view.findViewById(R.id.fillup);
        expenses = view.findViewById(R.id.expenses);
        odometer = view.findViewById(R.id.odometer);
        trips = view.findViewById(R.id.trip);
        vehicle = view.findViewById(R.id.vehicles);
        mAddReminderButton = view.findViewById(R.id.add);
        mAddReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!vandi.equals("")){
                    Bundle bundle = new Bundle();
                    bundle.putString("vehicle", vandi);
                    DialogFragment dialog = new DialogFragments();
                    dialog.setArguments(bundle);
                    dialog.show(getActivity().getSupportFragmentManager(), "reminder_dialog"); // Fixed tag
                }else{
                    Toast.makeText(getActivity(), "Please add vehicles first", Toast.LENGTH_SHORT).show();
                }
            }
        });
        myDB1 = new fuel_records(getActivity());
        myDB3 = new trip_records(getActivity());
        myDB2 = new service_records(getActivity());
        myDB4 = new odometer_records(getActivity());

        try {
            FileInputStream fis = requireContext().openFileInput("myHashMap.json");

            byte[] data = new byte[fis.available()];
            fis.read(data);
            fis.close();

            String json = new String(data, StandardCharsets.UTF_8); // Make sure you specify encoding if needed

            vehicles = gson.fromJson(json, new TypeToken<HashMap<String, String>>() {}.getType());

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
                activity_fid = new ArrayList<>();
                activity_fuid = new ArrayList<>();
                activity_fdate = new ArrayList<>();
                activity_fodo = new ArrayList<>();
                activity_fqua = new ArrayList<>();
                activity_fprice = new ArrayList<>();
                activity_ftot = new ArrayList<>();
                activity_fsta = new ArrayList<>();
                activity_fnotes = new ArrayList<>();
                activity_fveh = new ArrayList<>();
                activity_frep = new ArrayList<>();

                activity_sid = new ArrayList<>();
                activity_suid = new ArrayList<>();
                activity_sdate = new ArrayList<>();
                activity_sodo = new ArrayList<>();
                activity_ss = new ArrayList<>();
                activity_stot = new ArrayList<>();
                activity_sta = new ArrayList<>();
                activity_snot = new ArrayList<>();
                activity_sveh = new ArrayList<>();
                activity_srep = new ArrayList<>();

                activity_tid = new ArrayList<>();
                activity_tuid = new ArrayList<>();
                activity_ddate = new ArrayList<>();
                activity_dodo = new ArrayList<>();
                activity_dloc = new ArrayList<>();
                activity_adate = new ArrayList<>();
                activity_aodo = new ArrayList<>();
                activity_aloc = new ArrayList<>();
                activity_tdis = new ArrayList<>();
                activity_tt = new ArrayList<>();
                activity_tavg = new ArrayList<>();
                activity_tpark = new ArrayList<>();
                activity_tnot = new ArrayList<>();
                activity_tveh = new ArrayList<>();

                activity_oid = new ArrayList<>();
                activity_ouid = new ArrayList<>();
                activity_odate = new ArrayList<>();
                activity_oo = new ArrayList<>();
                activity_onotes = new ArrayList<>();
                activity_oveh = new ArrayList<>();
                storeDataInArrays(vandi);
                storeDataInArrays1(vandi);
                storeDataInArrays2(vandi);
                storeDataInArrays3(vandi);

                if(activity_fid.size()!=0){
                    fillups.setVisibility(View.VISIBLE);
                    recyclerView1.setVisibility(View.VISIBLE);
                    customAdapter1 = new CustomAdapter2(getActivity(),getContext(), activity_fid, activity_fuid, activity_fdate, activity_fodo, activity_fqua, activity_fprice, activity_ftot, activity_fsta, activity_fnotes, activity_fveh, activity_frep);
                    recyclerView1.setAdapter(customAdapter1);
                    recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity()));
                }else{
                    fillups.setVisibility(View.GONE);
                    recyclerView1.setVisibility(View.GONE);
                    customAdapter1 = new CustomAdapter2(getActivity(),getContext(), activity_fid, activity_fuid, activity_fdate, activity_fodo, activity_fqua, activity_fprice, activity_ftot, activity_fsta, activity_fnotes, activity_fveh, activity_frep);
                    recyclerView1.setAdapter(customAdapter1);
                    recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity()));
                }

                if (activity_sid.size()!=0) {
                    expenses.setVisibility(View.VISIBLE);
                    recyclerView2.setVisibility(View.VISIBLE);
                    customAdapter2 = new CustomAdapter3(getActivity(),getContext(), activity_sid, activity_suid, activity_sdate, activity_sodo, activity_ss, activity_stot, activity_sta, activity_snot, activity_sveh, activity_srep);
                    recyclerView2.setAdapter(customAdapter2);
                    recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
                }else{
                    expenses.setVisibility(View.GONE);
                    recyclerView2.setVisibility(View.GONE);
                    customAdapter2 = new CustomAdapter3(getActivity(),getContext(), activity_sid, activity_suid, activity_sdate, activity_sodo, activity_ss, activity_stot, activity_sta, activity_snot, activity_sveh, activity_srep);
                    recyclerView2.setAdapter(customAdapter2);
                    recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
                }

                if (activity_tid.size()!=0) {
                    trips.setVisibility(View.VISIBLE);
                    recyclerView3.setVisibility(View.VISIBLE);
                    customAdapter3 = new CustomAdapter4(getActivity(),getContext(), activity_tid, activity_tuid, activity_ddate, activity_dodo, activity_dloc, activity_adate, activity_aodo, activity_aloc, activity_tdis ,activity_tt, activity_tavg, activity_tpark, activity_tnot, activity_tveh);
                    recyclerView3.setAdapter(customAdapter3);
                    recyclerView3.setLayoutManager(new LinearLayoutManager(getActivity()));
                }else{
                    trips.setVisibility(View.GONE);
                    recyclerView3.setVisibility(View.GONE);
                    customAdapter3 = new CustomAdapter4(getActivity(),getContext(), activity_tid, activity_tuid, activity_ddate, activity_dodo, activity_dloc, activity_adate, activity_aodo, activity_aloc, activity_tdis ,activity_tt, activity_tavg, activity_tpark, activity_tnot, activity_tveh);
                    recyclerView3.setAdapter(customAdapter3);
                    recyclerView3.setLayoutManager(new LinearLayoutManager(getActivity()));
                }

                if (activity_oid.size()!=0) {
                    odometer.setVisibility(View.VISIBLE);
                    recyclerView4.setVisibility(View.VISIBLE);
                    customAdapter4 = new CustomAdapter5(getActivity(),getContext(), activity_oid, activity_ouid, activity_odate, activity_oo, activity_onotes, activity_oveh);
                    recyclerView4.setAdapter(customAdapter4);
                    recyclerView4.setLayoutManager(new LinearLayoutManager(getActivity()));
                }else{
                    odometer.setVisibility(View.GONE);
                    recyclerView4.setVisibility(View.GONE);
                    customAdapter4 = new CustomAdapter5(getActivity(),getContext(), activity_oid, activity_ouid, activity_odate, activity_oo, activity_onotes, activity_oveh);
                    recyclerView4.setAdapter(customAdapter4);
                    recyclerView4.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;

    }

    void storeDataInArrays(String vandi){
        Cursor cursor = myDB1.readAllData1(vandi);
        if(cursor.getCount() != 0){
            while (cursor.moveToNext()){
                try {
                    activity_fid.add(cursor.getString(0));
                    activity_fuid.add(cursor.getString(1));
                    activity_fdate.add(cursor.getString(2));
                    activity_fodo.add(cursor.getString(3));
                    activity_fqua.add(cursor.getString(4));
                    activity_fprice.add(cursor.getString(5));
                    activity_ftot.add(cursor.getString(6));
                    activity_fsta.add(cursor.getString(7));
                    activity_fnotes.add(cursor.getString(8));
                    activity_fveh.add(cursor.getString(9));
                    activity_frep.add(cursor.getBlob(10));
                }catch(Exception e){
                    System.out.println(e);
                }
            }
        }else{
            //Toast.makeText(getContext(), "No Fuel Data", Toast.LENGTH_SHORT).show();
        }
    }

    void storeDataInArrays1(String vandi){
        Cursor cursor = myDB2.readAllData0(vandi);
        if(cursor.getCount() != 0){
            while (cursor.moveToNext()){
                try {
                    activity_sid.add(cursor.getString(0));
                    activity_suid.add(cursor.getString(1));
                    activity_sdate.add(cursor.getString(2));
                    activity_sodo.add(cursor.getString(3));
                    activity_ss.add(cursor.getString(4));
                    activity_stot.add(cursor.getString(5));
                    activity_sta.add(cursor.getString(6));
                    activity_snot.add(cursor.getString(7));
                    activity_sveh.add(cursor.getString(8));
                    activity_srep.add(cursor.getBlob(9));
                }catch(Exception e){
                    System.out.println(e);
                }
            }
        }else{
            //Toast.makeText(getContext(), "No Service Data", Toast.LENGTH_SHORT).show();
        }
    }

    void storeDataInArrays2(String vandi){
        Cursor cursor = myDB3.readAllData1(vandi);
        if(cursor.getCount() != 0){
            while (cursor.moveToNext()){
                try {
                    activity_tid.add(cursor.getString(0));
                    activity_tuid.add(cursor.getString(1));
                    activity_ddate.add(cursor.getString(2));
                    activity_dodo.add(cursor.getString(3));
                    activity_dloc.add(cursor.getString(4));
                    activity_adate.add(cursor.getString(5));
                    activity_aodo.add(cursor.getString(6));
                    activity_aloc.add(cursor.getString(7));
                    activity_tdis.add(cursor.getString(8));
                    activity_tt.add(cursor.getString(9));
                    activity_tavg.add(cursor.getString(10));
                    activity_tpark.add(cursor.getString(11));
                    activity_tnot.add(cursor.getString(12));
                    activity_tveh.add(cursor.getString(13));
                }catch(Exception e){
                    System.out.println(e);
                }
            }
        }else{
            //Toast.makeText(getContext(), "No Trip Data", Toast.LENGTH_SHORT).show();
        }
    }

    void storeDataInArrays3(String vandi){
        Cursor cursor = myDB4.readAllData1(vandi);
        if(cursor.getCount() != 0){
            while (cursor.moveToNext()){
                try {
                    activity_oid.add(cursor.getString(0));
                    activity_ouid.add(cursor.getString(1));
                    activity_odate.add(cursor.getString(2));
                    activity_oo.add(cursor.getString(3));
                    activity_onotes.add(cursor.getString(4));
                    activity_oveh.add(cursor.getString(5));
                }catch(Exception e){
                    System.out.println(e);
                }
            }
        }else{
            //Toast.makeText(getContext(), "No Odo Data", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
