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
import com.izoneapps.carmanager.edit_fill_up;

import java.util.ArrayList;
public class CustomAdapter2 extends RecyclerView.Adapter<CustomAdapter2.MyViewHolder> {
    private Context context;
    private Activity activity;
    private ArrayList activity_fid, activity_fuid, activity_fdate, activity_fodo, activity_fqua, activity_fprice, activity_ftot, activity_fsta, activity_fnotes, activity_fveh;
    private ArrayList<byte[]> activity_frep;
    public CustomAdapter2(Activity activity, Context context, ArrayList fid, ArrayList fuid, ArrayList fdate, ArrayList fodo, ArrayList fqua, ArrayList fprice, ArrayList ftot, ArrayList fsta, ArrayList fnotes, ArrayList fveh, ArrayList frep){
        this.activity = activity;
        this.context = context;
        this.activity_fid = fid;
        this.activity_fuid = fuid;
        this.activity_fdate = fdate;
        this.activity_fodo = fodo;
        this.activity_fqua = fqua;
        this.activity_fprice = fprice;
        this.activity_ftot = ftot;
        this.activity_fsta = fsta;
        this.activity_fnotes = fnotes;
        this.activity_fveh = fveh;
        this.activity_frep = frep;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row2, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        // Do not treat position as fixed; only use immediately and call holder.getAdapterPosition() to look it up later
        int adapterPosition = holder.getAdapterPosition();
        holder.make.setText(String.valueOf(activity_fdate.get(adapterPosition)));
        holder.model.setText(String.valueOf(activity_fqua.get(adapterPosition)) + " l");
        holder.year.setText(String.valueOf(activity_ftot.get(adapterPosition)) + " LKR");
        holder.fuel.setText(String.valueOf(activity_fsta.get(adapterPosition)));

        //Recyclerview onClickListener
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, edit_fill_up.class);
                intent.putExtra("id", String.valueOf(activity_fid.get(adapterPosition)));
                intent.putExtra("uuid", String.valueOf(activity_fuid.get(adapterPosition)));
                intent.putExtra("date", String.valueOf(activity_fdate.get(adapterPosition)));
                intent.putExtra("odometer", String.valueOf(activity_fodo.get(adapterPosition)));
                intent.putExtra("quantity", String.valueOf(activity_fqua.get(adapterPosition)));
                intent.putExtra("price", String.valueOf(activity_fprice.get(adapterPosition)));
                intent.putExtra("total", String.valueOf(activity_ftot.get(adapterPosition)));
                intent.putExtra("station", String.valueOf(activity_fsta.get(adapterPosition)));
                intent.putExtra("notes", String.valueOf(activity_fnotes.get(adapterPosition)));
                intent.putExtra("vehicle", String.valueOf(activity_fveh.get(adapterPosition)));
                //intent.putExtra("receipt", activity_frep.get(adapterPosition));
                activity.startActivityForResult(intent, 1);
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return activity_fid.size();
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
