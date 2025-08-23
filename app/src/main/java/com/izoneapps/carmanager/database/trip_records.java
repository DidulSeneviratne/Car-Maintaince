package com.izoneapps.carmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class trip_records extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "records.db";
    private static final int DATABASE_VERSION = 4;
    private static final String TABLE_NAME = "trip_records";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_SID = "uuid";
    private static final String COLUMN_D_DATE = "d_date";
    private static final String COLUMN_D_ODOMETER = "d_odometer";
    private static final String COLUMN_D_LOCATION = "d_location";
    private static final String COLUMN_A_DATE = "a_date";
    private static final String COLUMN_A_ODOMETER = "a_odometer";
    private static final String COLUMN_A_LOCATION = "a_location";
    private static final String COLUMN_DISTANCE = "distance";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_AVG_SPEED = "avg_speed";
    private static final String COLUMN_PARKING = "parking";
    private static final String COLUMN_NOTES = "notes";
    private static final String COLUMN_VEHICLES = "vehicles";

    public trip_records(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SID + " TEXT, " +
                COLUMN_D_DATE + " TEXT, " +
                COLUMN_D_ODOMETER + " TEXT, " +
                COLUMN_D_LOCATION + " TEXT, " +
                COLUMN_A_DATE + " TEXT, " +
                COLUMN_A_ODOMETER + " TEXT, " +
                COLUMN_A_LOCATION + " TEXT, " +
                COLUMN_DISTANCE + " TEXT, " +
                COLUMN_TIME + " TEXT, " +
                COLUMN_AVG_SPEED + " TEXT, " +
                COLUMN_PARKING + " TEXT, " +
                COLUMN_NOTES + " TEXT, " +
                COLUMN_VEHICLES + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 4) {
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_SID + " TEXT;");
        }
    }

    public void insertData(String uuid, String d_date, String d_odometer, String d_location, String a_date, String a_odometer, String a_location, String distance, String time, String avg_speed, String parking, String notes, String vehicle){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_SID,uuid);
        cv.put(COLUMN_D_DATE,d_date);
        cv.put(COLUMN_D_ODOMETER,d_odometer);
        cv.put(COLUMN_D_LOCATION,d_location);
        cv.put(COLUMN_A_DATE,a_date);
        cv.put(COLUMN_A_ODOMETER,a_odometer);
        cv.put(COLUMN_A_LOCATION,a_location);
        cv.put(COLUMN_DISTANCE,distance);
        cv.put(COLUMN_TIME,time);
        cv.put(COLUMN_AVG_SPEED,avg_speed);
        cv.put(COLUMN_PARKING,parking);
        cv.put(COLUMN_NOTES,notes);
        cv.put(COLUMN_VEHICLES,vehicle);
        long result = MyDB.insert(TABLE_NAME, null, cv);
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }

    public Cursor readAllData1(String vehicle){
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_VEHICLES + " = " + "'" + vehicle + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }

    public long updateData(String uuid, String row_id, String d_date, String d_odometer, String d_location, String a_date, String a_odometer, String a_location, String distance, String time, String avg_speed, String parking, String notes, String vehicle){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_SID,uuid);
        cv.put(COLUMN_D_DATE,d_date);
        cv.put(COLUMN_D_ODOMETER,d_odometer);
        cv.put(COLUMN_D_LOCATION,d_location);
        cv.put(COLUMN_A_DATE,a_date);
        cv.put(COLUMN_A_ODOMETER,a_odometer);
        cv.put(COLUMN_A_LOCATION,a_location);
        cv.put(COLUMN_DISTANCE,distance);
        cv.put(COLUMN_TIME,time);
        cv.put(COLUMN_AVG_SPEED,avg_speed);
        cv.put(COLUMN_PARKING,parking);
        cv.put(COLUMN_NOTES,notes);
        cv.put(COLUMN_VEHICLES,vehicle);
        long result = db.update(TABLE_NAME, cv, "id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
            return result;
        }
        return -1;
    }

    public void deleteOneRow(String row_id){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + "'" + row_id + "'");
        }catch(Exception e){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }

    }

}
