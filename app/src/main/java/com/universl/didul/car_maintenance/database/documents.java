package com.universl.didul.car_maintenance.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class documents extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "pictures.db";
    private static final int DATABASE_VERSION = 3;
    private static final String TABLE_NAME = "pictures";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FID = "f_id";
    private static final String COLUMN_IMAGE = "image";
    ByteArrayOutputStream byteArrayOutputStream;
    byte[] imageInByte;

    public documents(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FID + " TEXT, " +
                COLUMN_IMAGE + " BLOB);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertData(String f_id, Bitmap img){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        byteArrayOutputStream = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        imageInByte = byteArrayOutputStream.toByteArray();
        cv.put(COLUMN_FID, f_id);
        cv.put(COLUMN_IMAGE, imageInByte);
        long result = MyDB.insert(TABLE_NAME, null, cv);
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else{
            //Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readAllData(String f_id){
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_FID + " = " + f_id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }

    public long updateData(String row_id, String f_id, Bitmap img){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        byteArrayOutputStream = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        imageInByte = byteArrayOutputStream.toByteArray();
        cv.put(COLUMN_FID,f_id);
        cv.put(COLUMN_IMAGE,imageInByte);
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

