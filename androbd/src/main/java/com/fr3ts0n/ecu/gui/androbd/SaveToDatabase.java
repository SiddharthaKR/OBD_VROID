package com.fr3ts0n.ecu.gui.androbd;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.fr3ts0n.ecu.EcuDataPv;

public class SaveToDatabase extends AsyncTask<Void, Void, Void> {
    private Context context;
    private DatabaseHelper dbHelper;
    private EcuDataPv data;
    private final Activity activity;

    public SaveToDatabase(Context context, DatabaseHelper dbHelper, EcuDataPv data) {
        this.context = context;
        this.dbHelper = dbHelper;
        this.data = data;
        this.activity = (Activity) context; // Assuming context is the Activity
    }

    @Override
    protected Void doInBackground(Void... params) {
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(1000); // Sleep for 1 second
                Log.d("BackgroundTask", "Iteration: " + (i + 1));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        dbHelper.insertEcuDataPv(context,data);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        // Handle success or error (optional)
        Toast.makeText(activity, "Data Inserted", Toast.LENGTH_SHORT).show();
    }
}
