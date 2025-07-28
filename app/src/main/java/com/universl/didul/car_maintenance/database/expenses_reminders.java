package com.universl.didul.car_maintenance.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class expenses_reminders extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "expenses.db";
    private static final int DATABASE_VERSION = 4;
    private static final String TABLE_NAME = "reminder_expenses";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_KM = "km";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_ENABLE = "enable";
    private static final String COLUMN_VEHICLES = "vehicles";

    public expenses_reminders(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TYPE + " TEXT, " +
                COLUMN_KM + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_VEHICLES + " TEXT, " +
                COLUMN_ENABLE + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 4) {
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_VEHICLES + " TEXT;");
        }
    }

    public long insertData(String type, String km, String date, String vehicle, String enable){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TYPE,type);
        cv.put(COLUMN_KM,km);
        cv.put(COLUMN_DATE,date);
        cv.put(COLUMN_VEHICLES,vehicle);
        cv.put(COLUMN_ENABLE,enable);
        long result = MyDB.insert(TABLE_NAME, null, cv);
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
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

    public long updateData(String row_id, String type, String km, String date, String vehicle, String enable){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TYPE,type);
        cv.put(COLUMN_KM,km);
        cv.put(COLUMN_DATE,date);
        cv.put(COLUMN_VEHICLES,vehicle);
        cv.put(COLUMN_ENABLE,enable);
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
