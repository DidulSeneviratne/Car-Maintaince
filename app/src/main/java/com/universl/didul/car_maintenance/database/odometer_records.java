package com.universl.didul.car_maintenance.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class odometer_records extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "o_records.db";
    private static final int DATABASE_VERSION = 3;
    private static final String TABLE_NAME = "odometer_records";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_ODOMETER = "odometer";
    private static final String COLUMN_NOTES = "notes";
    private static final String COLUMN_VEHICLES = "vehicles";

    public odometer_records(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_ODOMETER + " TEXT, " +
                COLUMN_NOTES + " TEXT, " +
                COLUMN_VEHICLES + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertData(String date, String odometer, String notes, String vehicle){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_DATE,date);
        cv.put(COLUMN_ODOMETER,odometer);
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

    public long updateData(String row_id, String date, String odometer, String notes, String vehicle){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_DATE,date);
        cv.put(COLUMN_ODOMETER,odometer);
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
