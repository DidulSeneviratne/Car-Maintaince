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
import com.universl.didul.car_maintenance.edit_service_reminder;

import java.util.ArrayList;

public class CustomAdapter6 extends RecyclerView.Adapter<CustomAdapter6.MyViewHolder> {
    private Context context;
    private Activity activity;
    private ArrayList activity_id, activity_name, activity_km, activity_days, activity_enable, activity_lkm, activity_ldate, activity_veh;
    public CustomAdapter6(Activity activity, Context context, ArrayList id, ArrayList name, ArrayList km, ArrayList days, ArrayList enable, ArrayList lkm, ArrayList ldate, ArrayList veh){
        this.activity = activity;
        this.context = context;
        this.activity_id = id;
        this.activity_name = name;
        this.activity_km = km;
        this.activity_days = days;
        this.activity_enable = enable;
        this.activity_lkm = lkm;
        this.activity_ldate = ldate;
        this.activity_veh = veh;
    }

    @NonNull
    @Override
    public CustomAdapter6.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row1, parent, false);
        return new CustomAdapter6.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomAdapter6.MyViewHolder holder, int position) {
        // Do not treat position as fixed; only use immediately and call holder.getAdapterPosition() to look it up later
        int adapterPosition = holder.getAdapterPosition();
        holder.make.setText(String.valueOf(activity_name.get(adapterPosition)));
        holder.model.setText(String.valueOf(activity_enable.get(adapterPosition)));
        holder.year.setText(String.valueOf(activity_days.get(adapterPosition)));
        holder.fuel.setText(String.valueOf(activity_km.get(adapterPosition)));

        //Recyclerview onClickListener
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, edit_service_reminder.class);
                intent.putExtra("id", String.valueOf(activity_id.get(adapterPosition)));
                intent.putExtra("name", String.valueOf(activity_name.get(adapterPosition)));
                intent.putExtra("km", String.valueOf(activity_km.get(adapterPosition)));
                intent.putExtra("days", String.valueOf(activity_days.get(adapterPosition)));
                intent.putExtra("enable", String.valueOf(activity_enable.get(adapterPosition)));
                intent.putExtra("lkm", String.valueOf(activity_lkm.get(adapterPosition)));
                intent.putExtra("ldate", String.valueOf(activity_ldate.get(adapterPosition)));
                intent.putExtra("vehicle", String.valueOf(activity_veh.get(adapterPosition)));
                activity.startActivityForResult(intent, 1);
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return activity_id.size();
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
