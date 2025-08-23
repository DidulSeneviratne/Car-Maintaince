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

import com.izoneapps.carmanager.R;
import com.izoneapps.carmanager.edit_odometer;

import java.util.ArrayList;
public class CustomAdapter5 extends RecyclerView.Adapter<CustomAdapter5.MyViewHolder> {
    private Context context;
    private Activity activity;
    private ArrayList activity_oid, activity_ouid, activity_odate, activity_oo, activity_onotes, activity_oveh;
    public CustomAdapter5(Activity activity, Context context, ArrayList oid, ArrayList ouid, ArrayList odate, ArrayList oo, ArrayList onotes, ArrayList oveh){
        this.activity = activity;
        this.context = context;
        this.activity_oid = oid;
        this.activity_ouid = ouid;
        this.activity_odate = odate;
        this.activity_oo = oo;
        this.activity_onotes = onotes;
        this.activity_oveh = oveh;
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
        holder.make.setText(String.valueOf(activity_odate.get(adapterPosition)));
        holder.model.setText(String.valueOf(activity_oo.get(adapterPosition)) + " km");

        //Recyclerview onClickListener
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, edit_odometer.class);
                intent.putExtra("id", String.valueOf(activity_oid.get(adapterPosition)));
                intent.putExtra("uuid", String.valueOf(activity_ouid.get(adapterPosition)));
                intent.putExtra("date", String.valueOf(activity_odate.get(adapterPosition)));
                intent.putExtra("odometer", String.valueOf(activity_oo.get(adapterPosition)));
                intent.putExtra("notes", String.valueOf(activity_onotes.get(adapterPosition)));
                intent.putExtra("vehicle", String.valueOf(activity_oveh.get(adapterPosition)));
                activity.startActivityForResult(intent, 1);
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return activity_oid.size();
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

