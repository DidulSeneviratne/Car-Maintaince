package com.universl.didul.car_maintenance.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.universl.didul.car_maintenance.edit_fill_up;
import com.universl.didul.car_maintenance.edit_service;

import java.util.ArrayList;
public class CustomAdapter3 extends RecyclerView.Adapter<CustomAdapter3.MyViewHolder> {
    private Context context;
    private Activity activity;
    private ArrayList activity_sid, activity_sdate, activity_sodo, activity_ss, activity_stot, activity_sta, activity_snot, activity_sveh;
    private ArrayList<byte[]> activity_srep;
    public CustomAdapter3(Activity activity, Context context, ArrayList sid, ArrayList sdate, ArrayList sodo, ArrayList ss, ArrayList stot, ArrayList sta, ArrayList snot, ArrayList sveh, ArrayList srep){
        this.activity = activity;
        this.context = context;
        this.activity_sid = sid;
        this.activity_sdate = sdate;
        this.activity_sodo = sodo;
        this.activity_ss = ss;
        this.activity_stot = stot;
        this.activity_sta = sta;
        this.activity_snot = snot;
        this.activity_sveh = sveh;
        this.activity_srep = srep;
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
        try {
            int adapterPosition = holder.getAdapterPosition();
            holder.make.setText(String.valueOf(activity_sdate.get(adapterPosition)));
            holder.model.setText(String.valueOf(activity_ss.get(adapterPosition)));
            holder.year.setText(String.valueOf(activity_stot.get(adapterPosition)));
            holder.fuel.setText(String.valueOf(activity_sta.get(adapterPosition)));

            //Recyclerview onClickListener
            holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, edit_service.class);
                    intent.putExtra("id", String.valueOf(activity_sid.get(adapterPosition)));
                    intent.putExtra("date", String.valueOf(activity_sdate.get(adapterPosition)));
                    intent.putExtra("odometer", String.valueOf(activity_sodo.get(adapterPosition)));
                    intent.putExtra("service", String.valueOf(activity_ss.get(adapterPosition)));
                    intent.putExtra("total", String.valueOf(activity_stot.get(adapterPosition)));
                    intent.putExtra("station", String.valueOf(activity_sta.get(adapterPosition)));
                    intent.putExtra("notes", String.valueOf(activity_snot.get(adapterPosition)));
                    intent.putExtra("vehicle", String.valueOf(activity_sveh.get(adapterPosition)));
                    intent.putExtra("receipt", activity_srep.get(adapterPosition));
                    activity.startActivityForResult(intent, 1);
                    activity.finish();
                }
            });
        }catch(Exception e){
            Log.d("msg", String.valueOf(e));
        }
    }

    @Override
    public int getItemCount() {
        return activity_sid.size();
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
