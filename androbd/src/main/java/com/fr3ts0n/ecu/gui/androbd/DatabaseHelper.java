package com.fr3ts0n.ecu.gui.androbd;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.fr3ts0n.ecu.Conversion;
import com.fr3ts0n.ecu.EcuDataItem;
import com.fr3ts0n.ecu.EcuDataPv;

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


//    public long insertEcuDataPv(Context context,String field, String value) {
//        SQLiteDatabase db = getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_PID, field);
//        Log.d("BackgroundTask","huaa");
//        values.put(COLUMN_TIMESTAMP, String.valueOf(dataPv.get(EcuDataPv.FID_TIME))); // Consider data type
//        values.put(COLUMN_PID, (String) dataPv.get(EcuDataPv.FID_MNEMONIC));
//
//        // Format value based on conversion rules (similar to previous implementation)
//        Object colVal = dataPv.get(EcuDataPv.FID_VALUE);
//        Object cnvObj = dataPv.get(EcuDataPv.FID_CNVID);
//        if (cnvObj instanceof Conversion[]) {
//            Conversion cnv = ((Conversion[]) cnvObj)[EcuDataItem.cnvSystem];
//            values.put(COLUMN_VALUE, cnv.physToPhysFmtString((Number) colVal,
//                    (String) dataPv.get(EcuDataPv.FID_FORMAT)));
//        } else {
//            values.put(COLUMN_VALUE, String.valueOf(colVal));
//        }
//
//        values.put(COLUMN_UNITS, (String) dataPv.get(EcuDataPv.FID_UNITS));
//        Log.d("database check",field+" "+value);
////        // Insert the data into the table
//        long newRowId = db.insert(TABLE_OBD_ITEMS, null, values);
//
//        if (newRowId == -1) {
//            Toast.makeText(context, "Failed to insert data", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(context, "Data inserted successfully", Toast.LENGTH_SHORT).show();
//        }
//
//        db.close();
//        return newRowId;
//    }
}
