package com.example.smartpantrymanager;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.database.Cursor;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Intent;
import android.widget.Button;

import java.util.ArrayList;

public class MyPantryActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private ArrayList<String> pantryItems;
    private ArrayList<Integer> pantryItemIds;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_pantry);
        ListView pantryListView = findViewById(R.id.pantryListView);
        Button backToHomeButton = findViewById(R.id.backToHomeButton);
        backToHomeButton.setOnClickListener(v -> {
            finish();
        });

        databaseHelper = new DatabaseHelper(this);

        pantryItems = new ArrayList<>();
        pantryItemIds = new ArrayList<>();


        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                pantryItems
        );

        pantryListView.setAdapter(adapter);
        pantryListView.setOnItemClickListener((parent, view, position, id) -> {

            int selectedItemId = pantryItemIds.get(position);

            Intent intent = new Intent(
                    MyPantryActivity.this,
                    EditItemActivity.class
            );

            intent.putExtra("itemId", selectedItemId);
            startActivity(intent);

        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadPantryItems();
    }
    private void loadPantryItems() {

        pantryItems.clear();
        pantryItemIds.clear();

        Cursor cursor = databaseHelper.getAllPantryItems();

        if (cursor.getCount() == 0) {

            Toast.makeText(
                    MyPantryActivity.this,
                    "Your pantry is empty",
                    Toast.LENGTH_SHORT
            ).show();

        } else {

            while (cursor.moveToNext()) {

                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID)
                );

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NAME)
                );

                int quantity = cursor.getInt(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COL_QUANTITY)
                );

                String unit = cursor.getString(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COL_UNIT)
                );

                String category = cursor.getString(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CATEGORY)
                );

                String expiryDate = cursor.getString(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EXPIRY)
                );

                String itemDetails =
                        name +
                                "\nQuantity: " + quantity + " " + unit +
                                "\nCategory: " + category +
                                "\nExpiry Date: " + expiryDate;

                pantryItems.add(itemDetails);
                pantryItemIds.add(id);
            }
        }

        cursor.close();
        adapter.notifyDataSetChanged();
    }
}