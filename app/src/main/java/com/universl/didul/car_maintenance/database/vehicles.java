package com.universl.didul.car_maintenance.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class vehicles extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "vehicles.db";
    private static final int DATABASE_VERSION = 3;
    private static final String TABLE_NAME = "vehicles";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_IMAGE = "image";
    private static final String COLUMN_MAKE = "make";
    private static final String COLUMN_MODEL = "model";
    private static final String COLUMN_YEAR = "year";
    private static final String COLUMN_LIC = "licence";
    private static final String COLUMN_CHASSIS = "chassis";
    private static final String COLUMN_INSURANCE = "insurance";
    private static final String COLUMN_FUEL = "fuel";
    private static final String COLUMN_IMAGE1 = "image1";
    ByteArrayOutputStream byteArrayOutputStream;
    byte[] imageInByte;

    public vehicles(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_IMAGE + " BLOB, " +
                COLUMN_MAKE + " TEXT, " +
                COLUMN_MODEL + " TEXT, " +
                COLUMN_YEAR + " TEXT, " +
                COLUMN_LIC + " TEXT, " +
                COLUMN_CHASSIS + " TEXT, " +
                COLUMN_INSURANCE + " TEXT, " +
                COLUMN_FUEL + " TEXT" +
                COLUMN_IMAGE1 + "BLOB);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insertData(Bitmap img, String make, String model, String year, String licence, String chassis, String insurance, String fuel){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        byteArrayOutputStream = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        imageInByte = byteArrayOutputStream.toByteArray();
        cv.put(COLUMN_IMAGE,imageInByte);
        cv.put(COLUMN_MAKE,make);
        cv.put(COLUMN_MODEL,model);
        cv.put(COLUMN_YEAR,year);
        cv.put(COLUMN_LIC,licence);
        cv.put(COLUMN_CHASSIS,chassis);
        cv.put(COLUMN_INSURANCE,insurance);
        cv.put(COLUMN_FUEL,fuel);
        long result = MyDB.insert(TABLE_NAME, null, cv);
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
            return -1;
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

    public long updateData(String row_id, Bitmap img, String make, String model, String year, String licence, String chassis, String insurance, String fuel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        byteArrayOutputStream = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        imageInByte = byteArrayOutputStream.toByteArray();
        cv.put(COLUMN_IMAGE,imageInByte);
        cv.put(COLUMN_MAKE,make);
        cv.put(COLUMN_MODEL,model);
        cv.put(COLUMN_YEAR,year);
        cv.put(COLUMN_LIC,licence);
        cv.put(COLUMN_CHASSIS,chassis);
        cv.put(COLUMN_INSURANCE,insurance);
        cv.put(COLUMN_FUEL,fuel);
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
