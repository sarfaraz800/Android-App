package com.example.medicinealarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

public class Database_file extends SQLiteOpenHelper {
    private static final String DB_Name="Medicine Database";
    private static final int DB_Version=1;
    private static final String table_Name="Medicine_table";
    private final Context context;

    public Database_file(Context context) {
        super(context, DB_Name, null , DB_Version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS " + table_Name + " (name VARCHAR2(20) PRIMARY KEY, date DATE,expiry DATE, time VARCHAR2(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+table_Name);
        onCreate(db);
    }

    public boolean insertData(String name, String date, String expiry, String time) {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            db.beginTransaction();

            // Check if the name already exists in the table
            Cursor cursor = db.query(table_Name, null, "name=?", new String[]{name}, null, null, null);
            if (cursor.getCount() > 0) {
                showToast("Name already exists");
                return false; // Exit the method without inserting the data
            }
            cursor.close();

            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("date", date);
            values.put("expiry",expiry);
            values.put("time", time);
            db.insert(table_Name, null, values);

            db.setTransactionSuccessful();
            showToast("Data inserted successfully");
        } catch (SQLiteException e) {
            e.printStackTrace();
            showToast(e.toString());
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return true;
    }
    public ArrayList<MedicineModel> fetchMedicine() {
        ArrayList<MedicineModel> arrMedicine = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM " + table_Name, null);

            while (cursor.moveToNext()) {
                MedicineModel model = new MedicineModel();
                model.name = cursor.getString(0);
                model.date = cursor.getString(1);
                model.expiry = cursor.getString(2);
                model.time = cursor.getString(3);

                arrMedicine.add(model);
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
            showToast(e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return arrMedicine;
    }
    public boolean deleteMedicine(String name) {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            db.beginTransaction();

            // Delete all rows with the given medicine name
            int deletedRows = db.delete(table_Name, "name=?", new String[]{name});
            if (deletedRows > 0) {
                db.setTransactionSuccessful();
                showToast("Medicine deleted successfully");
                return true;
            } else {
                showToast("Medicine not found");
                return false;
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
            showToast(e.toString());
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }

    public void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}