package com.universl.didul.car_maintenance.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

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
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.universl.didul.car_maintenance.Adapter.CustomAdapter7;
import com.universl.didul.car_maintenance.MainActivity;
import com.universl.didul.car_maintenance.R;
import com.universl.didul.car_maintenance.add_expense_reminder;
import com.universl.didul.car_maintenance.database.expenses_reminders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class ForthFragment extends Fragment {
    FloatingActionButton mAddReminderButton;
    RecyclerView recyclerView;
    ArrayList<String> activity_id, activity_name, activity_km, activity_days, activity_enable, activity_veh;
    expenses_reminders myDB1;
    CustomAdapter7 customAdapter7;
    Spinner vehicle;
    String vandi = "";
    public static HashMap<String, String> vehicles = MainActivity.vehicles;
    ArrayList<String> motor = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_third, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        vehicle = view.findViewById(R.id.vehicles);
        mAddReminderButton = view.findViewById(R.id.add);
        mAddReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!vandi.equals("")){
                    Intent intent = new Intent(getActivity(), add_expense_reminder.class);
                    intent.putExtra("vehicle",vandi);
                    startActivity(intent);
                    getActivity().finish(); // Fixed tag
                }else{
                    Toast.makeText(getActivity(), "Please add vehicles first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        myDB1 = new expenses_reminders(getActivity());

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
                activity_id = new ArrayList<>();
                activity_name = new ArrayList<>();
                activity_km = new ArrayList<>();
                activity_days = new ArrayList<>();
                activity_veh = new ArrayList<>();
                activity_enable = new ArrayList<>();
                storeDataInArrays(vandi);

                customAdapter7 = new CustomAdapter7(getActivity(),getContext(), activity_id, activity_name, activity_km, activity_days, activity_veh, activity_enable);
                recyclerView.setAdapter(customAdapter7);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
                    activity_id.add(cursor.getString(0));
                    activity_name.add(cursor.getString(1));
                    activity_km.add(cursor.getString(2));
                    activity_days.add(cursor.getString(3));
                    activity_veh.add(cursor.getString(4));
                    activity_enable.add(cursor.getString(5));
                }catch(Exception e){
                    System.out.println(e);
                }
            }
        }
    }
}
