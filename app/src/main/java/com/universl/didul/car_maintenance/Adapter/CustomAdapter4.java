package com.universl.didul.car_maintenance.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.universl.didul.car_maintenance.Edit_vehicle;
import com.universl.didul.car_maintenance.R;
import com.universl.didul.car_maintenance.edit_service;
import com.universl.didul.car_maintenance.edit_trip;

import java.util.ArrayList;
public class CustomAdapter4 extends RecyclerView.Adapter<CustomAdapter4.MyViewHolder> {
    private Context context;
    private Activity activity;
    private ArrayList activity_tid, activity_ddate, activity_dodo, activity_dloc, activity_adate, activity_aodo, activity_aloc, activity_tdis, activity_tt, activity_tavg, activity_tpark, activity_tnot, activity_tveh;
    public CustomAdapter4(Activity activity, Context context, ArrayList tid, ArrayList ddate, ArrayList dodo, ArrayList dloc, ArrayList adate, ArrayList aodo, ArrayList aloc, ArrayList tdis, ArrayList tt, ArrayList tavg, ArrayList tpark, ArrayList tnot, ArrayList tveh){
        this.activity = activity;
        this.context = context;
        this.activity_tid = tid;
        this.activity_ddate = ddate;
        this.activity_dodo = dodo;
        this.activity_dloc = dloc;
        this.activity_adate = adate;
        this.activity_aodo = aodo;
        this.activity_aloc = aloc;
        this.activity_tdis = tdis;
        this.activity_tt = tt;
        this.activity_tavg = tavg;
        this.activity_tpark = tpark;
        this.activity_tnot = tnot;
        this.activity_tveh = tveh;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row1, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        // Do not treat position as fixed; only use immediately and call holder.getAdapterPosition() to look it up later
        int adapterPosition = holder.getAdapterPosition();
        //holder.make.setText(String.valueOf(activity_make.get(adapterPosition)));
        holder.model.setText(String.valueOf(activity_aloc.get(adapterPosition)));
        holder.year.setText(String.valueOf(activity_tdis.get(adapterPosition)));
        holder.fuel.setText(String.valueOf(activity_tt.get(adapterPosition)));

        //Recyclerview onClickListener
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, edit_trip.class);
                intent.putExtra("id", String.valueOf(activity_tid.get(adapterPosition)));
                intent.putExtra("d_date", String.valueOf(activity_ddate.get(adapterPosition)));
                intent.putExtra("d_odometer", String.valueOf(activity_dodo.get(adapterPosition)));
                intent.putExtra("d_location", String.valueOf(activity_dloc.get(adapterPosition)));
                intent.putExtra("a_date", String.valueOf(activity_adate.get(adapterPosition)));
                intent.putExtra("a_odometer", String.valueOf(activity_aodo.get(adapterPosition)));
                intent.putExtra("a_location", String.valueOf(activity_aloc.get(adapterPosition)));
                intent.putExtra("distance", String.valueOf(activity_tdis.get(adapterPosition)));
                intent.putExtra("time", String.valueOf(activity_tt.get(adapterPosition)));
                intent.putExtra("avg_speed", String.valueOf(activity_tavg.get(adapterPosition)));
                intent.putExtra("parking", String.valueOf(activity_tpark.get(adapterPosition)));
                intent.putExtra("notes", String.valueOf(activity_tnot.get(adapterPosition)));
                intent.putExtra("vehicle", String.valueOf(activity_tveh.get(adapterPosition)));
                activity.startActivityForResult(intent, 1);
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return activity_tid.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView make, model, year, fuel;
        LinearLayout mainLayout;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            make = itemView.findViewById(R.id.make);
            model = itemView.findViewById(R.id.model);
            year = itemView.findViewById(R.id.year);
            fuel = itemView.findViewById(R.id.fuel);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            //Animate Recyclerview
            Animation translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            mainLayout.setAnimation(translate_anim);
        }

    }

}

