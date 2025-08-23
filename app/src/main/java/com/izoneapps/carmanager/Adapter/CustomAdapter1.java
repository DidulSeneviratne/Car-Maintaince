package com.izoneapps.carmanager.Adapter;

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

import com.izoneapps.carmanager.Edit_vehicle;
import com.izoneapps.carmanager.R;

import java.util.ArrayList;
public class CustomAdapter1 extends RecyclerView.Adapter<CustomAdapter1.MyViewHolder> {
    private Context context;
    private Activity activity;
    private ArrayList activity_id, activity_make, activity_model, activity_year, activity_lic, activity_chassi, activity_insurance, activity_fuel;
    private ArrayList<byte[]> activity_img;
    public CustomAdapter1(Activity activity, Context context, ArrayList id, ArrayList img, ArrayList make, ArrayList model, ArrayList year, ArrayList lic, ArrayList chassi, ArrayList insurance, ArrayList fuel){
        this.activity = activity;
        this.context = context;
        this.activity_id = id;
        this.activity_img = img;
        this.activity_make = make;
        this.activity_model = model;
        this.activity_year = year;
        this.activity_lic = lic;
        this.activity_chassi = chassi;
        this.activity_insurance = insurance;
        this.activity_fuel = fuel;
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
        holder.make.setText(String.valueOf(activity_make.get(adapterPosition)));
        holder.model.setText(String.valueOf(activity_model.get(adapterPosition)));
        holder.year.setText(String.valueOf(activity_year.get(adapterPosition)));
        holder.fuel.setText(String.valueOf(activity_fuel.get(adapterPosition)));

        //Recyclerview onClickListener
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Edit_vehicle.class);
                intent.putExtra("id", String.valueOf(activity_id.get(adapterPosition)));
                //intent.putExtra("img", activity_img.get(adapterPosition));
                intent.putExtra("make", String.valueOf(activity_make.get(adapterPosition)));
                intent.putExtra("model", String.valueOf(activity_model.get(adapterPosition)));
                intent.putExtra("year", String.valueOf(activity_year.get(adapterPosition)));
                intent.putExtra("lic", String.valueOf(activity_lic.get(adapterPosition)));
                intent.putExtra("chassi", String.valueOf(activity_chassi.get(adapterPosition)));
                intent.putExtra("insurance", String.valueOf(activity_insurance.get(adapterPosition)));
                intent.putExtra("fuel", String.valueOf(activity_fuel.get(adapterPosition)));
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
