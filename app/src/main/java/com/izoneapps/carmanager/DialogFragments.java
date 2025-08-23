package com.izoneapps.carmanager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DialogFragments extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.pop_up, null);
        TextView ad_fuel = dialogView.findViewById(R.id.add_fuel);
        TextView ad_service = dialogView.findViewById(R.id.add_service);
        TextView ad_expense = dialogView.findViewById(R.id.add_expenses);
        TextView ad_trip = dialogView.findViewById(R.id.add_trip);
        TextView ad_odometer = dialogView.findViewById(R.id.add_odo);
        String message = getArguments().getString("vehicle");

        ad_fuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), add_fill_up.class);
                intent.putExtra("vehicle",message);
                startActivity(intent);
                getActivity().finish();
            }
        });

        ad_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), add_service.class);
                intent.putExtra("vehicle",message);
                startActivity(intent);
                getActivity().finish();
            }
        });

        ad_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), add_expense.class);
                intent.putExtra("vehicle",message);
                startActivity(intent);
                getActivity().finish();
            }
        });

        ad_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), add_trip.class);
                intent.putExtra("vehicle",message);
                startActivity(intent);
                getActivity().finish();
            }
        });

        ad_odometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), add_odometer.class);
                intent.putExtra("vehicle",message);
                startActivity(intent);
                getActivity().finish();
            }
        });

        // Find and configure UI elements in dialogView

        builder.setView(dialogView);
        // Set additional properties like title, positive/negative buttons

        return builder.create();

    }
}
