package com.universl.didul.car_maintenance.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class fuel_records extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "f_records.db";
    private static final int DATABASE_VERSION = 3;
    private static final String TABLE_NAME = "fuel_records";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_ODOMETER = "odometer";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_PRICE_PER_LITER = "price_per_liter";
    private static final String COLUMN_TOTAL = "total";
    private static final String COLUMN_STATION = "station";
    private static final String COLUMN_NOTES = "notes";
    private static final String COLUMN_RECIPT = "recipt";
    private static final String COLUMN_VEHICLES = "vehicles";
    ByteArrayOutputStream byteArrayOutputStream;
    byte[] imageInByte;

    public fuel_records(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_ODOMETER + " TEXT, " +
                COLUMN_QUANTITY + " TEXT, " +
                COLUMN_PRICE_PER_LITER + " TEXT, " +
                COLUMN_TOTAL + " TEXT, " +
                COLUMN_STATION + " TEXT, " +
                COLUMN_NOTES + " TEXT, " +
                COLUMN_VEHICLES + " TEXT, " +
                COLUMN_RECIPT + " BLOB);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertData(String date, String odometer, String quantity, String price_per_liter, String total, String station, String notes, String vehicle, Bitmap img){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        byteArrayOutputStream = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        imageInByte = byteArrayOutputStream.toByteArray();
        cv.put(COLUMN_DATE,date);
        cv.put(COLUMN_ODOMETER,odometer);
        cv.put(COLUMN_QUANTITY,quantity);
        cv.put(COLUMN_PRICE_PER_LITER,price_per_liter);
        cv.put(COLUMN_TOTAL,total);
        cv.put(COLUMN_STATION,station);
        cv.put(COLUMN_NOTES,notes);
        cv.put(COLUMN_VEHICLES,vehicle);
        cv.put(COLUMN_RECIPT,imageInByte);
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
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_VEHICLES + " LIKE " + "'" + vehicle + "%'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }

    public long updateData(String row_id, String date, String odometer, String quantity, String price_per_liter, String total, String station, String notes, String vehicle, Bitmap img){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        byteArrayOutputStream = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        imageInByte = byteArrayOutputStream.toByteArray();
        cv.put(COLUMN_DATE,date);
        cv.put(COLUMN_ODOMETER,odometer);
        cv.put(COLUMN_QUANTITY,quantity);
        cv.put(COLUMN_PRICE_PER_LITER,price_per_liter);
        cv.put(COLUMN_TOTAL,total);
        cv.put(COLUMN_STATION,station);
        cv.put(COLUMN_NOTES,notes);
        cv.put(COLUMN_VEHICLES,vehicle);
        cv.put(COLUMN_RECIPT,imageInByte);
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
