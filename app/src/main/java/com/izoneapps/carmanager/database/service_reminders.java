package com.izoneapps.carmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class service_reminders extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "services.db";
    private static final int DATABASE_VERSION = 4;
    private static final String TABLE_NAME = "reminder_services";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_SID = "uuid";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_KM = "km";
    private static final String COLUMN_DAYS = "days";
    private static final String COLUMN_ENABLE = "enable";
    private static final String COLUMN_LAST_ODOMETER = "l_odometer";
    private static final String COLUMN_LAST_DATE = "l_date";
    private static final String COLUMN_VEHICLES = "vehicles";

    public service_reminders(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SID + " TEXT, " +
                COLUMN_TYPE + " TEXT, " +
                COLUMN_KM + " TEXT, " +
                COLUMN_DAYS + " TEXT, " +
                COLUMN_ENABLE + " TEXT, " +
                COLUMN_LAST_ODOMETER + " TEXT, " +
                COLUMN_LAST_DATE + " TEXT, " +
                COLUMN_VEHICLES + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 4) {
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_SID + " TEXT;");
        }
    }

    public long insertData(String uuid, String type, String km, String days, String enable, String l_odometer, String l_date, String vehicle){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_SID,uuid);
        cv.put(COLUMN_TYPE,type);
        cv.put(COLUMN_KM,km);
        cv.put(COLUMN_DAYS,days);
        cv.put(COLUMN_ENABLE,enable);
        cv.put(COLUMN_LAST_ODOMETER,l_odometer);
        cv.put(COLUMN_LAST_DATE,l_date);
        cv.put(COLUMN_VEHICLES,vehicle);
        long result = MyDB.insert(TABLE_NAME, null, cv);
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else{
            //Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }
        return result;
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
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_VEHICLES + " LIKE " + "'" + vehicle + "%'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }

    public long updateData(String uuid, String row_id, String type, String km, String days, String enable, String l_odometer, String l_date, String vehicle){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_SID,uuid);
        cv.put(COLUMN_TYPE,type);
        cv.put(COLUMN_KM,km);
        cv.put(COLUMN_DAYS,days);
        cv.put(COLUMN_ENABLE,enable);
        cv.put(COLUMN_LAST_ODOMETER,l_odometer);
        cv.put(COLUMN_LAST_DATE,l_date);
        cv.put(COLUMN_VEHICLES,vehicle);
        long result = db.update(TABLE_NAME, cv, "id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
            return result;
        }else {
            //Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
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
