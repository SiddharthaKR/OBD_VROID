package com.fr3ts0n.ecu.gui.androbd;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.fr3ts0n.ecu.Conversion;
import com.fr3ts0n.ecu.EcuDataItem;
import com.fr3ts0n.ecu.EcuDataPv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "obd_items.db";
    private static final int DATABASE_VERSION = 1;
    private String currentTripId; // To store the current trip ID

    // Table names and column names
    private static final String TABLE_OBD_ITEMS = "obd_items";
    private static final String COLUMN_ID = "_id"; // Use AUTOINCREMENT for primary key
    private static final String COLUMN_TIMESTAMP = "timestamp";
    private static final String COLUMN_PID = "pid";
    private static final String COLUMN_VALUE = "value";
    private static final String COLUMN_UNITS = "units";


    // Create table queries for OBD items and trip items (if needed)
//    private static final String CREATE_TABLE_OBD_ITEMS_QUERY = "CREATE TABLE " + TABLE_OBD_ITEMS + " (" +
//            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//            COLUMN_TIMESTAMP + " TEXT, " + // Consider using a more suitable data type
//            COLUMN_PID + " TEXT, " +
//            COLUMN_VALUE + " TEXT, " +
//            COLUMN_UNITS + " TEXT)";
    private String createBaseObdItemsTable() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE obd_items (");
        sb.append("_id INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sb.append("timestamp TEXT, ");
        sb.append("pid TEXT, ");
        sb.append("value TEXT, ");
        sb.append("units TEXT)");
        return sb.toString();
    }

    private static DatabaseHelper instance;

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(createBaseObdItemsTable());

        // Create table for trip items if needed
        // db.execSQL(CREATE_TABLE_TRIP_ITEMS_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OBD_ITEMS);
        onCreate(db);
    }
    private void createTableForTrip(SQLiteDatabase db, String tripId) {
        String tableName = "trip_" + tripId;
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ");
        sb.append(tableName);
        sb.append(" (");
        sb.append("_id INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sb.append("timestamp TEXT, ");
        sb.append("pid TEXT, ");
        sb.append("value TEXT, ");
        sb.append("units TEXT)");
        db.execSQL(sb.toString());
        Log.d("BackgroundTask", "{}}{}}{}}{} TABLE CREATED ONCE");
    }
    public void insertOBDData(String time,String pid, String value,String unit,String tripID){
        long newRowId;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // Check if current trip has been initiated
        if (currentTripId == null) {
            currentTripId = tripID; // Use timestamp as initial trip ID
            createTableForTrip(db, currentTripId);
        }
        // Check if trip-specific table exists
        String tableName = "trip_" + currentTripId;
        values.put("timestamp", time);
        values.put("pid", pid);
        values.put("value", value);
        values.put("units", unit);
        newRowId = db.insert(tableName, null, values);
        Log.d("BackgroundTask", "Data passed "+pid+":"+value+unit);
        if (newRowId == -1) {
            Log.d("BackgroundTask", "Data inserted Failed:"+pid+":"+value+unit);
//            Toast.makeText(context, "Failed to insert data", Toast.LENGTH_SHORT).show();
        } else {
            Log.d("BackgroundTask", "Data inserted Success "+pid+":"+value+unit);
//            Toast.makeText(context, "Data inserted successfully", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    public List<String> getTableNames() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'android_metadata' AND name NOT LIKE 'sqlite_sequence'", null);
        List<String> tableNames = new ArrayList<>();
        while (cursor.moveToNext()) {
            tableNames.add(cursor.getString(0));
        }
        cursor.close();
        Log.d("kappa", "getTableNames: "+tableNames);
        return tableNames;
    }

    public void exportTableToCSV(Context context, String tableName) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + tableName, null);

            int columnCount = cursor.getColumnCount();
            int rowCount = cursor.getCount();

            if (rowCount == 0) {
                Toast.makeText(context, "No data available to export", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create directory if not exists
            File directory = new File(Environment.getExternalStorageDirectory() + "/your_directory");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Create Excel file
            String excelFilePath = directory.getPath() + "/" + tableName + ".xls";
            File excelFile = new File(excelFilePath);
            FileOutputStream fos = new FileOutputStream(excelFile);
            StringBuilder stringBuilder = new StringBuilder();

            // Write column names to Excel file
            for (int i = 0; i < columnCount; i++) {
                stringBuilder.append(cursor.getColumnName(i)).append("\t");
            }
            stringBuilder.append("\n");

            // Write data to Excel file
            while (cursor.moveToNext()) {
                for (int i = 0; i < columnCount; i++) {
                    stringBuilder.append(cursor.getString(i)).append("\t");
                }
                stringBuilder.append("\n");
            }

            fos.write(stringBuilder.toString().getBytes());
            fos.close();
            Toast.makeText(context, "Data exported to " + excelFilePath, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("DatabaseHelper", "Error exporting data to Excel: " + e.getMessage());
            Toast.makeText(context, "Error exporting data to Excel", Toast.LENGTH_SHORT).show();
        }

    }
}
