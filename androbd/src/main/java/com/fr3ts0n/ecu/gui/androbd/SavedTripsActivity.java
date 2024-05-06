package com.fr3ts0n.ecu.gui.androbd;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class SavedTripsActivity extends Activity {
    private ListView listView;
    private DatabaseHelper dbHelper;
    private List<String> tableNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_trips_list); // Replace with your layout containing the list view

        // Get reference to list view
        listView = findViewById(R.id.saved_trips); // Replace with your list view id

        // Get instance of DatabaseHelper
        dbHelper = DatabaseHelper.getInstance(this);

        // Get list of table names
        tableNames = dbHelper.getTableNames();

        // Create a list adapter
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tableNames);
        // Create a custom list adapter
        CustomListAdapter adapter = new CustomListAdapter(this, tableNames);
        // Set the adapter on the list view
        listView.setAdapter(adapter);
    }
    // Custom list adapter class
    private class CustomListAdapter extends BaseAdapter {

        private Context context;
        private List<String> tableNames;

        public CustomListAdapter(Context context, List<String> tableNames) {
            this.context = context;
            this.tableNames = tableNames;
        }

        @Override
        public int getCount() {
            return tableNames.size();
        }

        @Override
        public Object getItem(int position) {
            return tableNames.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.saved_trip_item, parent, false);
            }
            // Get references to text view and button
            TextView tableNameText = convertView.findViewById(R.id.table_name);
            Button exportButton = convertView.findViewById(R.id.export_button);

            // Set table name
            tableNameText.setText(tableNames.get(position));

            // Set click listener for export button
            exportButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tableName = tableNames.get(position);
                    exportTableToCSV(tableName); // Call method to export data
                }
            });

            return convertView;
        }
    }

    // Method to export table data to CSV (implementation needed)
    private void exportTableToCSV(String tableName) {
        // Implement logic to export data from the specified table to a CSV file
        // You'll need database helper methods to retrieve data and write it to a CSV file.
        // Consider using libraries like "opencsv" for easier CSV generation.

        // Example (incomplete):
        // List<String[]> data = dbHelper.getTableData(tableName);
        // // ... write data to CSV file
    }
}

